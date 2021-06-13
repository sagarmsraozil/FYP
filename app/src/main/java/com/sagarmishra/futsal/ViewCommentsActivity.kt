package com.sagarmishra.futsal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.sagarmishra.futsal.Repository.CommentRepository
import com.sagarmishra.futsal.adapter.CommentRetrieveAdapter
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.Futsal
import com.sagarmishra.futsal.entityapi.FutsalComment
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ViewCommentsActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var tvFutsal:TextView
    private lateinit var tvUser:TextView
    private lateinit var ivProfile:CircleImageView
    private lateinit var etComment:EditText
    private lateinit var tvSend:TextView
    private lateinit var recycler:RecyclerView
//    private lateinit var swipe:SwipeRefreshLayout
    private  var futsal:Futsal? = null
    private lateinit var adapter:CommentRetrieveAdapter
    var lstComments:MutableList<FutsalComment> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_comments)
        supportActionBar?.hide()
        binding()
        tvFutsal.text = "Here are the listed reviews of "+futsal!!.futsalName+"."
        tvUser.text = "Hi, "+StaticData.user!!.firstName
        if(StaticData.user!!.profilePicture != "no-photo.jpg")
        {
            var imagePath = RetrofitService.loadImagePath()+StaticData.user!!.profilePicture!!.replace("\\","/")
            Glide.with(this@ViewCommentsActivity).load(imagePath).into(ivProfile)
        }
        tvSend.setOnClickListener(this)
//        swipe.setOnRefreshListener(this)
        if(RetrofitService.online == true)
        {
            viewComments()
        }

    }

    private fun binding()
    {
        tvFutsal = findViewById(R.id.tvFutsal)
        tvUser = findViewById(R.id.tvUser)
        ivProfile = findViewById(R.id.ivProfile)
        etComment = findViewById(R.id.etComment)
        tvSend = findViewById(R.id.tvSend)
        recycler = findViewById(R.id.recycler)
//        swipe = findViewById(R.id.swipe)
        futsal = intent.getParcelableExtra("futsal")
    }

    private fun viewComments()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = CommentRepository()
                val response = repo.retrieveComments(futsal!!._id)
                if(response.success == true)
                {
                    lstComments = response.data!!
                    adapter = CommentRetrieveAdapter(this@ViewCommentsActivity,lstComments)
                    withContext(Dispatchers.Main)
                    {
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(this@ViewCommentsActivity)
                    }
                }
//                else
//                {
//                    withContext(Dispatchers.Main)
//                    {
//                        tvFutsal.snackbar("${response.message}")
//
//                    }
//                }
            }
            catch (ex:Exception)
            {
                withContext(Dispatchers.Main)
                {
                    tvFutsal.snackbar("Server Issue")
                    println(ex.printStackTrace())
                }
            }
        }
    }

    private fun addComment()
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repo = CommentRepository()
                val response = repo.addComment(futsal!!._id,etComment.text.toString())
                if(response.success == false)
                {
                    withContext(Dispatchers.Main)
                    {
                        tvSend.snackbar("${response.message}")
                    }
                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        val obj = FutsalComment(_id = response.id!!,futsal_id = futsal!!._id,user_id = StaticData.user,comment = etComment.text.toString(),commentedAt2 = response.commentAt)
                        lstComments.add(obj)
                        adapter = CommentRetrieveAdapter(this@ViewCommentsActivity,lstComments)
                        recycler.adapter = adapter
                        adapter.notifyDataSetChanged()
                        etComment.text!!.clear()
                        
                    }
                }

            }
            catch (ex:Exception)
            {
                withContext(Dispatchers.Main)
                {
                    tvSend.snackbar("Server Issue")
                    println(ex.printStackTrace())
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.tvSend->{
                if(TextUtils.isEmpty(etComment.text))
                {
                    etComment.error = "Insert comment to proceed"
                    etComment.requestFocus()
                }
                else
                {
                    if(RetrofitService.online == true)
                    {
                        addComment()
                    }
                    else
                    {
                        tvSend.snackbar("No Internet Connection")
                    }
                }
            }
        }
    }

//    override fun onRefresh() {
//        val intent = Intent(this@ViewCommentsActivity,ViewCommentsActivity::class.java)
//        startActivity(intent)
//        finish()
//    }

}