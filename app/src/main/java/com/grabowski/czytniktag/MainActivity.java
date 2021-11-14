package com.grabowski.czytniktag;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.grabowski.czytniktag.ui.main.SectionsPagerAdapter;
import com.grabowski.czytniktag.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

//  Intialize attributes for NFC
//    NfcAdapter nfcAdapter;
//    PendingIntent pendingIntent;
//    final static String TAG = "nfc_test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//      Initialise NfcAdapter
//        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//
//      If no NfcAdapter, display that the device has no NFC
//        if (nfcAdapter == null){
//            Toast.makeText(this,"Urządzenie nie wspiera NFC!",
//                    Toast.LENGTH_SHORT).show();
//            finish();
//    }
//
//    Create a PendingIntent object so the Android system can
//    populate it with the details of the tag when it is scanned.
//    //PendingIntent.getActivity(Context,requestcode(identifier for
//    //                           intent),intent,int)
//    pendingIntent = PendingIntent.getActivity(this,0,new Intent(this,this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
    }
}