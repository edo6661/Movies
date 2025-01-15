package com.example.domain.repository.user

import com.example.domain.common.Result
import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {

  suspend fun login(email : String, password : String) : Flow<Result<String>>
  suspend fun register(user : User) : Flow<Result<String>>
  suspend fun update(user : User, newPassword : String) : Flow<Result<String>>
}