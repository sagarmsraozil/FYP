package com.sagarmishra.futsal.adapter

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.Repository.CommentRepository
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.entityapi.FutsalComment
import com.sagarmishra.futsal.model.StaticData
import com.sagarmishra.futsal.utils.snackbar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import java.lang.Exception

class CommentRetrieveAdapter(val context:Context,var lstComments:MutableList<FutsalComment>):RecyclerView.Adapter<CommentRetrieveAdapter.CommentRetrieveViewHolder>() {
    class CommentRetrieveViewHolder(val view:View):RecyclerView.ViewHolder(view)
    {
        val tvName:TextView
        val tvDate:TextView
        val tvComment:TextView
        val cvProfile:CircleImageView
        val ivUpdate: ImageView

        init {
            tvName = view.findViewById(R.id.tvName)
            tvDate = view.findViewById(R.id.tvDate)
            tvComment = view.findViewById(R.id.tvComment)
            cvProfile = view.findViewById(R.id.cvProfile)
            ivUpdate = view.findViewById(R.id.ivUpdate)
        }
    }





    override fun getItemCount(): Int {
        return lstComments.size
    }

    override fun onBindViewHolder(holder: CommentRetrieveViewHolder, position: Int) {
        var comment = lstComments[position]
        holder.tvName.text = "${comment.user_id!!.firstName} ${comment.user_id!!.lastName}"
        holder.tvDate.text = comment.commentedAt2
        holder.tvComment.text = comment.comment
        if(comment.user_id!!.profilePicture != "no-photo.jpg")
        {
            var imagePath = RetrofitService.loadImagePath()+comment.user_id!!.profilePicture!!.replace("\\","/")
            Glide.with(context).load(imagePath).into(holder.cvProfile)
        }
        if(comment.user_id!!._id == StaticData.user!!._id)
        {
            holder.ivUpdate.visibility = View.VISIBLE
            holder.tvName.text = holder.tvName.text.toString()+" (You)"
        }
        else
        {
            holder.ivUpdate.visibility = View.GONE
        }

        var popMenu = PopupMenu(context,holder.ivUpdate)
        popMenu.menuInflater.inflate(R.menu.update_delete,popMenu.menu)
        popMenu.setOnMenuItemClickListener {
            when(it.itemId)
            {
                R.id.update->{
                    val dialog = Dialog(context)
                    dialog.setContentView(R.layout.comment_update)
                    dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                    dialog.setCancelable(true)
                    var etComment:EditText = dialog.findViewById(R.id.etComment)
                    var btnEdit:Button = dialog.findViewById(R.id.btnEdit)
                    etComment.setText(comment.comment)
                    btnEdit.setOnClickListener {
                        if(TextUtils.isEmpty(etComment.text))
                        {
                            etComment.error = "Enter Comments"
                            etComment.requestFocus()
                        }
                        else
                        {
                            if(RetrofitService.online == true)
                            {
                                val obj = FutsalComment(_id=comment._id,comment = etComment.text.toString())
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        val repo = CommentRepository()
                                        val response = repo.updateComment(obj)
                                        if(response.success == true)
                                        {
                                            lstComments[position].comment = etComment.text.toString()
                                            withContext(Dispatchers.Main)
                                            {
                                                notifyDataSetChanged()
                                                etComment.text!!.clear()
                                                dialog.cancel()
                                            }
                                        }
                                        else
                                        {
                                            withContext(Dispatchers.Main)
                                            {
                                                holder.cvProfile.snackbar("${response.message}")
                                            }
                                        }
                                    }
                                    catch (ex:Exception)
                                    {
                                        withContext(Dispatchers.Main)
                                        {
                                            holder.cvProfile.snackbar("Server Issue")
                                        }
                                    }

                                }
                            }
                            else
                            {
                                holder.cvProfile.snackbar("No Internet connection")
                            }
                        }
                    }
                    dialog.show()
                }
                R.id.delete->{
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Do you really want to delete your comment?")
                    builder.setMessage("Press yes to Confirm.\nCancel to Deny.")
                    builder.setPositiveButton("Yes"){ dialogInterface: DialogInterface, i: Int ->
                        if(RetrofitService.online == true)
                        {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val repo = CommentRepository()
                                    val response = repo.deleteComment(comment._id)
                                    if(response.success == true)
                                    {
                                        lstComments.removeAt(position)
                                        withContext(Dispatchers.Main)
                                        {
                                            notifyDataSetChanged()
                                        }

                                    }
                                    else
                                    {
                                        withContext(Dispatchers.Main)
                                        {
                                            holder.cvProfile.snackbar("${response.message}")
                                        }
                                    }
                                }
                                catch (ex:Exception)
                                {
                                    withContext(Dispatchers.Main)
                                    {
                                        holder.cvProfile.snackbar("Server Issue")
                                        println(ex.printStackTrace())
                                    }

                                }
                            }
                        }
                        else
                        {
                            holder.cvProfile.snackbar("No Internet Connection")
                        }
                    }
                    builder.setNegativeButton("Cancel"){ dialogInterface: DialogInterface, i: Int ->

                    }

                    val alert = builder.create()
                    alert.setCancelable(false)
                    alert.show()
                }
            }
            true
        }

        holder.ivUpdate.setOnClickListener {
            popMenu.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentRetrieveViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comments_layout,parent,false)
        return CommentRetrieveViewHolder(view)
    }
}