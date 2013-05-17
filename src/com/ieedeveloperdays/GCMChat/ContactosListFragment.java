package com.ieedeveloperdays.GCMChat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.ieedeveloperdays.GCMChat.chat.ChatActivity;
import com.ieedeveloperdays.GCMChat.db.ContactosAdapter;
import com.ieedeveloperdays.GCMChat.db.ContactosProvider;


public class ContactosListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    ContactosAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.element_list, container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(0x01, null, this);
        adapter = new ContactosAdapter(getActivity(),null);

        setListAdapter(adapter);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), ContactosProvider.CONTACTOS_CONTENT_URI,ContactosProvider.PROYECCION_CONTACTOS,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String gcmId = adapter.getGcmId(position);
        if(!TextUtils.isEmpty(gcmId))
        {
            Intent i = new Intent(getActivity(), ChatActivity.class);
            i.putExtra(ChatActivity.GCMID_EXTRA,gcmId);
            startActivity(i);
        }
    }
}
