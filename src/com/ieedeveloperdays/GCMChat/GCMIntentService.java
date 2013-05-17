package com.ieedeveloperdays.GCMChat;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gcm.GCMBaseIntentService;
import com.ieedeveloperdays.GCMChat.chat.ChatActivity;
import com.ieedeveloperdays.GCMChat.utils.Database;

public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    public static final String SENDER_ID = "44629282218";

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        String texto = intent.getExtras().getString("message");
        String gcm_id = intent.getExtras().getString("gcm_id");
        String nick = intent.getExtras().getString("nick");
        Log.i(TAG, "Received msg  nick: "+nick+" nick:"+texto+" gcm_id:"+gcm_id);

        Database.guardaMensaje(context,gcm_id,nick,texto,">");
        creaNotificacion(context,nick+": "+texto,gcm_id);

    }

    @Override
    protected void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);

        Database.setPrefsGcmid(context,registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String s) {
        Log.i(TAG, "Device unregistered");
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        Log.i(TAG, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }

    private void creaNotificacion(Context ctx, String msg, String gcm_id) {
        mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent i = new Intent(ctx, ChatActivity.class);
        i.putExtra(ChatActivity.GCMID_EXTRA,gcm_id);
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, i, 0);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("GCMChat notification")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
