package com.ikhokha.techcheck.di

import com.ikhokha.techcheck.data.repository.CartRepositoryImpl
import com.ikhokha.techcheck.data.repository.HomeRepositoryImpl
import com.ikhokha.techcheck.data.repository.LoginRepositoryImpl
import com.ikhokha.techcheck.domain.repository.CartRepository
import com.ikhokha.techcheck.domain.repository.HomeRepository
import com.ikhokha.techcheck.domain.repository.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindLoginRepository(
        loginRepositoryImpl: LoginRepositoryImpl
    ): LoginRepository

    @Binds
    @Singleton
    fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    fun bindCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository
}