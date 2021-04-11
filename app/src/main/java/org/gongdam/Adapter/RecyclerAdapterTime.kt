package org.gongdam.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.form_time_pick_recycler.view.*
import org.gongdam.R
import org.gongdam.Struct.BoardStringBoolean

class RecyclerAdapterTime(val context: Context, val boardList: MutableList<BoardStringBoolean>, val itemClick:(BoardStringBoolean, isSelected:Boolean, Int) -> Unit): RecyclerView.Adapter<RecyclerAdapterTime.MainViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = MainViewHolder(parent, itemClick)

    override fun getItemCount(): Int = boardList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder?.bind(boardList[position], context, position)
    }

    inner class MainViewHolder(parent: ViewGroup, itemClick:(BoardStringBoolean, Boolean, Int) -> Unit) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.form_time_pick_recycler, parent, false)) {
        val btnUnSelected = itemView.form_time_picker_text_white
        val btnSelected = itemView.form_time_picker_text_blue

        fun bind(board:BoardStringBoolean, context:Context, position: Int){
            if(board.isSelected){
                btnSelected.visibility = View.VISIBLE
                btnUnSelected.visibility = View.GONE
            }

            btnUnSelected.setOnClickListener {
                btnSelected.visibility = View.VISIBLE
                btnUnSelected.visibility = View.GONE
                itemClick(board, true, position)
            }
            btnSelected.setOnClickListener {
                btnSelected.visibility = View.GONE
                btnUnSelected.visibility = View.VISIBLE
                itemClick(board, false, position)
            }
        }
    }


}