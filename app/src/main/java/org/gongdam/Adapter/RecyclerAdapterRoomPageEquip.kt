package org.gongdam.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.gongdam.R


class RecyclerAdapterRoomPageEquip(val context: Context, val boardList: MutableList<Int>): RecyclerView.Adapter<RecyclerAdapterRoomPageEquip.BaseViewHolder<*>>() {


        abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
            abstract fun bind(item: T)
        }

        inner class ViewHolder1(itemView: View):BaseViewHolder<Int>(itemView){
            override fun bind(board: Int) {
            }
        }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
            return when(viewType){
                7 -> {
                    val view = LayoutInflater.from(context).inflate(R.layout.form_equip_icon_camera, parent, false)
                    ViewHolder1(view)
                }
                8 -> {
                    val view = LayoutInflater.from(context).inflate(R.layout.form_equip_icon_lens, parent, false)
                    ViewHolder1(view)
                }
                9 -> {
                    val view = LayoutInflater.from(context).inflate(R.layout.form_equip_icon_light, parent, false)
                    ViewHolder1(view)
                }
                10 -> {
                    val view = LayoutInflater.from(context).inflate(R.layout.form_equip_icon_back, parent, false)
                    ViewHolder1(view)
                }
                11 -> {
                    val view = LayoutInflater.from(context).inflate(R.layout.form_equip_icon_mic, parent, false)
                    ViewHolder1(view)
                }
                12 -> {
                    val view = LayoutInflater.from(context).inflate(R.layout.form_equip_icon_etc, parent, false)
                    ViewHolder1(view)
                }
                else -> throw IllegalArgumentException("Invalid view type")
            }
        }

        override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
            val element = boardList[position]
            when(holder){
                is ViewHolder1 -> holder.bind(element as Int)
                else -> throw IllegalArgumentException()
            }
        }

        override fun getItemViewType(position: Int): Int {
            return boardList[position]
        }

        override fun getItemCount(): Int {
            return boardList.size
        }


}