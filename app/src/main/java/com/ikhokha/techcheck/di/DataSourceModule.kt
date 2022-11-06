package com.ikhokha.techcheck.di

import com.ikhokha.techcheck.data.remote.firebase.FirebaseBusyShopNetwork
import com.ikhokha.techcheck.data.remote.BusyShopNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    @Singleton
    fun bindLoginDataSource(firebaseBusyShopNetwork: FirebaseBusyShopNetwork): BusyShopNetworkDataSource
}