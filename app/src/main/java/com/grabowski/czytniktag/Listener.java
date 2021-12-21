package com.grabowski.czytniktag;

import android.nfc.NdefMessage;
import android.nfc.Tag;

public interface Listener {
    void onDisplayDialog();
    void onDialogDismissed();
    void setActualView(int x);
    NdefMessage createNdefMessage(String content);
    void writeNdefMessage(Tag tag, NdefMessage ndefMessage);
}
