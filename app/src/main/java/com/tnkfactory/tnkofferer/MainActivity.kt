package com.tnkfactory.tnkofferer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import com.tnkfactory.ad.PlacementEventListener
import com.tnkfactory.ad.TnkAdConfig
import com.tnkfactory.ad.TnkOfferwall
import com.tnkfactory.ad.basic.AdPlacementView
import com.tnkfactory.ad.basic.PlacementFeedViewLayout
import com.tnkfactory.ad.basic.PlacementScrollViewLayout
import com.tnkfactory.ad.basic.PlacementViewPagerLayout
import com.tnkfactory.ad.basic.TnkAdPlacementFeedImageItem
import com.tnkfactory.ad.basic.TnkAdPlacementFeedItem
import com.tnkfactory.ad.basic.TnkAdPlacementIconItem
import com.tnkfactory.ad.basic.TnkAdPlacementListItem
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
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
            TnkAdManager.setCustomClass()
            TnkAdManager.startDefaultActivity(this@MainActivity)
        }

        binding.btnCustomAddview.setOnClickListener {
            TnkAdManager.setUserInfo(this@MainActivity, false, userName)
            TnkAdManager.setCustomClass()
            TnkAdManager.startEmbedActivity(this@MainActivity)
        }

        binding.tvPlacement1.setOnClickListener {
            TnkAdConfig.setPlacementLayout("open_ad", TnkAdPlacementFeedItem::class, PlacementFeedViewLayout::class)
            setupPlacementView()
        }
        binding.tvPlacement2.setOnClickListener {
            TnkAdConfig.setPlacementLayout("open_ad", TnkAdPlacementFeedImageItem::class, PlacementFeedViewLayout::class)
            setupPlacementView()
        }
        binding.tvPlacement3.setOnClickListener {
            TnkAdConfig.setPlacementLayout("open_ad", TnkAdPlacementIconItem::class, PlacementScrollViewLayout::class)
            setupPlacementView()
        }
        binding.tvPlacement4.setOnClickListener {
            TnkAdConfig.setPlacementLayout("open_ad", TnkAdPlacementListItem::class, PlacementViewPagerLayout::class)
            setupPlacementView()
            adPlacementView.spanCount = 1
            adPlacementView.pageRowCount = 3
        }
    }


    val placementContainerView: ViewGroup by lazy { binding.flPlacementAd }


    lateinit var adPlacementView: AdPlacementView
    fun setupPlacementView() {
        adPlacementView = offerwall.getAdPlacementView(this)
        placementContainerView.removeAllViews()
        placementContainerView.addView(adPlacementView)
        loadPlacementView()


        adPlacementView.placementEventListener = object : PlacementEventListener {
            override fun didAdDataLoaded(placementId: String, customData: String?) {
                adPlacementView.showAdList()
            }

            override fun didFailedToLoad(placementId: String) {
                Toast.makeText(this@MainActivity, "광고 로딩 실패", Toast.LENGTH_SHORT).show()
            }

            override fun didAdItemClicked(appId: String, appName: String) {
                Log.d("didAdItemClicked", "appId : $appId, appName : $appName")
            }

            override fun didMoreLinkClicked() {
                offerwall.startOfferwallActivity(this@MainActivity)
            }
        }
    }

    fun loadPlacementView() {
        adPlacementView.loadAdList("open_ad")
    }

}
