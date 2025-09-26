package com.tnkfactory.offerrer

import android.content.Context
import com.tnkfactory.ad.TnkAdConfig
import com.tnkfactory.ad.TnkOfferwall
import com.tnkfactory.ad.basic.TnkAdListItemFeed
import com.tnkfactory.ad.basic.TnkAdListItemLayout
import com.tnkfactory.ad.rwd.data.layout.TnkLayoutType
import com.tnkfactory.offerrer.scene.TnkEmbedActivity

/**
 * @author hanago
 * @email hans@tnkfactory.com
 * @since 2023/04/24
 **/
object TnkAdManager {

    fun setUserInfo(context: Context, coppa: Boolean, userName: String) {
        TnkOfferwall(context).run {
            setCOPPA(coppa)
            setUserName(userName)
        }
    }

    fun startEmbedActivity(context: Context) {
        TnkEmbedActivity.start(context)
    }

    fun startDefaultActivity(context: Context) {
        TnkOfferwall(context).startOfferwallActivity(context)
    }


    fun useTerms(flag: Boolean) {
        TnkAdConfig.useTermsPopup = flag
    }

    fun usePointUnit(flag: Boolean) {
        TnkAdConfig.usePointUnit = flag
    }

    fun useCuration(flag: Boolean) {
        TnkAdConfig.useCuration = flag
    }

    fun setCustomClass() {
        useTerms(true)
        usePointUnit(false)
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_NORMAL, TnkAdListItemFeed::class, TnkAdListItemLayout::class)
        //useCuration(true)
    }
}
