package com.example.submissionexpert1.data.db.dao

import androidx.room.*
import com.example.submissionexpert1.data.db.entity.UserEntity
import com.example.submissionexpert1.data.db.entity.relation.UserWithFavoriteMovies
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUser(user : UserEntity)

  @Query("SELECT * FROM users WHERE userId = :id")
  fun getUserById(id : Long) : Flow<UserEntity>

  @Query("SELECT * FROM users")
  fun getAllUsers() : Flow<List<UserEntity>>

  @Transaction
  @Query("SELECT * FROM users WHERE userId = :userId")
  fun getUserWithFavoriteMovies(userId : Long) : Flow<UserWithFavoriteMovies>


}