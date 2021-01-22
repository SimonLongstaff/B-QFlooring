package BQ.Flooring.ui.Order;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import BQ.Flooring.R;

public class OrderAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> code;
    private final List<String> num;

    public OrderAdapter(Activity context, List<String> code, List<String> num) {
        super(context, R.layout.item_order, code);
        this.context = context;
        this.code = code;
        this.num = num;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_order, null, true);

        TextView barcode = view.findViewById(R.id.barcode_title);
        TextView number = view.findViewById(R.id.barcode_amount);

        barcode.setText(code.get((position)));
        number.setText(num.get(position));

        return  view;

    }
}
