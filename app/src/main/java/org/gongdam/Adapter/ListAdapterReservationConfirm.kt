package org.gongdam.Adapter

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import org.gongdam.MainActivity
import org.gongdam.R
import org.gongdam.Struct.BoardReservationConfirm

class ListAdapterReservationConfirm(val context: Context, val boardList: MutableList<BoardReservationConfirm>): BaseAdapter() {

    val TYPE_WAIT = 0
    val TYPE_ADJUST = 1
    val TYPE_PAYMENT_WAIT = 2
    val TYPE_RESERV_FINISH = 3
    val TYPE_REFUND_WAIT = 4
    var typeArr : MutableList<Int> = mutableListOf()
    val moneyFormat = MainActivity.moneyFormatter


    fun asdf(){
        for(i in 0 until boardList.size){
            typeArr.add(boardList[i].type)
        }
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View
        val holder: ViewHolder
        var viewType = getItemViewType(position)

        //if(convertView == null){
            if(viewType == TYPE_WAIT) {
                view = LayoutInflater.from(context).inflate(R.layout.form_reserv_confirm_wait, null)
                holder = ViewHolder()
                holder.boardWholeView =
                    view.findViewById(R.id.board_reserv_confirm_wait_LinearLayout)
                holder.boardImage = view.findViewById(R.id.board_reserv_confirm_wait_ImageView)
                holder.boardTitle = view.findViewById(R.id.board_reserv_confirm_wait_Title)
                holder.boardDate = view.findViewById(R.id.board_reserv_confirm_wait_Date)
                holder.boardTime = view.findViewById(R.id.board_reserv_confirm_wait_Time)
                holder.boardLocation = view.findViewById(R.id.board_reserv_confirm_wait_Location)
                holder.boardTotalPrice = view.findViewById(R.id.board_reserv_confirm_wait_price_sum2)
                holder.boardDetailView =
                    view.findViewById(R.id.board_reserv_confirm_wait_detail_layout)
                holder.boardDetailBtn = view.findViewById(R.id.board_reserv_confirm_wait_detail_btn)

                holder.boardBasicPrice = view.findViewById(R.id.board_reserv_confirm_wait_basic_price_textView)
                holder.boardAddFarePerson = view.findViewById(R.id.board_reserv_confirm_wait_add_fare_person)
                holder.boardAddFare = view.findViewById(R.id.board_reserv_confirm_wait_add_fare)
                holder.boardPriceSum = view.findViewById(R.id.board_reserv_confirm_wait_price_sum)
                holder.boardDiscountPrice = view.findViewById(R.id.board_reserv_confirm_wait_discount_price)
                holder.boardDiscountPerson = view.findViewById(R.id.board_reserv_confirm_wait_discount_person)
                holder.boardDiscountSum = view.findViewById(R.id.board_reserv_confirm_wait_discount_sum)
                holder.boardEquipPrice = view.findViewById(R.id.board_reserv_confirm_wait_equip_price)
                holder.boardEquipDiscount = view.findViewById(R.id.board_reserv_confirm_wait_equip_discount)
                holder.boardPriceSum2 = view.findViewById(R.id.board_reserv_confirm_wait_price_sum2)
                holder.boardDiscountTotal = view.findViewById(R.id.board_reserv_confirm_wait_discount_total)
                holder.boardUseTimeTotal = view.findViewById(R.id.board_reserv_confirm_wait_use_time_total)
                holder.boardEquipTotal = view.findViewById(R.id.board_reserv_confirm_wait_equip_total)
                holder.boardTotalPrice2 = view.findViewById(R.id.board_reserv_confirm_wait_total_price2)
                view.tag = holder
            }
            else if(viewType == TYPE_ADJUST){
                view = LayoutInflater.from(context).inflate(R.layout.form_reserv_confirm_adjust, null)
                holder = ViewHolder()
                holder.boardWholeView =
                    view.findViewById(R.id.board_reserv_confirm_adjust_LinearLayout)
                holder.boardImage = view.findViewById(R.id.board_reserv_confirm_adjust_ImageView)
                holder.boardTitle = view.findViewById(R.id.board_reserv_confirm_adjust_Title)
                holder.boardDate = view.findViewById(R.id.board_reserv_confirm_adjust_Date)
                holder.boardTime = view.findViewById(R.id.board_reserv_confirm_adjust_Time)
                holder.boardLocation = view.findViewById(R.id.board_reserv_confirm_adjust_Location)
                holder.boardTotalPrice = view.findViewById(R.id.board_reserv_confirm_adjust_price_sum2)
                holder.boardDetailView =
                    view.findViewById(R.id.board_reserv_confirm_adjust_detail_layout)
                holder.boardDetailBtn = view.findViewById(R.id.board_reserv_confirm_adjust_detail_btn)

                holder.boardBasicPrice = view.findViewById(R.id.board_reserv_confirm_adjust_basic_price_textView)
                holder.boardAddFarePerson = view.findViewById(R.id.board_reserv_confirm_adjust_add_fare_person)
                holder.boardAddFare = view.findViewById(R.id.board_reserv_confirm_adjust_add_fare)
                holder.boardPriceSum = view.findViewById(R.id.board_reserv_confirm_adjust_price_sum)
                holder.boardDiscountPrice = view.findViewById(R.id.board_reserv_confirm_adjust_discount_price)
                holder.boardDiscountPerson = view.findViewById(R.id.board_reserv_confirm_adjust_discount_person)
                holder.boardDiscountSum = view.findViewById(R.id.board_reserv_confirm_adjust_discount_sum)
                holder.boardEquipPrice = view.findViewById(R.id.board_reserv_confirm_adjust_equip_price)
                holder.boardEquipDiscount = view.findViewById(R.id.board_reserv_confirm_adjust_equip_discount)
                holder.boardPriceSum2 = view.findViewById(R.id.board_reserv_confirm_adjust_price_sum2)
                holder.boardDiscountTotal = view.findViewById(R.id.board_reserv_confirm_adjust_discount_total)
                holder.boardUseTimeTotal = view.findViewById(R.id.board_reserv_confirm_adjust_use_time_total)
                holder.boardEquipTotal = view.findViewById(R.id.board_reserv_confirm_adjust_equip_total)
                holder.boardTotalPrice2 = view.findViewById(R.id.board_reserv_confirm_adjust_total_price2)
                view.tag = holder
            }
            else if(viewType == TYPE_PAYMENT_WAIT) {
                view = LayoutInflater.from(context).inflate(R.layout.form_reserv_confirm_payment_wait, null)
                holder = ViewHolder()
                holder.boardWholeView =
                    view.findViewById(R.id.board_reserv_confirm_payment_wait_LinearLayout)
                holder.boardImage = view.findViewById(R.id.board_reserv_confirm_payment_wait_ImageView)
                holder.boardTitle = view.findViewById(R.id.board_reserv_confirm_payment_wait_Title)
                holder.boardDate = view.findViewById(R.id.board_reserv_confirm_payment_wait_Date)
                holder.boardTime = view.findViewById(R.id.board_reserv_confirm_payment_wait_Time)
                holder.boardLocation = view.findViewById(R.id.board_reserv_confirm_payment_wait_Location)
                holder.boardTotalPrice = view.findViewById(R.id.board_reserv_confirm_payment_wait_price_sum2)
                holder.boardDetailView =
                    view.findViewById(R.id.board_reserv_confirm_payment_wait_detail_layout)
                holder.boardDetailBtn = view.findViewById(R.id.board_reserv_confirm_payment_wait_detail_btn)

                holder.boardBasicPrice = view.findViewById(R.id.board_reserv_confirm_payment_wait_basic_price_textView)
                holder.boardAddFarePerson = view.findViewById(R.id.board_reserv_confirm_payment_wait_add_fare_person)
                holder.boardAddFare = view.findViewById(R.id.board_reserv_confirm_payment_wait_add_fare)
                holder.boardPriceSum = view.findViewById(R.id.board_reserv_confirm_payment_wait_price_sum)
                holder.boardDiscountPrice = view.findViewById(R.id.board_reserv_confirm_payment_wait_discount_price)
                holder.boardDiscountPerson = view.findViewById(R.id.board_reserv_confirm_payment_wait_discount_person)
                holder.boardDiscountSum = view.findViewById(R.id.board_reserv_confirm_payment_wait_discount_sum)
                holder.boardEquipPrice = view.findViewById(R.id.board_reserv_confirm_payment_wait_equip_price)
                holder.boardEquipDiscount = view.findViewById(R.id.board_reserv_confirm_payment_wait_equip_discount)
                holder.boardPriceSum2 = view.findViewById(R.id.board_reserv_confirm_payment_wait_price_sum2)
                holder.boardDiscountTotal = view.findViewById(R.id.board_reserv_confirm_payment_wait_discount_total)
                holder.boardUseTimeTotal = view.findViewById(R.id.board_reserv_confirm_payment_wait_use_time_total)
                holder.boardEquipTotal = view.findViewById(R.id.board_reserv_confirm_payment_wait_equip_total)
                holder.boardTotalPrice2 = view.findViewById(R.id.board_reserv_confirm_payment_wait_total_price2)
                view.tag = holder
            }
            else if(viewType == TYPE_RESERV_FINISH){
                view = LayoutInflater.from(context).inflate(R.layout.form_reserv_confirm_reserv_finish, null)
                holder = ViewHolder()
                holder.boardWholeView =
                    view.findViewById(R.id.board_reserv_confirm_reserv_finish_LinearLayout)
                holder.boardImage = view.findViewById(R.id.board_reserv_confirm_reserv_finish_ImageView)
                holder.boardTitle = view.findViewById(R.id.board_reserv_confirm_reserv_finish_Title)
                holder.boardDate = view.findViewById(R.id.board_reserv_confirm_reserv_finish_Date)
                holder.boardTime = view.findViewById(R.id.board_reserv_confirm_reserv_finish_Time)
                holder.boardLocation = view.findViewById(R.id.board_reserv_confirm_reserv_finish_Location)
                holder.boardTotalPrice = view.findViewById(R.id.board_reserv_confirm_reserv_finish_price_sum2)
                holder.boardDetailView =
                    view.findViewById(R.id.board_reserv_confirm_reserv_finish_detail_layout)
                holder.boardDetailBtn = view.findViewById(R.id.board_reserv_confirm_reserv_finish_detail_btn)

                holder.boardBasicPrice = view.findViewById(R.id.board_reserv_confirm_reserv_finish_basic_price_textView)
                holder.boardAddFarePerson = view.findViewById(R.id.board_reserv_confirm_reserv_finish_add_fare_person)
                holder.boardAddFare = view.findViewById(R.id.board_reserv_confirm_reserv_finish_add_fare)
                holder.boardPriceSum = view.findViewById(R.id.board_reserv_confirm_reserv_finish_price_sum)
                holder.boardDiscountPrice = view.findViewById(R.id.board_reserv_confirm_reserv_finish_discount_price)
                holder.boardDiscountPerson = view.findViewById(R.id.board_reserv_confirm_reserv_finish_discount_person)
                holder.boardDiscountSum = view.findViewById(R.id.board_reserv_confirm_reserv_finish_discount_sum)
                holder.boardEquipPrice = view.findViewById(R.id.board_reserv_confirm_reserv_finish_equip_price)
                holder.boardEquipDiscount = view.findViewById(R.id.board_reserv_confirm_reserv_finish_equip_discount)
                holder.boardPriceSum2 = view.findViewById(R.id.board_reserv_confirm_reserv_finish_price_sum2)
                holder.boardDiscountTotal = view.findViewById(R.id.board_reserv_confirm_reserv_finish_discount_total)
                holder.boardUseTimeTotal = view.findViewById(R.id.board_reserv_confirm_reserv_finish_use_time_total)
                holder.boardEquipTotal = view.findViewById(R.id.board_reserv_confirm_reserv_finish_equip_total)
                holder.boardTotalPrice2 = view.findViewById(R.id.board_reserv_confirm_reserv_finish_total_price2)

                holder.boardTel = view.findViewById(R.id.board_reserv_confirm_reserv_finish_tel)
                view.tag = holder
            }
            else{  //else if(viewType == TYPE_REFUND_WAIT)
                view = LayoutInflater.from(context).inflate(R.layout.form_reserv_confirm_refund_wait, null)
                holder = ViewHolder()
                holder.boardWholeView =
                    view.findViewById(R.id.board_reserv_confirm_refund_wait_LinearLayout)
                holder.boardImage = view.findViewById(R.id.board_reserv_confirm_refund_wait_ImageView)
                holder.boardTitle = view.findViewById(R.id.board_reserv_confirm_refund_wait_Title)
                holder.boardDate = view.findViewById(R.id.board_reserv_confirm_refund_wait_Date)
                holder.boardTime = view.findViewById(R.id.board_reserv_confirm_refund_wait_Time)
                holder.boardLocation = view.findViewById(R.id.board_reserv_confirm_refund_wait_Location)
                holder.boardTotalPrice = view.findViewById(R.id.board_reserv_confirm_refund_wait_price_sum2)
                holder.boardDetailView =
                    view.findViewById(R.id.board_reserv_confirm_refund_wait_detail_layout)
                holder.boardDetailBtn = view.findViewById(R.id.board_reserv_confirm_refund_wait_detail_btn)

                holder.boardBasicPrice = view.findViewById(R.id.board_reserv_confirm_refund_wait_basic_price_textView)
                holder.boardAddFarePerson = view.findViewById(R.id.board_reserv_confirm_refund_wait_add_fare_person)
                holder.boardAddFare = view.findViewById(R.id.board_reserv_confirm_refund_wait_add_fare)
                holder.boardPriceSum = view.findViewById(R.id.board_reserv_confirm_refund_wait_price_sum)
                holder.boardDiscountPrice = view.findViewById(R.id.board_reserv_confirm_refund_wait_discount_price)
                holder.boardDiscountPerson = view.findViewById(R.id.board_reserv_confirm_refund_wait_discount_person)
                holder.boardDiscountSum = view.findViewById(R.id.board_reserv_confirm_refund_wait_discount_sum)
                holder.boardEquipPrice = view.findViewById(R.id.board_reserv_confirm_refund_wait_equip_price)
                holder.boardEquipDiscount = view.findViewById(R.id.board_reserv_confirm_refund_wait_equip_discount)
                holder.boardPriceSum2 = view.findViewById(R.id.board_reserv_confirm_refund_wait_price_sum2)
                holder.boardDiscountTotal = view.findViewById(R.id.board_reserv_confirm_refund_wait_discount_total)
                holder.boardUseTimeTotal = view.findViewById(R.id.board_reserv_confirm_refund_wait_use_time_total)
                holder.boardEquipTotal = view.findViewById(R.id.board_reserv_confirm_refund_wait_equip_total)
                holder.boardTotalPrice2 = view.findViewById(R.id.board_reserv_confirm_refund_wait_total_price2)

                holder.boardTel = view.findViewById(R.id.board_reserv_confirm_refund_wait_tel)

                holder.boardTotalPrice3 = view.findViewById(R.id.board_reserv_confirm_refund_wait_total_price3)
                view.tag = holder
            }
        /*}else{
            holder = convertView.tag as ViewHolder
            view = convertView
        }*/


        val boardReservationConfirm = boardList[position]
        val resoarceId = context.resources.getIdentifier(boardReservationConfirm.image, "drawable", context.packageName)
        holder.boardImage?.setImageResource(resoarceId)
        holder.boardTitle?.text = boardReservationConfirm.studio_name
        holder.boardDate?.text = boardReservationConfirm.date
        holder.boardTime?.text = boardReservationConfirm.time
        holder.boardLocation?.text = boardReservationConfirm.location
        holder.boardTotalPrice?.text = moneyFormat.format(boardReservationConfirm.price)

        holder.boardImage?.setColorFilter(Color.parseColor("#888888"), PorterDuff.Mode.MULTIPLY)

        holder.boardDetailBtn!!.setOnClickListener {
            showHide(holder.boardDetailView)
        }

        return view
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

    override fun getItemViewType(position: Int): Int {
        return boardList[position].type
    }

    override fun getCount(): Int {
        return boardList.size
    }

    private class ViewHolder{
        var boardWholeView : LinearLayout? = null
        var boardImage : ImageView? = null
        var boardTitle : TextView? = null
        var boardDate : TextView? = null
        var boardTime : TextView? = null
        var boardLocation : TextView? = null
        var boardTotalPrice : TextView? = null
        var boardDetailView : View? = null
        var boardDetailBtn : ConstraintLayout? = null
        var boardBasicPrice : TextView? = null
        var boardAddFarePerson : TextView? = null
        var boardAddFare : TextView? = null
        var boardPriceSum : TextView? = null
        var boardDiscountPrice : TextView? = null
        var boardDiscountPerson : TextView? = null
        var boardDiscountSum : TextView? = null
        var boardEquipPrice : TextView? = null
        var boardEquipDiscount : TextView? = null
        var boardPriceSum2 : TextView? = null
        var boardDiscountTotal : TextView? = null
        var boardUseTimeTotal : TextView? = null
        var boardEquipTotal : TextView? = null
        var boardTotalPrice2 : TextView? = null

        var boardTel : TextView? = null

        var boardTotalPrice3 : TextView? = null
    }
}