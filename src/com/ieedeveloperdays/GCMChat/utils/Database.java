package com.ieedeveloperdays.GCMChat.utils;


import android.content.*;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.ieedeveloperdays.GCMChat.db.ContactosProvider;
import com.ieedeveloperdays.GCMChat.db.MensajesProvider;
import com.ieedeveloperdays.GCMChat.utils.prefs.AbstractPrefsPersistStrategy;

import java.io.IOException;

public class Database {

    private static final String server_id="AIzaSyAt9DliLd6rBO140cw4_lxAjCbzROzvwv8";

    private static final String TAG = "Database";
    
    private static final String PREFERENCES_FILE = "gcmchat_preferences";
    private static final String PREFS_NICK = "nick";
    private static final String PREFS_GCMID = "gcm_id";

    public static void enviaMensaje(final Context ctx,final String gcm_id, final String nick, final String texto)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String our_gcmid = Database.getPrefsGcmid(ctx);
                Sender sender = new Sender(server_id);
                Message message = new Message.Builder()
                                                .addData("message",texto)
                                                .addData("gcm_id",our_gcmid)
                                                .addData("nick",nick)
                                                .build();
                Result result = null;
                try {
                    result = sender.send(message, gcm_id, 5);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (result!=null && result.getMessageId() != null) {
                    String canonicalRegId = result.getCanonicalRegistrationId();
                    if (canonicalRegId != null) {
                        // El usuario ha cambiado de id, actualizar la base de datos con el nuevo
                    }
                } else {
                    String error = result.getErrorCodeName();
                    if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                        // El usuario ha eliminado la aplicación del dispositivo, debemos borrar el contacto
                    }
                }

            }
        }).start();
    }

    public static void guardaMensaje(Context ctx, String gcm_id, String nick, String texto, String estado )
    {
        ContentResolver cResolver = ctx.getContentResolver();
        if(cResolver==null)return;

        ContentProviderClient cpClient = cResolver.acquireContentProviderClient(MensajesProvider.MENSAJES_CONTENT_URI);
        if (cpClient == null) return;

        ContentValues values = new ContentValues();
        values.put(MensajesProvider.DB_TEXTO,texto);
        values.put(MensajesProvider.DB_GCM_ID, gcm_id);
        values.put(MensajesProvider.DB_NICK,nick);
        values.put(MensajesProvider.DB_ESTADO,estado);
        values.put(MensajesProvider.DB_TIMESTAMP,System.currentTimeMillis());

        try {
            cpClient.insert(MensajesProvider.MENSAJES_CONTENT_URI, values);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void guardaContacto(Context ctx, String gcm_id, String nick )
    {
        Log.d(TAG, "Guarda contacto");
        ContentResolver cResolver = ctx.getContentResolver();
        if(cResolver==null)return;

        Log.d(TAG, "Guarda contacto");

        ContentProviderClient cpClient = cResolver.acquireContentProviderClient(ContactosProvider.CONTACTOS_CONTENT_URI);
        if (cpClient == null) return;

        Log.d(TAG, "Guarda contacto");

        ContentValues values = new ContentValues();
        values.put(ContactosProvider.DB_GCM_ID, gcm_id);
        values.put(ContactosProvider.DB_NICK,nick);

        try {
            cpClient.insert(ContactosProvider.CONTACTOS_CONTENT_URI, values);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void setPrefsGcmid(Context ctx, String gcm_id)
    {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit();

        editor.putString(PREFS_GCMID, gcm_id);

        AbstractPrefsPersistStrategy.persist(editor);
    }

    public static String getPrefsGcmid(Context ctx)
    {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return prefs.getString(PREFS_GCMID,null);
    }


    public static void setPrefsNick(Context ctx, String nick)
    {

        SharedPreferences.Editor editor = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit();

        editor.putString(PREFS_NICK, nick);

        AbstractPrefsPersistStrategy.persist(editor);
    }

    public static String getPrefsNick(Context ctx)
    {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return prefs.getString(PREFS_NICK,null);
    }
}
