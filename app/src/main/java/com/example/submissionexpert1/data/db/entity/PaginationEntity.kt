package com.example.submissionexpert1.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pagination")
data class PaginationEntity(
  @PrimaryKey val page : Int,
  val totalResults : Int,
  val totalPages : Int
)
