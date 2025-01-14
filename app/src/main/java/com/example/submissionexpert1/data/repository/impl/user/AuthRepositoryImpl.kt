package com.example.submissionexpert1.data.repository.impl.user

import com.example.submissionexpert1.core.utils.checkPassword
import com.example.submissionexpert1.data.db.dao.AuthDao
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
  private val authDao : AuthDao,
  private val pref : UserPreferences

) : IAuthRepository, BaseRepository() {

  override suspend fun login(email : String, password : String) : Flow<Result<String>> = flow {

    val loginResult = safeDatabaseCall {
      authDao.login(email).firstOrNull()
    }
    emit(handleLogin(loginResult, password))
  }

  private suspend fun handleLogin(
    resultUser : Result<UserEntity?>,
    password : String
  ) : Result<String> {
    return when (resultUser) {
      is Result.Success -> {
        checkUser(resultUser.data, password)
      }


      is Result.Error   -> {
        Result.Error(resultUser.message)
      }
    }
  }

  private suspend fun checkUser(user : UserEntity?, password : String) : Result<String> {
    return if (user != null && checkPassword(password, user.password)) {
      pref.saveUserSession(user.toUser())
      Result.Success("Login Success")
    } else {
      Result.Error("Invalid Email or Password")
    }
  }


  override suspend fun register(user : User) : Flow<Result<String>> = flow {
    val isEmailExistResult = safeDatabaseCall {
      authDao.isEmailExist(user.email).firstOrNull() ?: false
    }
    if (isEmailExistResult is Result.Success && isEmailExistResult.data) {
      emit(Result.Error("Email Already Exist"))
      return@flow
    }
    val registerResult = safeDatabaseCall {
      authDao.register(user.toEntity())
    }
    emit(handleRegister(registerResult))
  }


  private fun handleRegister(result : Result<Unit>) : Result<String> {
    return when (result) {
      is Result.Success -> {
        Result.Success("Register Success")
      }


      is Result.Error   -> {
        Result.Error(result.message)
      }
    }

  }


}