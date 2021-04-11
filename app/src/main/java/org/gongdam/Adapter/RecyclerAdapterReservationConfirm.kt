package org.gongdam.Adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.form_reserv_confirm_adjust.view.*
import kotlinx.android.synthetic.main.form_reserv_confirm_payment_wait.view.*
import kotlinx.android.synthetic.main.form_reserv_confirm_refund_wait.view.*
import kotlinx.android.synthetic.main.form_reserv_confirm_reserv_finish.view.*
import kotlinx.android.synthetic.main.form_reserv_confirm_use_finish.view.*
import kotlinx.android.synthetic.main.form_reserv_confirm_wait.view.*
import org.gongdam.MainActivity
import org.gongdam.R
import org.gongdam.Struct.BoardMyReview1
import org.gongdam.Struct.BoardReservationConfirm

class RecyclerAdapterReservationConfirm(val context: Context, val boardList: MutableList<BoardReservationConfirm>, val itemClick:(status:Int, btn:Int, board:BoardReservationConfirm) -> Unit)
    : RecyclerView.Adapter<RecyclerAdapterReservationConfirm.BaseViewHolder<*>>() {

    val TYPE_WAIT = 1           //예약대기
    val TYPE_ADJUST = 2         //예약조정
    val TYPE_PAYMENT_WAIT = 17  //입금대기
    val TYPE_RESERV_FINISH = 18 //입금완료
    val TYPE_REFUND_WAIT = 33   //환불대기
    val TYPE_REFUND_FINISH = 34 //환불완료
    val TYPE_USE_FINISH = 49    //사용완료
    val TYPE_RESERV_EXPIRE = 241    //예약 expire
    val TYPE_PAYMENT_EXPIRE = 242   //입금 expire
    val TYPE_RESERV_DELETE = 243    //예약 삭제

    val moneyFormat = MainActivity.moneyFormatter

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }


    inner class ViewHolder0Wait(view:View):BaseViewHolder<BoardReservationConfirm>(view){
        val boardWholeView = view.board_reserv_confirm_wait_LinearLayout
        val boardImage = view.board_reserv_confirm_wait_ImageView
        val boardTitle = view.board_reserv_confirm_wait_Title
        val boardDate = view.board_reserv_confirm_wait_Date
        val boardTime = view.board_reserv_confirm_wait_Time
        val boardLocation = view.board_reserv_confirm_wait_Location
        val boardTotalPrice = view.board_reserv_confirm_wait_total_price
        val boardDetailView = view.board_reserv_confirm_wait_detail_layout
        val boardDetailBtn = view.board_reserv_confirm_wait_detail_btn

        val boardCancelBtn = view.board_reserv_confirm_wait_cancel_btn

        val boardBasicPrice = view.board_reserv_confirm_wait_basic_price_textView
        val boardAddFarePerson = view.board_reserv_confirm_wait_add_fare_person
        val boardAddFare = view.board_reserv_confirm_wait_add_fare
        val boardPriceSum = view.board_reserv_confirm_wait_price_sum
        val boardDiscountPrice = view.board_reserv_confirm_wait_discount_price
        val boardDiscountPerson = view.board_reserv_confirm_wait_discount_person
        val boardDiscountSum = view.board_reserv_confirm_wait_discount_sum
        val boardEquipPrice = view.board_reserv_confirm_wait_equip_price
        val boardEquipDiscount = view.board_reserv_confirm_wait_equip_discount
        val boardPriceSum2 = view.board_reserv_confirm_wait_price_sum2
        val boardDiscountTotal = view.board_reserv_confirm_wait_discount_total
        val boardUseTimeTotal = view.board_reserv_confirm_wait_use_time_total
        val boardEquipTotal = view.board_reserv_confirm_wait_equip_total
        val boardTotalPrice2 = view.board_reserv_confirm_wait_total_price2

        override fun bind(board: BoardReservationConfirm) {
            boardImage?.setColorFilter(Color.parseColor("#888888"), PorterDuff.Mode.MULTIPLY)
            Glide.with(context).load(board.image).into(boardImage)
            boardTitle?.text = "[${board.studio_name}] ${board.room_name}"
            boardDate?.text = board.date
            boardTime?.text = board.time
            boardLocation?.text = board.location

            boardTotalPrice?.text = moneyFormat.format(board.price)
            boardBasicPrice?.text = moneyFormat.format(board.basic_price)
            boardAddFarePerson?.text = moneyFormat.format(board.add_fare_person)
            boardAddFare?.text = moneyFormat.format(board.add_fare)//board.add_fare
            boardPriceSum?.text = moneyFormat.format(board.price_sum)//board.price_sum
            boardDiscountPrice?.text = moneyFormat.format(board.discount_price)//board.discount_price
            boardDiscountPerson?.text = moneyFormat.format(board.discount_person)//board.discount_person
            boardDiscountSum?.text = moneyFormat.format(board.discount_sum)//board.discount_sum
            boardEquipPrice?.text = moneyFormat.format(board.equip_price)//board.equip_price
            boardEquipDiscount?.text = moneyFormat.format(board.equip_discount)//board.equip_discount
            boardPriceSum2?.text = moneyFormat.format(board.price_sum2)//board.price_sum2
            boardDiscountTotal?.text = moneyFormat.format(board.discount_total)//board.discount_total
            boardUseTimeTotal?.text = moneyFormat.format(board.time_total)//board.time_total
            boardEquipTotal?.text = moneyFormat.format(board.equip_total)//board.equip_total
            boardTotalPrice2?.text = moneyFormat.format(board.total_price2)//board.total_price2

            boardDetailBtn!!.setOnClickListener {
                showHide(boardDetailView)
            }

            boardCancelBtn.setOnClickListener {
                itemClick(TYPE_WAIT, 1, board)
            }

        }
    }

    inner class ViewHolder1Adjust(view:View):BaseViewHolder<BoardReservationConfirm>(view){
        val boardWholeView = view.board_reserv_confirm_adjust_LinearLayout
        val boardImage = view.board_reserv_confirm_adjust_ImageView
        val boardTitle = view.board_reserv_confirm_adjust_Title
        val boardDate = view.board_reserv_confirm_adjust_Date
        val boardTime = view.board_reserv_confirm_adjust_Time
        val boardLocation = view.board_reserv_confirm_adjust_Location
        val boardTotalPrice = view.board_reserv_confirm_adjust_total_price
        val boardDetailView = view.board_reserv_confirm_adjust_detail_layout
        val boardDetailBtn = view.board_reserv_confirm_adjust_detail_btn

        val boardAgreeBtn = view.board_reserv_confirm_adjust_agree_btn
        val boardCancelBtn = view.board_reserv_confirm_adjust_cancel_btn

        val boardBasicPrice = view.board_reserv_confirm_adjust_basic_price_textView
        val boardAddFarePerson = view.board_reserv_confirm_adjust_add_fare_person
        val boardAddFare = view.board_reserv_confirm_adjust_add_fare
        val boardPriceSum = view.board_reserv_confirm_adjust_price_sum
        val boardDiscountPrice = view.board_reserv_confirm_adjust_discount_price
        val boardDiscountPerson = view.board_reserv_confirm_adjust_discount_person
        val boardDiscountSum = view.board_reserv_confirm_adjust_discount_sum
        val boardEquipPrice = view.board_reserv_confirm_adjust_equip_price
        val boardEquipDiscount = view.board_reserv_confirm_adjust_equip_discount
        val boardPriceSum2 = view.board_reserv_confirm_adjust_price_sum2
        val boardDiscountTotal = view.board_reserv_confirm_adjust_discount_total
        val boardUseTimeTotal = view.board_reserv_confirm_adjust_use_time_total
        val boardEquipTotal = view.board_reserv_confirm_adjust_equip_total
        val boardTotalPrice2 = view.board_reserv_confirm_adjust_total_price2

        override fun bind(board: BoardReservationConfirm) {
            boardImage?.setColorFilter(Color.parseColor("#888888"), PorterDuff.Mode.MULTIPLY)
            Glide.with(context).load(board.image).into(boardImage)
            boardTitle?.text = "[${board.studio_name}] ${board.room_name}"
            boardDate?.text = board.date
            boardTime?.text = board.time
            boardLocation?.text = board.location
            boardTotalPrice?.text = moneyFormat.format(board.price)
            boardBasicPrice?.text = moneyFormat.format(board.basic_price)
            boardAddFarePerson?.text = moneyFormat.format(board.add_fare_person)
            boardAddFare?.text = moneyFormat.format(board.add_fare)//board.add_fare
            boardPriceSum?.text = moneyFormat.format(board.price_sum)//board.price_sum
            boardDiscountPrice?.text = moneyFormat.format(board.discount_price)//board.discount_price
            boardDiscountPerson?.text = moneyFormat.format(board.discount_person)//board.discount_person
            boardDiscountSum?.text = moneyFormat.format(board.discount_sum)//board.discount_sum
            boardEquipPrice?.text = moneyFormat.format(board.equip_price)//board.equip_price
            boardEquipDiscount?.text = moneyFormat.format(board.equip_discount)//board.equip_discount
            boardPriceSum2?.text = moneyFormat.format(board.price_sum2)//board.price_sum2
            boardDiscountTotal?.text = moneyFormat.format(board.discount_total)//board.discount_total
            boardUseTimeTotal?.text = moneyFormat.format(board.time_total)//board.time_total
            boardEquipTotal?.text = moneyFormat.format(board.equip_total)//board.equip_total
            boardTotalPrice2?.text = moneyFormat.format(board.total_price2)//board.total_price2

            boardDetailBtn!!.setOnClickListener {
                showHide(boardDetailView)
            }

            boardAgreeBtn.setOnClickListener {
                itemClick(TYPE_ADJUST, 0, board)
            }

            boardCancelBtn.setOnClickListener {
                itemClick(TYPE_ADJUST, 1, board)
            }
        }
    }

    inner class ViewHolder2PaymentWait(view:View):BaseViewHolder<BoardReservationConfirm>(view){
        val boardWholeView = view.board_reserv_confirm_payment_wait_LinearLayout
        val boardImage = view.board_reserv_confirm_payment_wait_ImageView
        val boardTitle = view.board_reserv_confirm_payment_wait_Title
        val boardDate = view.board_reserv_confirm_payment_wait_Date
        val boardTime = view.board_reserv_confirm_payment_wait_Time
        val boardLocation = view.board_reserv_confirm_payment_wait_Location
        val boardTotalPrice = view.board_reserv_confirm_payment_wait_total_price
        val boardDetailView = view.board_reserv_confirm_payment_wait_detail_layout
        val boardDetailBtn = view.board_reserv_confirm_payment_wait_detail_btn

        val boardPaymentBtn = view.board_reserv_confirm_payment_wait_payment_btn
        val boardCancelBtn = view.board_reserv_confirm_payment_wait_cancel_btn

        val boardBasicPrice = view.board_reserv_confirm_payment_wait_basic_price_textView
        val boardAddFarePerson = view.board_reserv_confirm_payment_wait_add_fare_person
        val boardAddFare = view.board_reserv_confirm_payment_wait_add_fare
        val boardPriceSum = view.board_reserv_confirm_payment_wait_price_sum
        val boardDiscountPrice = view.board_reserv_confirm_payment_wait_discount_price
        val boardDiscountPerson = view.board_reserv_confirm_payment_wait_discount_person
        val boardDiscountSum = view.board_reserv_confirm_payment_wait_discount_sum
        val boardEquipPrice = view.board_reserv_confirm_payment_wait_equip_price
        val boardEquipDiscount = view.board_reserv_confirm_payment_wait_equip_discount
        val boardPriceSum2 = view.board_reserv_confirm_payment_wait_price_sum2
        val boardDiscountTotal = view.board_reserv_confirm_payment_wait_discount_total
        val boardUseTimeTotal = view.board_reserv_confirm_payment_wait_use_time_total
        val boardEquipTotal = view.board_reserv_confirm_payment_wait_equip_total
        val boardTotalPrice2 = view.board_reserv_confirm_payment_wait_total_price2

        override fun bind(board: BoardReservationConfirm) {
            boardImage?.setColorFilter(Color.parseColor("#888888"), PorterDuff.Mode.MULTIPLY)
            Glide.with(context).load(board.image).into(boardImage)
            boardTitle?.text = "[${board.studio_name}] ${board.room_name}"
            boardDate?.text = board.date
            boardTime?.text = board.time
            boardLocation?.text = board.location
            boardTotalPrice?.text = moneyFormat.format(board.price)
            boardBasicPrice?.text = moneyFormat.format(board.basic_price)
            boardAddFarePerson?.text = moneyFormat.format(board.add_fare_person)
            boardAddFare?.text = moneyFormat.format(board.add_fare)//board.add_fare
            boardPriceSum?.text = moneyFormat.format(board.price_sum)//board.price_sum
            boardDiscountPrice?.text = moneyFormat.format(board.discount_price)//board.discount_price
            boardDiscountPerson?.text = moneyFormat.format(board.discount_person)//board.discount_person
            boardDiscountSum?.text = moneyFormat.format(board.discount_sum)//board.discount_sum
            boardEquipPrice?.text = moneyFormat.format(board.equip_price)//board.equip_price
            boardEquipDiscount?.text = moneyFormat.format(board.equip_discount)//board.equip_discount
            boardPriceSum2?.text = moneyFormat.format(board.price_sum2)//board.price_sum2
            boardDiscountTotal?.text = moneyFormat.format(board.discount_total)//board.discount_total
            boardUseTimeTotal?.text = moneyFormat.format(board.time_total)//board.time_total
            boardEquipTotal?.text = moneyFormat.format(board.equip_total)//board.equip_total
            boardTotalPrice2?.text = moneyFormat.format(board.total_price2)//board.total_price2

            boardDetailBtn!!.setOnClickListener {
                showHide(boardDetailView)
            }

            boardPaymentBtn.setOnClickListener {
                itemClick(TYPE_PAYMENT_WAIT, 0, board)
            }

            boardCancelBtn.setOnClickListener {
                itemClick(TYPE_PAYMENT_WAIT, 1, board)
            }
        }
    }

    inner class ViewHolder3ReservFinish(view:View):BaseViewHolder<BoardReservationConfirm>(view){
        val boardWholeView = view.board_reserv_confirm_reserv_finish_LinearLayout
        val boardImage = view.board_reserv_confirm_reserv_finish_ImageView
        val boardTitle = view.board_reserv_confirm_reserv_finish_Title
        val boardDate = view.board_reserv_confirm_reserv_finish_Date
        val boardTime = view.board_reserv_confirm_reserv_finish_Time
        val boardLocation = view.board_reserv_confirm_reserv_finish_Location
        val boardTotalPrice = view.board_reserv_confirm_reserv_finish_total_price
        val boardDetailView = view.board_reserv_confirm_reserv_finish_detail_layout
        val boardDetailBtn = view.board_reserv_confirm_reserv_finish_detail_btn

        val boardCancelBtn = view.board_reserv_confirm_reserv_finish_cancel_btn

        val boardBasicPrice = view.board_reserv_confirm_reserv_finish_basic_price_textView
        val boardAddFarePerson = view.board_reserv_confirm_reserv_finish_add_fare_person
        val boardAddFare = view.board_reserv_confirm_reserv_finish_add_fare
        val boardPriceSum = view.board_reserv_confirm_reserv_finish_price_sum
        val boardDiscountPrice = view.board_reserv_confirm_reserv_finish_discount_price
        val boardDiscountPerson = view.board_reserv_confirm_reserv_finish_discount_person
        val boardDiscountSum = view.board_reserv_confirm_reserv_finish_discount_sum
        val boardEquipPrice = view.board_reserv_confirm_reserv_finish_equip_price
        val boardEquipDiscount = view.board_reserv_confirm_reserv_finish_equip_discount
        val boardPriceSum2 = view.board_reserv_confirm_reserv_finish_price_sum2
        val boardDiscountTotal = view.board_reserv_confirm_reserv_finish_discount_total
        val boardUseTimeTotal = view.board_reserv_confirm_reserv_finish_use_time_total
        val boardEquipTotal = view.board_reserv_confirm_reserv_finish_equip_total
        val boardTotalPrice2 = view.board_reserv_confirm_reserv_finish_total_price2
        val boardTel = view.board_reserv_confirm_reserv_finish_tel

        override fun bind(board: BoardReservationConfirm) {
            boardImage?.setColorFilter(Color.parseColor("#888888"), PorterDuff.Mode.MULTIPLY)
            Glide.with(context).load(board.image).into(boardImage)
            boardTitle?.text = "[${board.studio_name}] ${board.room_name}"
            boardDate?.text = board.date
            boardTime?.text = board.time
            boardLocation?.text = board.location
            boardTotalPrice?.text = moneyFormat.format(board.price)
            boardBasicPrice?.text = moneyFormat.format(board.basic_price)
            boardAddFarePerson?.text = moneyFormat.format(board.add_fare_person)
            boardAddFare?.text = moneyFormat.format(board.add_fare)//board.add_fare
            boardPriceSum?.text = moneyFormat.format(board.price_sum)//board.price_sum
            boardDiscountPrice?.text = moneyFormat.format(board.discount_price)//board.discount_price
            boardDiscountPerson?.text = moneyFormat.format(board.discount_person)//board.discount_person
            boardDiscountSum?.text = moneyFormat.format(board.discount_sum)//board.discount_sum
            boardEquipPrice?.text = moneyFormat.format(board.equip_price)//board.equip_price
            boardEquipDiscount?.text = moneyFormat.format(board.equip_discount)//board.equip_discount
            boardPriceSum2?.text = moneyFormat.format(board.price_sum2)//board.price_sum2
            boardDiscountTotal?.text = moneyFormat.format(board.discount_total)//board.discount_total
            boardUseTimeTotal?.text = moneyFormat.format(board.time_total)//board.time_total
            boardEquipTotal?.text = moneyFormat.format(board.equip_total)//board.equip_total
            boardTotalPrice2?.text = moneyFormat.format(board.total_price2)//board.total_price2
            boardTel?.text = board.tel

            boardDetailBtn!!.setOnClickListener {
                showHide(boardDetailView)
            }


            boardCancelBtn.setOnClickListener {
                itemClick(TYPE_RESERV_FINISH, 0, board)
            }
        }
    }

    inner class ViewHolder4RefundWait(view:View):BaseViewHolder<BoardReservationConfirm>(view){
        val boardWholeView = view.board_reserv_confirm_refund_wait_LinearLayout
        val boardImage = view.board_reserv_confirm_refund_wait_ImageView
        val boardTitle = view.board_reserv_confirm_refund_wait_Title
        val boardDate = view.board_reserv_confirm_refund_wait_Date
        val boardTime = view.board_reserv_confirm_refund_wait_Time
        val boardLocation = view.board_reserv_confirm_refund_wait_Location
        val boardTotalPrice = view.board_reserv_confirm_refund_wait_total_price
        val boardDetailView = view.board_reserv_confirm_refund_wait_detail_layout
        val boardDetailBtn = view.board_reserv_confirm_refund_wait_detail_btn

        val boardBasicPrice = view.board_reserv_confirm_refund_wait_basic_price_textView
        val boardAddFarePerson = view.board_reserv_confirm_refund_wait_add_fare_person
        val boardAddFare = view.board_reserv_confirm_refund_wait_add_fare
        val boardPriceSum = view.board_reserv_confirm_refund_wait_price_sum
        val boardDiscountPrice = view.board_reserv_confirm_refund_wait_discount_price
        val boardDiscountPerson = view.board_reserv_confirm_refund_wait_discount_person
        val boardDiscountSum = view.board_reserv_confirm_refund_wait_discount_sum
        val boardEquipPrice = view.board_reserv_confirm_refund_wait_equip_price
        val boardEquipDiscount = view.board_reserv_confirm_refund_wait_equip_discount
        val boardPriceSum2 = view.board_reserv_confirm_refund_wait_price_sum2
        val boardDiscountTotal = view.board_reserv_confirm_refund_wait_discount_total
        val boardUseTimeTotal = view.board_reserv_confirm_refund_wait_use_time_total
        val boardEquipTotal = view.board_reserv_confirm_refund_wait_equip_total
        val boardTotalPrice2 = view.board_reserv_confirm_refund_wait_total_price2
        val boardTotalPrice3 = view.board_reserv_confirm_refund_wait_total_price3
        val boardTel = view.board_reserv_confirm_refund_wait_tel

        override fun bind(board: BoardReservationConfirm) {
            boardImage?.setColorFilter(Color.parseColor("#888888"), PorterDuff.Mode.MULTIPLY)
            Glide.with(context).load(board.image).into(boardImage)
            boardTitle?.text = "[${board.studio_name}] ${board.room_name}"
            boardDate?.text = board.date
            boardTime?.text = board.time
            boardLocation?.text = board.location
            boardTotalPrice?.text = moneyFormat.format(board.price)
            boardBasicPrice?.text = moneyFormat.format(board.basic_price)
            boardAddFarePerson?.text = moneyFormat.format(board.add_fare_person)
            boardAddFare?.text = moneyFormat.format(board.add_fare)//board.add_fare
            boardPriceSum?.text = moneyFormat.format(board.price_sum)//board.price_sum
            boardDiscountPrice?.text = moneyFormat.format(board.discount_price)//board.discount_price
            boardDiscountPerson?.text = moneyFormat.format(board.discount_person)//board.discount_person
            boardDiscountSum?.text = moneyFormat.format(board.discount_sum)//board.discount_sum
            boardEquipPrice?.text = moneyFormat.format(board.equip_price)//board.equip_price
            boardEquipDiscount?.text = moneyFormat.format(board.equip_discount)//board.equip_discount
            boardPriceSum2?.text = moneyFormat.format(board.price_sum2)//board.price_sum2
            boardDiscountTotal?.text = moneyFormat.format(board.discount_total)//board.discount_total
            boardUseTimeTotal?.text = moneyFormat.format(board.time_total)//board.time_total
            boardEquipTotal?.text = moneyFormat.format(board.equip_total)//board.equip_total
            boardTotalPrice2?.text = moneyFormat.format(board.total_price2)//board.total_price2
            boardTotalPrice3?.text = moneyFormat.format(board.total_price2) + "원"//board.total_price2
            boardTel?.text = board.tel

            boardDetailBtn!!.setOnClickListener {
                showHide(boardDetailView)
            }
        }
    }

    inner class ViewHolder5UseFinish(view:View, txt1:String? = null, txt2:String? = null):BaseViewHolder<BoardReservationConfirm>(view){
        val boardWholeView = view.board_reserv_confirm_use_finish_LinearLayout
        val boardTxt1 = view.board_reserv_confirm_use_finish_txt1
        val boardTxt2 = view.board_reserv_confirm_use_finish_txt2
        val text1 = txt1
        val text2 = txt2

        val boardImage = view.board_reserv_confirm_use_finish_ImageView
        val boardTitle = view.board_reserv_confirm_use_finish_Title
        val boardDate = view.board_reserv_confirm_use_finish_Date
        val boardTime = view.board_reserv_confirm_use_finish_Time
        val boardLocation = view.board_reserv_confirm_use_finish_Location
        val boardTotalPrice = view.board_reserv_confirm_use_finish_total_price
        val boardDetailView = view.board_reserv_confirm_use_finish_detail_layout
        val boardDetailBtn = view.board_reserv_confirm_use_finish_detail_btn

        val boardBasicPrice = view.board_reserv_confirm_use_finish_basic_price_textView
        val boardAddFarePerson = view.board_reserv_confirm_use_finish_add_fare_person
        val boardAddFare = view.board_reserv_confirm_use_finish_add_fare
        val boardPriceSum = view.board_reserv_confirm_use_finish_price_sum
        val boardDiscountPrice = view.board_reserv_confirm_use_finish_discount_price
        val boardDiscountPerson = view.board_reserv_confirm_use_finish_discount_person
        val boardDiscountSum = view.board_reserv_confirm_use_finish_discount_sum
        val boardEquipPrice = view.board_reserv_confirm_use_finish_equip_price
        val boardEquipDiscount = view.board_reserv_confirm_use_finish_equip_discount
        val boardPriceSum2 = view.board_reserv_confirm_use_finish_price_sum2
        val boardDiscountTotal = view.board_reserv_confirm_use_finish_discount_total
        val boardUseTimeTotal = view.board_reserv_confirm_use_finish_use_time_total
        val boardEquipTotal = view.board_reserv_confirm_use_finish_equip_total
        val boardTotalPrice2 = view.board_reserv_confirm_use_finish_total_price2
        val boardTel = view.board_reserv_confirm_use_finish_tel

        override fun bind(board: BoardReservationConfirm) {
            if(text1 != null){
                boardTxt1.text = text1
            }
            if(text2 != null){
                boardTxt2.text = text2
            }
            boardImage?.setColorFilter(Color.parseColor("#888888"), PorterDuff.Mode.MULTIPLY)
            Glide.with(context).load(board.image).into(boardImage)
            boardTitle?.text = "[${board.studio_name}] ${board.room_name}"
            boardDate?.text = board.date
            boardTime?.text = board.time
            boardLocation?.text = board.location
            boardTotalPrice?.text = moneyFormat.format(board.price)
            boardBasicPrice?.text = moneyFormat.format(board.basic_price)
            boardAddFarePerson?.text = moneyFormat.format(board.add_fare_person)
            boardAddFare?.text = moneyFormat.format(board.add_fare)//board.add_fare
            boardPriceSum?.text = moneyFormat.format(board.price_sum)//board.price_sum
            boardDiscountPrice?.text = moneyFormat.format(board.discount_price)//board.discount_price
            boardDiscountPerson?.text = moneyFormat.format(board.discount_person)//board.discount_person
            boardDiscountSum?.text = moneyFormat.format(board.discount_sum)//board.discount_sum
            boardEquipPrice?.text = moneyFormat.format(board.equip_price)//board.equip_price
            boardEquipDiscount?.text = moneyFormat.format(board.equip_discount)//board.equip_discount
            boardPriceSum2?.text = moneyFormat.format(board.price_sum2)//board.price_sum2
            boardDiscountTotal?.text = moneyFormat.format(board.discount_total)//board.discount_total
            boardUseTimeTotal?.text = moneyFormat.format(board.time_total)//board.time_total
            boardEquipTotal?.text = moneyFormat.format(board.equip_total)//board.equip_total
            boardTotalPrice2?.text = moneyFormat.format(board.total_price2)//board.total_price2
            boardTel?.text = board.tel

            boardDetailBtn!!.setOnClickListener {
                showHide(boardDetailView)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType){
            TYPE_WAIT -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_reserv_confirm_wait, parent, false)
                ViewHolder0Wait(view)
            }
            TYPE_ADJUST -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_reserv_confirm_adjust, parent, false)
                ViewHolder1Adjust(view)
            }
            TYPE_PAYMENT_WAIT -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_reserv_confirm_payment_wait, parent, false)
                ViewHolder2PaymentWait(view)
            }
            TYPE_RESERV_FINISH -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_reserv_confirm_reserv_finish, parent, false)
                ViewHolder3ReservFinish(view)
            }
            TYPE_REFUND_WAIT -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_reserv_confirm_refund_wait, parent, false)
                ViewHolder4RefundWait(view)
            }
            TYPE_REFUND_FINISH -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_reserv_confirm_use_finish, parent, false)
                ViewHolder5UseFinish(view, "환불 완료","환불이 완료되었습니다.")
            }
            TYPE_USE_FINISH -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_reserv_confirm_use_finish, parent, false)
                ViewHolder5UseFinish(view)
            }
            TYPE_RESERV_EXPIRE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_reserv_confirm_use_finish, parent, false)
                ViewHolder5UseFinish(view, "예약 만료","예약 기간이 만료되었습니다.")
            }
            TYPE_PAYMENT_EXPIRE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_reserv_confirm_use_finish, parent, false)
                ViewHolder5UseFinish(view, "입금 만료","입금 기간이 만료되었습니다.")
            }
            TYPE_RESERV_DELETE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_reserv_confirm_use_finish, parent, false)
                ViewHolder5UseFinish(view, "예약 삭제", "예약이 취소되었습니다.")
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = boardList[position]
        when(holder){
            is ViewHolder0Wait -> holder.bind(element as BoardReservationConfirm)
            is ViewHolder1Adjust -> holder.bind(element as BoardReservationConfirm)
            is ViewHolder2PaymentWait -> holder.bind(element as BoardReservationConfirm)
            is ViewHolder3ReservFinish -> holder.bind(element as BoardReservationConfirm)
            is ViewHolder4RefundWait -> holder.bind(element as BoardReservationConfirm)
            is ViewHolder5UseFinish -> holder.bind(element as BoardReservationConfirm)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return boardList[position].type
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    fun showHide(view:View?){
        view?.visibility = if(view?.visibility == View.VISIBLE){
            View.GONE
        } else{
            View.VISIBLE
        }
    }

}

