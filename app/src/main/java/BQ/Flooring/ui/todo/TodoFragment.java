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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import BQ.Flooring.MainActivity;
import BQ.Flooring.R;

/**
 * To do list fragment
 */
public class TodoFragment extends Fragment {

    private static final String TAG = "TODOFrag";
    private ListView taskListView;
    private TodoDatabase todoDatabase;
    ToDoAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoDatabase = new TodoDatabase(getContext());
        getAndSetReferences();
    }

    /**
     * Updates the reference to this class in main
     * TODO: Find a better way to do this/Remove direct references back and forth
     */
    private void getAndSetReferences() {
        MainActivity main = (MainActivity) getParentFragment().getActivity();
        assert main != null;
        main.MainActivityTodoDatabase = todoDatabase;
        main.todoFragment = this;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_todo, container, false);
        bindUIElements(root);
        addTaskListener(root);
        updateUI();
        return root;
    }

    /**
     * Set up the on click listener for the add task button
     * @param root - the current view
     */
    private void addTaskListener(View root) {
        final FloatingActionButton addTask = root.findViewById(R.id.addTodo);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout taskLayout = new LinearLayout(getContext());
                taskLayout.setOrientation(LinearLayout.VERTICAL);
                final EditText taskEditText = getEditText(taskLayout, "Task?");
                final EditText taskAisleEditText = getEditText(taskLayout, "Where?");

                addTaskAlertDialog(taskLayout, taskEditText, taskAisleEditText);
            }
        });
    }

    /**
     * Builds an alert dialog with two separate text entry fields
     * @param taskLayout - the linear layout that contains the two EditText fields
     * @param taskEditText - the editText field that will contain the task
     * @param taskAisleEditText - the editText field that will contain the location
     */
    private void addTaskAlertDialog(LinearLayout taskLayout, EditText taskEditText, EditText taskAisleEditText) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Add a new task")
                .setView(taskLayout)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        String aisle = String.valueOf((taskAisleEditText.getText()));
                        todoDatabase.insertTask(task, aisle);
                        updateUI();
                        Log.d(TAG, "Task to add: " + task + " in Aisle" + aisle);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();
    }

    /**
     * Adds an editText field to a linear layout
     * @param taskLayout - the linear layout
     * @param hint - the hint for the EditText
     * @return - Linear layout with the field added
     */
    @NotNull
    private EditText getEditText(LinearLayout taskLayout, String hint) {
        final EditText taskEditText = new EditText(getContext());
        taskEditText.setHint(hint);
        taskLayout.addView(taskEditText);
        return taskEditText;
    }

    /**
     * Binds the UI elements
     * @param root - the current view
     */
    private void bindUIElements(View root) {
        taskListView = root.findViewById(R.id.list_todo);
    }


    /**
     * Updates the list view
     */
    public void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        ArrayList<String> taskListAisle = new ArrayList<>();
        SQLiteDatabase db = todoDatabase.getReadableDatabase();
        Cursor cursor = db.query(TodoDatabase.TODO_TABLE, new String[]{TodoDatabase.UID, TodoDatabase.TASK, TodoDatabase.AISLE},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(TodoDatabase.TASK);
            int locIndex = cursor.getColumnIndex((TodoDatabase.AISLE));
            taskList.add(cursor.getString(index));
            taskListAisle.add((cursor.getString(locIndex)));
        }

        adapter = new ToDoAdapter(getActivity(), taskList, taskListAisle);
        taskListView.setAdapter(adapter);


        cursor.close();
        db.close();

    }

}