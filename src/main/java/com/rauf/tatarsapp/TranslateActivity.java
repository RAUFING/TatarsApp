package com.rauf.tatarsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class TranslateActivity extends AppCompatActivity {

    private EditText etSource, etResult;
    private MaterialButton btnTranslate;

    private static final String API_KEY = secret;
    private static final String FOLDER_ID = secret_dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        initViews();
        setupButtons();
    }

    private void initViews() {
        etSource = findViewById(R.id.etSource);
        etResult = findViewById(R.id.etResult);
        btnTranslate = findViewById(R.id.btnTranslate);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void setupButtons() {
        btnTranslate.setOnClickListener(v -> {
            String text = etSource.getText().toString().trim();
            if (text.isEmpty()) {
                etSource.setError("Введите текст");
                return;
            }

            boolean isRussian = text.matches(".*[а-яА-ЯёЁ].*");
            String sourceLang = isRussian ? "ru" : "tt";
            String targetLang = isRussian ? "tt" : "ru";

            translateText(text, sourceLang, targetLang);
        });
    }

    private void translateText(String text, String sourceLang, String targetLang) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.api.cloud.yandex.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YandexTranslateApi api = retrofit.create(YandexTranslateApi.class);
        TranslateRequest request = new TranslateRequest(
                sourceLang,
                targetLang,
                new String[]{text},
                FOLDER_ID
        );

        Call<TranslateResponse> call = api.translate("Api-Key " + API_KEY, request);
        call.enqueue(new Callback<TranslateResponse>() {
            @Override
            public void onResponse(Call<TranslateResponse> call, Response<TranslateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    etResult.setText(response.body().translations[0].text);
                } else {
                    showError("Ошибка перевода");
                }
            }

            @Override
            public void onFailure(Call<TranslateResponse> call, Throwable t) {
                showError("Нет подключения к интернету");
            }
        });
    }

    private void showError(String message) {
        Snackbar.make(btnTranslate, message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(this, R.color.error_color))
                .show();
    }

    // Модели данных
    static class TranslateRequest {
        String sourceLanguageCode;
        String targetLanguageCode;
        String[] texts;
        String folderId;

        TranslateRequest(String sourceLanguageCode, String targetLanguageCode, String[] texts, String folderId) {
            this.sourceLanguageCode = sourceLanguageCode;
            this.targetLanguageCode = targetLanguageCode;
            this.texts = texts;
            this.folderId = folderId;
        }
    }

    static class TranslateResponse {
        Translation[] translations;

        static class Translation {
            String text;
        }
    }

    interface YandexTranslateApi {
        @POST("translate/v2/translate")
        Call<TranslateResponse> translate(
                @Header("Authorization") String apiKey,
                @Body TranslateRequest request
        );
    }
}