package org.gongdam.Adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import org.gongdam.MainActivity
import org.gongdam.R
import org.gongdam.Struct.BoardSearch

class ListAdapterSearch(val context: Context, val boardList: MutableList<BoardSearch>): BaseAdapter() {

    private val ITEM_VIEW_TYPE_PREMIUM = 1
    private val ITEM_VIEW_TYPE_NORMAL = 0
    private val moneyFormat = MainActivity.moneyFormatter

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View
        val holder: ViewHolder
        var viewType = getItemViewType(position)


        if(convertView == null){
            if (viewType == ITEM_VIEW_TYPE_PREMIUM){
                view = LayoutInflater.from(context).inflate(R.layout.form_search_board_premium, null)
                holder = ViewHolder()
                holder.boardImageCoveredView = view.findViewById(R.id.form_search_premium_board_layout)
                holder.boardImage = view.findViewById(R.id.form_search_premium_board_image)
                holder.boardStudioName = view.findViewById(R.id.form_search_premium_board_studio_name)
                holder.boardRoomName = view.findViewById(R.id.form_search_premium_board_room_name)
                holder.boardRatingBar = view.findViewById(R.id.form_search_premium_board_ratingBar)
                holder.boardLocation = view.findViewById(R.id.form_search_premium_board_location)
                holder.boardContent = view.findViewById(R.id.form_search_premium_board_content)
                holder.boardGongdamConfirm = view.findViewById(R.id.form_search_premium_board_gongdam_confirmed_layout)
                holder.boardCost = view.findViewById(R.id.form_search_premium_board_cost_per_hour)
                view.tag = holder


            } else{
                view = LayoutInflater.from(context).inflate(R.layout.form_search_board, null)
                holder = ViewHolder()
                holder.boardWholeView = view.findViewById(R.id.form_search_board_layout)
                holder.boardImageCoveredView = view.findViewById(R.id.form_search_board_image_covered_layout)
                holder.boardImage = view.findViewById(R.id.form_search_board_image)
                holder.boardStudioName = view.findViewById(R.id.form_search_board_studio_name)
                holder.boardRoomName = view.findViewById(R.id.form_search_board_room_name)
                holder.boardLocation = view.findViewById(R.id.form_search_board_location)
                holder.boardContent = view.findViewById(R.id.form_search_board_content)
                holder.boardGongdamConfirm = view.findViewById(R.id.form_search_board_gongdam_confirmed_layout)
                holder.boardCost = view.findViewById(R.id.form_search_board_cost_per_hour)
                view.tag = holder
            }


        }else{
            holder = convertView.tag as ViewHolder
            view = convertView

            /*holderPremium = convertView.tag as ViewHolderPremium
            viewPremium = convertView*/
        }

        val boardSearch = boardList[position]
        Glide.with(context).load(boardSearch.image).into(holder.boardImage!!)
        holder.boardStudioName?.text = boardSearch.studio_name
        holder.boardRoomName?.text = boardSearch.room_name
        var locArr = boardSearch.location.split(" ")
        holder.boardLocation?.text = "${locArr[0]} ${locArr[1]} ${locArr[2]}"
        holder.boardContent?.text = boardSearch.studio_content
        holder.boardRatingBar?.rating = boardSearch.rating_bar_num
        holder.boardCost?.text = moneyFormat.format(boardSearch.cost)  + "원/1시간"


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
        var boardGongdamConfirm : LinearLayout? = null
        var boardRatingBar : RatingBar? = null
        var boardCost : TextView? = null
    }


}