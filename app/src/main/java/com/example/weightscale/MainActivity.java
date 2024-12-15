package com.example.weightscale;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://192.168.8.104/";
    private EditText weight, barcode;
    private Button scanBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        barcode = findViewById(R.id.barcode);
        weight = findViewById(R.id.weight);
        scanBtn = findViewById(R.id.scanButton);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValues();
            }
        });
    }

    private void getValues(){

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // Set the base URL for the API
                .addConverterFactory(GsonConverterFactory.create()) // Use Gson for JSON parsing
                .build();
        // Create an instance of ApiService
        ApiService apiService = retrofit.create(ApiService.class);
        // Make the GET request
        Call<MyResponseModel> call = apiService.getValue();
        call.enqueue(new Callback<MyResponseModel>() {
            @Override
            public void onResponse(Call<MyResponseModel> call, Response<MyResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Handle the response
                    MyResponseModel result = response.body();
                    Log.d("API_RESPONSE", "Barcode: " + result.getBarcode() + ", Weight: " + result.getWeight());
                    barcode.setText(result.getBarcode());
                    weight.setText(result.getWeight());

                    // You can update your UI here based on the result
                } else {
                    Log.e("API_ERROR", "Response code: " + response.code());
                    Toast.makeText(MainActivity.this,"Device is Offline",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MyResponseModel> call, Throwable t) {
                // Handle the failure
                Log.e("API_ERROR", "Error: " + t.getMessage());
                Toast.makeText(MainActivity.this,"Device is Offline",Toast.LENGTH_LONG).show();
            }
        });
    }
}