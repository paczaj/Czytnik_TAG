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

    CountDownTimer timer;
    Button delBtnTag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.delete_tag, container, false);
        delBtnTag = rootView.findViewById(R.id.delBtn);

        delBtnTag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ((MainActivity)getActivity()).onDisplayDialog();

                AlertDialog alertDelete = new AlertDialog.Builder(getActivity())
                        .setTitle("Skasuj zawartość z TAG")
                        .setMessage("Przyłóż TAG, aby skasować zawartość")
                        .setCancelable(false)
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((MainActivity) getActivity()).onDialogDismissed();
                                dialogInterface.cancel();
                            }
                        })
                        .create();
                alertDelete.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(final DialogInterface dialog) {
                        final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                        defaultButton.setText("Anuluj");
                        timer = new CountDownTimer(60000, 500) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                                if (((MainActivity) getActivity()).isReadDone() == true) {
                                    ((MainActivity) getActivity()).offReadDone();
                                    ((MainActivity) getActivity()).onDialogDismissed();
                                    onFinish();
                                    timer.cancel();
                                }
                            }

                            @Override
                            public void onFinish() {
                                if (alertDelete.isShowing()) {
                                    alertDelete.dismiss();
                                }
                            }
                        };
                        timer.start();
                    }
                });
                alertDelete.show();
            }
        });
        return  rootView;
    }
}
