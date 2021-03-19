package BQ.Flooring.ui.Order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import BQ.Flooring.MainActivity;
import BQ.Flooring.R;

/**
 * Fragment that contains the list view of the current order items
 * As well a button to activate barcode scanning
 */
public class OrderScanner extends Fragment {

    private Button scanner;
    public OrderDatabase orderDatabase;
    ListView orders;
    OrderAdapter orderAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAndSet();

    }

    /**
     * Gets the database from main
     * Updates the reference to this class in main
     * TODO: Find a better way to do this/Remove direct references back and forth
     */
    private void getAndSet() {
        MainActivity main = (MainActivity) getActivity();
        assert main != null;
        orderDatabase = main.orderDatabase;
        main.orderScanner = this;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order, container, false);
        bindUIElements(root);
        setScannerOnClickListener();
        updateUI();
        return root;
    }

    /**
     * Sets the listener for the scan button
     * Launches the scanner when clicked
     */
    private void setScannerOnClickListener() {
        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_scan,new ScannerFragment());
                fragmentTransaction.commit();

            }
        });
    }

    /**
     * Binds the UI elements
     * @param root - current view
     */
    private void bindUIElements(View root) {
        orders = root.findViewById(R.id.orderList);
        scanner = root.findViewById(R.id.scan);
    }

    /**
     * Updates the values in the list view
     */
    public void updateUI(){
        ArrayList<String> barcodes = new ArrayList<>();
        ArrayList<String> amounts = new ArrayList<>();
        SQLiteDatabase db = orderDatabase.getReadableDatabase();
        Cursor cursor = db.query(OrderDatabase.ORDER_TABLE, new String[]{OrderDatabase.UID,OrderDatabase.BARCODE,OrderDatabase.AMOUNT},
                null,null,null,null,null);

        while (cursor.moveToNext()){
            int bcIndex = cursor.getColumnIndex(OrderDatabase.BARCODE);
            int amtIndex = cursor.getColumnIndex(OrderDatabase.AMOUNT);
            barcodes.add(cursor.getString(bcIndex));
            amounts.add(cursor.getString(amtIndex));
        }

        orderAdapter = new OrderAdapter(getActivity(),barcodes,amounts);
        orders.setAdapter(orderAdapter);

        cursor.close();
        db.close();

    }

    /**
     * Launches an alert dialogue to update a value in the database
     * @param number - EditText for the alert dialogue
     * @param code - the barcode being updated
     */
    public void updateAmount(EditText number, String code) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Edit Amount")
                .setView(number)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        orderDatabase.updateAmount(code,number.getText().toString());
                        updateUI();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }
}