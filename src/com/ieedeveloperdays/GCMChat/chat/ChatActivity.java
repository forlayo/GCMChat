package com.ieedeveloperdays.GCMChat.chat;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.ieedeveloperdays.GCMChat.R;
import com.ieedeveloperdays.GCMChat.utils.Database;

public class ChatActivity extends FragmentActivity{

    protected String gcm_id;
    private String nick;
    private EditText editText;

    public static final String GCMID_EXTRA="gcm_id";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        gcm_id = getIntent().getStringExtra(GCMID_EXTRA);
        nick = Database.getPrefsNick(this);
        editText = (EditText)findViewById(R.id.chat_edittext);
    }

    public void enviarMensaje(View v)
    {
        String texto =editText.getText().toString();
        if(!TextUtils.isEmpty(texto))
        {
            Database.enviaMensaje(this,gcm_id,nick,texto);
            Database.guardaMensaje(this,gcm_id,nick,texto,"<");
            editText.getText().clear();
        }
    }

}
