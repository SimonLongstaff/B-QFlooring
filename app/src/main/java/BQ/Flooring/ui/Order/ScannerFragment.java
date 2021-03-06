package BQ.Flooring.ui.Order;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

import BQ.Flooring.MainActivity;
import BQ.Flooring.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Scanner fragment that implements Zxing Scanner view libary
 */
public class ScannerFragment extends Fragment implements ZXingScannerView.ResultHandler {

    //Variables
    private ZXingScannerView scannerView;
    private TextView textResult;
    OrderDatabase orderDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDatabase();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.scanner, container, false);
        bindingUIElements(root);
        getPermissions();
        return root;
    }

    @Override
    public void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }

    /**
     * Gets the database from mainactivity
     */
    private void getDatabase() {
        MainActivity main = (MainActivity) getActivity();
        assert main != null;
        orderDatabase = main.orderDatabase;
    }

    /**
     * Gets permissions for use of camera for scanning
     */
    private void getPermissions() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(ScannerFragment.this);
                        scannerView.startCamera();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getContext(),"You must accept",Toast.LENGTH_LONG);

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }

    /**
     * Binds UI elements
     * @param root - current view
     */
    private void bindingUIElements(View root) {
        scannerView = (ZXingScannerView)root.findViewById(R.id.zx_scan);
        textResult = root.findViewById(R.id.text_result);
    }

    /**
     * Handles the results of the scanner
     * @param rawResult - raw result from barcode scanner libary
     */
    @Override
    public void handleResult(Result rawResult) {
        final EditText number = getEditText();
        ScannerAlertDialogue(rawResult, number);
        textResult.setText(rawResult.getText());
    }

    /**
     * Alert dialogue that handles collection of information about scanned object
     * Writes object data to database
     * @param rawResult
     * @param number
     */
    private void ScannerAlertDialogue(Result rawResult, EditText number) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("How Many?")
                .setView(number)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        orderDatabase.insertBarcode(rawResult.getText(), number.getText().toString());
                        FragmentTransaction fragmentTransaction = getActivity()
                                .getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_scan,new OrderScanner());
                        fragmentTransaction.commit();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentTransaction fragmentTransaction = getActivity()
                                .getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_scan,new OrderScanner());
                        fragmentTransaction.commit();
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * Generates an editText layout for the alert dialogue
     * @return
     */
    @NotNull
    private EditText getEditText() {
        final EditText number = new EditText(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        number.setLayoutParams(layoutParams);
        return number;
    }
}
