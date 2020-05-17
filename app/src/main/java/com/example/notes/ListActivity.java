package com.example.notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ListActivity extends AppCompatActivity {

    private ListView noteList;
    private Cursor userCursor;
    private SimpleCursorAdapter userAdapter;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list1);

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
    }

    @Override
    protected void onResume() {
        super.onResume();

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
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_list1);
//
//        mDbHelper = new DatabaseHelper(this);
//
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        displayDatabaseInfo();
//    }
//
//    private void displayDatabaseInfo() {
//        // Создадим и откроем для чтения базу данных
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        // Зададим условие для выборки - список столбцов
//        String[] projection = {
//                MainActivity.NewNote._ID,
//                MainActivity.NewNote.COLUMN_NAME,
//                MainActivity.NewNote.COLUMN_body,
//                MainActivity.NewNote.COLUMN_DATE
//        };
//
//        // Делаем запрос
//        Cursor cursor = db.query(
//                MainActivity.NewNote.TABLE_NAME,   // таблица
//                projection,            // столбцы
//                null,                  // столбцы для условия WHERE
//                null,                  // значения для условия WHERE
//                null,                  // Don't group the rows
//                null,                  // Don't filter by row groups
//                null);                   // порядок сортировки
//
//        TextView displayTextView = (TextView) findViewById(R.id.list);
//
//        try {
//            displayTextView.setText("Таблица содержит " + cursor.getCount() + " записаей.\n\n");
//            displayTextView.append(MainActivity.NewNote._ID + " - " +
//                    MainActivity.NewNote.COLUMN_NAME + " - " +
//                    MainActivity.NewNote.COLUMN_body + " - " +
//                    MainActivity.NewNote.COLUMN_DATE + "\n");
//
//            // Узнаем индекс каждого столбца
//            int idColumnIndex = cursor.getColumnIndex(MainActivity.NewNote._ID);
//            int nameColumnIndex = cursor.getColumnIndex(MainActivity.NewNote.COLUMN_NAME);
//            int bodyColumnIndex = cursor.getColumnIndex(MainActivity.NewNote.COLUMN_body);
//            int dateColumnIndex = cursor.getColumnIndex(MainActivity.NewNote.COLUMN_DATE);
//
//            // Проходим через все ряды
//            while (cursor.moveToNext()) {
//                // Используем индекс для получения строки или числа
//                int currentID = cursor.getInt(idColumnIndex);
//                String currentName = cursor.getString(nameColumnIndex);
//                String currentBody = cursor.getString(bodyColumnIndex);
//                int currentDate = cursor.getInt(dateColumnIndex);
//                // Выводим значения каждого столбца
//                displayTextView.append(("\n" + currentID + " - " +
//                        currentName + " - " +
//                        currentBody + " - " +
//                        currentDate));
//            }
//        } finally {
//            // Всегда закрываем курсор после чтения
//            cursor.close();
//        }
//    }


}
