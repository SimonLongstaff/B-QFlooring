package BQ.Flooring.ui.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import BQ.Flooring.MainActivity;
import BQ.Flooring.R;

public class TodoFragment extends Fragment {

    private static final String TAG = "TODOFrag";
    private ListView taskListView;
    private  TodoDatabase todoDatabase;
    private ArrayAdapter<String> mAdapter;
    ToDoAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoDatabase = new TodoDatabase(getContext());
        MainActivity main = (MainActivity) getParentFragment().getActivity();
        main.MainActivityTodoDatabase = todoDatabase;
        main.todoFragment = this;


    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        taskListView = root.findViewById(R.id.list_todo);

        final FloatingActionButton addTask = root.findViewById(R.id.addTodo);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout taskLayout = new LinearLayout(getContext());
                taskLayout.setOrientation(LinearLayout.VERTICAL);

                final EditText taskEditText = new EditText( getContext() );
                taskEditText.setHint("Task?");
                taskLayout.addView(taskEditText);

                final EditText taskAisleEditText = new EditText( getContext() );
                taskAisleEditText.setHint("Where?");
                taskLayout.addView(taskAisleEditText);




                AlertDialog dialog = new AlertDialog.Builder( getContext() )
                        .setTitle("Add a new task")
                        .setView(taskLayout)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                String aisle = String.valueOf((taskAisleEditText.getText()));
                                todoDatabase.insertTask(task,aisle);
                                updateUI();
                                Log.d(TAG, "Task to add: " + task + " in Aisle" + aisle);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();

                dialog.show();
            }
        });

        updateUI();
        return root;
    }



    public void updateUI(){
        ArrayList<String> taskList = new ArrayList<>();
        ArrayList<String>taskListAisle = new ArrayList<>();
        SQLiteDatabase db = todoDatabase.getReadableDatabase();
        Cursor cursor = db.query(TodoDatabase.TODO_TABLE, new String[]{TodoDatabase.UID,TodoDatabase.TASK, TodoDatabase.AISLE},
                null,null,null,null,null);

        while (cursor.moveToNext()){
            int index = cursor.getColumnIndex(TodoDatabase.TASK);
            int locIndex = cursor.getColumnIndex((TodoDatabase.AISLE));
            taskList.add(cursor.getString(index));
            taskListAisle.add((cursor.getString(locIndex)));
        }

        adapter = new ToDoAdapter(getActivity(),taskList,taskListAisle);
        taskListView.setAdapter(adapter);




        cursor.close();
        db.close();

    }

}