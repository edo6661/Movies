package com.example.submissionexpert1.core.utils

import org.mindrot.jbcrypt.BCrypt

fun hashPassword(password : String) : String {
  return BCrypt.hashpw(password, BCrypt.gensalt())
}

fun checkPassword(plainPassword : String, hashedPassword : String) : Boolean {
  return BCrypt.checkpw(plainPassword, hashedPassword)
}


