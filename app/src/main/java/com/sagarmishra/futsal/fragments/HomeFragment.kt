package com.sagarmishra.futsal.fragments

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.sagarmishra.futsal.MapsActivity
import com.sagarmishra.futsal.Permissions.Logout
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.AdsRepository
import com.sagarmishra.futsal.Repository.FutsalRepository
import com.sagarmishra.futsal.adapter.FutsalAdapter
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.db.FutsalRoomDB
import com.sagarmishra.futsal.entityapi.Futsal
import com.sagarmishra.futsal.notification.NotificationSender
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.*
import java.lang.Exception


class HomeFragment : Fragment(),View.OnClickListener,SensorEventListener {
    private lateinit var recycler:RecyclerView
    private lateinit var linear : LinearLayout
    private lateinit var myCarousel:ImageSlider
    var lstFutsal:MutableList<Futsal> = mutableListOf()
    private lateinit var adapter:FutsalAdapter
    private lateinit var floatMap:FloatingActionButton
    private lateinit var etSearch : AutoCompleteTextView
    private lateinit var tvSearchData : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recycler = view.findViewById(R.id.recycler)
        linear = view.findViewById(R.id.linear)
        floatMap = view.findViewById(R.id.floatMap)
        myCarousel = view.findViewById(R.id.myCarousel)
        etSearch = view.findViewById(R.id.etSearch)
        tvSearchData = view.findViewById(R.id.tvSearchData)
        floatMap.setOnClickListener(this)




        initializeCarousel()
        if(RetrofitService.online == true)
        {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val futsalRepository = FutsalRepository()
                    val response = futsalRepository.showFutsals()
                    if(response.success == true)
                    {
                        withContext(Dispatchers.Main)
                        {
                            lstFutsal = response.data!!
                            val instance = FutsalRoomDB.getFutsalDBInstance(requireContext())
                            instance.getFutsalDAO().deleteAll()
                            instance.getFutsalDAO().insertFutsals(lstFutsal)
                            adapter = FutsalAdapter(requireContext(),lstFutsal)
                            recycler.adapter = adapter
                            recycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                        }

                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            val snk = Snackbar.make(linear,"${response.message}",Snackbar.LENGTH_INDEFINITE)
                            snk.setAction("Ok",View.OnClickListener {
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
                        val snk = Snackbar.make(linear,"${ex}",Snackbar.LENGTH_INDEFINITE)
                        snk.setAction("Ok",View.OnClickListener {
                            snk.dismiss()
                        })
                        snk.show()
                    }


                }
            }
        }
        else
        {
            CoroutineScope(Dispatchers.IO).launch {
                val instance = FutsalRoomDB.getFutsalDBInstance(requireContext())
                lstFutsal = instance.getFutsalDAO().retrieveFutsals()
                withContext(Dispatchers.Main)
                {
                    adapter = FutsalAdapter(requireContext(),lstFutsal)
                    recycler.adapter = adapter
                    recycler.layoutManager=LinearLayoutManager(requireContext())
                }
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(500)
            var lstFutsalNames : List<String> = mutableListOf()
            if(lstFutsal.size > 0)
            {
                lstFutsalNames = lstFutsal.map {
                    it.futsalName!!
                }
            }
            var adapter2 = ArrayAdapter(requireContext(),android.R.layout.simple_expandable_list_item_1,lstFutsalNames)
            etSearch.setAdapter(adapter2)
            etSearch.threshold = 1
        }

        searchListener()



        return view
    }

    private fun initializeCarousel()
    {
        var sliders:MutableList<SlideModel> = mutableListOf()
        if(RetrofitService.online == true)
        {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = AdsRepository()
                    val response = repo.retrieveAds()
                    if(response.success == true)
                    {
                        val fetchedData = response.data
                        for(i in fetchedData!!)
                        {
                            var imagePath = RetrofitService.loadImagePath()+i.image
                            imagePath=imagePath.replace("\\","/")
                            sliders.add(SlideModel(imagePath))
                        }
                        withContext(Dispatchers.Main)
                        {
                            myCarousel.setImageList(sliders,ScaleTypes.FIT)
                        }
                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            linear.snackbar("${response.message}")
                        }
                    }
                }
                catch (ex:Exception)
                {
                    withContext(Dispatchers.Main)
                    {
                        linear.snackbar("Problem retrieving ads!!")
                    }
                }
            }
        }


    }

    private fun searchListener()
    {
        etSearch.doOnTextChanged { text, start, before, count ->
            if(etSearch.text.toString().length > 0)
            {
                var searchList = lstFutsal.filter{
                    it.futsalName!!.toLowerCase().startsWith(etSearch.text.toString().toLowerCase().trim()) || it.location!!.toLowerCase().startsWith(etSearch.text.toString().toLowerCase().trim())
                }
                adapter = FutsalAdapter(requireContext(),searchList.toMutableList())
                recycler.adapter = adapter
                adapter.notifyDataSetChanged()
                if(searchList.size > 0)
                {
                    tvSearchData.visibility = View.GONE
                }
                else
                {
                    tvSearchData.visibility = View.VISIBLE
                }
            }
            else
            {
                adapter = FutsalAdapter(requireContext(),lstFutsal)
                recycler.adapter = adapter
                adapter.notifyDataSetChanged()
                tvSearchData.visibility = View.GONE
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.floatMap ->{
                if(RetrofitService.token != null)
                {
                    var lstFutsals:ArrayList<Futsal> = arrayListOf()
                    for(i in lstFutsal)
                    {
                        lstFutsals.add(i)
                    }
                    val intent = Intent(context,MapsActivity::class.java)
                    intent.putExtra("futsals",lstFutsals)
                    startActivity(intent)
                }


            }




        }
    }

    override fun onSensorChanged(event: SensorEvent?) {

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}