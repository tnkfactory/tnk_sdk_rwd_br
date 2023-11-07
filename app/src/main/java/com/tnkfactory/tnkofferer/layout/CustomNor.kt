package com.tnkfactory.tnkofferer.layout

import android.view.View
import com.tnkfactory.ad.*
import com.tnkfactory.ad.basic.TnkAdListBasicItem
import com.tnkfactory.ad.off.TnkDirection
import com.xwray.groupie.GroupieViewHolder

/**
 * @author hanago
 * @email hans@tnkfactory.com
 * @since 2022/10/21
 **/
open class CustomNor : TnkAdListBasicItem() {

    fun getDivider() = root.findViewById<View>(R.id.com_tnk_off_item_divider)

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        super.bind(viewHolder, position)

        viewHolder.itemView.let { v ->
            if (direction == TnkDirection.BOTTOM) {
                getDivider()?.visibility = View.GONE
            } else {
                getDivider()?.visibility = View.VISIBLE
            }

            v.setOnClickListener { onItemClick() }
        }
    }

    override fun getLayout(): Int = R.layout.com_tnk_offerwall_adlist_item_normal
}