package com.tnkfactory.offerrer.scene.layout

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.tnkfactory.ad.R
import com.tnkfactory.ad.basic.TnkAdListBasicItem
import com.xwray.groupie.GroupieViewHolder

/**
 * @author hanago
 * @email hans@tnkfactory.com
 * @since 2022/10/05
 **/
class CustomNews : TnkAdListBasicItem() {

    fun getRewardLayout() = root.findViewById<LinearLayout>(R.id.com_tnk_off_detail_ll_reward)
    fun getNewsComliete() = root.findViewById<TextView>(R.id.com_tnk_off_news_complete)


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        super.bind(viewHolder, position)
        viewHolder.itemView.let { v ->

            getTitleView()?.text = adItem.app_nm

            if (adItem.payYn == "Y") {
                v.alpha = 0.5f
                getNewsComliete()?.visibility = View.VISIBLE
                getRewardLayout()?.visibility = View.GONE
            } else {
                v.alpha = 1.0f
                getNewsComliete()?.visibility = View.GONE
                getRewardLayout()?.visibility = View.VISIBLE
            }

            v.setOnClickListener { onItemClick() }

        }
    }

    override fun getLayout(): Int = R.layout.com_tnk_offerwall_adlist_news

}