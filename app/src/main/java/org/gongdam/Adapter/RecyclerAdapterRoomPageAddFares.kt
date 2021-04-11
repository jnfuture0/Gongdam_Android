package org.gongdam.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.form_room_page_add_fares.view.*
import org.gongdam.MainActivity
import org.gongdam.R
import org.gongdam.Struct.BoardNameCost

class RecyclerAdapterRoomPageAddFares(val context: Context, val boardList: MutableList<BoardNameCost>): RecyclerView.Adapter<RecyclerAdapterRoomPageAddFares.MainViewHolder>() {

    val moneyFormatter = MainActivity.moneyFormatter
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = MainViewHolder(parent)

    override fun getItemCount(): Int = boardList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(boardList[position], context)
    }

    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.form_room_page_add_fares, parent, false)) {
        val boardName = itemView.form_room_page_add_fares_name
        val boardCost = itemView.form_room_page_add_fares_cost

        fun bind(board:BoardNameCost, context:Context){
            boardName.text = board.name
            val money = moneyFormatter.format(board.cost)
            boardCost.text = "+$money"

            //boardImage.setOnClickListener { itemClick(board) }
        }
    }


}