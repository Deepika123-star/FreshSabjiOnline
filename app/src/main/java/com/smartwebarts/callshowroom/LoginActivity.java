package com.smartwebarts.callshowroom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.smartwebarts.callshowroom.dashboard.DashboardActivity;
import com.smartwebarts.callshowroom.retrofit.UtilMethods;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void signIn(View view) {
        Intent intent = new Intent(LoginActivity.this, SignInActivity.class);

        Pair[] pairs = new Pair[2];
        pairs[0] = new Pair<View, String> (findViewById(R.id.logo), "logo");
        pairs[1] = new Pair<View, String> (findViewById(R.id.appname), "appname");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
        startActivity(intent, options.toBundle());
    }

    public void signUp(View view) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public void skip(View view) {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UtilMethods.INSTANCE.version(this, null);

    }
}