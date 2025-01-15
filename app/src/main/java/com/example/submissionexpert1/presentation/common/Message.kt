package com.example.submissionexpert1.presentation.common

sealed class Message {
  data class Error(val message : String) : Message()
  data class Success(val message : String) : Message()
}