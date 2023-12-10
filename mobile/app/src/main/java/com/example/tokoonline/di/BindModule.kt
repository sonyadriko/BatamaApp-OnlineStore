package com.example.tokoonline.di

import com.example.tokoonline.data.repository.midtrans.MidtransRepository
import com.example.tokoonline.data.repository.midtrans.MidtransRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(ActivityComponent::class, FragmentComponent::class)
abstract class BindModule {

    @Binds
    abstract fun bindMidtransRepository(
        midtransRepositoryImpl: MidtransRepositoryImpl
    ): MidtransRepository
}