package com.tnkfactory.offerrer.scene.layout

import com.tnkfactory.ad.R
import com.tnkfactory.ad.basic.TnkAdListBasicItem
import com.xwray.groupie.GroupieViewHolder

/**
 * @author hanago
 * @email hans@tnkfactory.com
 * @since 2022/10/21
 **/
class CustomNew : TnkAdListBasicItem() {

    override fun getLayout(): Int {
        return R.layout.com_tnk_offerwall_adlist_new
    }

    override fun getSpanSize(spanCount: Int, position: Int) = 12

}