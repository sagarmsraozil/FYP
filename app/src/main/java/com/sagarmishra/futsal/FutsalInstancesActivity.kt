package com.sagarmishra.futsal

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sagarmishra.futsal.Repository.FutsalRepository
import com.sagarmishra.futsal.adapter.InstanceAdapter
import com.sagarmishra.futsal.adapter.WeekAdapter
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.db.FutsalRoomDB
import com.sagarmishra.futsal.entityapi.Futsal
import com.sagarmishra.futsal.entityapi.FutsalInstances
import com.sagarmishra.futsal.entityapi.FutsalRating
import com.sagarmishra.futsal.interfaces.RefreshInstance
import com.sagarmishra.futsal.model.StaticData
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FutsalInstancesActivity : AppCompatActivity(),RefreshInstance,SwipeRefreshLayout.OnRefreshListener {
    private lateinit var tvFutsalName: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvContact: TextView
    private lateinit var tvSize: TextView
    private lateinit var tvGrounds: TextView
    private lateinit var tvOpening: TextView
    private lateinit var tvCloses: TextView
    private lateinit var tvMandD: TextView
    private lateinit var tvEandN: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvRating: TextView
    private lateinit var recyclerDays: RecyclerView
    private lateinit var recyclerTime: RecyclerView
    private lateinit var adapter: WeekAdapter
    private lateinit var adapter2: InstanceAdapter
    private lateinit var star1:ImageView
    private lateinit var star2:ImageView
    private lateinit var star3:ImageView
    private lateinit var star4:ImageView
    private lateinit var star5:ImageView
    private lateinit var userRating:RatingBar
    private lateinit var swipeRefresh:SwipeRefreshLayout
    private lateinit var ivFutsal:ImageView
    var lstInstance: MutableList<FutsalInstances> = mutableListOf()
    var stars:MutableList<ImageView> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_futsal_instances)
        supportActionBar?.hide()
        binding()
        initialize()
        listener()

    }

    private fun binding() {
        tvFutsalName = findViewById(R.id.tvFutsalName)
        userRating = findViewById(R.id.userRating)
        recyclerDays = findViewById(R.id.recyclerDays)
        recyclerTime = findViewById(R.id.recyclerTime)
        tvLocation = findViewById(R.id.tvLocation)
        tvContact = findViewById(R.id.tvContact)
        tvSize = findViewById(R.id.tvSize)
        tvGrounds = findViewById(R.id.tvGrounds)
        tvOpening = findViewById(R.id.tvOpening)
        tvCloses = findViewById(R.id.tvCloses)
        tvMandD = findViewById(R.id.tvMandD)
        tvEandN = findViewById(R.id.tvEandN)
        tvDescription = findViewById(R.id.tvDescription)
        tvRating = findViewById(R.id.tvRating)
        star1=findViewById(R.id.star1)
        star2=findViewById(R.id.star2)
        star3=findViewById(R.id.star3)
        star4=findViewById(R.id.star4)
        star5=findViewById(R.id.star5)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        ivFutsal = findViewById(R.id.ivFutsal)

    }

    private fun initialize() {
        stars = mutableListOf(star1,star2,star3,star4,star5)
        val intentFutsal: Futsal? = intent.getParcelableExtra("futsal")
        tvFutsalName.text = intentFutsal!!.futsalName

        tvLocation.text = intentFutsal!!.location
        tvContact.text = intentFutsal!!.contact
        tvSize.text = intentFutsal!!.size
        tvGrounds.text = intentFutsal!!.grounds
        tvOpening.text = intentFutsal!!.openingTime.toString()+"AM"
        tvCloses.text = intentFutsal!!.closingTime.toString()+"PM"
        tvMandD.text = "Rs ${intentFutsal!!.morningPrice}/${intentFutsal!!.dayPrice}"
        tvEandN.text = "Rs ${intentFutsal!!.eveningPrice}/${intentFutsal!!.nightPrice}"
        tvDescription.text = intentFutsal!!.futsalDescription
        tvRating.text = "${intentFutsal!!.rating}/5"
        var imagePath = RetrofitService.loadImagePath()+intentFutsal!!.futsalImage
        imagePath = imagePath.replace("\\","/")
        Glide.with(this@FutsalInstancesActivity).load(imagePath).into(ivFutsal)
        adapter = WeekAdapter(this@FutsalInstancesActivity, StaticData.makeDateAndTime(), this)
        recyclerDays.adapter = adapter
        recyclerDays.layoutManager =
            LinearLayoutManager(this@FutsalInstancesActivity, LinearLayoutManager.HORIZONTAL, false)

        adapter2 = InstanceAdapter(this@FutsalInstancesActivity, lstInstance)
        recyclerTime.adapter = adapter2
        recyclerTime.layoutManager = LinearLayoutManager(this@FutsalInstancesActivity)


            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = FutsalRepository()

                    val response = repo.myRatings(StaticData.futsalId)
                    if(response.success == true)
                    {
                        userRating.rating = response.data!!.rating.toFloat()
                    }

                }
                catch (ex:java.lang.Exception)
                {
                    var snk = Snackbar.make(star1,"${ex}",Snackbar.LENGTH_INDEFINITE)
                    snk.setAction("OK", View.OnClickListener {
                        snk.dismiss()
                    })
                    snk.show()
                    println(ex.printStackTrace())
                }
            }


            




        starRating(intentFutsal!!.rating)


    }

    private fun starRating(ratings:Int)
    {
        for(i in stars.indices)
        {
            stars[i].setColorFilter(Color.parseColor("#020006"))
        }
        if(ratings > 0)
        {
            for(i in 1..ratings)
            {
                stars[i-1].setColorFilter(Color.parseColor("#F5FA05"))
            }
        }
    }

    private fun listener()
    {
        swipeRefresh.setOnRefreshListener(this)
            userRating.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                    if(RetrofitService.online == true) {
                        CoroutineScope(Dispatchers.IO).launch {

                            try {
                                val repo = FutsalRepository()
                                val response = repo.rateFutsal(StaticData.futsalId, rating)
                                if (response.success == true) {

                                    try {

                                        val repo1 = FutsalRepository()
                                        val response1 = repo1.singleFutsal(StaticData.futsalId)
                                        if (response1.success == true) {
                                            withContext(Dispatchers.Main)
                                            {
                                                starRating(response1.data!!.rating)
                                                tvRating.text = "${response1.data!!.rating}/5"
                                            }

                                            FutsalRoomDB.getFutsalDBInstance(this@FutsalInstancesActivity)
                                                .getFutsalDAO().updateInstance(response1.data!!)
                                        }
                                    } catch (ex: Exception) {
                                        var snk = Snackbar.make(
                                            star1,
                                            "${ex}",
                                            Snackbar.LENGTH_INDEFINITE
                                        )
                                        snk.setAction("OK", View.OnClickListener {
                                            snk.dismiss()
                                        })
                                        snk.show()
                                        println(ex.printStackTrace())
                                    }
                                } else {
                                    withContext(Dispatchers.Main)
                                    {
                                        var snk = Snackbar.make(
                                            star1,
                                            "${response.message}",
                                            Snackbar.LENGTH_INDEFINITE
                                        )
                                        snk.setAction("OK", View.OnClickListener {
                                            snk.dismiss()
                                        })
                                        snk.show()
                                    }

                                }
                            } catch (ex: Exception) {
                                withContext(Dispatchers.Main)
                                {

                                    var snk = Snackbar.make(
                                        star1,
                                        "${ex.toString()}",
                                        Snackbar.LENGTH_INDEFINITE
                                    )
                                    snk.setAction("OK", View.OnClickListener {
                                        snk.dismiss()
                                    })
                                    snk.show()
                                    println(ex.printStackTrace())
                                }

                            }
                        }
                    }
                else
                    {
                        val snk = Snackbar.make(tvLocation,"Please connect to internet for further proceeding.",
                            Snackbar.LENGTH_INDEFINITE)
                        snk.setAction("Ok", View.OnClickListener {
                            snk.dismiss()
                        })
                        snk.show()
                    }

            }

    }


    override fun refreshInstances(position: Int, lstInstance: MutableList<FutsalInstances>) {
        adapter2 = InstanceAdapter(this@FutsalInstancesActivity,lstInstance)
        recyclerTime.adapter = adapter2
        adapter2.notifyDataSetChanged()

    }

    override fun onRefresh() {
        val intentFutsal: Futsal? = intent.getParcelableExtra("futsal")
        StaticData.futsalId = intentFutsal!!._id
        val intent = Intent(this@FutsalInstancesActivity,FutsalInstancesActivity::class.java)
        intent.putExtra("futsal",intentFutsal)
        startActivity(intent)
        finish()
    }
}