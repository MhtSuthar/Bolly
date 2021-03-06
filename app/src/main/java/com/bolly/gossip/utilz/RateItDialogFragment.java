package com.bolly.gossip.utilz;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.bolly.gossip.R;

public class RateItDialogFragment extends DialogFragment {

    private static final int LAUNCHES_UNTIL_PROMPT = 3;
    private static final int DAYS_UNTIL_PROMPT = 3;
    private static final String LAST_PROMPT = "LAST_PROMPT";
    private static final String LAUNCHES = "LAUNCHES";
    private static final String DISABLED = "DISABLED";
    private static final String MY_PREF = "MY_PREF";
    private static final String TAG = "RateItDialogFragment";
    private static SharedPreferences mSharedPreferences;

    public static void show(Context context, FragmentManager fragmentManager) {
        boolean shouldShow = false;
        long currentTime = System.currentTimeMillis();
        mSharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        long lastPromptTime = mSharedPreferences.getLong(LAST_PROMPT, 0);
        if (lastPromptTime == 0) {
            lastPromptTime = currentTime;
        }

        if (!mSharedPreferences.getBoolean(DISABLED, false)) {
            int launches = mSharedPreferences.getInt(LAUNCHES, 0) + 1;
            if (launches > LAUNCHES_UNTIL_PROMPT) {
                shouldShow = true;
            }
            mSharedPreferences.edit().putInt(LAUNCHES, launches).commit();
        }

        if (shouldShow) {
            mSharedPreferences.edit().putInt(LAUNCHES, 0).commit();
            new RateItDialogFragment().show(fragmentManager, null);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.rate_title)
                .setMessage(R.string.rate_message)
                .setPositiveButton(R.string.rate_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
                        mSharedPreferences.edit().putBoolean(DISABLED, true).commit();
                        dismiss();
                    }
                })
                .setNeutralButton(R.string.rate_remind_later, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.rate_never, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSharedPreferences.edit().putBoolean(DISABLED, true).commit();
                        dismiss();
                    }
                }).create();
    }
}