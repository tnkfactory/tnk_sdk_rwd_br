package com.tnkfactory.offerrer.scene.layout

import android.view.View
import android.view.ViewGroup
import com.tnkfactory.ad.R
import com.tnkfactory.ad.basic.TnkAdListBasicItem
import com.tnkfactory.ad.off.TnkDirection
import com.tnkfactory.ad.style.DpUtil
import com.xwray.groupie.GroupieViewHolder

/**
 * @author hanago
 * @email hans@tnkfactory.com
 * @since 2022/10/21
 **/
class CustomPromotion : TnkAdListBasicItem() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        super.bind(viewHolder, position)

        val root = viewHolder.root
        if (adItem.appId == 0L) {
            (root as ViewGroup).getChildAt(0).visibility = View.INVISIBLE
            return
        } else {
            (root as ViewGroup).getChildAt(0).visibility = View.VISIBLE
        }
//            if (adLayout.adwall.numColumnsPortrait != 12) {
        if (direction and TnkDirection.LEFT == TnkDirection.LEFT) {
            root.setPadding(DpUtil.dpToPx(20.0f), root.paddingTop, DpUtil.dpToPx(5.0f), root.paddingBottom)
        } else {
            root.setPadding(DpUtil.dpToPx(5.0f), root.paddingTop, DpUtil.dpToPx(20.0f), root.paddingBottom)
        }
    }


    override fun getLayout(): Int = R.layout.com_tnk_offerwall_adlist_promotion

    override fun getSpanSize(spanCount: Int, position: Int) = 6

}
