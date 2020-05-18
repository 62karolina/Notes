package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ListActivity extends AppCompatActivity {

    private ListView noteList;
    private Cursor userCursor;
    private SimpleCursorAdapter userAdapter;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private EditText noteFilter;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list1);

        noteFilter = (EditText)findViewById(R.id.noteFilter);

        noteList = (ListView)findViewById(R.id.list);
        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        databaseHelper = new DatabaseHelper(getApplicationContext());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    try{
        // открываем подключение
        db = databaseHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора
        userCursor =  db.rawQuery("select * from note", null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[] {MainActivity.NewNote.COLUMN_NAME, MainActivity.NewNote.COLUMN_DATE};
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        noteList.setAdapter(userAdapter);

        // если в текстовом поле есть текст, выполняем фильтрацию
        // данная проверка нужна при переходе от одной ориентации экрана к другой
        if(!noteFilter.getText().toString().isEmpty())
            userAdapter.getFilter().filter(noteFilter.getText().toString());

        // установка слушателя изменения текста
        noteFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) { }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            // при изменении текста выполняем фильтрацию
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                userAdapter.getFilter().filter(s.toString());
            }
        });

        // устанавливаем провайдер фильтрации
        userAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {

                if (constraint == null || constraint.length() == 0) {

                    return db.rawQuery("select * from " + MainActivity.NewNote.TABLE_NAME, null);
                }
                else {
                    return db.rawQuery("select * from " + MainActivity.NewNote.TABLE_NAME + " where " +
                            MainActivity.NewNote.COLUMN_NAME + " like ?", new String[]{"%" + constraint.toString() + "%"});
                }
            }
        });

        noteList.setAdapter(userAdapter);
    }
        catch (SQLException e){}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }
}
