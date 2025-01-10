package com.example.submissionexpert1.presentation.implementation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.NoPhotography
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.submissionexpert1.R
import com.example.submissionexpert1.core.constants.Prefix
import com.example.submissionexpert1.domain.model.Movie
import com.example.submissionexpert1.presentation.common.Size
import com.example.submissionexpert1.presentation.ui.shared.MainText
import com.example.submissionexpert1.presentation.ui.shared.movie.DetailRating
import com.example.submissionexpert1.presentation.ui.state.error.MainError
import com.example.submissionexpert1.presentation.ui.state.loading.CenteredCircularLoading
import com.example.submissionexpert1.presentation.ui.theme.CoolGray
import com.example.submissionexpert1.presentation.viewmodel.DetailEvent
import com.example.submissionexpert1.presentation.viewmodel.DetailViewModel

@Composable
fun DetailScreen(
  modifier : Modifier = Modifier,
  vm : DetailViewModel = hiltViewModel(),
  id : Int,
  navigateToLogin : () -> Unit
) {

  val state by vm.state.collectAsState()
  fun onEvent(event : DetailEvent) {
    vm.onEvent(event)
  }


  Column(
    modifier = modifier.fillMaxSize(),
  ) {
    when {
      state.isLoading -> {
        CenteredCircularLoading(
          modifier = Modifier.fillMaxSize()
        )
      }

      state.error != null -> {
        Box(
          modifier = Modifier.padding(
            horizontal = 16.dp
          )
        ) {
          MainError(
            message = state.error as String,
            onRetry = { onEvent(DetailEvent.OnMovieLoaded(id)) }
          )
        }
      }

      state.movie != null -> {
        DetailContent(
          movie = state.movie as Movie,
          onToggleFavorite = { id -> onEvent(DetailEvent.OnToggleFavorite(id)) },
          userId = state.userId,
          navigateToLogin = navigateToLogin
        )
      }
    }

  }
}

@Composable
fun DetailContent(
  movie : Movie,
  onToggleFavorite : (Int) -> Unit,
  userId : Long? = null,
  navigateToLogin : () -> Unit
) {
  var currentTab by remember { mutableIntStateOf(0) }

  Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    TopSection(
      movie = movie,
      onToggleFavorite = onToggleFavorite,
      userId = userId,
      navigateToLogin = navigateToLogin
    )
    MiddleSection(
      movie = movie
    )
    BottomSection(
      movie = movie,
      currentTab = currentTab,
      onTabSelected = { tabIndex ->
        currentTab = tabIndex
      }

    )
  }
}

@Composable
private fun BottomSection(
  movie : Movie,
  currentTab : Int,
  onTabSelected : (Int) -> Unit
) {
  Column(
    modifier = Modifier
      .padding(
        horizontal = 16.dp,
        vertical = 32.dp
      )
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(12.dp),

      ) {


      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
      ) {

        MainText(
          text = "Overview",
          textSize = Size.Large,
          modifier = Modifier

            .weight(0.5f)
            .clickable { onTabSelected(0) },
          textAlign = TextAlign.Center,
          color = if (currentTab == 0)
            MaterialTheme.colorScheme.primary
          else
            MaterialTheme.colorScheme.onSurface
        )

        MainText(
          text = "Genre",
          textSize = Size.Large,
          modifier = Modifier
            .weight(0.5f)
            .clickable { onTabSelected(1) },
          textAlign = TextAlign.Center,
          color = if (currentTab == 1)
            MaterialTheme.colorScheme.primary
          else
            MaterialTheme.colorScheme.onSurface
        )
      }
      val indicatorOffset by animateFloatAsState(
        targetValue = currentTab * 0.5f,
        label = "indicator"
      )
      val screenWithDp = LocalConfiguration.current.screenWidthDp

      Box(
        modifier = Modifier
          .padding()
          .fillMaxWidth(0.5f)
          .height(4.dp)
          .offset {
            IntOffset(
              // ! 32 nih: padding horizontal
              x = (indicatorOffset * (screenWithDp - 32).dp.toPx()).toInt(),
              y = 0
            )
          }
          .background(
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
          )
      )

    }


  }
}

@Composable
private fun TopSection(
  movie : Movie,
  onToggleFavorite : (Int) -> Unit,
  userId : Long? = null,
  navigateToLogin : () -> Unit

) {
  Box(
    modifier = Modifier
  ) {

    Box() {
      Image(
        painter = rememberAsyncImagePainter(
          model = Prefix.PREFIX_IMAGE_URL + movie.backdropPath,
          placeholder = painterResource(id = R.drawable.user_placeholder),
          error = painterResource(id = R.drawable.error_image)
        ),
        contentDescription = movie.title,
        contentScale = ContentScale.Fit,
        modifier = Modifier
          .fillMaxWidth()
          .clip(
            RoundedCornerShape(
              bottomEnd = 24.dp,
              bottomStart = 24.dp
            )
          )
      )
      Row(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(
            start = 16.dp
          )
          .offset(
            y = (80).dp
          ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)

      ) {
        Image(
          rememberAsyncImagePainter(
            model = Prefix.PREFIX_IMAGE_URL + movie.posterPath,
            error = painterResource(id = R.drawable.error_image),
          ),
          contentDescription = movie.title,
          modifier = Modifier
            .width(120.dp)
            .height(160.dp)
            .clip(
              RoundedCornerShape(
                16.dp
              )
            ),
          contentScale = ContentScale.Crop
        )
        MainText(
          text = movie.title,
          modifier = Modifier.padding(
            top = 40.dp
          ),
          textSize = Size.ExtraLarge
        )
      }

      DetailRating(
        modifier = Modifier
          .align(Alignment.BottomEnd),
        rating = movie.voteAverage
      )

    }

//    ToggleButtonFavorite(
//      isFavorite = movie.isFavorite,
//      isLoadingToggleFavorite = false,
//      onToggleFavorite = {
//
//        when (userId) {
//          null -> navigateToLogin()
//          else -> onToggleFavorite(movie.id)
//        }
//
//      },
//      id = movie.id,
//      modifier = Modifier
//        .align(Alignment.BottomEnd)
//        .clip(
//          RoundedCornerShape(
//            topStart = 4.dp,
//          )
//        )
//        .background(MaterialTheme.colorScheme.secondaryContainer)
//
//    )
  }
}

@Composable
private fun MiddleSection(
  movie : Movie
) {

  val paddingTopDodgePoster = 64
  Column(
    verticalArrangement = Arrangement.Center,
    modifier = Modifier
      .fillMaxWidth()
      .padding(
        horizontal = 16.dp,
      )
      .padding(
        top = (paddingTopDodgePoster + 32).dp
      ),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      KeyValueIcon(
        icon = Icons.Default.Star,
        key = "Popularity",
        value = movie.popularity.toString()
      )
      KeyValueIcon(
        icon = Icons.Default.NoPhotography,
        key = "Adult",
        value = if (movie.adult) "Yes" else "No"
      )
      KeyValueIcon(
        icon = Icons.Default.DateRange,
        key = "Release Date",
        value = movie.releaseDate.substring(0, 4),
        latest = true
      )


    }

  }
}

@Composable
private fun KeyValueIcon(
  key : String,
  value : String,
  icon : ImageVector,
  latest : Boolean = false
) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = key,
      tint = CoolGray
    )
    MainText(
      text = value,
      color = CoolGray
    )
  }
  if (! latest) {
    VerticalDivider(
      modifier = Modifier.height(24.dp),
    )
  }
}
