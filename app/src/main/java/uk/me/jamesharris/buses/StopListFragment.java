package uk.me.jamesharris.buses;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by james on 18/06/15.
 */
public class StopListFragment extends ListFragment /*implements LoaderManager.LoaderCallbacks<Cursor>*/ {
    private static final String TAG = "BusListFragment";
    private static final int REFRESH_STOPLIST = 0;

    private BusManager mBusManager;


    @Override
    public void onCreate(Bundle sis) {
        super.onCreate(sis);
        setHasOptionsMenu(true);
        mBusManager = BusManager.get(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup viewGroup,
                             Bundle savedInstanceState) {

        View v = super.onCreateView(inflater,viewGroup,savedInstanceState);
        setupAdapter();
        return v;
    }

    void setupAdapter() {
        //getLoaderManager().initLoader(0, null, this);
        ListAdapter bl = new StopListAdapter();
        setListAdapter(bl);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.stop_list_options, menu);
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            mBusManager.refreshStopList();
            return null;
        }

        @Override
        protected void onPostExecute(Void b) {
            //mItems = items;
            setupAdapter();
            //getLoaderManager().restartLoader(0, null, StopListFragment.this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_refresh_stoplist:

                new FetchItemsTask().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent i = new Intent(getActivity(), BusListActivity.class);

        i.putExtra(BusListActivity.EXTRA_STOPSPEC, position);
        startActivity(i);
    }

    private class StopListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return mBusManager.numStopSpecs();
        }

        @Override
        public Object getItem(int position) {
            return mBusManager.getStopSpec(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(android.R.layout.simple_list_item_1, parent, false);

            }
            TextView textView = (TextView) convertView;
            String cellText = mBusManager.getStopSpec(position).getDescription();
            textView.setText(cellText);
            return convertView;
        }
    }

    /*
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG,"onCreateLoader");
        return new StopSpecCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        StopSpecCursorAdapter adapter = new StopSpecCursorAdapter(
                getActivity(), (StopSpecDatabaseHelper.StopSpecCursor) cursor);
        setListAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        setListAdapter(null);
    }

    @TargetApi(11)
    private static class StopSpecCursorAdapter extends CursorAdapter {
        private StopSpecDatabaseHelper.StopSpecCursor mCursor;
        public StopSpecCursorAdapter(Context context, StopSpecDatabaseHelper.StopSpecCursor cursor) {
            super(context,cursor,0);
            mCursor = cursor;
            Log.i(TAG,"StopSpecCursorAdapter");
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return li.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            Log.i(TAG,"StopSpecCursorAdapter::bindView");
            StopSpec stop = mCursor.getStopSpec();
            TextView startDateTextView = (TextView) view;
            String cellText = stop.getDescription();
            startDateTextView.setText(cellText);
        }
    }


    private static class StopSpecCursorLoader extends SQLiteCursorLoader {
        public StopSpecCursorLoader(Context context) {
            super(context);
        }

        @Override
        protected Cursor loadCursor() {
            Log.i(TAG, "StopSpecCursorLoader::loadCursor()");
            return BusManager.get(getContext()).queryStopSpecs();
        }
    }
    */


}
