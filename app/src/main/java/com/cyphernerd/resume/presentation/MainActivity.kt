package com.cyphernerd.resume.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.cyphernerd.resume.presentation.baking.BakingScreen
import com.cyphernerd.resume.presentation.navigation.AppNavigation
import com.cyphernerd.resume.presentation.ui.theme.ResumeTheme
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PDFBoxResourceLoader.init(applicationContext)
        setContent {
            ResumeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppNavigation()
//                    BakingScreen()
                }
            }
        }
    }
}