package com.sagarmishra.futsal.socket

import android.app.Application
//import com.github.nkzawa.socketio.client.IO
//import com.github.nkzawa.socketio.client.Socket
//import com.sagarmishra.futsal.api.RetrofitService
//import java.net.URISyntaxException
//
//class ChatSocket() {
//    private lateinit var socket2:Socket
//    var opts:IO.Options = IO.Options().apply {
//        transports = arrayOf("websocket","polling","flashsocket")
//    }
//    var url = RetrofitService.BASE_URL2
//
//
//    fun getSocket():Socket
//    {
//        try
//        {
//            socket2 = IO.socket(url,opts);
//
//        }
//        catch(ex:URISyntaxException)
//        {
//            println(ex.printStackTrace())
//            println(ex)
//        }
//        return socket2
//    }
//}