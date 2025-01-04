package com.example.submissionexpert1.data.usecase.impl.user

import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.model.User
import com.example.submissionexpert1.domain.repository.user.IAuthRepository
import com.example.submissionexpert1.domain.usecase.user.IAuthUseCase
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


}