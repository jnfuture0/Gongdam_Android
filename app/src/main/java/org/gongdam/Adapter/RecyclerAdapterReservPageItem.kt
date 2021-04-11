package org.gongdam.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.form_reservation_page_item_list.view.*
import kotlinx.android.synthetic.main.form_room_equipment_page.view.*
import org.gongdam.MainActivity
import org.gongdam.R
import org.gongdam.Struct.BoardNameCost
import org.gongdam.Struct.BoardRoomEquipment

class RecyclerAdapterReservPageItem(val context: Context, val boardList: MutableList<BoardNameCost>): RecyclerView.Adapter<RecyclerAdapterReservPageItem.BaseViewHolder<*>>() {

    val moneyFormatter = MainActivity.moneyFormatter
    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    inner class ViewHolder1(itemView:View):BaseViewHolder<BoardNameCost>(itemView){
        val boardName = itemView.form_reservation_page_item_name
        val boardPrice = itemView.form_reservation_page_item_cost
        override fun bind(board: BoardNameCost) {
            boardName?.text = board.name
            val money = moneyFormatter.format(board.cost)
            boardPrice?.text = "+ ${money}"
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType){
            0 -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_reservation_page_item_list, parent, false)
                ViewHolder1(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = boardList[position]
        when(holder){
            is ViewHolder1 -> holder.bind(element as BoardNameCost)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

}

