package com.foursoftware.frekans.navigation
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.foursoftware.frekans.ui.screens.AboutScreen
import com.foursoftware.frekans.ui.screens.FrequencySelectionScreen
import com.foursoftware.frekans.ui.screens.HomeScreen
import com.foursoftware.frekans.ui.screens.MediaPlayerScreen
import com.foursoftware.frekans.ui.screens.SettingsScreen
import com.foursoftware.frekans.viewmodel.FavoritesViewModel
import com.foursoftware.frekans.viewmodel.FavoritesViewModelFactory
import com.foursoftware.frekans.viewmodel.PlantDetailViewModel
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object FrequencySelection : Screen("frequency_selection/{plantId}") {
        fun createRoute(plantId: Int) = "frequency_selection/$plantId"
    }
    object MediaPlayer : Screen("media_player/{plantId}/{frequency}") {
        fun createRoute(plantId: Int, frequency: Double) = "media_player/$plantId/$frequency"
    }
    object Settings : Screen("settings")
    object About : Screen("about")
}
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            val context = LocalContext.current
            val favoritesViewModel: FavoritesViewModel = viewModel(
                factory = FavoritesViewModelFactory(context)
            )
            HomeScreen(
                onPlantClick = { plantId ->
                    navController.navigate(Screen.FrequencySelection.createRoute(plantId))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToAbout = {
                    navController.navigate(Screen.About.route)
                },
                favoritesViewModel = favoritesViewModel
            )
        }
        composable(route = Screen.FrequencySelection.route) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getString("plantId")?.toIntOrNull() ?: return@composable
            FrequencySelectionScreen(
                plantId = plantId,
                onBackClick = { navController.popBackStack() },
                onFrequencyClick = { frequency ->
                    navController.navigate(Screen.MediaPlayer.createRoute(plantId, frequency))
                }
            )
        }
        composable(route = Screen.MediaPlayer.route) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getString("plantId")?.toIntOrNull() ?: return@composable
            val frequency = backStackEntry.arguments?.getString("frequency")?.toDoubleOrNull() ?: return@composable
            val context = LocalContext.current
            val viewModel: com.foursoftware.frekans.viewmodel.PlantDetailViewModel = viewModel(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return PlantDetailViewModel(context) as T
                    }
                }
            )
            MediaPlayerScreen(
                plantId = plantId,
                frequency = frequency,
                onBackClick = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(route = Screen.About.route) {
            AboutScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
