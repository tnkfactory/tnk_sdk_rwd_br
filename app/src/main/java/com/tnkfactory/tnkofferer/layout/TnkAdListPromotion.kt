package com.tnkfactory.tnkofferer.layout

import android.view.View
import android.view.ViewGroup
import com.tnkfactory.ad.basic.TnkAdListBasicItem
import com.tnkfactory.ad.off.TnkDirection
import com.tnkfactory.ad.style.DpUtil
import com.tnkfactory.tnkofferer.R
import com.xwray.groupie.GroupieViewHolder

/**
 * @author hanago
 * @email hans@tnkfactory.com
 * @since 2022/10/21
 **/
class TnkAdListPromotion : TnkAdListBasicItem() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        super.bind(viewHolder, position)

        val root = viewHolder.root

        if (direction and TnkDirection.LEFT == TnkDirection.LEFT) {
            root.setPadding(DpUtil.dpToPx(20.0f), root.paddingTop, DpUtil.dpToPx(5.0f), root.paddingBottom)
        } else {
            root.setPadding(DpUtil.dpToPx(5.0f), root.paddingTop, DpUtil.dpToPx(20.0f), root.paddingBottom)
        }
    }


    override fun getLayout(): Int = R.layout.com_tnk_off_ad_list_promotion

    override fun getSpanSize(spanCount: Int, position: Int) = 6

}
