package rs.edu.httpsraf.rasporedraf.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Objects;

import rs.edu.httpsraf.rasporedraf.data.SubjectContract.SubjectEntry;

public class SubjectProvider extends ContentProvider {

    public static final String LOG_TAG = SubjectProvider.class.getSimpleName();

    private SubjectDbHelper subjectDbHelper;

    private static final int ITEMS = 100;

    private static final int ITEM_ID = 10;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //static initializer.This is run the first time anything is called from this class
    static {

        sUriMatcher.addURI(SubjectContract.CONTENT_AUTHORITY, SubjectContract.PATH_SUBJECTS, ITEMS);

        sUriMatcher.addURI(SubjectContract.CONTENT_AUTHORITY, SubjectContract.PATH_SUBJECTS + "/#", ITEM_ID);

    }

    @Override
    public boolean onCreate() {
        subjectDbHelper = new SubjectDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = subjectDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                // For the ITEMS code, query the items table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the items table.
                cursor = database.query(SubjectEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case ITEM_ID:
                // For the ITEM_ID code, extract out the ID from the URI.
                // For an example URI such as "content://rs.edu.httpsraf.rasporedraf/subjects/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = SubjectEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the subjects table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(SubjectContract.SubjectEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        //Set notification URI on the Cursor
        //so we know what content URI the Cursor was created for
        //If the data at this URI changes, then we know we need to update the Cursor
        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return insertSubject(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a subject into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertSubject(Uri uri, ContentValues values) {

        String predmet = values.getAsString(SubjectEntry.COLUMN_PREDMET);
//        if (predmet == null) {
//            throw new IllegalArgumentException("Requires a predmet");
//        }
        String tip = values.getAsString(SubjectEntry.COLUMN_TIP);
//        if (tip == null) {
//            throw new IllegalArgumentException("Requires a tip");
//        }
        String nastavnik = values.getAsString(SubjectEntry.COLUMN_NASTAVNIK);
//        if (nastavnik == null) {
//            throw new IllegalArgumentException("Requires a nastavnik");
//        }
        String grupe = values.getAsString(SubjectEntry.COLUMN_GRUPE);
//        if (grupe == null) {
//            throw new IllegalArgumentException("Requires a grupa");
//        }
//        int dan = values.getAsInteger(SubjectEntry.COLUMN_DAN);
//        if (dan == -1) {
//            throw new IllegalArgumentException("Requires a dan");
//        }
        String termin = values.getAsString(SubjectEntry.COLUMN_TERMIN);
//        if (termin == null) {
//            throw new IllegalArgumentException("Requires a termin");
//        }
        String ucionica = values.getAsString(SubjectEntry.COLUMN_UCIONICA);
//        if (ucionica == null) {
//            throw new IllegalArgumentException("Requires a ucionica");
//        }
        String predmet_exam = values.getAsString(SubjectEntry.COLUMN_NAME_EXAM);

        String date_time_exam = values.getAsString(SubjectEntry.COLUMN_DATE_EXAM);

        String ucionica_exam = values.getAsString(SubjectEntry.COLUMN_PROFESSOR_EXAM);

        String profesor_exam = values.getAsString(SubjectEntry.COLUMN_UCIONICA_EXAM);

        String tip_exam = values.getAsString(SubjectEntry.COLUMN_TYPE_EXAM);


        // Get writable database
        SQLiteDatabase database = subjectDbHelper.getWritableDatabase();

        long id = database.insert(SubjectEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //Notify all listeners that the data has changed for the subject content URI
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);//ovo vraca uri objekat sa tacnim id-ijem.Npr vraca content://com.example.android.pets/pets/6
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get writable database
        SQLiteDatabase database = subjectDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                // Delete all rows that match the selection and selection args
                // return database.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
                rowsDeleted = database.delete(SubjectEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        //No implementations
        return 0;
    }
}
