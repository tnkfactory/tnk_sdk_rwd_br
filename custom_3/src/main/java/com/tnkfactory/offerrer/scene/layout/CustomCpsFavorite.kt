package com.tnkfactory.offerrer.scene.layout

import com.tnkfactory.ad.R
import com.tnkfactory.ad.basic.TnkAdListCpsBasic

/**
 * @author hanago
 * @email hans@tnkfactory.com
 * @since 2022/10/21
 **/
open class CustomCpsFavorite : TnkAdListCpsBasic() {

    override fun getLayout(): Int = R.layout.com_tnk_offerwall_adlist_cps_favorite

    override fun getSpanSize(spanCount: Int, position: Int) = 6
}
