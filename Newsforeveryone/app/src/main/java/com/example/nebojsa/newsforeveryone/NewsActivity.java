package com.example.nebojsa.newsforeveryone;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
//TODO Responsive design,for landscape orientation and for tablet devices
public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

   // private static final String USGS_REQUEST_URL =
     //       "https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&category=cience-and-nature&apiKey=4c11b311bd5e4620a09d1f753f5b1044";

    private static final String USGS_REQUEST_URL =
         "https://newsapi.org/v1/articles";

    private static final String LOG_TAG = NewsActivity.class.getName();

    private TextView mEmptyStateTextView;

    private View loadingIndicator;

    private static final int NEWS_LOADER_ID = 1;

    private ListView newsListView;

    /** Adapter for the list of earthquakes */
    private News_adapter myAdapter;

    private int index = 0;

    private int top = 0;

//    private Parcelable state ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST: onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        if(getIntent()!=null){
            index = getIntent().getIntExtra("index_2", 0);
            top = getIntent().getIntExtra("top_2", 0);
//            state = getIntent().getParcelableExtra("state_2");
        }

        newsListView = findViewById(R.id.list);

        myAdapter = new News_adapter(this, new ArrayList<News>());

        newsListView.setAdapter(myAdapter);

//        Log.i("PodatakAAAAAAAA1111",  "" + state);

//        if(state!=null) {
//            // e.g. set new items
//            newsListView.onRestoreInstanceState(state);
//        }


        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current news that was clicked on
                News currentNews = myAdapter.getItem(position);

                  index = newsListView.getFirstVisiblePosition();
                  View v = newsListView.getChildAt(0);
                  top = (v == null) ? 0 : (v.getTop() - newsListView.getPaddingTop());

                //Log.i("PodatakAAAAAAAA1111", "" + index);

//                state = newsListView.onSaveInstanceState();

//                Log.i("Podatakaaaaaaaaaaaaaaaa", "" + state);


                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getUrl());
                String url = newsUri.toString();

                String image = currentNews.getUrlToImage();
                String title = currentNews.getTitle();
                String author = currentNews.getAuthor();
                String time = currentNews.getTime();
                String text = currentNews.getDescription();


                Intent detailIntent = new Intent(NewsActivity.this, DetailActivity.class);
                detailIntent.putExtra("image_key", image);
                detailIntent.putExtra("title_key", title);
                detailIntent.putExtra("author_key", author);
                detailIntent.putExtra("time_key", time);
                detailIntent.putExtra("text_key", text);
                detailIntent.putExtra("index", index);
                detailIntent.putExtra("top", top);
                detailIntent.putExtra("web", url);
//                detailIntent.putExtra("state", state);
                startActivity(detailIntent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        //slucaj kad nemamo koja podatke da prikazemo
        mEmptyStateTextView = findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);


        loadingIndicator = findViewById(R.id.loading_indicator);


            // Get a reference to the ConnectivityManager to check state of network connectivity
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            // Get details on the currently active default data network
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            // If there is a network connection, fetch data
            if (networkInfo != null && networkInfo.isConnected()) {
                // Get a reference to the LoaderManager, in order to interact with loaders.
                LoaderManager loaderManager = getLoaderManager();

                Log.i("IDEMOMOMOMOMO", "TEST: initLoader called");
                // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                // because this activity implements the LoaderCallbacks interface).
                loaderManager.initLoader(NEWS_LOADER_ID, null, this);
            } else {
                // Otherwise, display error
                // First, hide loading indicator so error message will be visible
                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.GONE);

                // Update empty state with no connection error message
                mEmptyStateTextView.setText(R.string.no_internet_connection);
            }

            // Setup FAB to open EditorActivity
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NewsActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {

            // Get a reference to the ConnectivityManager to check state of network connectivity
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            // Get details on the currently active default data network
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                mEmptyStateTextView.setVisibility(View.GONE);
                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.VISIBLE);
               // Toast.makeText(this,"wait for a moment", Toast.LENGTH_SHORT).show();
                // Get a reference to the LoaderManager, in order to interact with loaders.
                LoaderManager loaderManager = getLoaderManager();

                Log.i(LOG_TAG, "TEST: initLoader called");
                // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                // because this activity implements the LoaderCallbacks interface).
                loaderManager.initLoader(NEWS_LOADER_ID, null, this);
            } else {
                loadingIndicator.setVisibility(View.GONE);
                 Toast.makeText(this, "No internet connection",Toast.LENGTH_SHORT).show();

            }

            return true;
        }
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "TEST: onCreateLoader called");
        // Create a new loader for the given URL
        //  return new EarthquakeLoader(this, USGS_REQUEST_URL);
        //stari url

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String source = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("source", source);
        uriBuilder.appendQueryParameter("sortBy", "top");
        uriBuilder.appendQueryParameter("apiKey", "223ef5f582e4462d91989ae4001578ff");

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> earthquakes) {
        Log.i(LOG_TAG, "TEST: onLoaderFinished called");
        loadingIndicator.setVisibility(View.GONE);
        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_news);
        // Clear the adapter of previous earthquake data
        myAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            myAdapter.addAll(earthquakes);
            newsListView.setSelectionFromTop(index,top);
        }
    }
    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Log.i(LOG_TAG, "TEST: onLoaderReset called");
        // Loader reset, so we can clear out our existing data.
        myAdapter.clear();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(state!=null) {
//            // e.g. set new items
//            newsListView.onRestoreInstanceState(state);
//        }
//    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
//        newsListView.setSelectionFromTop(index,top);
//        Log.i("OPAAAAAAAAAAAAAA", "" + index);
//        Log.i("OPAAAAAAAAAAAAAA@@@@@@@", "" + top);
//    }
    //NACINI NA KOJE SAM PROBAO DA RESIM PROBLE:
    //PRVO DA SACUVAM VREDNOST PRVOG VIDLJIVOG ITEMA I NJEGOVU POZICIJU NA EKRANU DA TO POSALJEMU U DETAIL ACTIVITY I ODATLE DA VRATIM U MAIN I METODOM SELECTIONFROMTOP DA ODATLE KRENEM LISTVIEW
    //DRUGI NACIN SLICNO PRVO SAMO KORISTIM METODU SELECTION I BEZ PARAMETRA TOP
    //TRECI NACIN PREKO STATE-A
}
