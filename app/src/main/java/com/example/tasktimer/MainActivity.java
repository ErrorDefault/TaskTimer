package com.example.tasktimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.text.InputType;
import android.text.method.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ArrayList<TaskDataModel> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private TimerDBHelper mHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.list_view);
        items = new ArrayList<TaskDataModel>();

        /*
        items.add(new TaskDataModel("Washing Machine", 3600, "WASH!"));
        items.add(new TaskDataModel("Dryer", 2100, "DRY!"));
        */

        TimerDBHelper mHelper = new TimerDBHelper(getApplicationContext());

        CustomAdapter adapter = new CustomAdapter(items, getApplicationContext());
        lvItems.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_task_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
//                Log.d(TAG, "Add a new task");
                LinearLayout layout = new LinearLayout(getApplicationContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText nameBox = new EditText(getApplicationContext());
                nameBox.setHint("Name");
                layout.addView(nameBox);

                final EditText timeBox = new EditText(getApplicationContext());
                timeBox.setHint("Time (Minutes)");
                timeBox.setInputType(InputType.TYPE_CLASS_NUMBER);

                layout.addView(timeBox);

//                dialog.setView(layout);

                final EditText taskEditText = new EditText(this);

                AlertDialog dialog = new AlertDialog.Builder(this)

                        .setView(layout)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(TimerDBContract.TimerEntry.COLUMN_NAME, task);

                                db.insertWithOnConflict(TimerDBContract.TimerEntry.TABLE_NAME,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}