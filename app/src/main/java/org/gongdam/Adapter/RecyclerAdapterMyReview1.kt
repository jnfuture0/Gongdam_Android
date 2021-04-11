package org.gongdam.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.form_my_review_1.view.*
import org.gongdam.PageMyReviewWrite
import org.gongdam.R
import org.gongdam.Struct.BoardMyReview1

class RecyclerAdapterMyReview1(val context: Context, val boardList: MutableList<BoardMyReview1>, val itemClick:(BoardMyReview1) -> Unit): RecyclerView.Adapter<RecyclerAdapterMyReview1.MainViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = MainViewHolder(parent, itemClick)

    override fun getItemCount(): Int = boardList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder?.bind(boardList[position], context)
    }

    inner class MainViewHolder(parent: ViewGroup, itemClick:(BoardMyReview1) -> Unit) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.form_my_review_1, parent, false)) {
        val boardImage = itemView.form_my_review_image
        val boardStudioName = itemView.form_my_review_studio_name
        val boardRoomName = itemView.form_my_review_room_name
        val boardDate = itemView.form_my_review_date
        val boardTime = itemView.form_my_review_time
        val boardButton = itemView.form_my_review_write_btn

        fun bind(board:BoardMyReview1, context:Context){
            Glide.with(context).load(board.image).into(boardImage)
            boardStudioName?.text = board.studio_name
            boardRoomName?.text = board.room_name
            boardDate?.text = board.date
            boardTime?.text = board.time

            boardButton.setOnClickListener {
                itemClick(board)
            }
        }
    }


}