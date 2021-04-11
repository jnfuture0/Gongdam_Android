package org.gongdam.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.form_room_equipment_page.view.*
import org.gongdam.R
import org.gongdam.Struct.BoardRoomEquipment

class RecyclerAdapterRoomEquip(val context: Context, val boardList: MutableList<BoardRoomEquipment>): RecyclerView.Adapter<RecyclerAdapterRoomEquip.BaseViewHolder<*>>() {


    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    inner class ViewHolder1(itemView:View):BaseViewHolder<BoardRoomEquipment>(itemView){
        val boardName = itemView.form_room_equipment_page_equip_name
        val boardPrice = itemView.form_room_equipment_page_equip_price
        val boardHour = itemView.form_room_equipment_page_equip_hour
        override fun bind(board: BoardRoomEquipment) {
            boardName?.text = board.name
            boardPrice?.text = board.price
            boardHour?.text = board.hour
        }
    }

    inner class ViewHolder2(itemView: View):BaseViewHolder<BoardRoomEquipment>(itemView){
        override fun bind(item: BoardRoomEquipment) {
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType){
            0 -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_room_equipment_page, parent, false)
                ViewHolder1(view)
            }
            1 -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_room_equipment_page_check_point, parent, false)
                ViewHolder2(view)
            }
            2 -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_room_equipment_page_nothing, parent, false)
                ViewHolder2(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = boardList[position]
        when(holder){
            is ViewHolder1 -> holder.bind(element as BoardRoomEquipment)
            is ViewHolder2 -> holder.bind(element as BoardRoomEquipment)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return boardList[position].type
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

}

