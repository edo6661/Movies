package com.example.submissionexpert1.presentation.implementation

import androidx.compose.animation.*
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
import com.example.submissionexpert1.domain.model.Genre
import com.example.submissionexpert1.domain.model.Movie
import com.example.submissionexpert1.domain.model.MovieWithGenres
import com.example.submissionexpert1.presentation.common.Size
import com.example.submissionexpert1.presentation.ui.animation.TabsSpec
import com.example.submissionexpert1.presentation.ui.shared.MainButton
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
          movie = state.movie as MovieWithGenres,
          onToggleFavorite = { id -> onEvent(DetailEvent.OnToggleFavorite(id)) },
          userId = state.userId,
          navigateToLogin = navigateToLogin,
          onToggleShowAllOverview = { onEvent(DetailEvent.OnToggleShowAllOverview) },
          showAllOverview = state.showAllOverview
        )
      }
    }

  }
}

@Composable
fun DetailContent(
  movie : MovieWithGenres,
  onToggleFavorite : (Int) -> Unit,
  userId : Long? = null,
  navigateToLogin : () -> Unit,
  onToggleShowAllOverview : () -> Unit,
  showAllOverview : Boolean
) {
  var currentTab by remember { mutableIntStateOf(0) }

  val onlyMovie = movie.movie

  Column(
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    TopSection(
      movie = onlyMovie,
      onToggleFavorite = onToggleFavorite,
      userId = userId,
      navigateToLogin = navigateToLogin
    )
    MiddleSection(
      movie = onlyMovie
    )
    BottomSection(
      movie = movie,
      currentTab = currentTab,
      onTabSelected = { tabIndex ->
        currentTab = tabIndex
      },
      onToggleShowAllOverview = onToggleShowAllOverview,
      showAllOverview = showAllOverview

    )
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
            top = 60.dp
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
      IconMiddleValue(
        icon = Icons.Default.Star,
        key = "Popularity",
        value = movie.popularity.toInt().toString(),
      )
      IconMiddleValue(
        icon = Icons.Default.NoPhotography,
        key = "Adult",
        value = if (movie.adult) "Yes" else "No"
      )
      IconMiddleValue(
        icon = Icons.Default.DateRange,
        key = "Release Date",
        value = movie.releaseDate.substring(0, 4),
        latest = true
      )


    }

  }
}

@Composable
private fun IconMiddleValue(
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


@Composable
private fun BottomAction(
  modifier : Modifier,
  onClick : () -> Unit,
  title : String,
  isActive : Boolean
) {
  val animatedColor by animateColorAsState(
    targetValue = if (isActive) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(
      alpha = 0.5f
    ),
    label = "color",
  )

  Box(
    modifier = modifier
      .height(48.dp)
      .clickable {
        if (isActive) return@clickable
        onClick()
      },
    contentAlignment = Alignment.Center

  ) {
    MainText(
      text = title,
      textSize = Size.Large,
      textAlign = TextAlign.Center,
      color = animatedColor
    )
  }
}

@Composable
private fun BottomSection(
  movie : MovieWithGenres,
  currentTab : Int,
  onTabSelected : (Int) -> Unit,
  onToggleShowAllOverview : () -> Unit,
  showAllOverview : Boolean
) {
  Column(
    modifier = Modifier
      .padding(
        horizontal = 16.dp,
      ),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    CustomBottomTab(
      currentTab = currentTab,
      onTabSelected = onTabSelected
    )
    ContentBottomTab(
      currentTab = currentTab,
      overview = movie.movie.overview,
      genres = movie.genres,
      onToggleShowAllOverview = onToggleShowAllOverview,
      showAllOverview = showAllOverview
    )


  }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalLayoutApi::class)
@Composable
private fun ContentBottomTab(
  currentTab : Int,
  overview : String,
  genres : List<Genre>,
  onToggleShowAllOverview : () -> Unit,
  showAllOverview : Boolean
) {
  val overviewBasedOnShowAll = if (showAllOverview) overview else overview.substring(0, 200)

  AnimatedContent(
    targetState = currentTab,
    transitionSpec = {
      TabsSpec(targetState, 0)
    },
    label = "content",
    modifier = Modifier
      .fillMaxWidth()
  ) {
    when (it) {
      0 -> {
        Column(
          modifier = Modifier.animateContentSize()
        ) {
          AnimatedContent(
            targetState = overviewBasedOnShowAll,
            label = "overview",
            transitionSpec = {
              (fadeIn() + slideInVertically())
                .togetherWith(fadeOut() + slideOutVertically())
            }
          ) { animatedOverview ->
            MainText(
              text = animatedOverview,
              textSize = Size.Medium
            )
          }

          MainText(
            text = if (showAllOverview) "Show Less" else "Show All",
            textSize = Size.Medium,
            color = MaterialTheme.colorScheme.primary,
            onClick = onToggleShowAllOverview
          )
        }

      }

      1 -> {
        FlowRow(
          modifier = Modifier
            .fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          genres.forEach { genre ->
            MainButton(
              text = genre.name,
              onClick = { /* TODO: navigate ke genre screen */ },
            )
          }
        }


      }
    }
  }
}


@Composable
private fun CustomBottomTab(
  currentTab : Int,
  onTabSelected : (Int) -> Unit

) {
  val indicatorOffset by animateFloatAsState(
    targetValue = currentTab * 0.5f,
    label = "indicator"
  )
  val screenWithDp = LocalConfiguration.current.screenWidthDp

  Column(

  ) {

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceAround,
      modifier = Modifier.fillMaxWidth()
    ) {
      BottomAction(
        modifier = Modifier
          .weight(0.5f),
        onClick = {
          onTabSelected(0)
        },
        title = "Overview",
        isActive = currentTab == 0
      )
      BottomAction(
        modifier = Modifier
          .weight(0.5f),
        onClick = {
          onTabSelected(1)
        },
        title = "Genre",
        isActive = currentTab == 1

      )
    }

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
          color = MaterialTheme.colorScheme.tertiary,
        )
    )

  }

}