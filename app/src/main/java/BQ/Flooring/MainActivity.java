package BQ.Flooring;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import BQ.Flooring.ui.todo.TodoDatabase;
import BQ.Flooring.ui.todo.TodoFragment;

public class MainActivity extends AppCompatActivity {

    public TodoDatabase MainActivityTodoDatabase;
    public TodoFragment todoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskText = parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskText.getText());
        MainActivityTodoDatabase.deleteTask(task);
        todoFragment.updateUI();

    }


}