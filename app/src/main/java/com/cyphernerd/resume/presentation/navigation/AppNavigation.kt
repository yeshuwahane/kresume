package com.cyphernerd.resume.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cyphernerd.resume.presentation.intro.GetStartedScreen
import com.cyphernerd.resume.presentation.review.ResumeViewModel
import com.cyphernerd.resume.presentation.review.ReviewScreen
import com.cyphernerd.resume.presentation.review.UploadResumeScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: ResumeViewModel = hiltViewModel()

    NavHost(navController, startDestination = "get_started") {
        composable("get_started") {
            GetStartedScreen(onContinue = { navController.navigate("upload_resume") })
        }
        composable("upload_resume") {
            UploadResumeScreen(viewModel, LocalContext.current) {
                navController.navigate("review_resume")
            }
        }
        composable("review_resume") {
            ReviewScreen()
        }
    }
}
