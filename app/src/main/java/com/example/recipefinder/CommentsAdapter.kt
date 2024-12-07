package com.example.recipefinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentsAdapter(private val comments: MutableList<Comments>) :
    RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.userName.text = comment.userName
        holder.commentText.text = comment.commentText
    }

    override fun getItemCount(): Int = comments.size

    fun addComment(comment: Comments) {
        comments.add(comment)
        notifyItemInserted(comments.size - 1)
    }

    fun updateComments(newComments: List<Comments>) {
        comments.clear()
        comments.addAll(newComments)
        notifyDataSetChanged() // Notify RecyclerView that the data has changed
    }

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.userName)
        val commentText: TextView = itemView.findViewById(R.id.commentText)
    }
}
