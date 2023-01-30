package com.tnkfactory.tnkofferer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tnkfactory.ad.AdWallActivity
import com.tnkfactory.tnkofferer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvContent.adapter = adapter
        binding.rvContent.layoutManager = LinearLayoutManager(this)


    }


    val adapter: CustomAdapter = CustomAdapter(arrayOf("activity", "inflaterA", "inflaterB"))

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
                    0 -> startActivity(Intent(this@MainActivity, AdWallActivity::class.java))
                    1 -> startActivity(Intent(this@MainActivity, EmbedActivityA::class.java))
                    2 -> startActivity(Intent(this@MainActivity, EmbedActivityB::class.java))
                    else -> {}
                }
            }

        }

        override fun getItemCount() = dataSet.size

    }


}