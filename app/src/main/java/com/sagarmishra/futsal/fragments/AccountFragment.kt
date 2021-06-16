package com.sagarmishra.futsal.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sagarmishra.futsal.GiveawayActivity
import com.sagarmishra.futsal.Permissions.Logout
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.UserEditActivity
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar


class AccountFragment : Fragment(),View.OnClickListener,PopupMenu.OnMenuItemClickListener {
    private lateinit var tvUsername:TextView
    private lateinit var tvEmail:TextView
    private lateinit var tvPhone:TextView
    private lateinit var tvLocation:TextView
    private lateinit var tvDOB:TextView
    private lateinit var tvName : TextView
    private lateinit var ivProfileImg : ImageView
    private lateinit var btnEdit : FloatingActionButton

    var CONTACT_REQUEST_CODE = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        binding(view)
        btnEdit.setOnClickListener(this)
        tvPhone.setOnClickListener(this)
        tvEmail.setOnClickListener(this)
        tvDOB.setOnClickListener(this)

        addValues()
        return view
    }

    private fun binding(v: View)
    {
        tvUsername = v.findViewById(R.id.tvUsername)
        tvEmail = v.findViewById(R.id.tvEmail)
        tvPhone = v.findViewById(R.id.tvPhone)
        tvLocation = v.findViewById(R.id.tvLocation)
        tvDOB = v.findViewById(R.id.tvDOB)
        tvName = v.findViewById(R.id.tvName)
        ivProfileImg = v.findViewById(R.id.ivProfileImage)
        btnEdit = v.findViewById(R.id.btnEdit)

    }

    private fun addValues()
    {
        if(RetrofitService.online == true)
        {
            tvUsername.text = StaticData.user!!.userName
            tvEmail.text = StaticData.user!!.email
            tvPhone.text = StaticData.user!!.mobileNumber
            tvLocation.text = StaticData.user!!.address
            tvDOB.text = "Logout"
            tvName.text = "${StaticData.user!!.firstName} ${StaticData.user!!.lastName}(${StaticData.user!!.gender})"

            if(StaticData.user!!.profilePicture != "no-photo.jpg")
            {
                var imagePath = RetrofitService.loadImagePath()+StaticData.user!!.profilePicture
                imagePath = imagePath.replace("\\", "/")
                Glide.with(requireContext()).load(imagePath).into(ivProfileImg)
            }
            else
            {
                if(StaticData.user!!.gender == "Male")
                {
                    ivProfileImg.setImageResource(R.drawable.boy)
                }
                else
                {
                    ivProfileImg.setImageResource(R.drawable.girl)
                }
            }
        }
        else
        {
            var pref = requireActivity().getSharedPreferences("credentials",Context.MODE_PRIVATE)
            var fn = pref.getString("fn","")
            var ln = pref.getString("ln","")
            var email = pref.getString("email","")
            var dob = pref.getString("dob","")
            var phone = pref.getString("phone","")
            var username = pref.getString("username","")
            var gender = pref.getString("gender","")
            var address = pref.getString("address","")

            tvUsername.text = username
            tvEmail.text = email
            tvPhone.text = phone
            tvLocation.text = address
            tvDOB.text = dob
            tvName.text = "${fn} ${ln}(${gender})"
        }

    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnEdit -> {
                if(RetrofitService.online == true)
                {
                    val intent = Intent(requireContext(), UserEditActivity::class.java)
                    startActivity(intent)
                }
                else
                {
                    btnEdit.snackbar("No Internet Connection")
                }
            }

            R.id.tvPhone -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("tel:" + tvPhone.text.toString())
                startActivity(intent)
            }

            R.id.tvEmail -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + tvEmail.text.toString()))
                intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject")
                intent.putExtra(Intent.EXTRA_TEXT, "your_text")
                startActivity(intent)
            }

            R.id.tvDOB->{
                Logout(requireActivity(),requireContext()).logout()
            }



        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item!!.itemId)
        {
            R.id.giveaway ->{
                var intent = Intent(requireContext(),GiveawayActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

}