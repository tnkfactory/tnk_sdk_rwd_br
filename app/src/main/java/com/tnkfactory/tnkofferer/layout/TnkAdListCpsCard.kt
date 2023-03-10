package com.tnkfactory.tnkofferer.layout

import com.tnkfactory.ad.basic.TnkAdListCpsBasic
import com.tnkfactory.tnkofferer.R

/**
 * @author hanago
 * @email hans@tnkfactory.com
 * @since 2022/10/21
 **/
open class TnkAdListCpsCard : TnkAdListCpsBasic() {

    override fun getLayout(): Int = R.layout.com_tnk_off_ad_list_cps_favorite

    override fun getSpanSize(spanCount: Int, position: Int) = 6
}
