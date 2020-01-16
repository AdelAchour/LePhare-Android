package com.production.achour_ar.phareenquete.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.production.achour_ar.phareenquete.R;
import com.production.achour_ar.phareenquete.managers.Constants;

public class AboutApp extends AppCompatActivity implements View.OnClickListener {

    private TextView versionTV;
    //private ImageView fbIV, instaIV, linkedinIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_app_act);

        initView();
        setListener();
        populateFields();

    }

    private void populateFields() {
        String txt = "Version "+Constants.VERSION_APP;
        versionTV.setText(txt);
    }

    private void setListener() {
        /*fbIV.setOnClickListener(this);
        instaIV.setOnClickListener(this);
        linkedinIV.setOnClickListener(this);*/
    }

    private void initView() {
        versionTV = findViewById(R.id.versionTV);
        /*fbIV = findViewById(R.id.fbIV);
        instaIV = findViewById(R.id.instaIV);
        linkedinIV = findViewById(R.id.linkedinIV);*/
    }

    @Override
    public void onClick(View view) {
        /*switch (view.getId()){
            case R.id.fbIV:
                Intent browserFb = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.FB_LINK));
                startActivity(browserFb);
                break;
            case R.id.instaIV:
                Intent browserInsta = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.INSTA_LINK));
                startActivity(browserInsta);
                break;

            case R.id.linkedinIV:
                Intent browserLinkedin = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.LINKEDIN_LINK));
                startActivity(browserLinkedin);
                break;

        }*/
    }
}
