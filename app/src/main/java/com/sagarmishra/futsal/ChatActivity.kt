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
import com.sagarmishra.futsal.Repository.ChatRepository
import com.sagarmishra.futsal.adapter.MessageAdapter
import com.sagarmishra.futsal.entityapi.AuthUser
import com.sagarmishra.futsal.entityapi.Chat
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        supportActionBar?.hide()
        binding()
        initialize()
        swipe.setOnRefreshListener(this)
        btnSend.setOnClickListener(this)
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
        tvReceiver.text = "${player.userName} (${playerTeam})"
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
}