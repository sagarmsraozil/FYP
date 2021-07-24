package com.sagarmishra.futsal.fragments.verticalNav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.adapter.RuleAdapter


class RulesFragment : Fragment() {
    private lateinit var recycler:RecyclerView
    var lstRules:MutableList<String> = mutableListOf()
    private lateinit var adapter: RuleAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rules, container, false)
        recycler = view.findViewById(R.id.recycler)
        lstRules = mutableListOf(
            "Select your opponent according to your teams ability and potential.",
            "Make sure your team is authorized team.",
            "You can find your match and chat with them in the chat section.",
            "During matchfinding you can see the neary futsals and have an eye on them for the later booking purpose.",
            "You cannot update your match stats yourself.",
            "You should ask in account section of the futsal to update your stats.",
            "Both team should be available in the account section while updating the stats.",
            "Stats update feature is not available in futsals, which are not listed in Futsal Arena.",
            "Make sure you are playing in futsal which has implemented feature of Futsal Arena.",
            "If you are absent in the match without early notice. 15-0 loss would be registered against your opponent team",
            "Available Team should show the chat proof to make the punishment happen. Chat need to occur in Futsal Arena system.",
            "Register battle after booking the futsal.",
            "If the both team register the match in battle before 1 hour of match time, they would gain 50 Futsal Points.",
            "Note that both team need to register to gain 50 Futsal points.",
            "50 points would be added to leader profile. To increase it, a team should consult to the account after match ends.",
            "Futsals which have not implemented Futsal Arena doesnot have this feature.",
            "Futsal Point would be provided in the gameday and is valid till the next hour after match ends.",
            "Predictions are given according to previous games data. Please don't get triggered."
        )


        var adapter = RuleAdapter(requireContext(),lstRules)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        return view
    }


}