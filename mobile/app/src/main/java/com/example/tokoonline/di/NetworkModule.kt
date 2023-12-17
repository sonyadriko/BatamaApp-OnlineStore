package com.example.tokoonline.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.example.tokoonline.BuildConfig
import com.example.tokoonline.core.base.MidtransApiService
import com.example.tokoonline.core.base.MidtransProcessorService
import com.example.tokoonline.data.network.MidtransService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @MidtransApiService
    @Provides
    fun provideMidtransService(
        okHttpClient: OkHttpClient
    ): MidtransService {
        val baseUrl = if (BuildConfig.DEBUG) {
            "https://api.sandbox.midtrans.com/v2/"
        } else "https://api.midtrans.com/v2/"

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(MidtransService::class.java)
    }

    @MidtransProcessorService
    @Provides
    fun provideMidtransProcessorService(
        okHttpClient: OkHttpClient
    ): MidtransService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(MidtransService::class.java)
    }

    @Provides
    fun providesOkHttp(
        @ApplicationContext context: Context
    ): OkHttpClient {
//        val chuckerCollector = ChuckerCollector(
//            context = context,
//            showNotification = true,
//            retentionPeriod = RetentionManager.Period.FOREVER
//        )

        return OkHttpClient.Builder()
            .addInterceptor(
                ChuckerInterceptor.Builder(context)
//                    .collector(chuckerCollector)
                    .maxContentLength(250_000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(true)
                    .build()
            )
            .build()
    }
}