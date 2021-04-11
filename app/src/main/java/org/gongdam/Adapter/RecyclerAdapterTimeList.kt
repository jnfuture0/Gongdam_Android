package org.gongdam.Adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.form_make_reserv_time_list.view.*
import org.gongdam.MainActivity
import org.gongdam.R
import org.gongdam.Struct.BoardTimeList

class RecyclerAdapterTimeList(val context: Context, val boardList: MutableList<BoardTimeList>): RecyclerView.Adapter<RecyclerAdapterTimeList.BaseViewHolder<*>>() {

    val moneyFormatter = MainActivity.moneyFormatter
    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    // promotion
    inner class ViewHolderPromotion(itemView:View):BaseViewHolder<BoardTimeList>(itemView){
        val boardTime = itemView.form_make_reserv_time_list_time_stamp
        val boardCost = itemView.form_make_reserv_time_list_cost
        val boardCostBefore = itemView.form_make_reserv_time_list_cost_before
        val boardLayout = itemView.form_make_reserv_time_list_right_layout
        override fun bind(board: BoardTimeList) {
            boardTime?.text = board.time
            val newMoney = moneyFormatter.format(board.newCost)
            boardCost?.text = "+ ${newMoney}"
            boardCost?.setTextColor(Color.parseColor("#a90000"))
            val money = moneyFormatter.format(board.cost)
            boardCostBefore?.text = "${money}"
            boardCostBefore?.setPaintFlags(boardCostBefore.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
            if(board.isChecked){
                boardLayout.setBackgroundColor(Color.parseColor("#001024"))
            }else{
                boardLayout.setBackgroundColor(Color.parseColor("#f6f6f6"))
            }
        }
    }


    inner class ViewHolderNormal(itemView: View):BaseViewHolder<BoardTimeList>(itemView){
        val boardTime = itemView.form_make_reserv_time_list_time_stamp
        val boardCost = itemView.form_make_reserv_time_list_cost
        val boardLayout = itemView.form_make_reserv_time_list_right_layout
        val boardWholeLayout = itemView.form_make_reserv_time_list_whole_layout
        override fun bind(board: BoardTimeList) {
            if(board.isShow) {
                boardWholeLayout.visibility = View.VISIBLE
                boardTime?.text = board.time
                val money = moneyFormatter.format(board.cost)
                boardCost?.text = "+ ${money}"
                boardCost?.setTextColor(Color.parseColor("#303030"))
                if (board.isChecked) {
                    boardLayout.setBackgroundColor(Color.parseColor("#001024"))
                    boardCost.setTextColor(Color.parseColor("#ffffff"))
                } else {
                    boardLayout.setBackgroundColor(Color.parseColor("#f6f6f6"))
                    boardCost.setTextColor(Color.parseColor("#303030"))
                }
            }else{
                boardWholeLayout.visibility = View.GONE
            }
        }
    }


    inner class ViewHolderCant(itemView: View):BaseViewHolder<BoardTimeList>(itemView){
        val boardTime = itemView.form_make_reserv_time_list_time_stamp
        val boardCost = itemView.form_make_reserv_time_list_cost
        val boardLayout = itemView.form_make_reserv_time_list_right_layout
        override fun bind(board: BoardTimeList) {
            boardTime?.text = board.time
            boardTime?.setTextColor(Color.parseColor("#d7d7d7"))
            boardCost?.text = "예약불가"
            boardCost?.setTextColor(Color.parseColor("#d7d7d7"))
            boardLayout.setBackgroundColor(Color.parseColor("#eeeeee"))
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType){
            0 -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_make_reserv_time_list, parent, false)
                ViewHolderNormal(view)
            }
            1 -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_make_reserv_time_list, parent, false)
                ViewHolderPromotion(view)
            }
            2 -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_make_reserv_time_list, parent, false)
                ViewHolderCant(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = boardList[position]
        when(holder){
            is ViewHolderNormal -> holder.bind(element as BoardTimeList)
            is ViewHolderPromotion -> holder.bind(element as BoardTimeList)
            is ViewHolderCant -> holder.bind(element as BoardTimeList)
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

