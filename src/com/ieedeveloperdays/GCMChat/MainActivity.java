package com.ieedeveloperdays.GCMChat;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gcm.server.*;
import com.google.android.gcm.GCMRegistrar;
import com.ieedeveloperdays.GCMChat.utils.Database;
import com.ieedeveloperdays.GCMChat.utils.Invite;

import java.io.IOException;


public class MainActivity extends FragmentActivity
{
    private static final String TAG="MainActivity";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
            GCMRegistrar.register(this, GCMIntentService.SENDER_ID);
        } else {
            Log.v(TAG, "Already registered "+regId);
            if(Database.getPrefsGcmid(this)==null)
            {
                Database.setPrefsGcmid(this,regId);
            }
        }

        Database.setPrefsNick(this,"noname");

    }

    public void invitaContacto(View v)
    {
        if(Database.getPrefsGcmid(this)==null)
        {
            Toast.makeText(this,"Necesitas haber registrado GCM antes",Toast.LENGTH_LONG).show();
            return;
        }
        Invite.sendInvite(this);
    }

}
