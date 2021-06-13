package com.sagarmishra.futsal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sagarmishra.futsal.R
import com.sagarmishra.futsal.ViewCommentsActivity
import com.sagarmishra.futsal.api.RetrofitService
import com.sagarmishra.futsal.databinding.LayoutCommentFutsalsBinding
import com.sagarmishra.futsal.entityapi.Futsal
import com.sagarmishra.futsal.utils.snackbar

class CommentFutsalAdapter(val context:Context,val lstFutsals:MutableList<Futsal>):RecyclerView.Adapter<CommentFutsalAdapter.CommentFutsalViewHolder>() {
    class CommentFutsalViewHolder(var layoutCommentFutsalsBinding: LayoutCommentFutsalsBinding):RecyclerView.ViewHolder(layoutCommentFutsalsBinding.root)
    {
        var commentVB = layoutCommentFutsalsBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentFutsalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val cb = LayoutCommentFutsalsBinding.inflate(inflater,parent,false)
        return CommentFutsalViewHolder(cb)
    }

    override fun onBindViewHolder(holder: CommentFutsalViewHolder, position: Int) {
        var futsal = lstFutsals[position]
        holder.commentVB.comment = futsal
        var imgPath = RetrofitService.loadImagePath()+futsal.futsalImage!!.replace("\\","/")
        Glide.with(context).load(imgPath).into(holder.commentVB.ivImage)
        var popUp = PopupMenu(context,holder.commentVB.ivImage)
        popUp.menuInflater.inflate(R.menu.comment_menu,popUp.menu)
        popUp.setOnMenuItemClickListener {
            when(it.itemId)
            {
                R.id.nav_menu->{
                    if(RetrofitService.online == true)
                    {
                        val intent = Intent(context,ViewCommentsActivity::class.java)
                        intent.putExtra("futsal",futsal)
                        context.startActivity(intent)
                    }
                    else
                    {
                        holder.commentVB.ivImage.snackbar("No Internet Connection")
                    }
                }
            }
            true
        }
        holder.commentVB.ivPopUp.setOnClickListener{
            popUp.show()
        }

    }

    override fun getItemCount(): Int {
        return lstFutsals.size
    }


}