package com.example.domain.event

sealed class FavoriteChangeEvent {
  data class Toggled(val movieId : Int) : FavoriteChangeEvent()
}
