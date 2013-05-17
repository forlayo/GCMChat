package com.ieedeveloperdays.GCMChat.utils;


import android.content.Context;
import android.content.Intent;

public class Invite {

    public static void sendInvite(Context ctx)
    {

        String gcm_id=Database.getPrefsGcmid(ctx);
        String nick=Database.getPrefsNick(ctx);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Te invito a mi increible minichat");
        intent.putExtra(Intent.EXTRA_TEXT, "Una vez hayas instalado la aplicación, usa este enlace (http://umh.ieeespain.org/"+gcm_id+"/"+nick+") para agregarme a tus contactos");
        ctx.startActivity(Intent.createChooser(intent, "Invitar a un amigo"));
    }
}
