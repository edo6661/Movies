package com.example.submissionexpert1.domain.model

data class User(
  val userId : Long = 0,
  val firstName : String,
  val lastName : String,
  val email : String,
  val password : String
)