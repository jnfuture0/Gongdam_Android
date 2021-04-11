package org.gongdam.Fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_fragment_home.*
import org.gongdam.*
import org.gongdam.Adapter.ImageSliderAdapter
import org.gongdam.Adapter.ImageSliderMainAdapter
import org.gongdam.Adapter.RecyclerAdapterHomeDiscount
import org.gongdam.Adapter.RecyclerAdapterHomePopular
import org.gongdam.Json.PromotionListGet
import org.gongdam.Json.RetrofitService
import org.gongdam.Json.RoomListGet
import org.gongdam.Struct.BoardHomeDiscount
import org.gongdam.Struct.BoardHomePopular
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.lang.NullPointerException

class FragmentHome : Fragment() {

    internal lateinit var imageSliderViewpager: ViewPager
    var discountList: MutableList<BoardHomeDiscount> = mutableListOf()
    var popularList: MutableList<BoardHomePopular> = mutableListOf()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://dev.gongdam.kr:443")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var server: RetrofitService? = retrofit.create(RetrofitService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_fragment_home, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        val refToken = MainActivity.refToken
        setRoomList(refToken)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        imageSliderViewpager = home_page_image_viewPager as ViewPager

        val refToken = MainActivity.refToken

        setImgSlider()
        setRoomList(refToken)
        setBtn()



        //SDK 23 이상
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            home_page_whole_scrollView.setOnScrollChangeListener(object:View.OnScrollChangeListener{
                override fun onScrollChange(p0: View?, x: Int, y: Int, oldx: Int, oldy: Int) {
                    // 위로 드래그 시 y > oldy
                    if (y > oldy + 50) {
                        scrollDownAnim()
                    } else if (y < oldy - 50) {
                        scrollUpAnim()
                    }
                    Log.e("CHECK_SCROLL_CHANGE", "$x $y $oldx $oldy")
                }
            })
        }*/
    }

    var upFlag = false
    var downFlag = false


    private fun scrollUpAnim(){
        val mContext = context as MainActivity
        val mToolBar = mContext.findViewById<ConstraintLayout>(R.id.main_tool_bar)
        val downAnim = AnimationUtils.loadAnimation(context, R.anim.translate_down)
        if(!upFlag){
            upFlag = true
            if(mToolBar.visibility != View.VISIBLE) {
                mToolBar.visibility = View.VISIBLE
                mToolBar.startAnimation(downAnim)
            }
            Handler().postDelayed({
                upFlag = false
            }, 1500)
        }
    }
    private fun scrollDownAnim(){
        val mContext = context as MainActivity
        val mToolBar = mContext.findViewById<ConstraintLayout>(R.id.main_tool_bar)
        val upAnim = AnimationUtils.loadAnimation(context, R.anim.translate_up)
        if(!downFlag){
            downFlag = true
            if(mToolBar.visibility != View.GONE) {
                mToolBar.startAnimation(upAnim)
                mToolBar.visibility = View.GONE
            }
            Handler().postDelayed({
                downFlag = false
            }, 1500)
        }
    }


    private fun setImgSlider(){
        val imageSliderNowNum = home_page_image_slider_now_image_number
        val imageSliderWholeNum = home_page_image_slider_whole_image_number
        val imageList = mutableListOf(R.drawable.main_img_1, R.drawable.main_img_2)
        val imgAdapter = ImageSliderMainAdapter(requireContext(), imageList)
        imageSliderWholeNum.text = imageList.size.toString()
        val appearAnim = AnimationUtils.loadAnimation(requireContext() , R.anim.fade_in)
        val delayHandler = Handler()
        imageSliderViewpager.adapter = imgAdapter
        imageSliderViewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageSelected(position: Int) {
                imageSliderNowNum.text = (position+1).toString()
                home_page_image_slider_num_layout.visibility = View.VISIBLE
                home_page_image_slider_num_layout.animation = appearAnim
                delayHandler.postDelayed({
                    //home_page_image_slider_num_layout.animation = disappearAnim
                    home_page_image_slider_num_layout.visibility = View.INVISIBLE
                }, 1500)
            }
            override fun onPageScrolled(position: Int,positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun setRoomList(refToken:String){
        /*promotion setting*/
        server?.getPromotionListSize(4)?.enqueue(object: Callback<PromotionListGet>{
            override fun onFailure(call: Call<PromotionListGet>?, t: Throwable?) { Log.e("GET_PROMOT_LIST_FAILED", t.toString()) }
            override fun onResponse(call: Call<PromotionListGet>?, response: Response<PromotionListGet>?) {
                val result = response?.body()?.result
                if(result!!.isNotEmpty()) {
                    var image = ""
                    var studioName = ""
                    var roomName = ""
                    var room_id = 0
                    var promotion_id = 0
                    var discount_percent = 0
                    var won_per_hour = 0
                    var won_per_hour_old = 0
                    for (element in result) {
                        image = element.room?.images!![0]
                        studioName = element.room?.studio?.name!!
                        roomName = element.room?.name!!
                        room_id = element.room?.id!!
                        promotion_id = element.id!!
                        won_per_hour = element.price?.per_hour!!
                        won_per_hour_old = element.room?.price?.per_hour!!
                        discount_percent = ((1 - won_per_hour.toDouble() / won_per_hour_old.toDouble()) * 100.0).toInt()
                        discountList.add(BoardHomeDiscount(image,discount_percent.toString(),studioName,roomName, promotion_id, room_id))
                    }
                }else{
                    home_page_discount_textView.visibility = View.GONE
                }
                try{
                    val discountAdapter = RecyclerAdapterHomeDiscount(requireContext(), discountList){
                        activity?.let{ it2 ->
                            val intent = Intent(it2, PagePromotion::class.java)
                            intent.putExtra("room_id", it.room_id)
                            intent.putExtra("promotion_id", it.promotion_id)
                            intent.putExtra("ref_token", refToken)
                            it2.startActivity(intent)
                        }
                    }

                    home_page_discount_recyclerView.adapter = discountAdapter
                    home_page_discount_recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
                    home_page_discount_recyclerView.canScrollHorizontally(0)
                    home_page_discount_recyclerView.canScrollVertically(0)
                }catch (e: Exception){}
                catch (e: NullPointerException){}
            }
        })
        /*popular setting*/
        server?.getRoomListSize(4)?.enqueue(object: Callback<RoomListGet>{
            override fun onFailure(call: Call<RoomListGet>?, t: Throwable?) { Log.e("GET_ROOM_LIST_FAILED", t.toString()) }
            override fun onResponse(call: Call<RoomListGet>?, response: Response<RoomListGet>?) {
                val result = response?.body()?.result
                if(result!!.isNotEmpty()) {
                    var image = ""
                    var studioName = ""
                    var roomName = ""
                    var room_id = 0
                    popularList = mutableListOf()
                    for (element in result) {
                        image = element.images!![0].toString()
                        studioName = element.studio?.name!!
                        roomName = element.name!!
                        room_id = element.id!!
                        popularList.add(BoardHomePopular(image,studioName,roomName, room_id))
                    }
                }else{
                    home_page_popular_textView.visibility = View.GONE
                }
                try{
                    val popularAdapter = RecyclerAdapterHomePopular(requireContext(), popularList){
                        activity?.let{ it2 ->
                            val intent = Intent(it2, PageRoom::class.java)
                            intent.putExtra("room_id", it.room_id)
                            intent.putExtra("ref_token", refToken)
                            it2.startActivity(intent)
                        }
                    }
                    home_page_popular_recyclerView.adapter = popularAdapter
                    home_page_popular_recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
                }catch (e: Exception){}
                catch (e: NullPointerException){}
            }
        })
    }

    private fun setBtn(){
        main_page_bottom_btn_1.setOnClickListener {
            activity?.let{
                val intent = Intent(it, JustInfo::class.java)
                it.startActivity(intent)
            }
        }

        main_page_bottom_btn_2.setOnClickListener {
            activity?.let{
                val intent = Intent(it, JustInfo2::class.java)
                it.startActivity(intent)
            }
        }
        main_page_bottom_btn_3.setOnClickListener {
            activity?.let{
                val intent = Intent(it, JustInfo3::class.java)
                it.startActivity(intent)
            }
        }
    }

}
