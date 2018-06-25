package rs.edu.httpsraf.rasporedraf.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class SubjectContract {

    public static final String CONTENT_AUTHORITY = "rs.edu.httpsraf.rasporedraf";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SUBJECTS = "subjects";

    private SubjectContract() {

    }

    public static final class SubjectEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SUBJECTS);

        public final static String TABLE_NAME = "rafroid";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PREDMET = "predmet";
        public final static String COLUMN_TIP = "tip";
        public final static String COLUMN_NASTAVNIK = "nastavnik";
        public final static String COLUMN_GRUPE = "grupe";
        public final static String COLUMN_DAN = "dan";
        public final static String COLUMN_TERMIN = "termin";
        public final static String COLUMN_UCIONICA = "ucionica";
        public final static String COLUMN_NAME_EXAM = "ime_predmeta_exam";
        public final static String COLUMN_DATE_EXAM = "vreme_datum_exam";
        public final static String COLUMN_UCIONICA_EXAM = "ucionica_exam";
        public final static String COLUMN_PROFESSOR_EXAM = "profesor_exam";
        public final static String COLUMN_TYPE_EXAM = "type_exam";
        public final static String COLUMN_TIME_EXAM = "time_exam";

    }

}
