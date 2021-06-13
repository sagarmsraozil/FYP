package com.sagarmishra.futsal.fragments.booking_screens

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.BookingRepository
import com.sagarmishra.futsal.Repository.FutsalRepository
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.Booking
import com.sagarmishra.futsal.entityapi.FutsalInstances
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.notification.NotificationSender
import com.sagarmishra.futsal.utils.snackbar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BookingFormFragment : Fragment(),View.OnClickListener {
    private lateinit var vp :ViewPager2

    private lateinit var ivFutsal:CircleImageView
    private lateinit var tvDate:TextView
    private lateinit var tvDay:TextView
    private lateinit var tvTime:TextView
    private lateinit var tvPrice:TextView
    private lateinit var tvUser:TextView
    private lateinit var tvFutsalName:TextView
    private lateinit var btnBook : Button
    private lateinit var etMobileNumber : TextInputEditText
    private lateinit var etTeam:TextInputEditText
    private lateinit var cbCheckBox: CheckBox
    var intentResult:FutsalInstances? = null
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_booking_form, container, false)
        binding(view)
        initialize()
        btnBook.setOnClickListener(this)
        cbCheckBox.setOnClickListener(this)
        return view
    }

    private fun binding(v:View?)
    {
        vp = requireActivity().findViewById(R.id.vp)
        ivFutsal = v!!.findViewById(R.id.ivFutsal)
        tvDate = v!!.findViewById(R.id.tvDate)
        tvDay = v!!.findViewById(R.id.tvDay)
        tvTime = v!!.findViewById(R.id.tvTime)
        tvPrice = v!!.findViewById(R.id.tvPrice)
        tvUser = v!!.findViewById(R.id.tvUser)
        tvFutsalName = v!!.findViewById(R.id.tvFutsalName)
        btnBook = v!!.findViewById(R.id.btnBook)
        etMobileNumber = v!!.findViewById(R.id.etMobileNumber)
        etTeam = v!!.findViewById(R.id.etTeam)
        cbCheckBox = v!!.findViewById(R.id.cbCheckBox)
    }

    private fun initialize()
    {
        intentResult = requireActivity().intent.getParcelableExtra("timeInstance")
        tvDate.text = intentResult!!.date
        tvDay.text = intentResult!!.day
        tvTime.text = intentResult!!.time
        if(intentResult!!.superPrice > 0)
        {
            tvPrice.text = "Rs "+intentResult!!.superPrice.toString()
        }
        else
        {
            tvPrice.text = "Rs "+intentResult!!.price.toString()
        }
        tvUser.text = StaticData.user!!.userName
        tvFutsalName.text = intentResult!!.futsal_id!!.futsalName

        btnBook.isEnabled = false
    }

    private fun validation():Boolean
    {
        if(TextUtils.isEmpty(etMobileNumber.text))
        {
            etMobileNumber.error = "Insert mobile number!!"
            etMobileNumber.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etTeam.text))
        {
            etTeam.error = "Insert teammate or opponent's number!!"
            etMobileNumber.requestFocus()
            return false
        }
        else if(etMobileNumber.text.toString().length > 10 || etMobileNumber.text.toString().length < 10)
        {
            etMobileNumber.error = "Invalid mobile number length!!"
            etMobileNumber.requestFocus()
            return false
        }
        else if(etTeam.text.toString().length > 10 || etTeam.text.toString().length < 10)
        {
            etTeam.error = "Invalid mobile number length!!"
            etTeam.requestFocus()
            return false
        }
        else
        {
            return true
        }
    }

    private fun bookTheInstance()
    {
        if(validation())
        {
            val bookingRecord = Booking(futsalInstance_id = intentResult!!,mobileNumber = etMobileNumber.text.toString(),teamMate_mobileNumber = etTeam.text.toString())
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = FutsalRepository()
                    val response = repo.checkTimeInstanceAvailability(intentResult!!._id)

                    if(response.success == true)
                    {
                        withContext(Dispatchers.Main)
                        {
                            val dialog = Dialog(requireContext())
                            dialog.setContentView(R.layout.alert_confirm)
                            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                            dialog.setCancelable(true)
                            var etConfirm : TextInputEditText = dialog.findViewById(R.id.etConfirm)
                            var confirmation : Button = dialog.findViewById(R.id.btnConfirm)
                            etConfirm.requestFocus()
                            etConfirm.doOnTextChanged { text, start, before, count ->
                                confirmation.isEnabled = etConfirm.text.toString().toUpperCase().trim() == "CONFIRM"
                            }
                            confirmation.setOnClickListener {
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        val repo2 = BookingRepository()
                                        val response2 = repo2.bookFutsal(bookingRecord)

                                        if(response2.success == true)
                                        {
                                            withContext(Dispatchers.Main)
                                            {
                                                NotificationSender(requireContext(),"${response2.message}","").createHighPriority()
                                                StaticData.bookedInstanceId = response2.id
                                                vp.currentItem = 1
                                            }
                                        }
                                        else
                                        {
                                            withContext(Dispatchers.Main)
                                            {
                                                tvDate.snackbar("${response2.message}")
                                            }

                                        }
                                    }
                                    catch (ex:Exception)
                                    {
                                        withContext(Dispatchers.Main)
                                        {
                                            tvDate.snackbar("${ex.toString()}")
                                        }
                                    }
                                }
                                dialog.cancel()
                            }
                            dialog.show()

                        }


                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            val snk = Snackbar.make(tvDate,"${response.message}",
                                Snackbar.LENGTH_INDEFINITE)
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
                        val snk = Snackbar.make(tvDate,"Sorry! We are having some problem.",Snackbar.LENGTH_INDEFINITE)
                        snk.setAction("OK",View.OnClickListener {
                            snk.dismiss()
                        })
                        println(ex.printStackTrace())
                        snk.show()
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnBook ->{
                if(RetrofitService.online == true)
                {
                    bookTheInstance()
                }
                else
                {
                    tvDate.snackbar("No Internet Connection")
                }
            }

            R.id.cbCheckBox ->{
                btnBook.isEnabled = cbCheckBox.isChecked
            }
        }
    }

}