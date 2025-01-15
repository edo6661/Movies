package com.example.submissionexpert1.data.api

import com.google.gson.annotations.SerializedName

data class ApiError(
  val success : Boolean,
  @field:SerializedName("status_code")
  val statusCode : Int,
  @field:SerializedName("status_message")
  val statusMessage : String
)
