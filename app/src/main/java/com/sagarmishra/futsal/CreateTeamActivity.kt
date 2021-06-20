package com.sagarmishra.futsal

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception

@Suppress("Deprecation")
class CreateTeamActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var etTeamName:TextInputEditText
    private lateinit var etTeamTag:TextInputEditText
    private lateinit var etAddress:TextInputEditText
    private lateinit var etContact:TextInputEditText
    private lateinit var etAgeGroup:TextInputEditText
    private lateinit var etTeamEmail:TextInputEditText
    private lateinit var btnFront:Button
    private lateinit var tvFrontUrl:TextView
    private lateinit var btnBack:Button
    private lateinit var tvBackUrl:TextView
    private lateinit var btnOwner:Button
    private lateinit var tvOwnerUrl:TextView
    private lateinit var cbCheck:CheckBox
    private lateinit var btnCreate:Button

    var index = -1
    var GALLERY_REQUEST_CODE = 1

    var frontUrl = ""
    var backUrl = ""
    var ownerUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        supportActionBar?.hide()
        binding()
        listeners()
    }

    private fun binding()
    {
        etTeamName = findViewById(R.id.etTeamName)
        etTeamTag = findViewById(R.id.etTeamTag)
        etAddress = findViewById(R.id.etAddress)
        etContact = findViewById(R.id.etContact)
        etAgeGroup = findViewById(R.id.etAgeGroup)
        btnFront = findViewById(R.id.btnFront)
        tvFrontUrl = findViewById(R.id.tvFrontUrl)
        btnBack = findViewById(R.id.btnBack)
        tvBackUrl = findViewById(R.id.tvBackUrl)
        btnOwner = findViewById(R.id.btnOwner)
        tvOwnerUrl = findViewById(R.id.tvOwnerUrl)
        cbCheck = findViewById(R.id.cbCheck)
        btnCreate = findViewById(R.id.btnCreate)
        etTeamEmail = findViewById(R.id.etTeamEmail)
    }

    private fun listeners()
    {
        btnFront.setOnClickListener(this)
        btnBack.setOnClickListener(this)
        btnOwner.setOnClickListener(this)
        btnCreate.setOnClickListener(this)
        cbCheck.setOnClickListener(this)
    }


    private fun mediaStore()
    {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE && data != null) {
                //overall location of selected image
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                val contentResolver = contentResolver
                //locator and identifier
                val cursor =
                    contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()//moveTONext // movetolast

                val columnIndex = cursor.getColumnIndex(filePathColumn[0])

                if(index == 0)
                {
                    frontUrl = cursor.getString(columnIndex)
                    tvFrontUrl.text = frontUrl
                }

                if(index == 1)
                {
                    backUrl = cursor.getString(columnIndex)
                    tvBackUrl.text = backUrl
                }

                if(index == 2)
                {
                    ownerUrl = cursor.getString(columnIndex)
                    tvOwnerUrl.text = ownerUrl
                }

                cursor.close()
            }
        }
    }

    private fun alert(title:String,message:String)
    {
        var alert = AlertDialog.Builder(this)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("Ok"){ dialogInterface: DialogInterface, i: Int ->

        }

        val alertD = alert.create()
        alertD.setCancelable(false)
        alertD.show()
    }

    private fun validation():Boolean
    {
        if(TextUtils.isEmpty(etTeamName.text))
        {
            etTeamName.error = "Insert team name"
            etTeamName.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etTeamTag.text))
        {
            etTeamTag.error = "Insert team tag"
            etTeamTag.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etAddress.text))
        {
            etAddress.error = "Insert address"
            etAddress.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etTeamEmail.text))
        {
            etTeamEmail.error = "Insert email address"
            etTeamEmail.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etContact.text))
        {
            etContact.error = "Insert contact"
            etContact.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(etAgeGroup.text))
        {
            etAgeGroup.error = "Insert Age Group"
            etAgeGroup.requestFocus()
            return false
        }
        else if(TextUtils.isEmpty(frontUrl))
        {
            alert("Error","Add frontside of your citizenship card.")
            return false
        }
        else if(TextUtils.isEmpty(backUrl))
        {
            alert("Error","Add backside of your citizenship card.")
            return false
        }
        else if(TextUtils.isEmpty(ownerUrl))
        {
            alert("Error","Add owner's photo.")
            return false
        }
        else if(etAgeGroup.text.toString().toInt() < 8 && etAgeGroup.text.toString().toInt() > 55)
        {
            etAgeGroup.error = "Teams age group is valid from range 8-55"
            etAgeGroup.requestFocus()
            return false
        }
        else
        {
            return true
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnFront ->{
                index = 0
                mediaStore()
            }
            R.id.btnBack ->{
               index = 1
                mediaStore()
            }
            R.id.btnOwner ->{
                index = 2
                mediaStore()
            }
            R.id.btnCreate ->{
                if(validation())
                {
                    val citizenFront = File(frontUrl)
                    val citizenBack = File(backUrl)
                    val owner = File(ownerUrl)

                    val extension = MimeTypeMap.getFileExtensionFromUrl(frontUrl)
                    val extension2 = MimeTypeMap.getFileExtensionFromUrl(backUrl)
                    val extension3 = MimeTypeMap.getFileExtensionFromUrl(ownerUrl)

                    val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                    val mimetype2 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension2)
                    val mimetype3 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension3)

                    val req_file = RequestBody.create(MediaType.parse(mimetype),citizenFront)
                    val req_file2 = RequestBody.create(MediaType.parse(mimetype2),citizenBack)
                    val req_file3 = RequestBody.create(MediaType.parse(mimetype3),owner)
                    val teamName = RequestBody.create(MediaType.parse("text/plain"),etTeamName.text.toString())
                    val teamTag = RequestBody.create(MediaType.parse("text/plain"),etTeamTag.text.toString())
                    val teamEmail = RequestBody.create(MediaType.parse("text/plain"),etTeamEmail.text.toString())
                    val contact = RequestBody.create(MediaType.parse("text/plain"),etContact.text.toString())
                    val age = RequestBody.create(MediaType.parse("text/plain"),etAgeGroup.text.toString())
                    val locatedCity = RequestBody.create(MediaType.parse("text/plain"),etAddress.text.toString())


                    val body1 = MultipartBody.Part.createFormData("citizenshipFront",citizenFront.name,req_file)
                    val body2 = MultipartBody.Part.createFormData("citizenshipBack",citizenBack.name,req_file2)
                    val body3 = MultipartBody.Part.createFormData("ownerPhoto",owner.name,req_file3)

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val repo = TeamRepository()
                            val response = repo.createTeam(teamName,teamTag,locatedCity,age,contact,teamEmail,body2,body1,body3)
                            if(response.success == true)
                            {
                                withContext(Dispatchers.Main)
                                {
                                    alert("Successful",response.message!!)
                                    var intent = Intent(this@CreateTeamActivity,MainActivity::class.java)
                                    intent.putExtra("FRAGMENT_NUMBER",8)
                                    startActivity(intent)
                                }
                            }
                            else
                            {
                                var errorBox = response.error!!.values.toMutableList().joinToString("\n")
                                withContext(Dispatchers.Main)
                                {
                                    alert("Error",errorBox)
                                }
                            }
                        }
                        catch(err:Exception)
                        {
                            println(err.printStackTrace())
                            withContext(Dispatchers.Main)
                            {
                                etTeamEmail.snackbar("Server Error!!")
                            }
                        }
                    }

                }
            }

            R.id.cbCheck->{
                if(cbCheck.isChecked == true)
                {
                    btnCreate.isEnabled = true
                }
                else
                {
                    btnCreate.isEnabled = false
                }
            }
        }
    }

}