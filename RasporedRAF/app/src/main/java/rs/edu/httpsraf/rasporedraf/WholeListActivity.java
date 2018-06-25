package rs.edu.httpsraf.rasporedraf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WholeListActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCES_FILE = "shared_preferences_name";
    private static final String NASTAVNICI_KEY = "nastavnik_key_shared";
    private static final String PREDMETI_KEY = "predmet_key_shared";
    private static final String CELA_LISTA = "cela_lista";
    private static final String DATA_SMER = "data_smer";

    private static final String NASTAVNICI_KEY_DIZAJN = "nastavnik_key_shared_diz";
    private static final String PREDMETI_KEY_DIZAJN = "predmet_key_shared_diz";
    private static final String NASTAVNICI_KEY_NAUKE = "nastavnik_key_shared_nauk";
    private static final String PREDMETI_KEY_NAUKE = "predmet_key_shared_nauk";
    private static final String NASTAVNICI_KEY_INZENJERSTVO = "nastavnik_key_shared_mreze";
    private static final String PREDMETI_KEY_INZENJERSTVO = "predmet_key_shared_mreze";
    private static final String NASTAVNICI_KEY_TEHNOLOGIJE = "nastavnik_key_shared_tehn";
    private static final String PREDMETI_KEY_TEHNOLOGIJE = "predmet_key_shared_tehn";

    protected ListView listView;
    protected AdapterWholeList customAdapter;

    protected String jsonNastavnici, jsonPredmeti;

    protected List<String> list = null;

    protected String[] nastavnici;
    protected String[] predmeti;

    protected ArrayList<String> nastavniciArrayList;
    protected ArrayList<String> predmetiArrayList;

    protected int podatak;
    protected int kolona;
    protected int smer;


    protected EditText editTextSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whole_list);

        if (getActionBar() != null)
            Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            podatak = getIntent().getIntExtra(CELA_LISTA, 0);
            smer = getIntent().getIntExtra(DATA_SMER, 0);
        }

        listView = findViewById(R.id.whole_list_view);

        editTextSearch = findViewById(R.id.editText_search);

        loadData(podatak, smer);


        if (podatak == 21 || podatak == 24) {
            predmeti = new String[predmetiArrayList.size()];
            predmeti = predmetiArrayList.toArray(predmeti);
            Arrays.sort(predmeti);
            list = new ArrayList<>(Arrays.asList(predmeti));
        } else if (podatak == 22 || podatak == 23) {
            nastavnici = new String[nastavniciArrayList.size()];
            nastavnici = nastavniciArrayList.toArray(nastavnici);
            Arrays.sort(nastavnici);
            list = new ArrayList<>(Arrays.asList(nastavnici));
        }

//        adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, android.R.id.text1, list);

          customAdapter = new AdapterWholeList(this, R.layout.itemlist, list);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (podatak == 21 || podatak == 24)
                    kolona = 3;
                else if (podatak == 22 || podatak == 23)
                    kolona = 1;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);


                Intent intent = new Intent(WholeListActivity.this, ScheduleDataActivity.class);
                intent.putExtra("data_key", itemValue);
                intent.putExtra("column_data_key", kolona);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //mislim da je ovo nepotrebno
                customAdapter.notifyDataSetChanged();

            }

        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                customAdapter.getFilter().filter(charSequence);
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                customAdapter.notifyDataSetChanged();
            }
        });

        listView.setAdapter(customAdapter);
    }

    private void loadData(int podatak, int smer) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        if (podatak == 21) {
            jsonPredmeti = sharedPreferences.getString(PREDMETI_KEY, null);
            Objects.requireNonNull(getSupportActionBar()).setTitle("Svi predmeti");
            predmetiArrayList = gson.fromJson(jsonPredmeti, type);
        } else if (podatak == 22) {
            jsonNastavnici = sharedPreferences.getString(NASTAVNICI_KEY, null);
            Objects.requireNonNull(getSupportActionBar()).setTitle("Svi predavači");
            nastavniciArrayList = gson.fromJson(jsonNastavnici, type);
        } else if (podatak == 23) {
            if (smer == 31)
                jsonNastavnici = sharedPreferences.getString(NASTAVNICI_KEY_NAUKE, null);
            else if (smer == 32)
                jsonNastavnici = sharedPreferences.getString(NASTAVNICI_KEY_INZENJERSTVO, null);
            else if (smer == 33)
                jsonNastavnici = sharedPreferences.getString(NASTAVNICI_KEY_DIZAJN, null);
            else if (smer == 34)
                jsonNastavnici = sharedPreferences.getString(NASTAVNICI_KEY_TEHNOLOGIJE, null);
            nastavniciArrayList = gson.fromJson(jsonNastavnici, type);
            Objects.requireNonNull(getSupportActionBar()).setTitle("Predavači");
        } else if (podatak == 24) {
            if (smer == 31)
                jsonPredmeti = sharedPreferences.getString(PREDMETI_KEY_NAUKE, null);
            else if (smer == 32)
                jsonPredmeti = sharedPreferences.getString(PREDMETI_KEY_INZENJERSTVO, null);
            else if (smer == 33)
                jsonPredmeti = sharedPreferences.getString(PREDMETI_KEY_DIZAJN, null);
            else if (smer == 34)
                jsonPredmeti = sharedPreferences.getString(PREDMETI_KEY_TEHNOLOGIJE, null);
            predmetiArrayList = gson.fromJson(jsonPredmeti, type);
            Objects.requireNonNull(getSupportActionBar()).setTitle("Predmeti");
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
        nastavniciArrayList = null;
        predmetiArrayList = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        customAdapter.clear();
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
        WholeListActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.getParentActivityIntent();
    }
}
