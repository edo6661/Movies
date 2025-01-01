package com.example.submissionexpert1.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.submissionexpert1.data.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {


  @Query("SELECT * FROM users WHERE userId = :id")
  fun getUserById(id : Long) : Flow<UserEntity>

  @Query("SELECT * FROM users")
  fun getAllUsers() : Flow<List<UserEntity>>

  @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
  fun login(email : String, password : String) : Flow<UserEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun register(user : UserEntity)

}