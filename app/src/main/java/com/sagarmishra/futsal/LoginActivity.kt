package com.sagarmishra.futsal

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.db.FutsalRoomDB
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.notification.NotificationSender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class LoginActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var ivFutsal : ImageView
    private lateinit var tvWelcome : TextView
    private lateinit var btnFb : View
    private lateinit var btnInsta : View
    private lateinit var tvTitle : TextView
    private lateinit var tvDont : TextView
    private lateinit var tvSignup : TextView
    private lateinit var etUn : TextInputEditText
    private lateinit var etPw : TextInputEditText
    private lateinit var btnLogin : Button
    private lateinit var cbRemember : CheckBox
    private lateinit var constraint:ConstraintLayout
    private lateinit var btnForgot : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        binding()
        //animations()
        initialize()
        listeners()
    }

    private fun binding()
    {
        ivFutsal = findViewById(R.id.ivFutsal)
        tvWelcome = findViewById(R.id.textView)
        btnFb = findViewById(R.id.btnFb)
        btnInsta = findViewById(R.id.btnInsta)
        tvTitle = findViewById(R.id.textTitle)
        tvDont = findViewById(R.id.tvDont)
        tvSignup = findViewById(R.id.tvSignUp)
        btnLogin = findViewById(R.id.btnLogin)
        etUn = findViewById(R.id.etUsername)
        etPw = findViewById(R.id.etPassword)
        cbRemember = findViewById(R.id.cbRemember)
        constraint = findViewById(R.id.constraint)
        btnForgot = findViewById(R.id.btnForgotPassword)
        etUn.requestFocus()
    }

    private fun animations()
    {
        ivFutsal.translationY = -400F
        tvWelcome.alpha = 0.3F
        tvTitle.alpha = 0.3F
        tvWelcome.translationX = -400F
        tvTitle.translationX = 700F
        btnFb.translationY = 200F
        btnInsta.translationY = 200F
        tvDont.translationX = -500F
        tvSignup.translationX = 500F
        tvDont.alpha = 0.3F
        tvSignup.alpha = 0.3F

       ivFutsal.animate().translationY(0F).setDuration(1000).setStartDelay(200)
        tvTitle.animate().translationX(0F).alpha(1.0F).setDuration(1000).setStartDelay(600)
       tvWelcome.animate().translationX(0F).alpha(1.0F).setDuration(1000).setStartDelay(900)
        tvDont.animate().translationX(0F).alpha(1.0F).setDuration(1000).setStartDelay(1300)
        tvSignup.animate().translationX(0F).alpha(1.0F).setDuration(1000).setStartDelay(1600)
        btnFb.animate().translationY(0F).setDuration(1000).setStartDelay(2000)
        btnInsta.animate().translationY(0F).setDuration(1000).setStartDelay(2300)
    }

    private fun initialize()
    {

    }

    private fun listeners()
    {
        tvSignup.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
        btnForgot.setOnClickListener(this)
    }

    private fun validation():Boolean
    {
        if(TextUtils.isEmpty(etUn.text))
        {
            etUn.error = "Insert Username"
            etUn.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etPw.text))
        {
            etPw.error = "Insert Password"
            etPw.requestFocus()
            return false
        }
        else
        {
            return true
        }
    }

    private fun saveUserData()
    {
        var getPref = getSharedPreferences("credentials", Context.MODE_PRIVATE)
        var editor = getPref.edit()

        editor.putString("username",etUn.text.toString())
        editor.putString("password",etPw.text.toString())
        editor.putString("fn",StaticData.user!!.firstName)
        editor.putString("ln",StaticData.user!!.lastName)
        editor.putString("gender",StaticData.user!!.gender)
        editor.putString("phone",StaticData.user!!.mobileNumber)
        editor.putString("email",StaticData.user!!.email)
        editor.putString("dob",StaticData.user!!.dob)
        editor.putString("address",StaticData.user!!.address)


        editor.apply()


    }

    private fun userAuthentication()
    {
        if(validation())
        {
            CoroutineScope(Dispatchers.IO).launch {
               try {
                   val repo = UserRepository()
                   val response = repo.authenticateUser(etUn.text.toString(),etPw.text.toString())

                   if(response.success == true)
                   {

                       RetrofitService.token = "Bearer "+response.token
                       StaticData.user = response.data
                       if(cbRemember.isChecked == true)
                       {
                           saveUserData()
                       }
                       withContext(Main)
                       {
                           NotificationSender(this@LoginActivity,"Logged in successfully!!","").createHighPriority()
                           clear()
                       }
                       val intent = Intent(this@LoginActivity,MainActivity::class.java)
                       startActivity(intent)
                       finish()
                   }
                   else
                   {
                       withContext(Main)
                       {
                           var snack =Snackbar.make(constraint,"${response.message}",Snackbar.LENGTH_INDEFINITE)
                           snack.setAction("Cancel",View.OnClickListener {
                               snack.dismiss()
                           })
                           snack.show()

                       }
                   }
               }
               catch (err:Exception)
               {
                   withContext(Main)
                   {
                       var snack =Snackbar.make(constraint,"404 Server Error.\nWe will soon fix it. Thanks for your patience:)",Snackbar.LENGTH_INDEFINITE)
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
        etUn.text!!.clear()
        etPw.text!!.clear()
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.tvSignUp ->{
                val intent = Intent(this,SignUpActivity::class.java)
                startActivity(intent)
                finish()
            }

            R.id.btnLogin ->{
                userAuthentication()
            }

            R.id.btnForgotPassword ->{
                val intent = Intent(this@LoginActivity,ForgotPasswordActivity::class.java)
                startActivity(intent)
            }
        }
    }
}