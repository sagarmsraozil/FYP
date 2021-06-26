package com.sagarmishra.futsal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.adapter.DotPricingAdapter
import com.sagarmishra.futsal.entityapi.DotsPricing
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

@Suppress("Deprecation")
class DotsPricingActivity : AppCompatActivity(),SwipeRefreshLayout.OnRefreshListener {
    private lateinit var swipe:SwipeRefreshLayout
    private lateinit var tvRecords:TextView
    private lateinit var etSearch:AutoCompleteTextView
    private lateinit var tvPrice:TextView
    private lateinit var recycler:RecyclerView
    private lateinit var adapter:DotPricingAdapter
    var dotsRecord:MutableList<DotsPricing> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dots_pricing)
        supportActionBar?.hide()
        binding()
        initialize()
        searchWork()
        swipe.setOnRefreshListener(this)
    }

    private fun binding()
    {
        swipe = findViewById(R.id.swipe)
        tvRecords = findViewById(R.id.tvRecords)
        etSearch = findViewById(R.id.etSearch)
        tvPrice = findViewById(R.id.tvPrice)
        recycler = findViewById(R.id.recycler)
    }

    private fun initialize()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = UserRepository()
                val response = repo.dotsPricing()
                if(response.success == true)
                {
                    dotsRecord = response.data!!
                    withContext(Dispatchers.Main)
                    {

                        var futsalNames = dotsRecord.map{
                            it.futsalInstance_id!!.futsal_id!!.futsalName
                        }.toMutableSet().toMutableList()

                        var price = 0
                        var prices = dotsRecord.map{
                            it.price
                        }
                        if(prices.size>0)
                        {
                            price = prices.reduce { acc, i ->
                                acc+i
                            }
                        }

                        tvPrice.text = "Overall Price: Rs "+price.toString()
                        tvRecords.text = "${dotsRecord.size} records"

                        var searchAdapter = ArrayAdapter(this@DotsPricingActivity,android.R.layout.simple_expandable_list_item_1,futsalNames)
                        etSearch.setAdapter(searchAdapter)
                        etSearch.threshold = 1

                        adapter = DotPricingAdapter(this@DotsPricingActivity,dotsRecord)
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(this@DotsPricingActivity)


                    }
                }
                else
                {
                    println(response.message!!)
                }
            }
            catch (ex:Exception)
            {
                println(ex.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    swipe.snackbar("Server Error!!")
                }
            }
        }
    }

    private fun searchWork()
    {
        etSearch.doOnTextChanged { text, start, before, count ->

            var searchedData = dotsRecord.filter{
                it.futsalInstance_id!!.futsal_id!!.futsalName!!.toLowerCase().trim().startsWith(text.toString().toLowerCase().trim())
            }

            adapter = DotPricingAdapter(this,searchedData.toMutableList())
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()


            if(searchedData.size > 0)
            {
                var price = 0
                var prices = searchedData.map{
                    it.price
                }
                if(prices.size>0)
                {
                    price = prices.reduce { acc, i ->
                        acc+i
                    }
                }
                tvPrice.text = "Overall Price: Rs "+price.toString()
            }




            if(text.toString().length > 0)
            {
                tvRecords.text = "${searchedData.size} records found."
            }
            else
            {
                tvRecords.text = "${dotsRecord.size} records."

                    var price = 0
                    var prices = dotsRecord.map{
                        it.price
                    }
                    if(prices.size>0)
                    {
                        price = prices.reduce { acc, i ->
                            acc+i
                        }
                    }
                    tvPrice.text = "Overall Price: Rs "+price.toString()

            }
        }
    }

    override fun onRefresh() {
        var intent = Intent(this,DotsPricingActivity::class.java)
        startActivity(intent)
        finish()
    }
}