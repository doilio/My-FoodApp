package com.example.dowy.foodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element adsElement = new Element();
        adsElement.setTitle("Advertise here");

        Element versionElement = new Element();
        versionElement.setTitle("Version 1.0.0");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.food_delivery)
                .setDescription("FoodApp is your handy grocery delivery app, here to help you get the things you need without having to leave the comfort of your house. You can pay using M-Pesa or Live at your doorstep.")
                .addItem(versionElement)
                .addItem(adsElement)
                .addGroup("Connect with us")
                .addEmail("doowimoz@gmail.com")
                .addWebsite("https://medium.com/@doiliomatsinhe/")
                .addFacebook("doilio.matsinhe")
                .addTwitter("DoilioMatsinhe")
                .addYoutube("UCR7kEl7kjk_pwcvYQALvlKw")
                //.addPlayStore("com.ideashower.readitlater.pro")
                .addGitHub("doilio")
                .addInstagram("doiliomatsinhe")
                .addItem(createCopyright())
                .create();

        setContentView(aboutPage);
    }

    private Element createCopyright() {
        Element copyright = new Element();
        final String copyrightString = String.format("Copyright %d by Doowimoz llc", Calendar.getInstance()
                .get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        copyright.setIconDrawable(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this, copyrightString, Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
    }
}
