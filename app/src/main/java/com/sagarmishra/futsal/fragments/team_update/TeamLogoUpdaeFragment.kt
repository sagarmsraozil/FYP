package com.sagarmishra.futsal.fragments.team_update

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.PopupMenu
import com.bumptech.glide.Glide
import com.sagarmishra.futsal.MainActivity
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.TeamRepository
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception


class TeamLogoUpdaeFragment : Fragment(),View.OnClickListener {
    private lateinit var cvLogo:CircleImageView
    private lateinit var btnChange:Button
    var img_url = ""
    var GALLERY_REQUEST_CODE = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_team_logo_updae, container, false)
        cvLogo = view.findViewById(R.id.cvLogo)
        btnChange = view.findViewById(R.id.btnChange)
        cvLogo.setOnClickListener(this)
        btnChange.setOnClickListener(this)
        if(StaticData.team!!.teamLogo == "no-logo.png")
        {
            cvLogo.setImageResource(R.drawable.logoteam)
        }
        else
        {
            var imgPath = RetrofitService.loadImagePath()+StaticData.team!!.teamLogo!!.replace("\\","/")
            Glide.with(requireContext()).load(imgPath).into(cvLogo)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
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


                img_url= cursor.getString(columnIndex)

                cvLogo.setImageBitmap(BitmapFactory.decodeFile(img_url))

                cursor.close()
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.cvLogo ->{
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent,GALLERY_REQUEST_CODE)
            }

            R.id.btnChange ->{
                if(img_url != "")
                {
                    var file = File(img_url)

                    var extension = MimeTypeMap.getFileExtensionFromUrl(img_url)
                    var mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

                    var reqBody = RequestBody.create(MediaType.parse(mime),file)
                    var idBody = RequestBody.create(MediaType.parse("text/plain"),StaticData.team!!._id)
                    var body = MultipartBody.Part.createFormData("teamLogo",file.name,reqBody)

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val repo = TeamRepository()
                            val response = repo.uploadLogo(body,idBody)
                            if(response.success == true)
                            {
                                withContext(Dispatchers.Main)
                                {
                                    val intent = Intent(requireContext(),MainActivity::class.java)
                                    intent.putExtra("FRAGMENT_NUMBER",8)
                                    startActivity(intent)
                                }
                            }
                            else
                            {
                                withContext(Dispatchers.Main)
                                {
                                    btnChange.snackbar(response.message!!)
                                }
                            }
                        }
                        catch (err:Exception)
                        {
                            println(err.printStackTrace())
                            withContext(Dispatchers.Main)
                            {
                                btnChange.snackbar("Server Error!!")
                            }
                        }
                    }

                }
                else
                {
                    cvLogo.snackbar("Please select image to update logo.")
                }
            }
        }

    }


}