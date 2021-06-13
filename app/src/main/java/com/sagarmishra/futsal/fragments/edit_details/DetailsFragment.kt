package com.sagarmishra.futsal.fragments.edit_details

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.sagarmishra.futsal.LoginActivity
import com.sagarmishra.futsal.MainActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.AuthUser
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.dateToAgeConverter
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class DetailsFragment : Fragment(),View.OnClickListener {
    private lateinit var etFn : TextInputEditText
    private lateinit var etLn : TextInputEditText
    private lateinit var etEmail : TextInputEditText
    private lateinit var etUn : TextInputEditText
    private lateinit var etDOB : TextInputEditText
    private lateinit var etAd : TextInputEditText
    private lateinit var etMb : TextInputEditText
    private lateinit var datePickerDialog : DatePickerDialog
    private lateinit var btnEdit : Button
    private lateinit var rgGender:RadioGroup
    var gender = "Male"
    private lateinit var editTexts:MutableList<EditText>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        binding(view)
        initialize()
        etDOB.setOnClickListener(this)
        rgGender.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId)
            {
                R.id.rbMale ->{
                    gender = "Male"
                }
                R.id.rbFemale->{
                    gender = "Female"
                }
                R.id.rbOthers-> {
                    gender = "Others"
                }
            }
        }
        btnEdit.setOnClickListener(this)
        editTexts = mutableListOf(
            etFn,etLn,etAd,etDOB,etEmail,etUn,etMb
        )
        return view
    }

    private fun binding(v:View)
    {
        etFn = v.findViewById(R.id.etFn)
        etLn = v.findViewById(R.id.etLn)
        etUn = v.findViewById(R.id.etUn)
        etEmail = v.findViewById(R.id.etEmail)
        etAd = v.findViewById(R.id.etAd)
        etMb = v.findViewById(R.id.etMb)
        etDOB = v.findViewById(R.id.etDOB)
        btnEdit = v.findViewById(R.id.btnEdits)
        rgGender = v.findViewById(R.id.rgGender)
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

        else if(TextUtils.isEmpty(etDOB.text))
        {
            var snk = Snackbar.make(etDOB,"Insert your Date of Birth", Snackbar.LENGTH_INDEFINITE)
            snk.setAction("Ok",View.OnClickListener {
                snk.dismiss()
            })
            snk.show()
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
        else if(dateToAgeConverter(etDOB.text.toString()) < 8 || dateToAgeConverter(etDOB.text.toString()) > 80)
        {
            var snk = Snackbar.make(etDOB,"Age range valids from 8-80", Snackbar.LENGTH_INDEFINITE)
            snk.setAction("Ok",View.OnClickListener {
                snk.dismiss()
            })
            snk.show()
            return false
        }


        else
        {
            return true
        }
    }


    private fun initialize()
    {
        etFn.setText(StaticData.user!!.firstName)
        etLn.setText(StaticData.user!!.lastName)
        etUn.setText(StaticData.user!!.userName)
        etEmail.setText(StaticData.user!!.email)
        etAd.setText(StaticData.user!!.address)
        etMb.setText(StaticData.user!!.mobileNumber)
        etDOB.setText(StaticData.user!!.dob)
        when(StaticData.user!!.gender)
        {
            "Male"->{
                rgGender.check(R.id.rbMale)
                gender="Male"
            }
            "Female"->{
                rgGender.check(R.id.rbFemale)
                gender="Female"
            }
            "Others"->{
                rgGender.check(R.id.rbOthers)
                gender="Others"
            }
        }
    }

    private fun editDetails()
    {
        if(validation())
        {
            var editObj = AuthUser(firstName = etFn.text.toString(),lastName = etLn.text.toString(),userName = etUn.text.toString(),email = etEmail.text.toString(),gender=gender,mobileNumber = etMb.text.toString(),address = etAd.text.toString(),dob = etDOB.text.toString())
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = UserRepository()
                    val response = repo.updateDetails(editObj)
                    if(response.success == true)
                    {
                        withContext(Dispatchers.Main)
                        {
                            alert("Details Changed","Details has been changed successfully!!")
                            var nextPhase = AuthUser(firstName = etFn.text.toString(),lastName = etLn.text.toString(),userName = etUn.text.toString(),email = etEmail.text.toString(),gender=gender,mobileNumber = etMb.text.toString(),address = etAd.text.toString(),dob = etDOB.text.toString(),profilePicture = StaticData.user!!.profilePicture)
                            StaticData.user = nextPhase
                            clear()
                        }
                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            val snk = Snackbar.make(etDOB,"${response.message}",Snackbar.LENGTH_INDEFINITE)
                            snk.setAction("OK",View.OnClickListener {
                                snk.dismiss()
                            })
                            snk.show()
                        }
                    }
                }
                catch (ex:Exception)
                {
                    withContext(Dispatchers.Main)
                    {
                        var snack =Snackbar.make(etDOB,"${ex.toString()}",Snackbar.LENGTH_INDEFINITE)
                        snack.setAction("Cancel",View.OnClickListener {
                            snack.dismiss()
                        })
                        snack.show()
                        println(ex.printStackTrace())
                    }
                }
            }
        }
    }


    private fun alert(title:String,msg:String)
    {
        var builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(msg)

        builder.setPositiveButton("Ok")
        {
                dialog: DialogInterface?, which: Int ->
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("FRAGMENT_NUMBER",4)
            startActivity(intent)
            requireActivity().finish()
        }

        var alertWork : AlertDialog = builder.create()
        alertWork.setCancelable(false)
        alertWork.show()
    }

    private fun clear()
    {
        for(i in editTexts)
        {
            i.text!!.clear()
        }

        rgGender.check(R.id.rbMale)
        gender = "Male"
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
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
            R.id.btnEdits->{
                if(RetrofitService.online == true)
                {
                    editDetails()
                }
                else
                {
                    btnEdit.snackbar("No Internet Connection")
                }
            }
        }
    }
}