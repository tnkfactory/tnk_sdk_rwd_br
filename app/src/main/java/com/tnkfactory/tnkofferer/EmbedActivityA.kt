package com.tnkfactory.tnkofferer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tnkfactory.ad.TnkError
import com.tnkfactory.ad.TnkOfferwall
import com.tnkfactory.ad.TnkResultListener
import com.tnkfactory.tnkofferer.databinding.ActivityEmbedABinding

class EmbedActivityA : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityEmbedABinding = DataBindingUtil.setContentView(this, R.layout.activity_embed_a)

        val offerwall = TnkOfferwall(this)

        offerwall.load(object : TnkResultListener {
            override fun onSuccess() {
                binding.contents.addView(offerwall.getAdListView())
            }

            override fun onFail(error: TnkError) {
                MaterialAlertDialogBuilder(this@EmbedActivityA)
                    .setMessage(error.message)
                    .create()
                    .show()
            }

        })

    }
}