package com.hdmstuttgart.financemanager.presentation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.hdmstuttgart.financemanager.R;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ImageView logo = findViewById(R.id.logoView);
        logo.animate().alpha(1).setDuration(2000);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            finish();
        }, 4000);
    }
}
