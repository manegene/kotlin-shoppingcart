package com.habahabamall.ShoppingCartApp.constants

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.navigation.NavController
import com.habahabamall.ShoppingCartApp.R
import com.habahabamall.ShoppingCartApp.persist.DAOAppDatabase
import com.habahabamall.ShoppingCartApp.services.users.UsersService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BottomNav(navController: NavController, usersService: UsersService) {
    val context = LocalContext.current
    var userIsLogged by remember { mutableStateOf(false) }
    DAOAppDatabase.getDatabase(context)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {}

    BottomAppBar(
        containerColor = Color.LightGray,
        actions =
        {
            //check user on app start
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    userIsLogged = usersService.checkUser()
                }
            }

            //contact us button
            ExtendedFloatingActionButton(
                onClick = {
                    launchEmailIntent(getString(context,R.string.email_address), context, launcher)
                },
                modifier = Modifier.padding(horizontal = 5.dp),
                icon = { Icon(Icons.Rounded.Email, contentDescription = "send us email") },
                text = { Text(text = stringResource(id = R.string.send_email)) }
            )

            //login button
            FloatingActionButton(
                onClick = {
                    //if user is logged in, then show the profile page,
                    // otherwise login page
                    if (userIsLogged) {
                        navController.navigate(NavPages.Profile.name) { launchSingleTop = true }
                    }
                    else {
                        navController.navigate(NavPages.Login.name) { launchSingleTop = true }
                    }

                },
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Icon(Icons.Rounded.AccountCircle, contentDescription = "manage account")
            }

        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                globalShare(context.resources.getString(R.string.app_link),context,launcher) })
            {
                Icon(Icons.Rounded.Share, contentDescription = "share app")
            }
        }

    )
}