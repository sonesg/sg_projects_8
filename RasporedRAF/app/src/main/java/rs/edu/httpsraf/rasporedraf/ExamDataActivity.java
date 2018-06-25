package rs.edu.httpsraf.rasporedraf;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import rs.edu.httpsraf.rasporedraf.data.SubjectContract;

public class ExamDataActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER = 238;

    private GridView gridView;

    private ExamCursorAdapter adapter;

    private String dateToStr;
    private int brojac1;
    private int brojac2;

    private Boolean tipPodatka;

    //TODO Uneti ovde podatke. Srediti prvi ekran aplikacije. Srediti prenos podataka izmedju

//    private String[] prvaGodNaukeMreze = {"Matematička analiza",
//            "Objektno-orijentisano programiranje", "Diskretne strukture",
//            "Engleski jezik 2 - RN i RI", "Pismeno i usmeno izražavanje"};
//    private String[] drugaGodNauke = {"Operativni sistemi", "Računarske mreže", "Upravljanje informacijama",
//            "Dizajn i analiza algoritama"};
//    private String[] trecaGodNauke = {"Interakcija čovek-računar", "Web programiranje", "Genetski algoritmi",
//            "Modelovanje i simulacija",
//            "Funkcionalno programiranje", "Softverske komponente",
//            "Mašinsko učenje", "Razvoj mobilnih aplikacija"};
//    private String[] cetvrtaGodNauke = {"Teorija algoritama, automata i jezika",
//            "Softversko inženjerstvo", "Konkurentni i distribuirani sistemi",
//            "Integrisani informacioni sistemi"};

//    private String[] drugaGodMreze = {};
//    private String[] trecaGodMreze = {};
//    private String[] cetvrtaGodMreze = {};
//
//    private String[] prvaGodDizajn = {};
//    private String[] drugaGodDizajn = {};
//    private String[] trecaGodDizajn = {};
//    private String[] cetvrtaGodDizajn = {};
//
//    private String[] prvaGodTehno = {};
//    private String[] drugaGodTehno = {};
//    private String[] trecaGodTehno = {};

    protected TextView empty_view;
    protected Button load_service;
    @SuppressLint("StaticFieldLeak")
    protected static ProgressBar progressBar;

    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    private final static String PROGRESS_BAR = "progress_54";
    private final static String SHARED_PREFERENCES_FILE_3 = "file3shared";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_exam);

        empty_view = findViewById(R.id.empty_adapter_exam);
        load_service = findViewById(R.id.startService_exam);
        progressBar = findViewById(R.id.progressBarData_exam);

        gridView = findViewById(R.id.gridViewExam);

        if (getIntent() != null) {
            tipPodatka = getIntent().getBooleanExtra("tip_podatka", false);
            if (!tipPodatka)
                Objects.requireNonNull(getSupportActionBar()).setTitle("Raspored ispita");
            else
                Objects.requireNonNull(getSupportActionBar()).setTitle("Raspored kolokvijuma");
        }

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_3, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        load_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean(PROGRESS_BAR, true);
                editor.apply();
                progressBar.setVisibility(View.VISIBLE);
                Intent i = new Intent(ExamDataActivity.this, ScheduleService.class);
                ExamDataActivity.this.startService(i);
            }
        });

        adapter = new ExamCursorAdapter(this, null, 0);

//        listView.setAdapter(adapter);
        gridView.setAdapter(adapter);

        Date today = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.");
        dateToStr = format.format(today);


        getLoaderManager().initLoader(LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                SubjectContract.SubjectEntry._ID,
                SubjectContract.SubjectEntry.COLUMN_NAME_EXAM,
                SubjectContract.SubjectEntry.COLUMN_DATE_EXAM,
                SubjectContract.SubjectEntry.COLUMN_UCIONICA_EXAM,
                SubjectContract.SubjectEntry.COLUMN_PROFESSOR_EXAM,
                SubjectContract.SubjectEntry.COLUMN_TIME_EXAM};

//        selection = SubjectContract.SubjectEntry.COLUMN_DAN + " LIKE '%" + "a" + "%'";
        String[] selectionArgs = {""};
//        String[] selectionArgs = prvaGodNaukeMreze;
//        String[] selectionArgs = {"Uvod u robotiku", "Poslovni softver",
//                "Razvoj Web aplikacija i servisa", "Engleski jezik 2 - RN i RI", "Pismeno i usmeno izrazavanje"};
//        selection = SubjectContract.SubjectEntry.COLUMN_NAME_EXAM + " IN(?,?,?,?,?,?,?,?,?,?) AND " + SubjectContract.SubjectEntry.COLUMN_TYPE_EXAM + "=" + tipPodatka;
        String selection;
        if (!tipPodatka)
            selection = SubjectContract.SubjectEntry.COLUMN_NAME_EXAM + "!=? AND " + SubjectContract.SubjectEntry.COLUMN_TYPE_EXAM + "='EXAM'";
        else
            selection = SubjectContract.SubjectEntry.COLUMN_NAME_EXAM + "!=? AND " + SubjectContract.SubjectEntry.COLUMN_TYPE_EXAM + "='CURRICULUM'";
        return new CursorLoader(this,   //Parent activity context
                SubjectContract.SubjectEntry.CONTENT_URI,   //Provider content URI to query
                projection,             //Columns to include in the resulting Cursor
                selection,
                selectionArgs,
                SubjectContract.SubjectEntry.COLUMN_DATE_EXAM + " ASC, " + SubjectContract.SubjectEntry.COLUMN_TIME_EXAM + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        while (cursor.moveToNext()){
         if(dateToStr.compareTo(cursor.getString(cursor.getColumnIndex(SubjectContract.SubjectEntry.COLUMN_DATE_EXAM))) > 0){
             brojac1++;
         }
         brojac2++;
        }
        Log.i("Brojaci",brojac1 + " " + brojac2);
        if(brojac1 == brojac2){
            adapter.swapCursor(null);
        }else {
            adapter.swapCursor(cursor);
        }
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
    protected void onStop() {
        super.onStop();
        editor.clear();
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.swapCursor(null);
        gridView = null;
        editor.clear();
        editor.apply();
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
        ExamDataActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.getParentActivityIntent();
    }
}
