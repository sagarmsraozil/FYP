package com.sagarmishra.futsal

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sagarmishra.futsal.Permissions.Logout
import com.sagarmishra.futsal.fragments.*
import com.sagarmishra.futsal.notification.NotificationSender
import com.sagarmishra.futsal.utils.snackbar

@Suppress("Deprecation")
class MainActivity : AppCompatActivity(),View.OnClickListener,BottomNavigationView.OnNavigationItemSelectedListener,SensorEventListener {
    private lateinit var horizontalNavView : BottomNavigationView
    var selected = R.id.nav_home
    var myFrags:MutableList<Fragment> = mutableListOf(HomeFragment(),TournamentFragment(),ChatFragment(),MyBookingsFragment(),AccountFragment())
    var navChangings:MutableList<Int> = mutableListOf(R.id.nav_home,R.id.nav_tournament,R.id.nav_chats,R.id.nav_bookings,R.id.nav_account)
    private lateinit var sensorManager:SensorManager
    private var sensor:Sensor?=null
    private var sensor2:Sensor?=null
    private var sensor3:Sensor?=null
    private val permissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        binding()
        initialize()
        listener()

        if(!checkPermission())
        {
            requestPermission()
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        if(!checkSensor())
        {
            return
        }
        else
        {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            sensor2 = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            sensor3 = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(this,sensor2,SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(this,sensor3,SensorManager.SENSOR_DELAY_NORMAL)
        }

        //var getPref = getSharedPreferences("credentials",Context.MODE_PRIVATE)
        //Toast.makeText(this@MainActivity, "Username: ${getPref.getString("username","")} and ${getPref.getString("password","")}", Toast.LENGTH_SHORT).show()
    }

    private fun checkSensor():Boolean
    {
        var flag = true
        if(sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) == null)
        {
            flag = false
        }
        if(sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == null)
        {
            flag = false
        }
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null)
        {
            flag = false
        }
        return flag
    }

    private fun requestPermission()
    {
        ActivityCompat.requestPermissions(this@MainActivity,permissions,123)
    }

    private fun binding()
    {
        horizontalNavView = findViewById(R.id.navView)
    }

    private fun listener()
    {
        horizontalNavView.setOnNavigationItemSelectedListener(this)
    }

    private fun initialize()
    {
        var pagerCount = intent.getIntExtra("FRAGMENT_NUMBER",-1)
        if(pagerCount > 0)
        {
            openFragment(myFrags[pagerCount])
            selected = navChangings[pagerCount]
            horizontalNavView.menu.findItem(navChangings[pagerCount]).isChecked = true
        }
        else
        {
            openFragment(HomeFragment())
        }

    }

    private fun openFragment(frag:Fragment)
    {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout,frag)
            addToBackStack(null)
            commit()
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {

        }
    }

    override fun onBackPressed() {
        if(selected != R.id.nav_home){
            openFragment(HomeFragment())
            selected = R.id.nav_home
            horizontalNavView.menu.findItem(R.id.nav_home).isChecked = true
        }
        else
        {
            val builder= AlertDialog.Builder(this@MainActivity)
            builder.setTitle("")
            builder.setMessage("Do you really want to close application?")
            builder.setPositiveButton("Yes")
            {
                dialog: DialogInterface?, which: Int ->
                finish()
            }
            builder.setNegativeButton("No"){
                    dialog: DialogInterface?, which: Int ->

            }


            val alertD : AlertDialog = builder.create()
            alertD.setCancelable(false)
            alertD.show()
        }

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.nav_home ->{
                   openFragment(HomeFragment())
                   selected = R.id.nav_home
                   horizontalNavView.menu.findItem(R.id.nav_home).isChecked = true

            }
            R.id.nav_tournament ->{
                openFragment(TournamentFragment())
                selected = R.id.nav_tournament
                horizontalNavView.menu.findItem(R.id.nav_tournament).isChecked = true
            }
            R.id.nav_chats ->{
                openFragment(ChatFragment())
                selected = R.id.nav_chats
                horizontalNavView.menu.findItem(R.id.nav_chats).isChecked = true
            }
            R.id.nav_bookings ->{
                openFragment(MyBookingsFragment())
                selected = R.id.nav_bookings
                horizontalNavView.menu.findItem(R.id.nav_bookings).isChecked = true
            }
            R.id.nav_account ->{
                openFragment(AccountFragment())
                selected = R.id.nav_account
                horizontalNavView.menu.findItem(R.id.nav_account).isChecked = true
            }

        }

        return true
    }


    private fun checkPermission():Boolean
    {
        var check = true
        for(i in permissions)
        {
            if(ActivityCompat.checkSelfPermission(this@MainActivity,i) != PackageManager.PERMISSION_GRANTED)
            {
                check = false
            }
        }
        return check
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.sensor.type == Sensor.TYPE_PROXIMITY)
        {
            val values = event!!.values[0]
            if(values <= 4)
            {
                val logOut = Logout(this,this)
                logOut.logout()

            }

        }

        if(event!!.sensor.type == Sensor.TYPE_LIGHT)
        {
            val values = event!!.values[0]
            if(values >= 40)
            {
                NotificationSender(this,"High Light can damage your eyes","").createHighPriority()

            }
        }


        if(event!!.sensor.type == Sensor.TYPE_ACCELEROMETER)
        {
            val values = event!!.values
            val x_axis = values[0]

            if(x_axis >= 7 && x_axis < 10)
            {

                openFragment(myFrags[3])
                horizontalNavView.menu.findItem(navChangings[3]).isChecked = true
            }
            else if (x_axis <= -7 && x_axis > -10)
            {

                openFragment(myFrags[4])
                horizontalNavView.menu.findItem(navChangings[4]).isChecked = true
            }
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}