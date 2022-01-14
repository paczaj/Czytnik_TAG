package com.grabowski.czytniktag;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class LoadTag extends Fragment {

    Button btnReadTag;

    TextView textIdTag;
    TextView textTypeTag;
    TextView textSizeTag;
    TextView textWritableTag;
    TextView textMessageTag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.load_tag, container, false);

        textIdTag = rootView.findViewById(R.id.textIdTag);
        textTypeTag = rootView.findViewById(R.id.textTypeTag);
        textSizeTag = rootView.findViewById(R.id.textSizeTag);
        textWritableTag = rootView.findViewById(R.id.textWritableTag);
        textMessageTag = rootView.findViewById(R.id.textMessageTag);

        btnReadTag = rootView.findViewById(R.id.btnReadTag);
        btnReadTag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Działa - pobiera jeżeli jest okienko, jezeli anuluj klikniete to false ustawia
                ((MainActivity) getActivity()).onDisplayDialog();

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Zeskanuj TAG");
                builder.setMessage("Przyłóż TAG, aby odczytać zawartość");
                builder.setCancelable(false);
                builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((MainActivity) getActivity()).onDialogDismissed();
                        dialogInterface.cancel();
                    }
                });

                final AlertDialog alertScan = builder.create();
                alertScan.show();
                new CountDownTimer(60000, 500) {
                    @Override
                    public void onTick(long l) {
                        if (((MainActivity) getActivity()).isReadDone() == true) {
                            textIdTag.setText(((MainActivity) getActivity()).getIdTag());
                            textTypeTag.setText(((MainActivity) getActivity()).getTypeTag());
                            textSizeTag.setText(((MainActivity) getActivity()).getSizeTag());
                            textMessageTag.setText(((MainActivity) getActivity()).getTextOnTag());
                            if (((MainActivity) getActivity()).isWritable() == true) {
                                textWritableTag.setText("Możliwy ponowny zapis");
                            } else {
                                textWritableTag.setText("Zapis zablokowany");
                            }
                            ((MainActivity) getActivity()).offReadDone();
                            ((MainActivity) getActivity()).onDialogDismissed();
                            cancel();
                            alertScan.cancel();
                        }
                    }
                    @Override
                    public void onFinish() {
                        alertScan.cancel();
                    }
                }.start();
            }
        });

        return rootView;
    }
}


