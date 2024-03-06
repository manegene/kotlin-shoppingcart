package com.habahabamall.ShoppingCartApp.constants

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat.getString
import androidx.navigation.NavController
import com.habahabamall.ShoppingCartApp.R


fun handleIntent(intent: Intent,context: Context, navController: NavController) {
    intent.let {
        if (it.action == Intent.ACTION_VIEW) {
            val uri = it.data
            uri?.let { uri ->
                    if (uri.scheme == "https" &&
                        uri.host == getString(context, R.string.web_host) &&
                        uri.path == "/en/login") {
                        navController.navigate(NavPages.Login.name)

                    }
            }
        }
    }
}


//redirect to web site sign up page
fun redirectRegister(
    redirectURL: String,
    context: Context,
    launcher: ActivityResultLauncher<Intent>
) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(redirectURL)
    }
    try {
        launcher.launch(intent)
    }
    catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No web app found", Toast.LENGTH_SHORT).show()
    }
}


//reset user password opened is web app
//redirect to web site sign up page
fun redirectForgotPassword(
    redirectURL: String,
    context: Context,
    launcher: ActivityResultLauncher<Intent>
) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(redirectURL)
    }
    try {
        launcher.launch(intent)
    }
    catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No web app found", Toast.LENGTH_SHORT).show()
    }
}


//opens user default email app with send to the email provided
fun launchEmailIntent(
    emailAddress: String,
    context: Context,
    launcher: ActivityResultLauncher<Intent>
) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$emailAddress")
    }
    try {
        launcher.launch(intent)
    }
    catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
    }
}

//share function
fun globalShare(
    link: String,
    context: Context,
    launcher: ActivityResultLauncher<Intent>
) {
    val share = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, link)
        type = "text/plain"
    }
    try {
        launcher.launch(share)
    }
    catch (e:Exception){
        Toast.makeText(context,"no app found to share the link",Toast.LENGTH_SHORT).show()
    }

}