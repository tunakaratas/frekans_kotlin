package com.foursoftware.frekans.ui.screens
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.*
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foursoftware.frekans.data.Plant
import com.foursoftware.frekans.data.PlantCategory
import com.foursoftware.frekans.data.PlantRepository
import com.foursoftware.frekans.ui.components.AnimatedPlantIcon
import com.foursoftware.frekans.ui.components.LoadingPlantList
import com.foursoftware.frekans.ui.theme.*
import com.foursoftware.frekans.viewmodel.FavoritesViewModel
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onPlantClick: (Int) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAbout: () -> Unit,
    favoritesViewModel: FavoritesViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<PlantCategory?>(null) }
    val favorites by favoritesViewModel.favorites.collectAsState()
    val isDrawerOpen by remember {
        derivedStateOf { drawerState.currentValue == DrawerValue.Open }
    }
    val filteredPlants = remember {
        derivedStateOf {
            val searchResults = PlantRepository.searchPlants(searchQuery)
            if (selectedCategory == null) {
                searchResults
            } else {
                searchResults.filter { it.category == selectedCategory }
            }
        }
    }.value
    val openDrawer: () -> Unit = {
        if (!isDrawerOpen) {
            scope.launch {
                try {
                    if (drawerState.currentValue == DrawerValue.Closed) {
                        drawerState.open()
                    }
                } catch (e: Exception) {
                }
            }
        }
    }
    val closeDrawer: () -> Unit = {
        scope.launch {
            try {
                if (drawerState.currentValue == DrawerValue.Open) {
                    drawerState.close()
                }
            } catch (e: Exception) {
            }
        }
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                onNavigateToSettings = {
                    closeDrawer()
                    onNavigateToSettings()
                },
                onNavigateToAbout = {
                    closeDrawer()
                    onNavigateToAbout()
                },
                onCloseDrawer = {
                    closeDrawer()
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            "Bitki Frekans Terapisi",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        ) 
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = openDrawer,
                            modifier = Modifier.size(48.dp),
                            enabled = !isDrawerOpen
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menü",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = PrimaryGreen,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                GradientLightStart,
                                GradientLightEnd,
                                Color(0xFFF5FDF5)
                            )
                        )
                    )
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
            item {
                ModernSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it }
                )
            }
            item {
                ModernCategoryFilterChips(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { category ->
                        selectedCategory = if (selectedCategory == category) null else category
                    }
                )
            }
            item {
                Text(
                    text = "${filteredPlants.size} bitki bulundu",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PrimaryGreen.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
                )
            }
            if (filteredPlants.isEmpty()) {
                item {
                    EmptyStateView(
                        hasSearch = searchQuery.isNotEmpty(),
                        hasFilter = selectedCategory != null
                    )
                }
            } else {
                items(
                    items = filteredPlants,
                    key = { it.id }
                ) { plant ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        ) + slideInVertically(
                            initialOffsetY = { 30 },
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        ),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        ModernPlantCard(
                            plant = plant,
                            isFavorite = favorites.contains(plant.id),
                            onFavoriteClick = { favoritesViewModel.toggleFavorite(plant.id) },
                            onClick = { onPlantClick(plant.id) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
                }
            }
        }
    }
}
@Composable
fun NavigationDrawerContent(
    onNavigateToSettings: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PrimaryGreen,
                        PrimaryGreenLight,
                        SecondaryGreen.copy(alpha = 0.9f)
                    )
                )
            )
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
            Text(
                text = "F",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Frekans",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Bitki Terapisi",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
        HorizontalDivider(
            color = Color.White.copy(alpha = 0.3f),
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        NavigationDrawerItem(
            icon = { 
                Icon(
                    Icons.Default.Home, 
                    contentDescription = null, 
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                ) 
            },
            label = { 
                Text(
                    "Bitkiler", 
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                ) 
            },
            selected = true,
            onClick = onCloseDrawer,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(16.dp)
        )
        NavigationDrawerItem(
            icon = { 
                Icon(
                    Icons.Default.Settings, 
                    contentDescription = null, 
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                ) 
            },
            label = { 
                Text(
                    "Ayarlar", 
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                ) 
            },
            selected = false,
            onClick = onNavigateToSettings,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(16.dp)
        )
        NavigationDrawerItem(
            icon = { 
                Icon(
                    Icons.Default.Info, 
                    contentDescription = null, 
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                ) 
            },
            label = { 
                Text(
                    "Hakkında", 
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                ) 
            },
            selected = false,
            onClick = onNavigateToAbout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = Color.White.copy(alpha = 0.15f)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Versiyon 1.0.0",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "© 2024 Four Software",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
@Composable
fun ModernSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = ShadowColor,
                ambientColor = ShadowColorLight
            ),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        tonalElevation = 0.dp
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { 
                Text(
                    "Bitki ara...",
                    color = Color.Gray.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodyLarge
                ) 
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Ara",
                    tint = PrimaryGreen,
                    modifier = Modifier.size(24.dp)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = PrimaryGreen.copy(alpha = 0.8f),
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.2f),
                focusedTextColor = PrimaryGreen,
                unfocusedTextColor = Color.Black
            ),
            textStyle = MaterialTheme.typography.bodyLarge
        )
    }
}
@Composable
fun ModernCategoryFilterChips(
    selectedCategory: PlantCategory?,
    onCategorySelected: (PlantCategory?) -> Unit
) {
    val categories = listOf(
        null to "Tümü",
        PlantCategory.HERBS to "Otlar",
        PlantCategory.VEGETABLES to "Sebzeler",
        PlantCategory.FRUITS to "Meyveler",
        PlantCategory.FLOWERS to "Çiçekler"
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(categories.size) { index ->
            val (category, label) = categories[index]
            val isSelected = selectedCategory == category
            AnimatedContent(
                targetState = isSelected,
                transitionSpec = {
                    scaleIn(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ) + fadeIn() togetherWith scaleOut() + fadeOut()
                },
                label = "chip"
            ) {
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelected(category) },
                    enabled = true,
                    label = { 
                        Text(
                            label,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            style = MaterialTheme.typography.bodyMedium
                        ) 
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryGreen,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = PrimaryGreen.copy(alpha = 0.8f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }
    }
}
@Composable
fun ModernPlantCard(
    plant: Plant,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 12.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "elevation"
    )
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )
    val alphaValue by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(150),
        label = "alpha"
    )
    Card(
        onClick = onClick,
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = alphaValue
            }
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(28.dp),
                spotColor = ShadowColor,
                ambientColor = ShadowColorLight
            ),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp),
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
                    modifier = Modifier.size(52.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = plant.turkishName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "${plant.frequency} Hz",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PrimaryGreenLight,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PrimaryGreen.copy(alpha = 0.5f)
                    )
                    Text(
                        text = getCategoryName(plant.category),
                        style = MaterialTheme.typography.labelMedium,
                        color = PrimaryGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "Favorilerden çıkar" else "Favorilere ekle",
                        tint = if (isFavorite) Color(0xFFFF6B9D) else Color.Gray.copy(alpha = 0.5f),
                        modifier = Modifier.size(28.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = PrimaryGreenLight,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
fun getCategoryName(category: PlantCategory): String {
    return when (category) {
        PlantCategory.HERBS -> "Ot"
        PlantCategory.VEGETABLES -> "Sebze"
        PlantCategory.FRUITS -> "Meyve"
        PlantCategory.FLOWERS -> "Çiçek"
    }
}
@Composable
fun EmptyStateView(
    hasSearch: Boolean,
    hasFilter: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "empty_state")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    val rotation by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )
    val alphaValue by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PrimaryGreen.copy(alpha = 0.2f),
                            PrimaryGreenLight.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    rotationZ = rotation
                    alpha = alphaValue
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = PrimaryGreen.copy(alpha = 0.6f)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = when {
                hasSearch && hasFilter -> "Aradığınız kriterlere uygun bitki bulunamadı"
                hasSearch -> "Aradığınız bitki bulunamadı"
                hasFilter -> "Bu kategoride bitki bulunamadı"
                else -> "Henüz bitki eklenmemiş"
            },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen,
            textAlign = TextAlign.Center,
            fontSize = 22.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = when {
                hasSearch || hasFilter -> "Farklı bir arama terimi veya kategori deneyin"
                else -> "Yakında daha fazla bitki eklenecek"
            },
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}
