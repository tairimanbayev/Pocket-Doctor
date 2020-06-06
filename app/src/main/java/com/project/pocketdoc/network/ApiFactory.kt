package com.project.pocketdoc.network

import com.project.pocketdoc.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiFactory {
    val apiService: ApiService by lazy {
        getRetrofit().create(ApiService::class.java)
    }

    private val CLIENT = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request =
                chain.request().newBuilder().addHeader("Accept", "application/json").build()
            chain.proceed(request)
        }
        .build()


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(CLIENT).build()
    }
}
