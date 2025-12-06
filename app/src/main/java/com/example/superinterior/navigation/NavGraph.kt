package com.example.superinterior.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.superinterior.ui.screens.addphoto.AddPhotoScreen
import com.example.superinterior.ui.screens.chooseroom.ChooseRoomScreen
import com.example.superinterior.ui.screens.designresult.DesignResultScreen
import com.example.superinterior.ui.screens.home.HomeScreen
import com.example.superinterior.ui.screens.listdesigns.ListDesignsScreen
import com.example.superinterior.ui.screens.selectstyle.SelectStyleScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddPhoto : Screen("add_photo/{designType}") {
        fun createRoute(designType: String) = "add_photo/$designType"
    }
    object ChooseRoom : Screen("choose_room")
    object SelectStyle : Screen("select_style")
    object DesignResult : Screen("design_result")
    object ListDesigns : Screen("list_designs")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAddPhoto = { designType ->
                    navController.navigate(Screen.AddPhoto.createRoute(designType))
                },
                onNavigateToListDesigns = {
                    navController.navigate(Screen.ListDesigns.route)
                }
            )
        }

        composable(Screen.ListDesigns.route) {
            ListDesignsScreen(
                onNavigateToHome = {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                }
            )
        }

        composable(
            route = Screen.AddPhoto.route,
            arguments = listOf(
                navArgument("designType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val designType = backStackEntry.arguments?.getString("designType") ?: "Interior Redesign"
            AddPhotoScreen(
                designType = designType,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContinue = {
                    navController.navigate(Screen.ChooseRoom.route)
                }
            )
        }

        composable(Screen.ChooseRoom.route) {
            ChooseRoomScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContinue = {
                    navController.navigate(Screen.SelectStyle.route)
                }
            )
        }

        composable(Screen.SelectStyle.route) {
            SelectStyleScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContinue = {
                    navController.navigate(Screen.DesignResult.route)
                }
            )
        }

        composable(Screen.DesignResult.route) {
            DesignResultScreen(
                onNavigateBack = {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                }
            )
        }
    }
}
