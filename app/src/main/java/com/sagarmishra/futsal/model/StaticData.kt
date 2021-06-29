package com.sagarmishra.futsal.model

import com.sagarmishra.futsal.entityapi.AuthUser
import com.sagarmishra.futsal.entityapi.Battle
import com.sagarmishra.futsal.entityapi.HistoryRecord
import com.sagarmishra.futsal.entityapi.Team
import com.sagarmishra.futsal.entityroom.User
import java.text.SimpleDateFormat
import java.util.*

object StaticData {
    val days :MutableList<String> = mutableListOf("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday")
    val months : MutableList<String> = mutableListOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sept","Oct","Nov","Dec")
    var futsalId = ""
    var user: AuthUser? = null
    var bookedInstanceId:String?=null
    var registration_id:String?=null
    var remnant :MutableList<String>? = null
    var markDetails:MutableMap<String,MutableList<String>>? = null
    var bookTimes:MutableMap<String,String>? = null
    var markAndInstance:MutableMap<String,Int>? =null
    var team: Team?=null
    var upcomingBattles:MutableList<Battle>?= mutableListOf()
    var resultBattles:MutableList<Battle>? = mutableListOf()
    var deletableBattle:MutableList<String>?= mutableListOf()
    var tournamentHistory:MutableMap<String,Any>? = mutableMapOf()
    var myTier:String = ""
    var matchPlayed:String = ""
    var titleReceiveCount:Int = 0
    var tournamentId:String = ""
    var playerTeam:MutableMap<String,String> = mutableMapOf()
    var unseenMessage:MutableMap<String,Int> = mutableMapOf()
    var receiverId:String = ""
    var historyTournament:MutableList<HistoryRecord> = mutableListOf()


    fun makeDateAndTime():MutableList<DateAndTime>
    {
        var dateAndTime : MutableList<DateAndTime> = mutableListOf()
        var getSwappedDays : MutableList<String> = getSwapped()
        var getDate :MutableList<MutableList<String>> = getTotalDate()
        for(i in getSwappedDays.indices)
        {
            dateAndTime.add(DateAndTime(getSwappedDays[i],getDate[0][i],getDate[1][i]));
        }
        return dateAndTime
    }

    private fun getSwapped():MutableList<String>
    {
        var swapContainer : MutableList<String> = mutableListOf()
        val cal = Calendar.getInstance()
        val todayDay = cal.get(Calendar.DAY_OF_WEEK)-1
        swapContainer.addAll(days.slice(todayDay..days.size-1))
        swapContainer.addAll(days.slice(0..todayDay-1))
        return swapContainer
    }


    private fun getTotalDate():MutableList<MutableList<String>>
    {
        var returning : MutableList<MutableList<String>> = mutableListOf()
        var plainDate : MutableList<String> = mutableListOf()
        var styleDate : MutableList<String> = mutableListOf()
        val cal = Calendar.getInstance()
        while(plainDate.size!=7)
        {
            val month = cal.get(Calendar.MONTH)
            val date = cal.get(Calendar.DATE)
            val year = cal.get(Calendar.YEAR)
            val style = "${date} ${months[month]},${year}"
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val format = formatter.format(cal.time)

            plainDate.add(format)
            styleDate.add(style)
            cal.add(Calendar.DATE,1)
        }
        returning.add(plainDate)
        returning.add(styleDate)
        return returning
    }

    fun getToday():String
    {
        var cal = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val format = formatter.format(cal.time)
        return format
    }
}