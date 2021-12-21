package com.grabowski.czytniktag;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class DeleteTag extends Fragment {

    Button delBtn;

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            ((MainActivity)getActivity()).setActualView(2);
//            //Log.d("ACTUAL_FR", "DELETE");
//        }
//    }

//    @Override
//    public void onDestroyView(){
//        super.onDestroyView();
//        //getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
//        //Log.d("ACTUAL_FR", "DESTROY DELETE");
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.delete_tag, container, false);
        delBtn = rootView.findViewById(R.id.delBtn);

        delBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ((MainActivity)getActivity()).onDisplayDialog();

                AlertDialog.Builder alertScanTag = new AlertDialog.Builder(getActivity())
                        .setTitle("Kasowanie zawartości TAG")
                        .setMessage("Przyłóż TAG, aby skasować zawartość")
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

        return  rootView;
    }
}
