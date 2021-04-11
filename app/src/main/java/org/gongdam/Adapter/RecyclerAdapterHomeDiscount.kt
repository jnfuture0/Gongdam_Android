package org.gongdam.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.form_home_discount.view.*
import org.gongdam.R
import org.gongdam.Struct.BoardHomeDiscount

class RecyclerAdapterHomeDiscount(val context: Context, val boardList: MutableList<BoardHomeDiscount>, val itemClick:(BoardHomeDiscount) -> Unit): RecyclerView.Adapter<RecyclerAdapterHomeDiscount.MainViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = MainViewHolder(parent)

    override fun getItemCount(): Int = boardList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder?.bind(boardList[position], context)
    }

    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.form_home_discount, parent, false)) {
        val boardImage = itemView.form_home_with_discount_image
        val boardStudioName = itemView.form_home_with_discount_studio_name
        val boardRoomName = itemView.form_home_with_discount_room_name
        val boardDiscount = itemView.form_home_with_discount_how_much_discount_textView
        val boardLayout = itemView.form_home_with_discount_layout

        fun bind(board:BoardHomeDiscount, context:Context){
            if(board.studioImage != ""){
                Glide.with(context).load(board.studioImage).into(boardImage)
                //val resourceId = context.resources.getIdentifier(board.studioImage, "drawable", context.packageName)
                //boardImage?.setImageResource(resourceId)
            }else{
                //boardImage?.setImageResource(R.mipmap.ic_launcher)
            }
            boardStudioName?.text = board.studioName
            boardRoomName?.text = board.roomName
            boardDiscount?.text = board.discount

            boardLayout.setOnClickListener {
                itemClick(board)
            }

        }
    }

    /*override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        if(p1!!.action == MotionEvent.ACTION_UP){

        }
    }*/


}