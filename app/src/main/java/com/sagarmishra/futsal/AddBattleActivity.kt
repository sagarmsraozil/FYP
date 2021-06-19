package com.sagarmishra.futsal

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.sagarmishra.futsal.Repository.FutsalRepository
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.entityapi.Battle
import com.sagarmishra.futsal.entityapi.Futsal
import com.sagarmishra.futsal.entityapi.Team
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*

class AddBattleActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var spinnerFutsal:SearchableSpinner
    private lateinit var spinnerTeam:SearchableSpinner
    private lateinit var etDate:TextInputEditText
    private lateinit var etTime:TextInputEditText
    private lateinit var etDescription:TextInputEditText
    private lateinit var cbCheck:CheckBox
    private lateinit var btnAdd:Button
    private lateinit var dateBox:DatePickerDialog
    private lateinit var tvTimeFormat:TextView
    private lateinit var layout:LinearLayout
    var lstFutsals:MutableList<Futsal> = mutableListOf()
    var lstTeams:MutableList<Team> = mutableListOf()
    var lstNotMyTeam:MutableList<Team> = mutableListOf()
    var timeMapping:MutableMap<String,Int> = mutableMapOf(
        "13" to 1,"14" to 2,"15" to 3,"16" to 4,"17" to 5, "18" to 6, "19" to 7,"20" to 8,"21" to 9, "22" to 10, "23" to 11
    )
    var futsalId:String? =""
    var teamId:String? =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_battle)
        supportActionBar?.hide()
        binding()
        listeners()
        initializeDate()
        initialize()
        timeNotifier()
        spinnerWork()
    }

    private fun binding()
    {
        spinnerFutsal = findViewById(R.id.spinnerFutsal)
        spinnerTeam = findViewById(R.id.spinnerTeam)
        etDate = findViewById(R.id.etDate)
        etTime = findViewById(R.id.etTime)
        etDescription = findViewById(R.id.etDescription)
        cbCheck = findViewById(R.id.cbCheck)
        btnAdd = findViewById(R.id.btnAdd)
        tvTimeFormat = findViewById(R.id.tvTimeFormat)
        layout = findViewById(R.id.layout)

    }

    private fun alertMessage(title:String,message:String)
    {
        var alert = AlertDialog.Builder(this@AddBattleActivity)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("OK"){ dialogInterface: DialogInterface, i: Int ->

        }
        var alertt = alert.create()
        alertt.setCancelable(false)
        alertt.show()
    }

    private fun listeners()
    {
        btnAdd.setOnClickListener(this)
        etDate.setOnClickListener(this)
        cbCheck.setOnClickListener(this)
    }

    private fun initialize()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = FutsalRepository()
                val response = repo.showFutsals()
                val repo2 = TeamRepository()
                val response2 = repo2.fetchEveryTeams()
                if(response.success == true)
                {
                    lstFutsals = response.data!!
                    withContext(Dispatchers.Main)
                    {
                        var lstFutsalName = lstFutsals.map {
                            it.futsalName!!+"(${it.futsalCode})"
                        }.toMutableList()
                        lstFutsalName.add(0,"Select A Futsal")
                        var futsalAdapter = ArrayAdapter(this@AddBattleActivity,android.R.layout.simple_expandable_list_item_1,lstFutsalName)
                        spinnerFutsal.adapter = futsalAdapter
                        spinnerFutsal.setTitle("Select A Futsal")
                    }
                }

                if(response2.success == true)
                {
                    lstTeams = response2.data!!

                    lstNotMyTeam = lstTeams.filter{
                        it._id != StaticData.team!!._id
                    }.toMutableList()

                    var teamNames = lstNotMyTeam.map{
                        it.teamName!!+"(${it.teamCode})"
                    }.toMutableList()
                    teamNames.add(0,"Select A Team")

                    withContext(Dispatchers.Main)
                    {

                        var teamAdapter = ArrayAdapter(this@AddBattleActivity,android.R.layout.simple_expandable_list_item_1,teamNames)
                        spinnerTeam.adapter = teamAdapter
                        spinnerTeam.setTitle("Select A Team")
                    }
                }
            }
            catch (ex:Exception)
            {
                println(ex.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    etTime.snackbar("Server Error!!")
                }
            }
        }
    }

    private fun digitizer(num:Int):String
    {
        var number = num.toString()
        if(number.toInt() < 10)
        {
            number= "0$number"
        }
        return number
    }


    private fun initializeDate()
    {
        val cal = Calendar.getInstance()
        var years = cal.get(Calendar.YEAR)
        var month = cal.get(Calendar.MONTH)
        var day = cal.get(Calendar.DATE)

        val cal2 = Calendar.getInstance()
        cal2.add(Calendar.DATE,7)

        dateBox = DatePickerDialog(this@AddBattleActivity,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            etDate.setText("${year}-${digitizer(month+1)}-${digitizer(dayOfMonth)}")
        },years,month,day)

        dateBox.datePicker.minDate = cal.timeInMillis
        dateBox.datePicker.maxDate = cal2.timeInMillis

    }

    private fun timeNotifier()
    {
        etTime.doOnTextChanged { text, start, before, count ->
            if(text.toString().length > 0)
            {
                if(text.toString().toInt() <= 23)
                {
                    var timeValue = "AM"
                    var timer = text.toString().toInt()
                    if(timer >= 12)
                    {
                        timeValue = "PM"
                    }
                    if(timer > 12)
                    {
                        timer = timeMapping[timer.toString()]!!
                    }

                    tvTimeFormat.text = "${timer} ${timeValue}"

                }
                else
                {
                    tvTimeFormat.text = "Time Format"
                }
            }

            else
            {
                tvTimeFormat.text = "Time Format"
            }
        }
    }

    private fun validation():Boolean
    {
        if(teamId == "")
        {
            alertMessage("Error","Please select a opponent team.")
            return false
        }
        else if(futsalId == "")
        {
            alertMessage("Error","Please select a futsal.")
            return false
        }

        else if(TextUtils.isEmpty(etDate.text))
        {
            etDate.error = "Choose a date for battle!!"
            etDate.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etTime.text))
        {
            etTime.error = "Choose a time for battle!!"
            etTime.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etDescription.text))
        {
            etDescription.error = "Enter Description for battle!!"
            etDescription.requestFocus()
            return false
        }
        else if(etTime.text.toString().toInt() > 22 && etTime.text.toString().toInt() < 6)
        {
            etTime.error="Time range is valid from 6-22."
            etTime.requestFocus()
            return false
        }
        else
        {
            return true
        }
    }

    private fun spinnerWork()
    {
        spinnerTeam.onItemSelectedListener =
            object:AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    var selected = parent!!.getItemAtPosition(position)
                    if(selected != "Select A Team")
                    {
                        teamId = lstNotMyTeam[position-1]._id
                    }
                    else
                    {
                        teamId=""
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }


        spinnerFutsal.onItemSelectedListener =
            object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    var selected = parent!!.getItemAtPosition(position)
                    if(selected != "Select A Futsal")
                    {
                        futsalId = lstFutsals[position-1]._id
                    }
                    else
                    {
                        futsalId=""
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnAdd ->{
                if(validation())
                {
                    val battleObj = Battle(
                        homeTeam = StaticData.team!!._id,
                        awayTeam = Team(_id = teamId!!),
                        time = etTime.text.toString(),
                        date = etDate.text.toString(),
                        futsal_id = Futsal(_id = futsalId!!),
                        description = etDescription.text.toString()
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val repo = TeamRepository()
                            val response = repo.addBattle(battleObj)
                            if(response.success == true)
                            {
                                withContext(Dispatchers.Main)
                                {
                                    layout.snackbar("${response.message}")
                                    etTime.text!!.clear()
                                    etDate.text!!.clear()
                                    var intent = Intent(this@AddBattleActivity,BattleLogActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                            else
                            {
                                var errors = response.error
                                var errorBox = errors!!.values.joinToString("\n")


                                withContext(Dispatchers.Main)
                                {
                                    println(errorBox)
                                    alertMessage("Errors",errorBox)
                                }
                            }
                        }
                        catch (err:Exception)
                        {
                            println(err.printStackTrace())
                            withContext(Dispatchers.Main)
                            {
                                layout.snackbar("Server Error!!")
                            }
                        }
                    }

                }
            }
            R.id.etDate ->{
                dateBox.show()
            }
            R.id.cbCheck ->{
                btnAdd.isEnabled = cbCheck.isChecked == true
            }
        }
    }
}