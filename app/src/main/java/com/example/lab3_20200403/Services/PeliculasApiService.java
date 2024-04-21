package com.example.lab3_20200403.Services;

import com.example.lab3_20200403.Dto.Pelicula;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface PeliculasApiService {
    @GET("/")
    Call<Pelicula> getPelicula(@Query("apikey") String apiKey, @Query("i") String imdbId);
}