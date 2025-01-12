package com.example.submissionexpert1.presentation.ui.shared.movie

import android.icu.text.DecimalFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.submissionexpert1.R
import com.example.submissionexpert1.core.constants.Prefix
import com.example.submissionexpert1.core.utils.safeUiString
import com.example.submissionexpert1.domain.model.Movie
import com.example.submissionexpert1.presentation.common.Size
import com.example.submissionexpert1.presentation.ui.shared.MainText
import com.example.submissionexpert1.presentation.ui.theme.Orange


@Composable
fun CardMovieItem(
  modifier : Modifier = Modifier,
  movie : Movie,
  onClick : () -> Unit,
  isLoadingToggleFavorite : Boolean,
  onToggleFavorite : (Int) -> Unit,
  userId : Long? = null

) {
//  val isActive =
  Card(
    modifier = modifier
      .fillMaxWidth(),
    onClick = onClick,
    colors = CardColors(
      containerColor = Color.Transparent,
      contentColor = MaterialTheme.colorScheme.onSecondary,
      disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
      disabledContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.5f),

      ),
    shape = MaterialTheme.shapes.medium,

    border = BorderStroke(
      width = 1.dp,
      color = MaterialTheme.colorScheme.tertiary
    )

  ) {
    Row(
      verticalAlignment = Alignment.Top,
      horizontalArrangement = Arrangement.spacedBy(
        16.dp
      ),
    ) {


      AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
          .data(Prefix.PREFIX_IMAGE_URL + movie.posterPath)
          .crossfade(true)
          .diskCachePolicy(CachePolicy.ENABLED)
          .diskCacheKey(movie.posterPath)
          .memoryCacheKey(movie.posterPath)
          .memoryCachePolicy(CachePolicy.ENABLED)
          .build(),
        contentDescription = movie.title,
        placeholder = painterResource(id = R.drawable.placeholder),
        error = painterResource(id = R.drawable.error_image),
        modifier = Modifier
          .fillMaxWidth(0.4f)
          .clip(
            RoundedCornerShape(
              topStart = 8.dp,
              bottomStart = 8.dp,
            )
          ),
        contentScale = ContentScale.Crop
      )




      Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
          .fillMaxWidth()
          .padding(
            top = 16.dp,
          )
      ) {
        MainText(
          text = movie.title,
          textSize = Size.Large,
          isEllipsis = true,
          maxLines = 3,
          modifier = Modifier.fillMaxWidth(0.9f)
        )
        Column(
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

          val keyValues : Map<ImageVector, String> = mapOf(
            Pair(
              Icons.Default.Star,
              DecimalFormat("#.#").format(movie.voteAverage)
            ),
            Pair(
              Icons.Default.DateRange,
              safeUiString(movie.releaseDate.take(4))

            ),
            Pair(
              Icons.Default.HowToVote,
              safeUiString(movie.voteCount.toString()),
            ),
          )
          keyValues.forEach { (key, value) ->
            MovieItemKeyValue(
              key = key,
              value = value,
              color = if (key == Icons.Default.Star) Orange else MaterialTheme.colorScheme.onBackground
            )


          }

        }

      }
    }
  }

}


@Composable
private fun MovieItemKeyValue(
  key : ImageVector,
  value : String,
  color : Color = MaterialTheme.colorScheme.onBackground
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Icon(
      imageVector = key,
      contentDescription = value,
      tint = color,
    )

    MainText(
      text = value,
      textSize = Size.Medium,
      color = color,


      )
  }
}