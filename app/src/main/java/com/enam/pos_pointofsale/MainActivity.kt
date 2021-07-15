package com.enam.pos_pointofsale

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    lateinit var context: Context;
    override fun onCreate(savedInstanceState: Bundle?) {
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.context = applicationContext

        RestApiService(this.context)
        var button = findViewById<Button>(R.id.nextArticle);
        button.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
        MobileAds.initialize(this) {}

//        Toast.makeText(this, getPublicIP(), Toast.LENGTH_SHORT).show()
        if (getPublicIP() == "Bangladesh") {
            button.isEnabled = false;
            button.text = "Please turn VPN On";
        } else {
            button.isEnabled = true;
            button.text = "Next Article";
        }


    }
}

@Throws(IOException::class)
fun getPublicIP(): String? {

    try {
        val doc: Document = Jsoup.connect("https://www.find-ip.net/").get()
        return doc.select(".ipcontent.pure-u-13-24")[1].text()
    } catch (e: Exception) {
    }
    return null;

}