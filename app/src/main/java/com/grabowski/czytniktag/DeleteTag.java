package com.grabowski.czytniktag;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class DeleteTag extends Fragment {

    Button delBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.delete_tag, container, false);
        delBtn = rootView.findViewById(R.id.delBtn);

        delBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ((MainActivity)getActivity()).onDisplayDialog();

                AlertDialog alertDelete = new AlertDialog.Builder(getActivity())
                        .setTitle("Skasuj zawartość z TAG")
                        .setMessage("Przyłóż TAG, aby skasować zawartość")
                        .setCancelable(false)
                        .setNegativeButton(android.R.string.no, null)
                        .create();
                alertDelete.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(final DialogInterface dialog) {
                        final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                        defaultButton.setText("Anuluj");
                        new CountDownTimer(60000, 500) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                                if (((MainActivity) getActivity()).isReadDone() == true) {
                                    onFinish();
                                    ((MainActivity) getActivity()).offReadDone();
                                    ((MainActivity) getActivity()).onDialogDismissed();
                                }
                            }

                            @Override
                            public void onFinish() {
                                if (alertDelete.isShowing()) {
                                    alertDelete.dismiss();
                                }
                            }
                        }.start();
                    }
                });
                alertDelete.show();
            }
        });
        return  rootView;
    }
}
