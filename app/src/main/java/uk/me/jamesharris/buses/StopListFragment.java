package uk.me.jamesharris.buses;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
            setupAdapter();

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




}
