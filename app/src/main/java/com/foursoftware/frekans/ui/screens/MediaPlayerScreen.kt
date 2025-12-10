package com.foursoftware.frekans.ui.screens
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeDown
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foursoftware.frekans.data.PlantRepository
import com.foursoftware.frekans.ui.components.AnimatedPlantIcon
import com.foursoftware.frekans.ui.components.PulsingFrequencyIndicator
import com.foursoftware.frekans.ui.theme.*
import com.foursoftware.frekans.viewmodel.PlantDetailViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaPlayerScreen(
    plantId: Int,
    frequency: Double,
    onBackClick: () -> Unit,
    viewModel: PlantDetailViewModel = viewModel()
) {
    val plant = PlantRepository.getPlantById(plantId) ?: return
    val isPlaying by viewModel.isPlaying.collectAsState()
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    val volume by viewModel.volume.collectAsState()
    val isShuffleEnabled by viewModel.isShuffleEnabled.collectAsState()
    val repeatMode by viewModel.repeatMode.collectAsState()
    LaunchedEffect(plantId, frequency) {
        viewModel.setPlant(plant)
        viewModel.resetTimer()
        viewModel.playFrequency(frequency)
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopAllFrequencies()
        }
    }
    val infinitePulse = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infinitePulse.animateFloat(
        initialValue = 1f,
        targetValue = if (isPlaying) 1.08f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) {
        MaterialTheme.colorScheme.background
    } else {
        GradientLightStart
    }
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        plant.turkishName,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ForestGreen40,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = if (isDarkTheme) {
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF1A1A1A),
                                Color.Black
                            )
                        )
                    } else {
                        Brush.verticalGradient(
                            colors = listOf(
                                GradientLightStart,
                                Color.White
                            )
                        )
                    }
                )
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                item {
                    SpotifyAlbumArt(
                        icon = plant.icon,
                        isPlaying = isPlaying,
                        pulseScale = pulseScale,
                        isDarkTheme = isDarkTheme,
                        surfaceColor = surfaceColor
                    )
                }
                item {
                    SpotifySongInfo(
                        title = plant.turkishName,
                        artist = plant.name,
                        frequency = frequency,
                        isPlaying = isPlaying,
                        isDarkTheme = isDarkTheme,
                        onSurfaceColor = onSurfaceColor
                    )
                }
                item {
                    SpotifyPlayerControls(
                        isPlaying = isPlaying,
                        elapsedTime = elapsedTime,
                        onTogglePlayback = { viewModel.togglePlayback() },
                        onPrevious = { viewModel.playPreviousFrequency() },
                        onNext = { viewModel.playNextFrequency() },
                        isShuffleEnabled = isShuffleEnabled,
                        onShuffleToggle = { viewModel.toggleShuffle() },
                        repeatMode = repeatMode,
                        onRepeatToggle = { viewModel.toggleRepeat() },
                        isDarkTheme = isDarkTheme,
                        onSurfaceColor = onSurfaceColor
                    )
                }
                item {
                    SpotifyProgressBar(
                        progress = if (isPlaying) (elapsedTime % 100) / 100f else 0f,
                        currentTime = formatTime(elapsedTime),
                        totalTime = "∞",
                        isDarkTheme = isDarkTheme,
                        onSurfaceColor = onSurfaceColor
                    )
                }
                item {
                    SpotifyVolumeControl(
                        volume = volume,
                        onVolumeChange = { viewModel.setVolume(it) },
                        isDarkTheme = isDarkTheme,
                        onSurfaceColor = onSurfaceColor
                    )
                }
            }
        }
    }
}
@Composable
fun SpotifyAlbumArt(
    icon: String,
    isPlaying: Boolean,
    pulseScale: Float,
    isDarkTheme: Boolean,
    surfaceColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(horizontal = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(32.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            if (isDarkTheme) Color(0xFF2A2A2A) else PrimaryGreenLight.copy(alpha = 0.15f),
                            if (isDarkTheme) Color(0xFF1A1A1A) else SecondaryGreen.copy(alpha = 0.12f),
                            if (isDarkTheme) Color(0xFF0A0A0A) else TertiaryGreen.copy(alpha = 0.08f)
                        )
                    )
                )
                .graphicsLayer {
                    scaleX = if (isPlaying) pulseScale else 1f
                    scaleY = if (isPlaying) pulseScale else 1f
                }
                .shadow(
                    elevation = if (isPlaying) 32.dp else 20.dp,
                    shape = RoundedCornerShape(32.dp),
                    spotColor = if (isPlaying) PrimaryGreen.copy(alpha = 0.4f) else ShadowColor,
                    ambientColor = ShadowColorLight
                ),
            contentAlignment = Alignment.Center
        ) {
            AnimatedPlantIcon(
                icon = icon,
                isPlaying = isPlaying,
                modifier = Modifier.size(140.dp)
            )
        }
    }
}
@Composable
fun SpotifySongInfo(
    title: String,
    artist: String,
    frequency: Double,
    isPlaying: Boolean,
    isDarkTheme: Boolean,
    onSurfaceColor: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = if (isDarkTheme) Color.White else onSurfaceColor,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = artist,
            style = MaterialTheme.typography.titleMedium,
            color = if (isDarkTheme) Color.Gray else onSurfaceColor.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        PulsingFrequencyIndicator(
            frequency = frequency,
            isPlaying = isPlaying,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
@Composable
fun SpotifyPlayerControls(
    isPlaying: Boolean,
    elapsedTime: Int,
    onTogglePlayback: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    isShuffleEnabled: Boolean,
    onShuffleToggle: () -> Unit,
    repeatMode: com.foursoftware.frekans.viewmodel.RepeatMode,
    onRepeatToggle: () -> Unit,
    isDarkTheme: Boolean,
    onSurfaceColor: Color
) {
    val iconColor = if (isDarkTheme) Color.White else onSurfaceColor
    val secondaryIconColor = if (isDarkTheme) Color.Gray else onSurfaceColor.copy(alpha = 0.6f)
    val activeIconColor = PrimaryGreen
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onShuffleToggle,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Shuffle,
                    contentDescription = "Karıştır",
                    tint = if (isShuffleEnabled) activeIconColor else secondaryIconColor,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = onRepeatToggle,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Repeat,
                    contentDescription = when (repeatMode) {
                        com.foursoftware.frekans.viewmodel.RepeatMode.OFF -> "Tekrar Kapalı"
                        com.foursoftware.frekans.viewmodel.RepeatMode.ALL -> "Tümünü Tekrarla"
                        com.foursoftware.frekans.viewmodel.RepeatMode.ONE -> "Birini Tekrarla"
                    },
                    tint = when (repeatMode) {
                        com.foursoftware.frekans.viewmodel.RepeatMode.OFF -> secondaryIconColor
                        else -> activeIconColor
                    },
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPrevious,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FastRewind,
                    contentDescription = "Önceki",
                    tint = iconColor,
                    modifier = Modifier.size(36.dp)
                )
            }
            FloatingActionButton(
                onClick = onTogglePlayback,
                modifier = Modifier
                    .size(88.dp)
                    .shadow(
                        elevation = if (isPlaying) 20.dp else 14.dp,
                        shape = androidx.compose.foundation.shape.CircleShape,
                        spotColor = if (isPlaying) PrimaryGreen.copy(alpha = 0.5f) else ShadowColor,
                        ambientColor = ShadowColorLight
                    ),
                containerColor = if (isPlaying) PrimaryGreen else if (isDarkTheme) Color.White else PrimaryGreen,
                contentColor = if (isPlaying) Color.White else if (isDarkTheme) Color.Black else Color.White,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                ),
                shape = androidx.compose.foundation.shape.CircleShape
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Durdur" else "Çal",
                    modifier = Modifier.size(44.dp)
                )
            }
            IconButton(
                onClick = onNext,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FastForward,
                    contentDescription = "Sonraki",
                    tint = iconColor,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
@Composable
fun SpotifyProgressBar(
    progress: Float,
    currentTime: String,
    totalTime: String,
    isDarkTheme: Boolean,
    onSurfaceColor: Color
) {
    val trackColor = if (isDarkTheme) Color.White else PrimaryGreen
    val textColor = if (isDarkTheme) Color.Gray else onSurfaceColor.copy(alpha = 0.7f)
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Slider(
            value = progress,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = trackColor,
                activeTrackColor = trackColor,
                inactiveTrackColor = if (isDarkTheme) Color.Gray.copy(alpha = 0.3f) else trackColor.copy(alpha = 0.3f)
            ),
            enabled = false
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currentTime,
                style = MaterialTheme.typography.bodySmall,
                color = textColor,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = totalTime,
                style = MaterialTheme.typography.bodySmall,
                color = textColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
@Composable
fun SpotifyVolumeControl(
    volume: Float,
    onVolumeChange: (Float) -> Unit,
    isDarkTheme: Boolean,
    onSurfaceColor: Color
) {
    val trackColor = if (isDarkTheme) Color.White else PrimaryGreen
    val iconColor = if (isDarkTheme) Color.Gray else onSurfaceColor.copy(alpha = 0.7f)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.VolumeDown,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
        Slider(
            value = volume,
            onValueChange = onVolumeChange,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = trackColor,
                activeTrackColor = trackColor,
                inactiveTrackColor = if (isDarkTheme) Color.Gray.copy(alpha = 0.3f) else trackColor.copy(alpha = 0.3f)
            )
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
    }
}
@Composable
fun SpotifyDescriptionCard(
    description: String,
    isDarkTheme: Boolean,
    surfaceColor: Color,
    onSurfaceColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = ShadowColor,
                ambientColor = ShadowColorLight
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) Color(0xFF1A1A1A) else Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Açıklama",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color.White else PrimaryGreen,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isDarkTheme) Color.Gray else onSurfaceColor.copy(alpha = 0.8f),
                lineHeight = 26.sp
            )
        }
    }
}
fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}
