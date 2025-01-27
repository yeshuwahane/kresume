package com.cyphernerd.resume.presentation.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun ReviewScreen() {
    val viewModel = hiltViewModel<ResumeViewModel>()
    val uiState = viewModel.atsScoreState.collectAsStateWithLifecycle()
    val atsScoreState = uiState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            atsScoreState.isLoading() -> {
                CircularProgressIndicator()
            }

            atsScoreState.isError() -> {
                Text(
                    text = "Error: ${atsScoreState.error?.message ?: "Unknown error"}",
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            atsScoreState.isSuccess() -> {
                Text(
                    text = "Your ATS Score: ${atsScoreState.data}",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            else -> {
                Text("No ATS Score available. Please upload a resume.")
            }
        }
    }
}
