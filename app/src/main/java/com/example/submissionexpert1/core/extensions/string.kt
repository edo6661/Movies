package com.example.submissionexpert1.core.extensions

fun String.validateEmail() : Boolean {
  val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
  return this.matches(emailPattern.toRegex())
}

fun String.validatePassword() : Boolean {
  val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
  return this.matches(passwordPattern.toRegex())
}

fun String.validateConfirmPassword(password : String) : Boolean {
  return this == password
}

fun String.validate3Char() : Boolean {
  return this.length >= 3
}


