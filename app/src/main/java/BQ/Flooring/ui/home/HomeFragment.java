package BQ.Flooring.ui.home;

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

import BQ.Flooring.R;

public class HomeFragment extends Fragment {

    SwitchCompat feet;
    EditText height;
    EditText width;
    EditText M2;
    EditText size;
    EditText price;

    boolean usingFeet;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
         size = root.findViewById(R.id.SizePerPack);
         price = root.findViewById(R.id.PricePerPack);
         height = root.findViewById(R.id.Height);
         width = root.findViewById(R.id.Width);
         M2 = root.findViewById(R.id.M2);

         TextView packsCalculated = root.findViewById(R.id.pack_calculated);

         feet = root.findViewById(R.id.Feet);

        Button calculate = root.findViewById(R.id.Calculate);

        feet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                usingFeet = isChecked;
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int packsCal = calculate();
               packsCalculated.setText(String.format("%d Packs", packsCal));

            }
        });

        return root;
    }

    private int calculate() {


        double floorSpace = 0;
        double packsize = Double.parseDouble(size.getText().toString());

        if (usingFeet) {

            if (!height.getText().toString().equals("") && !width.getText().toString().equals("")) {
                double h = Double.parseDouble(height.getText().toString());
                double w = Double.parseDouble(width.getText().toString());
                floorSpace = Calculations.FeetToMeters(Calculations.MetersSquared(h, w));
            }

            if (!M2.getText().toString().equals("")) {
                floorSpace = Calculations.FeetToMeters(Double.parseDouble(M2.getText().toString()));
            }

        } else {

            if (!height.getText().toString().equals("") && !width.getText().toString().equals("")) {
                double h = Double.parseDouble(height.getText().toString());
                double w = Double.parseDouble(width.getText().toString());
                floorSpace = Calculations.MetersSquared(h,w);

            }

            if (!M2.getText().toString().equals("")) {
                floorSpace =Double.parseDouble(M2.getText().toString());
            }


        }

        return Calculations.NumPacks(floorSpace,packsize);
    }
}