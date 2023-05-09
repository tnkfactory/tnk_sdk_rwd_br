package com.tnkfactory.offerrer.scene.layout

import com.tnkfactory.ad.R
import com.tnkfactory.ad.basic.TnkAdListCpsNormal

/**
 * @author hanago
 * @email hans@tnkfactory.com
 * @since 2022/10/21
 **/
open class CustomCpsMyLike : TnkAdListCpsNormal() {

    override fun getLayout(): Int {
        return R.layout.com_tnk_offerwall_adlist_cps_my_like
    }

    override fun getSpanSize(spanCount: Int, position: Int) = 6
}
