package com.grabowski.czytniktag;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class LoadTag extends Fragment {

    Button btn;
    TextView resultTag;

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isResumed() && isVisibleToUser) {
//            ((MainActivity)getActivity()).setActualView(1);
//            //Log.d("ACTUAL_FR", "SAVE");
//        }
//    }
//
//    @Override
//    public void onDestroyView(){
//        super.onDestroyView();
//        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
//        //Log.d("ACTUAL_FR", "DESTROY LOAD");
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.load_tag, container, false);
        btn = rootView.findViewById(R.id.button);
        resultTag = rootView.findViewById(R.id.resultTag);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Działa - pobiera jeżeli jest okienko, jezeli anuluj klikniete to false ustawia
                ((MainActivity)getActivity()).onDisplayDialog();

                AlertDialog.Builder alertScanTag = new AlertDialog.Builder(getActivity())
                        .setTitle("Zeskanuj TAG")
                        .setMessage("Przyłóż TAG, aby odczytać zawartość")
                        .setCancelable(false);
                alertScanTag.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btn.setBackgroundColor(Color.BLACK);
                        ((MainActivity)getActivity()).onDialogDismissed();
                        dialog.cancel();
                    }
                });
                alertScanTag.show();
            }
        });

        if(((MainActivity)getActivity()).tagResultScan != null){
            //Toast.makeText(getActivity(), "to jest alert", Toast.LENGTH_SHORT).show();
            //wypisuje zawartość w string do loadtag
            resultTag.setText(((MainActivity)getActivity()).tagResultScan);
        }

        return  rootView;
    }
}
