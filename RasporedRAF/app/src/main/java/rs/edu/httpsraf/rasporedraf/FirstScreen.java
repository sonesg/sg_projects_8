package rs.edu.httpsraf.rasporedraf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import de.hdodenhof.circleimageview.CircleImageView;
import rs.edu.httpsraf.rasporedraf.data.SubjectContract;

public class FirstScreen extends AppCompatActivity {

    private static final String JOB_TAG = "OUR_JOB_TAG";
    private static final String SHARED_PREFERENCES_FILE = "shared_preferences_name";
    private static final String SHARED_PREFERENCES_FILE_2 = "shared_preferences_name_2";
    private static final String KEY_PREFS_DATA = "loading_data";
    private static final String KEY_PREFS_DATA_CHECK = "loading_data_check";
    private static final String KEY_PREFS = "firstRun";
    private static final String CELA_LISTA = "cela_lista";
    private static final String PREFER_SCHEDULE = "prefer_schedule";
    private static final String DATA_SMER = "data_smer";

    private final int INTERVAL = 1000 * 60 * 60 * 12; //jedan dan

    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferences2;
    protected boolean finish;

    protected FloatingActionButton fab;

    protected FirebaseJobDispatcher firebaseJobDispatcher;

    private Toast toast;

    @SuppressLint("StaticFieldLeak")
    protected static CircleImageView btnNauke, btnMreze, btnTehnologije, btnDizajn;

    protected TextView txtNauke, txtMreze, txtTehnologije, txtDizajn;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_screen);

//        Objects.requireNonNull(getSupportActionBar()).setTitle("OVO ONO");

        btnNauke = findViewById(R.id.buttonNauke);
        btnMreze = findViewById(R.id.buttonMreze);
        btnTehnologije = findViewById(R.id.buttonTehnologija);
        btnDizajn = findViewById(R.id.buttonDizajn);

        txtNauke = findViewById(R.id.textNauke);
        txtMreze = findViewById(R.id.textMreze);
        txtTehnologije = findViewById(R.id.textTehnoligija);
        txtDizajn = findViewById(R.id.textDizajn);

        txtNauke.setSingleLine(false);
        txtNauke.setText("Računarske \n" + "nauke");
        txtMreze.setSingleLine(false);
        txtMreze.setText("Računarsko \n" + "inženjerstvo");
        txtTehnologije.setSingleLine(false);
        txtTehnologije.setText("Informacione \n" + "tehnologije");
        txtDizajn.setSingleLine(false);
        txtDizajn.setText("Računarski \n" + "dizajn");


        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);
        sharedPreferences2 = getSharedPreferences(SHARED_PREFERENCES_FILE_2, MODE_PRIVATE);

        boolean firstRun = sharedPreferences2.getBoolean(KEY_PREFS, true);

        firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));

        if (firstRun) {
            Intent i = new Intent(FirstScreen.this, ScheduleService.class);
            this.startService(i);
//            start job scheduler when app is launch
            if (Build.VERSION.SDK_INT >= 21) {
                startJob();
            }
        }


        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = sharedPreferences2.getString(PREFER_SCHEDULE, "");
                int kolona = 1;
                if (data.contains("1") || data.contains("2") || data.contains("3") || data.contains("4"))
                    kolona = 0;
                if (data.equals("")) {
                    Snackbar.make(view, "Morate izabrati grupu ili ime predavača u opciji podešavanja", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Intent intent = new Intent(FirstScreen.this, ScheduleDataActivity.class);
                    intent.putExtra("data_key", data);
                    intent.putExtra("column_data_key", kolona);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

        btnNauke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish = sharedPreferences.getBoolean(KEY_PREFS_DATA, false);
                if (finish) {
                    Intent intent = new Intent(FirstScreen.this, ChooseActivity2.class);
                    intent.putExtra(DATA_SMER, 31);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    deleteDataAll();
                    if (toast != null)
                        toast.cancel();
                    toast = Toast.makeText(FirstScreen.this, "Proverite internet", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent i = new Intent(FirstScreen.this, ScheduleService.class);
                    FirstScreen.this.startService(i);
                    //TODO da se pojavi button za ucitavanje podataka sa neta,koji ce pokrenuti MyService.class
                }
            }
        });

        btnMreze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish = sharedPreferences.getBoolean(KEY_PREFS_DATA, false);
//                check = sharedPreferences.getBoolean(KEY_PREFS_DATA_CHECK, false);
                if (finish) {
                    Intent intent = new Intent(FirstScreen.this, ChooseActivity2.class);
                    intent.putExtra(DATA_SMER, 32);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    deleteDataAll();
                    if (toast != null)
                        toast.cancel();
                    toast = Toast.makeText(FirstScreen.this, "Proverite internet", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent i = new Intent(FirstScreen.this, ScheduleService.class);
                    FirstScreen.this.startService(i);
                    //TODO da se pojavi button za ucitavanje podataka sa neta,koji ce pokrenuti MyService.class
//                    if(check){
//                        btnNauke.setEnabled(true);
//                        btnMreze.setEnabled(true);
//                        btnDizajn.setEnabled(true);
//                        btnTehnologije.setEnabled(true);
//                    }
                }
            }
        });

        btnDizajn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish = sharedPreferences.getBoolean(KEY_PREFS_DATA, false);
                if (finish) {
//                    Log.i("MIMIVAU", "usao");
                    Intent intent = new Intent(FirstScreen.this, ChooseActivity2.class);
                    intent.putExtra(DATA_SMER, 33);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
//                    Log.i("MIMIVAU", "usao1");
                    deleteDataAll();
//                    Log.i("MIMIVAU", "usao2");
                    if (toast != null)
                        toast.cancel();
                    toast = Toast.makeText(FirstScreen.this, "Proverite internet", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent i = new Intent(FirstScreen.this, ScheduleService.class);
                    FirstScreen.this.startService(i);
//                    Log.i("MIMIVAU", "usao3");
                    //TODO da se pojavi button za ucitavanje podataka sa neta,koji ce pokrenuti MyService.class
                }
            }
        });

        btnTehnologije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish = sharedPreferences.getBoolean(KEY_PREFS_DATA, false);
                if (finish) {
                    Intent intent = new Intent(FirstScreen.this, ChooseActivity2.class);
                    intent.putExtra(DATA_SMER, 34);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    deleteDataAll();
                    if (toast != null)
                        toast.cancel();
                    toast = Toast.makeText(FirstScreen.this, "Proverite internet", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent i = new Intent(FirstScreen.this, ScheduleService.class);
                    FirstScreen.this.startService(i);
                    //TODO da se pojavi button za ucitavanje podataka sa neta,koji ce pokrenuti MyService.class
                }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putBoolean(KEY_PREFS, false);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        finish = sharedPreferences.getBoolean(KEY_PREFS_DATA, false);
//        check = sharedPreferences.getBoolean(KEY_PREFS_DATA_CHECK, false);
//        if(check && finish){
//            btnNauke.setEnabled(true);
//            btnMreze.setEnabled(true);
//            btnDizajn.setEnabled(true);
//            btnTehnologije.setEnabled(true);
//            Intent intent = new Intent(FirstScreen.this, ChooseActivity.class);
//            intent.putExtra("data_smer", 31);
//            startActivity(intent);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish = sharedPreferences.getBoolean(KEY_PREFS_DATA, false);
            if (finish) {
                Intent intent = new Intent(FirstScreen.this, SettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                if (toast != null)
                    toast.cancel();
                toast = Toast.makeText(this, "Proverite internet", Toast.LENGTH_SHORT);
                toast.show();
            }
            return true;
        } else if (id == R.id.sviPredmeti) {
            finish = sharedPreferences.getBoolean(KEY_PREFS_DATA, false);
            if (finish) {
                Intent intent = new Intent(FirstScreen.this, WholeListActivity.class);
                intent.putExtra(CELA_LISTA, 21);
                intent.putExtra(DATA_SMER, 0);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                if (toast != null)
                    toast.cancel();
                toast = Toast.makeText(this, "Proverite internet", Toast.LENGTH_SHORT);
                toast.show();
            }
            return true;
        } else if (id == R.id.sviPredavaci) {
            finish = sharedPreferences.getBoolean(KEY_PREFS_DATA, false);
            if (finish) {
                Intent intent = new Intent(FirstScreen.this, WholeListActivity.class);
                intent.putExtra(CELA_LISTA, 22);
                intent.putExtra(DATA_SMER, 0);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                if (toast != null)
                    toast.cancel();
                toast = Toast.makeText(this, "Proverite internet", Toast.LENGTH_SHORT);
                toast.show();
            }
            return true;
        } else if (id == R.id.app_logo) {
            return true;
            //Ispiti
        } else if (id == R.id.exams) {
            finish = sharedPreferences.getBoolean(KEY_PREFS_DATA, false);
            if (finish) {
                Intent i = new Intent(FirstScreen.this, ExamDataActivity.class);
                i.putExtra("tip_podatka", false);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                if (toast != null)
                    toast.cancel();
                toast = Toast.makeText(this, "Proverite internet", Toast.LENGTH_SHORT);
                toast.show();
            }
            //Kolokvijumi
        } else if (id == R.id.exams2) {
            finish = sharedPreferences.getBoolean(KEY_PREFS_DATA, false);
            if (finish) {
                Intent i = new Intent(FirstScreen.this, ExamDataActivity.class);
                i.putExtra("tip_podatka", true);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                if (toast != null)
                    toast.cancel();
                toast = Toast.makeText(this, "Proverite internet", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else if (id == R.id.osvezi){
            finish = sharedPreferences.getBoolean(KEY_PREFS_DATA, false);
            if (finish) {
                Intent i = new Intent(FirstScreen.this, ScheduleService.class);
                FirstScreen.this.startService(i);
            } else {
                if (toast != null)
                    toast.cancel();
                toast = Toast.makeText(this, "Proverite internet", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteDataAll() {
        // Only perform the delete if this is an existing pet.
        if (SubjectContract.SubjectEntry.CONTENT_URI != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(SubjectContract.SubjectEntry.CONTENT_URI, null, null);
            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
//                Toast.makeText(this, "error with deleting data",
//                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
//                Toast.makeText(this, "all data are deleted",
//                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startJob() {
        Log.i("JOB", "STARTOVAN JE");
        Job job = firebaseJobDispatcher.newJobBuilder()
                .setService(JobService.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTag(JOB_TAG)
                .setTrigger(Trigger.executionWindow(INTERVAL, INTERVAL))
//                .setTrigger(Trigger.executionWindow(0, 0))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setReplaceCurrent(false)
                .setConstraints(Constraint.ON_UNMETERED_NETWORK)
                .build();
        firebaseJobDispatcher.mustSchedule(job);
    }

}
