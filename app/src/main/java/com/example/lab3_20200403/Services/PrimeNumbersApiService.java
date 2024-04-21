package com.example.lab3_20200403.Services;

import com.example.lab3_20200403.Dto.NumeroPrimo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PrimeNumbersApiService {
    @GET("primeNumbers")
    Call<List<NumeroPrimo>> getPrimeNumbers(@Query("len") int len, @Query("order") int order);
}

