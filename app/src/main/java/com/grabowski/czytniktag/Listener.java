package com.grabowski.czytniktag;

import android.nfc.NdefMessage;
import android.nfc.Tag;

public interface Listener {
    //aktywacja użycia NFC w intent, dzięki czemu bez okienka potwierdzającego nie wykona się polecenie
    void onDisplayDialog();
    void onDialogDismissed();

    void setActualView(int x);
        //0 - load
        //1 - save
        //2 - delete

    //bez sensu trzeba zrobić w main override
    void setWriteMode(int x);

    //NdefMessage createNdefMessage(String content);
    //void writeNdefMessage(Tag tag, NdefMessage ndefMessage);

    void setPhoneNumber(String x);
    void setTextMessage(String x);
    void setWebLink(String protocol, String webLink);
    void setVcard(String name, String surname, String organisation, String telephone, String email);
    void setText(String text);
    void setAddressMaps(String address);
}
