package rs.edu.httpsraf.rasporedraf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCES_FILE_2 = "shared_preferences_name_2";
    private static final String PREFER_SCHEDULE = "prefer_schedule";
    private static final String CELA_LISTA = "cela_list";

    protected CircleImageView btnGrupa, btnPredavc;
    @SuppressLint("StaticFieldLeak")
    protected static TextView textViewIzabrano;

    protected SharedPreferences sharedPreferences;

    protected String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getActionBar() != null)
            Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Podešavanja");

        btnGrupa = findViewById(R.id.buttonGrupe);
        btnPredavc = findViewById(R.id.buttonPredavaci);

        textViewIzabrano = findViewById(R.id.textIzabrano);

        btnPredavc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, WholeListActivityChoose.class);
                intent.putExtra(CELA_LISTA, 22);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnGrupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, WholeListActivityChoose.class);
                intent.putExtra(CELA_LISTA, 23);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_2, MODE_PRIVATE);
        text = sharedPreferences.getString(PREFER_SCHEDULE, "Nije izabrana vrednost");

        textViewIzabrano.setText(text);
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        finish();
        SettingsActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.getParentActivityIntent();
    }
}
