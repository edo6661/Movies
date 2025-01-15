package com.example.submissionexpert1.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
  @PrimaryKey(autoGenerate = true)
  val userId : Long = 0,
  @ColumnInfo(name = "first_name") val firstName : String,
  @ColumnInfo(name = "last_name") val lastName : String,
  val email : String,
  val password : String
)