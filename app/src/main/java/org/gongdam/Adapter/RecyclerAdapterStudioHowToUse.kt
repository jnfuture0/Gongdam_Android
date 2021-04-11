package org.gongdam.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.form_home_popular.view.*
import kotlinx.android.synthetic.main.form_studio_page_how_to_use.view.*
import org.gongdam.R
import org.gongdam.Struct.BoardHomePopular
import org.gongdam.Struct.BoardHowToUse

class RecyclerAdapterStudioHowToUse(val context: Context, val boardList: MutableList<BoardHowToUse>): RecyclerView.Adapter<RecyclerAdapterStudioHowToUse.MainViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = MainViewHolder(parent)

    override fun getItemCount(): Int = boardList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder?.bind(boardList[position], context)
    }

    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.form_studio_page_how_to_use, parent, false)) {
        val roomName = itemView.form_studio_page_how_to_use_room_name
        val st1 = itemView.form_studio_page_price_per_hour_TextV
        val st2 = itemView.form_studio_page_min_reservation_time_TextV
        val st3 = itemView.form_studio_page_min_people_TextV
        val st4 = itemView.form_studio_page_one_more_people_per_hour_TextV

        fun bind(item:BoardHowToUse, context:Context){
            roomName.text = item.room_name
            st1.text = item.st1
            st2.text = item.st2
            st3.text = item.st3
            st4.text = item.st4
        }
    }


}