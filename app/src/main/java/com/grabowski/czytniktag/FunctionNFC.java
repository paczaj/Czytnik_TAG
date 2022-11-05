package com.grabowski.czytniktag;

import android.content.Context;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;

public class FunctionNFC {
    //Zapis sms do tag
    public static void writeSMS(Tag tag, String phoneNumber, String textMessage, Context context) {
        String smsUri = "sms:" + phoneNumber + "?body=" + textMessage;
        NdefRecord smsUriRecord = NdefRecord.createUri(smsUri);
        NdefMessage smsMessage = new NdefMessage(smsUriRecord);

        Ndef ndef = Ndef.get(tag);
        try {
            ndef.connect();
            ndef.writeNdefMessage(smsMessage);
            ndef.close();
            Toast.makeText(context, "Zapisano SMS TAG", Toast.LENGTH_SHORT).show();
        } catch (IOException | FormatException e) {
            Toast.makeText(context, "Błąd zapisu, ponów próbę", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //Zapis linku do tag
    public static void writeWebLink(Tag tag, String protocol, String webLink, Context context) {
        Uri uri = Uri.parse(protocol + webLink);
        NdefRecord recordNFC = NdefRecord.createUri(uri);
        NdefMessage webMessage = new NdefMessage(recordNFC);

        Ndef ndef = Ndef.get(tag);
        try {
            ndef.connect();
            ndef.writeNdefMessage(webMessage);
            ndef.close();
            Toast.makeText(context, "Zapisano Link WWW TAG", Toast.LENGTH_SHORT).show();
        } catch (IOException | FormatException e) {
            Toast.makeText(context, "Błąd zapisu, ponów próbę", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //Zapis telefonu do tag
    public static void writeNumberPhone(Tag tag, String tel, Context context) {
        String telUri = "tel:" + tel;
        NdefRecord telUriRecord = NdefRecord.createUri(telUri);
        NdefMessage telMessage = new NdefMessage(telUriRecord);

        Ndef ndef = Ndef.get(tag);
        try {
            ndef.connect();
            ndef.writeNdefMessage(telMessage);
            ndef.close();
            Toast.makeText(context, "Zapisano numer do TAG", Toast.LENGTH_SHORT).show();
        } catch (IOException | FormatException e) {
            Toast.makeText(context, "Błąd zapisu, ponów próbę", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //Zapis kontaktu do tag
    public static void writeContact(Tag tag, NdefMessage message, Context context) {

        Ndef ndef = Ndef.get(tag);
        try {
            ndef.connect();
            ndef.writeNdefMessage(message);
            ndef.close();
            Toast.makeText(context, "Zapisano Kontakt TAG", Toast.LENGTH_SHORT).show();
        } catch (IOException | FormatException e) {
            Toast.makeText(context, "Błąd zapisu, ponów próbę", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //Zapis do aplikacji maps do tag
    public static void writeAdressGMaps(Tag tag, String adress, Context context) {
        String telUri = "google.navigation:q=" + adress;
        NdefRecord telUriRecord = NdefRecord.createUri(telUri);
        NdefMessage telMessage = new NdefMessage(telUriRecord);

        Ndef ndef = Ndef.get(tag);
        try {
            ndef.connect();
            ndef.writeNdefMessage(telMessage);
            ndef.close();
            Toast.makeText(context, "Zapisano Adres do TAG", Toast.LENGTH_SHORT).show();
        } catch (IOException | FormatException e) {
            Toast.makeText(context, "Błąd zapisu, ponów próbę", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //Zapis do aplikacji maps do tag
    public static void writeAdressMaps(Tag tag, String adress, Context context) {
        String telUri = "geo:0,0?q=" + adress;
        NdefRecord telUriRecord = NdefRecord.createUri(telUri);
        NdefMessage telMessage = new NdefMessage(telUriRecord);

        Ndef ndef = Ndef.get(tag);
        try {
            ndef.connect();
            ndef.writeNdefMessage(telMessage);
            ndef.close();
            Toast.makeText(context, "Zapisano Adres do TAG", Toast.LENGTH_SHORT).show();
        } catch (IOException | FormatException e) {
            Toast.makeText(context, "Błąd zapisu, ponów próbę", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //Tworzy vcard, do kontaktów
    public static NdefRecord createVcardRecord(String name, String surname, String org, String tel, String email) throws UnsupportedEncodingException {
        String payloadStr = "BEGIN:VCARD" + "\n" +
                "VERSION:2.1" + "\n" +
                "N:;" + surname + ";" + name + "\n" +
                "ORG:" + org + "\n" +
                "TEL:" + tel + "\n" +
                "EMAIL:" + email + "\n" +
                "END:VCARD";

        byte[] uriField = payloadStr.getBytes(Charset.forName("UTF-8"));
        byte[] payload = new byte[uriField.length + 1];
        System.arraycopy(uriField, 0, payload, 1, uriField.length);
        NdefRecord nfcRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/vcard".getBytes(), new byte[0], payload);

        return nfcRecord;
    }

    public static void writeText(Tag tag, String text, Context context) {
        NdefRecord ndefRecord = createTextRecord(text);
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});
        Ndef ndef = Ndef.get(tag);

        try {
            ndef.connect();
            ndef.writeNdefMessage(ndefMessage);
            ndef.close();
            Toast.makeText(context, "Zapisano tekst do TAG", Toast.LENGTH_SHORT).show();
        } catch (IOException | FormatException e) {
            Toast.makeText(context, "Błąd zapisu, ponów próbę", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static String readTextFromMessage(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if (ndefRecords != null && ndefRecords.length > 0) {
            NdefRecord ndefRecord = ndefRecords[0];
            String tagContent = getTextFromNdefRecord(ndefRecord);
            Log.d("readTextFromMSG", tagContent);
            return tagContent;
        } else {
            return "brak";
        }
    }

    public static String getTextFromNdefRecord(NdefRecord ndefRecord) {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();

                String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
                int languageSize = payload[0] & 0063;
                tagContent = new String(payload, languageSize + 1, payload.length - languageSize - 1, textEncoding);
                Log.d("getTextFromNdefRecord", tagContent);
        } catch (UnsupportedEncodingException | ArrayIndexOutOfBoundsException e) {
            return "";
        }
        return tagContent;
    }

    //Tworzenie wiadomości zrozumiały dla zapisu tag
    private static NdefRecord createTextRecord(String content) {
        try {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");

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

    //usuwanie zawartości tag
    public static void deleteMemTag(Tag tag, Context context) {
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{
                new NdefRecord(NdefRecord.TNF_EMPTY, null, null, null)
        });
        Ndef ndef = Ndef.get(tag);
        try {
            ndef.connect();
            ndef.writeNdefMessage(ndefMessage);
            ndef.close();
            Toast.makeText(context, "Zawartość została skasowana", Toast.LENGTH_SHORT).show();
        } catch (IOException | FormatException e) {
            Toast.makeText(context, "Błąd kasowania zawartości, ponów próbę", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
