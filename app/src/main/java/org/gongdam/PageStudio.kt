package org.gongdam

import android.content.Intent
import android.content.SharedPreferences
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
import kotlinx.android.synthetic.main.activity_page_studio.*
import org.gongdam.Adapter.*
import org.gongdam.Json.*
import org.gongdam.Struct.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class PageStudio : AppCompatActivity() {

    internal lateinit var imageSliderViewPager: ViewPager
    var roomList : MutableList<Board3String1Int> = mutableListOf()
    var roomIDList : MutableList<Int> = mutableListOf()

    //var token = MainActivity.getToken(MainActivity.refToken)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_studio)

        val server = MainActivity.server
        val studioID= intent.getIntExtra("studio_id", 1)
        val refToken = intent.getStringExtra("ref_token")


        /*studio_page_see_all_how_to_use_btn.setOnClickListener {
            studio_page_how_to_use_recyclerView.visibility = View.VISIBLE
            studio_page_see_all_how_to_use_btn.visibility = View.GONE
        }*/

        /*studio_page_see_all_review_btn.setOnClickListener {
            val intent = Intent(this, PageReviewShow::class.java)
            intent.putExtra("studio_id", studioID)
            intent.putExtra("isRoom", false)
            startActivity(intent)
        }*/



        server?.getStudio(studioID)?.enqueue(object: Callback<StudioGet> {
            override fun onFailure(call: Call<StudioGet>?, t: Throwable?) { Log.e("GET_STUDIO_FAILED", t.toString()) }
            override fun onResponse(call: Call<StudioGet>?, response: Response<StudioGet>?) {
                val result = response?.body()?.result

                /*스튜디오 이미지 슬라이더*/
                var studioImageList:MutableList<String> = mutableListOf()
                for (i in 0 until result?.images?.size!!.toInt()){
                    val imageString = result?.images!![i] //"https://dev.gongdam.kr/images/" +
                    studioImageList.add(imageString)
                }
                imageSliderViewPager = findViewById<ViewPager>(R.id.studio_page_image_viewPager)
                val imageSliderNowNum = findViewById<TextView>(R.id.studio_page_image_slider_now_image_number)
                val imageSliderWholeNum = findViewById<TextView>(R.id.studio_page_image_slider_whole_image_number)
                val imageSliderAdapter = ImageSliderAdapter(this@PageStudio, studioImageList)
                imageSliderWholeNum.text = studioImageList.size.toString()
                val appearAnim = AnimationUtils.loadAnimation(applicationContext , R.anim.fade_in)
                val delayHandler = Handler()
                imageSliderViewPager.adapter = imageSliderAdapter
                imageSliderViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                    override fun onPageSelected(position: Int) {
                        imageSliderNowNum.text = (position+1).toString()
                        studio_page_image_slider_num_layout.visibility = View.VISIBLE
                        studio_page_image_slider_num_layout.animation = appearAnim
                        delayHandler.postDelayed({
                            //studio_page_image_slider_num_layout.animation = disappearAnim
                            studio_page_image_slider_num_layout.visibility = View.INVISIBLE
                        }, 1500)
                    }
                    override fun onPageScrolled(position: Int,positionOffset: Float, positionOffsetPixels: Int) {}
                    override fun onPageScrollStateChanged(state: Int) {}
                })

                studio_page_studio_name_textView.text = result.name
                studio_page_studio_name_title.text = result.name
                studio_page_studio_name_basic_info.text = result.name
                studio_page_location.text = result.address?.district
                studio_page_studio_detail_content.text = result.description
                var tagString :String = ""
                for (i in 0 until result?.rooms[0]?.tags?.size!!.toInt()){
                    tagString += result?.rooms[0]?.tags?.get(i).toString() + " "
                }
                studio_page_keyword_content.text = tagString

//                studio_page_how_to_use_room_name.text = result.rooms[0].name
//                studio_page_price_per_hour_TextV.text = (result.rooms[0].price?.per_hour!! / 10000).toString()
//                val defaultReservationMintue = result.rooms[0].price?.default_reservation_minute
//                studio_page_min_reservation_time_TextV.text = (defaultReservationMintue!! / 60 + defaultReservationMintue % 60).toString()
//                studio_page_min_people_TextV.text = result.rooms[0].price?.default_user_amount.toString()
//                val additionalPerPerson = result.rooms[0].price?.additional_per_person
//                studio_page_one_more_people_per_hour_TextV.text = (additionalPerPerson!! / 10000 + (additionalPerPerson % 10000) * 0.0001).toString()

                /*이용안내 recycler view 세팅*/
                /*if(result.rooms.size > 1){
                    var roomHowToUseList :MutableList<BoardHowToUse> = mutableListOf()
                    for(i in 1 until result.rooms.size){
                        val resultI = result.rooms[i]
                        val defaultReservationMintue2 = resultI.price?.default_reservation_minute
                        val additionalPerPerson2 = resultI.price?.additional_per_person
                        roomHowToUseList.add(
                            BoardHowToUse(
                                resultI.name!!,
                                (resultI.price?.per_hour!! / 10000).toString(),
                                (defaultReservationMintue2!! / 60 + defaultReservationMintue2 % 60).toString(),
                                resultI.price?.default_user_amount.toString(),
                                (additionalPerPerson2!! / 10000 + (additionalPerPerson2 % 10000) * 0.0001).toString()
                            ))
                    }
                    val howToUseAdapter = RecyclerAdapterStudioHowToUse(this@PageStudio, roomHowToUseList)
                    studio_page_how_to_use_recyclerView.adapter = howToUseAdapter
                    studio_page_how_to_use_recyclerView.layoutManager = LinearLayoutManager(this@PageStudio)
                }else{
                    studio_page_see_all_how_to_use_btn.visibility = View.GONE
                }*/



                /*스태프*/
                /*val staffList :List<Staff2> = result?.staffs!!
                var staffMutable :MutableList<BoardNameImg> = mutableListOf()
                if( staffList.isNotEmpty() ){
                    for(element in staffList){
                        staffMutable.add(BoardNameImg(element.name!!, element.image_url!!))
                    }
                    var staffAdapter = RecyclerAdapterRoomPageStaff(this@PageStudio, staffMutable)
                    studio_page_staff_recyclerView.adapter = staffAdapter
                    studio_page_staff_recyclerView.layoutManager = LinearLayoutManager(this@PageStudio, LinearLayoutManager.HORIZONTAL, false)
                }else{
                    studio_page_staff_layout.visibility = View.GONE
                }*/

                /*보유장비*/
                /*var equipmentIdList:MutableList<Int> = mutableListOf()
                var equipmentIdListTemp:MutableList<Int> = mutableListOf()
                for(element in result?.rooms){
                    for(element2 in element.equipments!!) {
                        if (!equipmentIdListTemp.contains(element2?.id!!)) {
                            equipmentIdListTemp.add(element2?.id!!)
                            equipmentIdList.add(element2?.id!!)
                        }
                    }
                }
                if(equipmentIdList.isNotEmpty()){
                    equipmentIdList.sort()
                    var equipmentListAdapter = RecyclerAdapterRoomPageEquip(this@PageStudio, equipmentIdList)
                    studio_page_equipment_recyclerView.adapter = equipmentListAdapter
                    studio_page_equipment_recyclerView.layoutManager = LinearLayoutManager(this@PageStudio, LinearLayoutManager.HORIZONTAL, false)
                }else{
                    studio_page_equipment_layout.visibility = View.GONE
                }*/
            }
        })

        val tokenMap:HashMap<String, Any> = HashMap()
        tokenMap["refresh_token"] = refToken
        MainActivity.server?.refreshToken(tokenMap)?.enqueue(object: Callback<RefreshToken> {
            override fun onFailure(call: Call<RefreshToken>?, t: Throwable?) { Log.e("REFRESH_TOKEN_FAILED", t.toString())}
            override fun onResponse(call: Call<RefreshToken>?, response: Response<RefreshToken>?) {
                var token = response?.body()?.result?.access_token!!
                /*룸 리스트 처리*/
                server?.getStudioGetRooms(studioID)?.enqueue(object:Callback<StudioGetRoomsGet>{
                    override fun onFailure(call: Call<StudioGetRoomsGet>?, t: Throwable?) { Log.e("GET_STUDIO_FAILED", t.toString()) }
                    override fun onResponse(call: Call<StudioGetRoomsGet>?, response: Response<StudioGetRoomsGet>?) {
                        val result = response?.body()?.result!!
                        for(element in result!!){
                            try {
                                roomList.add(Board3String1Int(element.images?.get(0)!!.toString(), element.name!!, element.description!!, element.id!!))
                            }catch (e:Exception){}
                        }
                        val roomListAdapter = RecyclerAdapterStudioPageRoomList(this@PageStudio, roomList, token){
                            val intent = Intent(applicationContext, PageRoom::class.java)
                            intent.putExtra("room_id", it.room_id)
                            intent.putExtra("ref_token",refToken)
                            startActivity(intent)
                        }
                        studio_page_room_list_recyclerView.adapter = roomListAdapter
                        studio_page_room_list_recyclerView.layoutManager = LinearLayoutManager(this@PageStudio)
                    }
                })
            }
        })


        /*server?.getStudioReviews(studioID)?.enqueue(object: Callback<StudioReviewsGet>{
            override fun onFailure(call: Call<StudioReviewsGet>?, t: Throwable?) {Log.e("GET_ROOM_REVIEW_FAILED", t.toString())}
            override fun onResponse(call: Call<StudioReviewsGet>?, response: Response<StudioReviewsGet>?) {
                var result = response?.body()?.result
                if( result?.total == 0){
                    studio_page_review_layout.visibility = View.GONE
                }else {
                    var resultReviews0 = result?.reviews?.get(0)
                    studio_page_review_name.text = resultReviews0?.user?.name
                    studio_page_review_content.text = resultReviews0?.content
                    studio_page_review_date.text = resultReviews0?.room?.created_at
                    studio_page_review_ratingbar.rating = resultReviews0?.star!!.toFloat()
                    //room_page_review_image.setImageURI(response?.body()?.result?.reviews?.get(0)?.images?.get(0)?.toUri())
                    val reviewImage = resultReviews0?.images!!.get(0)
                    Glide.with(this@PageStudio).load(reviewImage).into(studio_page_review_image)
                }
            }
        })*/
    }
}
