package de.hdmstuttgart.financemanager.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import de.hdmstuttgart.financemanager.R;

public class IntroActivity extends AppCompatActivity {

    final Handler handler = new Handler(); //Handler for delayed execute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ImageView logo = findViewById(R.id.logoView);

        logo.animate().alpha(1).setDuration(2000); //Fade in Logo

        handler.postDelayed(() -> {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);// New activity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish(); // Call once you redirect to another activity
        }, 4000);
    }
}