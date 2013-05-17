package com.ieedeveloperdays.GCMChat.db;


import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

public class MensajesProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "com.ieedeveloperdays.GCMChat.Mensajes";
    public static final Uri MENSAJES_CONTENT_URI = Uri.parse("content://"+ PROVIDER_NAME + "/mensajes");


    public static final String DB_ID = BaseColumns._ID;
    public static final String DB_GCM_ID = "gcm_id";
    public static final String DB_NICK = "nick";
    public static final String DB_TEXTO = "texto";
    public static final String DB_ESTADO = "estado";
    public static final String DB_TIMESTAMP = "timestamp";

    public static final String[] PROYECCION_MENSAJES = new String[]{
           MensajesProvider.DB_ID,
           MensajesProvider.DB_GCM_ID,
           MensajesProvider.DB_NICK,
           MensajesProvider.DB_TEXTO,
           MensajesProvider.DB_ESTADO,
           MensajesProvider.DB_TIMESTAMP
    };

    private static final int MENSAJES = 1;
    private static final int MENSAJES_ID = 2;


    private static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "mensajes", MENSAJES);
        uriMatcher.addURI(PROVIDER_NAME, "mensajes/#", MENSAJES_ID);
    }

    private SQLiteDatabase dataBase = null;
    private DataBaseHelper mDbHelper = null;

    @Override
    public boolean onCreate() {

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
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        sqlBuilder.setTables(DataBaseHelper.MENSAJES_TABLE);

        if (uriMatcher.match(uri) == MENSAJES_ID)
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
            case MENSAJES:
                return "vnd.android.cursor.item/vnd.gcmchat.mensajes";
            case MENSAJES_ID:
                return "vnd.android.cursor.dir/vnd.gcmchat.mensajes";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = getOrOpenDatabase().insert(DataBaseHelper.MENSAJES_TABLE, "", values);

        if (rowID>0)
        {
            Uri _uri = ContentUris.withAppendedId(MENSAJES_CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count=0;
        switch (uriMatcher.match(uri)){
            case MENSAJES:
                count = getOrOpenDatabase().delete(
                        DataBaseHelper.MENSAJES_TABLE,
                        selection,
                        selectionArgs);
                break;
            case MENSAJES_ID:
                String id = uri.getPathSegments().get(1);
                count = getOrOpenDatabase().delete(
                        DataBaseHelper.MENSAJES_TABLE,
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
            case MENSAJES:
                count = getOrOpenDatabase().update(DataBaseHelper.MENSAJES_TABLE, values,selection, selectionArgs);
                break;
            case MENSAJES_ID:
                count = getOrOpenDatabase().update(DataBaseHelper.MENSAJES_TABLE, values,
                        DB_ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default: throw new IllegalArgumentException(
                    "Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
