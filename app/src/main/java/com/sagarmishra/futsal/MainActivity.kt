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
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.sagarmishra.futsal.Permissions.Logout
import com.sagarmishra.futsal.fragments.*
import com.sagarmishra.futsal.fragments.verticalNav.*
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.notification.NotificationSender
import com.sagarmishra.futsal.utils.snackbar

@Suppress("Deprecation")
class MainActivity : AppCompatActivity(),View.OnClickListener,BottomNavigationView.OnNavigationItemSelectedListener,SensorEventListener,NavigationView.OnNavigationItemSelectedListener {
    private lateinit var horizontalNavView : BottomNavigationView
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var verticalNavView : NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    var selected = R.id.nav_home
    var myFrags:MutableList<Fragment> = mutableListOf(HomeFragment(),TournamentFragment(),ChatFragment(),MyBookingsFragment(),AccountFragment(),ActivationFragment(),GiveawayFragment(),UserPointsFragment(),TeamFragment(),TournamentsFragment(),ChatsFragment(),RulesFragment()
    )
    var navChangings:MutableList<Int> = mutableListOf(R.id.nav_home,R.id.nav_tournament,R.id.nav_chats,R.id.nav_bookings,R.id.nav_account,R.id.activate,R.id.giveaway,R.id.warning,R.id.team,R.id.tournament,R.id.chat,R.id.rules)
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
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding()
        setSupportActionBar(toolbar)
        navDrawerMenu()
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

        //activation account check
        val menu = verticalNavView.menu
        if(!StaticData.user!!.account_activation)
        {
            menu.findItem(R.id.activate).isVisible = true
        }
        else
        {
            menu.findItem(R.id.activate).isVisible = false
        }



        //var getPref = getSharedPreferences("credentials",Context.MODE_PRIVATE)
        //Toast.makeText(this@MainActivity, "Username: ${getPref.getString("username","")} and ${getPref.getString("password","")}", Toast.LENGTH_SHORT).show()
    }

    private fun navDrawerMenu()
    {
        verticalNavView.bringToFront()
        val toggle : ActionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
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
        verticalNavView = findViewById(R.id.verticalBar)
        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.toolBar)
    }

    private fun listener()
    {
        horizontalNavView.setOnNavigationItemSelectedListener(this)
        verticalNavView.setNavigationItemSelectedListener(this)
    }

    private fun initialize()
    {
        var pagerCount = intent.getIntExtra("FRAGMENT_NUMBER",-1)
        println(pagerCount)
        if(pagerCount > 0 && pagerCount <= 4)
        {
            openFragment(myFrags[pagerCount])
            selected = navChangings[pagerCount]
            horizontalNavView.menu.findItem(navChangings[pagerCount]).isChecked = true
        }
        else if(pagerCount > 4)
        {
            openFragment(myFrags[pagerCount])
            selected = navChangings[pagerCount]
            verticalNavView.menu.findItem(navChangings[pagerCount]).isChecked = true
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
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else
        {
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


    }

    private fun horizontalOff()
    {
        var horizontalFrag :MutableList<Int> = mutableListOf(
            R.id.nav_home,R.id.nav_chats,R.id.nav_account,R.id.nav_tournament,R.id.nav_bookings
        )
        for(i in horizontalFrag)
        {
            horizontalNavView.menu.findItem(i).isChecked = false
        }
        horizontalNavView.menu.findItem(R.id.nav_bookings).isChecked = false

    }

    private fun verticalOff()
    {
        var verticalFrag:MutableList<Int> = mutableListOf(
            R.id.activate,R.id.giveaway,R.id.warning,R.id.tournament,R.id.chat,R.id.team
        )

        for(i in verticalFrag)
        {
            verticalNavView.menu.findItem(i).isChecked = false
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.nav_home ->{
                   openFragment(HomeFragment())
                   selected = R.id.nav_home
                    verticalOff()
                   horizontalNavView.menu.findItem(R.id.nav_home).isChecked = true

            }
            R.id.nav_tournament ->{
                openFragment(TournamentFragment())
                selected = R.id.nav_tournament
                verticalOff()
                horizontalNavView.menu.findItem(R.id.nav_tournament).isChecked = true
            }
            R.id.nav_chats ->{
                openFragment(ChatFragment())
                selected = R.id.nav_chats
                verticalOff()
                horizontalNavView.menu.findItem(R.id.nav_chats).isChecked = true
            }
            R.id.nav_bookings ->{
                openFragment(MyBookingsFragment())
                selected = R.id.nav_bookings
                verticalOff()
                horizontalNavView.menu.findItem(R.id.nav_bookings).isChecked = true
            }
            R.id.nav_account ->{
                openFragment(AccountFragment())
                selected = R.id.nav_account
                verticalOff()
                horizontalNavView.menu.findItem(R.id.nav_account).isChecked = true
            }
            R.id.activate ->{
                openFragment(ActivationFragment())
                selected = R.id.activate
                horizontalOff()
                verticalNavView.menu.findItem(R.id.activate).isChecked = true
            }

            R.id.giveaway->
            {
                openFragment(GiveawayFragment())
                selected = R.id.giveaway
                horizontalOff()
                verticalNavView.menu.findItem(R.id.giveaway).isChecked = true
            }

            R.id.warning->{
                openFragment(UserPointsFragment())
                selected = R.id.warning
                horizontalOff()
                verticalNavView.menu.findItem(R.id.warning).isChecked = true
            }
            R.id.team->{
                openFragment(TeamFragment())
                selected = R.id.team
                horizontalOff()
                verticalNavView.menu.findItem(R.id.team).isChecked = true
            }
            R.id.tournament ->{
                openFragment(TournamentsFragment())
                selected = R.id.tournament
                horizontalOff()
                verticalNavView.menu.findItem(R.id.tournament).isChecked = true
            }

            R.id.chat ->{
                openFragment(ChatsFragment())
                selected = R.id.chat
                horizontalOff()
                verticalNavView.menu.findItem(R.id.chat).isChecked = true
            }

            R.id.rules ->{
                openFragment(RulesFragment())
                selected = R.id.rules
                horizontalOff()
                verticalNavView.menu.findItem(R.id.rules).isChecked = true
            }


        }
        drawerLayout.closeDrawer(GravityCompat.START)
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