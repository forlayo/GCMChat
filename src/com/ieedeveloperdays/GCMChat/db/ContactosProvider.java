package com.ieedeveloperdays.GCMChat.db;


import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

public class ContactosProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "com.ieedeveloperdays.GCMChat.Contactos";
    public static final Uri CONTACTOS_CONTENT_URI = Uri.parse("content://"+ PROVIDER_NAME + "/contactos");


    public static final String DB_ID = BaseColumns._ID;
    public static final String DB_GCM_ID = "gcm_id";
    public static final String DB_NAME = "name";
    public static final String DB_NICK = "nick";

    public static final String[] PROYECCION_CONTACTOS = new String[]{
           ContactosProvider.DB_ID,
           ContactosProvider.DB_GCM_ID,
           ContactosProvider.DB_NAME,
           ContactosProvider.DB_NICK
    };

    private static final int CONTACTOS = 1;
    private static final int CONTACTOS_ID = 2;


    private static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "contactos", CONTACTOS);
        uriMatcher.addURI(PROVIDER_NAME, "contactos/#", CONTACTOS_ID);
    }

    private SQLiteDatabase dataBase = null;
    private DataBaseHelper mDbHelper = null;

    @Override
    public boolean onCreate() {
        Log.d("CULO", "Provider create");
        Context context = getContext();
        mDbHelper = DataBaseHelper.getInstance(context);
        dataBase = mDbHelper.getWritableDatabase();
        return (dataBase == null&&dataBase.isOpen())? false:true;

    }

    private SQLiteDatabase getOrOpenDatabase(){
        SQLiteDatabase db =null;
        if(mDbHelper==null)mDbHelper = DataBaseHelper.getInstance(getContext());

        if(this.dataBase!=null&&dataBase.isOpen())
        {
            db =this.dataBase;
        }
        else
        {
            db = mDbHelper.getWritableDatabase();
        }
        return db;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Log.d("CULO", "Provider query");

        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        sqlBuilder.setTables(DataBaseHelper.CONTACTOS_TABLE);

        if (uriMatcher.match(uri) == CONTACTOS_ID)
            sqlBuilder.appendWhere(DB_ID + " = " + uri.getPathSegments().get(1));

        Cursor c = sqlBuilder.query(
                getOrOpenDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case CONTACTOS:
                return "vnd.android.cursor.item/vnd.gcmchat.contactos";
            case CONTACTOS_ID:
                return "vnd.android.cursor.dir/vnd.gcmchat.contactos";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Log.d("CULO", "Provider insert");

        long rowID = getOrOpenDatabase().insert(DataBaseHelper.CONTACTOS_TABLE, "", values);

        if (rowID>0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTACTOS_CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count=0;
        switch (uriMatcher.match(uri)){
            case CONTACTOS:
                count = getOrOpenDatabase().delete(
                        DataBaseHelper.CONTACTOS_TABLE,
                        selection,
                        selectionArgs);
                break;
            case CONTACTOS_ID:
                String id = uri.getPathSegments().get(1);
                count = getOrOpenDatabase().delete(
                        DataBaseHelper.CONTACTOS_TABLE,
                        DB_ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default: throw new IllegalArgumentException(
                    "Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case CONTACTOS:
                count = getOrOpenDatabase().update(DataBaseHelper.CONTACTOS_TABLE, values,selection, selectionArgs);
                break;
            case CONTACTOS_ID:
                count = getOrOpenDatabase().update(DataBaseHelper.CONTACTOS_TABLE, values,
                        DB_ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default: throw new IllegalArgumentException(
                    "Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
