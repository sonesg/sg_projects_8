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

public class WholeListActivityChoose extends AppCompatActivity {

    private static final String SHARED_PREFERENCES_FILE = "shared_preferences_name";
    private static final String SHARED_PREFERENCES_FILE_2 = "shared_preferences_name_2";
    private static final String NASTAVNICI_KEY = "nastavnik_key_shared";
    private static final String CELA_LISTA = "cela_list";
    private static final String PREFER_SCHEDULE = "prefer_schedule";

    protected SharedPreferences sharedPreferences;
    protected SharedPreferences sharedPreferences2;
    protected SharedPreferences.Editor editor2;

    protected ListView listView;

    protected String jsonNastavnici;

    protected List<String> list = null;

    protected String[] nastavnici;

    protected String[] grupeSve = new String[]{"101", "102", "103", "104", "105", "106", "1d1", "1s1",
            "1s2", "201", "202", "203", "204", "205", "206", "2d1", "2d2", "2s1", "2s2", "301", "302",
            "303", "304", "306", "307", "308", "309", "310", "312", "313", "314", "317", "319", "3s1", "3s2",
            "401", "402", "403"
    };

    protected ArrayList<String> nastavniciArrayList;

    protected int podatak;

    protected EditText editTextSearch;

    protected AdapterWholeList customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whole_list_choose);

        if (getActionBar() != null)
            Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null)
            podatak = getIntent().getIntExtra(CELA_LISTA, 0);

        listView = findViewById(R.id.whole_list_view_choose);

        editTextSearch = findViewById(R.id.editText_search_choose);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        sharedPreferences2 = getSharedPreferences(SHARED_PREFERENCES_FILE_2, MODE_PRIVATE);
        editor2 = sharedPreferences2.edit();

        loadData(podatak);

        if (podatak == 22) {
            nastavnici = new String[nastavniciArrayList.size()];
            nastavnici = nastavniciArrayList.toArray(nastavnici);
            Arrays.sort(nastavnici);
            list = new ArrayList<>(Arrays.asList(nastavnici));
        } else if (podatak == 23) {
            list = new ArrayList<>(Arrays.asList(grupeSve));
        }

//        adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, android.R.id.text1, list);
        customAdapter = new AdapterWholeList(this, R.layout.itemlist, list);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                editor2.putString(PREFER_SCHEDULE, itemValue);
                editor2.apply();

//                Intent intent = new Intent(WholeListActivityChoose.this,SettingsActivity.class);
//                startActivity(intent);
                SettingsActivity.textViewIzabrano.setText(itemValue);
                finish();
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

    private void loadData(int podatak) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        if (podatak == 22) {
            jsonNastavnici = sharedPreferences.getString(NASTAVNICI_KEY, null);
            Objects.requireNonNull(getSupportActionBar()).setTitle("Svi predavači");
            nastavniciArrayList = gson.fromJson(jsonNastavnici, type);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return id == R.id.app_logo || super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStop() {
        super.onStop();
        nastavniciArrayList = null;
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
        WholeListActivityChoose.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.getParentActivityIntent();
    }
}
