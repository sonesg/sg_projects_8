package com.example.nebojsa.notepad;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nebojsa.notepad.data.PetContract.PetEntry;
import com.example.nebojsa.notepad.data.PetDbHelper;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private PetDbHelper mDbHelper;

    String delete = "";

    private static final int PET_LOADER = 0;

    CursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        //mDbHelper = new PetDbHelper(this);

        //displayDatabaseInfo();
        // Find the ListView which will be populated with the pet data
        ListView petListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

        mCursorAdapter = new PetCursorAdapter(this,null);
        petListView.setAdapter(mCursorAdapter);




        //Setup item click listener
        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                //Create the content URI thaht represents the specific pet that was clicked on,
                //by appending the "id" (passed as input to this method)
                //the URI would be content://com.example.nebojsa.android.pet/pets/2
                //if the pet with ID 2 was clicked on

                Uri currentPetUri = ContentUris.withAppendedId(PetEntry.CONTENT_URI, id);

                //Set the uri on the data field of the intent
                intent.setData(currentPetUri);

                startActivity(intent);

            }
        });

        petListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long id) {


                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(id);
                delete = sb.toString();

                String CONTENT_AUTHORITY_DELETE = "com.example.nebojsa.notepad/notes/";

                Uri BASE_CONTENT_URI_FOR_ITEM_DELETE = Uri.parse("content://" + CONTENT_AUTHORITY_DELETE);

                String PATH_PETS = delete;

                Uri CONTENT_URI_DELETE_ITEM = Uri.withAppendedPath(BASE_CONTENT_URI_FOR_ITEM_DELETE, PATH_PETS);

                showDeleteConfirmationDialog(CONTENT_URI_DELETE_ITEM);

                return true;
            }
        });


        //Kick off the loader
        getLoaderManager().initLoader(PET_LOADER, null, this);



    }

    private void showDeleteConfirmationDialog(final Uri CONTENT_URI) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deletePet(CONTENT_URI);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deletePet(Uri CONTENT_URI) {
        // Only perform the delete if this is an existing pet.
        if (CONTENT_URI != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(CONTENT_URI, null, null);
            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    //   private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.

        // Create and/or open a database to read from it
    //    SQLiteDatabase db = mDbHelper.getReadableDatabase();

  /**      String[] project = {PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT};
   **/
/**
        Cursor cursor = db.query(PetEntry.TABLE_NAME,
                project,
                null,
                null,
                null,
                null,
                null);
                **/

//Perform a query on the provider using the ContentResolver.
        //Use the PetEntry.CONTENT_URI to access the pet data
    //    Cursor cursor = getContentResolver().query(PetEntry.CONTENT_URI ,project, null, null, null);

        // Find the ListView which will be populated with the pet data
     //           ListView petListView = (ListView) findViewById(R.id.list);

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
   //             PetCursorAdapter adapter = new PetCursorAdapter(this, cursor);

        // Attach the adapter to the ListView.
   //             petListView.setAdapter(adapter);
//
//        try {
//            // Create a header in the Text View that looks like this:
//
//            // The pets table contains <number of rows in Cursor> pets.
//            // _id - name - breed - gender - weight
//
//            // In the while loop below, iterate through the rows of the cursor and display
//            // the information from each column in this order.
//            displayView.setText("The pets table contains " + cursor.getCount() + " pets.\n\n");
//            displayView.append(PetEntry._ID + " - " +
//                    PetEntry.COLUMN_PET_NAME + " - " +
//                    PetEntry.COLUMN_PET_BREED + " - " +
//                    PetEntry.COLUMN_PET_GENDER + " - " +
//                    PetEntry.COLUMN_PET_WEIGHT + "\n");
//
//            // Figure out the index of each column
//            int idColumnIndex = cursor.getColumnIndex(PetEntry._ID);
//            int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
//            int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
//            int genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
//            int weightColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);
//            // Iterate through all the returned rows in the cursor
//            while (cursor.moveToNext()) {
//                // Use that index to extract the String or Int value of the word
//                // at the current row the cursor is on.
//                int currentID = cursor.getInt(idColumnIndex);
//                String currentName = cursor.getString(nameColumnIndex);
//                String currentBreed = cursor.getString(breedColumnIndex);
//                int currentGender = cursor.getInt(genderColumnIndex);
//                int currentWeight = cursor.getInt(weightColumnIndex);
//                // Display the values from each column of the current row in the cursor in the TextView
//                //append metodu koristimo da bi zadrzali red u textu,kako koji text dodajemo
//                displayView.append(("\n" + currentID + " - " +
//                        currentName + " - " +
//                        currentBreed + " - " +
//                        currentGender + " - " +
//                        currentWeight));
//            }
//        } finally {//try blok koristimo iz razloga da ne bi doslo do greske u while petlji odnosno ako cursorov index izadje iz opsega tabele,kada se to bude desilo pozvace se final blok i cursor ce biti zatvoren kako ne bi doslo do curenja memorije
//            // Always close the cursor when you're done reading from it. This releases all its
//            // resources and makes it invalid.
//            cursor.close();
//        }
//
//    }

/**
    private void insertPet(){

        ContentValues values = new ContentValues();

        values.put(PetEntry.COLUMN_PET_NAME, "Totto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terier");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
       // values.put(PetEntry.COLUMN_PET_WEIGHT, 7);

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(PetEntry.CONTENT_URI, values);

    }
**/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    /**
     +     * Helper method to delete all pets in the database.
     +     */
    private void showDeleteConfirmationDialog_2() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg_2);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deletePetAll();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deletePetAll() {
        // Only perform the delete if this is an existing pet.
        if (PetEntry.CONTENT_URI != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(PetEntry.CONTENT_URI, null, null);
            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_pet_failed_2),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_pet_successful_2),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     +     * Helper method to delete all pets in the database.
     +     */
    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(PetEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog_2();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
    @Override
    protected void onStart() {//ovo je matoda koje se poziva kad se pokrene ovaj activity
        super.onStart();
        displayDatabaseInfo();
    }
     **/

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER};

        //this loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   //Parent activity context
                PetEntry.CONTENT_URI,   //Provider content URI to query
                projection,             //Columns to include in the resulting Cursor
                null,
                null,
                null);                  //Default sort order
        }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        //Update with this new cursor containing updated pet data
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        //Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}