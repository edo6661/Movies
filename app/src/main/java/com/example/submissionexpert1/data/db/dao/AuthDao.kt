package com.example.submissionexpert1.data.db.dao

import androidx.room.*
import com.example.submissionexpert1.data.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthDao {

  @Query("SELECT * FROM users WHERE email = :email")
  fun login(email : String) : Flow<UserEntity?>

  @Query("SELECT COUNT(*) > 0 FROM users WHERE userId = :userId AND password = :password")
  fun isPasswordMatch(userId : Long, password : String) : Flow<Boolean>

  @Query("SELECT COUNT(*) > 0 FROM users WHERE email = :email")
  fun isEmailExist(email : String) : Flow<Boolean>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun register(user : UserEntity)

  @Update(onConflict = OnConflictStrategy.ABORT)
  suspend fun update(user : UserEntity)

}
