package com.habahabamall.ShoppingCartApp.userShared

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.habahabamall.ShoppingCartApp.R
import com.habahabamall.ShoppingCartApp.constants.NavPages
import com.habahabamall.ShoppingCartApp.constants.ProgressIndicator
import com.habahabamall.ShoppingCartApp.constants.redirectForgotPassword
import com.habahabamall.ShoppingCartApp.constants.redirectRegister
import com.habahabamall.ShoppingCartApp.models.users.UsersModel
import com.habahabamall.ShoppingCartApp.models.users.UsersResponse
import com.habahabamall.ShoppingCartApp.persist.DAOAppDatabase
import com.habahabamall.ShoppingCartApp.persist.models.PersistUser
import com.habahabamall.ShoppingCartApp.services.users.UsersService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Login(
    modifier: Modifier,
    usersService: UsersService, context: Context, navController: NavController
) {
    var user by remember { mutableStateOf(UsersModel()) }
    var isLoading by remember {
        mutableStateOf(false)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {}

    val dbService = DAOAppDatabase.getDatabase(context)
    var received by remember {
        mutableStateOf(UsersResponse())
    }

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        //.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    )
    {

        TextField(value = email,
            onValueChange = { email = it },
            singleLine = true,
            trailingIcon = { Icon(Icons.Rounded.Email, contentDescription = "email") },
            label = { Text(text = "login with email") }
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            trailingIcon = { Icon(Icons.Rounded.Lock, contentDescription = "password") },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text(text = "enter password") }

        )
        Button(
            onClick = {
                isLoading = true
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        user = UsersModel(emailId = email, password = password)
                        received = usersService.loginUserService(user)

                        if (received.EmailId.isNotEmpty()) {
                            //store received user in local db
                            dbService.getUser().addNew(
                                PersistUser(
                                    id = received.Id,
                                    email = received.EmailId,
                                    firstName = received.Firstname?:"first",
                                    lastName = received.lastName?:"last",
                                    addressId = received.AddressId?:0
                                )
                            )

                        }
                        isLoading = false
                    }
                    if (received.EmailId.isNotEmpty())
                        navController.navigate(NavPages.Home.name) {
                            popUpTo(navController.graph.id) {
                                inclusive = false
                            }
                        }
                }

            },
            shape = CircleShape

        ) {
            Text(text = "login")
        }
        //show progressbar
        if (isLoading)
            ProgressIndicator(context)

        Row(
            modifier = modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    redirectForgotPassword(
                        "http://${R.string.web_host}/passwordrecovery",
                        context,
                        launcher
                    )
                },
                shape = CircleShape

            )
            {
                Text(text = "reset password")
            }
            Button(
                onClick = {
                    redirectRegister("https://${R.string.web_host}/register", context, launcher)
                },
                shape = CircleShape
            )

            {
                Text(text = "sign up")
            }
        }
    }


}