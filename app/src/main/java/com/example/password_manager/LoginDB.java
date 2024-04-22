package com.example.password_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class LoginDB {
    private final String DATABASE_NAME = "LoginDB";
    private final String DATABASE_TABLE = "Logins_Table";
    private final String KEY_ID = "_id";
    private final String KEY_NAME = "_name";
    private final String KEY_PASSWORD = "_password";
    private final String KEY_URL = "_url";

    private final int DATABASE_VERSION = 1;

    Context context;

    MyDatabaseHelper helper;
    SQLiteDatabase sqLiteDatabase;

    public LoginDB(Context c)
    {
        context = c;
    }

    public void open()
    {
        helper = new MyDatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = helper.getWritableDatabase();
    }

    public void insert(String name, String password, String url)
    {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_PASSWORD, password);
        cv.put(KEY_URL, url);

        long temp = sqLiteDatabase.insert(DATABASE_TABLE, null, cv);
        if(temp == -1)
        {
            Toast.makeText(context, "Item not added", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Item Added", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<LoginNoteClass> readAllContacts()
    {
        Cursor c =  sqLiteDatabase.rawQuery("SELECT * FROM "+DATABASE_TABLE, null);
        ArrayList<LoginNoteClass> allLogins = new ArrayList<>();
        int id_index = c.getColumnIndex(KEY_ID);
        int id_name = c.getColumnIndex(KEY_NAME);
        int id_password = c.getColumnIndex(KEY_PASSWORD);
        int id_url = c.getColumnIndex(KEY_URL);

        if(c.moveToFirst())
        {
            do{
                LoginNoteClass login = new LoginNoteClass();
                login.setId(c.getInt(id_index));
                login.setName(c.getString(id_name));
                login.setPassword(c.getString(id_password));
                login.setUrl(c.getString(id_url));

                allLogins.add(login);

            }while(c.moveToNext());
        }

        c.close();
        return allLogins;
    }

    public int numberOfItems() {
        ArrayList<LoginNoteClass> temp = readAllContacts();
        return temp.isEmpty()? 0 : temp.size();
    }

    public void close()
    {
        sqLiteDatabase.close();
        helper.close();
    }




    private class MyDatabaseHelper extends SQLiteOpenHelper
    {
        public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "+DATABASE_TABLE+"("+
                    KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    KEY_NAME+" TEXT NOT NULL, "+
                    KEY_PASSWORD+" TEXT NOT NULL, "+
                    KEY_URL+" TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE "+DATABASE_TABLE+" IF EXISTS");
            onCreate(db);
        }
    }
}
