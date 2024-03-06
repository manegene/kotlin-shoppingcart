package com.habahabamall.ShoppingCartApp.services.authentication

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat.getString
import com.habahabamall.ShoppingCartApp.MainApplication
import com.habahabamall.ShoppingCartApp.R
import com.habahabamall.ShoppingCartApp.models.authentication.GetFireBaseToken
import com.habahabamall.ShoppingCartApp.models.authentication.OrderFireBaseToken
import com.habahabamall.ShoppingCartApp.persist.DAOAppDatabase
import com.habahabamall.ShoppingCartApp.persist.models.PersistToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ApiService(application: MainApplication, context: Context) :
    IApiService {

    private val url:String=getString(context, R.string.firebase_address)
    private  val key:String= getString(context,R.string.firebase_key)
    private val apiService: IApiService = application.auth(url).create(IApiService::class.java)
    private val dbServ = DAOAppDatabase.getDatabase(context)
    override fun setContext():String {
        return key
    }


    override suspend fun tokenService(orderToken: OrderFireBaseToken): GetFireBaseToken {
        var response = GetFireBaseToken()
        try {
            response = apiService.tokenService(orderToken)

            CoroutineScope(Dispatchers.IO).launch {

                //check if a token record exist. If so then just
                // update the fields and not insert a new row
                //store the token for future usage
                if (checkStoredToken()) {
                    val tokenModel = PersistToken(
                        id = 1,
                        token = response.idToken,
                        tokenRefresh = response.refreshToken,
                        validTill = LocalDateTime.now().plusHours(1).toString()
                    )
                    dbServ.getToken().updateToken(tokenModel)
                }
                //there is no record in the database. Add a new record
                else {
                    val tokenModel = PersistToken(
                        token = response.idToken,
                        tokenRefresh = response.refreshToken,
                        validTill = LocalDateTime.now().plusHours(1).toString()
                    )
                    dbServ.getToken().addToken(tokenModel)
                }
            }
        }
        catch (ex: Exception) {
            Log.e("api error", setContext())
        }
        return response
    }

    override suspend fun checkStoredToken(): Boolean {

        //first check if there is a token stored in local db.
        return dbServ.getToken().ifExists() > 0
    }

    //entry point for any public service access. To be used to get new and stored token
    override suspend fun useStoredToken(): String {
        var token: String = ""
        GlobalScope.launch {
            try {
                if (checkStoredToken()) {
                    //check first if stored token is still valid. First get the stored date string
                    val stringTokenValidity = dbServ.getToken().getAll().validTill

                    //convert to date
                    val stringDate = LocalDateTime.now().compareTo(
                        LocalDateTime.parse(
                            stringTokenValidity, DateTimeFormatter.ISO_LOCAL_DATE_TIME
                        )
                    )

                    //if current date is later than stored, stored token has expired. Get a new one from API
                    if (stringDate > 0) {
                        token = tokenService(orderToken = OrderFireBaseToken()).idToken
                        return@launch
                    }

                    //otherwise token will be retrieved from the database
                    token = dbServ.getToken().getAll().token
                }
                //get a token from api
                else {
                    val newToken = tokenService(orderToken = OrderFireBaseToken())
                    token = newToken.idToken
                    return@launch

                }
            }
            catch (exc: Exception) {
                Log.e("exception", exc.toString())
            }
        }
        delay(5000)

        return token

    }
}
