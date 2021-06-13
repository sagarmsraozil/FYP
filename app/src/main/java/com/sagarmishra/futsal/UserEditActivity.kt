package com.sagarmishra.futsal

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.sagarmishra.futsal.fragments.edit_details.StartFragment
import com.sagarmishra.futsal.notification.NotificationSender

class UserEditActivity : AppCompatActivity(),SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var sensor:Sensor?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit)
        supportActionBar?.hide()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        if(!checkSensor()) {
            return
        }
        else
        {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            sensorManager.registerListener(this,sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.dataLayout,StartFragment())
            commit()
        }
    }

    private fun checkSensor():Boolean
    {
        var flag = true
        if(sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) == null)
        {
            flag = false
        }
        return flag
    }


    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values[0]

        if(values > 20000)
        {
            println(values)
            NotificationSender(this,"High brightness can damage your eyes","").createHighPriority()

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}