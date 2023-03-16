package com.tnkfactory.tnkofferer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tnkfactory.ad.TnkAdConfig
import com.tnkfactory.ad.TnkError
import com.tnkfactory.ad.TnkOfferwall
import com.tnkfactory.ad.TnkResultListener
import com.tnkfactory.ad.basic.TnkAdLayoutNone
import com.tnkfactory.ad.basic.TnkAdListItemLayout
import com.tnkfactory.ad.basic.TnkAdListLayoutCpsF
import com.tnkfactory.ad.basic.TnkAdListLayoutCpsNone
import com.tnkfactory.ad.basic.TnkBasicCurationTypeNew
import com.tnkfactory.ad.basic.TnkBasicCurationTypeSuggest
import com.tnkfactory.ad.basic.TnkSectionHorizontalSingle
import com.tnkfactory.ad.rwd.data.layout.TnkLayoutType
import com.tnkfactory.tnkofferer.databinding.ActivityEmbedBBinding
import com.tnkfactory.tnkofferer.layout.TnkAdListCpsCard
import com.tnkfactory.tnkofferer.layout.TnkAdListCpsGrid
import com.tnkfactory.tnkofferer.layout.TnkAdListCpsNormal
import com.tnkfactory.tnkofferer.layout.TnkAdListMultiJoin
import com.tnkfactory.tnkofferer.layout.TnkAdListNew
import com.tnkfactory.tnkofferer.layout.TnkAdListNormal
import com.tnkfactory.tnkofferer.layout.TnkAdListPromotion
import com.tnkfactory.tnkofferer.layout.TnkAdListSuggest

class CustomLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityEmbedBBinding = DataBindingUtil.setContentView(this, R.layout.activity_embed_b)

        val offerwall = TnkOfferwall(this)

        setAdLayoutConfig()

        offerwall.load(object : TnkResultListener {
            override fun onSuccess() {
                binding.contents.addView(offerwall.getAdListView())
            }

            override fun onFail(error: TnkError) {
                MaterialAlertDialogBuilder(this@CustomLayoutActivity)
                    .setMessage(error.message)
                    .create()
                    .show()
            }

        })

    }


    fun setAdLayoutConfig() {

        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_NORMAL, TnkAdListNormal::class, TnkAdLayoutNone::class)
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_PROMOTION, TnkAdListPromotion::class, TnkAdListItemLayout::class)
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_NEW, TnkAdListNew::class, TnkBasicCurationTypeNew::class)
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_SUGGEST, TnkAdListSuggest::class, TnkBasicCurationTypeSuggest::class)
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_MULTI, TnkAdListMultiJoin::class, TnkSectionHorizontalSingle::class)

        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_CPS_NORMAL, TnkAdListCpsNormal::class, TnkAdListLayoutCpsNone::class)
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_CPS_FAVORITE, TnkAdListCpsCard::class, TnkSectionHorizontalSingle::class)
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_CPS_POPULAR, TnkAdListCpsGrid::class, TnkAdListLayoutCpsF::class)
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_CPS_REWARD, TnkAdListCpsGrid::class, TnkAdListLayoutCpsF::class)
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_CPS_NEW, TnkAdListCpsNormal::class, TnkAdListItemLayout::class)
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_CPS_RECOMMEND, TnkAdListCpsCard::class, TnkSectionHorizontalSingle::class)

        TnkAdConfig.layoutConfig.adDetailLayout = R.layout.com_tnk_custom_detail_basic
        TnkAdConfig.layoutConfig.adDetailCampaignLayout = R.layout.com_tnk_custom_detail_action_item
        TnkAdConfig.usePointUnit = true

        // 차후 지원 예정
//        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_CPS_SEARCH, TnkAdListCpsB::class, TnkAdListLayoutCpsNone::class)
//        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_CPS_MY_FAVORITE, TnkAdListCpsLikePopup::class, TnkAdListLayoutCpsNone::class)

//        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_NEWS, TnkAdListNews::class, TnkAdListItemLayout::class)
//        TnkAdConfig.setLayoutInfo(TnkLayoutType.LAYOUT_TYPE_NOAD, TnkAdListItemIcon::class, TnkAdListLayoutCpsNone::class)



    }

}