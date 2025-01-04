package com.example.submissionexpert1.data.repository.impl.user

import com.example.submissionexpert1.core.constants.ErrorMessages
import com.example.submissionexpert1.data.db.dao.UserDao
import com.example.submissionexpert1.data.db.entity.UserEntity
import com.example.submissionexpert1.data.helper.mapper.toEntity
import com.example.submissionexpert1.data.helper.mapper.toUser
import com.example.submissionexpert1.data.repository.BaseRepository
import com.example.submissionexpert1.data.source.local.preferences.UserPreferences
import com.example.submissionexpert1.domain.common.Result
import com.example.submissionexpert1.domain.model.User
import com.example.submissionexpert1.domain.repository.user.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
  private val dao : UserDao,
  private val pref : UserPreferences

) : IAuthRepository, BaseRepository() {

  override suspend fun login(email : String, password : String) : Flow<Result<String>> = flow {
    emit(Result.Loading)
    val result = safeDatabaseCall {
      dao.login(email, password).firstOrNull()
    }
    emit(handleLogin(result))
  }

  private suspend fun handleLogin(result : Result<UserEntity?>) : Result<String> {
    when (result) {
      is Result.Success -> {
        val user = result.data?.toUser() ?: return Result.Error("Login Failed")
        pref.saveUserSession(user)
        return Result.Success("Login Success")
      }

      is Result.Loading -> {
        return Result.Loading
      }

      is Result.Error   -> {
        return Result.Error("Login Failed")
      }
    }

  }

  override suspend fun register(user : User) : Flow<Result<String>> = flow {
    emit(Result.Loading)
    val isEmailExistResult = safeDatabaseCall {
      dao.isEmailExist(user.email).firstOrNull() ?: false
    }

    if (isEmailExistResult is Result.Success && isEmailExistResult.data) {
      emit(Result.Error("Email Already Exist"))
      return@flow
    }

    val result = safeDatabaseCall {
      dao.register(user.toEntity())
    }
    emit(handleRegister(result))
  }

  private fun handleRegister(result : Result<Unit>) : Result<String> {
    return when (result) {
      is Result.Success -> {
        Result.Success("Register Success")
      }

      is Result.Loading -> {
        Result.Loading
      }

      is Result.Error   -> {
        Result.Error(ErrorMessages.SOMETHING_WENT_WRONG)
      }
    }

  }


  override suspend fun logout() {
    try {
      pref.clearUserSession()
    } catch (e : Exception) {
      e.printStackTrace()
      throw Exception("Logout Failed")
    }

  }


}