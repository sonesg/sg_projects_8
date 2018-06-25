package rs.edu.httpsraf.rasporedraf;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;

import rs.edu.httpsraf.rasporedraf.data.SubjectContract;


public class JobService extends com.firebase.jobdispatcher.JobService {
    private static final String TAG = "JobService";


    ReadJSON readJSON;

//    private static final String JSON_API = "https://rfidis.raf.edu.rs/raspored/json.php";

    private static final String JSON_API = "https://api.raf.ninja/v1/classes";
    private static final String API_KEY = "bJoZoihORa9jzGlHg698";

    private static final String SHARED_PREFERENCES_FILE = "shared_preferences_name";
    private static final String NASTAVNICI_KEY = "nastavnik_key_shared";
    private static final String PREDMETI_KEY = "predmet_key_shared";
    private static final String UCIONICE_KEY = "ucionica_key_shared";

    private static final String NASTAVNICI_KEY_DIZAJN = "nastavnik_key_shared_diz";
    private static final String PREDMETI_KEY_DIZAJN = "predmet_key_shared_diz";
    private static final String NASTAVNICI_KEY_NAUKE = "nastavnik_key_shared_nauk";
    private static final String PREDMETI_KEY_NAUKE = "predmet_key_shared_nauk";
    private static final String NASTAVNICI_KEY_INZENJERSTVO = "nastavnik_key_shared_mreze";
    private static final String PREDMETI_KEY_INZENJERSTVO = "predmet_key_shared_mreze";
    private static final String NASTAVNICI_KEY_TEHNOLOGIJE = "nastavnik_key_shared_tehn";
    private static final String PREDMETI_KEY_TEHNOLOGIJE = "predmet_key_shared_tehn";

    private static final String PRVA_GOD_NAUKE_MREZE = "god_prva_obe";
    private static final String DRUGA_GOD_NAUKE = "god_druga_nauke";
    private static final String TRECA_GOD_NAUKE = "god_treca_nauke";
    private static final String CETVRTA_GOD_NAUKE = "god_cetvrta_nauke";

    private static final String DRUGA_GOD_MREZE = "god_druga_mreze";
    private static final String TRECA_GOD_MREZE = "god_treca_mreze";
    private static final String CETVRTA_GOD_MREZE = "god_cetvrta_mreze";

    private static final String PRVA_GOD_DIZAJN = "god_prva_dizajn";
    private static final String DRUGA_GOD_DIZAJN = "god_druga_dizajn";

    private static final String DRUGA_GOD_TEHNO = "god_druga_tehno";
    private static final String TRECA_GOD_TEHNO = "god_treca_tehno";
    private static final String PRVA_GOD_TEHNO = "god_prva_tehno";

    private static final String KEY_PREFS_DATA = "loading_data";

    private ArrayList<ContentValues> vrednosti;

    protected LinkedHashSet<String> nastavnici;
    protected LinkedHashSet<String> predmeti;
    protected LinkedHashSet<String> ucionice;

    protected LinkedHashSet<String> predmetiPrvaGodNaukeMreze;
    protected LinkedHashSet<String> predmetiDrugaGodNauke;
    protected LinkedHashSet<String> predmetiTrecaGodNauke;
    protected LinkedHashSet<String> predmetiCetvrtaGodNauke;
    protected LinkedHashSet<String> predmetiDrugaGodMreze;
    protected LinkedHashSet<String> predmetiTrecaGodMreze;
    protected LinkedHashSet<String> predmetiCetvrtaGodMreze;

    protected LinkedHashSet<String> predmetiPrvaGodDizajn;
    protected LinkedHashSet<String> predmetiDrugaGodDizajn;
//    protected LinkedHashSet<String> predmetiTrecaGodDizajn;
//    protected LinkedHashSet<String> predmetiCetvrtaGodDizajn;

    protected LinkedHashSet<String> predmetiPrvaGodTehno;
    protected LinkedHashSet<String> predmetiDrugaGodTehno;
    protected LinkedHashSet<String> predmetiTrecaGodTehno;


    protected LinkedHashSet<String> nastavniciNauke;
    protected LinkedHashSet<String> predmetiNauke;

    protected LinkedHashSet<String> nastavniciInzenjerstvo;
    protected LinkedHashSet<String> predmetiInzenjerstvo;

    protected LinkedHashSet<String> nastavniciDizajn;
    protected LinkedHashSet<String> predmetiDizajn;

    protected LinkedHashSet<String> nastavniciTehnologije;
    protected LinkedHashSet<String> predmetiTehnologije;


    protected ArrayList<String> predmetiPrvaGodNaukeMrezeArrayList;
    protected ArrayList<String> predmetiDrugaGodNaukeArrayList;
    protected ArrayList<String> predmetiTrecaGodNaukeArrayList;
    protected ArrayList<String> predmetiCetvrtaGodNaukeArrayList;
    protected ArrayList<String> predmetiDrugaGodMrezeArrayList;
    protected ArrayList<String> predmetiTrecaGodMrezeArrayList;
    protected ArrayList<String> predmetiCetvrtaGodMrezeArrayList;

    protected ArrayList<String> predmetiPrvaGodDizajnArrayList;
    protected ArrayList<String> predmetiDrugaGodDizajnArrayList;
    protected ArrayList<String> predmetiTrecaGodDizajnArrayList;
    protected ArrayList<String> predmetiCetvrtaGodDizajnArrayList;

    protected ArrayList<String> predmetiPrvaGodTehnoArrayList;
    protected ArrayList<String> predmetiDrugaGodTehnoArrayList;
    protected ArrayList<String> predmetiTrecaGodTehnoArrayList;


    protected ArrayList<String> nastavniciArrayList;
    protected ArrayList<String> predmetiArrayList;
    protected ArrayList<String> ucioniceArrayList;

    protected ArrayList<String> nastavniciArrayListNauke;
    protected ArrayList<String> predmetiArrayListNauke;

    protected ArrayList<String> nastavniciArrayListInzenjerstvo;
    protected ArrayList<String> predmetiArrayListInzenjerstvo;

    protected ArrayList<String> nastavniciArrayListDizajn;
    protected ArrayList<String> predmetiArrayListDizajn;

    protected ArrayList<String> nastavniciArrayListTehnologije;
    protected ArrayList<String> predmetiArrayListTehnologije;


    protected String[] predmetiPrvaGodNaukeMrezeArray;
    protected String[] predmetiDrugaGodNaukeArray;
    protected String[] predmetiTrecaGodNaukeArray;
    protected String[] predmetiCetvrtaGodNaukeArray;
    protected String[] predmetiDrugaGodMrezeArray;
    protected String[] predmetiTrecaGodMrezeArray;
    protected String[] predmetiCetvrtaGodMrezeArray;

    protected String[] predmetiPrvaGodDizajnArray;
    protected String[] predmetiDrugaGodDizajnArray;
    protected String[] predmetiTrecaGodDizajnArray;
    protected String[] predmetiCetvrtaGodDizajnArray;

    protected String[] predmetiPrvaGodTehnoArray;
    protected String[] predmetiDrugaGodTehnoArray;
    protected String[] predmetiTrecaGodTehnoArray;


    protected String[] nastvniciArray;
    protected String[] predmetiArray;
    protected String[] ucioniceArray;

    protected String[] nastavniciArrayNauke;
    protected String[] predmetiArrayNauke;

    protected String[] nastavniciArrayInzenjerstvo;
    protected String[] predmetiArrayInzenjerstvo;

    protected String[] nastavniciArrayDizajn;
    protected String[] predmetiArrayDizajn;

    protected String[] nastavniciArrayTehnologije;
    protected String[] predmetiArrayTehnologije;

    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    protected Gson gson;

    protected int dan = -1;

    protected String termin;


    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final com.firebase.jobdispatcher.JobParameters job) {


//        backgroundTask = new BackgroundTask(){
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                Toast.makeText(getApplicationContext(), "Message from background task" + " " + s, Toast.LENGTH_SHORT).show();
//                Log.d("PONOVO", "NEEEEEEEEEEEEEEEEE");
//                jobFinished(job, false);
//            }
//        };
//        backgroundTask.execute();

        readJSON = new ReadJSON() {
            @Override
            protected void onPostExecute(String content) {
                super.onPostExecute(content);

                Log.i("START", "NOW_JOB_SERVICE");

                try {
//                      JSONObject jsonObject = new JSONObject(content);
//                        JSONArray jsonArray = new JSONArray(content);
                    JSONObject jsonObject = new JSONObject(content);
                    JSONArray jsonArray = jsonObject.getJSONArray("schedule");

                    nastavnici = new LinkedHashSet<>();
                    predmeti = new LinkedHashSet<>();
                    ucionice = new LinkedHashSet<>();

                    nastavniciNauke = new LinkedHashSet<>();
                    predmetiNauke = new LinkedHashSet<>();

                    nastavniciInzenjerstvo = new LinkedHashSet<>();
                    predmetiInzenjerstvo = new LinkedHashSet<>();

                    nastavniciDizajn = new LinkedHashSet<>();
                    predmetiDizajn = new LinkedHashSet<>();

                    nastavniciTehnologije = new LinkedHashSet<>();
                    predmetiTehnologije = new LinkedHashSet<>();

                    predmetiPrvaGodNaukeMreze = new LinkedHashSet<>();
                    predmetiDrugaGodNauke = new LinkedHashSet<>();
                    predmetiTrecaGodNauke = new LinkedHashSet<>();
                    predmetiCetvrtaGodNauke = new LinkedHashSet<>();
                    predmetiDrugaGodMreze = new LinkedHashSet<>();
                    predmetiTrecaGodMreze = new LinkedHashSet<>();
                    predmetiCetvrtaGodMreze = new LinkedHashSet<>();

                    predmetiPrvaGodDizajn = new LinkedHashSet<>();
                    predmetiDrugaGodDizajn = new LinkedHashSet<>();
//                        predmetiTrecaGodDizajn = new LinkedHashSet<>();
//                        predmetiCetvrtaGodDizajn = new LinkedHashSet<>();

                    predmetiPrvaGodTehno = new LinkedHashSet<>();
                    predmetiDrugaGodTehno = new LinkedHashSet<>();
                    predmetiTrecaGodTehno = new LinkedHashSet<>();

                    sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);//MODE_PRIVATE = 0
                    editor = sharedPreferences.edit();
                    gson = new Gson();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject productObject = jsonArray.getJSONObject(i);

                        String predmet = productObject.getString("class_name");
                        String nastavnik = productObject.getString("lecturer");
                        String grupa = productObject.getString("student_groups");

                        // Create a ContentValues object where column names are the keys,
                        // and subject attributes from the editor are the values.

                        //Change values of column dan
                        switch (productObject.getString("day_of_week")) {
                            case "PON":
                                dan = 1;
                                break;
                            case "UTO":
                                dan = 2;
                                break;
                            case "SRE":
                                dan = 3;
                                break;
                            case "ČET":
                                dan = 4;
                                break;
                            case "PET":
                                dan = 5;
                                break;
                            case "SUB":
                                dan = 6;
                                break;
                            default:
                                dan = -1;
                                break;
                        }

                        termin = productObject.getString("time");
                        if (termin.charAt(1) == ':') {
                            termin = String.format("%s%s", '0', termin);
                        }


                        ContentValues values = new ContentValues();
                        values.put(SubjectContract.SubjectEntry.COLUMN_PREDMET, predmet);
                        values.put(SubjectContract.SubjectEntry.COLUMN_TIP, productObject.getString("type"));
                        values.put(SubjectContract.SubjectEntry.COLUMN_NASTAVNIK, nastavnik);
                        values.put(SubjectContract.SubjectEntry.COLUMN_GRUPE, grupa);
                        values.put(SubjectContract.SubjectEntry.COLUMN_DAN, dan);
                        values.put(SubjectContract.SubjectEntry.COLUMN_TERMIN, termin);
                        values.put(SubjectContract.SubjectEntry.COLUMN_UCIONICA, productObject.getString("classroom"));

                        if (grupa.contains("1d1") || grupa.contains("2d1") || grupa.contains("2d2") || grupa.contains("3d1")
                                || grupa.contains("3d2") || grupa.contains("4d1") || grupa.contains("4d2")) {
                            if (grupa.contains("1d1"))
                                predmetiPrvaGodDizajn.add(predmet);
                            else
                                predmetiDrugaGodDizajn.add(predmet);
//                                else if(grupa.contains("3d1") || grupa.contains("3d2"))
//                                    predmetiTrecaGodDizajn.add(predmet);
//                                else
//                                    predmetiCetvrtaGodDizajn.add(predmet);
                            nastavniciDizajn.add(nastavnik);
                            predmetiDizajn.add(predmet);
                        }
                        if (grupa.contains("1s1") || grupa.contains("1s2") || grupa.contains("2s1") || grupa.contains("2s2")
                                || grupa.contains("3s1") || grupa.contains("3s2")) {
                            if (grupa.contains("1s1") || grupa.contains("1s2"))
                                predmetiPrvaGodTehno.add(predmet);
                            else if (grupa.contains("2s1") || grupa.contains("2s2"))
                                predmetiDrugaGodTehno.add(predmet);
                            else
                                predmetiTrecaGodTehno.add(predmet);
                            nastavniciTehnologije.add(nastavnik);
                            predmetiTehnologije.add(predmet);
                        }
                        if (grupa.contains("101") || grupa.contains("102") || grupa.contains("103") ||
                                grupa.contains("104") || grupa.contains("105") || grupa.contains("106") ||
                                grupa.contains("201") || grupa.contains("202") || grupa.contains("203") ||
                                grupa.contains("204") || grupa.contains("301") || grupa.contains("302") ||
                                grupa.contains("303") || grupa.contains("304") || grupa.contains("306") ||
                                grupa.contains("308") || grupa.contains("309") || grupa.contains("401") ||
                                grupa.contains("402")) {
                            if (grupa.contains("101") || grupa.contains("102") || grupa.contains("103") ||
                                    grupa.contains("104") || grupa.contains("105") || grupa.contains("106")) {
                                predmetiPrvaGodNaukeMreze.add(predmet);
                            } else if (grupa.contains("201") || grupa.contains("202") || grupa.contains("203") ||
                                    grupa.contains("204")) {
                                predmetiDrugaGodNauke.add(predmet);
                            } else if (grupa.contains("401") ||
                                    grupa.contains("402")) {
                                predmetiCetvrtaGodNauke.add(predmet);
                            } else {
                                predmetiTrecaGodNauke.add(predmet);
                            }
                            nastavniciNauke.add(nastavnik);
                            predmetiNauke.add(predmet);
                        }
                        if (grupa.contains("101") || grupa.contains("102") || grupa.contains("103") ||
                                grupa.contains("104") || grupa.contains("105") || grupa.contains("106") ||
                                grupa.contains("205") || grupa.contains("206") || grupa.contains("312") ||
                                grupa.contains("313") || grupa.contains("314") || grupa.contains("317") ||
                                grupa.contains("319") || grupa.contains("403")) {
                            if (grupa.contains("205") || grupa.contains("206"))
                                predmetiDrugaGodMreze.add(predmet);
                            else if (grupa.contains("403"))
                                predmetiCetvrtaGodMreze.add(predmet);
                            else
                                predmetiTrecaGodMreze.add(predmet);
                            nastavniciInzenjerstvo.add(nastavnik);
                            predmetiInzenjerstvo.add(predmet);
                        }

                        nastavnici.add(nastavnik);
                        predmeti.add(predmet);
                        ucionice.add(productObject.getString("classroom"));


                        //stavljam vrednosti koje treba da idu u bazu podataka prvo u arrayListu
                        //razlog toga je ukoliko internet nestane u tom intervalu od neke 2 sekunde koliko treba da se izvrsi sve ovo
                        //a mi pre toga obrisemo bazu,bice lose,nece biti podataka.Zbog toga brisemo tek u final sekciji
                        vrednosti.add(values);
                        // This is a NEW subject, so insert a new subject into the provider,
                        // returning the content URI for the new subject.
//                        Uri newUri = getContentResolver().insert(SubjectEntry.CONTENT_URI, values);
                        //getContentResolver().insert(SubjectContract.SubjectEntry.CONTENT_URI, values);


                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                } finally {
                    if (CheckNetwork.isInternetAvailable(JobService.this)) {
                        deleteDataAll();
                        Log.i("START", "START_FINAL_JOB_SERVICE");
                        for (ContentValues value : vrednosti) {
                            getContentResolver().insert(SubjectContract.SubjectEntry.CONTENT_URI, value);
                        }

                        if (nastavnici != null && nastavnici.size() > 0
                                && predmeti != null && predmeti.size() > 0 &&
                                ucionice != null && ucionice.size() > 0) {

                            //prvo kreiramo obican niz stringova od LinkedHashListe pa onda tog niza srigova kreiramo ArrayListu
                            //koju posle stavljamo u SharedPreferences fajl

                            //svi nastavnici
                            nastvniciArray = new String[nastavnici.size()];
                            nastvniciArray = nastavnici.toArray(nastvniciArray);
                            nastavniciArrayList = new ArrayList<>(Arrays.asList(nastvniciArray));

                            //svi predmeti
                            predmetiArray = new String[predmeti.size()];
                            predmetiArray = predmeti.toArray(predmetiArray);
                            predmetiArrayList = new ArrayList<>(Arrays.asList(predmetiArray));

                            //sve ucionice
                            ucioniceArray = new String[ucionice.size()];
                            ucioniceArray = ucionice.toArray(ucioniceArray);
                            ucioniceArrayList = new ArrayList<>(Arrays.asList(ucioniceArray));


                            //Dizajn
                            nastavniciArrayDizajn = new String[nastavniciDizajn.size()];
                            nastavniciArrayDizajn = nastavniciDizajn.toArray(nastavniciArrayDizajn);
                            nastavniciArrayListDizajn = new ArrayList<>(Arrays.asList(nastavniciArrayDizajn));

                            predmetiArrayDizajn = new String[predmetiDizajn.size()];
                            predmetiArrayDizajn = predmetiDizajn.toArray(predmetiArrayDizajn);
                            predmetiArrayListDizajn = new ArrayList<>(Arrays.asList(predmetiArrayDizajn));

                            //Tehnologije
                            nastavniciArrayTehnologije = new String[nastavniciTehnologije.size()];
                            nastavniciArrayTehnologije = nastavniciTehnologije.toArray(nastavniciArrayTehnologije);
                            nastavniciArrayListTehnologije = new ArrayList<>(Arrays.asList(nastavniciArrayTehnologije));

                            predmetiArrayTehnologije = new String[predmetiTehnologije.size()];
                            predmetiArrayTehnologije = predmetiTehnologije.toArray(predmetiArrayTehnologije);
                            predmetiArrayListTehnologije = new ArrayList<>(Arrays.asList(predmetiArrayTehnologije));

                            //Nauke
                            nastavniciArrayNauke = new String[nastavniciNauke.size()];
                            nastavniciArrayNauke = nastavniciNauke.toArray(nastavniciArrayNauke);
                            nastavniciArrayListNauke = new ArrayList<>(Arrays.asList(nastavniciArrayNauke));

                            predmetiArrayNauke = new String[predmetiNauke.size()];
                            predmetiArrayNauke = predmetiNauke.toArray(predmetiArrayNauke);
                            predmetiArrayListNauke = new ArrayList<>(Arrays.asList(predmetiArrayNauke));

                            //Mreze
                            nastavniciArrayInzenjerstvo = new String[nastavniciInzenjerstvo.size()];
                            nastavniciArrayInzenjerstvo = nastavniciInzenjerstvo.toArray(nastavniciArrayInzenjerstvo);
                            nastavniciArrayListInzenjerstvo = new ArrayList<>(Arrays.asList(nastavniciArrayInzenjerstvo));

                            predmetiArrayInzenjerstvo = new String[predmetiInzenjerstvo.size()];
                            predmetiArrayInzenjerstvo = predmetiInzenjerstvo.toArray(predmetiArrayInzenjerstvo);
                            predmetiArrayListInzenjerstvo = new ArrayList<>(Arrays.asList(predmetiArrayInzenjerstvo));

                            //PrvaGodMrezeNauke
                            predmetiPrvaGodNaukeMrezeArray = new String[predmetiPrvaGodNaukeMreze.size()];
                            predmetiPrvaGodNaukeMrezeArray = predmetiPrvaGodNaukeMreze.toArray(predmetiPrvaGodNaukeMrezeArray);
                            predmetiPrvaGodNaukeMrezeArrayList = new ArrayList<>(Arrays.asList(predmetiPrvaGodNaukeMrezeArray));
                            //DrugaGodNauke
                            predmetiDrugaGodNaukeArray = new String[predmetiDrugaGodNauke.size()];
                            predmetiDrugaGodNaukeArray = predmetiDrugaGodNauke.toArray(predmetiDrugaGodNaukeArray);
                            predmetiDrugaGodNaukeArrayList = new ArrayList<>(Arrays.asList(predmetiDrugaGodNaukeArray));
                            //TrecaGodNauke
                            predmetiTrecaGodNaukeArray = new String[predmetiTrecaGodNauke.size()];
                            predmetiTrecaGodNaukeArray = predmetiTrecaGodNauke.toArray(predmetiTrecaGodNaukeArray);
                            predmetiTrecaGodNaukeArrayList = new ArrayList<>(Arrays.asList(predmetiTrecaGodNaukeArray));
                            //CetGodNauke
                            predmetiCetvrtaGodNaukeArray = new String[predmetiCetvrtaGodNauke.size()];
                            predmetiCetvrtaGodNaukeArray = predmetiCetvrtaGodNauke.toArray(predmetiCetvrtaGodNaukeArray);
                            predmetiCetvrtaGodNaukeArrayList = new ArrayList<>(Arrays.asList(predmetiCetvrtaGodNaukeArray));

                            //DrugaGodMreze
                            predmetiDrugaGodMrezeArray = new String[predmetiDrugaGodMreze.size()];
                            predmetiDrugaGodMrezeArray = predmetiDrugaGodMreze.toArray(predmetiDrugaGodMrezeArray);
                            predmetiDrugaGodMrezeArrayList = new ArrayList<>(Arrays.asList(predmetiDrugaGodMrezeArray));
                            //TrecaGodMreze
                            predmetiTrecaGodMrezeArray = new String[predmetiTrecaGodMreze.size()];
                            predmetiTrecaGodMrezeArray = predmetiTrecaGodMreze.toArray(predmetiTrecaGodMrezeArray);
                            predmetiTrecaGodMrezeArrayList = new ArrayList<>(Arrays.asList(predmetiTrecaGodMrezeArray));
                            //CetGodMreze
                            predmetiCetvrtaGodMrezeArray = new String[predmetiCetvrtaGodMreze.size()];
                            predmetiCetvrtaGodMrezeArray = predmetiCetvrtaGodMreze.toArray(predmetiCetvrtaGodMrezeArray);
                            predmetiCetvrtaGodMrezeArrayList = new ArrayList<>(Arrays.asList(predmetiCetvrtaGodMrezeArray));

                            //PrvaGodDizajn
                            predmetiPrvaGodDizajnArray = new String[predmetiPrvaGodDizajn.size()];
                            predmetiPrvaGodDizajnArray = predmetiPrvaGodDizajn.toArray(predmetiPrvaGodDizajnArray);
                            predmetiPrvaGodDizajnArrayList = new ArrayList<>(Arrays.asList(predmetiPrvaGodDizajnArray));
                            //DrugaGodDizajn
                            predmetiDrugaGodDizajnArray = new String[predmetiDrugaGodDizajn.size()];
                            predmetiDrugaGodDizajnArray = predmetiDrugaGodDizajn.toArray(predmetiDrugaGodDizajnArray);
                            predmetiDrugaGodDizajnArrayList = new ArrayList<>(Arrays.asList(predmetiDrugaGodDizajnArray));
                            //TrecaGodDizajn
//                                predmetiPrvaGodDizajnArray= new String[predmetiPrvaGodDizajn.size()];
//                                predmetiPrvaGodDizajnArray = predmetiPrvaGodDizajn.toArray(predmetiPrvaGodDizajnArray);
//                                predmetiPrvaGodDizajnArrayList = new ArrayList<>(Arrays.asList(predmetiPrvaGodDizajnArray));
                            //CetGodDizajn

                            //PrvaGodTehno
                            predmetiPrvaGodTehnoArray = new String[predmetiPrvaGodTehno.size()];
                            predmetiPrvaGodTehnoArray = predmetiPrvaGodTehno.toArray(predmetiPrvaGodTehnoArray);
                            predmetiPrvaGodTehnoArrayList = new ArrayList<>(Arrays.asList(predmetiPrvaGodTehnoArray));
                            //DrugaGodTehno
                            predmetiDrugaGodTehnoArray = new String[predmetiDrugaGodTehno.size()];
                            predmetiDrugaGodTehnoArray = predmetiDrugaGodTehno.toArray(predmetiDrugaGodTehnoArray);
                            predmetiDrugaGodTehnoArrayList = new ArrayList<>(Arrays.asList(predmetiDrugaGodTehnoArray));
                            //TrecaGodTehno
                            predmetiTrecaGodTehnoArray = new String[predmetiTrecaGodTehno.size()];
                            predmetiTrecaGodTehnoArray = predmetiTrecaGodTehno.toArray(predmetiTrecaGodTehnoArray);
                            predmetiTrecaGodTehnoArrayList = new ArrayList<>(Arrays.asList(predmetiTrecaGodTehnoArray));


                            String jsonNastavnici = gson.toJson(nastavniciArrayList);
                            String jsonPredmeti = gson.toJson(predmetiArrayList);
                            String jsonUcionice = gson.toJson(ucioniceArrayList);

                            String jsonNastavniciDizajn = gson.toJson(nastavniciArrayListDizajn);
                            String jsonPredmetiDizajn = gson.toJson(predmetiArrayListDizajn);

                            String jsonNastavniciTehnologije = gson.toJson(nastavniciArrayListTehnologije);
                            String jsonPredmetiTehnologije = gson.toJson(predmetiArrayListTehnologije);

                            String jsonNastavniciNauke = gson.toJson(nastavniciArrayListNauke);
                            String jsonPredmetiNauke = gson.toJson(predmetiArrayListNauke);

                            String jsonNastavniciMreze = gson.toJson(nastavniciArrayListInzenjerstvo);
                            String jsonPredmetiMreze = gson.toJson(predmetiArrayListInzenjerstvo);

                            String prvaGodMrezeNauke = gson.toJson(predmetiPrvaGodNaukeMrezeArrayList);
                            String drugaGodNauke = gson.toJson(predmetiDrugaGodNaukeArrayList);
                            String trecaGodNauke = gson.toJson(predmetiTrecaGodNaukeArrayList);
                            String cetvrtaGodNauke = gson.toJson(predmetiCetvrtaGodNaukeArrayList);

                            String drugaGodMreze = gson.toJson(predmetiDrugaGodMrezeArrayList);
                            String trecaGodMreze = gson.toJson(predmetiTrecaGodMrezeArrayList);
                            String cetvrtaGodMreze = gson.toJson(predmetiCetvrtaGodMrezeArrayList);

                            String prvaGodDizajn = gson.toJson(predmetiPrvaGodDizajnArrayList);
                            String drugaGodDizajn = gson.toJson(predmetiDrugaGodDizajnArrayList);

                            String prvaGodTehno = gson.toJson(predmetiPrvaGodTehnoArrayList);
                            String drugaGodTehno = gson.toJson(predmetiDrugaGodTehnoArrayList);
                            String trecaGodTehno = gson.toJson(predmetiTrecaGodTehnoArrayList);

                            editor.clear();
                            editor.apply();

                            editor.putString(NASTAVNICI_KEY, jsonNastavnici);
                            editor.putString(PREDMETI_KEY, jsonPredmeti);
                            editor.putString(UCIONICE_KEY, jsonUcionice);

                            editor.putString(NASTAVNICI_KEY_DIZAJN, jsonNastavniciDizajn);
                            editor.putString(PREDMETI_KEY_DIZAJN, jsonPredmetiDizajn);
                            editor.putString(NASTAVNICI_KEY_NAUKE, jsonNastavniciNauke);
                            editor.putString(PREDMETI_KEY_NAUKE, jsonPredmetiNauke);
                            editor.putString(NASTAVNICI_KEY_INZENJERSTVO, jsonNastavniciMreze);
                            editor.putString(PREDMETI_KEY_INZENJERSTVO, jsonPredmetiMreze);
                            editor.putString(NASTAVNICI_KEY_TEHNOLOGIJE, jsonNastavniciTehnologije);
                            editor.putString(PREDMETI_KEY_TEHNOLOGIJE, jsonPredmetiTehnologije);

                            editor.putString(PRVA_GOD_NAUKE_MREZE, prvaGodMrezeNauke);
                            editor.putString(DRUGA_GOD_NAUKE, drugaGodNauke);
                            editor.putString(TRECA_GOD_NAUKE, trecaGodNauke);
                            editor.putString(CETVRTA_GOD_NAUKE, cetvrtaGodNauke);

                            editor.putString(DRUGA_GOD_MREZE, drugaGodMreze);
                            editor.putString(TRECA_GOD_MREZE, trecaGodMreze);
                            editor.putString(CETVRTA_GOD_MREZE, cetvrtaGodMreze);

                            editor.putString(PRVA_GOD_DIZAJN, prvaGodDizajn);
                            editor.putString(DRUGA_GOD_DIZAJN, drugaGodDizajn);

                            editor.putString(PRVA_GOD_TEHNO, prvaGodTehno);
                            editor.putString(DRUGA_GOD_TEHNO, drugaGodTehno);
                            editor.putString(TRECA_GOD_TEHNO, trecaGodTehno);
                            editor.apply();

                            Log.i("START", "FINISH_JOB_SERVICE");

                            editor.putBoolean(KEY_PREFS_DATA, true);
                            editor.apply();


                        }
                    }
                }

            }
        };
        if (CheckNetwork.isInternetAvailable(JobService.this)) {
            //delete all data from database
//                deleteDataAll();
            vrednosti = new ArrayList<>();
            Log.i("START", "NOW_CHECK_INTERNET_JOB_SERVICE");
            readJSON.execute(JSON_API);
        }

        return true;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        readJSON.cancel(true);
        readJSON = null;
        return true;
    }

//    public static class BackgroundTask extends AsyncTask<Void,Void,String>{
//
//        @Override
//        protected String doInBackground(Void... voids) {
//
//            return "Hello from background job";
//        }
//    }

    @SuppressLint("StaticFieldLeak")
    class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            if (isCancelled())
                return null;
            return readURL(params[0]);
        }
    }

    private static String readURL(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            // create a url object
            URL url = new URL(theUrl);
            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("apikey", API_KEY);
            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
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
                Log.d(TAG, "OBRISANO_NIJE");
            } else {
                // Otherwise, the delete was successful and we can display a toast.
//                Toast.makeText(this, "all data are deleted",
//                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "OBRISANO");
            }
        }
        Intent e = new Intent(JobService.this, ExamService.class);
        JobService.this.startService(e);
    }

    static class CheckNetwork {

        private static final String TAG = JobService.CheckNetwork.class.getSimpleName();

        static boolean isInternetAvailable(Context context) {

            NetworkInfo info = ((ConnectivityManager) Objects.requireNonNull(context.getSystemService(Context.CONNECTIVITY_SERVICE))).getActiveNetworkInfo();

            if (info == null) {
                Log.d(TAG, "No internet connection");
                return false;

            } else {
                if (info.isConnected()) {

                    Log.d(TAG, "Internet connection available");
                    return true;
                } else {
                    Log.d(TAG, "Internet connection");
                    return true;

                }
            }


        }
    }
}