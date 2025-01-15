package com.example.submissionexpert1.presentation.utils

import com.example.domain.model.PaginationMovie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

suspend fun avoidSameMovieId(
  currentData : com.example.domain.model.PaginationMovie?,
  incomingData : com.example.domain.model.PaginationMovie,
  dispatcher : CoroutineDispatcher
) : com.example.domain.model.PaginationMovie = withContext(dispatcher) {
  val existingIdsCurrentData = currentData?.results?.map { it.id }?.toSet() ?: emptySet()
  val filteredNewResults = incomingData.results.filterNot { it.id in existingIdsCurrentData }
  val combinedFilteredResults = currentData?.copy(
    results = currentData.results + filteredNewResults
  ) ?: incomingData.copy(results = filteredNewResults)
  combinedFilteredResults
}
