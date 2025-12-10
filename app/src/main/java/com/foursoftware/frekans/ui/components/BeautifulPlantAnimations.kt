package com.foursoftware.frekans.ui.components
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foursoftware.frekans.ui.theme.*
import com.foursoftware.frekans.ui.components.FloatingLeaf
import com.foursoftware.frekans.ui.components.FloatingLeavesAnimation
import com.foursoftware.frekans.ui.components.AnimatedPlantIcon
@Composable
fun BeautifulPlantBackground(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        FloatingLeavesAnimation(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )
        FloatingLeavesAnimation(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val paint = Paint().apply {
                color = ForestGreen40.copy(alpha = 0.05f)
                style = PaintingStyle.Stroke
                strokeWidth = 2.dp.toPx()
            }
            repeat(5) { i ->
                val y = size.height * (i + 1) / 6f
                val x = size.width * (i % 2 + 1) / 3f
                drawCircle(
                    color = ForestGreen40.copy(alpha = 0.03f),
                    radius = 30.dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }
    }
}
@Composable
fun AnimatedPlantWithLeaves(
    icon: String,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "plant_leaves")
    val rotation by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )
    val scale by infiniteTransition.animateFloat(
        initialValue = if (isPlaying) 1f else 0.95f,
        targetValue = if (isPlaying) 1.15f else 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(24.dp)) {
                FloatingLeaf(
                    icon = "🍃",
                    delay = 0
                )
            }
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        rotationZ = rotation
                        scaleX = scale
                        scaleY = scale
                    }
            ) {
                androidx.compose.material3.Text(
                    text = icon,
                    fontSize = 48.sp,
                    style = MaterialTheme.typography.displaySmall
                )
            }
            Box(modifier = Modifier.size(24.dp)) {
                FloatingLeaf(
                    icon = "🍃",
                    delay = 1000
                )
            }
        }
    }
}
@Composable
fun BeautifulPlantCard(
    plantIcon: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "beautiful_plant")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    Box(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .graphicsLayer {
                shadowElevation = 8.dp.toPx()
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ForestGreen40.copy(alpha = glowAlpha),
                        Color.Transparent
                    )
                ),
                radius = size.width / 2
            )
        }
        AnimatedPlantIcon(
            icon = plantIcon,
            isPlaying = false,
            modifier = Modifier.size(40.dp)
        )
    }
}
@Composable
fun GrowingPlantVisualization(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "growing_visualization")
    val stemHeight by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "stem"
    )
    val leafScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "leaf"
    )
    Box(
        modifier = modifier.size(120.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val bottomY = size.height
            val topY = size.height * (1 - stemHeight)
            val paint = Paint().apply {
                color = ForestGreen40
                style = PaintingStyle.Stroke
                strokeWidth = 4.dp.toPx()
            }
            drawLine(
                color = ForestGreen40,
                start = Offset(centerX, bottomY),
                end = Offset(centerX, topY),
                strokeWidth = 4.dp.toPx()
            )
            val leafSize = 20.dp.toPx() * leafScale
            drawCircle(
                color = SageGreen40.copy(alpha = 0.6f),
                radius = leafSize,
                center = Offset(centerX - 15.dp.toPx(), topY + 10.dp.toPx())
            )
            drawCircle(
                color = SageGreen40.copy(alpha = 0.6f),
                radius = leafSize,
                center = Offset(centerX + 15.dp.toPx(), topY + 10.dp.toPx())
            )
        }
    }
}
@Composable
fun AnimatedPlantGarden(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf("🌿", "🌱", "🍃", "🌾").forEachIndexed { index, plant ->
            val delay = index * 300
            val infiniteTransition = rememberInfiniteTransition(label = "garden_$index")
            val offsetY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -15f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000 + delay, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "offset"
            )
            val rotation by infiniteTransition.animateFloat(
                initialValue = -10f,
                targetValue = 10f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3000 + delay, easing = LinearEasing),
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
                    text = plant,
                    fontSize = 32.sp
                )
            }
        }
    }
}
@Composable
fun FrequencyWaveVisualization(
    isPlaying: Boolean,
    frequency: Double,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "frequency_wave")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (1000 / (frequency / 100)).toInt().coerceIn(500, 2000),
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave"
    )
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        val paint = Paint().apply {
            color = if (isPlaying) ForestGreen40 else ForestGreen40.copy(alpha = 0.3f)
            style = PaintingStyle.Stroke
            strokeWidth = 3.dp.toPx()
        }
        val centerY = size.height / 2
        val amplitude = size.height * 0.3f
        val path = Path().apply {
            moveTo(0f, centerY)
            for (x in 0..size.width.toInt() step 5) {
                val y = centerY + amplitude * kotlin.math.sin(
                    (x.toFloat() / size.width * 4 * kotlin.math.PI) + 
                    (waveOffset * kotlin.math.PI / 180)
                ).toFloat()
                lineTo(x.toFloat(), y)
            }
        }
        drawPath(path, color = paint.color, style = Stroke(width = paint.strokeWidth))
    }
}
