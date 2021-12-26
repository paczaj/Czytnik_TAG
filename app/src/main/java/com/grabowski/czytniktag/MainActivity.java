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

public class MainActivity extends AppCompatActivity implements Listener {

    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdapter adapter;

    //Intialize attributes for NFC
    NfcAdapter nfcAdapter;
    boolean isDialogDisplayed = false;
    String tagResultScan = null;
    int numberOfFragment = 0;
    int writeMode = 0;

    //Variable SaveTag
    String phoneNumber;
    String textMessage;
    String webLink;
    String protocol;
    String name;
    String surname;
    String organisation;
    String email;

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

                //Ustala na jakiej karcie aktualie user jest, TAG jest wykonywany jak powinien być
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

//        Email Button
//        FloatingActionButton fab = binding.fab;
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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
            vibration();
            Toast.makeText(this, "Wykryto TAG, wybierz opcję!", Toast.LENGTH_SHORT).show();
            //Operacja usuwania
            if (numberOfFragment == 2 && isDialogDisplayed == true) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                NdefMessage ndefMessage = createNdefMessage("");
                writeNdefMessage(tag, ndefMessage);

                Toast.makeText(this, "Kasowanie TAG", Toast.LENGTH_SHORT).show();
            }

            //operacja odczytu
            if (numberOfFragment == 0 && isDialogDisplayed == true) {
                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                if (parcelables != null && parcelables.length > 0) {
                    readTextFromMessage((NdefMessage) parcelables[0]);
                    Toast.makeText(this, "Zeskanowano", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Brak treści w TAG", Toast.LENGTH_SHORT).show();
                }
            }
            //Operacja zapisu
            if (numberOfFragment == 1 && isDialogDisplayed == true) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                if (writeMode == 1) {

                    //TUTAJ POBRAC DANE Z KLASY SAVETAG I WSTAWIC DO WRITE SMS
                    //Toast.makeText(this, phoneNumber, Toast.LENGTH_SHORT).show();
                    writeSMS(tag, phoneNumber, textMessage);
                    //Toast.makeText(this, "Writemode1", Toast.LENGTH_SHORT).show();
                    }

                if (writeMode == 2) {
                    NdefRecord[] records = new NdefRecord[1];
                    try {
                        records[0] = createVcardRecord(name, surname,organisation, phoneNumber, email);
                    } catch (UnsupportedEncodingException e) {
                    }
                    NdefMessage contactMessage = new NdefMessage(records);

                    writeContact(tag, contactMessage); // your tag writing function
                }

                if (writeMode == 3) {
                    writeWebLink(tag, protocol, webLink);
                }

                if (writeMode == 0) {

                    Toast.makeText(this, "Writemode0", Toast.LENGTH_SHORT).show();
                }
                }

//                Ndef ndef = Ndef.get(mytag);
//                try {
//                    ndef.connect();
//                    //dla kontaktów
//                    ndef.writeNdefMessage(contactMessage);
//                    ndef.close();
//                    Toast.makeText(this, "Zapisano link", Toast.LENGTH_SHORT).show();
//                } catch (IOException | FormatException e) {
//                    Toast.makeText(this, "Error zapisu", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
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

//MOZNA POPRAWIC TUTAJ
    @SuppressLint("MissingPermission")
    private void vibration() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(250);
        }
    }

    private void writeSMS(Tag tag, String phoneNumber, String textMessage) {
        String smsUri = "sms:" + phoneNumber + "?body=" + textMessage;
        NdefRecord smsUriRecord = NdefRecord.createUri(smsUri);
        NdefMessage smsMessage = new NdefMessage(smsUriRecord);

        Ndef ndef = Ndef.get(tag);
        try {
            ndef.connect();
            ndef.writeNdefMessage(smsMessage);
            ndef.close();
            Toast.makeText(this, "Zapisano SMS TAG", Toast.LENGTH_SHORT).show();
        } catch (IOException | FormatException e) {
            Toast.makeText(this, "Błąd zapisu, ponów próbę", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void writeWebLink(Tag tag, String protocol, String webLink) {
        Uri uri = Uri.parse(protocol + webLink);
        NdefRecord recordNFC = NdefRecord.createUri(uri);
        NdefMessage webMessage = new NdefMessage(recordNFC);

        Ndef ndef = Ndef.get(tag);
        try {
            ndef.connect();
            ndef.writeNdefMessage(webMessage);
            ndef.close();
            Toast.makeText(this, "Zapisano Link WWW TAG", Toast.LENGTH_SHORT).show();
        } catch (IOException | FormatException e) {
            Toast.makeText(this, "Błąd zapisu, ponów próbę", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void writeContact(Tag tag, NdefMessage message) {

        Ndef ndef = Ndef.get(tag);
        try {
            ndef.connect();
            ndef.writeNdefMessage(message);
            ndef.close();
            Toast.makeText(this, "Zapisano Kontakt TAG", Toast.LENGTH_SHORT).show();
        } catch (IOException | FormatException e) {
            Toast.makeText(this, "Błąd zapisu, ponów próbę", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public NdefRecord createVcardRecord(String name, String surname, String org, String tel, String email)
            throws UnsupportedEncodingException {

        String payloadStr = "BEGIN:VCARD" +"\n"+
                "VERSION:2.1" +"\n" +
                "N:;" + surname + ";" + name + "\n" +
                "ORG:"+org+"\n"+
                "TEL:"+tel+"\n"+
                "EMAIL:"+email+"\n"+
                "END:VCARD";
        byte[] uriField = payloadStr.getBytes(Charset.forName("US-ASCII"));
        byte[] payload = new byte[uriField.length + 1];
        System.arraycopy(uriField, 0, payload, 1, uriField.length);
        NdefRecord nfcRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/vcard".getBytes(), new byte[0], payload);

        return nfcRecord;
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if (ndefRecords != null && ndefRecords.length > 0) {
            NdefRecord ndefRecord = ndefRecords[0];
            String tagContent = getTextFromNdefRecord(ndefRecord);
            Toast.makeText(this, tagContent, Toast.LENGTH_SHORT).show();
            tagResultScan = tagContent;
        }
    }

    public String getTextFromNdefRecord(NdefRecord ndefRecord) {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1, payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }

    private void formatTag(Tag tag, NdefMessage ndefMessage) {
        try {
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if (ndefFormatable == null) {
                Toast.makeText(this, "Tag is not ndef formatable", Toast.LENGTH_SHORT).show();
            }

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();
        } catch (Exception e) {
            Log.e("formatTag", e.getMessage());
        }
    }

    private NdefRecord createTextRecord(String content) {
        try {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("US-ASCII");

            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write((byte) (languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());
        } catch (UnsupportedEncodingException e) {
            Log.e("createTextRecord", e.getMessage());
        }
        return null;
    }

    @Override
    public NdefMessage createNdefMessage(String content) {
        NdefRecord ndefRecord = createTextRecord(content);
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});

        return ndefMessage;
    }

    @Override
    public void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {
        try {
            if (tag == null) {
                Toast.makeText(this, "Tag object cannot be null", Toast.LENGTH_SHORT).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                formatTag(tag, ndefMessage);
            } else {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Toast.makeText(this, "Tag is not writable", Toast.LENGTH_SHORT).show();
                    ndef.close();
                    return;
                }

                Toast.makeText(this, "Operacja udana", Toast.LENGTH_SHORT).show();
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
            }
        } catch (Exception e) {
            Log.e("writeNdefMessange", e.getMessage());
        }
    }

    @Override
    public void onDisplayDialog() {
        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {
        isDialogDisplayed = false;
    }

    @Override
    public void setWriteMode(int x) {
        //0 - text
        //1 - sms
        //2 - contact
        //3 - www
        //4 - flash
        writeMode = x;
    }

    @Override
    public void setActualView(int x){
        //0 - Load TAG
        //1 - Save TAG
        //2 - Delete TAG
        numberOfFragment = x;
    }

    @Override
    public void setPhoneNumber(String x){
        phoneNumber = x;
    }

    @Override
    public void setTextMessage(String x){
        textMessage = x;
    }

    @Override
    public void setWebLink(String protocol, String webLink){
        this.protocol = protocol;
        this.webLink = webLink;
    }

    @Override
    public void setVcard(String name, String surname, String organisation, String phoneNumber, String email){
        this.name = name;
        this.surname = surname;
        this.organisation = organisation;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}