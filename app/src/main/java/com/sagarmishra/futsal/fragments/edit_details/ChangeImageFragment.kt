package com.sagarmishra.futsal.fragments.edit_details

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.PopupMenu
import com.bumptech.glide.Glide
import com.sagarmishra.futsal.MainActivity
import com.sagarmishra.futsal.Permissions.Permission
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.UserRepository
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class ChangeImageFragment : Fragment(),View.OnClickListener,PopupMenu.OnMenuItemClickListener {
    private lateinit var cvProfilePic:CircleImageView
    private lateinit var btnAdd:Button
    var GALLERY_REQUEST_CODE = 0
    var CAMERA_REQUEST_CODE = 1
    var image_url = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_change_image, container, false)
        var permissions = Permission(requireContext(),requireActivity())
        if(!permissions.checkPermissions())
        {
            permissions.requestPermissions()
        }
        cvProfilePic = view.findViewById(R.id.cvProfilePic)
        btnAdd = view.findViewById(R.id.btnAdd)
        cvProfilePic.setOnClickListener(this)
        btnAdd.setOnClickListener(this)
        if(StaticData.user!!.profilePicture != "no-photo.jpg")
        {
            var path = RetrofitService.loadImagePath()+StaticData.user!!.profilePicture
            path=path.replace("\\","/")
            Glide.with(requireContext()).load(path).into(cvProfilePic)
        }
        else
        {
            if(StaticData.user!!.gender == "Male")
            {
                cvProfilePic.setImageResource(R.drawable.boy)
            }
            else
            {
                cvProfilePic.setImageResource(R.drawable.girl)
            }
        }
        return view
    }

    private fun showPopUp()
    {
        val pop = PopupMenu(requireContext(),cvProfilePic)
        pop.menuInflater.inflate(R.menu.gallery_camera,pop.menu)
        pop.setOnMenuItemClickListener(this)
        pop.show()
    }
    private fun uploadImage()
    {
        if(image_url != "")
        {
            val file = File(image_url)
            val extension = MimeTypeMap.getFileExtensionFromUrl(image_url)
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            val req_body = RequestBody.create(MediaType.parse(mimeType),file)
            val body = MultipartBody.Part.createFormData("profileImg",file.name,req_body)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = UserRepository()
                    val response = repo.changeProfilePicture(body)
                    if(response.success == true)
                    {
                        try {
                            val response2 = repo.getUser()
                            if(response2.success == true)
                            {
                                StaticData.user!!.profilePicture = response2.data!!.profilePicture
                            }
                            else
                            {
                                withContext(Dispatchers.Main)
                                {
                                    cvProfilePic.snackbar("${response.message}")

                                }
                            }
                        }
                        catch (ex:Exception)
                        {
                            withContext(Dispatchers.Main)
                            {
                                cvProfilePic.snackbar("Sorry we are having some problem!!")
                                println(ex.printStackTrace())
                            }

                        }
                        withContext(Dispatchers.Main)
                        {
                            alert("${response.message}","",1)

                        }
                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            cvProfilePic.snackbar("${response.message}")

                        }
                    }
                }
                catch (ex:Exception)
                {
                    withContext(Dispatchers.Main)
                    {
                        cvProfilePic.snackbar("Sorry we are having some problem!!")
                        println(ex.printStackTrace())
                    }

                }
            }
        }
        else
        {
            alert("You havenot added picture!!","Do you want to have a empty image for your profile? If yes,continue",0)
        }
    }

    private fun alert(title:String,msg:String,token:Int)
    {
        var builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(msg)
        if(token == 1)
        {
            builder.setPositiveButton("Ok")
            {
                    dialog: DialogInterface?, which: Int ->
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.putExtra("FRAGMENT_NUMBER",4)
                startActivity(intent)
                requireActivity().finish()
            }
        }
        else
        {
            builder.setPositiveButton("Continue")
            {
                    dialog: DialogInterface?, which: Int ->
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repo = UserRepository()
                        val response = repo.noImage()
                        if(response.success == true)
                        {
                            try {
                                val response2 = repo.getUser()
                                if(response2.success == true)
                                {
                                    StaticData.user!!.profilePicture = response2.data!!.profilePicture
                                }
                                else
                                {
                                    withContext(Dispatchers.Main)
                                    {
                                        cvProfilePic.snackbar("${response.message}")

                                    }
                                }
                            }
                            catch (ex:Exception)
                            {
                                withContext(Dispatchers.Main)
                                {
                                    cvProfilePic.snackbar("Sorry we are having some problem!!")
                                    println(ex.printStackTrace())
                                }

                            }
                            withContext(Dispatchers.Main)
                            {
                                alert("${response.message}","",1)
                            }
                        }
                        else
                        {
                            withContext(Dispatchers.Main)
                            {
                                cvProfilePic.snackbar("${response.message}")

                            }
                        }
                    }
                    catch (ex:Exception)
                    {
                        withContext(Dispatchers.Main)
                        {
                            cvProfilePic.snackbar("Sorry we are having some problem!!")
                            println(ex.printStackTrace())
                        }

                    }
                }
//                val intent = Intent(requireContext(), MainActivity::class.java)
//                intent.putExtra("FRAGMENT_NUMBER",4)
//                startActivity(intent)

            }
            builder.setNeutralButton("Cancel")
            { dialogInterface: DialogInterface, i: Int ->

            }
        }

        var alertWork : AlertDialog = builder.create()
        alertWork.setCancelable(false)
        alertWork.show()
    }



    override fun onClick(v: View?) {
       when(v!!.id)
       {
           R.id.cvProfilePic ->{
               showPopUp()
           }
           R.id.btnAdd ->{
               if(RetrofitService.online == true)
               {
                   uploadImage()
               }
               else
               {
                   btnAdd.snackbar("No Internet Connection")
               }
           }
       }
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