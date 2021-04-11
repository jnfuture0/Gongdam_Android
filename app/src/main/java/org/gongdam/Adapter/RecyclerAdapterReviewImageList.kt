package org.gongdam.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import org.gongdam.R
import org.gongdam.Struct.BoardStringInt


class RecyclerAdapterReviewImageList(val context: Context, val boardList: MutableList<BoardStringInt>, val itemClick:(BoardStringInt) -> Unit): RecyclerView.Adapter<RecyclerAdapterReviewImageList.BaseViewHolder<*>>() {

    val PLUS_BTN = 1

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    inner class ViewHolder1(itemView: View):BaseViewHolder<BoardStringInt>(itemView){
        val img = itemView.findViewById<ImageView>(R.id.form_review_img_list_img)
        override fun bind(board: BoardStringInt) {
            img.setImageURI(board.img.toUri())
        }
    }

    inner class ViewHolder2(itemView: View):BaseViewHolder<BoardStringInt>(itemView){
        val img = itemView.findViewById<ImageView>(R.id.form_review_img_list_img)
        override fun bind(board: BoardStringInt) {
            img.setOnClickListener {
                itemClick(board)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType){
            0 -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_review_img_list, parent, false)
                ViewHolder1(view)
            }
            PLUS_BTN -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_review_img_list, parent, false)
                ViewHolder2(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = boardList[position]
        when(holder){
            is ViewHolder1 -> holder.bind(element as BoardStringInt)
            is ViewHolder2 -> holder.bind(element as BoardStringInt)
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