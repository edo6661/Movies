package com.example.submissionexpert1.data.repository.impl.user

import com.example.cori.constants.Auth
import com.example.cori.utils.checkPassword
import com.example.cori.utils.hashPassword
import com.example.submissionexpert1.data.db.dao.AuthDao
import com.example.submissionexpert1.data.db.entity.UserEntity
import com.example.submissionexpert1.data.helper.mapper.toEntity
import com.example.submissionexpert1.data.helper.mapper.toUser
import com.example.submissionexpert1.data.repository.BaseRepository
import com.example.submissionexpert1.data.source.local.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
  private val authDao : AuthDao,
  private val pref : UserPreferences

) : com.example.domain.repository.user.IAuthRepository, BaseRepository() {

  override suspend fun login(
    email : String,
    password : String
  ) : Flow<com.example.domain.common.Result<String>> = flow {

    val loginResult = safeDatabaseCall {
      authDao.login(email).firstOrNull()
    }
    emit(handleLogin(loginResult, password))
  }

  private suspend fun handleLogin(
    resultUser : com.example.domain.common.Result<UserEntity?>,
    password : String
  ) : com.example.domain.common.Result<String> {
    return when (resultUser) {
      is com.example.domain.common.Result.Success -> {
        checkUser(resultUser.data, password)
      }


      is com.example.domain.common.Result.Error   -> {
        com.example.domain.common.Result.Error(resultUser.message)
      }
    }
  }

  private suspend fun checkUser(
    user : UserEntity?,
    password : String
  ) : com.example.domain.common.Result<String> {
    return if (user != null && com.example.cori.utils.checkPassword(password, user.password)) {
      pref.saveUserSession(user.toUser())
      com.example.domain.common.Result.Success("Login Success")
    } else {
      com.example.domain.common.Result.Error("Invalid Email or Password")
    }
  }


  override suspend fun register(user : com.example.domain.model.User) : Flow<com.example.domain.common.Result<String>> =
    flow {
      val isEmailExistResult = safeDatabaseCall {
        authDao.isEmailExist(user.email).firstOrNull() ?: false
      }
      if (isEmailExistResult is com.example.domain.common.Result.Success && isEmailExistResult.data) {
        emit(com.example.domain.common.Result.Error("Email Already Exist"))
        return@flow
      }
      val registerResult = safeDatabaseCall {
        authDao.register(user.toEntity())
      }
      emit(handleRegister(registerResult))
    }


  private fun handleRegister(result : com.example.domain.common.Result<Unit>) : com.example.domain.common.Result<String> {
    return when (result) {
      is com.example.domain.common.Result.Success -> {
        com.example.domain.common.Result.Success("Register Success")
      }


      is com.example.domain.common.Result.Error   -> {
        com.example.domain.common.Result.Error(result.message)
      }
    }
  }

  override suspend fun update(
    user : com.example.domain.model.User, newPassword : String
  ) : Flow<com.example.domain.common.Result<String>> = flow {
    val userFromPref = pref.getUserData().first() !!
    val isUserChangeEmail = user.email != userFromPref.email
    if (isUserChangeEmail) {
      val isEmailExistResult = safeDatabaseCall {
        authDao.isEmailExist(user.email).firstOrNull() ?: false
      }
      if (isEmailExistResult is com.example.domain.common.Result.Success && isEmailExistResult.data) {
        emit(com.example.domain.common.Result.Error(com.example.cori.constants.Auth.EMAIL_ALREADY_EXIST))
        return@flow
      }
    }
    val userFromLocal = safeDatabaseCall {
      authDao.login(userFromPref.email).first() !!
    }
    if (userFromLocal is com.example.domain.common.Result.Success && ! com.example.cori.utils.checkPassword(
        user.password,
        userFromLocal.data.password
      )
    ) {
      emit(com.example.domain.common.Result.Error(com.example.cori.constants.Auth.PASSWORD_INVALID))
      return@flow
    }


    val updateResult = safeDatabaseCall {
      authDao.update(
        user.copy(
          password = com.example.cori.utils.hashPassword(newPassword)
        ).toEntity(
        )
      )
    }
    emit(handleUpdate(updateResult))
  }

  private suspend fun handleUpdate(result : com.example.domain.common.Result<Unit>) : com.example.domain.common.Result<String> {
    return when (result) {
      is com.example.domain.common.Result.Success -> {
        pref.clearUserSession()
        com.example.domain.common.Result.Success("Update Success")
      }

      is com.example.domain.common.Result.Error   -> {
        com.example.domain.common.Result.Error(result.message)
      }
    }
  }


}