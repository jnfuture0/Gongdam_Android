package org.gongdam.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.form_home_popular.view.*
import org.gongdam.R
import org.gongdam.Struct.BoardHomePopular

class RecyclerAdapterHomePopular(val context: Context, val boardList: MutableList<BoardHomePopular>, val itemClick:(BoardHomePopular) -> Unit): RecyclerView.Adapter<RecyclerAdapterHomePopular.MainViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = MainViewHolder(parent)

    override fun getItemCount(): Int = boardList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder?.bind(boardList[position], context)
    }

    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.form_home_popular, parent, false)) {
        val boardImage = itemView.form_home_popular_image
        val boardStudioName = itemView.form_home_popular_studio_name
        val boardRoomName = itemView.form_home_popular_room_name
        val boardLayout = itemView.form_home_popular_layout

        fun bind(board:BoardHomePopular, context:Context){

            Glide.with(context).load(board.studioImage).into(boardImage)
            boardStudioName?.text = board.studioName
            boardRoomName?.text = board.roomName

            boardLayout.setOnClickListener { itemClick(board) }
        }
    }


}