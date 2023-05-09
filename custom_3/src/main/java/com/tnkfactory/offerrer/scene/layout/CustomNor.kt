package com.tnkfactory.offerrer.scene.layout

import android.view.View
import android.widget.ImageView
import android.widget.TextView
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

    override fun getAdImageView() = root.findViewById<ImageView>(R.id.com_tnk_off_item_image)
    override fun getIconImageView() = root.findViewById<ImageView>(R.id.com_tnk_off_item_icon)
    override fun getTitleView() = root.findViewById<TextView>(R.id.com_tnk_off_item_title)
    override fun getSubTitleView() = root.findViewById<TextView>(R.id.com_tnk_off_item_sub_title)
    override fun getOrgPntAmtView() = root.findViewById<TextView>(R.id.com_tnk_off_item_org_pnt_amt)
    override fun getPointView() = root.findViewById<TextView>(R.id.com_tnk_off_item_point)
    override fun getUnitView() = root.findViewById<TextView>(R.id.com_tnk_off_item_unit)
    override fun getUnitIconView() = root.findViewById<View>(R.id.com_tnk_off_item_unit_icon)
    override fun getCampnTypeView() = root.findViewById<TextView>(R.id.com_tnk_off_item_campaign)
    override fun getMultiRewardView() = root.findViewById<TextView>(R.id.com_tnk_off_item_multi_reward_text)
    override fun getReceiptView() = root.findViewById<TextView>(R.id.com_tnk_off_item_complete)
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