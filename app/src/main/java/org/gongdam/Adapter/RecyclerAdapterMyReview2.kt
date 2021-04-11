package org.gongdam.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.form_my_review_1.view.*
import kotlinx.android.synthetic.main.form_my_review_2.view.*
import kotlinx.android.synthetic.main.form_my_review_2_no_image.view.*
import kotlinx.android.synthetic.main.form_my_space_list.view.*
import org.gongdam.Json.FavoriteGet
import org.gongdam.MainActivity
import org.gongdam.PageRoom
import org.gongdam.R
import org.gongdam.Struct.BoardMyReview1
import org.gongdam.Struct.BoardMyReview2
import org.gongdam.Struct.BoardMySpaceList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

class RecyclerAdapterMyReview2(val context: Context, val boardList: MutableList<BoardMyReview2>, val itemClick:(BoardMyReview2) -> Unit): RecyclerView.Adapter<RecyclerAdapterMyReview2.BaseViewHolder<*>>(){

    val server = MainActivity.server

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    inner class ViewHolder1(itemView:View):BaseViewHolder<BoardMyReview2>(itemView){
        val boardImage = itemView.form_my_review2_image
        val boardStudioName = itemView.form_my_review2_studio_name
        val boardRoomName = itemView.form_my_review2_room_name
        val boardReviewDate = itemView.form_my_review2_date
        val boardRatingBar = itemView.form_my_review2_ratingbar
        val boardButton = itemView.form_my_review2_btn
        val boardReviewContent = itemView.form_my_review2_review_content
        val boardStudioName2 = itemView.form_my_review2_studio_name_reply
        val boardReplyDate = itemView.form_my_review2_reply_date
        val boardReplyContent = itemView.form_my_review2_reply_content
        val replyLayout = itemView.form_my_review2_reply_layout

        override fun bind(board: BoardMyReview2) {
            if(board.image != ""){
                Glide.with(context).load(board.image).into(boardImage)
            }else{
                boardImage?.setImageResource(R.mipmap.ic_launcher)
            }
            boardStudioName?.text = board.studio_name
            boardRoomName?.text = board.room_name
            boardReviewDate?.text = board.review_date
            boardReviewContent?.text = board.review_content
            boardStudioName2?.text = board.studio_name
            boardReplyDate?.text = board.reply_date
            boardReplyContent?.text = board.reply_content
            boardRatingBar?.rating = board.review_rating
            if(board.is_reply){
                replyLayout.visibility = View.VISIBLE
            }
            boardButton.setOnClickListener { itemClick(board) }
        }
    }

    inner class ViewHolder2(itemView: View):BaseViewHolder<BoardMyReview2>(itemView){
        val boardStudioName = itemView.form_my_review2_noImg_studio_name
        val boardRoomName = itemView.form_my_review2_noImg_room_name
        val boardReviewDate = itemView.form_my_review2_noImg_date
        val boardRatingBar = itemView.form_my_review2_noImg_ratingbar
        val boardButton = itemView.form_my_review2_noImg_btn
        val boardReviewContent = itemView.form_my_review2_noImg_review_content
        val boardStudioName2 = itemView.form_my_review2_noImg_studio_name_reply
        val boardReplyDate = itemView.form_my_review2_noImg_reply_date
        val boardReplyContent = itemView.form_my_review2_noImg_reply_content
        val replyLayout = itemView.form_my_review2_noImg_reply_layout

        override fun bind(board: BoardMyReview2) {
            boardStudioName?.text = board.studio_name
            boardRoomName?.text = board.room_name
            boardReviewDate?.text = board.review_date
            boardReviewContent?.text = board.review_content
            boardStudioName2?.text = board.studio_name
            boardReplyDate?.text = board.reply_date
            boardReplyContent?.text = board.reply_content
            boardRatingBar?.rating = board.review_rating
            if(board.is_reply){
                replyLayout.visibility = View.VISIBLE
            }
            boardButton.setOnClickListener { itemClick(board) }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType){
            0 -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_my_review_2, parent, false)
                ViewHolder1(view)
            }
            1 -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_my_review_2_no_image, parent, false)
                ViewHolder2(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(boardList[position].is_picture){
            0
        }else{
            1
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = boardList[position]
        when(holder){
            is ViewHolder1 -> holder.bind(element as BoardMyReview2)
            is ViewHolder2 -> holder.bind(element as BoardMyReview2)
            else -> throw IllegalArgumentException()
        }
    }


    override fun getItemCount(): Int {
        return boardList.size
    }
}