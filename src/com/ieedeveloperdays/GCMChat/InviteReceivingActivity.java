package com.ieedeveloperdays.GCMChat;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.ieedeveloperdays.GCMChat.utils.Database;

import java.util.List;

public class InviteReceivingActivity extends Activity {

    private static final String TAG="CustomURLReceiver";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onReceive");

        if(getIntent()==null)finish();

        Uri data = getIntent().getData();

        String host=data.getHost().toLowerCase();

        // http/https://umh.ieeespain.org ?
        if(host.equals("umh.ieeespain.org"))
        {
            Log.d(TAG,"url lleva el host \""+host+"\"! procesando..");

            List<String> params = data.getPathSegments();
            if(params==null) return;

            String gcm_id=params.get(0);
            Log.d(TAG,"url primer param \""+gcm_id+"\" procesando..");

            String nick=params.get(1);
            Log.d(TAG,"url segundo param \""+nick+"\" procesando..");

            Database.guardaContacto(this,gcm_id,nick);

            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);

            finish();

        }
        finish();
    }

}
