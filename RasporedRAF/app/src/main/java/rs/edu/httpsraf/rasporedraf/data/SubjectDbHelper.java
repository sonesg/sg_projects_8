package rs.edu.httpsraf.rasporedraf.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import rs.edu.httpsraf.rasporedraf.data.SubjectContract.SubjectEntry;


public class SubjectDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "rafroid.db";

    private static final int DATABASE_VERSION = 1;

    SubjectDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQL_CREATE_SUBJECTS_TABLE = "CREATE TABLE " + SubjectEntry.TABLE_NAME + "("
                + SubjectEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SubjectEntry.COLUMN_PREDMET + " TEXT, "
                + SubjectEntry.COLUMN_TIP + " TEXT, "
                + SubjectEntry.COLUMN_NASTAVNIK + " TEXT, "
                + SubjectEntry.COLUMN_GRUPE + " TEXT, "
                + SubjectEntry.COLUMN_DAN + " INTEGER, "
                + SubjectEntry.COLUMN_TERMIN + " TEXT, "
                + SubjectEntry.COLUMN_NAME_EXAM + " TEXT, "
                + SubjectEntry.COLUMN_DATE_EXAM + " TEXT, "
                + SubjectEntry.COLUMN_UCIONICA_EXAM + " TEXT, "
                + SubjectEntry.COLUMN_PROFESSOR_EXAM + " TEXT, "
                + SubjectEntry.COLUMN_TYPE_EXAM + " TEXT, "
                + SubjectEntry.COLUMN_TIME_EXAM + " TEXT, "
                + SubjectEntry.COLUMN_UCIONICA + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_SUBJECTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
