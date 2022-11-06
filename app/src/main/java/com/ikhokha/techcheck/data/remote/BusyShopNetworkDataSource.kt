package com.ikhokha.techcheck.data.remote

import com.ikhokha.techcheck.domain.model.LoggedInUser
import com.ikhokha.techcheck.domain.model.Product
import com.ikhokha.techcheck.domain.util.Resource


interface BusyShopNetworkDataSource {
    suspend fun login(email: String, password: String): Resource<LoggedInUser>

    suspend fun getProducts(): List<Product>

}