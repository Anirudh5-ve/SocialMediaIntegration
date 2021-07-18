package com.example.socialmedia;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

public class gmail_userprofile extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    android.widget.ImageView ImageView;
    Button logout;
    TextView name, email;
    String Pemail_name, Pemail_profileUrl, Pemail_email;
    GoogleApiClient googleApiClient;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedprefs";
    public static final String EmailprofileUrl = "PemailprofileUrl", Emailname = "Pemail_name", Emailemail = "Pemail_email";

    public static final String GMAIL_LOGIN = "gmail_login";
    public static final String GMAIL_SAVE = "gmail_save";
    ProgressDialog progressDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmail_userprofile);

        ImageView = findViewById(R.id.gmail_profile_pic);
        logout = findViewById(R.id.gmail_logout);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);

        progressDialog = new ProgressDialog(gmail_userprofile.this);
        progressDialog.setTitle("Loading data...");
        progressDialog.show();

        SharedPreferences settings_save = getSharedPreferences(GMAIL_LOGIN, 0);
        if (settings_save.getString("gmail_saved", "").toString().equals("gmail_saved")) {

            sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            name.setText(sharedPreferences.getString(Emailname, ""));
            email.setText(sharedPreferences.getString(Emailemail, ""));
            Picasso.get().load(sharedPreferences.getString(EmailprofileUrl, "")).into(ImageView);
            progressDialog.dismiss();

        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            Pemail_name = account.getDisplayName();
            Pemail_email = account.getEmail();
            if(account.getPhotoUrl() != null) {
                Pemail_profileUrl = account.getPhotoUrl().toString();
            }else{
                Pemail_profileUrl = "null";
            }

            Picasso.get().load(Pemail_profileUrl).into(ImageView);
            name.setText("User Name : "+Pemail_name);
            email.setText("Email : "+Pemail_email);
            progressDialog.dismiss();

            sendemailData();
        }

        logout.setOnClickListener(view -> {
            AlertDialog.Builder builder_exitbutton = new AlertDialog.Builder(gmail_userprofile.this);
            builder_exitbutton.setTitle("Sign out ")
                    .setMessage("Are you sure you want to sign out?")
                    .setPositiveButton("Sign out", (dialogInterface, i) -> Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(status -> {
                        if(status.isSuccess()){
                            SharedPreferences settings_save1 = getSharedPreferences(GMAIL_LOGIN, 0);
                            SharedPreferences.Editor editor_save = settings_save1.edit();
                            editor_save.remove("gmail_saved");
                            editor_save.clear();
                            editor_save.apply();

                            SharedPreferences settings = getSharedPreferences(GMAIL_LOGIN, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.remove("gmail_logged");
                            editor.clear();
                            editor.apply();

                            SharedPreferences.Editor editor1 = sharedPreferences.edit();
                            editor1.clear();
                            editor1.apply();

                            Toast.makeText(gmail_userprofile.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(gmail_userprofile.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(gmail_userprofile.this, "Gmail Log out failed!", Toast.LENGTH_SHORT).show();
                        }
                    })).setNegativeButton("Cancel", null);
            AlertDialog alertexit = builder_exitbutton.create();
            alertexit.show();
        });
    }

    private void sendemailData() {
        SharedPreferences settings = getSharedPreferences(GMAIL_SAVE, 0);
        SharedPreferences.Editor editor_save = settings.edit();
        editor_save.putString("gmail_saved", "gmail_saved");
        editor_save.apply();

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Emailname,Pemail_name);
        editor.putString(Emailemail,Pemail_email);
        editor.putString(EmailprofileUrl,Pemail_profileUrl);
        editor.apply();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
