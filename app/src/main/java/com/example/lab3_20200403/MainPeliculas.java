package com.example.lab3_20200403;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab3_20200403.Dto.Pelicula;
import com.example.lab3_20200403.Dto.Rating;
import com.example.lab3_20200403.Services.PeliculasApiService;
import com.example.lab3_20200403.Services.PrimeNumbersApiService;
import com.example.lab3_20200403.databinding.ActivityPeliculasBinding;
import com.example.lab3_20200403.databinding.ActivityPrimosBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainPeliculas extends AppCompatActivity {
    private PeliculasApiService peliculasApiService;
    private ActivityPeliculasBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPeliculasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String imdbId = getIntent().getStringExtra("imdbId");
        String apiKey = "bf81d461";
        Log.d("IMDb ID", imdbId); // Mostrar el IMDb ID en el log
        String apiUrl = "https://www.omdbapi.com/?apikey=" + apiKey + "&i=" + imdbId;

        fetchAndDisplayPelicula(imdbId);

        CheckBox checkBoxConfirm = findViewById(R.id.checkBoxConfirm);
        Button buttonRegresar = findViewById(R.id.buttonRegresar);
        buttonRegresar.setVisibility(View.INVISIBLE);

        checkBoxConfirm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonRegresar.setEnabled(isChecked);
                buttonRegresar.setVisibility(View.VISIBLE);
            }
        });

        buttonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    private void fetchAndDisplayPelicula(String imdbId) {
        PeliculasApiService peliculasApiService = new Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PeliculasApiService.class);

        peliculasApiService.getPelicula("bf81d461", imdbId).enqueue(new Callback<Pelicula>() {
            @Override
            public void onResponse(Call<Pelicula> call, Response<Pelicula> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Pelicula pelicula = response.body();

                    // Mostrar el resultado en la consola
                    Log.d("Pelicula", "Title: " + pelicula.getTitle());
                    Log.d("Pelicula", "Director: " + pelicula.getDirector());
                    Log.d("Pelicula", "Actores: " + pelicula.getActors());
                    // En la vista

                    // Mostrar el resultado en la vista activity_peliculas
                    binding.tituloPelicula.setText(pelicula.getTitle());
                    binding.textDirectorContent.setText(pelicula.getDirector());
                    binding.textActoresContent.setText(pelicula.getActors());
                    binding.textFechaContent.setText(pelicula.getReleased());
                    binding.textGeneroContent.setText(pelicula.getGenre());
                    binding.textEscritoresContent.setText(pelicula.getWriter());
                    binding.textTramaContent.setText(pelicula.getPlot());
//                    if (pelicula.getRatings() != null && !pelicula.getRatings().isEmpty()) {
//                        for (Rating rating : pelicula.getRatings()) {
//                            switch (rating.getSource()) {
//                                case "Internet Movie Database":
//                                    binding.textInternetContent.setText(rating.getValue());
//                                    break;
//                                case "Rotten Tomatoes":
//                                    binding.textRottenTomatoesContent.setText(rating.getValue());
//                                    break;
//                                case "Metacritic":
//                                    binding.textMetacriticContent.setText(rating.getValue());
//                                    break;
//                                default:
//                                    // Otra fuente de rating desconocida
//                                    break;
//                            }
//                        }
//                    }

                } else {
                    Log.e("Pelicula", "Error al recuperar datos");
                }
            }
            @Override
            public void onFailure(Call<Pelicula> call, Throwable t) {
                Log.e("Pelicula", "Error al conectar con el servidor: " + t.getMessage());
            }
        });
    }

}