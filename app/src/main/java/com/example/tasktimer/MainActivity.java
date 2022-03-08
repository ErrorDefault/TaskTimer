package com.example.tasktimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static String PACKAGE_NAME;
    private static final String TAG = "MainActivity";
    private ArrayList<TaskDataModel> items;
    private CustomAdapter adapter;
    private ListView lvItems;
    private TimerDBHelper mHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getApplicationContext().getPackageName();
        createNotificationChannel();
        lvItems = (ListView) findViewById(R.id.list_view);
        mHelper = new TimerDBHelper(getApplicationContext());
        //Query Data
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String[] projection = {
                TimerDBContract.TimerEntry._ID,
                TimerDBContract.TimerEntry.COLUMN_NAME,
                TimerDBContract.TimerEntry.COLUMN_TIME
        };

        Cursor cursor = db.query(
                TimerDBContract.TimerEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        items = new ArrayList<TaskDataModel>();
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(TimerDBContract.TimerEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(TimerDBContract.TimerEntry.COLUMN_NAME));
            int time = cursor.getInt(cursor.getColumnIndexOrThrow(TimerDBContract.TimerEntry.COLUMN_TIME));
            items.add(new TaskDataModel(name, time, id));
        }
        cursor.close();
        db.close();
        adapter = new CustomAdapter(items, getApplicationContext());
        lvItems.setAdapter(adapter);
    }

    public void deleteItem(TaskDataModel entry){
        runOnUiThread(new Runnable() {
            public void run() {
                items.remove(entry);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            NotificationChannel channel = new NotificationChannel(PACKAGE_NAME, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
                Context context = getApplicationContext();
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText taskEditText = new EditText(this);
                taskEditText.setHint("Task Name");
                final EditText timeEditText = new EditText(this);
                timeEditText.setHint("Time (minutes)");
                timeEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                layout.addView(taskEditText);
                layout.addView(timeEditText);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new task")
                        .setView(layout)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                String time = String.valueOf((timeEditText.getText()));
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(TimerDBContract.TimerEntry.COLUMN_NAME, task);
                                values.put(TimerDBContract.TimerEntry.COLUMN_TIME, Integer.parseInt(time)*60);
                                db.insertWithOnConflict(TimerDBContract.TimerEntry.TABLE_NAME,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                db = mHelper.getReadableDatabase();
                                String[] projection = {TimerDBContract.TimerEntry._ID};
                                String sortOrder = "TimerEntry._ID ASC";
                                Cursor cursor = db.query(
                                        TimerDBContract.TimerEntry.TABLE_NAME,
                                        projection,
                                        null,
                                        null,
                                        null,
                                        null,
                                        sortOrder);
                                int id = -1;
                                while(cursor.moveToNext()){
                                    id = cursor.getInt(cursor.getColumnIndexOrThrow(TimerDBContract.TimerEntry._ID));
                                }
                                cursor.close();
                                db.close();
                                int finalId = id;
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        items.add(new TaskDataModel(task, Integer.parseInt(time)*60, finalId));
                                        adapter.notifyDataSetChanged();
                                    }
                                });
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