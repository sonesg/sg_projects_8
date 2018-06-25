package rs.edu.httpsraf.rasporedraf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import rs.edu.httpsraf.rasporedraf.data.SubjectContract.SubjectEntry;


public class ExamCursorAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;
    private String dateToStr;

    ExamCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        Date today = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.");
        dateToStr = format.format(today);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.exam_item, parent, false);
    }


    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

//        CardView cardView = view.findViewById(R.id.card_view_exam);

        LinearLayout linearLayout = view.findViewById(R.id.layout_list_item_exam);

        LinearLayout linearLayout1 = view.findViewById(R.id.list_exam_linear);

        TextView txtPredmet = view.findViewById(R.id.list_predmet_exam);

//        TextView txtTip = view.findViewById(R.id.list_tip);

        TextView txtNastavnik = view.findViewById(R.id.list_nastavnik_exam);

//        TextView txtGrupe = view.findViewById(R.id.list_grupe);

        TextView txtDate = view.findViewById(R.id.list_date_exam);

//        TextView txtTermin = view.findViewById(R.id.list_termin);

        TextView txtUcionica = view.findViewById(R.id.list_ucionica_exam);

        TextView txtTermin = view.findViewById(R.id.list_termin_exam);


        // Find the columns of subject attributes that we're interested in
        int predmetColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_NAME_EXAM);
//        int tipColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_TIP);
        int nastavnikColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_PROFESSOR_EXAM);
//        int grupeColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_GRUPE);
        int dateColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_DATE_EXAM);
//        int terminColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_TERMIN);
        int ucionicaColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_UCIONICA_EXAM);

        int timeColumnIndex = cursor.getColumnIndex(SubjectEntry.COLUMN_TIME_EXAM);

        // Read the subject attributes from the Cursor for the current subject
        String strPredmet = cursor.getString(predmetColumnIndex);
//        String strTip = cursor.getString(tipColumnIndex);
        String strNastavnik = cursor.getString(nastavnikColumnIndex);
//        String strGrupe = cursor.getString(grupeColumnIndex);
        String strDan = cursor.getString(dateColumnIndex);
//        String strTermin = cursor.getString(terminColumnIndex);
        String strUcionica = cursor.getString(ucionicaColumnIndex);

        String strTime = cursor.getString(timeColumnIndex);

        switch (strUcionica) {
            case "1":
                strUcionica = "U1";
                break;
            case "3":
                strUcionica = "U3";
                break;
            case "4":
                strUcionica = "U4";
                break;
            case "5":
                strUcionica = "U5";
                break;
            case "6":
                strUcionica = "U6";
                break;
            case "7":
                strUcionica = "U7";
                break;
            case "8":
                strUcionica = "U8";
                break;
            case "9":
                strUcionica = "U9";
                break;
        }

        if (dateToStr.compareTo(strDan) <= 0) {
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout.setPadding(8, 20, 8, 0);
        } else {
            linearLayout1.setVisibility(View.GONE);
        }

//        switch (strDan){
//            case 1:
//                dan = "PONEDELJAK";
//            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
////            Log.i("PON", "1");
//                txtPredmet.setTextColor(ContextCompat.getColor(context, R.color.black));
//                txtTip.setTextColor(ContextCompat.getColor(context, R.color.black));
//                txtNastavnik.setTextColor(ContextCompat.getColor(context, R.color.black));
//                txtGrupe.setTextColor(ContextCompat.getColor(context, R.color.black));
//                txtDan.setTextColor(ContextCompat.getColor(context, R.color.black));
//                txtTermin.setTextColor(ContextCompat.getColor(context, R.color.black));
//                txtUcionica.setTextColor(ContextCompat.getColor(context, R.color.black));
//                break;
//            case 2:
//                dan = "UTORAK";
//                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color2));
//                Log.i("PON", "2");
//                txtPredmet.setTextColor(ContextCompat.getColor(context, R.color.black));
//                txtTip.setTextColor(ContextCompat.getColor(context, R.color.black));
//                txtNastavnik.setTextColor(ContextCompat.getColor(context, R.color.black));
//                txtGrupe.setTextColor(ContextCompat.getColor(context, R.color.black));
//                txtDan.setTextColor(ContextCompat.getColor(context, R.color.black));
//                txtTermin.setTextColor(ContextCompat.getColor(context, R.color.black));
//                txtUcionica.setTextColor(ContextCompat.getColor(context, R.color.black));
//                break;
//            case 3:
//                dan = "SREDA";
////                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color3));
////                txtPredmet.setTextColor(ContextCompat.getColor(context, R.color.black));
////                txtTip.setTextColor(ContextCompat.getColor(context, R.color.black));
////                txtNastavnik.setTextColor(ContextCompat.getColor(context, R.color.black));
////                txtGrupe.setTextColor(ContextCompat.getColor(context, R.color.black));
////                txtDan.setTextColor(ContextCompat.getColor(context, R.color.black));
////                txtTermin.setTextColor(ContextCompat.getColor(context, R.color.black));
////                txtUcionica.setTextColor(ContextCompat.getColor(context, R.color.black));
//                Log.i("PON", "3");
//                break;
//            case 4:
//                dan = "ČETVRTAK";
//                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color4));
//                txtPredmet.setTextColor(ContextCompat.getColor(context, R.color.white));
//                txtTip.setTextColor(ContextCompat.getColor(context, R.color.white));
//                txtNastavnik.setTextColor(ContextCompat.getColor(context, R.color.white));
//                txtGrupe.setTextColor(ContextCompat.getColor(context, R.color.white));
//                txtDan.setTextColor(ContextCompat.getColor(context, R.color.white));
//                txtTermin.setTextColor(ContextCompat.getColor(context, R.color.white));
//                txtUcionica.setTextColor(ContextCompat.getColor(context, R.color.white));
//                Log.i("PON", "4");
//                break;
//            case 5:
//                dan = "PETAK";
//                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color5));
//                txtPredmet.setTextColor(ContextCompat.getColor(context, R.color.white));
//                txtTip.setTextColor(ContextCompat.getColor(context, R.color.white));
//                txtNastavnik.setTextColor(ContextCompat.getColor(context, R.color.white));
//                txtGrupe.setTextColor(ContextCompat.getColor(context, R.color.white));
//                txtDan.setTextColor(ContextCompat.getColor(context, R.color.white));
//                txtTermin.setTextColor(ContextCompat.getColor(context, R.color.white));
//                txtUcionica.setTextColor(ContextCompat.getColor(context, R.color.white));
//                Log.i("PON", "5");
//                break;
//            case 6:
//                dan = "SUBOTA";
//                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.color6));
//                txtPredmet.setTextColor(ContextCompat.getColor(context, R.color.white));
//                txtTip.setTextColor(ContextCompat.getColor(context, R.color.white));
//                txtNastavnik.setTextColor(ContextCompat.getColor(context, R.color.white));
//                txtGrupe.setTextColor(ContextCompat.getColor(context, R.color.white));
//                txtDan.setTextColor(ContextCompat.getColor(context, R.color.white));
//                txtTermin.setTextColor(ContextCompat.getColor(context, R.color.white));
//                txtUcionica.setTextColor(ContextCompat.getColor(context, R.color.white));
//                Log.i("PON", "6");
//                break;
//        }

        // Update the TextViews with the attributes for the current subject
        txtPredmet.setText(strPredmet);
//        txtTip.setText(strTip);
        txtNastavnik.setText(strNastavnik);
//        txtGrupe.setText(strGrupe);
        txtDate.setText(strDan);
        txtTermin.setText(String.format("%s%s", strTime, 'h'));
        txtUcionica.setText(strUcionica);
//        strTime = strTime + "h";
//        txtTermin.setText(strTime);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(context, strPredmet, Toast.LENGTH_SHORT).show();
//                final Dialog settingsDialog = new Dialog(context);
//                if (settingsDialog.getWindow() != null) {
//                    settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//                }
//                settingsDialog.setContentView(View.inflate(context, R.layout.dialog_layout
//                        , null));
////                final TextView name_dialog = settingsDialog.findViewById(R.id.dialog_name_list);
////                name_dialog.setText(newsName);
//                settingsDialog.show();
//                final ImageView imageViewBack = settingsDialog.findViewById(R.id.dialog_back);
//                imageViewBack.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        imageViewBack.setImageResource(android.R.color.transparent);
//                        settingsDialog.dismiss();
//                    }
//                });
//            }
//        });


    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}