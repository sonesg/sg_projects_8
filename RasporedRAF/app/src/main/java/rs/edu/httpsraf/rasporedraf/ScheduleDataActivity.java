package rs.edu.httpsraf.rasporedraf;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import rs.edu.httpsraf.rasporedraf.data.SubjectContract.SubjectEntry;

public class ScheduleDataActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //    ListView listView;
    GridView gridView;

    ScheduleCursorAdapter adapter;

    private static final int LOADER = 238;

    protected String podatak;
    protected int kolona;
    protected String selection = null;

    private final static String SHARED_PREFERENCES_FILE_3 = "file3shared";
    private final static String PON_KEY = "pon_key";
    private final static String UTO_KEY = "uto_key";
    private final static String SRE_KEY = "sre_key";
    private final static String CET_KEY = "cet_key";
    private final static String PET_KEY = "pet_key";
    private final static String SUB_KEY = "sub_key";

    private final static String PROGRESS_BAR = "progress_54";
//    private Bundle bundle;

    protected int dan;

    protected TextView empty_view;
    protected Button load_service;
    @SuppressLint("StaticFieldLeak")
    protected static ProgressBar progressBar;

    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

//        listView = findViewById(R.id.listView);
        gridView = findViewById(R.id.gridViewTry);

        empty_view = findViewById(R.id.empty_adapter);
        load_service = findViewById(R.id.startService);
        progressBar = findViewById(R.id.progressBarData);

        if (getActionBar() != null)
            Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            podatak = getIntent().getStringExtra("data_key");
//            Log.i("GETINTENT", podatak + "");
            kolona = getIntent().getIntExtra("column_data_key", 0);
            if (podatak != null)
                Objects.requireNonNull(getSupportActionBar()).setTitle(podatak + " raspored");
            else
                Objects.requireNonNull(getSupportActionBar()).setTitle("Čitav raspored");
        }

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_3, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

//        Log.i("BUOUO", sharedPreferences.getBoolean(PON_KEY, false) + "");

        load_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean(PROGRESS_BAR, true);
                editor.apply();
                progressBar.setVisibility(View.VISIBLE);
                Intent i = new Intent(ScheduleDataActivity.this, ScheduleService.class);
                ScheduleDataActivity.this.startService(i);
            }
        });

//        bundle = new Bundle();

        adapter = new ScheduleCursorAdapter(this, null, 0);

//        listView.setAdapter(adapter);
        gridView.setAdapter(adapter);

        getLoaderManager().initLoader(LOADER, null, this);
    }

    //Primer metode za brisanje svih elemenata iz nase lokalne baze
    private void deleteDataAll() {
        // Only perform the delete if this is an existing pet.
        if (SubjectEntry.CONTENT_URI != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(SubjectEntry.CONTENT_URI, null, null);
            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, "error with deleting data",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, "all data are deleted",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                SubjectEntry._ID,
                SubjectEntry.COLUMN_PREDMET,
                SubjectEntry.COLUMN_TIP,
                SubjectEntry.COLUMN_NASTAVNIK,
                SubjectEntry.COLUMN_GRUPE,
                SubjectEntry.COLUMN_DAN,
                SubjectEntry.COLUMN_TERMIN,
                SubjectEntry.COLUMN_UCIONICA};

//        String[] args = { "Matematicka analiza" };
//        String selection = SubjectEntry.COLUMN_PREDMET + "=?";
        if (kolona == 0) {
            selection = SubjectEntry.COLUMN_GRUPE + " LIKE '%" + podatak + "%'";
            if (podatak == null) {
                selection = null;
            }
        } else if (kolona == 1) {
            selection = SubjectEntry.COLUMN_NASTAVNIK + " LIKE '%" + podatak + "%'";
        } else if (kolona == 2) {
            selection = SubjectEntry.COLUMN_UCIONICA + " LIKE '%" + podatak + "%'";
        } else if (kolona == 3) {
            selection = SubjectEntry.COLUMN_PREDMET + " LIKE '%" + podatak + "%'";
        } else if (kolona == 4) {
            switch (podatak) {
                case "Ponedeljak":
                    dan = 1;
                    break;
                case "Utorak":
                    dan = 2;
                    break;
                case "Sreda":
                    dan = 3;
                    break;
                case "Četvrtak":
                    dan = 4;
                    break;
                case "Petak":
                    dan = 5;
                    break;
                case "Subota":
                    dan = 6;
                    break;
            }
            selection = SubjectEntry.COLUMN_DAN + " LIKE '%" + dan + "%'";
        }

        //this loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   //Parent activity context
                SubjectEntry.CONTENT_URI,   //Provider content URI to query
                projection,             //Columns to include in the resulting Cursor
                selection,
                null,
                SubjectEntry.COLUMN_DAN + " ASC, " + SubjectEntry.COLUMN_TERMIN + " ASC");//Default sort order

//        String[] args = { "Matematicka analiza", "second@string.com" };
//        Cursor cursor = db.query("TABLE_NAME", null, "name=? AND email=?", args, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        while (cursor.moveToNext()) {
            boolean pon = sharedPreferences.getBoolean(PON_KEY, false);
            boolean uto = sharedPreferences.getBoolean(UTO_KEY, false);
            boolean sre = sharedPreferences.getBoolean(SRE_KEY, false);
            boolean cet = sharedPreferences.getBoolean(CET_KEY, false);
            boolean pet = sharedPreferences.getBoolean(PET_KEY, false);
            boolean sub = sharedPreferences.getBoolean(SUB_KEY, false);
            if (cursor.getInt(cursor.getColumnIndex(SubjectEntry.COLUMN_DAN)) == 1 && !pon) {
                editor.putBoolean(PON_KEY, true);
                editor.putInt(cursor.getString(cursor.getColumnIndex(SubjectEntry.COLUMN_PREDMET)) +
                        cursor.getString(cursor.getColumnIndex(SubjectEntry.COLUMN_TERMIN)) +
                        cursor.getString(cursor.getColumnIndex(SubjectEntry.COLUMN_DAN)), 21);
                editor.apply();
            } else if (cursor.getInt(cursor.getColumnIndex(SubjectEntry.COLUMN_DAN)) == 2 && !uto) {
                editor.putBoolean(UTO_KEY, true);
                editor.putInt(cursor.getString(cursor.getColumnIndex(SubjectEntry.COLUMN_PREDMET)) +
                        cursor.getString(cursor.getColumnIndex(SubjectEntry.COLUMN_TERMIN)) +
                        cursor.getString(cursor.getColumnIndex(SubjectEntry.COLUMN_DAN)), 22);
                editor.apply();
            } else if (cursor.getInt(cursor.getColumnIndex(SubjectEntry.COLUMN_DAN)) == 3 && !sre) {
                editor.putBoolean(SRE_KEY, true);
                editor.putInt(cursor.getString(cursor.getColumnIndex(SubjectEntry.COLUMN_PREDMET)) +
                        cursor.getString(cursor.getColumnIndex(SubjectEntry.COLUMN_TERMIN)) +
                        cursor.getString(cursor.getColumnIndex(SubjectEntry.COLUMN_DAN)), 23);
                editor.apply();
            } else if (cursor.getInt(cursor.getColumnIndex(SubjectEntry.COLUMN_DAN)) == 4 && !cet) {
                editor.putBoolean(CET_KEY, true);
                editor.putInt(cursor.getString(cursor.getColumnIndex(SubjectEntry.COLUMN_PREDMET)) +
                        cursor.getString(cursor.getColumnIndex(SubjectEntry.COLUMN_TERMIN)) +
                        cursor.getString(cursor.getColumnIndex(SubjectEntry.COLUMN_DAN)), 24);
                editor.apply();
            } else if (cursor.getInt(cursor.getColumnIndex(SubjectEntry.COLUMN_DAN)) == 5 && !pet) {
                editor.putBoolean(PET_KEY, true);
                editor.putInt(cursor.getString(cursor.getColumnIndex(SubjectEntry.COLUMN_PREDMET)) +
                        cursor.getString(cursor.getColumnIndex(SubjectEntry.COLUMN_TERMIN)) +
                        cursor.getString(cursor.getColumnIndex(SubjectEntry.COLUMN_DAN)), 25);
                editor.apply();
            } else if (cursor.getInt(cursor.getColumnIndex(SubjectEntry.COLUMN_DAN)) == 6 && !sub) {
                editor.putBoolean(SUB_KEY, true);
                editor.putInt(cursor.getString(cursor.getColumnIndex(SubjectEntry.COLUMN_PREDMET)) +
                        cursor.getString(cursor.getColumnIndex(SubjectEntry.COLUMN_TERMIN)) +
                        cursor.getString(cursor.getColumnIndex(SubjectEntry.COLUMN_DAN)), 26);
                editor.apply();
            }
        }

        //Update with this new cursor containing updated pet data
        adapter.swapCursor(cursor);
        if (adapter.isEmpty()) {
            gridView.setVisibility(View.GONE);
            empty_view.setVisibility(View.VISIBLE);
            load_service.setVisibility(View.VISIBLE);
        } else {
            gridView.setVisibility(View.VISIBLE);
            empty_view.setVisibility(View.GONE);
            load_service.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Callback called when the data needs to be deleted
        adapter.swapCursor(null);
        if (!adapter.isEmpty()) {
            gridView.setVisibility(View.VISIBLE);
            empty_view.setVisibility(View.GONE);
            load_service.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.app_logo) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        bundle.clear();
        adapter.swapCursor(null);
        gridView = null;
        editor.clear();
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        bundle.clear();
//        adapter.swapCursor(null);
//        gridView = null;
        editor.clear();
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (load_service.getVisibility() == View.VISIBLE){
//            //start Service
//            Intent i = new Intent(ScheduleDataActivity.this, ScheduleService.class);
//            this.startService(i);
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        finish();
        ScheduleDataActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.getParentActivityIntent();
    }
}
