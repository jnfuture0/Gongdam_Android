package org.gongdam.Fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_fragment_review2.*
import org.gongdam.*
import org.gongdam.Adapter.RecyclerAdapterReviewShow
import org.gongdam.Json.*
import org.gongdam.Struct.BoardReviewShow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentReviewShow2_OnlyPicture : Fragment() {


    //var token = MainActivity.getToken(MainActivity.refToken)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    var boardList :MutableList<BoardReviewShow> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_fragment_review2, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val server = MainActivity.server
        val roomID = PageReviewShow.room_id
        val studioID = PageReviewShow.studio_id
        val isRoom = PageReviewShow.isRoom
        var ratingAvg = 0f


        if(isRoom) { //룸 리뷰들
            server?.getRoomReview(roomID)?.enqueue(object : Callback<RoomReviewGet> {
                override fun onFailure(call: Call<RoomReviewGet>?, t: Throwable?) {
                    Log.e("GET_ROOM_REVIEW_FAILED", t.toString())
                }

                override fun onResponse(
                    call: Call<RoomReviewGet>?,
                    response: Response<RoomReviewGet>?
                ) {
                    var result = response?.body()?.result?.reviews!!
                    var resultNum = response?.body()?.result?.total!!
                    var isPicture: Boolean
                    var image: String
                    var userName: String
                    var rating: Int
                    var date: String
                    var content: String
                    for (element in result) {
                        if (element.images?.isNotEmpty()!!) {
                            isPicture = true
                            image = element.images!![0]
                            if (image == "") {
                                resultNum -= 1
                                continue
                            }
                        } else {
                            resultNum -= 1
                            continue
                        }
                        userName = element.user?.name!!
                        rating = element.star!!
                        date = element.created_at!!.substring(0, 10)
                        content = element.content!!
                        ratingAvg += rating.toFloat()
                        boardList.add(
                            BoardReviewShow(
                                image,
                                userName,
                                rating.toFloat(),
                                date,
                                content,
                                isPicture
                            )
                        )
                    }
                    var boardListAdapter = RecyclerAdapterReviewShow(context!!, boardList)
                    review2_show_page_recycler_view.adapter = boardListAdapter

                    if (resultNum != 0) {
                        ratingAvg /= resultNum!!
                        review2_show_page_rating.rating = ratingAvg
                        review2_show_page_rating_text.text = String.format("%.2f", ratingAvg)
                    }
                }
            })
            review2_show_page_recycler_view.layoutManager =
                LinearLayoutManager(this@FragmentReviewShow2_OnlyPicture.requireContext())
        }else{ //스튜디오 리뷰
            server?.getStudioReviews(studioID)?.enqueue(object : Callback<StudioReviewsGet> {
                override fun onFailure(call: Call<StudioReviewsGet>?, t: Throwable?) { Log.e("GET_STUDI_REVIEW_FAILED", t.toString()) }
                override fun onResponse(call: Call<StudioReviewsGet>?, response: Response<StudioReviewsGet>?) {
                    var result = response?.body()?.result?.reviews!!
                    var resultNum = response?.body()?.result?.total!!
                    var isPicture: Boolean
                    var image: String
                    var userName: String
                    var rating: Int
                    var date: String
                    var content: String
                    for (element in result) {
                        if (element.images?.isNotEmpty()!!) {
                            isPicture = true
                            image = element.images!![0]
                            if (image == "") {
                                resultNum -= 1
                                continue
                            }
                        } else {
                            resultNum -= 1
                            continue
                        }
                        userName = element.user?.name!!
                        rating = element.star!!
                        date = element.created_at!!.substring(0, 10)
                        content = element.content!!
                        ratingAvg += rating.toFloat()
                        boardList.add(
                            BoardReviewShow(
                                image,
                                userName,
                                rating.toFloat(),
                                date,
                                content,
                                isPicture
                            )
                        )
                    }
                    var boardListAdapter = RecyclerAdapterReviewShow(context!!, boardList)
                    review2_show_page_recycler_view.adapter = boardListAdapter

                    if (resultNum != 0) {
                        ratingAvg /= resultNum!!
                        review2_show_page_rating.rating = ratingAvg
                        review2_show_page_rating_text.text = String.format("%.2f", ratingAvg)
                    }
                }
            })
            review2_show_page_recycler_view.layoutManager =
                LinearLayoutManager(this@FragmentReviewShow2_OnlyPicture.requireContext())
        }



        review2_show_page_rating_first_btn.setOnClickListener {
            review2_show_page_rating_first_btn.setTextColor(Color.parseColor("#303030"))
            review2_show_page_recent_first_btn.setTextColor(Color.parseColor("#8d8d8d"))
        }

        review2_show_page_recent_first_btn.setOnClickListener {
            review2_show_page_recent_first_btn.setTextColor(Color.parseColor("#303030"))
            review2_show_page_rating_first_btn.setTextColor(Color.parseColor("#8d8d8d"))
        }





    }

}
