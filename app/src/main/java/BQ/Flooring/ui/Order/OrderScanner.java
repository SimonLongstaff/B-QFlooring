package BQ.Flooring.ui.Order;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import BQ.Flooring.MainActivity;
import BQ.Flooring.R;

public class OrderScanner extends Fragment {

    private Button scanner;
    public OrderDatabase orderDatabase;
    ListView orders;
    OrderAdapter orderAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity main = (MainActivity) getActivity();
        orderDatabase = main.orderDatabase;
        main.orderScanner = this;

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_order, container, false);

        orders = root.findViewById(R.id.orderList);

        scanner = root.findViewById(R.id.scan);
        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_scan,new ScannerFragment());
                fragmentTransaction.commit();

            }
        });

        updateUI();

        return root;
    }

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
}