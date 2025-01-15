package com.example.submissionexpert1.data.helper.mapper

import com.example.submissionexpert1.data.db.entity.UserEntity
import com.example.submissionexpert1.domain.model.User

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
