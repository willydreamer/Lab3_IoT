package com.example.lab3_20200403;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.lab3_20200403.Dto.NumeroPrimo;
import com.example.lab3_20200403.Services.PrimeNumbersApiService;
import com.example.lab3_20200403.ViewModel.ContadorViewModel;
import com.example.lab3_20200403.databinding.ActivityMainBinding;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.boton1derecho.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PrimosActivity.class);
            startActivity(intent);
        });
        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imdbId = binding.editTextInput.getText().toString();
                Intent intent = new Intent(MainActivity.this, MainPeliculas.class);
                intent.putExtra("imdbId", imdbId);
                startActivity(intent);
            }
        });

        // Conexion a internet
        Toast.makeText(this, "Tiene internet: " + tengoInternet(), Toast.LENGTH_LONG).show();

    }
    //Validación de la conectividad a internet
    public boolean tengoInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean tieneInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Log.d("msg-test-internet", "Internet: " + tieneInternet);
        return tieneInternet;
    }
}