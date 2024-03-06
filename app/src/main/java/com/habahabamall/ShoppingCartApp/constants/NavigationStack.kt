package com.habahabamall.ShoppingCartApp.constants

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.habahabamall.ShoppingCartApp.MainApplication
import com.habahabamall.ShoppingCartApp.R
import com.habahabamall.ShoppingCartApp.models.products.ProductsModel
import com.habahabamall.ShoppingCartApp.models.users.GetCartAndWishModel
import com.habahabamall.ShoppingCartApp.products.Home
import com.habahabamall.ShoppingCartApp.products.ProductDetails
import com.habahabamall.ShoppingCartApp.services.products.ProductsService
import com.habahabamall.ShoppingCartApp.services.users.UsersService
import com.habahabamall.ShoppingCartApp.userShared.Login
import com.habahabamall.ShoppingCartApp.userShared.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
Screen names to be displayed in the selected navigation screen
*/
enum class NavPages {
    Login,
    Home,
    Product,
    Profile,
    Cart,
    Fav,
    Orders
}


/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationAppBar(
    //currentScreen: UserAccounts,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    userService: UsersService,
) {
    var loggedUser by remember { mutableStateOf(String()) }
    var cart by remember { mutableStateOf(listOf<ProductsModel>()) }
    var wish by remember { mutableStateOf(listOf<ProductsModel>()) }


    TopAppBar(
        title = {
            if (loggedUser.isNotEmpty())
                Text(text = loggedUser)
            else
                Text(stringResource(R.string.app_name))
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        actions =
        {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
            else {
                Row {

                    //shopping cart
                    BadgedBox(badge = {
                        if (cart.isNotEmpty()) {
                            Text(text = cart.size.toString())
                        }
                    },
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable { }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ShoppingCart,
                            contentDescription = stringResource(R.string.shopping_cart)
                        )
                    }

                    //favourite
                    BadgedBox(badge = {
                        if (wish.isNotEmpty()) {
                            Text(text = wish.size.toString())
                        }
                      },
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable { }
                    )
                    {
                        Icon(
                            imageVector = Icons.Rounded.Favorite,
                            contentDescription = stringResource(R.string.favorite)
                        )
                    }

                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    //check if user is logged in
                    val user = userService.checkUser()
                    if (user) {
                        val userDetails= userService.getLoggedUser()
                        loggedUser=userDetails.email

                        //get user wish and cart items
                        val userCart= GetCartAndWishModel(userDetails.id,1)
                        val userWish= GetCartAndWishModel(userDetails.id,2)

                        cart=userService.getUserCartandWish(userCart)
                        wish=userService.getUserCartandWish(userWish)
                    }
                }
            }

        }
    )

}

@Composable
fun NavApp(
    navController: NavHostController = rememberNavController(),
    productsService: ProductsService,
    usersService: UsersService,
    application: MainApplication,
    context: Context

) {

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    NavPages.valueOf(
        backStackEntry?.destination?.route ?: NavPages.Home.name
    )

    Scaffold(
        topBar = {
            NavigationAppBar(
                //currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                userService = UsersService(application, context)
            )
        },
        bottomBar = { BottomNav(navController, usersService) }

    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavPages.Home.name,
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 20.dp)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
        {
            composable(route = NavPages.Home.name) {
                Home(navController, productsService, context)
            }
            composable(route = NavPages.Login.name) {
                Login(
                    modifier = Modifier, usersService, context, navController
                )
            }
            composable(route = NavPages.Product.name) {
                ProductDetails(productsService, context)

            }
            composable(route = NavPages.Profile.name) {
                UserProfile(usersService)
            }

        }
    }
}
