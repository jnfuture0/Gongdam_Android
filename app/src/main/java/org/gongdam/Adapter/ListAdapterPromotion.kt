package org.gongdam.Adapter

import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import org.gongdam.MainActivity
import org.gongdam.R
import org.gongdam.Struct.BoardPromotion
import java.text.DateFormat
import java.text.SimpleDateFormat

class ListAdapterPromotion(val context: Context, val boardList: MutableList<BoardPromotion>): BaseAdapter() {

    private val ITEM_VIEW_TYPE_PREMIUM = 1
    private val ITEM_VIEW_TYPE_NORMAL = 0
    val moneyFormat = MainActivity.moneyFormatter
    val iso8601Format : DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View
        val holder: ViewHolder
        var viewType = getItemViewType(position)


        if(convertView == null){
            if (viewType == ITEM_VIEW_TYPE_PREMIUM){
                view = LayoutInflater.from(context).inflate(R.layout.form_promotion_board_premium, null)
                holder = ViewHolder()
                holder.boardImageCoveredView = view.findViewById(R.id.form_promotion_premium_board_layout)
                holder.boardImage = view.findViewById(R.id.form_promotion_premium_board_image)
                holder.boardStudioName = view.findViewById(R.id.form_promotion_premium_board_studio_name)
                holder.boardRoomName = view.findViewById(R.id.form_promotion_premium_board_room_name)
                holder.boardRatingBar = view.findViewById(R.id.form_promotion_premium_board_ratingBar)
                holder.boardLocation = view.findViewById(R.id.form_promotion_premium_board_location)
                holder.boardContent = view.findViewById(R.id.form_promotion_premium_board_content)
                holder.boardDiscountPercent = view.findViewById(R.id.form_promotion_premium_board_discount_percent_textView)
                holder.boardWonPerHour = view.findViewById(R.id.form_promotion_premium_board_won_per_hour_textView)
                holder.boardWonPerHourOld = view.findViewById(R.id.form_promotion_premium_board_won_per_hour_textView_old)
                holder.boardDay = view.findViewById(R.id.form_promotion_premium_board_day)
                holder.boardTime = view.findViewById(R.id.form_promotion_premium_board_time)
                holder.boardDay2 = view.findViewById(R.id.form_promotion_premium_board_day2)
                holder.boardTime2 = view.findViewById(R.id.form_promotion_premium_board_time2)
                view.tag = holder


            } else{
                view = LayoutInflater.from(context).inflate(R.layout.form_promotion_board, null)
                holder = ViewHolder()
                holder.boardWholeView = view.findViewById(R.id.form_promotion_board_layout)
                holder.boardImageCoveredView = view.findViewById(R.id.form_promotion_board_image_covered_layout)
                holder.boardImage = view.findViewById(R.id.form_promotion_board_image)
                holder.boardStudioName = view.findViewById(R.id.form_promotion_board_studio_name)
                holder.boardRoomName = view.findViewById(R.id.form_promotion_board_room_name)
                holder.boardLocation = view.findViewById(R.id.form_promotion_board_location)
                holder.boardContent = view.findViewById(R.id.form_promotion_board_content)
                holder.boardDiscountPercent = view.findViewById(R.id.form_promotion_board_discount_percent_textView)
                holder.boardWonPerHour = view.findViewById(R.id.form_promotion_board_won_per_hour_textView)
                holder.boardWonPerHourOld = view.findViewById(R.id.form_promotion_board_won_per_hour_textView_old)
                holder.boardDay = view.findViewById(R.id.form_promotion_board_day)
                holder.boardTime = view.findViewById(R.id.form_promotion_board_time)
                holder.boardDay2 = view.findViewById(R.id.form_promotion_board_day2)
                holder.boardTime2 = view.findViewById(R.id.form_promotion_board_time2)
                view.tag = holder
            }


        }else{
            holder = convertView.tag as ViewHolder
            view = convertView

            /*holderPremium = convertView.tag as ViewHolderPremium
            viewPremium = convertView*/
        }

        val boardPromotion = boardList[position]
        Glide.with(context).load(boardPromotion.image).into(holder.boardImage!!)
        holder.boardStudioName?.text = boardPromotion.studio_name
        holder.boardRoomName?.text = boardPromotion.room_name
        var locArr = boardPromotion.location.split(" ")
        holder.boardLocation?.text = "${locArr[0]} ${locArr[1]} ${locArr[2]}"
        holder.boardContent?.text = boardPromotion.studio_content
        holder.boardRatingBar?.rating = boardPromotion.rating_bar_num
        holder.boardDiscountPercent?.text = "${boardPromotion.discount_percent}%"
        holder.boardWonPerHour?.text = moneyFormat.format(boardPromotion.won_per_hour)  + "원/1시간"
        holder.boardWonPerHourOld?.text = moneyFormat.format(boardPromotion.won_per_hour_old)  + "원/1시간"//boardPromotion.won_per_hour_old
        val stTime = iso8601Format.parse(boardPromotion.startTime)
        val enTime = iso8601Format.parse(boardPromotion.endTime)
        var stMonth = ""
        var stDay = ""
        var stHour = ""
        stMonth = if(stTime.month+1 < 10){
            "0${stTime.month+1}"
        }else{
            (stTime.month+1).toString()
        }
        stDay = if(stTime.date < 10){
            "0${stTime.date}"
        }else{
            stTime.date.toString()
        }
        stHour = if(stTime.hours < 10){
            "0${stTime.hours}"
        }else{
            stTime.hours.toString()
        }
        var endMonth = ""
        var endDay = ""
        var endHour = ""
        endMonth = if(enTime.month+1 < 10){
            "0${enTime.month+1}"
        }else{
            (enTime.month+1).toString()
        }
        endDay = if(enTime.date < 10){
            "0${enTime.date}"
        }else{
            enTime.date.toString()
        }
        endHour = if(enTime.hours < 10){
            "0${enTime.hours}"
        }else{
            enTime.hours.toString()
        }

        holder.boardDay?.text = "${stMonth}.${stDay}"
        holder.boardTime?.text = "${stHour}시"
        holder.boardDay2?.text = "${endMonth}.${endDay}"
        holder.boardTime2?.text = "${endHour}시"

        val wonPerHourOld = holder.boardWonPerHourOld
        wonPerHourOld?.setPaintFlags(wonPerHourOld.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)


        /*이미지 코너 둥글게하기*/
        holder.boardImageCoveredView.apply {
            this!!.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                clipToOutline = true
            }
        }

        /*holder.boardRatingBar.apply{
            this!!.max = boardSearch.rating_bar_num
        }*/


        return view
    }

    override fun getViewTypeCount(): Int {
        return 2
    }

    override fun getItemViewType(position: Int): Int {
        return boardList[position].type_0_premium_1_normal
    }

    fun showHide(view:View?){
        view?.visibility = if(view?.visibility == View.VISIBLE){
            View.GONE
        } else{
            View.VISIBLE
        }
    }

    override fun getItem(p0: Int): Any {
        return boardList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return boardList.size
    }

    private class ViewHolder{
        var boardWholeView : ConstraintLayout? = null
        var boardImageCoveredView : ConstraintLayout? = null
        var boardImage : ImageView? = null
        var boardStudioName : TextView? = null
        var boardRoomName : TextView? = null
        var boardLocation : TextView? = null
        var boardContent : TextView? = null
        var boardRatingBar : RatingBar? = null
        var boardDiscountPercent : TextView? = null
        var boardWonPerHour : TextView? = null
        var boardWonPerHourOld : TextView? = null
        var boardDay : TextView? = null
        var boardTime : TextView? = null
        var boardDay2 : TextView? = null
        var boardTime2 : TextView? = null
    }


}