package com.sagarmishra.futsal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.sagarmishra.futsal.Repository.ChatRepository
import com.sagarmishra.futsal.adapter.MessageAdapter
import com.sagarmishra.futsal.entityapi.AuthUser
import com.sagarmishra.futsal.entityapi.Chat
import com.sagarmishra.futsal.model.StaticData

import com.sagarmishra.futsal.utils.snackbar
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URISyntaxException

@Suppress("Deprecation")
class ChatActivity : AppCompatActivity(),SwipeRefreshLayout.OnRefreshListener,View.OnClickListener {
    private lateinit var tvReceiver:TextView
    private lateinit var recycler:RecyclerView
    private lateinit var tvSeenTime:TextView
    private lateinit var etMessage:EditText
    private lateinit var btnSend:Button
    private lateinit var player:AuthUser
    private lateinit var adapter:MessageAdapter
    private lateinit var swipe:SwipeRefreshLayout
    var messags:MutableList<Chat> = mutableListOf()

    private lateinit var socket:Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        supportActionBar?.hide()
        binding()
        initialize()
        swipe.setOnRefreshListener(this)
        btnSend.setOnClickListener(this)
//
//        var opts: IO.Options = IO.Options().apply {
//            transports = arrayOf("websocket")
//        }


        try
        {
            socket = IO.socket("http://192.168.1.68:90")
            socket.connect()
        }
        catch(ex: URISyntaxException)
        {
            println(ex.printStackTrace())
            println(ex)
        }
//        socket.on("chatMessage", onUpdateChat)
    }

    private fun binding()
    {
        tvReceiver = findViewById(R.id.tvReceiver)
        recycler = findViewById(R.id.recycler)
        tvSeenTime = findViewById(R.id.tvSeenTime)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)
        swipe = findViewById(R.id.swipe)
    }

    private fun initialize()
    {
        player = intent.getParcelableExtra("player")!!
        var playerTeam = StaticData.playerTeam[player._id]
        if(playerTeam == null)
        {
            tvReceiver.text = "${player.userName} (Not in a team)"
        }
        else
        {
            tvReceiver.text = "${player.userName} (${playerTeam})"
        }

        StaticData.unseenMessage[player._id] = 0

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = ChatRepository()
                val response = repo.getChatMessages(player._id)
                if(response.success == true)
                {
                    messags = response.data!!

                    withContext(Dispatchers.Main)
                    {
                        adapter = MessageAdapter(this@ChatActivity,messags)
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(this@ChatActivity)
                        recycler.scrollToPosition(recycler!!.adapter!!.itemCount-1)
                        if(response.eyeball != "Not Seen")
                        {
                            tvSeenTime.text = response.eyeball
                            tvSeenTime.visibility = View.VISIBLE
                        }
                        else
                        {
                            tvSeenTime.visibility = View.GONE
                        }
                    }
                }
                else
                {
                    println(response.message!!)
                }
            }
            catch(err:Exception)
            {
                println(err.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    btnSend.snackbar("Server Error!!")
                }
            }
        }
    }

    override fun onRefresh() {
        var intent = Intent(this,ChatActivity::class.java)
        intent.putExtra("player",player)
        startActivity(intent)
        finish()
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnSend ->{
                if(!TextUtils.isEmpty(etMessage.text))
                {
                    CoroutineScope(Dispatchers.IO).launch {
                        try
                        {
                            val repo = ChatRepository()
                            val response = repo.sendMessage(player._id,etMessage.text.toString())
                            if(response.success == true)
                            {
                                withContext(Dispatchers.Main)
                                {
                                    val gson = Gson()
                                    var json = gson.toJson(response.data!!)
//                                    socket.emit("chatMessage",json)
                                    etMessage.text!!.clear()
                                }
                            }
                            else
                            {
                                btnSend.snackbar(response.message!!)
                            }
                        }
                        catch (ex:Exception)
                        {
                            println(ex.printStackTrace())
                            withContext(Dispatchers.Main)
                            {
                                btnSend.snackbar("Server Error!!")
                            }
                        }
                    }
                }
            }
        }
    }


//    var onUpdateChat = Emitter.Listener {
//        var gson = Gson()
//        var stringfyData = it[0].toString()
//        var stringJSON = gson.fromJson(stringfyData,Chat::class.java)
//
//        messags.add(stringJSON)
//        adapter = MessageAdapter(this,messags)
//        recycler.adapter = adapter
//        adapter.notifyDataSetChanged()
//    }

}