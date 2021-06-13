package com.sagarmishra.futsal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.sagarmishra.futsal.Repository.BookingRepository
import com.sagarmishra.futsal.entityapi.Booking
import com.sagarmishra.futsal.model.StaticData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MyBookingDetailsActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var tvFutsal: TextView
    private lateinit var tvCode: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvDay: TextView
    private lateinit var tvTime : TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvInitiator: TextView
    private lateinit var btnDone: Button
    private lateinit var tvLocation:TextView
    private lateinit var tvBooked:TextView
    private lateinit var tvBookedTime : TextView
    private lateinit var tvInitiator2 : TextView
    private lateinit var tvExpired : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_booking_details)
        supportActionBar?.hide()
        binding()
        initialize()
        btnDone.setOnClickListener(this)
    }

    private fun binding()
    {
        tvFutsal = findViewById(R.id.tvFutsal)
        tvCode = findViewById(R.id.tvCode)
        tvDate = findViewById(R.id.tvDate)
        tvDay = findViewById(R.id.tvDay)
        tvTime = findViewById(R.id.tvTime)
        tvPrice = findViewById(R.id.tvPrice)
        tvInitiator = findViewById(R.id.tvInitiator)
        btnDone = findViewById(R.id.btnDone)
        tvLocation = findViewById(R.id.tvLocation)
        tvBooked = findViewById(R.id.tvBooked)
        tvBookedTime = findViewById(R.id.tvBookedTime)
        tvInitiator2 = findViewById(R.id.tvInitiator2)
        tvExpired = findViewById(R.id.tvExpired)
    }

    private fun initialize()
    {
        var bookingId = intent.getStringExtra("record")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = BookingRepository()
                val response = repo.retrieveSingleRecord(bookingId!!)
                if(response.success == true)
                {
                    val data: Booking = response.singleData!!
                    withContext(Dispatchers.Main)
                    {
                        tvFutsal.text = data.futsalInstance_id!!.futsal_id!!.futsalName
                        tvCode.text = data.bookingCode
                        tvDate.text = data.futsalInstance_id.date
                        tvDay.text = data.futsalInstance_id.day
                        tvTime.text = data.futsalInstance_id.time
                        if(data.futsalInstance_id.superPrice > 0)
                        {
                            tvPrice.text = "Rs "+data.futsalInstance_id.superPrice
                        }
                        else
                        {
                            tvPrice.text = "Rs "+data.futsalInstance_id.price
                        }
                        tvInitiator.text = data.mobileNumber
                        tvLocation.text = data.futsalInstance_id.futsal_id!!.location
                        tvBooked.text = data.booked_date2
                        tvBookedTime.text = data.booked_time
                        tvInitiator2.text = data.teamMate_mobileNumber
                        tvExpired.text = data.expired.toString()
                    }

                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        var snk = Snackbar.make(tvDate,"${response.message}", Snackbar.LENGTH_INDEFINITE)
                        snk.setAction("OK", View.OnClickListener {
                            snk.dismiss()
                        })
                        snk.show()
                    }
                }
            }
            catch (ex: Exception)
            {
                withContext(Dispatchers.Main)
                {
                    var snk = Snackbar.make(tvDate,"${ex.toString()}", Snackbar.LENGTH_INDEFINITE)
                    snk.setAction("OK", View.OnClickListener {
                        snk.dismiss()
                    })
                    snk.show()
                }
                ex.printStackTrace()

            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnDone ->{
                val intent = Intent(this@MyBookingDetailsActivity,MainActivity::class.java)
                intent.putExtra("FRAGMENT_NUMBER",3)
                startActivity(intent)
                finish()
            }
        }
    }
}