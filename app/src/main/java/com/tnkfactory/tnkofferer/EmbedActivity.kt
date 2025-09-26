package com.tnkfactory.tnkofferer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tnkfactory.ad.TnkError
import com.tnkfactory.ad.TnkOfferwall
import com.tnkfactory.ad.TnkResultListener
import com.tnkfactory.tnkofferer.databinding.ActivityEmbedBinding

class EmbedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityEmbedBinding = ActivityEmbedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val offerwall = TnkOfferwall(this)

        offerwall.load(object : TnkResultListener {
            override fun onSuccess() {
                binding.contents.addView(offerwall.getAdListView())
            }

            override fun onFail(error: TnkError) {
                MaterialAlertDialogBuilder(this@EmbedActivity)
                    .setMessage(error.message)
                    .create()
                    .show()
            }

        })

    }
}