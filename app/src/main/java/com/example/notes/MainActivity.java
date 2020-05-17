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

    public EditText editText;
    public String filename = null;
    private String path = Environment.getExternalStorageDirectory().toString() + "/files/";
    private DatabaseHelper mDbHelper;
    private Cursor c = null;

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
                editText.setText("");
                return true;
            case R.id.action_open:

                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_save:
//                AlertDialog.Builder  alert = new AlertDialog.Builder(this);
//                alert.setTitle("Имя файла");
//                alert.setMessage("Введите имя файла для сохранения");
//                final EditText input2 = new EditText(this);
//                alert.setView(input2);
//                alert.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                            String value = input2.getText().toString();
//                            filename = value;
//                            saveFile(filename, editText.getText().toString());
//                    }
//                });
//                alert.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                alert.show();

                String todayAsString = new SimpleDateFormat("yyyyMMdd").format(new Date());
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase("notes.db", MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS note (_id integer primary key autoincrement, body TEXT, date TEXT)");
              //  Cursor query = db.rawQuery("SELECT * FROM note;", null);
                EditText body = (EditText)findViewById(R.id.editText);
                String text = body.getText().toString();
                db.execSQL("INSERT INTO note VALUES ('" + text + "'," + todayAsString + ");");
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
        editText.setTextSize(fSize);

        String regular = sharedPreferences.getString("Стиль", "");
        int typeface = Typeface.NORMAL;
        if(regular.contains("Полужирный"))
            typeface += Typeface.BOLD;

        if(regular.contains("Курсив"))
            typeface += Typeface.ITALIC;

        editText.setTypeface(null, typeface);

        int color = Color.BLACK;

        if(sharedPreferences.getBoolean(getString(R.string.pref_color_black), false))
            color += Color.BLACK;
        if(sharedPreferences.getBoolean(getString(R.string.pref_color_green), false))
            color += Color.GREEN;
        if(sharedPreferences.getBoolean(getString(R.string.pref_color_red), false))
            color += Color.RED;
        if(sharedPreferences.getBoolean(getString(R.string.pref_color_yellow), false))
            color += Color.YELLOW;

        editText.setTextColor(color);
    }

    private void saveFile(String filename, String body){
        try {
            File root = new File(this.path);
            if(!root.exists()){
                root.mkdirs();
            }
            File file = new File(root, filename);
            FileWriter writer = new FileWriter(file);
            writer.append(body);
            writer.flush();
            writer.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String openFile(String fileName){
        StringBuilder  text = new StringBuilder();
        try {
            File file = new File(this.path, fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) != null){
                text.append(line + "\n");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return text.toString();
    }
}
