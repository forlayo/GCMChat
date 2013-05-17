package com.ieedeveloperdays.GCMChat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "DataBaseHelper";


    private static final String DATABASE_NAME = "GCMChatDB";

    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    public static final String CONTACTOS_TABLE="contactos";
    private static final String CREATE_CONTACTS_TABLE="create table "+CONTACTOS_TABLE+" ( " +
                                                        BaseColumns._ID+" integer primary key autoincrement, " +
                                                      "gcm_id text,"+
                                                      "name text,"+
                                                      "nick text"+
                                                      ");";

    public static final String MENSAJES_TABLE="messages";
    private static final String CREATE_MENSAJES_TABLE="create table "+MENSAJES_TABLE+" ( " +
            BaseColumns._ID+" integer primary key autoincrement, " +
            "gcm_id text,"+
            "nick text,"+
            "texto text,"+
            "estado integer,"+
            "timestamp integer"+
            ");";

    private static DataBaseHelper mInstance = null;
    public static DataBaseHelper getInstance(Context ctx) {
        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activities
         * context (see this article for more information:
         * http://developer.android.com/resources/articles/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new DataBaseHelper (ctx.getApplicationContext());
        }
        return mInstance;
    }


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_CONTACTS_TABLE);
        database.execSQL(CREATE_MENSAJES_TABLE);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        Log.w(LOG_TAG,
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS MyEmployees");
        onCreate(database);
    }

}