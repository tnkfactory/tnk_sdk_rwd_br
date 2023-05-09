package com.tnkfactory.tnkofferer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.tnkfactory.ad.TnkAdConfig
import com.tnkfactory.ad.TnkOfferwall
import com.tnkfactory.ad.rwd.AdvertisingIdInfo
import com.tnkfactory.offerrer.TnkAdManager
import com.tnkfactory.tnkofferer.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var offerwall: TnkOfferwall

    var userName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        offerwall = TnkOfferwall(this).apply {

            lifecycleScope.launch(Dispatchers.IO) {
                // 고유 아이디는 매체사에서 유저 식별을 위한 고유값을 사용하셔야 하며
                // 이 예제에서는 google adid를 사용 합니다.
                userName = AdvertisingIdInfo.requestIdInfo(this@MainActivity).id // backgroud thread 처리 필요

                // 2) 유저 식별값 설정
                setUserName(userName)
                // 3) COPPA 설정 (https://www.ftc.gov/business-guidance/privacy-security/childrens-privacy)
                setCOPPA(false)

                getEarnPoint() { point ->
                    binding.tvPoint.text = "받을 수 있는 포인트 : $point p"
                }
            }
        }



        binding.btnDefault.setOnClickListener {
            TnkAdConfig.layoutConfig.setListHeader(1)
            offerwall.startOfferwallActivity(this@MainActivity)
        }



        binding.btnAddview.setOnClickListener {
            TnkAdConfig.layoutConfig.setListHeader(0)
            startActivity(Intent(this@MainActivity, EmbedActivity::class.java))
        }


        binding.btnCustomDefault.setOnClickListener {
            TnkAdManager.setUserInfo(this@MainActivity, false, userName)

            TnkAdManager.useTerms(true)
            TnkAdManager.usePointUnit(true)
            TnkAdManager.useCuration(true)

            TnkAdManager.setCustomClass()

            TnkAdManager.startDefaultActivity(this@MainActivity)
        }

        binding.btnCustomAddview.setOnClickListener {
            TnkAdManager.setUserInfo(this@MainActivity, false, userName)

            TnkAdManager.useTerms(true)
            TnkAdManager.usePointUnit(true)
            TnkAdManager.useCuration(true)

            TnkAdManager.setCustomClass()

            TnkAdManager.startEmbedActivity(this@MainActivity)
        }

    }

}
