package com.habahabamall.ShoppingCartApp.userShared

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.habahabamall.ShoppingCartApp.services.users.UsersService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun UserProfile(usersService: UsersService) {
    var user by remember {
        mutableStateOf(String())
    }

    Column {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                user = usersService.getLoggedUser().email
            }
        }

        Text(text = "Logged in user: $user")

    }
}