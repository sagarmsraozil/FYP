package com.sagarmishra.futsal.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.sagarmishra.futsal.MainActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.BookingRepository
import com.sagarmishra.futsal.UserEditActivity
import com.sagarmishra.futsal.adapter.ExpiredBookingAdapter
import com.sagarmishra.futsal.adapter.MyBookingsAdapter
import com.sagarmishra.futsal.adapter.OfflineExpiredAdapter
import com.sagarmishra.futsal.adapter.OfflineNonExpiredAdapter
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.db.FutsalRoomDB
import com.sagarmishra.futsal.entityapi.Booking
import com.sagarmishra.futsal.entityapi.BookingRoomDB
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.*
import java.lang.Exception


class MyBookingsFragment : Fragment(),View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {
    private lateinit var tvQueue : TextView
    private lateinit var recycler : RecyclerView
    private lateinit var recycler2 : RecyclerView
    private lateinit var adapter : MyBookingsAdapter
    private lateinit var adapter2 : ExpiredBookingAdapter
    var myBookings:MutableList<Booking> = mutableListOf()
 //   var offlineData:MutableList<BookingRoomDB> = mutableListOf()
    private lateinit var tvRecords:TextView
    private lateinit var etSearch:AutoCompleteTextView
    private lateinit var swipe:SwipeRefreshLayout
    var nonExpired : List<Booking> = listOf()
    var expired : List<Booking> = listOf()
    var mySearchList: MutableList<String?> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_bookings, container, false)
        binding(view)

        initialize()
        searchListener()
        swipe.setOnRefreshListener(this)
        return view
    }

    private fun binding(v:View)
    {
        tvQueue = v.findViewById(R.id.tvQueue)
        recycler = v.findViewById(R.id.recycler)
        recycler2 = v.findViewById(R.id.recycler2)
        tvRecords = v.findViewById(R.id.tvRecords)
        etSearch = v.findViewById(R.id.etSearch)
        swipe = v.findViewById(R.id.swipe)

    }

//    private fun getBookingRoom(data:MutableList<Booking>):MutableList<BookingRoomDB>
//    {
//        var nonExpiredData:MutableList<BookingRoomDB> = mutableListOf()
//        for(i in data)
//        {
//            var price = 0
//            if(i.futsalInstance_id!!.superPrice>0)
//            {
//                price = i.futsalInstance_id!!.superPrice
//            }
//            else
//            {
//                price =  i.futsalInstance_id!!.price
//            }
//            var bookingObj = BookingRoomDB(_id = i._id,futsal_id = i.futsalInstance_id!!.futsal_id!!._id,user_id = i.user_id!!._id,time = i.futsalInstance_id.time,date = i.futsalInstance_id.date,day = i.futsalInstance_id.day,price = price,expired = i.expired,futsalName = i.futsalInstance_id.futsal_id!!.futsalName)
//            nonExpiredData.add(bookingObj)
//        }
//        return nonExpiredData
//    }

    private fun initialize()
    {
        if(RetrofitService.online == true) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = BookingRepository()
                    val response = repo.retrieveBookings()
                    if (response.success == true) {
                        myBookings = response.data!!
                        StaticData.markAndInstance = response.markAndInstance

//                    for(myBooking in myBookings){
//                        myBooking.futsalInstance_id.futsal_id._id;
//                    }
                        val instance =
                            FutsalRoomDB.getFutsalDBInstance(requireContext()).getBookingDAO()
                        instance.deleteBookings()
                        getExpiryAndNonExpire()

                        mySearchList= mySearchList()

                        var overallData:MutableList<Booking> = mutableListOf()
                        if (nonExpired.size >= 5) {
                            var remainTime = nonExpired.slice(0..4).toMutableList()
                           // instance.insertBookingRecords(getBookingRoom(remainTime))
                            overallData.addAll(remainTime)
                        } else {
                            //instance.insertBookingRecords(getBookingRoom(nonExpired.toMutableList()))
                            overallData.addAll(nonExpired)
                        }
                        if (expired.size >= 5) {
                            var remainTime = expired.slice(0..4).toMutableList()
                          //  instance.insertBookingRecords(getBookingRoom(remainTime))
                            overallData.addAll(remainTime)
                        } else {
                            //instance.insertBookingRecords(getBookingRoom(expired.toMutableList()))
                            overallData.addAll(expired)
                        }

                        // adding values to denormalized columns in entity
                        for(i in overallData)
                        {

                            i.offlineFutsalName = i.futsalInstance_id!!.futsal_id!!.futsalName
                            i.offlineDate = i.futsalInstance_id!!.date
                            i.offlineDay = i.futsalInstance_id!!.day
                            i.offlineTime = i.futsalInstance_id!!.time
                            if(i.futsalInstance_id!!.superPrice > 0)
                            {
                                i.offlinePrice = i.futsalInstance_id!!.superPrice
                            }
                            else
                            {
                                i.offlinePrice = i.futsalInstance_id!!.price
                            }

                        }

                        instance.deleteAllBookings()
                        instance.insertBookings(overallData)

                        withContext(Dispatchers.Main)
                        {
                            loadData()

                        }
                    } else {
                        withContext(Dispatchers.Main)
                        {
                            recycler.snackbar("${response.message}")
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main)
                    {
                        recycler.snackbar("${ex.toString()}")
                    }
                }
            }
        }
        else
        {
            CoroutineScope(Dispatchers.IO).launch {
                var instance = FutsalRoomDB.getFutsalDBInstance(requireContext()).getBookingDAO()
               // offlineData = instance.retrieveBookings()
                myBookings = instance.retrieveMyBookings()
                getExpiryAndNonExpire()
                withContext(Dispatchers.Main)
                {
                    loadData()
                }

//                var expiredData = offlineData.filter {
//                    it.expired == true
//                }
//
//                var nonExpiredData = offlineData.filter {
//                    it.expired == false
//                }
//
//                withContext(Dispatchers.Main)
//                {
//                    tvQueue.text = "${nonExpiredData.size} matches in queue"
//                    tvRecords.text = "${expiredData.size} records"
//                    var adapter = OfflineNonExpiredAdapter(requireContext(),nonExpiredData.toMutableList())
//                    var adapter2 = OfflineExpiredAdapter(requireContext(),expiredData.toMutableList())
//                    recycler.adapter = adapter
//                    recycler2.adapter = adapter2
//                    recycler.layoutManager = LinearLayoutManager(requireContext())
//                    recycler2.layoutManager = LinearLayoutManager(requireContext())
//
//                }
            }
        }

    }

    private fun getExpiryAndNonExpire()
    {
        nonExpired = myBookings.filter {
            it.expired == false
        }
        expired = myBookings.filter {
            it.expired == true
        }
    }

    private fun addAutoSearchAdapter()
    {
        var adapter3 = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_expandable_list_item_1,
            mySearchList
        )
        etSearch.setAdapter(adapter3)
        etSearch.threshold = 1
    }

    private fun loadData()
    {
        tvQueue.text = "${nonExpired.size} matches in queue"
        tvRecords.text = "${expired.size} records"
        adapter =
            MyBookingsAdapter(requireActivity(),requireContext(), nonExpired.toMutableList())
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        adapter2 =
            ExpiredBookingAdapter(requireContext(), expired.toMutableList())
        recycler2.layoutManager = LinearLayoutManager(requireContext())
        recycler2.adapter = adapter2
        if(RetrofitService.online == true)
        {
            addAutoSearchAdapter()
        }

    }

    private fun mySearchList():MutableList<String?>
    {
        var mySearchList: MutableList<String?> = mutableListOf()

            if(myBookings.size > 0)
            {
                var futsals = myBookings.map {
                    it.futsalInstance_id!!.futsal_id!!.futsalName
                }
                var bookingCodes = myBookings.map {
                    it.bookingCode
                }
                var futsals2: MutableList<String?> = futsals.toMutableSet().toMutableList()
                var bookingCodes2: MutableList<String?> = bookingCodes.toMutableSet().toMutableList()

                for (i in futsals2) {
                    mySearchList.add(i)
                }
                for (i in bookingCodes2) {
                    mySearchList.add(i)
                }
            }







        return mySearchList
    }

    private fun searchListener()
    {
        try {
            etSearch.doOnTextChanged { text, start, before, count ->
                if (RetrofitService.online == true) {
                    var search = etSearch.text.toString()
                    if (search.isNotEmpty()) {

                        var searched = myBookings.filter {
                            it.futsalInstance_id!!.futsal_id!!.futsalName!!.toLowerCase()
                                .startsWith(search.trim().toLowerCase()) || it.bookingCode!!.startsWith(
                                search.trim()
                            )
                        }
                        var nonExpiredSearch = searched.filter {
                            it.expired == false
                        }
                        var expiredSearch = searched.filter {
                            it.expired == true
                        }

                        tvQueue.text = "${nonExpiredSearch.size} matches searched"
                        tvRecords.text = "${expiredSearch.size} records searched"

                        adapter = MyBookingsAdapter(requireActivity(),requireContext(), nonExpiredSearch.toMutableList())
                        recycler.adapter = adapter
                        adapter.notifyDataSetChanged()

                        adapter2 =
                            ExpiredBookingAdapter(requireContext(), expiredSearch.toMutableList())
                        recycler2.adapter = adapter2
                        adapter2.notifyDataSetChanged()

                    } else {
                        getExpiryAndNonExpire()
                        loadData()
                    }
                }

            }
        }

        catch (ex:Exception)
        {
            recycler2.snackbar("Refresh to continue")
        }


    }

    override fun onClick(v: View?) {


    }

    override fun onRefresh() {
        var intent = Intent(requireContext(),MainActivity::class.java)
        intent.putExtra("FRAGMENT_NUMBER",3)
        startActivity(intent)
        requireActivity().finish()
    }
}