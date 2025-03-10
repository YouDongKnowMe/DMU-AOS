package com.dongyang.android.youdongknowme.standard.network

import com.dongyang.android.youdongknowme.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitObject {
    private const val TIME_OUT_COUNT: Long = 40

    fun getNetwork(): Retrofit {
        val baseInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
            val originalHttpUrl = chain.request().url

            val url = originalHttpUrl.newBuilder()
                .build()
            request.url(url)
            chain.proceed(request.build())
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(TIME_OUT_COUNT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT_COUNT, TimeUnit.SECONDS)
            .addInterceptor(baseInterceptor)
            .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }).build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}