package com.tnkfactory.tnkofferer

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tnkfactory.ad.TnkError
import com.tnkfactory.ad.TnkOfferwall
import com.tnkfactory.ad.TnkResultListener
import com.tnkfactory.ad.TnkSession
import com.tnkfactory.ad.basic.TnkAdListHeader
import com.tnkfactory.ad.basic.TnkBasicHeaderNoTitle
import com.tnkfactory.ad.rwd.common.TAlertDialog
import com.tnkfactory.tnkofferer.databinding.ActivityEmbedABinding
import com.tnkfactory.tnkofferer.databinding.ActivityMainBinding

class EmbedActivityA : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityEmbedABinding = DataBindingUtil.setContentView(this, R.layout.activity_embed_a)

        val offwall = TnkOfferwall(this)

        offwall.getConfig().apply {
            adListConfig.title = "충전소"
        }

        offwall.load(object : TnkResultListener {
            override fun onSuccess() {
                val adList = offwall.getTnkAdList()
                binding.contents.addView(adList.showListview())
            }

            override fun onFail(error: TnkError) {
                TAlertDialog.show(this@EmbedActivityA, error.message, { finish() }, null)
            }

        })

    }
}