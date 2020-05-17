package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ListActivity extends AppCompatActivity {

    ListView noteList;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    private DatabaseHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list1);

        mDbHelper = new DatabaseHelper(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Зададим условие для выборки - список столбцов
        String[] projection = {
                MainActivity.NewNote._ID,
                MainActivity.NewNote.COLUMN_NAME,
                MainActivity.NewNote.COLUMN_body,
                MainActivity.NewNote.COLUMN_DATE
        };

        // Делаем запрос
        Cursor cursor = db.query(
                MainActivity.NewNote.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        TextView displayTextView = (TextView) findViewById(R.id.list);

        try {
            displayTextView.setText("Таблица содержит " + cursor.getCount() + " записаей.\n\n");
            displayTextView.append(MainActivity.NewNote._ID + " - " +
                    MainActivity.NewNote.COLUMN_NAME + " - " +
                    MainActivity.NewNote.COLUMN_body + " - " +
                    MainActivity.NewNote.COLUMN_DATE + "\n");

            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(MainActivity.NewNote._ID);
            int nameColumnIndex = cursor.getColumnIndex(MainActivity.NewNote.COLUMN_NAME);
            int bodyColumnIndex = cursor.getColumnIndex(MainActivity.NewNote.COLUMN_body);
            int dateColumnIndex = cursor.getColumnIndex(MainActivity.NewNote.COLUMN_DATE);

            // Проходим через все ряды
            while (cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentBody = cursor.getString(bodyColumnIndex);
                int currentDate = cursor.getInt(dateColumnIndex);
                // Выводим значения каждого столбца
                displayTextView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentBody + " - " +
                        currentDate));
            }
        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
        }
    }
}
