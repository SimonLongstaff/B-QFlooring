package BQ.Flooring.ui.todo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import BQ.Flooring.R;

/**
 * Adapter for To do list view
 */
public class ToDoAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> items;
    private final List<String> aisle;

    public ToDoAdapter (Activity context, List<String> items, List<String> aisle) {


        super(context, R.layout.item_todo, items);
        this.context = context;
        this.items = items;
        this.aisle = aisle;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_todo, null,true);

        TextView task = view.findViewById(R.id.task_title);
        TextView aislenum = view.findViewById(R.id.task_aisle);

        task.setText(items.get(position));
        aislenum.setText(aisle.get(position));

        return view;
    }
}
