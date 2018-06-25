package com.example.korisnik.chatproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {

    private static final int GALLERY_PICK = 1;
    private static final int MAX_LENGTH = 20;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    //Layout things
    private CircleImageView displayImage;
    private TextView textName;
    private TextView textStatus;
    private Button statusBtn;
    private Button imageBtn;

    //Firebase Storage
    private StorageReference imageStorage;
    //ProgressDialog
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReferenceOnline,databaseReferenceOnlineLastSeen;

    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        displayImage = (CircleImageView) findViewById(R.id.circle_image_display_settings);
        textName = (TextView) findViewById(R.id.textNameSettings);
        textStatus = (TextView) findViewById(R.id.textStatusSettings);
        statusBtn = (Button) findViewById(R.id.changeStatusSettings);
        imageBtn = (Button) findViewById(R.id.changeImageSettings);

        imageStorage = FirebaseStorage.getInstance().getReference();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        databaseReference.keepSynced(true);

        //ovo je namestanje da upisemo u korisnikov deo baze da je online
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {

            databaseReferenceOnline = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(mAuth.getCurrentUser().getUid());
            databaseReferenceOnline.keepSynced(true);
            databaseReferenceOnlineLastSeen = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(mAuth.getCurrentUser().getUid());
            databaseReferenceOnlineLastSeen.keepSynced(true);
        }


        //now we want to retrieve all data bellow user id
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(SettingsActivity.this, dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                final String thumbImage = dataSnapshot.child("thumb_image").getValue().toString();

                textName.setText(name);
                textStatus.setText(status);

                progressBar = findViewById(R.id.loading_indicator);
                // Show progress bar
                progressBar.setVisibility(View.VISIBLE);
                // Hide progress bar on successful load
                Picasso.with(SettingsActivity.this).load(thumbImage)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.img_avatar)
                        .into(displayImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onError() {
                                progressBar.setVisibility(View.GONE);
                                Picasso.with(SettingsActivity.this).load(thumbImage).placeholder(R.drawable.img_avatar).into(displayImage);
                            }
                        });

                //Picasso.with(SettingsActivity.this).load(image).into(displayImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        statusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = textStatus.getText().toString();
                Intent intent = new Intent(SettingsActivity.this, StatusActivity.class);
                intent.putExtra("status_value", status);
                startActivity(intent);
            }
        });

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

                //Using Cropping library
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .start(SettingsActivity.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){
            Uri imageUri = data.getData();

            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .setMinCropResultSize(500, 500)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                progressDialog = new ProgressDialog(SettingsActivity.this);
                progressDialog.setTitle("Uploading image...");
                progressDialog.setMessage("Please wait while we upload and process the image");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                Uri resultUri = result.getUri();

                //treba nam fajl za compress fju
                File thumb_filePath = new File(resultUri.getPath());


                    //kreiramo nasu kompresovanu sliku
                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            //kvalitet slike
                            .setQuality(75)
                            .compressToBitmap(thumb_filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //this actually converts data to byte form which will then allow us to upload the image in firebase storage
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();


                //uzimamo id da bi dali ime image u bazi podataka.Ime slike ce biti njegov id
                String current_user_uid = currentUser.getUid();

                // STARIJA VERZIJA StorageReference filePath = imageStorage.child("profile_images").child(random() + ".jpg");
                StorageReference filePath = imageStorage.child("profile_images").child(current_user_uid + ".jpg");
                //kreiramo za kompresovane podatke(u ovom slucaju slike) direktorijum u firebase storage sekciji
                final StorageReference filePath_thumb = imageStorage.child("profile_images").child("thumb_img").child(current_user_uid + ".jpg");
                //moramo da pazimo,prvo se u storage stavlja obicna nekompresovana slika pa tek onda od nje nastaje kompresovana slika
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            //get url for image
                            final String download_url = task.getResult().getDownloadUrl().toString();
                            //upload thumb images in storage
                            UploadTask uploadTask = filePath_thumb.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    String thumb_url = thumb_task.getResult().getDownloadUrl().toString();
                                    if(thumb_task.isSuccessful()){
                                        //ne stavljamo HashMap<String,String> jer ce u bazu dodati samo satvke image i thumb_image bez name i status
                                        //zato koristimo super klasu Map i posle samo radimo updateChildren
                                        Map hashMap = new HashMap<>();
                                        hashMap.put("image", download_url);
                                        hashMap.put("thumb_image", thumb_url);
                                        //ovde nece biti .child("image") jer dodajemo dva tipa podatka u bazu
                                        databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    progressDialog.dismiss();
                                                    Toast.makeText(SettingsActivity.this, "Successful upload image!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }else{
                                        progressDialog.hide();
                                        Toast.makeText(SettingsActivity.this, "Error in uploading thumbnail.", Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser!=null) {
            databaseReferenceOnline.child("online").setValue(true);
            databaseReferenceOnlineLastSeen.child("lastSeen").setValue("Online");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            databaseReferenceOnline.child("online").setValue(false);
        }
    }

    //function who's give images random names
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
//DOBRA STVAR U FIREBASE-U JE TO STO SE PODACI OVERWRITE-UJU TAKO DA NE MORAMO DA SE BAVIMO BRISANJE STARIH PODATKA
//KADA UBACUJEMO NOVE PODATKE
