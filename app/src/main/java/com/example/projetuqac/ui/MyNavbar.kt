package com.example.projetuqac.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projetuqac.Greeting2
import com.example.projetuqac.R
import com.example.projetuqac.db.ApiViewModel
import com.example.projetuqac.ui.accountscreen.AccountActivity
import com.example.projetuqac.ui.historyscreen.HistoryScreen
import com.example.projetuqac.ui.mainscreen.MainActivity
import com.example.projetuqac.ui.mainscreen.MainScreen

sealed class Screen(@StringRes val resourceIdText : Int, @StringRes val resourceIdRoute: Int, val imageId: Int) {
    object Home : Screen(R.string.navbar_item_home, R.string.navbar_item_home_route, androidx.core.R.drawable.ic_call_decline)
    object History : Screen(R.string.navbar_item_history, R.string.navbar_item_history_route, androidx.core.R.drawable.ic_call_answer_video)
    object Login : Screen(R.string.navbar_item_login, R.string.navbar_item_login_route, androidx.core.R.drawable.ic_call_answer)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNavbar(modifier: Modifier = Modifier) {

    val bottomNavItems = listOf(
        Screen.Home,
        Screen.History,
        Screen.Login
    )
    

    val navController = rememberNavController()

    Scaffold(modifier = modifier,
        bottomBar = {
            BottomNavigation {

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { navItem ->
                    val currentRoute = stringResource(id = navItem.resourceIdRoute)
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = navItem.imageId),
                                contentDescription = null
                            )
                        },
                        label = { Text(text = stringResource(id = navItem.resourceIdText)) },
                        selected = currentDestination?.hierarchy?.any {
                            it.route == stringResource(
                                id = navItem.resourceIdRoute
                            )
                        } == true,
                        onClick = {
                            navController.navigate(currentRoute) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        })
            { innerPadding ->
                val resourceIdRouteHome = stringResource(id = Screen.Home.resourceIdRoute)
                val resourceIdRouteHistory = stringResource(id = Screen.History.resourceIdRoute)
                val resourceIdRouteLogin = stringResource(id = Screen.Login.resourceIdRoute)

            NavHost(
                navController = navController,
                startDestination = resourceIdRouteHome,
                modifier = modifier.padding(innerPadding))
                {
                    composable(resourceIdRouteHome) {
                        val viewModel : ApiViewModel = hiltViewModel()
                        MainScreen(modifier = Modifier.padding(innerPadding), uiState = viewModel.uiState.value)
                    }
                    composable(resourceIdRouteHistory) {
                        HistoryScreen(name = "2", modifier = Modifier.padding(innerPadding))
                    }
                    composable(resourceIdRouteLogin) {
                        AccountActivity(name = "3", modifier = Modifier.padding(innerPadding))
                    }
                }

    }

}