package com.example.tokoonline.di

import com.example.tokoonline.BuildConfig
import com.example.tokoonline.data.network.MidtransService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideAnalyticsService(
    ): MidtransService {
        val baseUrl = if (BuildConfig.DEBUG) {
            "https://api.sandbox.midtrans.com/v2/"
        } else "https://api.midtrans.com/v2/"

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .build()
            .create(MidtransService::class.java)
    }
}