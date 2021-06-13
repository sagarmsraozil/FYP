package com.sagarmishra.futsal.fragments.booking_screens

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.sagarmishra.futsal.MainActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.BookingRepository
import com.sagarmishra.futsal.entityapi.Booking
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class SuccessBookingFragment : Fragment(),View.OnClickListener {
    private lateinit var tvFutsal:TextView
    private lateinit var tvCode:TextView
    private lateinit var tvDate:TextView
    private lateinit var tvDay:TextView
    private lateinit var tvTime : TextView
    private lateinit var tvPrice:TextView
    private lateinit var tvInitiator:TextView
    private lateinit var btnDone:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_success_booking, container, false)
        binding(view)
        initialize()
        btnDone.setOnClickListener(this)
        return view
    }

    private fun binding(v:View)
    {
        tvFutsal = v.findViewById(R.id.tvFutsal)
        tvCode = v.findViewById(R.id.tvCode)
        tvDate = v.findViewById(R.id.tvDate)
        tvDay = v.findViewById(R.id.tvDay)
        tvTime = v.findViewById(R.id.tvTime)
        tvPrice = v.findViewById(R.id.tvPrice)
        tvInitiator = v.findViewById(R.id.tvInitiator)
        btnDone = v.findViewById(R.id.btnDone)
    }


    private fun initialize()
    {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = BookingRepository()
                val response = repo.retrieveSingleRecord(StaticData.bookedInstanceId!!)
                if(response.success == true)
                {
                    val data:Booking = response.singleData!!
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
                    }
                    StaticData.bookedInstanceId = null
                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        var snk = Snackbar.make(tvDate,"${response.message}",Snackbar.LENGTH_INDEFINITE)
                        snk.setAction("OK",View.OnClickListener {
                            snk.dismiss()
                        })
                        snk.show()
                    }
                }
            }
            catch (ex:Exception)
            {
                withContext(Dispatchers.Main)
                {
                    var snk = Snackbar.make(tvDate,"${ex.toString()}",Snackbar.LENGTH_INDEFINITE)
                    snk.setAction("OK",View.OnClickListener {
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
                val intent = Intent(requireContext(),MainActivity::class.java)
                intent.putExtra("FRAGMENT_NUMBER",3)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

}