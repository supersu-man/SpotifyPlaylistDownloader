package dev.sumanth.spd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.sumanth.spd.model.NavigationItem
import dev.sumanth.spd.model.Routes
import dev.sumanth.spd.ui.component.Background
import dev.sumanth.spd.ui.component.BottomBar
import dev.sumanth.spd.ui.component.PermissionDialog
import dev.sumanth.spd.ui.component.TopBar
import dev.sumanth.spd.ui.component.UpdateDialog
import dev.sumanth.spd.ui.screen.HomeScreen
import dev.sumanth.spd.ui.screen.PreferencesScreen
import dev.sumanth.spd.ui.viewmodel.HomeScreenViewModel
import dev.sumanth.spd.ui.viewmodel.UpdaterViewModel
import dev.sumanth.spd.utils.Downloader
import dev.sumanth.spd.utils.Spotify
import dev.sumanth.spd.utils.spotify
import org.schabi.newpipe.extractor.NewPipe


class MainActivity : ComponentActivity() {

    private val url = "https://github.com/supersu-man/spotify-playlist-downloader/releases/latest"

    private val navigationItems = listOf(
        NavigationItem("Home", Icons.Filled.Home, Icons.Outlined.Home, Routes.Home.name),
        NavigationItem("Preferences", Icons.Filled.Settings, Icons.Outlined.Settings, Routes.Preferences.name)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        spotify = Spotify(this)

        val updateViewModel = UpdaterViewModel(this@MainActivity, url)
        NewPipe.init(Downloader.getInstance())

        setContent {
            val navController = rememberNavController()
            var title by remember { mutableStateOf("Home") }
            val homeViewModel = HomeScreenViewModel()

            Background {
                Scaffold(topBar = { TopBar(title) }, bottomBar = { BottomBar(navigationItems, navController) }) { innerPadding ->
                    NavHost(navController = navController, startDestination = Routes.Home.name, modifier = Modifier.padding(innerPadding)) {
                        composable(Routes.Home.name) {
                            title = "Home"
                            HomeScreen(homeViewModel)
                        }
                        composable(Routes.Preferences.name) {
                            title = "Preferences"
                            PreferencesScreen()
                        }
                    }
                    PermissionDialog(this)
                    UpdateDialog(updateViewModel)
                }
            }
        }
    }
}