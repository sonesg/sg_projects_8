package rs.edu.httpsraf.rasporedraf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ActivityListGrupeDanUci extends AppCompatActivity {

    private static final String DATA_SMER = "data_smer";
    private static final String PODPODATAK = "pod_podatak";
    private static final String GRUPA = "grupa";
    private static final String UCIONICA = "ucionica";
    private static final String DAN = "dan";

    private static final String SHARED_PREFERENCES_FILE = "shared_preferences_name";

    private static final String UCIONICE_KEY = "ucionica_key_shared";

    protected ArrayList<String> ucioniceArrayList;

    protected SharedPreferences sharedPreferences;

    protected String[] grupa = new String[]{};

    protected GridView gridView;

    protected String[] grupeNauke = new String[]{"101", "102", "103", "104", "105", "106",
            "201", "202", "203", "204", "301", "302",
            "303", "304", "306", "308", "309", "310", "401", "402"
    };

    protected String[] grupeMreze = new String[]{"101", "102", "103", "104", "105", "106",
            "205", "206", "312", "313", "314", "317", "319", "403"
    };

    protected String[] grupeDizajn = new String[]{"1d1", "2d1", "2d2", "3d1", "3d2", "4d1", "4d2"};

    protected String[] grupeTehnologije = new String[]{"1s1", "1s2", "2s1", "2s2", "3s1", "3s2"};

    protected String[] dani = new String[]{"Ponedeljak", "Utorak", "Sreda", "Četvrtak", "Petak", "Subota"};

    protected int smer;
    protected String podpodatak;
    protected int workWith;

    protected List<String> list = null;

    protected String[] ucionice;

    protected int kolona;

    protected AdapterListSchedule customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_grupe_dan_uci);

        gridView = findViewById(R.id.listViewActivity2);

        if (getActionBar() != null)
            Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            smer = getIntent().getIntExtra(DATA_SMER, 31);
            podpodatak = getIntent().getStringExtra(PODPODATAK);
            switch (podpodatak) {
                case GRUPA:
                    workWith = 1;
                    Objects.requireNonNull(getSupportActionBar()).setTitle("Grupe");
                    break;
                case UCIONICA:
                    workWith = 2;
                    Objects.requireNonNull(getSupportActionBar()).setTitle("Učionice");
                    break;
                case DAN:
                    workWith = 3;
                    Objects.requireNonNull(getSupportActionBar()).setTitle("Dani");
                    break;
            }
        }
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);

        loadData(smer, workWith);

        customAdapter = new AdapterListSchedule(this, R.layout.itemlist, list);


        // ListView Item Click Listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //kolona podatak saljemo dataActivity klasi da bi znali medju kojom kolonom  da trazimo podatak koji je korisnik kliknuo

                if (workWith == 1)//grupe
                    kolona = 0;
                else if (workWith == 2)//ucionice
                    kolona = 2;
                else if (workWith == 3)//dani
                    kolona = 4;

                // ListView Clicked item value
                String itemValue = (String) gridView.getItemAtPosition(position);

                // Show Alert
//                Toast.makeText(ChooseActivity.this,
//                        "Position :" + position + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
//                        .show();
                Intent intent = new Intent(ActivityListGrupeDanUci.this, ScheduleDataActivity.class);
                intent.putExtra("data_key", itemValue);
                intent.putExtra("column_data_key", kolona);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                adapter.notifyDataSetChanged();
                customAdapter.notifyDataSetChanged();

            }

        });

        gridView.setAdapter(customAdapter);
    }

    private void loadData(int smer, int workWith) {
        if (workWith == 1) {

            if (smer == 31) {
                grupa = grupeNauke;
            } else if (smer == 32) {
                grupa = grupeMreze;
            } else if (smer == 33) {
                grupa = grupeDizajn;
            } else if (smer == 34) {
                grupa = grupeTehnologije;
            }
            list = new ArrayList<>(Arrays.asList(grupa));

        } else if (workWith == 2) {
            Gson gson = new Gson();

            String jsonUcionice = sharedPreferences.getString(UCIONICE_KEY, null);
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ucioniceArrayList = gson.fromJson(jsonUcionice, type);

            ucionice = new String[Objects.requireNonNull(ucioniceArrayList).size()];
            ucionice = ucioniceArrayList.toArray(ucionice);
            Arrays.sort(ucionice);
            list = new ArrayList<>(Arrays.asList(ucionice));

        } else if (workWith == 3) {
            list = new ArrayList<>(Arrays.asList(dani));
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
        ucioniceArrayList = null;
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
        ActivityListGrupeDanUci.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.getParentActivityIntent();
    }
}
