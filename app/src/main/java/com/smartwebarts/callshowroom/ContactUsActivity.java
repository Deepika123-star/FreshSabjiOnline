package com.smartwebarts.callshowroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.smartwebarts.callshowroom.utils.ApplicationConstants;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

//        MyGlide.withCircle(this, getResources().getDrawable(R.drawable.logo), (ImageView) findViewById(R.id.logo));
    }

    public void back(View view) {
        onBackPressed();
    }

    public void facebook(View view) {
//        Toast.makeText(this, "Facebook", Toast.LENGTH_SHORT).show();
        openUrl(ApplicationConstants.INSTANCE.FACEBOOK);
    }

    public void instagram(View view) {
//        Toast.makeText(this, "Instagram", Toast.LENGTH_SHORT).show();
        openUrl(ApplicationConstants.INSTANCE.INSTAGRAM);
    }

    public void twitter(View view) {
        Toast.makeText(this, "Twitter", Toast.LENGTH_SHORT).show();
        //openUrl(ApplicationConstants.INSTANCE.TWITTER);
    }

    public  void openUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void linkedin(View view) {
        Toast.makeText(this, "Linkedin", Toast.LENGTH_SHORT).show();
       // openUrl(ApplicationConstants.INSTANCE.LINKEDIN);
    }

    public void youtube(View view) {
        Toast.makeText(this, "Youtube", Toast.LENGTH_SHORT).show();
       // openUrl(ApplicationConstants.INSTANCE.YOUTUBE);
    }
}