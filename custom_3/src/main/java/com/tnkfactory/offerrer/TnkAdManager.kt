package com.tnkfactory.offerrer

import android.content.Context
import com.tnkfactory.ad.TnkAdConfig
import com.tnkfactory.ad.TnkOfferwall
import com.tnkfactory.offerrer.scene.layout.CustomNor
import com.tnkfactory.ad.basic.TnkAdLayoutNone
import com.tnkfactory.ad.basic.TnkAdListCpsNormal
import com.tnkfactory.ad.basic.TnkAdListItemLayout
import com.tnkfactory.ad.basic.TnkAdListLayoutCpsF
import com.tnkfactory.ad.basic.TnkAdListLayoutCpsNone
import com.tnkfactory.ad.basic.TnkBasicCurationTypeNew
import com.tnkfactory.ad.basic.TnkBasicCurationTypeSuggest
import com.tnkfactory.ad.basic.TnkSectionHorizontalSingle
import com.tnkfactory.ad.rwd.data.layout.TnkLayoutType
import com.tnkfactory.offerrer.scene.TnkEmbedActivity
import com.tnkfactory.offerrer.scene.layout.CustomCpsFavorite
import com.tnkfactory.offerrer.scene.layout.CustomCpsMyLike
import com.tnkfactory.offerrer.scene.layout.CustomCpsNew
import com.tnkfactory.offerrer.scene.layout.CustomCpsNor
import com.tnkfactory.offerrer.scene.layout.CustomCpsRecommend
import com.tnkfactory.offerrer.scene.layout.CustomCpsSearch
import com.tnkfactory.offerrer.scene.layout.CustomJoinMultiItem
import com.tnkfactory.offerrer.scene.layout.CustomNew
import com.tnkfactory.offerrer.scene.layout.CustomNews
import com.tnkfactory.offerrer.scene.layout.CustomPopular
import com.tnkfactory.offerrer.scene.layout.CustomPromotion
import com.tnkfactory.offerrer.scene.layout.CustomReward
import com.tnkfactory.offerrer.scene.layout.CustomSuggest

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
        TnkAdConfig.layoutConfig.setListHeader(0)
        TnkEmbedActivity.start(context)
    }

    fun startDefaultActivity(context: Context) {
        TnkAdConfig.layoutConfig.setListHeader(1)
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
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_NORMAL, CustomNor::class, TnkAdLayoutNone::class)                       // normal
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_PROMOTION, CustomPromotion::class, TnkAdListItemLayout::class)          // 소진 큐레이션
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_NEW, CustomNew::class, TnkBasicCurationTypeNew::class)                  // 신규
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_SUGGEST, CustomSuggest::class, TnkBasicCurationTypeSuggest::class)      // 운영자
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_CPS_NORMAL, CustomCpsNor::class, TnkAdListLayoutCpsNone::class)         // 구매형 일반
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_CPS_FAVORITE, CustomCpsFavorite::class, TnkSectionHorizontalSingle::class)    // 구매형 관심
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_CPS_POPULAR, CustomPopular::class, TnkAdListLayoutCpsF::class)          // 구매형 인기
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_CPS_REWARD, CustomReward::class, TnkAdListLayoutCpsF::class)            // 구매형 리워드 가성비
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_CPS_NEW, CustomCpsNew::class, TnkAdListLayoutCpsF::class)               // 구매형 신규
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_CPS_RECOMMEND, CustomCpsRecommend::class, TnkSectionHorizontalSingle::class)   // 구매형 운영자 등록
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_CPS_SEARCH, CustomCpsSearch::class, TnkAdListLayoutCpsNone::class)      // 검색 결과
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_CPS_MY_FAVORITE, CustomCpsMyLike::class, TnkAdListLayoutCpsNone::class)   // my메뉴 관심상품
        TnkAdConfig.setLayoutInfo(TnkLayoutType.LAYOUT_TYPE_NOAD, CustomNor::class, TnkAdListLayoutCpsNone::class)              // 광고 없을때 SDK 생성 추천

        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_NEWS, CustomNews::class, TnkAdListItemLayout::class)                    // 뉴스 컨텐츠 기본
        TnkAdConfig.setLayoutInfo(TnkLayoutType.AD_LIST_MULTI, CustomJoinMultiItem::class, TnkSectionHorizontalSingle::class)   // 진행중인 멀티액션 광고
    }


}