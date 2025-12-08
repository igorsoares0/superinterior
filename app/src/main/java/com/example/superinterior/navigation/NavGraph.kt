package com.example.superinterior.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.superinterior.data.local.AppDatabase
import com.example.superinterior.data.network.ApiService
import com.example.superinterior.data.repository.DesignRepository
import com.example.superinterior.ui.screens.addphoto.AddPhotoScreen
import com.example.superinterior.ui.screens.addreferencephoto.AddReferencePhotoScreen
import com.example.superinterior.ui.screens.addroomphoto.AddRoomPhotoScreen
import com.example.superinterior.ui.screens.chooseroom.ChooseRoomScreen
import com.example.superinterior.ui.screens.designresult.DesignResultScreen
import com.example.superinterior.ui.screens.home.HomeScreen
import com.example.superinterior.ui.screens.listdesigns.ListDesignsScreen
import com.example.superinterior.ui.screens.selectstyle.SelectStyleScreen
import com.example.superinterior.ui.viewmodel.ApiState
import com.example.superinterior.ui.viewmodel.DesignFlowViewModel
import com.example.superinterior.ui.viewmodel.DesignFlowViewModelFactory
import com.example.superinterior.ui.viewmodel.DesignResultViewModel
import com.example.superinterior.ui.viewmodel.ListDesignsViewModel
import com.example.superinterior.ui.viewmodel.ListDesignsViewModelFactory

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddPhoto : Screen("add_photo/{designType}") {
        fun createRoute(designType: String) = "add_photo/$designType"
    }
    object AddRoomPhoto : Screen("add_room_photo")
    object AddReferencePhoto : Screen("add_reference_photo")
    object ChooseRoom : Screen("choose_room")
    object SelectStyle : Screen("select_style/{isGardenDesign}") {
        fun createRoute(isGardenDesign: Boolean) = "select_style/$isGardenDesign"
    }
    object DesignResult : Screen("design_result")
    object ListDesigns : Screen("list_designs")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    appDatabase: AppDatabase,
    startDestination: String = Screen.Home.route
) {
    // Create repository
    val repository = remember {
        DesignRepository(
            apiService = ApiService(),
            designDao = appDatabase.designDao()
        )
    }

    // Shared ViewModel for design flow
    val designFlowViewModel: DesignFlowViewModel = viewModel(
        factory = DesignFlowViewModelFactory(repository)
    )
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAddPhoto = { designType ->
                    if (designType == "Reference Style") {
                        navController.navigate(Screen.AddRoomPhoto.route)
                    } else {
                        navController.navigate(Screen.AddPhoto.createRoute(designType))
                    }
                },
                onNavigateToListDesigns = {
                    navController.navigate(Screen.ListDesigns.route)
                }
            )
        }

        composable(Screen.ListDesigns.route) {
            val listViewModel: ListDesignsViewModel = viewModel(
                factory = ListDesignsViewModelFactory(repository)
            )

            ListDesignsScreen(
                viewModel = listViewModel,
                onNavigateToHome = {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                },
                onNavigateToAddPhoto = { designType ->
                    if (designType == "Reference Style") {
                        navController.navigate(Screen.AddRoomPhoto.route)
                    } else {
                        navController.navigate(Screen.AddPhoto.createRoute(designType))
                    }
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

            // Set design type when entering this screen
            LaunchedEffect(designType) {
                designFlowViewModel.setDesignType(designType)
            }

            AddPhotoScreen(
                designType = designType,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContinue = { file, path ->
                    designFlowViewModel.setImageFile(file, path)
                    if (designType == "Garden Design") {
                        navController.navigate(Screen.SelectStyle.createRoute(isGardenDesign = true))
                    } else {
                        navController.navigate(Screen.ChooseRoom.route)
                    }
                }
            )
        }

        composable(Screen.ChooseRoom.route) {
            ChooseRoomScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContinue = { roomType ->
                    designFlowViewModel.setRoomType(roomType)
                    navController.navigate(Screen.SelectStyle.createRoute(isGardenDesign = false))
                }
            )
        }

        composable(
            route = Screen.SelectStyle.route,
            arguments = listOf(
                navArgument("isGardenDesign") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val isGardenDesign = backStackEntry.arguments?.getBoolean("isGardenDesign") ?: false
            SelectStyleScreen(
                isGardenDesign = isGardenDesign,
                designFlowViewModel = designFlowViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContinue = { style ->
                    designFlowViewModel.setStyle(style)
                    designFlowViewModel.generateDesign()
                    navController.navigate(Screen.DesignResult.route)
                }
            )
        }

        composable(Screen.DesignResult.route) {
            val flowState by designFlowViewModel.flowState.collectAsState()
            val resultViewModel: DesignResultViewModel = viewModel()

            LaunchedEffect(flowState.generationState) {
                when (val state = flowState.generationState) {
                    is ApiState.Success -> {
                        resultViewModel.setGeneratedImage(
                            imageUrl = state.data.outputUrl,
                            processingTime = state.data.processingTime
                        )
                    }
                    else -> {}
                }
            }

            DesignResultScreen(
                viewModel = resultViewModel,
                onNavigateBack = {
                    designFlowViewModel.reset()
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                }
            )
        }

        composable(Screen.AddRoomPhoto.route) {
            AddRoomPhotoScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContinue = {
                    navController.navigate(Screen.AddReferencePhoto.route)
                }
            )
        }

        composable(Screen.AddReferencePhoto.route) {
            AddReferencePhotoScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContinue = {
                    navController.navigate(Screen.DesignResult.route)
                }
            )
        }
    }
}
