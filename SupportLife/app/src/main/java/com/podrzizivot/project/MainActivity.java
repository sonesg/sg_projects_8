package com.podrzizivot.project;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.podrzizivot.project.fragments.FondationConntactFragment;
import com.podrzizivot.project.fragments.FondationAmbasadorsFragment;
import com.podrzizivot.project.fragments.FondationFriendsFragment;
import com.podrzizivot.project.fragments.FondationKidsFragment;
import com.podrzizivot.project.fragments.FondationNewsFragment;
import com.podrzizivot.project.fragments.FondationWhosHelpFragment;
import com.podrzizivot.project.fragments.FondationOverallFragment;
import com.tapadoo.alerter.Alerter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_SMS = 3456;
    private static final int REQUEST_READ_PHONE_STATE = 8642;
    private static final String MY_PREFS_NAME = "Prefs_2104";
    private Dialog messageDialog;
    private Button sendSMS;
    private Toast toast;
    private String message;
    private TextView uplacenoNovcatxt;
    private static final String mobNo = "5757";
    private boolean viewIsAtHome;
    private ViewPager pager;
    private int brojUplata = 0;
    private String uplateStr;
    private FloatingActionButton fab;
    private EditText textMessage;
    private String SENT = "SMS_SENT";
    private String DELIVERED = "SMS_DELIVERED";
    private PendingIntent sentPI,deliveredPI;
    private BroadcastReceiver smsSentReceiver,smsDeliveredReceiver;

    //    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        //View view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        uplacenoNovcatxt = header.findViewById(R.id.uplaceno_novca);


        SelectionPagerAdapter pagerAdapter = new SelectionPagerAdapter(fragmentManager);
        pager = findViewById(R.id.tabPager);
        pager.setAdapter(pagerAdapter);

        //Attach the ViewPager to the TabLayout
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        brojUplata = prefs.getInt("brojUplata", 0); //0 is the default value.
        uplateStr = brojUplata + " dinara";
        if(uplacenoNovcatxt!=null)
            uplacenoNovcatxt.setText(uplateStr);


//        if(getIntent()!=null){
//            index = getIntent().getIntExtra("index", 0);
//        }
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                messageDialog = new Dialog(MainActivity.this);
                if (messageDialog.getWindow() != null) {
                    messageDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                }
                messageDialog.setContentView(View.inflate(MainActivity.this, R.layout.send_message, null));
                sendSMS = messageDialog.findViewById(R.id.sendSMS);
                textMessage = messageDialog.findViewById(R.id.textMessage);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                            == PackageManager.PERMISSION_GRANTED)
                    {
                        sendSMS.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                sendSMS.setEnabled(false);
                                message = textMessage.getText().toString();
                                if (message.length() > 0) {
                                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                                                == PackageManager.PERMISSION_GRANTED)
                                        {
                                            SmsManager sms = SmsManager.getDefault();
                                            sms.sendTextMessage(mobNo, null, message, sentPI, deliveredPI);
                                        } else {
//                                            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//                                                    Manifest.permission.SEND_SMS)) {
//                                                Toast.makeText(getApplicationContext(),
//                                                        "Potrebna je određena dozvola za slanje poruka.Molimo Vas da prihvatite i tako pomognete deci slanjem poruka.Hvala Vam.",
//                                                        Toast.LENGTH_LONG).show();
//                                            }
                                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS);
                                        }
                                } else {
                                    if (toast != null)
                                        toast.cancel();
                                    toast = Toast.makeText(getApplicationContext(), "Morate uneti barem jedan karakter.", Toast.LENGTH_LONG);
                                    toast.show();
                                    sendSMS.setEnabled(true);
                                }
                            }
                        });

                    } else {
//                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//                                Manifest.permission.READ_PHONE_STATE)) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Potrebna je određena dozvola za slanje poruka.Molimo Vas da prihvatite i tako pomognete deci slanjem poruka.Hvala Vam.",
//                                    Toast.LENGTH_LONG).show();
//                        }
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);

                    }
                } else {
                    sendSMS.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            sendSMS.setEnabled(false);
                            message = textMessage.getText().toString();
                            if (message.length() > 0) {
                                    SmsManager sms = SmsManager.getDefault();
                                    sms.sendTextMessage(mobNo, null, message, sentPI, deliveredPI);
                            } else {
                                if (toast != null)
                                    toast.cancel();
                                toast = Toast.makeText(getApplicationContext(), "Morate uneti barem jedan karakter.", Toast.LENGTH_LONG);
                                toast.show();
                                sendSMS.setEnabled(true);
                            }
                        }
                    });
                }



                //ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.SEND_SMS}, 1);
                final ImageView imageViewBack = messageDialog.findViewById(R.id.dialog_back_message);
                messageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                messageDialog.show();
                imageViewBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        messageDialog.dismiss();
                    }
                });

            }

            //https://stackoverflow.com/questions/30719047/android-m-check-runtime-permission-how-to-determine-if-the-user-checked-nev

        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.pomozimo_deci);


//        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
//        tx.replace(R.id.content_frame, new FondationKidsFragment());
//        tx.commit();

    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        messageDialog.dismiss();
                        Alerter.create(MainActivity.this)
                                .setText("Poruka je poslata.Molimo Vas da sačekate odgovor fondacije.Hvala Vam.")
                                .setDuration(3000)
                                .setIcon(R.mipmap.ic_launcher_round)
                                .enableSwipeToDismiss()
                                .setBackgroundColorRes(R.color.colorAccent)
                                .show();
                        sendSMS.setEnabled(true);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        if (toast != null)
                            toast.cancel();
                        toast = Toast.makeText(getApplicationContext(), "Došlo je do greške u slanju poruke",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        sendSMS.setEnabled(true);
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        if (toast != null)
                            toast.cancel();
                        toast = Toast.makeText(getApplicationContext(), "Došlo je do greške u slanju poruke",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        sendSMS.setEnabled(true);
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        if (toast != null)
                            toast.cancel();
                        toast = Toast.makeText(getApplicationContext(), "Došlo je do greške u slanju poruke",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        sendSMS.setEnabled(true);
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        if (toast != null)
                            toast.cancel();
                        toast = Toast.makeText(getApplicationContext(), "Došlo je do greške u slanju poruke",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        sendSMS.setEnabled(true);
                        break;
                    default:
                        if (toast != null)
                            toast.cancel();
                        toast = Toast.makeText(getApplicationContext(), "Došlo je do greške u slanju poruke",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        sendSMS.setEnabled(true);
                        break;
                }
            }
        };

        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putInt("brojUplata", brojUplata += 100);
                        editor.apply();
                        uplateStr = brojUplata + " dinara";
                        if (uplacenoNovcatxt != null)
                            uplacenoNovcatxt.setText(uplateStr);
                        sendSMS.setEnabled(true);
                        //promeniTextUplata();
//                        Alerter.create(MainActivity.this)
//                                .setText("Novac je uplaćen.Hvala Vam.")
//                                .setDuration(2300)
//                                .setIcon(R.mipmap.ic_launcher_round)
//                                .enableSwipeToDismiss()
//                                .setBackgroundColorRes(R.color.colorPrimaryDark)
//                                .show();
                        break;
                    case Activity.RESULT_CANCELED:
                        sendSMS.setEnabled(true);
                        fab.setEnabled(true);
                        if (toast != null)
                            toast.cancel();
                        toast = Toast.makeText(getApplicationContext(), "Došlo je do greške u slanju poruke.Molimo Vas pokušajte ponovo.",
                                Toast.LENGTH_LONG);
                        toast.show();
                        break;
                }
            }
        };

        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_facebook) {
            Intent intent;
            try {
                this.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/732473596832681"));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } catch (Exception e) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/PodrziZivot/"));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }
        }
        if (id == R.id.action_instagram) {
            Intent intent;
            try {
                Uri uri = Uri.parse("http://instagram.com/_u/podrzizivot/");
                intent = new Intent(Intent.ACTION_VIEW, uri);

                intent.setPackage("com.instagram.android");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/podrzizivot/")));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            return true;
        }
        if (id == R.id.action_twitter) {
            String twitter_user_name = "PodrziZivot";
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitter_user_name)));
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + twitter_user_name)));
            }
            return true;
        }
        if (id == R.id.action_youtube) {
            Intent intent;
            try {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setPackage("com.google.android.youtube");
                intent.setData(Uri.parse("https://www.youtube.com/channel/UCsrFpilqQu6cnyp3Cxjdjsg/feed"));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } catch (ActivityNotFoundException e) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/channel/UCsrFpilqQu6cnyp3Cxjdjsg/feed"));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        displaySelectedScreen(item.getItemId());
//        item.setChecked(true);
//
////        Fragment fragment = null;
//
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.pomozimo_deci) {
////            Bundle bundle = new Bundle();
////            bundle.putInt("index_bundle", index);
////            fragment = new FondationKidsFragment();
//            pager.setCurrentItem(0);
//            viewIsAtHome = true;
////            fragment.setArguments(bundle);
//        } else if (id == R.id.aktuelnosti) {
////            fragment = new FondationNewsFragment();
//            pager.setCurrentItem(1);
//            viewIsAtHome = false;
//        } else if (id == R.id.pomogli_smo) {
////            fragment = new FondationWhosHelpFragment();
//            pager.setCurrentItem(2);
//            viewIsAtHome = false;
//        } else if (id == R.id.donatori) {
////            fragment = new FondationFriendsFragment();
//            pager.setCurrentItem(3);
//            viewIsAtHome = false;
//        } else if (id == R.id.ambasadori) {
////            fragment = new FondationAmbasadorsFragment();
//            pager.setCurrentItem(4);
//            viewIsAtHome = false;
//        } else if (id == R.id.o_nama) {
////            fragment = new FondationOverallFragment();
//            pager.setCurrentItem(5);
//            viewIsAtHome = false;
//        }else if(id == R.id.kontakt){
////            fragment = new FondationConntactFragment();
//            pager.setCurrentItem(6);
//            viewIsAtHome = false;
//        }
//
//        if (fragment != null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction ft = fragmentManager.beginTransaction();
//            ft.replace(R.id.content_frame, fragment);
//            ft.commit();
//        }
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!viewIsAtHome) { //if the current view is not the News fragment
            displaySelectedScreen(R.id.pomozimo_deci); //display the News fragment
            //onNavigationItemSelected(item);
        } else {
            super.onBackPressed();//If view is in News fragment, exit application
        }
    }

    private void displaySelectedScreen(int itemId) {


        //initializing the fragment object which is selected
//        FragmentTransaction ft;
        switch (itemId) {
            case R.id.pomozimo_deci:
//                if (fragment != null) {
//                    ft = fragmentManager.beginTransaction();
//                    ft.remove(fragment).commit();
//                }
//                fragment = null;
//                fragment = new FondationKidsFragment();
                pager.setCurrentItem(0);
                Log.i("KLASAIZNAD", "1");
                viewIsAtHome = true;
                break;
            case R.id.aktuelnosti:
//                if (fragment != null) {
//                    ft = fragmentManager.beginTransaction();
//                    ft.remove(fragment).commit();
//                }
//                fragment = null;
//                fragment = new FondationNewsFragment();
                pager.setCurrentItem(1);
                Log.i("KLASAIZNAD", "2");
                viewIsAtHome = false;
                break;
            case R.id.pomogli_smo:
//                if (fragment != null) {
//                    ft = fragmentManager.beginTransaction();
//                    ft.remove(fragment).commit();
//                }
//                fragment = null;
//                fragment = new FondationWhosHelpFragment();
                pager.setCurrentItem(2);
                Log.i("KLASAIZNAD", "3");
                viewIsAtHome = false;
                break;
            case R.id.donatori:
//                if (fragment != null) {
//                    ft = fragmentManager.beginTransaction();
//                    ft.remove(fragment).commit();
//                }
//                fragment = null;
//                fragment = new FondationFriendsFragment();
                pager.setCurrentItem(3);
                Log.i("KLASAIZNAD", "4");
                viewIsAtHome = false;
                break;
            case R.id.ambasadori:
//                if (fragment != null) {
//                    ft = fragmentManager.beginTransaction();
//                    ft.remove(fragment).commit();
//                }
//                fragment = null;
//                fragment = new FondationAmbasadorsFragment();
                pager.setCurrentItem(4);
                Log.i("KLASAIZNAD", "5");
                viewIsAtHome = false;
                break;
            case R.id.o_nama:
//                if (fragment != null) {
//                    ft = fragmentManager.beginTransaction();
//                    ft.remove(fragment).commit();
//                }
//                fragment = null;
//                fragment = new FondationOverallFragment();
                pager.setCurrentItem(5);
                Log.i("KLASAIZNAD", "6");
                viewIsAtHome = false;
                break;
            case R.id.kontakt:
//                if (fragment != null) {
//                    ft = fragmentManager.beginTransaction();
//                    ft.remove(fragment).commit();
//                }
//                fragment = null;
//                fragment = new FondationConntactFragment();
                pager.setCurrentItem(6);
                Log.i("KLASAIZNAD", "7");
                viewIsAtHome = false;
                break;
            case R.id.spisak_uplata:
                String url = "http://www.podrzizivot.com/wp-content/uploads/2018/03/spisak-donatora-2018.pdf";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.postani_volonter:
                String url_help = "https://www.podrzizivot.com/aktuelno/#donator";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url_help));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.potrebna_pomoc:
                String url_help_need = "https://www.podrzizivot.com/aktuelno/#pomoc";
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse(url_help_need));
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }

//        if (fragment != null) {
////            FragmentManager fragmentManager = getSupportFragmentManager();
//            ft = fragmentManager.beginTransaction();
//            ft.replace(R.id.content_frame, fragment);
//            ft.commit();
//        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    //Method to send SMS.
//    private void sendSMS(String mobNo, String message) {
//        Log.i("KOLIKK", "konj1");
////        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
////        editor.putInt("brojUplata", brojUplata+=100);
////        editor.apply();
////        uplateStr = brojUplata + " dinara";
////        if(uplacenoNovcatxt!=null)
////            uplacenoNovcatxt.setText(uplateStr);
//        String smsSent = "SMS_SENT";
//        String smsDelivered = "SMS_DELIVERED";
////        https://www.youtube.com/watch?v=9VLPa8_tcQE
//
//        // Receiver for Sent SMS.
//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context arg0, Intent arg1) {
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        messageDialog.dismiss();
//                        fab.setEnabled(false);
//                        Alerter.create(MainActivity.this)
//                                .setText("Poruka je poslata.Molimo Vas da sačekate odgovor fondacije.Hvala Vam.")
//                                .setDuration(3000)
//                                .setIcon(R.mipmap.ic_launcher_round)
//                                .enableSwipeToDismiss()
//                                .setBackgroundColorRes(R.color.colorAccent)
//                                .show();
//                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                            editor.putInt("brojUplata", brojUplata += 100);
//                            editor.apply();
//                            uplateStr = brojUplata + " dinara";
//                            if (uplacenoNovcatxt != null)
//                                uplacenoNovcatxt.setText(uplateStr);
//                        Log.i("KOLIKK", "konj3");
//                        break;
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        if (toast != null)
//                            toast.cancel();
//                        toast = Toast.makeText(getApplicationContext(), "Došlo je do greške u slanju poruke",
//                                Toast.LENGTH_SHORT);
//                        toast.show();
//                        sendSMS.setEnabled(true);
//                        break;
//                    case SmsManager.RESULT_ERROR_NO_SERVICE:
//                        if (toast != null)
//                            toast.cancel();
//                        toast = Toast.makeText(getApplicationContext(), "Došlo je do greške u slanju poruke",
//                                Toast.LENGTH_SHORT);
//                        toast.show();
//                        sendSMS.setEnabled(true);
//                        break;
//                    case SmsManager.RESULT_ERROR_NULL_PDU:
//                        if (toast != null)
//                            toast.cancel();
//                        toast = Toast.makeText(getApplicationContext(), "Došlo je do greške u slanju poruke",
//                                Toast.LENGTH_SHORT);
//                        toast.show();
//                        sendSMS.setEnabled(true);
//                        break;
//                    case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        if (toast != null)
//                            toast.cancel();
//                        toast = Toast.makeText(getApplicationContext(), "Došlo je do greške u slanju poruke",
//                                Toast.LENGTH_SHORT);
//                        toast.show();
//                        sendSMS.setEnabled(true);
//                        break;
//                    default:
//                        if (toast != null)
//                            toast.cancel();
//                        toast = Toast.makeText(getApplicationContext(), "Došlo je do greške u slanju poruke",
//                                Toast.LENGTH_SHORT);
//                        toast.show();
//                        sendSMS.setEnabled(true);
//                        break;
//                }
//            }
//        }, new IntentFilter(smsSent));
//
//        // Receiver for Delivered SMS.
//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context arg0, Intent arg1) {
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        sendSMS.setEnabled(true);
//                        fab.setEnabled(true);
//                        //promeniTextUplata();
////                        Alerter.create(MainActivity.this)
////                                .setText("Novac je uplaćen.Hvala Vam.")
////                                .setDuration(2300)
////                                .setIcon(R.mipmap.ic_launcher_round)
////                                .enableSwipeToDismiss()
////                                .setBackgroundColorRes(R.color.colorPrimaryDark)
////                                .show();
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        sendSMS.setEnabled(true);
//                        fab.setEnabled(true);
//                        if (toast != null)
//                            toast.cancel();
//                        toast = Toast.makeText(getApplicationContext(), "Došlo je do greške u slanju poruke.Molimo Vas pokušajte ponovo.",
//                                Toast.LENGTH_LONG);
//                        toast.show();
//                        break;
//                }
//            }
//        }, new IntentFilter(smsDelivered));
//
//
//        SmsManager sms = SmsManager.getDefault();
//        sms.sendTextMessage(mobNo, null, message, sentPI, deliveredPI);
//    }

//    private void promeniTextUplata() {
//
//        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//        brojUplata = prefs.getInt("brojUplata", 0); //0 is the default value.
//        Log.i("BROJUPLATA", brojUplata + "");
//        uplateStr = brojUplata + " dinara";
//        if(uplacenoNovcatxt!=null)
//            uplacenoNovcatxt.setText(uplateStr);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUEST_SMS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Hvala Vam na podršci.", Toast.LENGTH_LONG).show();
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(mobNo, null, message, sentPI, deliveredPI);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Potrebna je određena dozvola za slanje poruka.Molimo Vas da prihvatite i tako pomognete deci slanjem poruka.Hvala Vam.",
                        Toast.LENGTH_LONG).show();
                messageDialog.dismiss();
            }

        }
        if (requestCode == REQUEST_READ_PHONE_STATE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        sendSMS.setEnabled(false);
                        message = textMessage.getText().toString();
                        if (message.length() > 0) {
                            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                                    == PackageManager.PERMISSION_GRANTED)
                            {
                                SmsManager sms = SmsManager.getDefault();
                                sms.sendTextMessage(mobNo, null, message, sentPI, deliveredPI);
                            } else {
//                                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//                                        Manifest.permission.SEND_SMS)) {
//                                    Toast.makeText(getApplicationContext(),
//                                            "Potrebna je određena dozvola za slanje poruka.Molimo Vas da prihvatite i tako pomognete deci slanjem poruka.Hvala Vam.",
//                                            Toast.LENGTH_LONG).show();
//                                }
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS);

                            }
                        } else {
                            if (toast != null)
                                toast.cancel();
                            toast = Toast.makeText(getApplicationContext(), "Morate uneti barem jedan karakter.", Toast.LENGTH_LONG);
                            toast.show();
                            sendSMS.setEnabled(true);
                        }
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(),
                        "Potrebna je određena dozvola za slanje poruka.Molimo Vas da prihvatite i tako pomognete deci slanjem poruka.Hvala Vam.",
                        Toast.LENGTH_LONG).show();
                messageDialog.dismiss();
            }
        }
    }

    private class SelectionPagerAdapter extends FragmentPagerAdapter {

        //        private FragmentManager fm;
        private Fragment fragment;
//        private FragmentTransaction ft = null;

        SelectionPagerAdapter(FragmentManager fm) {
            super(fm);
//            this.fm = fm;
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
//                    navigationView.getMenu().findItem(R.id.pomozimo_deci).setChecked(true);
//                    displaySelectedScreen(R.id.pomozimo_deci);
//                    if(getActionBar()!=null)
//                    getActionBar().setTitle("Pomozimo deci");
                    Log.i("PRIMERKO", "1");
                    fragment = new FondationKidsFragment();
//                    ft = fm.beginTransaction();
//                    ft.replace(R.id.content_frame, fragment);
//                    ft.commit();
                    return fragment;
                case 1:
                    viewIsAtHome = false;
//                    navigationView.getMenu().findItem(R.id.aktuelnosti).setChecked(true);
//                    displaySelectedScreen(R.id.aktuelnosti);
//                    if(getActionBar()!=null)
//                        getActionBar().setTitle("Aktuelnosti");
                    Log.i("PRIMERKO", "2");
                    fragment = new FondationNewsFragment();
//                    ft = fm.beginTransaction();
//                    ft.replace(R.id.content_frame, fragment);
//                    ft.commit();
                    return fragment;
                case 2:
                    viewIsAtHome = false;
//                    navigationView.getMenu().findItem(R.id.pomogli_smo).setChecked(true);
//                    displaySelectedScreen(R.id.pomogli_smo);
//                    if(getActionBar()!=null)
//                        getActionBar().setTitle("Pomogli smo");
                    Log.i("PRIMERKO", "3");
                    fragment = new FondationWhosHelpFragment();
//                    ft = fm.beginTransaction();
//                    ft.replace(R.id.content_frame, fragment);
//                    ft.commit();
                    return fragment;
                case 3:
                    viewIsAtHome = false;
//                    navigationView.getMenu().findItem(R.id.donatori).setChecked(true);
//                    displaySelectedScreen(R.id.donatori);
//                    if(getActionBar()!=null)
//                        getActionBar().setTitle("Donatori");
                    Log.i("PRIMERKO", "4");
                    fragment = new FondationFriendsFragment();
//                    ft = fm.beginTransaction();
//                    ft.replace(R.id.content_frame, fragment);
//                    ft.commit();
                    return fragment;
                case 4:
                    viewIsAtHome = false;
//                    navigationView.getMenu().findItem(R.id.ambasadori).setChecked(true);
//                    displaySelectedScreen(R.id.ambasadori);
//                    if(getActionBar()!=null)
//                        getActionBar().setTitle("Ambasadori");
                    Log.i("PRIMERKO", "5");
                    fragment = new FondationAmbasadorsFragment();
//                    ft = fm.beginTransaction();
//                    ft.replace(R.id.content_frame, fragment);
//                    ft.commit();
                    return fragment;
                case 5:
                    viewIsAtHome = false;
//                    navigationView.getMenu().findItem(R.id.o_nama).setChecked(true);
//                    displaySelectedScreen(R.id.o_nama);
//                    if(getActionBar()!=null)
//                        getActionBar().setTitle("O nama");
                    Log.i("PRIMERKO", "6");
                    fragment = new FondationOverallFragment();
//                    ft = fm.beginTransaction();
//                    ft.replace(R.id.content_frame, fragment);
//                    ft.commit();
                    return fragment;
                case 6:
                    viewIsAtHome = false;
//                    navigationView.getMenu().findItem(R.id.kontakt).setChecked(true);
//                    displaySelectedScreen(R.id.kontakt);
//                    if(getActionBar()!=null)
//                        getActionBar().setTitle("Kontakt");
                    fragment = new FondationConntactFragment();
//                    ft = fm.beginTransaction();
//                    ft.replace(R.id.content_frame, fragment);
//                    ft.commit();
                    return fragment;
            }
            return null;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Pomozimo deci";
                case 1:
                    return "Aktuelnosti";
                case 2:
                    return "Pomogli smo";
                case 3:
                    return "Donatori";
                case 4:
                    return "Ambasadori";
                case 5:
                    return "O nama";
                case 6:
                    return "Kontakt";
            }
            return null;
        }
    }
}
