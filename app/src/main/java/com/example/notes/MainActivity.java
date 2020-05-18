package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public EditText editText, editText1;
    public String filename = null;
    private String path = Environment.getExternalStorageDirectory().toString() + "/files/";
    private DatabaseHelper mDbHelper;
    SQLiteDatabase db;
     Cursor cursor;
    private long noteId = 0;

    public static final class NewNote implements BaseColumns {
        public final static String TABLE_NAME = "note";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_body = "body";
        public final static String COLUMN_DATE = "date";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText)findViewById(R.id.editText);
        editText1 = (EditText)findViewById(R.id.editTextBody);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            noteId = extras.getLong("id");
        }
        mDbHelper = new DatabaseHelper(this);
        db = mDbHelper.getWritableDatabase();
        // если 0, то добавление
        if (noteId > 0) {
            // получаем элемент по id из бд
            cursor = db.rawQuery("select * from note where " +
                    NewNote._ID + "=?", new String[]{String.valueOf(noteId)});
            cursor.moveToFirst();
            editText.setText(cursor.getString(1));
            editText1.setText(cursor.getString(2));
            cursor.close();
        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_clear:
                db.delete(NewNote.TABLE_NAME, "_id = ?", new String[]{String.valueOf(noteId)});
                db.close();
                Intent intent1 = new Intent(this, ListActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent1);
                return true;
            case R.id.action_open:

                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_save:

                String name = editText.getText().toString().trim();
                String body = editText1.getText().toString().trim();
                String date = new SimpleDateFormat("yyyyMMdd").format(new Date());

                ContentValues cv = new ContentValues();
                cv.put(NewNote.COLUMN_NAME, name);
                cv.put(NewNote.COLUMN_body, body);
                cv.put(NewNote.COLUMN_DATE, date);

                if (noteId > 0) {
                    db.update("note", cv, NewNote._ID + "=" + String.valueOf(noteId), null);
                } else {
                    db.insert("note", null, cv);
                }
                Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();


                return true;
            case R.id.action_settings:
                Intent i = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        float fSize = Float.parseFloat(sharedPreferences.getString("Размер", "20"));
        editText1.setTextSize(fSize);

        String regular = sharedPreferences.getString("Стиль", "");
        int typeface = Typeface.NORMAL;
        if(regular.contains("Полужирный"))
            typeface += Typeface.BOLD;

        if(regular.contains("Курсив"))
            typeface += Typeface.ITALIC;

        editText1.setTypeface(null, typeface);

        int color = Color.BLACK;

        if(sharedPreferences.getBoolean(getString(R.string.pref_color_black), false))
            color += Color.BLACK;
        if(sharedPreferences.getBoolean(getString(R.string.pref_color_green), false))
            color += Color.GREEN;
        if(sharedPreferences.getBoolean(getString(R.string.pref_color_red), false))
            color += Color.RED;
        if(sharedPreferences.getBoolean(getString(R.string.pref_color_yellow), false))
            color += Color.YELLOW;

        editText1.setTextColor(color);
    }

}
