package com.grabowski.czytniktag;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

public class SaveTag extends Fragment{
//pierwsze testowe
    Button btn;
    Button btnSelectTypeWrite;
    Button btnSMSWrite;
    Button btnConctactWrite;
    Button btnWebLinkWrite;
    Button btnChangeTypeProtocol;
    
    EditText fieldSMSText;
    EditText fieldNumberSMS;
    EditText fieldWebAddress;
    EditText fieldName;
    EditText fieldSurname;
    EditText fieldOrganisation;
    EditText fieldEmail;
    EditText fieldNumberContact;

    TextView textTypeProtocol;

    Group groupSMS;
    Group groupWebLink;
    Group groupContact;

    String phoneNumber;
    String textMessage;
    String typeProtocol;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.save_tag, container, false);

        groupSMS = rootView.findViewById(R.id.groupSMS);
        groupWebLink = rootView.findViewById(R.id.groupWebLink);
        groupContact = rootView.findViewById(R.id.groupContact);

        btnSelectTypeWrite = rootView.findViewById(R.id.selectTypeWrite);
        btnSMSWrite = rootView.findViewById(R.id.btnSMSWrite);
        btnConctactWrite = rootView.findViewById(R.id.btnContactWrite);
        btnWebLinkWrite = rootView.findViewById(R.id.btnWebLinkWrite);
        btnChangeTypeProtocol = rootView.findViewById(R.id.btnChangeTypeProtocol);

        fieldSMSText = rootView.findViewById(R.id.editTextSMS);
        fieldNumberSMS = rootView.findViewById(R.id.editTextPhone);
        fieldWebAddress = rootView.findViewById(R.id.editTextWebAddress);
        fieldName = rootView.findViewById(R.id.editTextName);
        fieldSurname = rootView.findViewById(R.id.editTextSurname);
        fieldOrganisation = rootView.findViewById(R.id.editTextOrganization);
        fieldEmail = rootView.findViewById(R.id.editTextEmail);
        fieldNumberContact = rootView.findViewById(R.id.editTextPhoneContact);

        textTypeProtocol = rootView.findViewById(R.id.textProtocolType);
//testowe
        btn = rootView.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Działa - pobiera jeżeli jest okienko, jezeli anuluj klikniete to false ustawia
                ((MainActivity)getActivity()).onDisplayDialog();
                ((MainActivity)getActivity()).setWriteMode(2);

                AlertDialog.Builder alertScanTag = new AlertDialog.Builder(getActivity())
                        .setTitle("Zapisz TAG")
                        .setMessage("Przyłóż TAG, aby zapisać zawartość")
                        .setCancelable(false);
                alertScanTag.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)getActivity()).onDialogDismissed();
                        dialog.cancel();
                    }
                });
                alertScanTag.show();
            }
        });

        btnSelectTypeWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertSelectTypeTag = new AlertDialog.Builder(getActivity())
                        .setTitle("Wybierz typ zapisu TAG")
                        .setCancelable(false);
                String[] options = {"Tekst", "SMS", "Kontakt", "Adres sieciowy", "Latarka"};

                alertSelectTypeTag.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Aby działało chowanie i pokazywanie, trzeba przypisać do zmiennych elementy wyżej w kodzie
                        clearAllGroups();

                        switch (which){
                            case 0: //Tekst
                                break;
                            case 1: //SMS
                                groupSMS.setVisibility(View.VISIBLE);

                                btnSMSWrite.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        phoneNumber = fieldNumberSMS.getText().toString();

                                        if(phoneNumber.isEmpty()){
                                            Toast.makeText(getActivity(), "Wpisz numer telefonu", Toast.LENGTH_SHORT).show();
                                        } else if (phoneNumber.length() == 9 ){

                                            textMessage = fieldSMSText.getText().toString();
                                            ((MainActivity)getActivity()).setPhoneNumber(phoneNumber);
                                            ((MainActivity)getActivity()).setTextMessage(textMessage);

                                            ((MainActivity)getActivity()).onDisplayDialog();
                                            ((MainActivity)getActivity()).setWriteMode(1);

                                            //PLAN NA ZAPISANIE - POBRAC W INTENT POTRZEBNE WARTOSCI ZE ZMIENNYCH PODCZAS INTERAKCJI Z TAG NFC, ON WIE, CO MA ROBIC, CZYLI NIE WYWALI SIE

                                            AlertDialog.Builder alertWriteSMSTag = new AlertDialog.Builder(getActivity())
                                                    .setTitle("Zapisz SMS TAG")
                                                    .setMessage("Przyłóż TAG, aby zapisać")
                                                    .setCancelable(false);

                                            alertWriteSMSTag.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ((MainActivity)getActivity()).onDialogDismissed();
                                                    dialog.cancel();
                                                }
                                            });

                                            alertWriteSMSTag.show();

                                        } else {
                                            Toast.makeText(getActivity(), "Popraw numer telefonu", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                break;
                            case 2: //Kontakty
                                groupContact.setVisibility(View.VISIBLE);

                                btnConctactWrite.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String name = fieldName.getText().toString();
                                        String surname = fieldSurname.getText().toString();
                                        String organisation = fieldOrganisation.getText().toString();
                                        String email = fieldEmail.getText().toString();
                                        String phoneNumber = fieldNumberContact.getText().toString();

                                        if (name.length() == 0 || phoneNumber.length() == 0){
                                            Toast.makeText(getActivity(), "Wpisz imię i numer telefonu", Toast.LENGTH_SHORT).show();
                                        } else {
                                            ((MainActivity)getActivity()).setVcard(name, surname, organisation, email, phoneNumber);
                                            ((MainActivity)getActivity()).onDisplayDialog();
                                            ((MainActivity)getActivity()).setWriteMode(2);

                                            AlertDialog.Builder alertContactTag = new AlertDialog.Builder(getActivity())
                                                    .setTitle("Zapisz Wizytówkę TAG")
                                                    .setMessage("Przyłóż TAG, aby zapisać")
                                                    .setCancelable(false);

                                            alertContactTag.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ((MainActivity)getActivity()).onDialogDismissed();
                                                    dialog.cancel();
                                                }
                                            });

                                            alertContactTag.show();
                                        }
                                    }
                                });
                                break;
                            case 3: //Adres sieciowy
                                groupWebLink.setVisibility(View.VISIBLE);
                                //niepodoba mi się robienie globalnej z tym http
                                typeProtocol = "http://";

                                btnChangeTypeProtocol.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (typeProtocol == "http://"){
                                            typeProtocol = "https://";
                                            textTypeProtocol.setText("HTTPS");
                                        } else {
                                            typeProtocol = "http://";
                                            textTypeProtocol.setText("HTTP");
                                        }
                                    }
                                });

                                btnWebLinkWrite.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String webLink = fieldWebAddress.getText().toString();

                                        if (webLink == null){
                                            Toast.makeText(getActivity(), "Wpisz adres", Toast.LENGTH_SHORT).show();
                                        } else {

                                            ((MainActivity)getActivity()).setWebLink(typeProtocol, webLink);
                                            ((MainActivity)getActivity()).onDisplayDialog();
                                            ((MainActivity)getActivity()).setWriteMode(3);

                                            //PLAN NA ZAPISANIE - POBRAC W INTENT POTRZEBNE WARTOSCI ZE ZMIENNYCH PODCZAS INTERAKCJI Z TAG NFC, ON WIE, CO MA ROBIC, CZYLI NIE WYWALI SIE

                                            AlertDialog.Builder alertWebLinkTag = new AlertDialog.Builder(getActivity())
                                                    .setTitle("Zapisz Link TAG")
                                                    .setMessage("Przyłóż TAG, aby zapisać")
                                                    .setCancelable(false);

                                            alertWebLinkTag.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ((MainActivity)getActivity()).onDialogDismissed();
                                                    dialog.cancel();
                                                }
                                            });

                                            alertWebLinkTag.show();
                                        }
                                    }
                                });
                                break;
                            case 4: //Latarka
                                break;
                        }
                    }
                });

                alertSelectTypeTag.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                //btnTypeAdress.setVisibility(View.INVISIBLE);
                alertSelectTypeTag.show();
            }
        });

        return  rootView;
    }

    private void clearAllGroups(){
        //text brakuje
        if (groupSMS.getVisibility() == View.VISIBLE) {
            groupSMS.setVisibility(View.GONE);
        }

        if (groupContact.getVisibility() == View.VISIBLE) {
            groupContact.setVisibility(View.GONE);
        }

        if (groupWebLink.getVisibility() == View.VISIBLE) {
            groupWebLink.setVisibility(View.GONE);
        }
        //latarka brakuje
    }
}
