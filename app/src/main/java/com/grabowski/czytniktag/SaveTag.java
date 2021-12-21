package com.grabowski.czytniktag;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class SaveTag extends Fragment {

    Button btn;
    Button btnSelectTypeWrite;

    Button btnTypeAdress;
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            ((MainActivity)getActivity()).setActualView(1);
//            //Log.d("ACTUAL_FR", "SAVE");
//        }
//    }

//    @Override
//    public void onDestroyView(){
//        super.onDestroyView();
//        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
//        //Log.d("ACTUAL_FR", "DESTROY SAVE");
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.save_tag, container, false);

        btn = rootView.findViewById(R.id.button);
        btnSelectTypeWrite = rootView.findViewById(R.id.selectTypeWrite);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Działa - pobiera jeżeli jest okienko, jezeli anuluj klikniete to false ustawia
                ((MainActivity)getActivity()).onDisplayDialog();

                AlertDialog.Builder alertScanTag = new AlertDialog.Builder(getActivity())
                        .setTitle("Zapisz TAG")
                        .setMessage("Przyłóż TAG, aby zapisać zawartość")
                        .setCancelable(false);
                alertScanTag.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btn.setBackgroundColor(Color.RED);
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
                        switch (which){
                            case 0: //Tekst
                                btnTypeAdress = rootView.findViewById(R.id.btnChangeAdress);
                                btnTypeAdress.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "Wybrano opcję 1", Toast.LENGTH_SHORT).show();
                                break;
                            case 1: //SMS
                                break;
                            case 2: //Kontakty
                                break;
                            case 3: //Adres sieciowy
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
}
