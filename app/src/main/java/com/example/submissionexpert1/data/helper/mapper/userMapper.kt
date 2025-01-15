package com.example.submissionexpert1.data.helper.mapper

import com.example.submissionexpert1.data.db.entity.UserEntity
import com.example.domain.model.User

fun UserEntity.toUser() = com.example.domain.model.User(
  userId = userId,
  firstName = firstName,
  lastName = lastName,
  email = email,
  password = password
)

fun com.example.domain.model.User.toEntity() = UserEntity(
  userId = userId,
  firstName = firstName,
  lastName = lastName,
  email = email,
  password = password
)
