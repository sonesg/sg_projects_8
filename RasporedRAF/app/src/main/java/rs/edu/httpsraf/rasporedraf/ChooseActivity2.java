package rs.edu.httpsraf.rasporedraf;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class ChooseActivity2 extends AppCompatActivity {

    private static final String PODPODATAK = "pod_podatak";
    private static final String GRUPA = "grupa";
    private static final String UCIONICA = "ucionica";
    private static final String DAN = "dan";
    private static final String CELA_LISTA = "cela_lista";
    private static final String DATA_SMER = "data_smer";

    private int smer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose2);

        if (getActionBar() != null)
            Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            smer = getIntent().getIntExtra(DATA_SMER, 31);
            if (smer == 31)
                Objects.requireNonNull(getSupportActionBar()).setTitle("Računarske nauke");
            else if (smer == 32)
                Objects.requireNonNull(getSupportActionBar()).setTitle("Računarsko inženjerstvo");
            else if (smer == 33)
                Objects.requireNonNull(getSupportActionBar()).setTitle("Računarski dizajn");
            else if (smer == 34)
                Objects.requireNonNull(getSupportActionBar()).setTitle("Informacione tehnologije");
        }

        Button grupaBtn = findViewById(R.id.choose_activity_2_grupe);
        Button predavacBtn = findViewById(R.id.choose_activity_2_predavaci);
        Button predmetBtn = findViewById(R.id.choose_activity_2_predmeti);
        Button ucionicaBtn = findViewById(R.id.choose_activity_2_ucionice);
        Button danBtn = findViewById(R.id.choose_activity_2_dani);

        grupaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseActivity2.this, ActivityListGrupeDanUci.class);
                intent.putExtra(DATA_SMER, smer);
                intent.putExtra(PODPODATAK, GRUPA);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        ucionicaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseActivity2.this, ActivityListGrupeDanUci.class);
                intent.putExtra(DATA_SMER, smer);
                intent.putExtra(PODPODATAK, UCIONICA);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        danBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseActivity2.this, ActivityListGrupeDanUci.class);
                intent.putExtra(DATA_SMER, smer);
                intent.putExtra(PODPODATAK, DAN);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        //TODO tek treba to da odradim, pokrece se activity WholeListActivity
        predmetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseActivity2.this, WholeListActivity.class);
                intent.putExtra(CELA_LISTA, 24);
                intent.putExtra(DATA_SMER, smer);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        predavacBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseActivity2.this, WholeListActivity.class);
                intent.putExtra(CELA_LISTA, 23);
                intent.putExtra(DATA_SMER, smer);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu_1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(ChooseActivity2.this, SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        } else if (id == R.id.sviPredmeti) {
            Intent intent = new Intent(ChooseActivity2.this, WholeListActivity.class);
            intent.putExtra(CELA_LISTA, 21);
            intent.putExtra(DATA_SMER, 0);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (id == R.id.sviPredavaci) {
            Intent intent = new Intent(ChooseActivity2.this, WholeListActivity.class);
            intent.putExtra(CELA_LISTA, 22);
            intent.putExtra(DATA_SMER, 0);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (id == R.id.app_logo) {
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
        ChooseActivity2.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return super.getParentActivityIntent();
    }
}
