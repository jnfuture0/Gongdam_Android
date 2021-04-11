package org.gongdam.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.frag_reserv_confirm_1.*
import org.gongdam.Adapter.ListAdapterReservationConfirm
import org.gongdam.Adapter.RecyclerAdapterReservationConfirm
import org.gongdam.Json.RefreshToken
import org.gongdam.Json.ReservSentGet
import org.gongdam.Json.RoomReviewGet
import org.gongdam.MainActivity
import org.gongdam.PageReservationConfirm
import org.gongdam.Payment.Payment
import org.gongdam.R
import org.gongdam.Struct.BoardReservationConfirm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservConfirm2 : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    val TYPE_WAIT = 1           //예약대기
    val TYPE_ADJUST = 2         //예약거절  - 취소 / 환불
    val TYPE_PAYMENT_WAIT = 17  //입금대기
    val TYPE_RESERV_FINISH = 18 //입금완료
    val TYPE_REFUND_WAIT = 33   //환불대기  - 취소 / 환불
    val TYPE_REFUND_FINISH = 34 //환불완료
    val TYPE_USE_FINISH = 49    //사용완료  - 지나간 예약
    val TYPE_RESERV_EXPIRE = 241 //예약 expire
    val TYPE_PAYMENT_EXPIRE = 242 //입금 expire
    val TYPE_RESERV_DELETE = 243 //예약 삭제

    //var token = MainActivity.getToken(MainActivity.refToken)
    //var token = PageReservationConfirm.token
    var refToken = PageReservationConfirm.refToken
    var server = MainActivity.server

    var boardList:MutableList<BoardReservationConfirm> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_reserv_confirm_1, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRefresh() {
        boardList = mutableListOf()
        init()
        reserv_confirm_page_1_swipeRefresh.isRefreshing = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()

        reserv_confirm_page_1_listview.layoutManager = LinearLayoutManager(context)
        reserv_confirm_page_1_swipeRefresh.setOnRefreshListener(this)

    }

    private fun init(){
        val tokenMap:HashMap<String, Any> = HashMap()
        tokenMap["refresh_token"] = refToken
        server?.refreshToken(tokenMap)?.enqueue(object: Callback<RefreshToken> {
            override fun onFailure(call: Call<RefreshToken>?, t: Throwable?) { Log.e("REFRESH_TOKEN_FAILED", t.toString())}
            override fun onResponse(call: Call<RefreshToken>?, response: Response<RefreshToken>?) {
                var token = response?.body()?.result?.access_token!!
                server?.getReservationSent(token)?.enqueue(object: Callback<ReservSentGet> {
                    override fun onFailure(call: Call<ReservSentGet>?, t: Throwable?) { Log.e("GET_RESERV_SENT_FAILED", t.toString()) }
                    override fun onResponse(call: Call<ReservSentGet>?, response: Response<ReservSentGet>?) {
                        val result = response?.body()?.result
                        for(element in result!!) {
                            val type:Int = element.status!!
                            if(type == TYPE_USE_FINISH) {

                                val reservID = element.id!!
                                val elementPrice = element.prices
                                var img = element.room?.images!![0]
                                val studio_name: String = element.room?.studio?.name!!
                                val room_name = element.room?.name!!
                                val date: String = element.started_at!!.substring(0, 10) + " " + element.started_at!!.substring(11, 16)
                                val time: String = " ~ " + element.ended_at!!.substring(0, 10) + " " + element.ended_at!!.substring(11, 16)
                                val location: String = element.room?.studio?.address?.district!!
                                val basic_price: Int = elementPrice?.room_price!!
                                val add_fare_person: Int =
                                    elementPrice?.additional_person_price!!
                                val add_fare: Int = elementPrice?.additional_fare_price!!
                                val price_sum =
                                    (elementPrice?.room_price!! + elementPrice?.additional_person_price!! + elementPrice.additional_fare_price!! )
                                val discount_price: Int = elementPrice?.discount_price!!
                                val discount_person: Int = 0
                                val discount_sum = discount_price
                                val equip_price: Int = elementPrice.equipment_price!!
                                val equip_discount: Int = 0
                                val discount_total: Int = 0
                                val st_time = element.started_at!!.substring(11, 13)
                                val end_time = element.ended_at!!.substring(11, 13)
                                val time_total: Int = Integer.parseInt(end_time) - Integer.parseInt(st_time)
                                val equip_total: Int = elementPrice.equipment_price!!
                                val total_price2: Int = price_sum - discount_price + equip_total
                                val price:Int = total_price2
                                val tel: String = ""
                                boardList.add(
                                    BoardReservationConfirm(
                                        reservID,
                                        img,
                                        studio_name,
                                        room_name,
                                        date,
                                        time,
                                        location,
                                        price,
                                        type,
                                        basic_price,
                                        add_fare_person,
                                        add_fare,
                                        price_sum,
                                        discount_price,
                                        discount_person,
                                        discount_sum,
                                        equip_price,
                                        equip_discount,
                                        price_sum,
                                        discount_total,
                                        time_total,
                                        equip_total,
                                        total_price2,
                                        tel
                                    )
                                )
                            }
                        }
                        var boardListAdapter = RecyclerAdapterReservationConfirm(context!!, boardList){status, btnNum, board ->
                        }
                        reserv_confirm_page_1_listview.adapter = boardListAdapter
                    }
                })
            }
        })
    }

    fun showHide(view:View){
        view.visibility = if(view.visibility == View.VISIBLE){
            View.GONE
        } else{
            View.VISIBLE
        }
    }
}
