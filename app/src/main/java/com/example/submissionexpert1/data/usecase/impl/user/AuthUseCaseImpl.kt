package com.example.submissionexpert1.data.usecase.impl.user

import com.example.domain.common.Result
import com.example.domain.model.User
import com.example.domain.repository.user.IAuthRepository
import com.example.domain.usecase.user.IAuthUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthUseCaseImpl @Inject constructor(
  private val repository : IAuthRepository
) : IAuthUseCase {

  override suspend fun login(email : String, password : String) : Flow<Result<String>> {
    return repository.login(email, password)
  }

  override suspend fun register(user : User) : Flow<Result<String>> {
    return repository.register(user)
  }

  override suspend fun update(user : User, newPassword : String) : Flow<Result<String>> {
    return repository.update(user, newPassword)
  }


}