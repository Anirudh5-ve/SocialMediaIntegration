package com.example.socialmedia;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    private static final int SPLASH_SCREEN = 3000;
    //    Variables
    Animation topAnim, bottomAnim ;
    ImageView image;
    TextView text , tagLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

//        Hooks
        image = findViewById(R.id.imageView);
        text  = findViewById(R.id.textView2);
        tagLine  = findViewById(R.id.textView3);

        image.setAnimation(topAnim);
        text.setAnimation(bottomAnim);
        tagLine.setAnimation(bottomAnim);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity2.this,MainActivity.class);
            startActivity(intent);
            finish();

        },SPLASH_SCREEN);
    }
}