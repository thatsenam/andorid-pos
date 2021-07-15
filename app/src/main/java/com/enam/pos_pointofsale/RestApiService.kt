package com.enam.pos_pointofsale

import android.content.Context
import android.content.Intent
import android.widget.Toast
import okhttp3.*
import retrofit2.Call
import retrofit2.http.GET
import java.lang.Exception


class RestApiService {
    var adview_endpoint = "https://cgbin.kinetixbd.com/api/ad_views"
    var token: String? = null;
    var POS = "pos"
    lateinit var context: Context;

    constructor(context: Context) {
        this.context = context;
        var localDb = context.getSharedPreferences(POS, Context.MODE_PRIVATE);
        if (localDb != null) {
            token = localDb.getString("token", null)
        }
        if (token == null) {
            context.startActivity(
                Intent(
                    context,
                    LoginActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }

    }

    interface GitHubService {

        @GET("ad_views")
        fun hitView(): Call<Any>?
    }

    fun hitAdView() {

        val client = OkHttpClient()

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("test", "test")
            .build()

        val request: Request = Request.Builder()
            .url(adview_endpoint)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer $token")
            .post(requestBody)
            .build()
        try {
            var response: Response = client.newCall(request).execute()
            if (response.code == 200) {
                Toast.makeText(context, "Point Added Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, response.body?.string() + response.code, Toast.LENGTH_SHORT)
                    .show()
                goToLoginActivity()
            }
        } catch (exception: Exception) {
            Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            goToLoginActivity()
        }


    }

    fun goToLoginActivity() {
        context.startActivity(
            Intent(
                context,
                LoginActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }


}