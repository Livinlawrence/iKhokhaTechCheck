package com.ikhokha.techcheck.domain.repository

import com.ikhokha.techcheck.domain.model.LoggedInUser
import com.ikhokha.techcheck.domain.util.Resource

interface LoginRepository {
    suspend fun login(username:String, password:String): Resource<LoggedInUser>
}