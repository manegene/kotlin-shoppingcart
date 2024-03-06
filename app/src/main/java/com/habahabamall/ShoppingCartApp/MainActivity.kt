package com.habahabamall.ShoppingCartApp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.habahabamall.ShoppingCartApp.constants.NavApp
import com.habahabamall.ShoppingCartApp.constants.handleIntent
import com.habahabamall.ShoppingCartApp.services.products.ProductsService
import com.habahabamall.ShoppingCartApp.services.users.UsersService
import com.habahabamall.ShoppingCartApp.ui.theme.HabahabamallTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics

        setContent {
            HabahabamallTheme {

                // A surface container using the default app color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    color = MaterialTheme.colorScheme.background

                ) {
                    NavApp(
                        productsService = ProductsService(
                            MainApplication(),
                            applicationContext
                        ),
                        context = applicationContext,
                        application = MainApplication(),
                        usersService = UsersService(MainApplication(), applicationContext)
                    )

                }
            }
        }

        handleIntent(intent = Intent(), context = applicationContext, navController = NavController(applicationContext))
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent = Intent(), context = applicationContext, navController = NavController(applicationContext))
    }


}
