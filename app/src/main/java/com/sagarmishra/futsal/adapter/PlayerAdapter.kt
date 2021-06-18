package com.sagarmishra.futsal.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.MainActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.entityapi.AuthUser
import com.sagarmishra.futsal.entityapi.Team
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerAdapter(val context:Context,val lstPlayers:MutableList<AuthUser>,val team: Team,var task:String):RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {
    class PlayerViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val playerName:TextView = view.findViewById(R.id.playerName)
        val playerRole:TextView = view.findViewById(R.id.playerRole)
        val playerTitle:TextView = view.findViewById(R.id.playerTitle)
        val ivSettings:ImageView = view.findViewById(R.id.ivSettings)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_player_list,parent,false)
        return PlayerViewHolder(view)
    }

    fun alertMsg(msg:String)
    {
        var alertD = AlertDialog.Builder(context)
        alertD.setTitle("Error")
        alertD.setMessage(msg)
        alertD.setNegativeButton("OK"){ dialogInterface: DialogInterface, i: Int ->

        }

        var alert = alertD.create()
        alert.setCancelable(false)
        alert.show()
    }

    fun alert(title:String,msg:String,task:String,holder:PlayerViewHolder,player:AuthUser)
    {
        var alertD = AlertDialog.Builder(context)
        alertD.setTitle(title)
        alertD.setMessage(msg)

        alertD.setPositiveButton("Yes"){ dialogInterface: DialogInterface, i: Int ->
            if(task == "Promote To Coleader")
            {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repo = TeamRepository()
                        val response = repo.promoteToColeader(team._id,player._id)
                        if(response.success == true)
                        {
                            withContext(Dispatchers.Main)
                            {
                                holder.playerTitle.snackbar("${response.message}")
                                val intent = Intent(context,MainActivity::class.java)
                                intent.putExtra("FRAGMENT_NUMBER",8)
                                context.startActivity(intent)
                            }
                        }
                        else
                        {
                            withContext(Dispatchers.Main)
                            {
                                alertMsg("${response.message}")
                            }
                        }
                    }
                    catch(err:Exception)
                    {
                        println(err.printStackTrace())
                        withContext(Dispatchers.Main)
                        {
                            holder.playerTitle.snackbar("Server Error!!")
                        }
                    }

                }
            }

            if(task == "Kick")
            {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repo = TeamRepository()
                        val response = repo.kickPlayer(team._id,player._id)
                        if(response.success == true)
                        {
                            withContext(Dispatchers.Main)
                            {
                                holder.playerTitle.snackbar("${response.message}")
                                val intent = Intent(context,MainActivity::class.java)
                                intent.putExtra("FRAGMENT_NUMBER",8)
                                context.startActivity(intent)
                            }
                        }
                        else
                        {
                            withContext(Dispatchers.Main)
                            {
                                alertMsg("${response.message}")

                            }
                        }
                    }
                    catch(err:Exception)
                    {
                        println(err.printStackTrace())
                        withContext(Dispatchers.Main)
                        {
                            holder.playerTitle.snackbar("Server Error!!")
                        }
                    }

                }
            }

            if(task == "Promote To Leader")
            {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repo = TeamRepository()
                        val response = repo.promoteToLeader(player._id)
                        if(response.success == true)
                        {
                            holder.playerTitle.snackbar("${response.message}")
                            val intent = Intent(context,MainActivity::class.java)
                            intent.putExtra("FRAGMENT_NUMBER",8)
                            context.startActivity(intent)
                        }
                        else
                        {
                            withContext(Dispatchers.Main)
                            {
                                alertMsg("${response.message}")
                            }
                        }
                    }
                    catch(err:Exception)
                    {
                        println(err.printStackTrace())
                        withContext(Dispatchers.Main)
                        {
                            holder.playerTitle.snackbar("Server Error!!")
                        }
                    }
                }
            }

            if(task == "Demote")
            {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repo = TeamRepository()
                        val response = repo.demotePlayer(team._id,player._id)
                        if(response.success == true)
                        {
                            holder.playerTitle.snackbar("${response.message}")
                            val intent = Intent(context,MainActivity::class.java)
                            intent.putExtra("FRAGMENT_NUMBER",8)
                            context.startActivity(intent)
                        }
                        else
                        {
                            withContext(Dispatchers.Main)
                            {
                                alertMsg("${response.message}")
                            }
                        }
                    }
                    catch(err:Exception)
                    {
                        println(err.printStackTrace())
                        withContext(Dispatchers.Main)
                        {
                            holder.playerTitle.snackbar("Server Error!!")
                        }
                    }
                }
            }


        }

        alertD.setNegativeButton("No"){ dialogInterface: DialogInterface, i: Int ->

        }


        var alert = alertD.create()
        alert.setCancelable(false)
        alert.show()


    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        var player = lstPlayers[position]
        var userId = StaticData.user!!._id
        var role:String = ""
        var playerRole:String = ""
        var pop = PopupMenu(context,holder.playerName)
        holder.playerName.text = "${player.firstName} ${player.lastName} as ${player.userName}"

        //fetching online users role
        if(userId == team.teamOwner)
        {
            role = "Leader"
        }
        else if(team.teamColeader!= null && team.teamColeader == userId)
        {
            role = "CoLeader"
        }
        else
        {
            role = "Member"
        }

        //fetching players role who is in count of loop
        if(player._id == team.teamOwner)
        {
            playerRole = "Leader"
        }
        else if(team.teamColeader!= null && team.teamColeader == player._id)
        {
            playerRole = "CoLeader"
        }
        else
        {
            playerRole = "Member"
        }

        holder.playerRole.text = playerRole

        if(player.activeTitle != "" && player.activeTitle != null)
        {
            holder.playerTitle.visibility = View.VISIBLE
            holder.playerTitle.text=player.activeTitle
        }
        else
        {
            holder.playerTitle.visibility = View.GONE
        }
        if(task == "other")
        {
            holder.ivSettings.visibility = View.GONE
        }
        else
        {
            if(playerRole == "Leader")
            {
                holder.ivSettings.visibility = View.GONE
            }
            if((role == "CoLeader" && playerRole == "CoLeader") || (role == "CoLeader" && playerRole == "Leader"))
            {
                holder.ivSettings.visibility = View.GONE
            }

            if(playerRole == "CoLeader" && role == "Leader")
            {
                holder.ivSettings.visibility = View.VISIBLE
                pop.menuInflater.inflate(R.menu.team_role,pop.menu)
                var menu = pop.menu
                menu.findItem(R.id.promote).isVisible = false
                menu.findItem(R.id.kick).isVisible = false

            }

            if((role == "Leader" || role == "CoLeader") && (playerRole == "Member"))
            {
                holder.ivSettings.visibility = View.VISIBLE
                pop.menuInflater.inflate(R.menu.team_role,pop.menu)
                var menu = pop.menu
                menu.findItem(R.id.promoteToLeader).isVisible = false
                menu.findItem(R.id.demote).isVisible = false
            }

            if(role == "Member")
            {
                holder.ivSettings.visibility = View.GONE
            }

        }


        holder.ivSettings.setOnClickListener{
            pop.show()
        }

        pop.setOnMenuItemClickListener {
            when(it.itemId)
            {
                R.id.promote ->{
                    alert("Promote ${player.userName} to CoLeader?","Do you really want to promote the player to CoLeader?","Promote To Coleader",holder,player)
                }
                R.id.promoteToLeader->{
                    alert("Promote ${player.userName} to Leader?","Do you really want to promote the player Leader?","Promote To Leader",holder,player)
                }
                R.id.kick->{
                    alert("Kick ${player.userName} from team?","Do you really want to Kick the player?","Kick",holder,player)
                }
                R.id.demote->{
                    alert("Demote ${player.userName} to Member?","Do you really want to demote the player?","Demote",holder,player)
                }
            }

            false
        }


    }

    override fun getItemCount(): Int {
        return lstPlayers.size
    }
}