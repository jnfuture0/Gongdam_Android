package org.gongdam.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.form_room_page_staff.view.*
import org.gongdam.R
import org.gongdam.Struct.BoardNameImg

class RecyclerAdapterRoomPageStaff(val context: Context, val boardList: MutableList<BoardNameImg>): RecyclerView.Adapter<RecyclerAdapterRoomPageStaff.MainViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = MainViewHolder(parent)

    override fun getItemCount(): Int = boardList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(boardList[position], context)
    }

    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.form_room_page_staff, parent, false)) {
        val boardImage = itemView.form_room_page_staff_img
        val boardName = itemView.form_room_page_staff_text

        fun bind(board:BoardNameImg, context:Context){
            if(board.img != "") {
                Glide.with(context).load(board.img).into(boardImage)
            }
            boardName.text = board.name
        }
    }
}