package com.example.superinterior

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.superinterior.data.local.AppDatabase
import com.example.superinterior.navigation.NavGraph
import com.example.superinterior.ui.theme.SuperinteriorTheme

class MainActivity : ComponentActivity() {

    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        appDatabase = AppDatabase.getDatabase(applicationContext)

        setContent {
            SuperinteriorTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    appDatabase = appDatabase
                )
            }
        }
    }
}