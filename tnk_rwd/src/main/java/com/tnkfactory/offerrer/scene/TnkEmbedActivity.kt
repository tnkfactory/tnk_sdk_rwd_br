package com.tnkfactory.offerrer.scene

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
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

    val binding: ActivityTnkEmbedBinding by lazy {
        ActivityTnkEmbedBinding.inflate(layoutInflater)
    }
    val offerwall: TnkOfferwall by lazy { TnkOfferwall(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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


    // appId 0이 아닐 경우 해당 Ad Campaign application Id의 광고를 상세 출력
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
