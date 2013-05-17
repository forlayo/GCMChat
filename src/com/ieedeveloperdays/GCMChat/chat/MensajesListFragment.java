package com.ieedeveloperdays.GCMChat.chat;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ieedeveloperdays.GCMChat.R;
import com.ieedeveloperdays.GCMChat.db.MensajesAdapter;
import com.ieedeveloperdays.GCMChat.db.MensajesProvider;

public class MensajesListFragment  extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    MensajesAdapter adapter;
    ChatActivity parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.element_list, container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        parent = (ChatActivity)getActivity();

        getActivity().getSupportLoaderManager().initLoader(0x01, null, this);
        adapter = new MensajesAdapter(getActivity(),null);

        setListAdapter(adapter);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String selection = MensajesProvider.DB_GCM_ID+"='"+parent.gcm_id+"'";
        return new CursorLoader(getActivity(), MensajesProvider.MENSAJES_CONTENT_URI, MensajesProvider.PROYECCION_MENSAJES,selection,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }

    protected void scrollMyListViewToBottom() {
        getListView().post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                getListView().setSelection(getListView().getCount() - 1);
            }
        });
    }
}
