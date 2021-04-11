package org.gongdam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_page_room.*
import org.gongdam.Adapter.*
import org.gongdam.Json.*
//import org.gongdam.Json.ImageGet
import org.gongdam.Struct.BoardNameCost
import org.gongdam.Struct.BoardNameImg
import org.gongdam.Struct.BoardOneString
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.HashMap

class PageRoom : AppCompatActivity() {


    //var token = MainActivity.getToken(MainActivity.refToken)
    var boardList:MutableList<BoardOneString> = mutableListOf()

    internal lateinit var imageSliderViewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_room)

        val server = MainActivity.server


        val roomID= intent.getIntExtra("room_id", 1)
        val refToken = intent.getStringExtra("ref_token")
        var studioID = 0

        var equipmentIdList:MutableList<Int> = mutableListOf()

        val tokenMap:HashMap<String, Any> = HashMap()
        tokenMap["refresh_token"] = refToken
        server?.refreshToken(tokenMap)?.enqueue(object: Callback<RefreshToken> {
            override fun onFailure(call: Call<RefreshToken>?, t: Throwable?) { Log.e("REFRESH_TOKEN_FAILED", t.toString())}
            override fun onResponse(call: Call<RefreshToken>?, response: Response<RefreshToken>?) {
                val token = response?.body()?.result?.access_token!!
                server.getRoomWithToken(roomID,token)?.enqueue(object: Callback<RoomGet>{
                    override fun onFailure(call: Call<RoomGet>?, t: Throwable?) { Log.e("GET_ROOM_FAILED", t.toString())}
                    override fun onResponse(call: Call<RoomGet>?, response: Response<RoomGet>?) {
                        var result = response?.body()?.result

                        /*룸 이미지 슬라이더*/
                        var roomImageList:MutableList<String> = mutableListOf()
                        for (i in 0 until result?.images?.size!!.toInt()){
                            val imageString = result?.images!![i].toString() //"https://dev.gongdam.kr/images/" +
                            roomImageList.add(imageString)
                        }
                        imageSliderViewPager = findViewById<ViewPager>(R.id.room_page_image_viewPager)
                        val imageSliderNowNum = findViewById<TextView>(R.id.room_page_image_slider_now_image_number)
                        val imageSliderWholeNum = findViewById<TextView>(R.id.room_page_image_slider_whole_image_number)
                        val imageSliderAdapter = ImageSliderAdapter(this@PageRoom, roomImageList)
                        imageSliderWholeNum.text = roomImageList.size.toString()
                        val appearAnim = AnimationUtils.loadAnimation(applicationContext , R.anim.fade_in)
                        val delayHandler = Handler()
                        imageSliderViewPager.adapter = imageSliderAdapter
                        imageSliderViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                            override fun onPageSelected(position: Int) {
                                imageSliderNowNum.text = (position+1).toString()
                                room_page_image_slider_num_layout.visibility = View.VISIBLE
                                room_page_image_slider_num_layout.animation = appearAnim
                                delayHandler.postDelayed({
                                    //room_page_image_slider_num_layout.animation = disappearAnim
                                    room_page_image_slider_num_layout.visibility = View.INVISIBLE
                                }, 1500)
                            }
                            override fun onPageScrolled(position: Int,positionOffset: Float, positionOffsetPixels: Int) {}
                            override fun onPageScrollStateChanged(state: Int) {}
                        })

                        /*룸이름, 스튜디오이름, 룸설명, 평점, 후기수, 위치, 키워드, */
                        room_page_room_name_title.text = result?.name
                        room_page_room_name.text = result?.name
                        room_page_studio_name.text = result?.studio?.name
                        room_page_room_detail_content.text = result?.description
                        room_page_ratingBar.rating = result?.star_average!!.toFloat()
                        room_page_review_num.text = "/ 후기 (${result?.review_total})"
                        if(result.review_total != 0){
                            room_page_review_num.setOnClickListener {
                                val intent = Intent(applicationContext, PageReviewShow::class.java)
                                intent.putExtra("room_id", roomID)
                                intent.putExtra("isRoom", true)
                                startActivity(intent)
                            }
                        }
                        room_page_location.text = result?.studio?.address?.district /*detail은 000-000 식으로 나오는데 이거 변환하는거 찾아봐야함*/
                        var tagString :String = ""
                        for (i in 0 until result?.tags?.size!!.toInt()){
                            tagString += result?.tags?.get(i).toString() + " "
                        }
                        room_page_keyword_content.text = tagString
                        room_page_studio_name2.text = result?.studio?.name

                        /*이용안내*/
                        val perHour = result?.price?.per_hour
                        room_page_price_per_hour_TextV.text = (perHour!! / 10000).toString()
                        val defaultReservationMintue = result?.price?.default_reservation_minute
                        room_page_min_reservation_time_TextV.text = (defaultReservationMintue!! / 60 + defaultReservationMintue % 60).toString()
                        room_page_min_people_TextV.text = result?.price?.default_user_amount.toString()
                        val additionalPerPerson = result?.price?.additional_per_person
                        room_page_one_more_people_per_hour_TextV.text = (additionalPerPerson!! / 10000 + (additionalPerPerson % 10000) * 0.0001).toString()


                        /*추가요금*/
                        var addFareList:MutableList<BoardNameCost> = mutableListOf()
                        for(i in 0 until result?.additional_fares?.size!!.toInt()){
                            val name = result?.additional_fares?.get(i)?.name!!
                            val fare = result?.additional_fares?.get(i)?.price?.amount?.toInt()
                            addFareList.add(BoardNameCost(name, fare!!))
                        }
                        if(addFareList.size == 0){
                            room_page_additional_fares_layout.visibility = View.GONE
                        }else {
                            val addFareListAdapter = RecyclerAdapterRoomPageAddFares(this@PageRoom, addFareList)
                            room_page_additional_fares_recyclerView.adapter = addFareListAdapter
                            room_page_additional_fares_recyclerView.layoutManager = LinearLayoutManager(this@PageRoom)
                        }



                        /*보유장비*/
                        if(result?.equipments!!.isNotEmpty()){
                            var equipmentIdListTemp:MutableList<Int> = mutableListOf()
                            for(element in result?.equipments!!){
                                if(!equipmentIdListTemp.contains(element?.id!!)) {
                                    equipmentIdListTemp.add(element?.id!!)
                                    equipmentIdList.add(element.id!!)
                                }
                            }
                            equipmentIdList.sort()
                            var equipmentListAdapter = RecyclerAdapterRoomPageEquip(this@PageRoom, equipmentIdList)
                            room_page_equipment_recyclerView.adapter = equipmentListAdapter
                            room_page_equipment_recyclerView.layoutManager = LinearLayoutManager(this@PageRoom, LinearLayoutManager.HORIZONTAL, false)
                        }else{
                            room_page_equipment_layout.visibility = View.GONE
                            room_page_no_equipment_layout.visibility = View.VISIBLE
                        }



                        studioID = result?.studio?.id!!


                        /*스태프*/
                        val staffList = result?.studio?.staffs!!
                        var staffMutable :MutableList<BoardNameImg> = mutableListOf()
                        if( staffList.isNotEmpty() ){
                            for(element in staffList){
                                staffMutable.add(BoardNameImg(element.name!!, element.image_url!!))
                            }
                            var staffAdapter = RecyclerAdapterRoomPageStaff(this@PageRoom, staffMutable)
                            room_page_staff_recyclerView.adapter = staffAdapter
                            room_page_staff_recyclerView.layoutManager = LinearLayoutManager(this@PageRoom, LinearLayoutManager.HORIZONTAL, false)
                        }else{
                            room_page_staff_recyclerView.visibility = View.GONE
                            room_page_no_staff_layout.visibility = View.VISIBLE
                        }
                    } /*getRoom onResponse*/
                })
                server.getFavoriteByRoomWithQuery(token, roomID).enqueue(object : Callback<FavoriteGet> {
                    override fun onFailure(call: Call<FavoriteGet>?, t: Throwable?) { Log.e("GET_FAVORITE_FAILED", t.toString()) }
                    override fun onResponse(call: Call<FavoriteGet>?, response: Response<FavoriteGet>?) {
                        val result = response?.body()?.result!!
                        if (result.total == 1) {
                            room_page_heart_on_btn.visibility = View.VISIBLE
                            room_page_heart_off_btn.visibility = View.INVISIBLE
                        }
                    }
                })
            }
        })

        room_page_share_btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            intent.putExtra(Intent.EXTRA_TEXT, "url. room_id = ${roomID}")

            val chooser = Intent.createChooser(intent, "공유하기")
            startActivity(chooser)
        }
        room_page_heart_off_btn.setOnClickListener {
            val tokenMap:HashMap<String, Any> = HashMap()
            tokenMap["refresh_token"] = refToken
            server?.refreshToken(tokenMap)?.enqueue(object: Callback<RefreshToken> {
                override fun onFailure(call: Call<RefreshToken>?, t: Throwable?) { Log.e("REFRESH_TOKEN_FAILED", t.toString()) }
                override fun onResponse(call: Call<RefreshToken>?, response: Response<RefreshToken>?) {
                    val token = response?.body()?.result?.access_token!!
                    server.postAddFavorite(token, roomID).enqueue(object : Callback<AddFavorite> {
                        override fun onFailure(call: Call<AddFavorite>?, t: Throwable?) { Log.e("ADD_FAVORITE_FAILED", t.toString()) }
                        override fun onResponse(call: Call<AddFavorite>?, response: Response<AddFavorite>?) {
                            room_page_heart_off_btn.visibility = View.INVISIBLE
                            room_page_heart_on_btn.visibility = View.VISIBLE
                        }
                    })
                }
            })
        }
        room_page_heart_on_btn.setOnClickListener {
            val tokenMap:HashMap<String, Any> = HashMap()
            tokenMap["refresh_token"] = refToken
            server?.refreshToken(tokenMap)?.enqueue(object: Callback<RefreshToken> {
                override fun onFailure(call: Call<RefreshToken>?, t: Throwable?) { Log.e("REFRESH_TOKEN_FAILED", t.toString()) }
                override fun onResponse(call: Call<RefreshToken>?, response: Response<RefreshToken>?) {
                    val token = response?.body()?.result?.access_token!!
                    server.deleteFavorite(token, roomID).enqueue(object : Callback<ResultBoolean> {
                        override fun onFailure(call: Call<ResultBoolean>?, t: Throwable?) { Log.e("ADD_FAVORITE_FAILED", t.toString()) }
                        override fun onResponse(call: Call<ResultBoolean>?, response: Response<ResultBoolean>?) {
                            room_page_heart_off_btn.visibility = View.VISIBLE
                            room_page_heart_on_btn.visibility = View.INVISIBLE
                        }
                    })
                }
            })
        }


        /*후기*/
        server?.getRoomReview(roomID)?.enqueue(object: Callback<RoomReviewGet>{
            override fun onFailure(call: Call<RoomReviewGet>?, t: Throwable?) {Log.e("GET_ROOM_REVIEW_FAILED", t.toString())}
            override fun onResponse(call: Call<RoomReviewGet>?, response: Response<RoomReviewGet>?) {
                var result = response?.body()?.result
                if( result?.total == 0){
                    room_page_review_layout.visibility = View.GONE
                    room_page_no_review_layout.visibility = View.VISIBLE
                }else {
                    var resultReviews0 = result?.reviews?.get(0)
                    room_page_review_name.text = resultReviews0?.user?.name
                    room_page_review_content.text = resultReviews0?.content
                    room_page_review_date.text = resultReviews0?.room?.created_at?.substring(0,10)
                    room_page_review_ratingbar.rating = resultReviews0?.star!!.toFloat()
                    //room_page_review_image.setImageURI(response?.body()?.result?.reviews?.get(0)?.images?.get(0)?.toUri())
                    val reviewImage = resultReviews0?.images!!.get(0)
                    Glide.with(this@PageRoom).load(reviewImage).into(room_page_review_image)
                }
            }
        })


        room_page_see_all_equipment_btn.setOnClickListener {
            val intent = Intent(this, PageRoomEquipment::class.java)
            intent.putExtra("room_id", roomID)
            startActivity(intent)
        }

        room_page_go_to_other_space_layout.setOnClickListener {
            val intent = Intent(this, PageStudio::class.java)
            intent.putExtra("studio_id", studioID)
            intent.putExtra("ref_token", refToken)
            startActivity(intent)
        }

        room_page_see_all_review_btn.setOnClickListener {
            val intent = Intent(this, PageReviewShow::class.java)
            intent.putExtra("room_id", roomID)
            intent.putExtra("isRoom", true)
            startActivity(intent)
        }

        room_page_make_reservation_btn.setOnClickListener {
            val intent = Intent(this, PageReservation::class.java)
            intent.putExtra("room_id", roomID)
            intent.putExtra("isRoom", true)
            startActivity(intent)
        }


    }
}
