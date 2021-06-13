package com.sagarmishra.futsal.fragments.registration

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.sagarmishra.futsal.LoginActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.model.StaticData
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class ProfilePictureFragment : Fragment(),View.OnClickListener,PopupMenu.OnMenuItemClickListener {
    private lateinit var cvProfilePic : CircleImageView
    private lateinit var btnAdd:Button
    private lateinit var tvSkip:TextView
    var GALLERY_REQUEST_CODE = 1
    var CAMERA_REQUEST_CODE = 2
    var image_url = ""
    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile_picture, container, false)

        if(!checkPermissions())
        {
            requestPermission()
        }
        binding(view)
        listener()
        return view
    }

    private fun checkPermissions():Boolean
    {
        var flag = true
        for(i in permissions)
        {
            if(ActivityCompat.checkSelfPermission(requireContext(),i) != PackageManager.PERMISSION_GRANTED)
            {
                flag = false
            }
        }
        return flag
    }


    private fun requestPermission()
    {
        ActivityCompat.requestPermissions(requireActivity(),permissions,100)
    }

    private fun binding(v:View)
    {
        cvProfilePic = v.findViewById(R.id.cvProfilePic)
        btnAdd = v.findViewById(R.id.btnAdd)
        tvSkip = v.findViewById(R.id.tvSkip)
    }

    private fun listener()
    {
        cvProfilePic.setOnClickListener(this)
        btnAdd.setOnClickListener(this)
        tvSkip.setOnClickListener(this)
    }

    private fun showPopMenu()
    {
        val popMenu = PopupMenu(requireContext(),cvProfilePic)
        popMenu.menuInflater.inflate(R.menu.gallery_camera,popMenu.menu)
        popMenu.setOnMenuItemClickListener(this)
        popMenu.show()
    }

    private fun uploadImage()
    {
        if(image_url!="")
        {
            val file = File(image_url)
            val extension = MimeTypeMap.getFileExtensionFromUrl(image_url)
            val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            val req_file = RequestBody.create(MediaType.parse(mimetype),file)
            val body = MultipartBody.Part.createFormData("profilePic",file.name,req_file)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = UserRepository()
                    val response = repo.addProfilePic(StaticData.registration_id!!,body)
                    if(response.success == true)
                    {
                        withContext(Dispatchers.Main)
                        {
                            alert("Profile Picture added!!","Congratulations!! You have successfully registered your account in Futsal Arena:)")
                        }

                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            val snk = Snackbar.make(cvProfilePic,"${response.message}",Snackbar.LENGTH_INDEFINITE)
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
                        println(ex.printStackTrace())
                        val snk = Snackbar.make(cvProfilePic,"Sorry! We are having some problem:(",Snackbar.LENGTH_INDEFINITE)
                        snk.setAction("OK",View.OnClickListener {
                            snk.dismiss()
                        })
                        snk.show()

                    }


                }
            }
        }
        else
        {
            val snk = Snackbar.make(cvProfilePic,"You havenot added picture to upload!!",Snackbar.LENGTH_INDEFINITE)
            snk.setAction("OK",View.OnClickListener {
                snk.dismiss()
            })
            snk.show()
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnAdd->{
                uploadImage()
            }
            R.id.tvSkip->{
                alert("Registration Success!!","Congratulations!! You have successfully registered your account in Futsal Arena:)")
            }
            R.id.cvProfilePic->{
                showPopMenu()
            }
        }
    }


    private fun alert(title:String,msg:String)
    {
        var builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setNeutralButton("Cancel")
        {
                dialog: DialogInterface?, which: Int ->

        }
        builder.setPositiveButton("Go to Login Page")
        {
                dialog: DialogInterface?, which: Int ->
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)

        }

        var alertWork : AlertDialog = builder.create()
        alertWork.setCancelable(false)
        alertWork.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK)
        {
            if (requestCode == GALLERY_REQUEST_CODE && data != null) {
                //overall location of selected image
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                val contentResolver = requireActivity().contentResolver
                //locator and identifier
                val cursor =
                    contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()//moveTONext // movetolast

                val columnIndex = cursor.getColumnIndex(filePathColumn[0])

                image_url = cursor.getString(columnIndex)

                //image preview
                cvProfilePic.setImageBitmap(BitmapFactory.decodeFile(image_url))
                cursor.close()
            } else if (requestCode == CAMERA_REQUEST_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                image_url = file!!.absolutePath
                cvProfilePic.setImageBitmap(BitmapFactory.decodeFile(image_url))
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item!!.itemId)
        {
            R.id.gallery ->{
                 val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent,GALLERY_REQUEST_CODE)


            }

            R.id.camera->{

                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent,CAMERA_REQUEST_CODE)


            }
        }
        return true
    }

    private fun bitmapToFile(
        bitmap: Bitmap,
        fileNameToSave: String
    ): File? {
        var file: File? = null
        return try {
            file = File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitMapData = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }

}