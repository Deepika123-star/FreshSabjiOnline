package com.smartwebarts.callshowroom.pushnotification;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.smartwebarts.callshowroom.retrofit.UtilMethods;
import com.smartwebarts.callshowroom.retrofit.mCallBackResponse;

import java.util.Objects;

public class AccessToken {

    private String access_token;
    private static SharedPreferences sharedpreferences;

    public static SharedPreferences getPreferences(Context context) {
        if (sharedpreferences==null)
            sharedpreferences = context.getSharedPreferences("TOKEN_PREFERENCES", Context.MODE_PRIVATE);
        return sharedpreferences;
    }

    public String getAccess_token(Context context) {
        access_token = getPreferences(context).getString("access_token", "");
        return access_token;
    }

    public void setAccess_token(Context context, String access_token) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString("access_token", access_token);
        editor.apply();
        this.access_token = access_token;
    }


    public static void updateAccessToken(Activity activity) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = Objects.requireNonNull(task.getResult()).getToken();
                        new AccessToken().setAccess_token(activity.getApplicationContext(), token);

                        UtilMethods.INSTANCE.updateAccessToken(activity, token, new mCallBackResponse() {
                            @Override
                            public void success(String from, String message) {

                            }

                            @Override
                            public void fail(String from) {

                            }
                        });

                        Log.e("TAG", token);
                    }
                });
    }
}
