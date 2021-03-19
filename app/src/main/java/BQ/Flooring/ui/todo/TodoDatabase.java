package BQ.Flooring.ui.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * To do item database
 */
public class TodoDatabase extends SQLiteOpenHelper {

    private static final String TAG = "TODO-DatabaseHelper";

    public TodoDatabase(@Nullable Context context) {
        super(context, "ToDo Database", null, 1);
    }

    //Table Names
    public static final String TODO_TABLE = "todoTable";

    //To-Do column names
    public static final String UID = "UID";
    public static final String TASK = "Task";
    public static final String AISLE = "Aisle";


    //Table Creation
    //To do List
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + " (" + UID +
            " INTEGER PRIMARY KEY AUTOINCREMENT ," + TASK + " TEXT," + AISLE + " TEXT" + ")";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);

    }

    /**
     * Insert new task
     * @param task - the task information
     * @param aisle - the location information
     */
    public void insertTask(String task, String aisle){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        cv.put(AISLE, aisle);
        db.insert(TODO_TABLE,null,cv);
        db.close();

    }

    /**
     * Deletes a task from the database
     * @param task - task to be removed
     */
    public void deleteTask(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TODO_TABLE,TASK + " = " + "'" + task + "'",null);
        db.close();
    }


}
