package com.enam.pos_pointofsale

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.*
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    var POS = "pos"
    var createTokenEndPoint: String = "https://cgbin.kinetixbd.com/api/tokens/create"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginBtn.setOnClickListener {
            loginBtn.isEnabled = false
            login(
                emailAddressET.text.toString(),
                passwordET.text.toString()
            )

        }

    }


    fun toast(msg: String?) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT)
            .show()

    }

    private fun login(email: String, password: String) {
        when {
            email.isEmpty() -> {
                loginBtn.isEnabled = true
                toast("Email address is required")
            }
            password.isEmpty() -> {
                toast("Password is required")
                loginBtn.isEnabled = true

            }
            else -> attemptLogin(email, password)
        }
    }

    fun attemptLogin(email: String, password: String) {

        val client = OkHttpClient()

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", email)
            .addFormDataPart("password", password)
            .build()
        val request: Request = Request.Builder()
            .url(createTokenEndPoint)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .post(requestBody)
            .build()
        try {
            var response: Response = client.newCall(request).execute()
            val responseBody = response.body?.string();
            val json = JSONObject(responseBody)

            if (response.code == 200) {

                var localDb = getSharedPreferences(POS, Context.MODE_PRIVATE)
                val editor = localDb.edit()
                val token = json.getString("token");
                editor.putString("token", token)
                editor.apply()
                toast("Login Successfully")
                startActivity(Intent(applicationContext, MainActivity::class.java))
            } else {
                toast(json.getString("message"))
            }
        } catch (exception: Exception) {
            toast(exception.message)
        }
        loginBtn.isEnabled = true

    }
}


