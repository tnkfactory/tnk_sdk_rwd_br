package com.tnkfactory.offerrer.scene.layout

import android.widget.TextView
import com.tnkfactory.ad.R
import com.tnkfactory.ad.TnkSession
import com.tnkfactory.ad.basic.TnkAdListCpsBasic
import com.xwray.groupie.GroupieViewHolder

/**
 * @author hanago
 * @email hans@tnkfactory.com
 * @since 2022/10/21
 **/
class CustomReward : TnkAdListCpsBasic() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        super.bind(viewHolder, position)

        viewHolder.itemView.let { root ->
            val tvNumber: TextView? = root.findViewById(R.id.com_tnk_off_list_cps_reward_number)
            tvNumber?.text = "" + (mPosition + 1)
        }
    }

    override fun getLayout(): Int {
        return R.layout.com_tnk_offerwall_adlist_cps_reward
    }

    override fun getSpanSize(spanCount: Int, position: Int): Int = TnkSession.DEFAULT_SPAN_SIZE
}