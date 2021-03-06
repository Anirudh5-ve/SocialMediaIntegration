//----------- SPLASH SCREEN ------------

package com.example.socialmedia;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.squareup.picasso.Picasso;

public class UserProfile  extends AppCompatActivity {

    ImageView circleImageView;
    Button logout;
    TextView fb_name, fb_email;
    String Pfb_id, Pfb_name, Pfb_profileUrl, Pfb_email;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedprefs";
    public static final String FbprofileUrl = "PfbprofileUrl", Fbname = "Pfb_name", Fbemail = "Pfb_email", Fbid = "Pfb_id";

    public static final String FB_LOGIN = "fb_login";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        circleImageView = findViewById(R.id.fb_profile_pic);
        logout = findViewById(R.id.logout);
        fb_name = findViewById(R.id.name);
        fb_email = findViewById(R.id.email);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Pfb_name = sharedPreferences.getString(Fbname, "");
        Pfb_email = sharedPreferences.getString(Fbemail, "");
        Pfb_profileUrl = sharedPreferences.getString(FbprofileUrl, "");
        Pfb_id = sharedPreferences.getString(Fbid, "");

        Picasso.get().load(Pfb_profileUrl).into(circleImageView);
        fb_name.setText("User Name : " +Pfb_name);
        fb_email.setText("Email : "+Pfb_email);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder_exitbutton = new AlertDialog.Builder(UserProfile.this);
                builder_exitbutton.setTitle("Confirmation")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences settings = getSharedPreferences(FB_LOGIN, 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.remove("fb_logged");
                                editor.clear();
                                editor.apply();

                                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                                editor1.clear();
                                editor1.apply();

                                Toast.makeText(UserProfile.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UserProfile.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("Cancel", null);
                AlertDialog alertexit = builder_exitbutton.create();
                alertexit.show();
            }
        });
    }

    AccessTokenTracker accessTokenTracker =  new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                circleImageView.setImageResource(0);
                fb_name.setText(" ");
                fb_email.setText(" ");
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}
