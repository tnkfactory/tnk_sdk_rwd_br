package com.tnkfactory.offerrer.scene

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.tnkfactory.ad.TnkError
import com.tnkfactory.ad.TnkOfferwall
import com.tnkfactory.ad.TnkResultListener
import com.tnkfactory.offerrer.R
import com.tnkfactory.offerrer.databinding.ActivityTnkEmbedBinding

/**
 * @author hanago
 * @email hans@tnkfactory.com
 * @since 2023/04/24
 **/
class TnkEmbedActivity : AppCompatActivity() {

    val binding: ActivityTnkEmbedBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_tnk_embed) }
    val offerwall: TnkOfferwall by lazy { TnkOfferwall(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val progress = ProgressDialog(this, 0)
        progress.show()

        offerwall.load(object : TnkResultListener {
            override fun onFail(error: TnkError) {
                progress.dismiss()
                Toast.makeText(this@TnkEmbedActivity, "error : ${error.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess() {
                progress.dismiss()
                offerwall.getAdListView(appId).let {
                    binding.contents.addView(it)
                }
            }
        })
    }


    val appId: Long
        get() = intent.getLongExtra("appId", 0)

    companion object {
        @JvmStatic
        fun start(context: Context, appId: Long? = 0) {
            Intent(context, TnkEmbedActivity::class.java).apply {
                putExtra("appId", appId)
            }.run {
                context.startActivity(this)
            }
        }
    }
}