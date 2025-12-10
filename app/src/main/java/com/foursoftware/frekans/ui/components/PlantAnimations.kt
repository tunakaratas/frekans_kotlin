package com.foursoftware.frekans.ui.components
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun AnimatedPlantIcon(
    icon: String,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "plant_animation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )
    val scale by infiniteTransition.animateFloat(
        initialValue = if (isPlaying) 1f else 0.98f,
        targetValue = if (isPlaying) 1.15f else 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    val alphaValue by infiniteTransition.animateFloat(
        initialValue = if (isPlaying) 0.9f else 0.95f,
        targetValue = if (isPlaying) 1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    Box(
        modifier = modifier
            .graphicsLayer {
                rotationZ = rotation
                scaleX = scale
                scaleY = scale
                alpha = alphaValue
            },
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Text(
            text = icon,
            fontSize = 48.sp,
            style = MaterialTheme.typography.displaySmall
        )
    }
}
@Composable
fun GrowingPlantAnimation(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "growing")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "grow"
    )
    Box(
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Text(
            text = "🌱",
            fontSize = 64.sp
        )
    }
}
@Composable
fun FloatingLeavesAnimation(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf("🍃", "🌿", "🍀").forEachIndexed { index, leaf ->
            FloatingLeaf(
                icon = leaf,
                delay = index * 200
            )
        }
    }
}
@Composable
fun FloatingLeaf(
    icon: String,
    delay: Int
) {
    val infiniteTransition = rememberInfiniteTransition(label = "float_$icon")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -20f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000 + delay,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )
    val rotation by infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000 + delay,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )
    Box(
        modifier = Modifier
            .graphicsLayer {
                translationY = offsetY
                rotationZ = rotation
            }
    ) {
        androidx.compose.material3.Text(
            text = icon,
            fontSize = 24.sp
        )
    }
}
@Composable
fun PulsingFrequencyIndicator(
    frequency: Double,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "frequency_pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = if (isPlaying) 0.6f else 0.4f,
        targetValue = if (isPlaying) 1f else 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    val scale by infiniteTransition.animateFloat(
        initialValue = if (isPlaying) 1f else 0.97f,
        targetValue = if (isPlaying) 1.08f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    androidx.compose.material3.Surface(
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        shadowElevation = if (isPlaying) 4.dp else 2.dp,
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
    ) {
        androidx.compose.material3.Text(
            text = "${frequency} Hz",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary.copy(alpha = alpha),
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
