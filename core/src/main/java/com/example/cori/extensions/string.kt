package com.example.cori.extensions

fun String.validateEmail() : Boolean {
  val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
  return this.matches(emailPattern.toRegex())
}


fun String.validateConfirmPassword(password : String) : Boolean {
  return this == password
}

fun String.validate3Char() : Boolean {
  return this.length >= 3
}


