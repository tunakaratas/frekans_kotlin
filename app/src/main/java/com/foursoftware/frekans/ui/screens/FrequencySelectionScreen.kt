package com.foursoftware.frekans.ui.screens
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foursoftware.frekans.data.PlantRepository
import com.foursoftware.frekans.ui.components.AnimatedPlantIcon
import com.foursoftware.frekans.ui.theme.*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrequencySelectionScreen(
    plantId: Int,
    onBackClick: () -> Unit,
    onFrequencyClick: (Double) -> Unit
) {
    val plant = PlantRepository.getPlantById(plantId) ?: return
    val frequencies = if (plant.frequencies.isNotEmpty()) {
        plant.frequencies.take(5)
    } else {
        listOf(
            plant.frequency,
            plant.frequency * 1.2,
            plant.frequency * 0.8,
            plant.frequency * 1.5,
            plant.frequency * 0.6
        )
    }
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
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(32.dp))
                                .shadow(
                                    elevation = 16.dp,
                                    shape = RoundedCornerShape(32.dp),
                                    spotColor = ShadowColor,
                                    ambientColor = ShadowColorLight
                                )
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            PrimaryGreenLight.copy(alpha = 0.15f),
                                            SecondaryGreen.copy(alpha = 0.12f),
                                            TertiaryGreen.copy(alpha = 0.1f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimatedPlantIcon(
                                icon = plant.icon,
                                isPlaying = false,
                                modifier = Modifier.size(72.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Frekans Dosyaları",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkTheme) Color.White else onSurfaceColor,
                            textAlign = TextAlign.Center,
                            fontSize = 28.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "${frequencies.size} frekans dosyası mevcut",
                            style = MaterialTheme.typography.bodyMedium,
                            color = PrimaryGreen.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                frequencies.forEachIndexed { index, frequency ->
                    item {
                        FrequencyFileCard(
                            frequency = frequency,
                            fileNumber = index + 1,
                            onClick = { onFrequencyClick(frequency) },
                            isDarkTheme = isDarkTheme,
                            onSurfaceColor = onSurfaceColor
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun FrequencyFileCard(
    frequency: Double,
    fileNumber: Int,
    onClick: () -> Unit,
    isDarkTheme: Boolean,
    onSurfaceColor: Color
) {
    var isPressed by remember { mutableStateOf(false) }
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 10.dp,
        animationSpec = spring(Spring.DampingRatioMediumBouncy),
        label = "elevation"
    )
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy),
        label = "scale"
    )
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(24.dp),
                spotColor = ShadowColor,
                ambientColor = ShadowColorLight
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) Color(0xFF2A2A2A) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(18.dp),
                            spotColor = ShadowColor,
                            ambientColor = ShadowColorLight
                        )
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    PrimaryGreen.copy(alpha = 0.15f),
                                    PrimaryGreenLight.copy(alpha = 0.12f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Column {
                    Text(
                        text = "Frekans Dosyası #$fileNumber",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else onSurfaceColor,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${frequency.toInt()} Hz",
                        style = MaterialTheme.typography.bodyLarge,
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = when {
                            frequency < 400 -> "Düşük Frekans"
                            frequency < 600 -> "Orta Frekans"
                            frequency < 800 -> "Yüksek Frekans"
                            else -> "Çok Yüksek Frekans"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isDarkTheme) Color.Gray else onSurfaceColor.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PrimaryGreen.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
            Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Çal",
                tint = ForestGreen40,
                    modifier = Modifier.size(28.dp)
            )
            }
        }
    }
}
