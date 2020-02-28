package com.example.ami300kl.instagramclone.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ami300kl.instagramclone.R;

import org.w3c.dom.Text;

/**
 * Created by Ami300kl on 29. 01. 2019.
 */
public class ConfirmPasswordDialog extends DialogFragment {
    private static final String TAG="ConfirmPasswordDialog";

    public interface OnConfirmPasswordListener {
        public void onConfirmPassword(String password);
    }
    OnConfirmPasswordListener mOnConfirmPasswordListenner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_confirm_password,container,false);
        Log.d(TAG, "onCreateView: ");

        TextView cancelDialog=(TextView)view.findViewById(R.id.dialogCancel);
        TextView acceptDialog=(TextView)view.findViewById(R.id.dialogConfirm);

        acceptDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: captured password and confirmming");

            }

        });
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: cancel clicked");
                getDialog().dismiss();
            }

        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try
        {
        mOnConfirmPasswordListenner=(OnConfirmPasswordListener)getTargetFragment();
        }catch(ClassCastException e)
        {
            Log.e(TAG, "onAttach::"+e.getMessage());
        }
    }
}
