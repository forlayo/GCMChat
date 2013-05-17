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


public class ContactosAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    public static class ViewHolder {
        TextView name;
    }

    public ContactosAdapter(Context context, Cursor c) {
        super(context, c, 0);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        View v = mInflater.inflate(R.layout.contactos_row, parent, false);
        holder.name = (TextView) v.findViewById(R.id.name);
        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();
        String contact_name = cursor.getString(cursor.getColumnIndex(ContactosProvider.DB_NICK));

        holder.name.setText(contact_name);
    }

    public String getGcmId(int position)
    {
        Cursor cursor = (Cursor) this.getItem(position);
        if(cursor.getCount()>0){
            int gcmIdIndex = cursor.getColumnIndex(ContactosProvider.DB_GCM_ID);
            if(gcmIdIndex>0)
            {
                return cursor.getString(gcmIdIndex);
            }
        }

        return null;
    }
}
