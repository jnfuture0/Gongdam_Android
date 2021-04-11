package org.gongdam.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_fragment_promotion.*
import org.gongdam.*
import org.gongdam.Adapter.ListAdapterPromotion
import org.gongdam.Json.PromotionListGet
import org.gongdam.Struct.BoardPromotion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FragmentPromotion : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    var boardList:MutableList<BoardPromotion> = mutableListOf()

    val server = MainActivity.server
    var refToken = ""
    val todayFormat : DateFormat = SimpleDateFormat("yyyy-MM-dd")
    fun sortSelectorPremiumType(p:BoardPromotion) : Int = p.type_0_premium_1_normal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_fragment_promotion, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        refToken = MainActivity.refToken

        init()

        promotion_frag_swipeRefreshLayout.setOnRefreshListener(this)

        /*클릭하는 부분*/
        setListener()

        /*reserv_confirm_page_detail_btn.setOnClickListener {
            showHide(reserv_confirm_page_detail_layout)
        }*/
        //setDescendantFocusability

        //reserv_confirm_page_1_listview.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS


        /*reserv_confirm_page_1_listview.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(context,"기본", Toast.LENGTH_LONG).show()

            //showHide(view.findViewById(R.id.reserv_confirm_page_detail_layout))
            //showHide(view)
            *//*reserv_confirm_page_detail_btn.setOnClickListener {
                Toast.makeText(context,"123", Toast.LENGTH_LONG).show()
                //showHide(reserv_confirm_page_detail_layout)
            }*//*
        }*/
    }

    override fun onRefresh() {
        init()
        promotion_frag_swipeRefreshLayout.isRefreshing = false
    }

    private fun init(){
        var postCodeStarts = ""
        for(element in PageFilterPromotion.userLocationList){
            if(MainActivity.filterMap[element] != null){
                postCodeStarts += MainActivity.filterMap[element]
            }
        }
        var startAt = PageFilterPromotion.startAt
        var endedAt = PageFilterPromotion.endedAt
        if(startAt == ""){
            startAt = todayFormat.format(Calendar.getInstance().time)
        }
        server?.getPromotionListWithFilter(postCodeStarts,startAt,endedAt)?.enqueue(object: Callback<PromotionListGet> {
            override fun onFailure(call: Call<PromotionListGet>?, t: Throwable?) { Log.e("GET_ROOM_LIST_FAILED", t.toString()) }
            override fun onResponse(call: Call<PromotionListGet>?, response: Response<PromotionListGet>?) {
                var image = " "
                var studio_name = " "
                var room_name = " "
                var location = " "
                var content = ""
                var premium_type = 1
                var rating = 3f
                var room_id = 0
                var promotion_id = 0
                var won_per_hour = 0
                var won_per_hour_old = 0
                var discount_percent = 0
                var startTime = ""
                var endTime = ""
                var result = response?.body()?.result
                boardList = mutableListOf()
                for( i in 0 until result?.size!!.toInt()){
                    val resultGetRoom = result?.get(i).room
                    if(resultGetRoom?.images!!.isNotEmpty()) {
                        image = resultGetRoom?.images!![0]
                        //image = MainActivity.makeImageString(image)
                    }
                    studio_name = resultGetRoom?.studio?.name!!
                    room_name = resultGetRoom?.name!!
                    location = resultGetRoom?.studio?.address?.district!!
                    for (j in 0 until resultGetRoom?.tags?.size!!.toInt()){
                        content += resultGetRoom?.tags?.get(j).toString() + " "
                    }
                    premium_type = resultGetRoom?.grade!!
                    rating = resultGetRoom?.star_average!!.toFloat()
                    room_id = resultGetRoom?.id!!
                    promotion_id = result?.get(i).id!!
                    won_per_hour = result?.get(i).price?.per_hour!!
                    won_per_hour_old = resultGetRoom?.price?.per_hour!!
                    discount_percent = ((1 - won_per_hour.toDouble() / won_per_hour_old.toDouble()) * 100.0).toInt()
                    startTime = result.get(i).started_at!!
                    endTime = result.get(i).ended_at!!

                    boardList.add(BoardPromotion(image, studio_name, room_name, location, content, premium_type, rating, promotion_id, room_id, discount_percent.toString(), won_per_hour,won_per_hour_old, startTime, endTime))
                    image = " "
                    studio_name = " "
                    room_name = " "
                    location = " "
                    content = ""
                }

                boardList.sortByDescending { sortSelectorPremiumType(it) }
                var boardListAdapter = ListAdapterPromotion(requireContext(), boardList)
                promotion_frag_page_listview.adapter = boardListAdapter
            }
        })
    }

    private fun setListener(){
        promotion_frag_page_listview.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val item = adapterView.getItemAtPosition(i) as BoardPromotion
            activity?.let{
                val intent = Intent(it, PagePromotion::class.java)
                intent.putExtra("promotion_id", item.promotion_id)
                intent.putExtra("room_id", item.room_id)
                intent.putExtra("ref_token", refToken)
                it.startActivity(intent)
            }
        }
    }

    fun showHide(view:View){
        view.visibility = if(view.visibility == View.VISIBLE){
            View.GONE
        } else{
            View.VISIBLE
        }
    }
}
