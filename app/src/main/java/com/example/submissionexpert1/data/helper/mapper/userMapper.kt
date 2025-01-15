package com.example.submissionexpert1.data.helper.mapper

import com.example.domain.model.User
import com.example.submissionexpert1.data.db.entity.UserEntity

fun UserEntity.toUser() = User(
  userId = userId,
  firstName = firstName,
  lastName = lastName,
  email = email,
  password = password
)

fun User.toEntity() = UserEntity(
  userId = userId,
  firstName = firstName,
  lastName = lastName,
  email = email,
  password = password
)
