package com.tnkfactory.tnkofferer.layout

import com.tnkfactory.ad.basic.TnkAdListCpsBasic
import com.tnkfactory.ad.off.TnkDirection
import com.tnkfactory.ad.style.DpUtil
import com.tnkfactory.tnkofferer.R
import com.xwray.groupie.GroupieViewHolder

/**
 * @author hanago
 * @email hans@tnkfactory.com
 * @since 2022/10/21
 **/
open class TnkAdListCpsNormal : TnkAdListCpsBasic() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        super.bind(viewHolder,position)
        viewHolder.itemView.let { root ->

            if (direction and TnkDirection.LEFT == TnkDirection.LEFT) {
                root.setPadding(DpUtil.dpToPx(12.50f), root.paddingTop, DpUtil.dpToPx(0.0f), root.paddingBottom)
            } else {
                root.setPadding(DpUtil.dpToPx(0.0f), root.paddingTop, DpUtil.dpToPx(12.5f), root.paddingBottom)
            }
        }
    }

    override fun setPosition(position: Int) {
        super.setPosition(position)
        if (position % 2 == 0) {
            direction = direction or TnkDirection.LEFT
            direction = direction and (TnkDirection.RIGHT).inv()
        } else {
            direction = direction and (TnkDirection.LEFT).inv()
            direction = direction or TnkDirection.RIGHT
        }
    }


    override fun getLayout(): Int {
        return R.layout.com_tnk_off_ad_list_cps_normal
    }

    override fun getSpanSize(spanCount: Int, position: Int) = 6
}
