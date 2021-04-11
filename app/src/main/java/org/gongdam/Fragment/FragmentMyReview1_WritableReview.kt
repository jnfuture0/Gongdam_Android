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
import kotlinx.android.synthetic.main.activity_fragment_my_review1.*
import org.gongdam.*
import org.gongdam.Adapter.RecyclerAdapterMyReview1
import org.gongdam.Json.RefreshToken
import org.gongdam.Json.ReservSentGet
import org.gongdam.Struct.BoardMyReview1
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentMyReview1_WritableReview : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_fragment_my_review1, container, false)
    }


    var boardList:MutableList<BoardMyReview1> = mutableListOf()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()

        myReview1_swipeRefreshLayout.setOnRefreshListener(this)

        myReview1_listView.layoutManager = LinearLayoutManager(this@FragmentMyReview1_WritableReview.requireContext())


    }

    private fun init(){
        val server = MainActivity.server
        val refToken = PageMyReview.refToken

        val tokenMap:HashMap<String, Any> = HashMap()
        tokenMap["refresh_token"] = refToken
        server?.refreshToken(tokenMap)?.enqueue(object: Callback<RefreshToken> {
            override fun onFailure(call: Call<RefreshToken>?, t: Throwable?) { Log.e("REFRESH_TOKEN_FAILED", t.toString())}
            override fun onResponse(call: Call<RefreshToken>?, response: Response<RefreshToken>?) {
                val token = response?.body()?.result?.access_token!!
                server.getReservationSent(token).enqueue(object: Callback<ReservSentGet> {
                    override fun onFailure(call: Call<ReservSentGet>?, t: Throwable?) { Log.e("GET_RESERV_SENT_FAILED", t.toString()) }
                    override fun onResponse(call: Call<ReservSentGet>?, response: Response<ReservSentGet>?) {
                        val result = response?.body()?.result

                        boardList = mutableListOf()
                        for(element in result!!){
                            if(element.status == 49){
                                val image = element.room?.images?.get(0)
                                val studioName = element.room?.studio?.name
                                val roomName = element.room?.name
                                val startDate = element.started_at
                                val endDate = element.ended_at
                                val reservationID = element.id
                                val date = " " + startDate!!.substring(0,10)
                                val startTime = startDate.substring(11,16)
                                val endTime = endDate!!.substring(11,16)
                                val time = "$startTime ~ $endTime"
                                boardList.add(BoardMyReview1(image!!, studioName!!, roomName!!, date, time, reservationID!!))
                                val boardListAdapter = RecyclerAdapterMyReview1(this@FragmentMyReview1_WritableReview.requireContext(), boardList){
                                    val intent = Intent(context, PageMyReviewWrite::class.java)
                                    intent.putExtra("reservation_id", it.reservation_id)
                                    intent.putExtra("ref_token", refToken)
                                    startActivity(intent)
                                }
                                myReview1_listView.adapter = boardListAdapter
                            }
                        }
                    }
                })
            }
        })
    }

    override fun onRefresh() {
        init()
        myReview1_swipeRefreshLayout.isRefreshing = false
    }
}
