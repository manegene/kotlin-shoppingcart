package com.habahabamall.ShoppingCartApp.products

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.habahabamall.ShoppingCartApp.R
import com.habahabamall.ShoppingCartApp.constants.AddWishAndCart
import com.habahabamall.ShoppingCartApp.constants.ProgressIndicator
import com.habahabamall.ShoppingCartApp.constants.calculateDiscount
import com.habahabamall.ShoppingCartApp.constants.isInternetAvailable
import com.habahabamall.ShoppingCartApp.models.products.ProductsModel
import com.habahabamall.ShoppingCartApp.persist.DAOAppDatabase
import com.habahabamall.ShoppingCartApp.services.products.ProductsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ProductDetails(productService: ProductsService, context: Context) {
    var product by remember { mutableStateOf(ProductsModel()) }

    val dbServ = DAOAppDatabase.getDatabase(context)
    val connected = isInternetAvailable(context)

    if (connected) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {

                val prodId = dbServ.getProduct().getCurrent().storedProductId
                product = productService.getProductById(prodId)

            }
        }
    }
    if (product.pname.isEmpty())
        ProgressIndicator(context)

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Box {
            AsyncImage(
                model = "https://${stringResource(R.string.web_host)}/images/thumbs/000/" + product.previewmage,
                contentDescription = product.pname,
                contentScale = ContentScale.Fit
            )

        }
        AddWishAndCart()


        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Text(

                text = "${product.currency} ${product.price}",
                style = MaterialTheme.typography.labelLarge,
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


        Text(
            text = product.fullDescription?:"description missing",
            style = MaterialTheme.typography.titleSmall,
        )


    }


    //}
}