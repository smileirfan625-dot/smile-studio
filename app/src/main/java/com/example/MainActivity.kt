package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.StudioBottomNavBar
import com.example.ui.components.StudioTopBar
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.BookingLookupScreen
import com.example.ui.screens.BookingScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PackagesScreen
import com.example.ui.screens.PortfolioScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.StudioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: StudioViewModel = viewModel()
                SmileStudioApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SmileStudioApp(viewModel: StudioViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val packages by viewModel.activePackages.collectAsState()
    val portfolio by viewModel.publishedPortfolio.collectAsState()
    val enquiries by viewModel.allEnquiries.collectAsState()
    val unreadCount = enquiries.count { !it.isRead }

    // Intercept back button to navigate backwards in the history stack
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.handleBack()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            StudioTopBar(
                currentScreen = currentScreen,
                unreadEnquiriesCount = unreadCount,
                onNavigate = { screen -> viewModel.navigateTo(screen) }
            )
        },
        bottomBar = {
            StudioBottomNavBar(
                currentScreen = currentScreen,
                onNavigate = { screen -> viewModel.navigateTo(screen) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBackground)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> HomeScreen(
                    viewModel = viewModel,
                    packages = packages,
                    portfolio = portfolio
                )
                AppScreen.SERVICES -> ServicesScreen(
                    viewModel = viewModel
                )
                AppScreen.PORTFOLIO -> PortfolioScreen(
                    viewModel = viewModel,
                    portfolioList = portfolio
                )
                AppScreen.PACKAGES -> PackagesScreen(
                    viewModel = viewModel,
                    packages = packages
                )
                AppScreen.BOOKING -> BookingScreen(
                    viewModel = viewModel,
                    packages = packages
                )
                AppScreen.LOOKUP -> BookingLookupScreen(
                    viewModel = viewModel
                )
                AppScreen.CONTACT -> ContactScreen(
                    viewModel = viewModel
                )
                AppScreen.ADMIN -> AdminDashboardScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}
