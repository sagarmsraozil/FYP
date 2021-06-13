package com.sagarmishra.futsal.fragments.registration

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.sagarmishra.futsal.LoginActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.entityapi.AuthUser
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.notification.NotificationSender
import com.sagarmishra.futsal.utils.dateToAgeConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*


class RegistrationFormFragment : Fragment(),View.OnClickListener {
    private lateinit var ivFutsal : ImageView
    private lateinit var tvTitle : TextView
    private lateinit var tvView : TextView
    private lateinit var already : TextView
    private lateinit var tvLogin : TextView
    private lateinit var etFn : TextInputEditText
    private lateinit var etLn : TextInputEditText
    private lateinit var etEmail : TextInputEditText
    private lateinit var etUn : TextInputEditText
    private lateinit var etPw : TextInputEditText
    private lateinit var etCpw : TextInputEditText
    private lateinit var etDOB : TextInputEditText
    private lateinit var etAd : TextInputEditText
    private lateinit var etMb : TextInputEditText
    private lateinit var datePickerDialog : DatePickerDialog
    private lateinit var btnRegister : Button
    private lateinit var rgGender : RadioGroup
    private lateinit var cbCheck : CheckBox
    private lateinit var constraint: ConstraintLayout
    var gender = "Male"
    private lateinit var editTexts : MutableList<TextInputEditText>
    private lateinit var vp:ViewPager2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_registration_form, container, false)

        binding(view)
        // animations()
        listener()
        initialize()
        return view
    }


    private fun binding(v:View)
    {
        ivFutsal = v.findViewById(R.id.ivFutsal)
        tvTitle = v.findViewById(R.id.tvTitle)
        tvView = v.findViewById(R.id.textView)
        already = v.findViewById(R.id.alreadyAccount)
        tvLogin = v.findViewById(R.id.tvLogin)
        constraint=v.findViewById(R.id.constraint)
        etFn = v.findViewById(R.id.etFn)
        etLn = v.findViewById(R.id.etLn)
        etUn = v.findViewById(R.id.etUn)
        etEmail = v.findViewById(R.id.etEmail)
        etPw = v.findViewById(R.id.etPw)
        etCpw = v.findViewById(R.id.etCpw)
        etAd = v.findViewById(R.id.etAd)
        etMb = v.findViewById(R.id.etMb)
        etDOB = v.findViewById(R.id.etDOB)
        btnRegister = v.findViewById(R.id.btnRegister)
        rgGender = v.findViewById(R.id.rgGender)
        cbCheck = v.findViewById(R.id.cbCheck)
        etFn.requestFocus()
        vp = requireActivity().findViewById(R.id.vp)
    }

    private fun initialize()
    {
        btnRegister.isEnabled = false
        editTexts = mutableListOf(
            etUn,etLn,etFn,etMb,etAd,etCpw,etPw,etDOB,etEmail
        )
    }

    private fun animations()
    {
        ivFutsal.translationY = -400F
        tvTitle.alpha = 0.2F
        tvView.alpha = 0.2F
        tvTitle.translationX = 800F
        tvView.translationX = -400F
        already.translationY = 400F
        tvLogin.translationY = 400F

        ivFutsal.animate().translationY(0F).setDuration(1000).setStartDelay(200)
        tvTitle.animate().translationX(0F).alpha(1F).setDuration(1000).setStartDelay(600)
        tvView.animate().translationX(0F).alpha(1F).setDuration(1000).setStartDelay(900)
        already.animate().translationY(0F).setDuration(1000).setStartDelay(1400)
        tvLogin.animate().translationY(0F).setDuration(1000).setStartDelay(1800)
    }

    private fun listener()
    {
        tvLogin.setOnClickListener(this)
        etDOB.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
        cbCheck.setOnClickListener(this)

        rgGender.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId)
            {
                R.id.rbMale ->{
                    gender = "Male"
                }

                R.id.rbFemale ->{
                    gender = "Female"
                }

                R.id.rbOthers ->{
                    gender = "Others"
                }
            }
        }
    }

    private fun validation():Boolean
    {
        if(TextUtils.isEmpty(etFn.text))
        {
            etFn.error = "Insert Firstname!!"
            etFn.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etLn.text))
        {
            etLn.error = "Insert Lastname!!"
            etLn.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etEmail.text))
        {
            etEmail.error = "Insert Email"
            etEmail.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etUn.text))
        {
            etUn.error = "Insert Username"
            etUn.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etAd.text))
        {
            etAd.error = "Insert Address"
            etAd.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etMb.text))
        {
            etMb.error = "Insert Mobile number"
            etMb.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etPw.text))
        {
            etPw.error = "Insert Password!!"
            etPw.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etDOB.text))
        {
            var snk = Snackbar.make(constraint,"Insert your Date of Birth", Snackbar.LENGTH_INDEFINITE)
            snk.setAction("Ok",View.OnClickListener {
                snk.dismiss()
            })
            snk.show()
            return false
        }
        else if(TextUtils.isEmpty(etCpw.text))
        {
            etCpw.error = "Insert Password!!"
            etCpw.requestFocus()
            return false
        }
        else if(!Regex("""[a-z][a-z]+""").matches(etFn.text.toString().toLowerCase().replace(" ","")))
        {
            etFn.error = "Firstname should not contain any numeric characters"
            etFn.requestFocus()
            return false
        }
        else if(!Regex("""[a-z][a-z]+""").matches(etLn.text.toString().toLowerCase().replace(" ","")))
        {
            etLn.error = "Lastname should not contain any numeric characters"
            etLn.requestFocus()
            return false
        }
        else if(etUn.text.toString().length < 6)
        {
            etUn.error = "Username should contain atleast 6 characeters"
            etUn.requestFocus()
            return false
        }
//        else if(!Regex("[a-zA-Z0-9]{3,15}@[a-zA-Z0-9]{3,15}[.][a-zA-Z]{2,5}").matches(etEmail.text.toString()))
//        {
//            etEmail.error = "Invalid email address!!"
//            etEmail.requestFocus()
//            return false
//        }
        else if(!Regex("""\d+""").matches(etMb.text.toString()))
        {
            etMb.error = "Mobile number should not contain any alphabetical characters!!"
            etMb.requestFocus()
            return false
        }
        else if(etMb.text.toString().length>10 || etMb.text.toString().length < 10)
        {
            etMb.error = "Inappropriate mobile number length!!"
            etMb.requestFocus()
            return false
        }
        else if(etPw.text.toString().length < 6)
        {
            etPw.error = "Password should contain atleast 6 characters"
            etPw.requestFocus()
            return false
        }
        else if(dateToAgeConverter(etDOB.text.toString()) < 8 || dateToAgeConverter(etDOB.text.toString()) > 80)
        {
            Snackbar.make(etDOB,"Age range valids from 8-80", Snackbar.LENGTH_LONG).show()
            return false
        }
        else if(etPw.text.toString() != etCpw.text.toString())
        {
            Snackbar.make(etDOB,"Password mismatch", Snackbar.LENGTH_LONG).show()
            return false
        }

        else
        {
            return true
        }
    }

    private fun userRegistration()
    {
        if(validation())
        {
            var fn = etFn.text.toString()
            var ln = etLn.text.toString()
            var un = etUn.text.toString()
            var em = etEmail.text.toString()
            var pw = etPw.text.toString()
            var cpw = etCpw.text.toString()
            var mobileNumber = etMb.text.toString()
            var address = etAd.text.toString()
            var dob = etDOB.text.toString()


            val uObj: AuthUser = AuthUser(firstName = fn,lastName = ln,userName = un,password = pw,confirmPassword = cpw,email = em,gender = gender,mobileNumber = mobileNumber,address = address,dob = dob)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = UserRepository()
                    val response =repo.registerUser(uObj)
                    if(response.success == true)
                    {
                        withContext(Dispatchers.Main)
                        {
                            NotificationSender(requireContext(),"Welcome to Futsal Arena,${etFn.text}","").createLowPriority()
                            StaticData.registration_id = response.data
                            vp.currentItem = 1
                            clear()

                        }

                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            var snack =Snackbar.make(constraint,"${response.message}",Snackbar.LENGTH_INDEFINITE)
                            snack.setAction("Cancel",View.OnClickListener {
                                snack.dismiss()
                            })
                            snack.show()

                        }
                    }
                }
                catch (err: Exception)
                {
                    withContext(Dispatchers.Main)
                    {
                        var snack =Snackbar.make(constraint,"404 Server Error.\n" +
                                "We will soon fix it.Thanks for your patience:)",Snackbar.LENGTH_INDEFINITE)
                        snack.setAction("Cancel",View.OnClickListener {
                            snack.dismiss()
                        })
                        snack.show()
                        println(err.printStackTrace())
                    }
                }
            }
        }
    }


    private fun clear()
    {
        for(i in editTexts)
        {
            i.text!!.clear()
        }
        cbCheck.isChecked = false
        rgGender.check(R.id.rbMale)
        gender = "Male"
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {

            R.id.tvLogin ->{

                val intent = Intent(requireContext(),LoginActivity::class.java)
                startActivity(intent)

            }

            R.id.etDOB ->{
                var cal = Calendar.getInstance()
                var year = cal.get(Calendar.YEAR)
                var month = cal.get(Calendar.MONTH)
                var day = cal.get(Calendar.DATE)
                var cal2 = Calendar.getInstance()
                cal2.add(Calendar.DATE,-(365*80))
                datePickerDialog = DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth -> etDOB.setText("${year}-${month+1}-${dayOfMonth}") } ,year,month,day)
                datePickerDialog.datePicker.maxDate = cal.timeInMillis
                datePickerDialog.datePicker.minDate = cal2.timeInMillis
                datePickerDialog.show()
            }

            R.id.btnRegister ->{

                userRegistration()
            }

            R.id.cbCheck ->{
                btnRegister.isEnabled = cbCheck.isChecked == true
            }
        }
    }

}