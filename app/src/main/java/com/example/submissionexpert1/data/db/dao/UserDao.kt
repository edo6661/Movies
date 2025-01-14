package com.example.submissionexpert1.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.submissionexpert1.data.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

  @Query("SELECT * FROM users WHERE userId = :id")
  fun getUserById(id : Long) : Flow<UserEntity>

  @Query("SELECT * FROM users")
  fun getAllUsers() : Flow<List<UserEntity>>


}

