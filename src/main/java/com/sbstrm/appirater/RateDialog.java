package com.sbstrm.appirater;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by rumburak on 28/08/2015.
 */
public class RateDialog extends DialogFragment {

    public interface RateDialogListener {
        void rateAction(Appirater.RateDialogAction rateDialogAction);
    }

    private RateDialogListener mListener;
    String mTitle;
    String mMessage;
    String mTextYes;
    String mTextLater;
    String mTextNo;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (RateDialogListener)activity;
    }

    @Override
    public void setArguments(Bundle args) {
        mTitle = args.getString("title");
        mMessage = args.getString("message");
        mTextYes = args.getString("yes");
        mTextLater = args.getString("later");
        mTextNo = args.getString("no");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.appirater, null);
        builder.setView(layout);
        TextView title = (TextView) layout.findViewById(R.id.title);
        TextView tv = (TextView) layout.findViewById(R.id.message);
        Button rateButton = (Button) layout.findViewById(R.id.rate);
        rateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.rateAction(Appirater.RateDialogAction.RATE_YES);
                dismiss();
            }
        });
        Button rateLaterButton = (Button) layout.findViewById(R.id.rateLater);
        rateLaterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.rateAction(Appirater.RateDialogAction.RATE_LATER);
                dismiss();
            }
        });
        Button cancelButton = (Button) layout.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.rateAction(Appirater.RateDialogAction.RATE_NO);
                dismiss();
            }
        });
        if (savedInstanceState == null) {
            rateButton.setText(mTextYes);
            tv.setText(mMessage);
            title.setText(mTitle);
            rateLaterButton.setText(mTextLater);
            cancelButton.setText(mTextNo);
        }
        return builder.create();
    }
}
