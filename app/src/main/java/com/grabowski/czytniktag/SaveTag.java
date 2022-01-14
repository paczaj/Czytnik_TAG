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

public class SaveTag extends Fragment {
    //pierwsze testowe
    Button btn;

    Button btnSelectTypeWrite;
    Button btnSMSWrite;
    Button btnContactWrite;
    Button btnWebLinkWrite;
    Button btnChangeTypeProtocol;
    Button btnTextWrite;
    Button btnTelephoneWrite;
    Button btnMapsWrite;

    EditText fieldSMSText;
    EditText fieldNumberSMS;
    EditText fieldWebAddress;
    EditText fieldName;
    EditText fieldSurname;
    EditText fieldOrganisation;
    EditText fieldEmail;
    EditText fieldNumberContact;
    EditText fieldText;
    EditText fieldTelephone;
    EditText fieldStreet;
    EditText fieldStreetNumber;
    EditText fieldCity;
    EditText fieldZipCode;

    TextView textTitle;
    TextView textTypeProtocol;

    Group groupSMS;
    Group groupWebLink;
    Group groupContact;
    Group groupText;
    Group groupTelephone;
    Group groupMaps;

    String typeProtocol;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.save_tag, container, false);

        groupSMS = rootView.findViewById(R.id.groupSMS);
        groupWebLink = rootView.findViewById(R.id.groupWebLink);
        groupContact = rootView.findViewById(R.id.groupContact);
        groupText = rootView.findViewById(R.id.groupText);
        groupTelephone = rootView.findViewById(R.id.groupTelephone);
        groupMaps = rootView.findViewById(R.id.groupMaps);

        btnSelectTypeWrite = rootView.findViewById(R.id.selectTypeWrite);
        btnSMSWrite = rootView.findViewById(R.id.btnSMSWrite);
        btnContactWrite = rootView.findViewById(R.id.btnContactWrite);
        btnWebLinkWrite = rootView.findViewById(R.id.btnWebLinkWrite);
        btnChangeTypeProtocol = rootView.findViewById(R.id.btnChangeTypeProtocol);
        btnTextWrite = rootView.findViewById(R.id.btnTextWrite);
        btnTelephoneWrite = rootView.findViewById(R.id.btnTelephoneWrite);
        btnMapsWrite = rootView.findViewById(R.id.btnAdressWrite);

        fieldSMSText = rootView.findViewById(R.id.editTextSMS);
        fieldNumberSMS = rootView.findViewById(R.id.editTextPhone);
        fieldWebAddress = rootView.findViewById(R.id.editTextWebAddress);
        fieldName = rootView.findViewById(R.id.editTextName);
        fieldSurname = rootView.findViewById(R.id.editTextSurname);
        fieldOrganisation = rootView.findViewById(R.id.editTextOrganization);
        fieldEmail = rootView.findViewById(R.id.editTextEmail);
        fieldNumberContact = rootView.findViewById(R.id.editTextPhoneContact);
        fieldText = rootView.findViewById(R.id.editTextMultiLine);
        fieldTelephone = rootView.findViewById(R.id.editTextTelephone);
        fieldStreet = rootView.findViewById(R.id.editTextStreet);
        fieldStreetNumber = rootView.findViewById(R.id.editTextNumberStreet);
        fieldCity = rootView.findViewById(R.id.editTextCity);
        fieldZipCode = rootView.findViewById(R.id.editTextZipCode);

        textTitle = rootView.findViewById(R.id.textTitleWrite);
        textTypeProtocol = rootView.findViewById(R.id.textProtocolType);
////testowe
//        btn = rootView.findViewById(R.id.btnReadTag);
//        btn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
////                Działa - pobiera jeżeli jest okienko, jezeli anuluj klikniete to false ustawia
//                ((MainActivity) getActivity()).onDisplayDialog();
//                ((MainActivity) getActivity()).setWriteMode(77);
//
//                AlertDialog.Builder alertScanTag = new AlertDialog.Builder(getActivity())
//                        .setTitle("Zapisz TAG")
//                        .setMessage("Przyłóż TAG, aby zapisać zawartość")
//                        .setCancelable(false);
//                alertScanTag.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ((MainActivity) getActivity()).onDialogDismissed();
//                        dialog.cancel();
//                    }
//                });
//                alertScanTag.show();
//            }
//        });

        btnSelectTypeWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertSelectTypeTag = new AlertDialog.Builder(getActivity())
                        .setTitle("Wybierz typ zapisu TAG")
                        .setCancelable(false);
                String[] options = {"Tekst", "SMS", "Telefon", "Kontakt", "Adres sieciowy", "Adres Aplikacja Google Maps", "Adres Aplikacja z Mapami"};

                alertSelectTypeTag.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Aby działało chowanie i pokazywanie, trzeba przypisać do zmiennych elementy wyżej w kodzie
                        clearAllGroups();

                        switch (which) {
                            case 0: //Tekst
                                textTitle.setText("Tekst");
                                groupText.setVisibility(View.VISIBLE);

                                btnTextWrite.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String text = fieldText.getText().toString();

                                        if (text.isEmpty()) {
                                            Toast.makeText(getActivity(), "Wpisz tekst", Toast.LENGTH_SHORT).show();
                                        } else {
                                            ((MainActivity) getActivity()).setText(text);
                                            ((MainActivity) getActivity()).onDisplayDialog();
                                            ((MainActivity) getActivity()).setWriteMode(0);

                                            AlertDialog.Builder alertWriteTextTag = new AlertDialog.Builder(getActivity())
                                                    .setTitle("Zapisz Tekst do TAG/TAG'ów")
                                                    .setMessage("Przyłóż TAG, aby zapisać")
                                                    .setCancelable(false);

                                            alertWriteTextTag.setNeutralButton("Zamknij", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ((MainActivity) getActivity()).onDialogDismissed();
                                                    dialog.dismiss();
                                                }
                                            });

                                            alertWriteTextTag.show();
                                        }
                                    }
                                });
                                break;
                            case 1: //SMS
                                textTitle.setText("SMS");
                                groupSMS.setVisibility(View.VISIBLE);

                                btnSMSWrite.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String phoneNumber = fieldNumberSMS.getText().toString();

                                        if (phoneNumber.isEmpty()) {
                                            Toast.makeText(getActivity(), "Wpisz numer telefonu", Toast.LENGTH_SHORT).show();
                                        } else if (phoneNumber.length() == 9) {

                                            String textMessage = fieldSMSText.getText().toString();
                                            ((MainActivity) getActivity()).setPhoneNumber(phoneNumber);
                                            ((MainActivity) getActivity()).setTextMessage(textMessage);

                                            ((MainActivity) getActivity()).onDisplayDialog();
                                            ((MainActivity) getActivity()).setWriteMode(1);

                                            //PLAN NA ZAPISANIE - POBRAC W INTENT POTRZEBNE WARTOSCI ZE ZMIENNYCH PODCZAS INTERAKCJI Z TAG NFC, ON WIE, CO MA ROBIC, CZYLI NIE WYWALI SIE

                                            AlertDialog.Builder alertWriteSMSTag = new AlertDialog.Builder(getActivity())
                                                    .setTitle("Zapisz SMS do TAG/TAGów")
                                                    .setMessage("Przyłóż TAG, aby zapisać")
                                                    .setCancelable(false);

                                            alertWriteSMSTag.setNeutralButton("Zamknij", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ((MainActivity) getActivity()).onDialogDismissed();
                                                    dialog.dismiss();
                                                }
                                            });

                                            alertWriteSMSTag.show();

                                        } else {
                                            Toast.makeText(getActivity(), "Popraw numer telefonu", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                break;
                            case 2: //Telefon
                                textTitle.setText("Numer telefonu");
                                groupTelephone.setVisibility(View.VISIBLE);

                                btnTelephoneWrite.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String phoneNumber = fieldTelephone.getText().toString();

                                        if (phoneNumber.isEmpty()) {
                                            Toast.makeText(getActivity(), "Wpisz numer telefonu", Toast.LENGTH_SHORT).show();
                                        } else if (phoneNumber.length() == 9) {

                                            ((MainActivity) getActivity()).setPhoneNumber(phoneNumber);

                                            ((MainActivity) getActivity()).onDisplayDialog();
                                            ((MainActivity) getActivity()).setWriteMode(2);

                                            AlertDialog.Builder alertWriteTelephoneTag = new AlertDialog.Builder(getActivity())
                                                    .setTitle("Zapisz Numer do TAG/TAGów")
                                                    .setMessage("Przyłóż TAG, aby zapisać")
                                                    .setCancelable(false);

                                            alertWriteTelephoneTag.setNeutralButton("Zamknij", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ((MainActivity) getActivity()).onDialogDismissed();
                                                    dialog.dismiss();
                                                }
                                            });

                                            alertWriteTelephoneTag.show();

                                        } else {
                                            Toast.makeText(getActivity(), "Popraw numer telefonu", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                break;
                            case 3: //Kontakty
                                textTitle.setText("Wizytówka kontakt");
                                groupContact.setVisibility(View.VISIBLE);

                                btnContactWrite.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String name = fieldName.getText().toString();
                                        String surname = fieldSurname.getText().toString();
                                        String organisation = fieldOrganisation.getText().toString();
                                        String email = fieldEmail.getText().toString();
                                        String phoneNumber = fieldNumberContact.getText().toString();

                                        if (name.length() == 0 || phoneNumber.length() == 0) {
                                            Toast.makeText(getActivity(), "Wpisz imię i numer telefonu", Toast.LENGTH_SHORT).show();
                                        } else {
                                            ((MainActivity) getActivity()).setVcard(name, surname, organisation, email, phoneNumber);
                                            ((MainActivity) getActivity()).onDisplayDialog();
                                            ((MainActivity) getActivity()).setWriteMode(3);

                                            AlertDialog.Builder alertWriteContactTag = new AlertDialog.Builder(getActivity())
                                                    .setTitle("Zapisz Wizytówkę do TAG/TAGów")
                                                    .setMessage("Przyłóż TAG, aby zapisać")
                                                    .setCancelable(false);

                                            alertWriteContactTag.setNeutralButton("Zamknij", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ((MainActivity) getActivity()).onDialogDismissed();
                                                    dialog.dismiss();
                                                }
                                            });

                                            alertWriteContactTag.show();
                                        }
                                    }
                                });
                                break;
                            case 4: //Adres sieciowy
                                textTitle.setText("Link do strony internetowej");
                                groupWebLink.setVisibility(View.VISIBLE);
                                //niepodoba mi się robienie globalnej z tym http
                                typeProtocol = "http://";

                                btnChangeTypeProtocol.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (typeProtocol == "http://") {
                                            typeProtocol = "https://";
                                            textTypeProtocol.setText("Typ: HTTPS");
                                        } else {
                                            typeProtocol = "http://";
                                            textTypeProtocol.setText("Typ: HTTP");
                                        }
                                    }
                                });

                                btnWebLinkWrite.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String webLink = fieldWebAddress.getText().toString();

                                        if (webLink == null) {
                                            Toast.makeText(getActivity(), "Wpisz adres", Toast.LENGTH_SHORT).show();
                                        } else {

                                            ((MainActivity) getActivity()).setWebLink(typeProtocol, webLink);
                                            ((MainActivity) getActivity()).onDisplayDialog();
                                            ((MainActivity) getActivity()).setWriteMode(4);

                                            //PLAN NA ZAPISANIE - POBRAC W INTENT POTRZEBNE WARTOSCI ZE ZMIENNYCH PODCZAS INTERAKCJI Z TAG NFC, ON WIE, CO MA ROBIC, CZYLI NIE WYWALI SIE

                                            AlertDialog.Builder alertWriteWebLinkTag = new AlertDialog.Builder(getActivity())
                                                    .setTitle("Zapisz Link TAG")
                                                    .setMessage("Przyłóż TAG, aby zapisać")
                                                    .setCancelable(false);

                                            alertWriteWebLinkTag.setNeutralButton("Zamknij", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ((MainActivity) getActivity()).onDialogDismissed();
                                                    dialog.dismiss();
                                                }
                                            });

                                            alertWriteWebLinkTag.show();
                                        }
                                    }
                                });
                                break;
                            case 5: //Adres Google Maps
                                textTitle.setText("Adres dla Aplikacji Google Maps");
                                groupMaps.setVisibility(View.VISIBLE);

                                btnMapsWrite.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String street = fieldStreet.getText().toString();
                                        String city = fieldCity.getText().toString();
                                        String streetNumber = fieldStreetNumber.getText().toString();
                                        String zipCode = fieldZipCode.getText().toString();

                                        if (street.isEmpty() || city.isEmpty()) {
                                            Toast.makeText(getActivity(), "Musisz wpisać miasto i adres", Toast.LENGTH_SHORT).show();
                                        } else {
                                            String address = street + "+" + streetNumber + "+" + zipCode + "+" + city;
                                            address = address.replaceAll(" ", "+");
                                            address = address.replaceAll("\\++", "+");

                                            ((MainActivity) getActivity()).setAddressMaps(address);

                                            ((MainActivity) getActivity()).onDisplayDialog();
                                            ((MainActivity) getActivity()).setWriteMode(5);

                                            AlertDialog.Builder alertWriteGMapsTag = new AlertDialog.Builder(getActivity())
                                                    .setTitle("Zapisz Adres dla Google Maps do TAG/TAGów")
                                                    .setMessage("Przyłóż TAG, aby zapisać")
                                                    .setCancelable(false);

                                            alertWriteGMapsTag.setNeutralButton("Zamknij", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ((MainActivity) getActivity()).onDialogDismissed();
                                                    dialog.dismiss();
                                                }
                                            });
                                            alertWriteGMapsTag.show();
                                        }
                                    }
                                });
                                break;
                            case 6: //Adres dla app z mapami
                                textTitle.setText("Adres dla dowolnej aplikacji z mapami");
                                groupMaps.setVisibility(View.VISIBLE);

                                btnMapsWrite.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String street = fieldStreet.getText().toString();
                                        String city = fieldCity.getText().toString();
                                        String streetNumber = fieldStreetNumber.getText().toString();
                                        String zipCode = fieldZipCode.getText().toString();

                                        if (street.isEmpty() || city.isEmpty()) {
                                            Toast.makeText(getActivity(), "Musisz wpisać miasto i adres", Toast.LENGTH_SHORT).show();
                                        } else {
                                            String address = street + "+" + streetNumber + "+" + zipCode + "+" + city;
                                            address = address.replaceAll(" ", "+");
                                            address = address.replaceAll("\\++", "+");

                                            ((MainActivity) getActivity()).setAddressMaps(address);

                                            ((MainActivity) getActivity()).onDisplayDialog();
                                            ((MainActivity) getActivity()).setWriteMode(6);

                                            AlertDialog.Builder alertWriteMapsTag = new AlertDialog.Builder(getActivity())
                                                    .setTitle("Zapisz Adres dla Aplikacji z Mapami do TAG/TAGów")
                                                    .setMessage("Przyłóż TAG, aby zapisać")
                                                    .setCancelable(false);

                                            alertWriteMapsTag.setNeutralButton("Zamknij", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ((MainActivity) getActivity()).onDialogDismissed();
                                                    dialog.dismiss();
                                                }
                                            });
                                            alertWriteMapsTag.show();
                                        }
                                    }
                                });
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
                alertSelectTypeTag.show();
            }
        });
        return rootView;
    }

    private void clearAllGroups() {
        if (groupText.getVisibility() == View.VISIBLE) {
            groupText.setVisibility(View.GONE);
        }

        if (groupSMS.getVisibility() == View.VISIBLE) {
            groupSMS.setVisibility(View.GONE);
        }

        if (groupTelephone.getVisibility() == View.VISIBLE) {
            groupTelephone.setVisibility(View.GONE);
        }

        if (groupContact.getVisibility() == View.VISIBLE) {
            groupContact.setVisibility(View.GONE);
        }

        if (groupWebLink.getVisibility() == View.VISIBLE) {
            groupWebLink.setVisibility(View.GONE);
        }

        if (groupMaps.getVisibility() == View.VISIBLE) {
            groupMaps.setVisibility(View.GONE);
        }
    }
}
