package com.sagarmishra.futsal.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


fun dateConversion(date:String):String
{
    var partition = date.split("-").toMutableList()
    if(partition[1].toInt() < 10 && partition[1].length == 1)
    {
        partition[1] = "0${partition[1]}"
    }
    if(partition[2].toInt() < 10 && partition[1].length == 1)
    {
        partition[2] = "0${partition[2]}"
    }

    return partition.joinToString("-")
}

fun dateToAgeConverter(date:String):Int
{
    var birthDate = dateConversion(date)
    var parsedBirthDate = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    var todayDate = LocalDate.now()

    var age = ChronoUnit.YEARS.between(parsedBirthDate,todayDate).toInt()
    return age
}

fun View.snackbar(message:String)
{
    var snk = Snackbar.make(this,message,Snackbar.LENGTH_LONG)
    snk.setAction("OK",View.OnClickListener {
        snk.dismiss()
    })
    snk.show()
}


//fun main()
//{
//    var lstPrice:MutableList<Int> = mutableListOf(100,200,400)
//    var sum = lstPrice.reduce { acc, i ->
//        acc+i
//    }
//
//    var sum2 = lstPrice.fold(300){ i: Int, i1: Int ->
//        i+i1
//    }
//    println(sum)
//    println(sum2)
//
//
//}
