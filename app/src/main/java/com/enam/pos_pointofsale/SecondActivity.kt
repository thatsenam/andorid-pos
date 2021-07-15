package com.enam.pos_pointofsale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class SecondActivity : AppCompatActivity() {

    private var mRewardedAd: RewardedAd? = null
    private final var TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        MobileAds.initialize(this) {}

        val restApiService = RestApiService(context = applicationContext)

        var rewardBtn = findViewById<Button>(R.id.rewardBtn);
        var loadBtn = findViewById<Button>(R.id.load);


        var adRequest = AdRequest.Builder().build()
        var adListener = object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError?.message)
                mRewardedAd = null
                rewardBtn.isEnabled = false
                Toast.makeText(baseContext, "Load: " + adError?.message, Toast.LENGTH_SHORT).show()

            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                Log.d(TAG, "Ad was loaded.")
                Toast.makeText(baseContext, "Load: Ad was loaded", Toast.LENGTH_SHORT).show()

                rewardBtn.isEnabled = true
                mRewardedAd = rewardedAd
                mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdShowedFullScreenContent() {
                        Toast.makeText(
                            applicationContext,
                            "onAdShowedFullScreenContent: Ad was shown.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                        rewardBtn.isEnabled = false

                        Toast.makeText(
                            applicationContext,
                            "onAdShowedFullScreenContent: Ad failed to show.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onAdDismissedFullScreenContent() {

                        restApiService.hitAdView()
                    }
                }
            }

        };
        // Real ca-app-pub-7468488190342162/7342062846
        loadBtn.setOnClickListener {
            RewardedAd.load(
                applicationContext,
                "ca-app-pub-7468488190342162/7342062846",
                adRequest,
                adListener
            )
//            restApiService.hitAdView()

        }

        rewardBtn.setOnClickListener {
            if (mRewardedAd != null) {
                mRewardedAd?.show(this) {
                    fun onUserEarnedReward(rewardItem: RewardItem) {
                        Toast.makeText(
                            applicationContext,
                            "Show: User earned the reward.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Log.d(TAG, "Show: The rewarded ad wasn't ready yet.")
                Toast.makeText(
                    applicationContext,
                    "Show: The rewarded ad wasn't ready yet.",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
        loadBtn.performClick()

    }
}