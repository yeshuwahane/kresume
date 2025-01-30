package com.cyphernerd.resume.presentation.review

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyphernerd.resume.data.util.DataResource
import kotlinx.coroutines.launch

@Composable
fun UploadResumeScreen(
    viewModel: ResumeViewModel,
    context: Context,
    onReview: () -> Unit
) {
    // Collect both states
    val resumeTextState = viewModel.resumeTextState.collectAsStateWithLifecycle()
    val atsScoreState = viewModel.atsScoreState.collectAsStateWithLifecycle()

    // State for job position input
    var jobPosition by remember { mutableStateOf("") }

    // SnackbarHostState to manage Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // SnackbarHost to display Snackbar
        SnackbarHost(hostState = snackbarHostState)

        // TextField for job position
        OutlinedTextField(
            value = jobPosition,
            onValueChange = { jobPosition = it },
            label = { Text("Enter Job Position") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Handle resume text upload state
        when {
            resumeTextState.value.isLoading() -> {
                CircularProgressIndicator()
            }

            resumeTextState.value.isError() -> {
                Text(
                    text = "Error: ${resumeTextState.value.error?.message ?: "Unknown error"}",
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            resumeTextState.value.isSuccess() -> {
                Text("Resume uploaded successfully!")
                Spacer(modifier = Modifier.height(16.dp))

                // Handle ATS score state
                when {
                    atsScoreState.value.isLoading() -> {
                        CircularProgressIndicator()
                    }

                    atsScoreState.value.isError() -> {
                        Text(
                            text = "Error calculating ATS score: ${atsScoreState.value.error?.message ?: "Unknown error"}",
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    atsScoreState.value.isSuccess() -> {
                        // Display ATS score
                        Text(
                            text = "ATS Score: ${atsScoreState.value.data}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        /*Button(onClick = onReview) {
                            Text("Review Resume")
                        }*/
                    }
                }
            }

            else -> {
                Text("Please upload a PDF to start.")
            }
        }

        // PDF picker launcher
        val pdfPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                viewModel.extractTextFromPdf(context, it,jobPosition)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (jobPosition.isNotBlank()) {
                    pdfPickerLauncher.launch("application/pdf")
                } else {
                    // Show Snackbar if job position is empty
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Please enter a job position.")
                    }
                }
            }
        ) {
            Text("Upload PDF")
        }
    }
}
