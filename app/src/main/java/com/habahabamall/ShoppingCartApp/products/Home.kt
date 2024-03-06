package com.habahabamall.ShoppingCartApp.products

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.habahabamall.ShoppingCartApp.R
import com.habahabamall.ShoppingCartApp.constants.NavPages
import com.habahabamall.ShoppingCartApp.constants.ProgressIndicator
import com.habahabamall.ShoppingCartApp.constants.calculateDiscount
import com.habahabamall.ShoppingCartApp.constants.isInternetAvailable
import com.habahabamall.ShoppingCartApp.models.products.ProductsModel
import com.habahabamall.ShoppingCartApp.persist.DAOAppDatabase
import com.habahabamall.ShoppingCartApp.persist.models.PersistProductDetails
import com.habahabamall.ShoppingCartApp.services.products.ProductsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//composable to display home page
@Composable
fun Home(navController: NavController, productService: ProductsService, context: Context) {


    var products by remember { mutableStateOf<List<ProductsModel>>(emptyList()) }
    val connected = isInternetAvailable(context)

    if (connected) {
        LaunchedEffect(true)
        {
            products = productService.getAllProducts()
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(10.dp),
        flingBehavior = ScrollableDefaults.flingBehavior(),
        userScrollEnabled = true,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxSize()
    ) {
        items(products) { product ->
            ProductItem(product, navController, context)
        }
    }

    //show progress bar while fetching products
    if (products.isEmpty()) {
        ProgressIndicator(context)
    }
}

@Composable
fun ProductItem(product: ProductsModel, navController: NavController, context: Context) {
    val dbServ = DAOAppDatabase.getDatabase(context)
    val modifier = Modifier


    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),


        modifier = modifier
            .height(300.dp)
            .fillMaxWidth()
            .padding(all = 2.dp)
            .clickable {

                navController.navigate(NavPages.Product.name) {
                    launchSingleTop = true


                }
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {

                        val exist = dbServ
                            .getProduct()
                            .getProductCount()
                        if (exist > 0) {
                            dbServ
                                .getProduct()
                                .updateNew(PersistProductDetails(storedProductId = product.id))
                        }
                        else {
                            dbServ
                                .getProduct()
                                .addProduct(PersistProductDetails(storedProductId = product.id))
                        }

                    }
                }
            }
            .clip(MaterialTheme.shapes.small)
            .background(color = Color.Blue)

    )
    {
        Box {
            AsyncImage(
                model = "https://${stringResource(R.string.web_host)}/images/thumbs/000/" + product.previewmage,
                contentDescription = product.pname,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxWidth()
                    .height(190.dp)
            )
            if (product.isNewProduct) {
                Text(
                    text = "new arrival", color = Color.Yellow,
                    style = TextStyle(background = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }

        }


        Text(
            text = product.pname,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.fillMaxWidth()
        )


        Text(
            text = "${product.currency} ${product.price}",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.fillMaxWidth()
        )
        if (product.oldprice > 0) {
            val discount = calculateDiscount(product.price, product.oldprice)
            Text(
                text = discount,
                style = TextStyle(
                    fontStyle = FontStyle.Italic,
                    color = Color.Red,
                )
            )

        }


    }

}

