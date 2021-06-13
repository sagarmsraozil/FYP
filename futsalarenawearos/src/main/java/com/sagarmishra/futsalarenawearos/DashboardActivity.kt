package com.sagarmishra.futsalarenawearos

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsalarenawearos.adapter.BookingAdapter
import com.sagarmishra.futsalarenawearos.entity.Booking

import com.sagarmishra.futsalarenawearos.repository.BookingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("Deprecation")
class DashboardActivity : WearableActivity() {
    private lateinit var recycler : RecyclerView
    private lateinit var adapter : BookingAdapter
    private lateinit var tvCount:TextView
    var nonExpired : List<Booking> = listOf()
    var expired : List<Booking> = listOf()
    var myBookings:MutableList<Booking> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        recycler = findViewById(R.id.recycler)
        tvCount = findViewById(R.id.tvCount)
        loadData()
        // Enables Always-on
        setAmbientEnabled()
    }

    private fun loadData()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = BookingRepository()
                val response = repo.getBookings()
                if(response.success == true)
                {
                    myBookings = response.data!!
                    nonExpired = myBookings.filter {
                        it.expired == false
                    }

                    expired = myBookings.filter {
                        it.expired == true
                    }

                    if(nonExpired.isNotEmpty())
                    {
                        adapter = BookingAdapter(this@DashboardActivity,nonExpired.toMutableList())
                    }
                    else
                    {
                        if(expired.size > 5)
                        {
                            expired = expired.slice(0..4)

                        }
                        adapter = BookingAdapter(this@DashboardActivity,expired.toMutableList())
                    }

                    withContext(Dispatchers.Main)
                    {

                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(this@DashboardActivity,LinearLayoutManager.VERTICAL,false)
                        tvCount.text = "${myBookings.size} records"
                    }

                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        recycler.snackbar("${response.message}")
                    }
                }
            }
            catch (ex:Exception)
            {
                println(ex.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    recycler.snackbar("Server Issue..")
                }
            }
        }
    }
}