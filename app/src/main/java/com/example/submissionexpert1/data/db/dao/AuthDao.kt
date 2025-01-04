package com.example.submissionexpert1.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.submissionexpert1.data.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthDao {

  @Query("SELECT * FROM users WHERE email = :email")
  fun login(email : String) : Flow<UserEntity?>

  @Query("SELECT COUNT(*) > 0 FROM users WHERE email = :email")
  fun isEmailExist(email : String) : Flow<Boolean>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun register(user : UserEntity)
}
