package com.grabowski.czytniktag;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdapter adapter;

    //Intialize attributes for NFC
    private NfcAdapter nfcAdapter;
    private boolean isDialogDisplayed = false;
    private int numberOfFragment = 0;
    private int writeMode = 0;
    private boolean isReadDone = false;

    //Variable SaveTag
    private String phoneNumber;
    private String textMessage;
    private String webLink;
    private String protocol;
    private String name;
    private String surname;
    private String organisation;
    private String email;
    private String textMultiline;
    private String address;

    //Variable LoadTag
    private String idTag;
    private String typeTag;
    private Ndef ndefTag;
    private String sizeTag;
    private boolean writable;
    private String textOnTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        pager2 = findViewById(R.id.view_pager2);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle());
        pager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("Odczytywanie TAG"));
        tabLayout.addTab(tabLayout.newTab().setText("Zapisywanie TAG"));
        tabLayout.addTab(tabLayout.newTab().setText("Usuwanie TAG"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());

                //Ustala na jakiej karcie aktualnie user jest, TAG jest wykonywany jak powinien być
                setActualView(tab.getPosition());
                //Log.d("ACTUAL_FR", "POSITION" + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        //Initialise NfcAdapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        //If no NfcAdapter, display that the device has no NFC
        if (nfcAdapter == null) {
            Toast.makeText(this, "Twoje urządzenie nie wspiera NFC!",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // If the intent caught is a NFC tag, handle it

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {

            //operacja odczytu
            if (numberOfFragment == 0 && isDialogDisplayed == true) {
                vibration();
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

                idTag = byteArrayToHexString(tag.getId());
                ndefTag = Ndef.get(tag);
                sizeTag = String.valueOf(ndefTag.getMaxSize()) + " Bytes";
                writable = ndefTag.isWritable();
                typeTag = ndefTag.getType();

                if (parcelables != null && parcelables.length > 0 ) {
                    textOnTag = FunctionNFC.readTextFromMessage((NdefMessage) parcelables[0]);
                    int sizeByteTextOnTag = textOnTag.getBytes().length;
                    textOnTag = String.valueOf(sizeByteTextOnTag) + " bytes: " + textOnTag;
                } else {
                    textOnTag = "Brak treści";
                }
                //informacja o poprawnym odczytaniu
                isReadDone = true;
            }

            //Operacja zapisu - skończone
            if (numberOfFragment == 1 && isDialogDisplayed == true) {
                vibration();
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

                switch (writeMode) {
                    case 0:
                        FunctionNFC.writeText(tag, textMultiline, this);
                        break;
                    case 1:
                        FunctionNFC.writeSMS(tag, phoneNumber, textMessage, this);
                        break;
                    case 2:
                        FunctionNFC.writeNumberPhone(tag, phoneNumber, this);
                        break;
                    case 3:
                        NdefRecord[] records = new NdefRecord[1];
                        try {
                            records[0] = FunctionNFC.createVcardRecord(name, surname, organisation, phoneNumber, email);
                        } catch (UnsupportedEncodingException e) {
                        }

                        NdefMessage contactMessage = new NdefMessage(records);
                        FunctionNFC.writeContact(tag, contactMessage, this);
                        break;
                    case 4:
                        FunctionNFC.writeWebLink(tag, protocol, webLink, this);
                        break;
                    case 5:
                        FunctionNFC.writeAdressGMaps(tag, address, this);
                        break;
                    case 6:
                        FunctionNFC.writeAdressMaps(tag, address, this);
                        break;
                }
            }

            //Operacja usuwania - nieskończone
            if (numberOfFragment == 2 && isDialogDisplayed == true) {
                vibration();
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                FunctionNFC.deleteMemTag(tag, this);

                isReadDone = true;
                Toast.makeText(this, "Kasowanie zawartości TAG zakończone", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilter = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, null);
        super.onResume();
    }

    @Override
    protected void onPause() {
        nfcAdapter.disableForegroundDispatch(this);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            finish();
    }

    //Wibracje po rozpoznaniu tag
    @SuppressLint("MissingPermission")
    private void vibration() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(250);
        }
    }

    //Konwertowanie na string
    String byteArrayToHexString(byte[] inArray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inArray.length; ++j) {
            in = (int) inArray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    public void onDisplayDialog() {
        isDialogDisplayed = true;
    }

    public void onDialogDismissed() {
        isDialogDisplayed = false;
    }

    public void setWriteMode(int x) {
        //0 - text
        //1 - sms
        //2 - telephone
        //3 - contact
        //4 - www
        //5 - GMaps
        //6 - Maps
        writeMode = x;
    }

    public void setActualView(int x) {
        //0 - Load TAG
        //1 - Save TAG
        //2 - Delete TAG
        numberOfFragment = x;
    }

    public void setPhoneNumber(String x) {
        phoneNumber = x;
    }

    public void setTextMessage(String x) {
        textMessage = x;
    }

    public void setWebLink(String protocol, String webLink) {
        this.protocol = protocol;
        this.webLink = webLink;
    }

    public void setVcard(String name, String surname, String organisation, String phoneNumber, String email) {
        this.name = name;
        this.surname = surname;
        this.organisation = organisation;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public void setText(String textMultiline) {
        this.textMultiline = textMultiline;
    }

    public void setAddressMaps(String address) {
        this.address = address;
    }

    public void offReadDone() {
        isReadDone = false;
    }

    public String getIdTag() {
        return idTag;
    }

    public String getTypeTag() {
        return typeTag;
    }

    public String getSizeTag() {
        return sizeTag;
    }

    public boolean isWritable() {
        return writable;
    }

    public boolean isReadDone() {
        return isReadDone;
    }

    public String getTextOnTag() {
        return textOnTag;
    }
}