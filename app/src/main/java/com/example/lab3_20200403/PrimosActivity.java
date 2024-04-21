package com.example.lab3_20200403;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab3_20200403.PrimosActivity;

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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.lab3_20200403.databinding.ActivityPrimosBinding;

public class PrimosActivity extends AppCompatActivity {
    int contador = 1;
    private PrimeNumbersApiService primeNumbersApiService;
    private ActivityPrimosBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrimosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonAscender.setVisibility(View.INVISIBLE);
        binding.buttonReiniciar.setVisibility(View.INVISIBLE);
        binding.textoOpcional.setVisibility(View.INVISIBLE);
        binding.buttonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initializeNetworking();
        fetchPrimeNumbersFromWs();

        Button buttonAscender = findViewById(R.id.buttonAscender);
        Button buttonDescender = findViewById(R.id.buttonDescender);

        buttonAscender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ascendente = true; // Establecer el modo como ascendente
                buttonAscender.setVisibility(View.INVISIBLE); // Ocultar el botón de ascender
                buttonDescender.setVisibility(View.VISIBLE); // Mostrar el botón de descender
                // Obtener el número actual
                int numeroActual = Integer.parseInt(binding.idNumeroPrimo.getText().toString());
                // Encontrar el orden del número actual
                int ordenActual = 1;
                for (NumeroPrimo numeroPrimo : numerosPrimos) {
                    if (numeroPrimo.getNumber() == numeroActual) {
                        ordenActual = numeroPrimo.getOrder();
                        break;
                    }
                }
                // Establecer el nuevo índice basado en el orden del número actual
                currentIndex = ordenActual - 1;
                showNextPrime(); // Mostrar los números primos en orden ascendente a partir del número actual
            }
        });

        buttonDescender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ascendente = false; // Establecer el modo como descendente
                buttonAscender.setVisibility(View.VISIBLE); // Mostrar el botón de ascender
                buttonDescender.setVisibility(View.INVISIBLE); // Ocultar el botón de descender
                // Obtener el número actual
                int numeroActual = Integer.parseInt(binding.idNumeroPrimo.getText().toString());
                // Encontrar el orden del número actual
                int ordenActual = 1;
                for (NumeroPrimo numeroPrimo : numerosPrimos) {
                    if (numeroPrimo.getNumber() == numeroActual) {
                        ordenActual = numeroPrimo.getOrder();
                        break;
                    }
                }
                // Establecer el nuevo índice basado en el orden del número actual
                currentIndex = ordenActual - 1;
                showNextPrime(); // Mostrar los números primos en orden descendente a partir del número actual
            }
        });
        // Botón de pausar
        binding.buttonPausar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paused = true;
                binding.buttonPausar.setVisibility(View.INVISIBLE);
                binding.buttonReiniciar.setVisibility(View.VISIBLE);
                binding.textoOpcional.setVisibility(View.VISIBLE);
            }
        });

        // Botón de reiniciar
        binding.buttonReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paused = false;
                binding.buttonPausar.setVisibility(View.VISIBLE);
                binding.buttonReiniciar.setVisibility(View.INVISIBLE);
                binding.textoOpcional.setVisibility(View.INVISIBLE);
                showNextPrime();
            }
        });



//        buttonDescender.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ascendente) {
//                    ascendente = false;
//                    buttonAscender.setVisibility(View.VISIBLE);
//                    buttonDescender.setVisibility(View.INVISIBLE);
//                    currentIndex = numerosPrimos.size() - 1;
//                    showNextPrime(99);
//                }
//            }
//        });

    }


    private void initializeNetworking() {
        if (tengoInternet()) {
            Toast.makeText(this, "Tiene internet", Toast.LENGTH_SHORT).show();
            primeNumbersApiService = new Retrofit.Builder()
                    .baseUrl("https://prime-number-api.onrender.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(PrimeNumbersApiService.class);
        } else {
            Toast.makeText(this, "No hay conexión a Internet", Toast.LENGTH_LONG).show();
        }
    }

    private boolean tengoInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void fetchPrimeNumbersFromWs() {

        ApplicationThreads application = (ApplicationThreads) getApplication();
        ExecutorService executorService = application.executorService;

        ContadorViewModel contadorViewModel =
                new ViewModelProvider(PrimosActivity.this).get(ContadorViewModel.class);

        executorService.execute(() -> {
            fetchAndDisplayPrime(contadorViewModel);
        });
    }

    private List<NumeroPrimo> numerosPrimos;
    private int currentIndex;
    private boolean ascendente;

    private void fetchAndDisplayPrime(ContadorViewModel viewModel) {
        primeNumbersApiService.getPrimeNumbers(999, 1).enqueue(new Callback<List<NumeroPrimo>>() {
            @Override
            public void onResponse(Call<List<NumeroPrimo>> call, Response<List<NumeroPrimo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    numerosPrimos = response.body();
                    currentIndex = 0;
                    ascendente = true;
                    showNextPrime();
                } else {
                    Toast.makeText(PrimosActivity.this, "Error al recuperar datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NumeroPrimo>> call, Throwable t) {
                Toast.makeText(PrimosActivity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                Log.e("API Error", t.getMessage());
            }
        });
    }

    private boolean paused = false;

    private void showNextPrime() {
        if (!paused) {
            if (currentIndex >= 0 && currentIndex < numerosPrimos.size()) {
                NumeroPrimo numeroPrimo = numerosPrimos.get(currentIndex);
                binding.idNumeroPrimo.setText(String.valueOf(numeroPrimo.getNumber()));
                Log.d("msg-test-PRIMO", "order: " + numeroPrimo.getOrder() + "|" + numeroPrimo.getNumber());
            }
            currentIndex += ascendente ? 1 : -1;
            if ((ascendente && currentIndex >= numerosPrimos.size()) || (!ascendente && currentIndex < 0)) {
                if (!ascendente && currentIndex < 0) {
                    currentIndex = 0;
                    ascendente = true;
                    // Ocultar el botón de descender y mostrar el de ascender
                    binding.buttonDescender.setVisibility(View.VISIBLE);
                    binding.buttonAscender.setVisibility(View.INVISIBLE);
                } else {
                    ascendente = !ascendente; // Cambiar la dirección del conteo
                    currentIndex = ascendente ? 0 : numerosPrimos.size() - 1;
                    if (ascendente) {
                        new Handler().postDelayed(this::showNextPrime, 1000);
                    }
                }
            } else {
                new Handler().postDelayed(this::showNextPrime, 2000);
            }
        }
    }

}

