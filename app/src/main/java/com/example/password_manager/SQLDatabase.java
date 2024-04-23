package com.example.password_manager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SQLDatabase {
    private final String DATABASE_NAME = "NotesDB";

    // TABLES
    private final String DATABASE_TABLE_LOGIN = "Login_Table";
    private final String DATABASE_TABLE_NOTES = "Notes_Table";


    // TABLE NO 1
    private final String KEY_ID_LOGIN = "_id";
    private final String KEY_FIRSTNAME_LOGIN = "_firstname";
    private final String KEY_LASTNAME_LOGIN = "_lastname";
    private final String KEY_MAIL_LOGIN = "_mail";
    private final String KEY_GENDER_LOGIN = "_gender";
    private final String KEY_PASSWORD_LOGIN = "_password";
    private final String KEY_ISLOGIN = "_islogin";


    // TABLE NO 2
    private final String KEY_ID_NOTE = "_id";
    private final String KEY_NAME_NOTE = "_name";
    private final String KEY_PASSWORD_NOTE = "_password";
    private final String KEY_URL_NOTE = "_url";
    private final String KEY_USER = "_user";
    private final String KEY_SHOW = "_toshow";

    private final int DATABASE_VERSION = 1;

    Context context;

    MyDatabaseHelper helper;
    SQLiteDatabase sqLiteDatabase;

    public SQLDatabase(Context c)
    {
        context = c;
    }

    public void open() {
        helper = new MyDatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = helper.getWritableDatabase();
    }
    public void close() {
        sqLiteDatabase.close();
        helper.close();
    }

    public void insertNote(String name, String password, String url) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME_NOTE, name);
        cv.put(KEY_PASSWORD_NOTE, password);
        cv.put(KEY_URL_NOTE, url);
        cv.put(KEY_SHOW,1);
        cv.put(KEY_USER,getLoggedInID());


        long temp = sqLiteDatabase.insert(DATABASE_TABLE_NOTES, null, cv);
        if(temp == -1)
        {
            Toast.makeText(context, "Item not added", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Item Added", Toast.LENGTH_SHORT).show();
        }
    }
    public void insertLogin(String firstName, String lastName, String email, String password, String gender) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_FIRSTNAME_LOGIN, firstName);
        cv.put(KEY_LASTNAME_LOGIN, lastName);
        cv.put(KEY_MAIL_LOGIN, email);
        cv.put(KEY_PASSWORD_LOGIN, password);
        cv.put(KEY_GENDER_LOGIN, gender);
        cv.put(KEY_ISLOGIN, 0);

        long temp = sqLiteDatabase.insert(DATABASE_TABLE_LOGIN, null, cv);
        if(temp == -1)
        {
            Toast.makeText(context, "Not Registered", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Registered", Toast.LENGTH_SHORT).show();
        }

    }

    public ArrayList<NoteClass> readAllNotes() {
        String loggedInUserID = getLoggedInID() + "";

        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM " + DATABASE_TABLE_NOTES + " WHERE " + KEY_USER + " = ?", new String[]{loggedInUserID});
        ArrayList<NoteClass> allLogins = new ArrayList<>();
        int id_index = c.getColumnIndex(KEY_ID_NOTE);
        int id_name = c.getColumnIndex(KEY_NAME_NOTE);
        int id_password = c.getColumnIndex(KEY_PASSWORD_NOTE);
        int id_url = c.getColumnIndex(KEY_URL_NOTE);

        if(c.moveToFirst())
        {
            do{
                NoteClass login = new NoteClass();
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

    //--------------------------------------------------------------------------------------------

    public void loginUser(String email, String password) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ISLOGIN, 1);

        sqLiteDatabase.update(DATABASE_TABLE_LOGIN, cv, KEY_MAIL_LOGIN+ "=? and "+KEY_PASSWORD_LOGIN+ "=?", new String[]{email,password});


    }

    public void logoutUser(){
        ContentValues cv = new ContentValues();
        cv.put(KEY_ISLOGIN, 0);

        sqLiteDatabase.update(DATABASE_TABLE_LOGIN, cv, null, new String[]{});


    }

    public boolean isUserLoggedIn() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DATABASE_TABLE_LOGIN + " WHERE " + KEY_ISLOGIN + " = 1", null);
        boolean isLoggedIn = cursor.getCount() > 0;
        cursor.close();
        return isLoggedIn;
    }

    public String getLoggedInUserName() {
        String firstName = null;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + KEY_FIRSTNAME_LOGIN + " FROM " + DATABASE_TABLE_LOGIN + " WHERE " + KEY_ISLOGIN + " = 1", null);
        if (cursor.moveToFirst()) {
            int id =cursor.getColumnIndex(KEY_FIRSTNAME_LOGIN);
            firstName = cursor.getString(id);
        }
        cursor.close();
        return firstName;
    }

    public int getLoggedInID(){
        int id = 0;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + KEY_ID_LOGIN + " FROM " + DATABASE_TABLE_LOGIN + " WHERE " + KEY_ISLOGIN + " = 1", null);
        if (cursor.moveToFirst()) {
            int temp =cursor.getColumnIndex(KEY_ID_LOGIN);
            id = cursor.getInt(temp);
        }
        cursor.close();
        return id;
    }


    private class MyDatabaseHelper extends SQLiteOpenHelper
    {
        public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            // LOGIN TABLE CREATION
            db.execSQL("CREATE TABLE "+DATABASE_TABLE_LOGIN+"("+
                    KEY_ID_LOGIN+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    KEY_FIRSTNAME_LOGIN+" TEXT NOT NULL, "+
                    KEY_LASTNAME_LOGIN+" TEXT NOT NULL, "+
                    KEY_MAIL_LOGIN+" TEXT NOT NULL, "+
                    KEY_GENDER_LOGIN+" TEXT NOT NULL, "+
                    KEY_ISLOGIN+" INTEGER NOT NULL, "+
                    KEY_PASSWORD_LOGIN+" TEXT NOT NULL);");


            // NOTES TABLE CREATION
            db.execSQL("CREATE TABLE "+DATABASE_TABLE_NOTES+"("+
                    KEY_ID_NOTE+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    KEY_NAME_NOTE+" TEXT NOT NULL, "+
                    KEY_PASSWORD_NOTE+" TEXT NOT NULL, "+
                    KEY_URL_NOTE+" TEXT NOT NULL, "+
                    KEY_SHOW+" INTEGER NOT NULL, "+
                    KEY_USER+" INTEGER NOT NULL, "+
                    "FOREIGN KEY("+KEY_USER+") REFERENCES "+DATABASE_TABLE_LOGIN+"("+KEY_ID_LOGIN+"));");




        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE "+DATABASE_TABLE_NOTES+" IF EXISTS");
            db.execSQL("DROP TABLE "+DATABASE_TABLE_LOGIN+" IF EXISTS");
            onCreate(db);
        }
    }
}
