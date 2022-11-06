package com.ikhokha.techcheck.data.repository

import com.ikhokha.techcheck.data.remote.BusyShopNetworkDataSource
import com.ikhokha.techcheck.domain.model.LoggedInUser
import com.ikhokha.techcheck.domain.repository.LoginRepository
import com.ikhokha.techcheck.domain.util.Resource
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val busyShopNetworkDataSource: BusyShopNetworkDataSource
) :
    LoginRepository {

    override suspend fun login(username: String, password: String): Resource<LoggedInUser> {
        return busyShopNetworkDataSource.login(username, password)
    }
}