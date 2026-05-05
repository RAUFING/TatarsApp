package com.rauf.tatarsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Анимация логотипа
        ImageView logo = findViewById(R.id.logo);
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(1000);
        logo.startAnimation(fadeIn);

        // Обработчики кнопок
        setupButtonAnimations();
        setupButtonClickListeners();
    }

    private void setupButtonAnimations() {
        int[] buttonIds = {
                R.id.btn_learn, R.id.btn_translate,
                R.id.btn_diaspora, R.id.btn_events,
                R.id.btn_chat, R.id.btn_contests
        };

        for (int id : buttonIds) {
            MaterialButton button = findViewById(id);
            button.setOnClickListener(this::animateButtonClick);
        }
    }

    private void animateButtonClick(View view) {
        Animation scale = AnimationUtils.loadAnimation(this, R.anim.button_scale);
        view.startAnimation(scale);

        view.postDelayed(() -> {
            int clickedId = view.getId();

            if (clickedId == R.id.btn_learn) {
                startActivity(new Intent(this, LearnActivity.class));
            } else if (clickedId == R.id.btn_translate) {
                startActivity(new Intent(this, TranslateActivity.class));
            } else if (clickedId == R.id.btn_diaspora) {
                startActivity(new Intent(this, DiasporaActivity.class));
            } else if (clickedId == R.id.btn_events) {
                startActivity(new Intent(this, EventsActivity.class));
            } else if (clickedId == R.id.btn_chat) {
                startActivity(new Intent(this, ChatActivity.class));
            } else if (clickedId == R.id.btn_contests) {
                startActivity(new Intent(this, ContestActivity.class));
            }
        }, 150);
    }

    private void setupButtonClickListeners() {
        // Можно добавить дополнительную логику здесь
    }
}