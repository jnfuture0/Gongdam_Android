package org.gongdam.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.form_review_show.view.*
import kotlinx.android.synthetic.main.form_review_show_no_image.view.*
import kotlinx.android.synthetic.main.form_room_equipment_page.view.*
import org.gongdam.R
import org.gongdam.Struct.BoardReviewShow
import org.gongdam.Struct.BoardRoomEquipment

class RecyclerAdapterReviewShow(val context: Context, val boardList: MutableList<BoardReviewShow>): RecyclerView.Adapter<RecyclerAdapterReviewShow.BaseViewHolder<*>>() {

    val TYPE_IMAGE = 0
    val TYPE_NO_IMAGE = 1

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    inner class ViewHolder1(itemView:View):BaseViewHolder<BoardReviewShow>(itemView){
        val image = itemView.form_review_show_image
        val userName = itemView.form_review_show_user_name
        val ratingBar = itemView.form_review_show_ratingbar
        val date = itemView.form_review_show_date
        val content = itemView.form_review_show_review_content
        override fun bind(board: BoardReviewShow) {
            Glide.with(context).load(board.image).into(image)
            userName.text = board.user_name
            ratingBar.rating = board.rating_bar_num
            date.text = board.date
            content.text = board.review_content
        }
    }

    inner class ViewHolder2(itemView: View):BaseViewHolder<BoardReviewShow>(itemView){
        val userName = itemView.form_review_show2_user_name
        val ratingBar = itemView.form_review_show2_ratingbar
        val date = itemView.form_review_show2_date
        val content = itemView.form_review_show2_review_content
        override fun bind(board: BoardReviewShow) {
            userName.text = board.user_name
            ratingBar.rating = board.rating_bar_num
            date.text = board.date
            content.text = board.review_content
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType){
            TYPE_IMAGE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_review_show, parent, false)
                ViewHolder1(view)
            }
            TYPE_NO_IMAGE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_review_show_no_image, parent, false)
                ViewHolder2(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = boardList[position]
        when(holder){
            is ViewHolder1 -> holder.bind(element as BoardReviewShow)
            is ViewHolder2 -> holder.bind(element as BoardReviewShow)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(boardList[position].isPicture){
            TYPE_IMAGE
        }else{
            TYPE_NO_IMAGE
        }
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

}

