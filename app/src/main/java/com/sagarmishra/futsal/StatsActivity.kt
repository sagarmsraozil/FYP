package com.sagarmishra.futsal

import android.app.Dialog
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.adapter.TitleAdapter
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.TeamStats
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class StatsActivity : AppCompatActivity(),OnChartValueSelectedListener,View.OnClickListener {
    private lateinit var tvTeamName:TextView
    private lateinit var spinner:Spinner
    private lateinit var ivTier:ImageView
    private lateinit var tvTierStats:TextView
    private lateinit var ivTitles:ImageView
    private lateinit var tvMatchPlayed:TextView
    private lateinit var tvWins:TextView
    private lateinit var tvDraw:TextView
    private lateinit var tvLoss:TextView
    private lateinit var tvWinPercent:TextView
    private lateinit var tvDrawPercent:TextView
    private lateinit var tvLossPercent:TextView
    private lateinit var tvGoalsScored:TextView
    private lateinit var tvGoalsConceaded:TextView
    private lateinit var tvRatio:TextView
    private lateinit var lineChart:LineChart
    var tid:String? = null
    var dialog:Dialog? = null

    //initialize container to add stats
    var lstStats:MutableList<TeamStats> = mutableListOf()
    var currentSeason :Int = 0
    var seasons:MutableList<Int> = mutableListOf()
    var pointsData:MutableList<Int> = mutableListOf()
    var titles:MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        supportActionBar?.hide()
        binding()
        initialize()
        ivTitles.setOnClickListener(this)
    }

    private fun binding()
    {
        tvTeamName = findViewById(R.id.tvTeamName)
        spinner = findViewById(R.id.spinner)
        ivTier = findViewById(R.id.ivTier)
        tvTierStats = findViewById(R.id.tvTierStats)
        ivTitles = findViewById(R.id.ivTitles)
        tvMatchPlayed = findViewById(R.id.tvMatchPlayed)
        tvWins = findViewById(R.id.tvWins)
        tvDraw = findViewById(R.id.tvDraw)
        tvLoss = findViewById(R.id.tvLoss)
        tvWinPercent = findViewById(R.id.tvWinPercent)
        tvDrawPercent = findViewById(R.id.tvDrawPercent)
        tvLossPercent = findViewById(R.id.tvLossPercent)
        tvGoalsConceaded = findViewById(R.id.tvGoalsConceaded)
        tvGoalsScored = findViewById(R.id.tvGoalsScored)
        tvRatio = findViewById(R.id.tvRatio)
        lineChart = findViewById(R.id.lineChart)

    }

    private fun makeTierViewer()
    {
        dialog = Dialog(this)
        dialog!!.setContentView(R.layout.dialog_title_viewer)
        dialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog!!.setCancelable(false)

        //binding
        var tvTitles : TextView = dialog!!.findViewById(R.id.tvTitles)
        var recycler : RecyclerView = dialog!!.findViewById(R.id.recycler)
        var ivCross : ImageView = dialog!!.findViewById(R.id.ivCross)

        var adapter : TitleAdapter = TitleAdapter(this,titles)
        tvTitles.text = "${titles.size} titles"
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)


        ivCross.setOnClickListener {
            dialog!!.dismiss()
        }


    }

    private fun setData()
    {
        var points:ArrayList<Entry> = arrayListOf()
        for(i in pointsData.indices)
        {
            points.add(Entry(i.toFloat(), pointsData[i].toFloat()))
        }

        var set1:LineDataSet = LineDataSet(points, "Points Tally")
        set1.setDrawIcons(false)
        // draw dashed line
        // draw dashed line
        set1.enableDashedLine(10f, 5f, 0f)

        // black lines and points

        // black lines and points
        set1.color = Color.BLACK
        set1.setCircleColor(Color.BLACK)



        // line thickness and point size
        set1.lineWidth = 1f
        set1.circleRadius = 3f

        // draw points as solid circles

        // draw points as solid circles
        set1.setDrawCircleHole(false)

        // customize legend entry

        // customize legend entry
        set1.formLineWidth = 1f
        set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        set1.formSize = 15f

        // text size of values

        // text size of values
        set1.valueTextSize = 9f

        // draw selection line as dashed

        // draw selection line as dashed
        set1.enableDashedHighlightLine(10f, 5f, 0f)

        set1.fillFormatter =
            IFillFormatter { dataSet, dataProvider -> lineChart.axisLeft.axisMinimum }

        // set color of filled area

        // set color of filled area
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            val drawable = ContextCompat.getDrawable(this, R.drawable.fade_red)
            set1.fillDrawable = drawable
        } else {
            set1.fillColor = Color.BLACK
        }
        set1.setDrawFilled(true)
        var data:LineData = LineData(set1)
        lineChart.data = data

    }

    private fun loadChart()
    {
        //background color
        lineChart.setBackgroundColor(Color.WHITE)
        //disable description text
        lineChart.description.isEnabled = false

        //enable touch gestures
        lineChart.setTouchEnabled(true)

        //set listeners
        lineChart.setOnChartValueSelectedListener(this)
        lineChart.setDrawGridBackground(false)

        //enable scrolling and dragging
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)

        // force pinch zoom along both axis
        lineChart.setPinchZoom(true)
        setData()
        lineChart.animateX(300)

        var legend:Legend = lineChart.legend
        legend.form = Legend.LegendForm.LINE
    }

    private fun loadData(season: Int)
    {
       var filterSeasonData = lstStats.filter{
           it.season == season
       }

       if(filterSeasonData.isNotEmpty())
       {
           var dataBox = filterSeasonData[0]
           tvMatchPlayed.text = dataBox.matchPlayed.toString()
           tvWins.text = dataBox.wins.toString()
           tvDraw.text = dataBox.draw.toString()
           tvLoss.text = dataBox.loss.toString()
           tvWinPercent.text = dataBox.winPercent.toString()+"%"
           tvLossPercent.text = dataBox.lossPercent.toString()+"%"
           tvDrawPercent.text = dataBox.drawPercent.toString()+"%"
           tvGoalsConceaded.text = dataBox.goalsConceaded.toString()
           tvGoalsScored.text = dataBox.goalsScored.toString()
           tvRatio.text = dataBox.ratio.toString()
           tvTierStats.text = "${dataBox.tier!!.tierNomenclature} (${dataBox.pointsCollected}/${(dataBox.pointsCollected-dataBox.pointsCollected%100)+100})"

           var imgPath = RetrofitService.loadImagePath()+dataBox.tier!!.tierLogo!!.replace(
               "\\",
               "/"
           )
           Glide.with(this).load(imgPath).into(ivTier)

           spinner.setSelection(seasons.indexOf(season))
            pointsData = dataBox.pointTree!!
           loadChart()
       }
    }

    private fun initialize()
    {
        tid = intent.getStringExtra("teamId")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = TeamRepository()
                val response = repo.teamStatsHistory(tid!!)
                if(response.success == true)
                {
                    lstStats = response.data!!
                    currentSeason = response.season
                    seasons = response.seasons!!

                    var inbuiltedSeason = seasons.map{
                        "Season $it"
                    }



                    withContext(Dispatchers.Main)
                    {
                        tvTeamName.text = response.data[0].team_id!!.teamName+"'s Stats"
                        titles = response.data[0].team_id!!.titles!!
                        var adapter = ArrayAdapter(
                            this@StatsActivity,
                            android.R.layout.simple_expandable_list_item_1,
                            inbuiltedSeason
                        )
                        spinner.adapter = adapter
                        loadData(currentSeason)
                        makeTierViewer()
                    }

                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        spinner.snackbar(response.message!!)
                    }
                }
            }
            catch (err: Exception)
            {
                println(err.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    spinner.snackbar("Server Error!!")
                }
            }
        }

        spinner.onItemSelectedListener =
            object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    var selectedData = parent!!.getItemAtPosition(position).toString().split(" ")[1]

                    loadData(selectedData.toInt())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

    override fun onNothingSelected() {

    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.ivTitles ->{
                dialog!!.show()
            }
        }
    }
}