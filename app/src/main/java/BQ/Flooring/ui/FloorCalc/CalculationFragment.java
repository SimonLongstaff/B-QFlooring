package BQ.Flooring.ui.FloorCalc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

import BQ.Flooring.R;

/**
 * Flooring calculation fragment
 * Default fragment on launch
 * @Author Simon Longstaff
 */
public class CalculationFragment extends Fragment {

    //Variables
    SwitchCompat feet;
    EditText height;
    EditText width;
    EditText M2;
    EditText size;
    EditText price;
    TextView additionalInfo;
    boolean usingFeet;

    /**
     * Creates the view and calls the binding methods
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_floor_cal, container, false);

        bindUIElements(root);
        isMeasurementInFeet();
        buttonBinding(root);

        return root;
    }

    /**
     * Binds all UI elements
     *
     * @param root - the current view
     */
    private void bindUIElements(View root) {
        size = root.findViewById(R.id.SizePerPack);
        price = root.findViewById(R.id.PricePerPack);
        height = root.findViewById(R.id.Height);
        width = root.findViewById(R.id.Width);
        M2 = root.findViewById(R.id.M2);
        additionalInfo = root.findViewById(R.id.other_stats);
        feet = root.findViewById(R.id.Feet);
    }

    /**
     * Binds the UI buttons & Text fields needed by it's listener
     *
     * @param root - the current view
     */
    private void buttonBinding(View root) {

        TextView packsCalculated = root.findViewById(R.id.pack_calculated);
        TextView totalPrice = root.findViewById(R.id.price);
        Button calculate = root.findViewById(R.id.Calculate);

        //Listener for calculation button press
        calculateButtonPress(packsCalculated, totalPrice, calculate);
    }

    /**
     * Sets up the listener for measurement in feet
     * On change switches the boolean value
     */
    private void isMeasurementInFeet() {
        feet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                usingFeet = isChecked;
            }
        });
    }

    /**
     * Sets up the listener for the calculate button press
     *
     * @param packsCalculated - Textview that will display the calculated number of packs
     * @param totalPrice      - Textview that will display the price if a price per pack is given (Optional)
     * @param calculate       - The button itself
     */
    private void calculateButtonPress(TextView packsCalculated, TextView totalPrice, Button calculate) {
        calculate.setOnClickListener(v -> {
            int packsCal = calculate();
            packsCalculated.setText(String.format("%d Packs", packsCal));
            if (!price.getText().toString().equals("")) {
                double pricePack = parseEdittext(price);
                String finPrice = "£" + pricePack * packsCal;
                totalPrice.setText(finPrice);
            }

        });
    }

    /**
     * Parses the measurements from the editText fields and sends it to the calculation method to return the # of packs needed
     * Passes the info the additional information string builders
     * #TODO: Make It warn the user that it preferentially chooses HxW measurements if both HxW & M2 are entered, possibly block it with a warning
     *
     * @return
     */
    private int calculate() {
        String additionalInformation = "";
        DecimalFormat format = new DecimalFormat("#0.00");

        double floorSpace = 0;
        double packsize = parseEdittext(size);

        if (usingFeet) {

            if (!height.getText().toString().equals("") && !width.getText().toString().equals("")) {
                double h = parseEdittext(height);
                double w = parseEdittext(width);
                floorSpace = Calculations.FeetToMeters(Calculations.MetersSquared(h, w));
                additionalInformation = additionInfoFeetHxW(additionalInformation, format, floorSpace, h, w);
            } else if (!M2.getText().toString().equals("")) {
                floorSpace = Calculations.FeetToMeters(Double.parseDouble(M2.getText().toString()));
                additionalInformation = additionInfoF2(additionalInformation, floorSpace);
            }

        } else {

            if (!height.getText().toString().equals("") && !width.getText().toString().equals("")) {
                double h = parseEdittext(height);
                double w = parseEdittext(width);
                floorSpace = Calculations.MetersSquared(h, w);
                additionalInformation = additionalInfoMetersHxW(additionalInformation, floorSpace, h, w);
            } else if (!M2.getText().toString().equals("")) {
                floorSpace = Double.parseDouble(M2.getText().toString());
            }

        }

        additionalInfo.setText(additionalInformation);
        return Calculations.NumPacks(floorSpace, packsize);
    }

    /**
     * Parses the value of an editText field as a double
     *
     * @param editText - the field being parsed
     * @return - a double value of the text
     */
    private double parseEdittext(EditText editText) {
        return Double.parseDouble(editText.getText().toString());
    }

    /**
     * Additional information string builder for when measurements given are height & width in meters
     *
     * @param additionalInformation
     * @param floorSpace
     * @param h
     * @param w
     * @return
     */
    @NotNull
    private String additionalInfoMetersHxW(String additionalInformation, double floorSpace, double h, double w) {
        additionalInformation = additionalInformation + h + " x " + w + " Meters \n";
        additionalInformation = additionalInformation + floorSpace + " Meters Squared \n ";
        return additionalInformation;
    }

    /**
     * Additional information string builder for when measurements given are feet squared
     *
     * @param additionalInformation
     * @param floorSpace
     * @return
     */
    @NotNull
    private String additionInfoF2(String additionalInformation, double floorSpace) {
        additionalInformation = additionalInformation + Double.parseDouble(M2.getText().toString()) + " feet squared \n";
        additionalInformation = additionalInformation + floorSpace + " Meters Squared \n";
        return additionalInformation;
    }

    /**
     * Additional information string builder for when measurements given are height & width in feet
     *
     * @param additionalInformation
     * @param format
     * @param floorSpace
     * @param h
     * @param w
     * @return
     */
    @NotNull
    private String additionInfoFeetHxW(String additionalInformation, DecimalFormat format, double floorSpace, double h, double w) {
        additionalInformation = additionalInformation + h + " x " + w + " Feet \n";
        additionalInformation = additionalInformation + format.format(Calculations.FeetToMeters(h)) + " x " + format.format(Calculations.FeetToMeters(w)) + " Meters \n";
        additionalInformation = additionalInformation + h * w + " Feet Squared \n";
        additionalInformation = additionalInformation + floorSpace + " Meters Squared \n ";
        return additionalInformation;
    }
}