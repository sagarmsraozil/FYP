package com.sagarmishra.futsal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.Repository.GiveawayRepository
import com.sagarmishra.futsal.adapter.GiveawayWrapperAdapter
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

@Suppress("Deprecation")
class GiveawayActivity : AppCompatActivity() {
    private lateinit var progressCount : TextView
    private lateinit var recycler : RecyclerView
    private lateinit var adapter : GiveawayWrapperAdapter
    var lstCode: MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_giveaway)
        supportActionBar!!.hide()
        binding()
        initialize()
    }

    private fun binding()
    {
        progressCount = findViewById(R.id.progressCount)
        recycler = findViewById(R.id.recycler)
    }

    private  fun initialize()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = GiveawayRepository()
                val response = repo.getOngoing()
                if(response.success == true)
                {
                    withContext(Dispatchers.Main)
                    {
                        progressCount.text = "${response.data} giveaway in progress"
                    }

                }
            }
            catch (ex:Exception)
            {
                println(ex.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    recycler.snackbar(ex.toString())
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = GiveawayRepository()
                val response = repo.getDistinctGiveaway()
                if(response.success == true)
                {
                    lstCode = response.data!!

                    adapter  = GiveawayWrapperAdapter(this@GiveawayActivity,this@GiveawayActivity,lstCode)
                    withContext(Dispatchers.Main)
                    {
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(this@GiveawayActivity)
                    }

                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        recycler.snackbar(response.message!!)
                    }
                }
            }
            catch (ex:Exception)
            {
                println(ex.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    recycler.snackbar(ex.toString())
                }
            }
        }
    }
}