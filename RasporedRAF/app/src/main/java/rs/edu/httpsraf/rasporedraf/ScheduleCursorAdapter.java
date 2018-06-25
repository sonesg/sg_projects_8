package rs.edu.httpsraf.rasporedraf;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import rs.edu.httpsraf.rasporedraf.data.SubjectContract.SubjectEntry;

import static android.content.Context.MODE_PRIVATE;


public class ScheduleCursorAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;
    private final static String SHARED_PREFERENCES_FILE_3 = "file3shared";
    private SharedPreferences sharedPreferences;

    ScheduleCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_3, MODE_PRIVATE);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.schedule_list, parent, false);
    }


    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        LinearLayout linearLayout = view.findViewById(R.id.layout_list_item);

        TextView txtPredmet = view.findViewById(R.id.list_predmet);

        ImageView imgTip = view.findViewById(R.id.list_tip);

        TextView txtNastavnik = view.findViewById(R.id.list_nastavnik);

        TextView txtDan = view.findViewById(R.id.list_dan);


        TextView txtTermin = view.findViewById(R.id.list_termin);

        TextView txtUcionica = view.findViewById(R.id.list_ucionica);

        // Find the columns of subject attributes that we're interested in
        int predmetColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_PREDMET);
        int tipColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_TIP);
        int nastavnikColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_NASTAVNIK);
        int grupeColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_GRUPE);
        int danColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_DAN);
        int terminColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_TERMIN);
        int ucionicaColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_UCIONICA);

        // Read the subject attributes from the Cursor for the current subject
        final String strPredmet = cursor.getString(predmetColumnIndex);
        String strTip = cursor.getString(tipColumnIndex);
        String strNastavnik = cursor.getString(nastavnikColumnIndex);
        final String strGrupe = cursor.getString(grupeColumnIndex);
        int strDan = cursor.getInt(danColumnIndex);
        String dan = null;
        String strTermin = cursor.getString(terminColumnIndex);
        String strUcionica = cursor.getString(ucionicaColumnIndex);
//        int danBaner = cursor.getExtras().getInt(BUNDLE_KEY, 0);

//        bundle = cursor.getExtras();
//
//        Log.i("OVAMO", bundle.getInt(PON_BUNDLE_KEY,0)+"");


        switch (strDan) {
            case 1:
                dan = "PONEDELJAK";
//                if(cursor.getExtras().getInt(strPredmet+strTermin+strDan)==21) {
//                    txtDan.setVisibility(View.VISIBLE);
//                    Log.i("ADAPTER","pon");
//                    linearLayout.setPadding(8,40,8,0);
//                }else{
//                    txtDan.setVisibility(View.GONE);
//                    linearLayout.setPadding(8,0,8,0);
//                }
                if (sharedPreferences.getInt(strPredmet + strTermin + strDan, 0) == 21) {
                    txtDan.setVisibility(View.VISIBLE);
//                    Log.i("ADAPTER", "pon");
                    linearLayout.setPadding(8, 40, 8, 0);
                } else {
                    txtDan.setVisibility(View.GONE);
                    linearLayout.setPadding(8, 0, 8, 0);
                }
                break;
            case 2:
                dan = "UTORAK";
                if (sharedPreferences.getInt(strPredmet + strTermin + strDan, 0) == 22) {
                    txtDan.setVisibility(View.VISIBLE);
                    linearLayout.setPadding(8, 40, 8, 0);
//                    Log.i("ADAPTER", "uto");
                } else {
                    txtDan.setVisibility(View.GONE);
                    linearLayout.setPadding(8, 0, 8, 0);
                }
                break;
            case 3:
                dan = "SREDA";
                if (sharedPreferences.getInt(strPredmet + strTermin + strDan, 0) == 23) {
                    txtDan.setVisibility(View.VISIBLE);
                    linearLayout.setPadding(8, 40, 8, 0);
//                    Log.i("ADAPTER", "sre");
                } else {
                    txtDan.setVisibility(View.GONE);
                    linearLayout.setPadding(8, 0, 8, 0);
                }
                break;
            case 4:
                dan = "ČETVRTAK";
                if (sharedPreferences.getInt(strPredmet + strTermin + strDan, 0) == 24) {
                    txtDan.setVisibility(View.VISIBLE);
                    linearLayout.setPadding(8, 40, 8, 0);
//                    Log.i("ADAPTER", "cet");
                } else {
                    txtDan.setVisibility(View.GONE);
                    linearLayout.setPadding(8,0,8,0);
                }
                break;
            case 5:
                dan = "PETAK";
                if (sharedPreferences.getInt(strPredmet + strTermin + strDan, 0) == 25) {
                    txtDan.setVisibility(View.VISIBLE);
                    linearLayout.setPadding(8, 40, 8, 0);
//                    Log.i("ADAPTER", "pet");
                } else {
                    txtDan.setVisibility(View.GONE);
                    linearLayout.setPadding(8, 0, 8, 0);
                }
                break;
            case 6:
                dan = "SUBOTA";
                if (sharedPreferences.getInt(strPredmet + strTermin + strDan, 0) == 26) {
                    txtDan.setVisibility(View.VISIBLE);
                    linearLayout.setPadding(8, 40, 8, 0);
//                    Log.i("ADAPTER", "sub");
                } else {
                    txtDan.setVisibility(View.GONE);
                    linearLayout.setPadding(8, 0, 8, 0);
                }
                break;
        }


        // Update the TextViews with the attributes for the current subject
        txtPredmet.setText(strPredmet);
        if (strTip.contains("Predavanja")) {
            imgTip.setBackgroundResource(R.drawable.predavanje);
        } else if (strTip.equals("Vezbe")) {
            imgTip.setBackgroundResource(R.drawable.vezbe);
        } else if (strTip.equals("Laboratorijske vezbe")) {
            imgTip.setBackgroundResource(R.drawable.lab);
        }
        txtNastavnik.setText(strNastavnik);
        txtDan.setText(dan);
        txtTermin.setText(String.format("%s%s", strTermin, 'h'));
        switch (strUcionica) {
            case "Ucionica 1":
                strUcionica = "U1";
                break;
            case "Ucionica 3":
                strUcionica = "U3";
                break;
            case "Ucionica 4":
                strUcionica = "U4";
                break;
            case "Ucionica 5":
                strUcionica = "U5";
                break;
            case "Ucionica 6":
                strUcionica = "U6";
                break;
            case "Ucionica 7":
                strUcionica = "U7";
                break;
            case "Ucionica 8":
                strUcionica = "U8";
                break;
            case "Ucionica 9":
                strUcionica = "U9";
                break;
            case "Profesorski kabinet":
                strUcionica = "P.K.";
                break;
            case "Kolarac":
                strUcionica = "Kol";
                break;
        }
        txtUcionica.setText(strUcionica);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog settingsDialog = new Dialog(context);
                if (settingsDialog.getWindow() != null) {
                    settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                }
                settingsDialog.setContentView(View.inflate(context, R.layout.dialog_layout
                        , null));
                final TextView name_dialog = settingsDialog.findViewById(R.id.dialog_text);
                name_dialog.setText(strGrupe);
                settingsDialog.show();
            }
        });

    }

}