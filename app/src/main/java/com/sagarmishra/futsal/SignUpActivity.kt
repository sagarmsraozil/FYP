package com.sagarmishra.futsal

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat

import com.google.android.material.textfield.TextInputEditText

import com.google.android.material.snackbar.Snackbar
import com.sagarmishra.futsal.Repository.UserRepository

import com.sagarmishra.futsal.db.FutsalRoomDB
import com.sagarmishra.futsal.entityapi.AuthUser
import com.sagarmishra.futsal.entityroom.User
import com.sagarmishra.futsal.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*


class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
       supportActionBar?.hide()

    }



}