package com.ieedeveloperdays.GCMChat.db;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ieedeveloperdays.GCMChat.R;

public class MensajesAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    public static class ViewHolder {
        TextView texto;
        TextView estado;
    }

    public MensajesAdapter(Context context, Cursor c) {
        super(context, c, 0);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        View v = mInflater.inflate(R.layout.mensajes_row, parent, false);
        holder.texto = (TextView) v.findViewById(R.id.texto);
        holder.estado = (TextView) v.findViewById(R.id.estado);
        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();
        String estado = cursor.getString(cursor.getColumnIndex(MensajesProvider.DB_ESTADO));
        holder.estado.setText(estado);

        String texto = cursor.getString(cursor.getColumnIndex(MensajesProvider.DB_TEXTO));
        holder.texto.setText(texto);
    }
}
