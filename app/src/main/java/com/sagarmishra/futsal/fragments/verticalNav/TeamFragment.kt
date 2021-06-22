package com.sagarmishra.futsal.fragments.verticalNav

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.sagarmishra.futsal.*
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.adapter.PlayerAdapter
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.AuthUser
import com.sagarmishra.futsal.entityapi.Team
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import java.lang.Exception


class TeamFragment : Fragment(),View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {
    private lateinit var tvTeamName:TextView
    private lateinit var tvTeamCode:TextView
    private lateinit var tvSeasonCount:TextView
    private lateinit var tvPointsObtained:TextView
    private lateinit var tvTierReached:TextView
    private lateinit var tvBattleCount:TextView
    private lateinit var tvTitleName:TextView
    private lateinit var ivLogo:CircleImageView
    private lateinit var recycler:RecyclerView
    private lateinit var adapter:PlayerAdapter
    private lateinit var btnLeave : Button
    private lateinit var btnDisband:Button
    private lateinit var swipe:SwipeRefreshLayout
    private lateinit var btnStats:Button
    private lateinit var btnSearchMatch:Button
    private lateinit var tvSeasonPoint:TextView
    private lateinit var ivSearch:ImageView
    private lateinit var tvStatus:TextView
    private lateinit var ivRequest:ImageView
    private lateinit var ivTournament:ImageView
    private lateinit var ivBattle:ImageView
    private lateinit var ivEdit:ImageView
    private lateinit var ivChallenge:ImageView
    var lstPlayers:MutableList<AuthUser> = mutableListOf()
    var teamId:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_team, container, false)
        binding(view)
        initialize()
        listeners()
        return view
    }

    private fun binding(v:View)
    {
        tvTeamName = v!!.findViewById(R.id.tvTeamName)
        tvTeamCode = v!!.findViewById(R.id.tvTeamCode)
        tvSeasonCount = v!!.findViewById(R.id.tvSeasonCount)
        tvPointsObtained = v!!.findViewById(R.id.tvPointsObtained)
        tvTierReached = v!!.findViewById(R.id.tvTierReached)
        tvBattleCount = v!!.findViewById(R.id.tvBattleCount)
        tvTitleName = v!!.findViewById(R.id.tvTitleName)
        ivLogo = v!!.findViewById(R.id.ivLogo)
        recycler = v!!.findViewById(R.id.recycler)
        btnLeave = v!!.findViewById(R.id.btnLeave)
        btnDisband = v!!.findViewById(R.id.btnDisband)
        swipe = v!!.findViewById(R.id.swipe)
        btnStats = v!!.findViewById(R.id.btnStats)
        btnSearchMatch = v!!.findViewById(R.id.btnSearchMatch)
        tvSeasonPoint = v!!.findViewById(R.id.tvSeasonPoint)
        ivSearch = v!!.findViewById(R.id.ivSearch)
        tvStatus = v!!.findViewById(R.id.tvStatus)
        ivRequest = v!!.findViewById(R.id.ivRequest)
        ivTournament = v!!.findViewById(R.id.ivTournament)
        ivBattle = v!!.findViewById(R.id.ivBattle)
        ivEdit = v!!.findViewById(R.id.ivEdit)
        ivChallenge = v!!.findViewById(R.id.ivChallenge)
    }

    private fun listeners()
    {
        btnDisband.setOnClickListener(this)
        btnLeave.setOnClickListener(this)
        swipe.setOnRefreshListener(this)
        btnStats.setOnClickListener(this)
        btnSearchMatch.setOnClickListener(this)
        ivSearch.setOnClickListener(this)
        ivTournament.setOnClickListener(this)
        ivBattle.setOnClickListener(this)
        ivRequest.setOnClickListener(this)
        ivEdit.setOnClickListener(this)
        ivChallenge.setOnClickListener(this)
    }

    private fun initialize()
    {


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = TeamRepository()
                val response = repo.getMyTeam()
                if(response.success == true)
                {
                    withContext(Dispatchers.Main)
                    {
                        teamId = response.data!!._id
                        StaticData.team = response.data!!
                        tvTeamName.text = response.data!!.teamName
                        tvTeamCode.text = response.data!!.teamCode
                        tvSeasonCount.text = response.data!!.teamPlayers!!.size.toString()+"/8"
                        lstPlayers = response.data!!.teamPlayers!!
                        tvStatus.text = response.data!!.status!!

                        if(response.data!!.teamOwner == StaticData.user!!._id)
                        {
                            btnLeave.visibility = View.GONE
                            btnDisband.visibility = View.VISIBLE
                        }
                        else
                        {
                            btnLeave.visibility = View.VISIBLE
                            btnDisband.visibility = View.GONE
                        }

                        if(response.data!!.activeTitle != null)
                        {
                            tvTitleName.text = response.data!!.activeTitle
                        }
                        else
                        {
                            tvTitleName.text = "N/A"
                        }
                        if(response.data!!.teamLogo == "no-logo.png")
                        {
                            ivLogo.setImageResource(R.drawable.logoteam)
                        }
                        else
                        {
                            var imgPath = RetrofitService.loadImagePath()+response.data!!.teamLogo!!.replace("\\","/")
                            Glide.with(requireContext()).load(imgPath).into(ivLogo)
                        }
                        StaticData.myTier = response.tierName!!
                        StaticData.matchPlayed = response.tierData!!
                        StaticData.titleReceiveCount = response.count
                        adapter = PlayerAdapter(requireContext(),lstPlayers,response.data!!,"team")
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)


                        if(StaticData.team!!.status == "Approved" && (StaticData.team!!.teamOwner == StaticData.user!!._id || (StaticData.team!!.teamColeader != null && StaticData.team!!.teamColeader == StaticData.user!!._id)))
                        {
                            btnSearchMatch.isEnabled = true
                            ivEdit.visibility = View.VISIBLE
                            ivRequest.visibility = View.VISIBLE
                        }
                        else
                        {
                            btnSearchMatch.isEnabled = false
                            ivEdit.visibility = View.GONE
                            ivRequest.visibility = View.GONE
                        }
                    }


                    if(response.data!!.status == "Approved")
                    {
                        val response1 = repo.getTeamStats(teamId)
                        if(response1.success == true)
                        {
                            withContext(Dispatchers.Main)
                            {

                                tvBattleCount.text = response1.data!!.matchPlayed.toString()
                                tvPointsObtained.text = response1.data!!.pointsCollected.toString()
                                tvTierReached.text = response1.data!!.tier!!.tierNomenclature
                                tvSeasonPoint.text = response1.data!!.season.toString()
                            }
                        }
                        else
                        {
                            withContext(Dispatchers.Main)
                            {
                                tvTitleName.snackbar("${response1.message}")
                            }
                        }
                    }


                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        var intent = Intent(requireContext(),NewTeamActivity::class.java)
                        intent.putExtra("task","Join")
                        startActivity(intent)

                    }

                }
            }
            catch(err:Exception)
            {
                println(err.printStackTrace())
                withContext(Dispatchers.Main)
                {
                    tvTitleName.snackbar("Server Error!!")
                }
            }
        }




    }

    private fun alert(title:String,msg:String,task:String)
    {
        var alertD = AlertDialog.Builder(requireContext())
        alertD.setTitle(title)
        alertD.setMessage(msg)

        alertD.setPositiveButton("Yes"){ dialogInterface: DialogInterface, i: Int ->
            if(task == "Disband")
            {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repo = TeamRepository()
                        val response = repo.disbandTeam()
                        if(response.success == true)
                        {
                            withContext(Dispatchers.Main)
                            {
                                btnDisband.snackbar("${response.message}")
                                StaticData.team = null
                                val intent = Intent(requireContext(),MainActivity::class.java)
                                intent.putExtra("FRAGMENT_NUMBER",0)
                                startActivity(intent)
                                requireActivity().finish()
                            }
                        }
                        else
                        {
                            withContext(Dispatchers.Main)
                            {
                                btnDisband.snackbar("${response.message}")
                            }
                        }
                    }
                    catch(err:Exception)
                    {
                        println(err.printStackTrace())
                        withContext(Dispatchers.Main)
                        {
                            btnDisband.snackbar("Server Error")
                        }
                    }

                }
            }

            if(task == "Leave")
            {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repo = TeamRepository()
                        val response = repo.leaveTeam()
                        if(response.success == true)
                        {
                            btnDisband.snackbar("${response.message}")
                            StaticData.team = null
                            val intent = Intent(requireContext(),MainActivity::class.java)
                            intent.putExtra("FRAGMENT_NUMBER",0)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                        else
                        {
                            btnLeave.snackbar("${response.message}")
                        }
                    }
                    catch (err:Exception)
                    {
                        println(err.printStackTrace())
                        withContext(Dispatchers.Main)
                        {
                            btnLeave.snackbar("Server Error")
                        }
                    }
                }
            }
        }

        alertD.setNegativeButton("No"){ dialogInterface: DialogInterface, i: Int ->

        }

        var alerts = alertD.create()
        alerts.setCancelable(false)
        alerts.show()
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnDisband->{
                alert("Disband Team","Do you really want to disband this team?","Disband")
            }

            R.id.btnLeave ->{
                alert("Leave Team","Do you really want to leave this team?","Leave")
            }
            R.id.ivBattle ->{
                val intent = Intent(requireContext(),BattleLogActivity::class.java)
                startActivity(intent)
            }
            R.id.ivTournament ->{
                val intent = Intent(requireContext(),TournamentActivity::class.java)
                startActivity(intent)
            }
            R.id.btnStats ->{
                val intent = Intent(requireContext(),StatsActivity::class.java)
                intent.putExtra("teamId",StaticData.team!!._id)
                startActivity(intent)
            }
            R.id.btnSearchMatch ->{
                val intent = Intent(requireContext(),SearchMatchActivity::class.java)
                startActivity(intent)
            }
            R.id.ivRequest ->{
                val intent = Intent(requireContext(),RequestsActivity::class.java)
                startActivity(intent)
            }
            R.id.ivSearch->{
                var intent = Intent(requireContext(),NewTeamActivity::class.java)
                intent.putExtra("task","Search")
                startActivity(intent)

            }
            R.id.ivEdit->{
                val intent = Intent(requireContext(),EditTeamDetailsActivity::class.java)
                startActivity(intent)
            }
            R.id.ivChallenge ->{
                val intent = Intent(requireContext(),ChallengeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onRefresh() {
       val intent = Intent(requireContext(),MainActivity::class.java)
        intent.putExtra("FRAGMENT_NUMBER",8)
        startActivity(intent)
        requireActivity().finish()
    }


}