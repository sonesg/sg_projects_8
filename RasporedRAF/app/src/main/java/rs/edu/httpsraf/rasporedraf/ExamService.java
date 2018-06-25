package rs.edu.httpsraf.rasporedraf;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

import rs.edu.httpsraf.rasporedraf.data.SubjectContract;

public class ExamService extends Service {

    ReadJSONEXAM readJSONEXAM;

    private static final String JSON_API = "https://api.raf.ninja/v1/exams";
    private static final String API_KEY = "bJoZoihORa9jzGlHg698";


    @SuppressLint("StaticFieldLeak")
    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        //TODO do something useful

//        if (ScheduleService.CheckNetwork.isInternetAvailable(ExamService.this)) {
//            String[] projection = {
//                    SubjectContract.SubjectEntry.COLUMN_NAME_EXAMP,
//                    SubjectContract.SubjectEntry.COLUMN_DATE_TIME_EXAMP,
//                    SubjectContract.SubjectEntry.COLUMN_UCIONICA_EXAMP,
//                    SubjectContract.SubjectEntry.COLUMN_UCIONICA_EXAMP,
//                    SubjectContract.SubjectEntry.COLUMN_TYPE_EXAMP};
//            //delete some data from database
//            getContentResolver().delete(SubjectContract.SubjectEntry.CONTENT_URI,,null);
//        }


        readJSONEXAM = new ReadJSONEXAM() {
            @Override
            protected void onPostExecute(String content) {
                super.onPostExecute(content);

//                if (ExamService.CheckNetwork.isInternetAvailable(ExamService.this)) {
//                    //delete all data from database
//                    deleteDataAll();
//                }
                Log.i("START", "NOW_EXAM_SERVICE");
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    JSONArray jsonArray = jsonObject.getJSONArray("schedule");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject productObject = jsonArray.getJSONObject(i);

                        String date_time = productObject.getString("date_and_time");
                        int index = date_time.indexOf("|");
                        String date = null;
                        String time = null;
                        if (index != -1)
                        {
                            date = date_time.substring(0 , index);
                            time = date_time.substring(index+1,date_time.length());
                        }

                        ContentValues values = new ContentValues();
                        values.put(SubjectContract.SubjectEntry.COLUMN_NAME_EXAM, productObject.getString("test_name"));
                        values.put(SubjectContract.SubjectEntry.COLUMN_DATE_EXAM, date);
                        values.put(SubjectContract.SubjectEntry.COLUMN_UCIONICA_EXAM, productObject.getString("classroom"));
                        values.put(SubjectContract.SubjectEntry.COLUMN_PROFESSOR_EXAM, productObject.getString("professor"));
                        values.put(SubjectContract.SubjectEntry.COLUMN_TYPE_EXAM, productObject.getString("type"));
                        values.put(SubjectContract.SubjectEntry.COLUMN_TIME_EXAM, time);
//                        values.put(SubjectContract.SubjectEntry.COLUMN_PREDMET, (String) null);
//                        values.put(SubjectContract.SubjectEntry.COLUMN_TIP, (String) null);
//                        values.put(SubjectContract.SubjectEntry.COLUMN_NASTAVNIK, (String) null);
//                        values.put(SubjectContract.SubjectEntry.COLUMN_GRUPE, (String) null);
//                        values.put(SubjectContract.SubjectEntry.COLUMN_DAN, (String) null);
//                        values.put(SubjectContract.SubjectEntry.COLUMN_TERMIN, (String) null);
//                        values.put(SubjectContract.SubjectEntry.COLUMN_UCIONICA, (String) null);

                        Log.i("PODATAK1", productObject.getString("test_name"));
                        Log.i("PODATAK2", date);
                        Log.i("PODATAK3", productObject.getString("classroom"));
                        Log.i("PODATAK4", productObject.getString("professor"));
                        Log.i("PODATAK5", productObject.getString("type"));
                        Log.i("PODATAK6", time);

                        getContentResolver().insert(SubjectContract.SubjectEntry.CONTENT_URI, values);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("START","FINISH_EXAM_SERVICE");
            }
        };

        if (ExamService.CheckNetwork.isInternetAvailable(ExamService.this)) {

//            Log.d("POKRENUT_SERVICE", "OPALAC");
            Log.i("START", "NOW_CHECK_INTERNET_EXAM_SERVICE");
            readJSONEXAM.execute(JSON_API);

//            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);
//            if(!sharedPreferences.getBoolean(KEY_PREFS_DATA, false)) {
//                FirstScreen.btnTehnologije.setEnabled(false);
//                FirstScreen.btnNauke.setEnabled(false);
//                FirstScreen.btnMreze.setEnabled(false);
//                FirstScreen.btnDizajn.setEnabled(false);
//            }

        } else {
//            sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, MODE_PRIVATE);//MODE_PRIVATE = 0
//            editor = sharedPreferences.edit();
//            editor.putBoolean(KEY_PREFS_DATA_CHECK, true);
//            editor.apply();

//            if(sharedPreferences2.getBoolean(PROGRESS_BAR,false)){
//                ScheduleDataActivity.progressBar.setVisibility(View.GONE);
//            }

//            Toast.makeText(ExamService.this, "Nema internet konekcije", Toast.LENGTH_SHORT).show();
        }

        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("StaticFieldLeak")
    class ReadJSONEXAM extends AsyncTask<String, Integer, String> {

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
        Log.i("Podatak", content.toString());
        return content.toString();
    }

    static class CheckNetwork {

        private static final String TAG = ScheduleService.CheckNetwork.class.getSimpleName();

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

//    public void deleteDataAll() {
//        // Only perform the delete if this is an existing pet.
//        if (SubjectContract.SubjectEntry.CONTENT_URI != null) {
//            // Call the ContentResolver to delete the pet at the given content URI.
//            // Pass in null for the selection and selection args because the mCurrentPetUri
//            // content URI already identifies the pet that we want.
//            int rowsDeleted = getContentResolver().delete(SubjectContract.SubjectEntry.CONTENT_URI, null, null);
//            // Show a toast message depending on whether or not the delete was successful.
//            if (rowsDeleted == 0) {
//                // If no rows were deleted, then there was an error with the delete.
//                Toast.makeText(ExamService.this, "error with deleting data",
//                        Toast.LENGTH_SHORT).show();
//            } else {
//                // Otherwise, the delete was successful and we can display a toast.
//                Toast.makeText(ExamService.this, "all data are deleted",
//                        Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}
