package com.tnkfactory.tnkofferer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tnkfactory.ad.TnkError
import com.tnkfactory.ad.TnkOfferwall
import com.tnkfactory.ad.TnkResultListener
import com.tnkfactory.ad.basic.TnkBasicHeaderNoTitle
import com.tnkfactory.tnkofferer.databinding.ActivityEmbedBBinding

class EmbedActivityB : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityEmbedBBinding = DataBindingUtil.setContentView(this, R.layout.activity_embed_b)

        val offerwall = TnkOfferwall(this)
        offerwall.navi.showLoading(true)

        offerwall.getConfig().apply {
            adListConfig.listHeader = TnkBasicHeaderNoTitle()
        }

        offerwall.load(object : TnkResultListener {
            override fun onSuccess() {
                offerwall.navi.showLoading(false)
                binding.contents.addView(offerwall.getTnkAdList())
            }

            override fun onFail(error: TnkError) {
                MaterialAlertDialogBuilder(this@EmbedActivityB)
                    .setMessage(error.message)
                    .create()
                    .show()
            }

        })

    }
}