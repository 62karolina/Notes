package com.example.notes;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = DatabaseHelper.class.getSimpleName();


    private static final String DATABASE_NAME = "notepad.db";

    private static final int DATABASE_VERSION = 1;

    /**
     * Конструктор {@link DatabaseHelper}.
     *
     * @param context Контекст приложения
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    /**
     * Вызывается при создании базы данных
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String SQL_CREATE_NOTE_TABLE = "CREATE TABLE note ("
                + MainActivity.NewNote._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MainActivity.NewNote.COLUMN_NAME + " TEXT NOT NULL, "
                + MainActivity.NewNote.COLUMN_body + " TEXT, "
                + MainActivity.NewNote.COLUMN_DATE + " TEXT);";

        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_NOTE_TABLE);
    }

    /**
     * Вызывается при обновлении схемы базы данных
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        // Создаём новую таблицу
        onCreate(db);
    }
}
