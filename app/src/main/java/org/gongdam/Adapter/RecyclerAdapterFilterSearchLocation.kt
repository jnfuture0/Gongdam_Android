package org.gongdam.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.form_filter_search.view.*
import org.gongdam.R
import org.gongdam.Struct.BoardOneString

class RecyclerAdapterFilterSearchLocation(val context: Context, val boardList: MutableList<BoardOneString>, val userLocationList : MutableList<String>, val itemClick:(tv:TextView) -> Unit): RecyclerView.Adapter<RecyclerAdapterFilterSearchLocation.MainViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = MainViewHolder(parent)



    override fun getItemCount(): Int = boardList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(boardList[position], context)
    }

    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.form_filter_search, parent, false)) {
        val filterString = itemView.form_filter_search_text
        val filterLayout = itemView.form_filter_search_layout


        fun bind(board:BoardOneString, context:Context){
            filterString.text = board.string

            if(userLocationList.contains(filterString.text.toString())){
                filterString.setTextColor(Color.parseColor("#ffffff"))
                filterString.setBackgroundResource(R.drawable.style_filter_background)
            }

            filterString.setOnClickListener {
                itemClick(itemView.form_filter_search_text)
            }

        }
    }


}