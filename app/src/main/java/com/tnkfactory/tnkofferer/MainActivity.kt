package com.tnkfactory.tnkofferer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tnkfactory.ad.TnkOfferwall
import com.tnkfactory.ad.TnkSession
import com.tnkfactory.ad.rwd.AdvertisingIdInfo
import com.tnkfactory.tnkofferer.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var offerwall: TnkOfferwall

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.rvContent.adapter = adapter
        binding.rvContent.layoutManager = LinearLayoutManager(this)

        // 1) TNK SDK 초기화
        offerwall = TnkOfferwall(this).apply {

            lifecycleScope.launch(Dispatchers.IO) {
                // 고유 아이디는 매체사에서 유저 식별을 위한 고유값을 사용하셔야 하며
                // 이 예제에서는 google adid를 사용 합니다.
                val adid = AdvertisingIdInfo.requestIdInfo(this@MainActivity) // backgroud thread 처리 필요

                // 2) 유저 식별값 설정
                setUserName(adid.id)
                // 3) COPPA 설정 (https://www.ftc.gov/business-guidance/privacy-security/childrens-privacy)
                setCOPPA(false)

                getEarnPoint() { point ->
                    binding.tvPoint.text = "받을 수 있는 포인트 : $point p"
                }
            }
        }
    }

    fun showOfferwallActivity() {
        // 4) 광고 목록 출력
        // 새 액티비티로 호출
        offerwall.startOfferwallActivity(this@MainActivity)
    }


    val adapter: CustomAdapter = CustomAdapter(arrayOf("activity", "inflaterA", "inflaterB", "customLayout"))

    inner class CustomAdapter(private val dataSet: Array<String>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var textView: TextView

            init {
                textView = view.findViewById(R.id.tv_name)
            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(R.layout.row_item, viewGroup, false)
            return ViewHolder(view)
        }


        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.textView.text = dataSet[position]
            viewHolder.itemView.setOnClickListener {
                when (position) {
                    0 -> showOfferwallActivity()
                    1 -> startActivity(Intent(this@MainActivity, EmbedActivityA::class.java))
                    2 -> startActivity(Intent(this@MainActivity, EmbedActivityB::class.java))
                    3 -> startActivity(Intent(this@MainActivity, CustomLayoutActivity::class.java))
                    else -> {}
                }
            }

        }

        override fun getItemCount() = dataSet.size

    }


}
