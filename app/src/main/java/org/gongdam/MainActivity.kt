package org.gongdam

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_fragment_promotion.*
import kotlinx.android.synthetic.main.activity_fragment_search.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_page_reservation2.*
import kotlinx.android.synthetic.main.main_drawer_header_logged_in.*
import kotlinx.android.synthetic.main.main_drawer_header_logged_out.*
import kotlinx.android.synthetic.main.main_include_drawer.*
import org.gongdam.Adapter.ListAdapterPromotion
import org.gongdam.Adapter.ListAdapterSearch
import org.gongdam.Adapter.TabAdapterMain
import org.gongdam.Json.*
import org.gongdam.Payment.Payment
import org.gongdam.Struct.BoardPromotion
import org.gongdam.Struct.BoardSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    private val adapter by lazy{ TabAdapterMain(supportFragmentManager) }

    companion object{
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dev.gongdam.kr:443")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var server: RetrofitService? = retrofit.create(RetrofitService::class.java)

        lateinit var refToken : String
        var filterMap:HashMap<String,Any> = HashMap()

        val moneyFormatter:DecimalFormat = DecimalFormat("###,###")


        /*var tokenForGetToken = ""
        @JvmStatic fun getToken(ref:String) : String{
            val tokenMap:HashMap<String, Any> = HashMap()
            tokenMap["refresh_token"] = ref
            server?.refreshToken(tokenMap)?.enqueue(object: Callback<RefreshToken> {
                override fun onFailure(call: Call<RefreshToken>?, t: Throwable?) { Log.e("REFRESH_TOKEN_FAILED", t.toString())}
                override fun onResponse(call: Call<RefreshToken>?, response: Response<RefreshToken>?) {
                    MainActivity@tokenForGetToken = response?.body()?.result?.access_token!!
                }
            })
            return tokenForGetToken
        }*/

        //var tokenForCheckTokenIsReal :Boolean = false
        /*@JvmStatic fun checkTokenIsReal(ref:String) : Boolean{
            val tokenMap:HashMap<String, Any> = HashMap()
            tokenMap["refresh_token"] = ref
            server?.refreshToken(tokenMap)?.enqueue(object: Callback<RefreshToken> {
                override fun onFailure(call: Call<RefreshToken>?, t: Throwable?) { Log.e("REFRESH_TOKEN_FAILED", t.toString())}
                override fun onResponse(call: Call<RefreshToken>?, response: Response<RefreshToken>?) {
                    var token44 = response?.body()?.result?.access_token!!
                    server?.getUserFromToken(token44)?.enqueue(object: Callback<UserGetFromToken> {
                        override fun onFailure(call: Call<UserGetFromToken>?, t: Throwable?) { Log.e("GET_USER_FR_TOK_FAILED", t.toString())}
                        override fun onResponse(call: Call<UserGetFromToken>?, response: Response<UserGetFromToken>?) {
                            MainActivity@tokenForCheckTokenIsReal =
                                response?.body()?.result?.account != "admin"
                        }
                    })
                }
            })
            return tokenForCheckTokenIsReal
        }*/
    }

    val REQUEST_CODE_LOGIN = 100
    val REQUEST_FILTER_SEARCH = 200
    val REQUEST_FILTER_PROMOTION = 300
    var viewPagePosition = 0

    fun setFilterMap(){
        filterMap["서울"] = "0,"
        filterMap["경기"] = "1,"
        filterMap["인천"] = "21,22,23,"
        filterMap["강원"] = "24,25,26,"
        filterMap["대전"] = "34,35,"
        filterMap["세종"] = "30,"
        filterMap["충청"] = "27,28,29,31,32,33,"
        filterMap["부산"] = "46,47,48,49,"
        filterMap["울산"] = "44,"
        filterMap["경남"] = "50,51,52,53,"
        filterMap["경북"] = "36,37,38,39,40,"
        filterMap["대구"] = "41,42,"
        filterMap["광주"] = "61,62,"
        filterMap["전남"] = "57,58,59,60,"
        filterMap["전북"] = "54,55,56,"
        filterMap["제주"] = "63,"

        filterMap["렌탈"] = "52,"
        filterMap["베이비"] = "53,"
        filterMap["웨딩"] = "54,"
        filterMap["프로필"] = "55,"
        filterMap["증명"] = "56,"
        filterMap["엘레베이터"] = "57,"
        filterMap["주차"] = "58,"
        filterMap["반려동물"] = "59,"
        filterMap["드레스룸"] = "60,"
        filterMap["영상촬영"] = "61,"
        filterMap["호리존"] = "62,"
        filterMap["냉/난방"] = "63,"
        filterMap["메이크업룸"] = "64,"
        filterMap["비비드"] = "65,"
        filterMap["빈티지"] = "66,"
        filterMap["컬러풀"] = "67,"
        filterMap["모던"] = "68,"
        filterMap["럭셔리"] = "69,"
        filterMap["러블리"] = "70,"
        filterMap["엔틱"] = "71,"
        filterMap["내츄럴"] = "72,"


    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_include_drawer)

        

        //화면 회전 없앰
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setFilterMap()

        /*홈, 공담찾기, 공담특가, 고객센터*/

        vpMainActivity.adapter = adapter
        vpMainActivity.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> {
                        //main_top_right_imageView.setImageResource(R.drawable.icon_board_question2)
                        main_top_right_btn.visibility = View.GONE
                        main_tool_bar_center_image.visibility = View.VISIBLE
                        main_tool_bar_center_text.visibility = View.GONE
                        viewPagePosition = 0
                    }
                    1 -> {
                        main_top_right_btn.visibility = View.VISIBLE
                        main_tool_bar_center_image.visibility = View.GONE
                        main_tool_bar_center_text.visibility = View.VISIBLE
                        main_tool_bar_center_text.text = "공담찾기"
                        viewPagePosition = 1
                    }
                    2 -> {
                        main_top_right_btn.visibility = View.VISIBLE
                        main_tool_bar_center_image.visibility = View.GONE
                        main_tool_bar_center_text.visibility = View.VISIBLE
                        main_tool_bar_center_text.text = "공담특가"
                        viewPagePosition = 2
                    }
                    3 -> {
                        main_top_right_btn.visibility = View.GONE
                        main_tool_bar_center_image.visibility = View.GONE
                        main_tool_bar_center_text.visibility = View.VISIBLE
                        main_tool_bar_center_text.text = "고객센터"
                        viewPagePosition = 3
                    }
                }
            }
        })


        val pref : SharedPreferences = getSharedPreferences("prefs", 0)

        val keepToken = pref.getString("keep_token", "")
        if(keepToken != ""){
            pref.edit().putString("refresh_token", keepToken).apply()
            refToken =  keepToken!!
            logIn(keepToken)
        }else{
            refToken =  pref.getString("refresh_token", "")!!
        }





        tabLayout_main.setupWithViewPager(vpMainActivity)
        tabLayout_main.getTabAt(0)?.setCustomView(R.layout.tablayout_icon_home)
        tabLayout_main.getTabAt(1)?.setCustomView(R.layout.tablayout_icon_search)
        tabLayout_main.getTabAt(2)?.setCustomView(R.layout.tablayout_icon_sale)
        tabLayout_main.getTabAt(3)?.setCustomView(R.layout.tablayout_icon_center)

        setBtn()
        getHashKey()

        //결제완료 시
        if(Intent.ACTION_VIEW.equals(intent.action)){
            val uri = intent.data
            if(uri != null){
                val payName = uri.getQueryParameter("name")
                val payCost = uri.getQueryParameter("cost")
                if(payName != "") {
                    val payFinIntent = Intent(this, PagePayResult::class.java)
                    payFinIntent.putExtra("name", payName)
                    payFinIntent.putExtra("cost",payCost)
                    startActivity(payFinIntent)
                }
            }
        }

    }

    fun getHashKey(){
        var packageInfo : PackageInfo = PackageInfo()
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException){
            e.printStackTrace()
        }

        for (signature: Signature in packageInfo.signatures){
            try{
                var md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KEY_HASH", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch(e: NoSuchAlgorithmException){
                Log.e("KEY_HASH", "Unable to get MessageDigest. signature = " + signature, e)
            }
        }
    }

    private fun setBtn(){
        main_hamburger_btn.setOnClickListener {
            main_drawer_layout.openDrawer(GravityCompat.START)
        }

        main_top_right_btn.setOnClickListener {
            if(viewPagePosition == 1) {
                val intent = Intent(this, PageFilterSearch::class.java)
                startActivityForResult(intent, REQUEST_FILTER_SEARCH)
            }else if(viewPagePosition == 2){
                val intent = Intent(this, PageFilterPromotion::class.java)
                startActivityForResult(intent, REQUEST_FILTER_PROMOTION)
            }
        }

        main_drawer_layout_gongdam_logo_btn.setOnClickListener {
            val intent = Intent(this, Payment::class.java)
            intent.putExtra("amount", "100")
            intent.putExtra("payname", "dasf")
            startActivity(intent)
        }

        main_navigation_myInfo_btn.setOnClickListener {
            val intent = Intent(this, PageUserInfo::class.java)
            intent.putExtra("ref_token", refToken)
            startActivity(intent)
        }

        main_navigation_check_reservation_btn.setOnClickListener {
            val intent = Intent(this, PageReservationConfirm::class.java)
            intent.putExtra("ref_token", refToken)
            startActivity(intent)
        }

        main_navigation_myReview_btn.setOnClickListener {
            val intent = Intent(this, PageMyReview::class.java)
            intent.putExtra("ref_token", refToken)
            startActivity(intent)
        }

        main_navigation_mySpace_btn.setOnClickListener {
            val intent = Intent(this, PageMySpace::class.java)
            intent.putExtra("ref_token", refToken)
            startActivity(intent)
        }

        navigation_header_logout_btn.setOnClickListener {
            val pref : SharedPreferences = getSharedPreferences("prefs", 0)
            pref.edit().putString("keep_token", "").apply()
            pref.edit().putString("refresh_token", "").apply()
            logOut()
        }

        navigation_header_login_btn.setOnClickListener {
            val intent = Intent(this, PageLoginActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_LOGIN)
        }
    }


    private fun logIn(ref: String){

        main_header_include_logged_in.visibility = View.VISIBLE
        main_header_include_logged_out.visibility = View.INVISIBLE
        main_navigation_myInfo_btn.isEnabled = true
        main_navigation_myInfo_btn.setTextColor(Color.parseColor("#ffffff"))
        main_navigation_check_reservation_btn.isEnabled = true
        main_navigation_check_reservation_btn.setTextColor(Color.parseColor("#ffffff"))
        main_navigation_myReview_btn.isEnabled = true
        main_navigation_myReview_btn.setTextColor(Color.parseColor("#ffffff"))
        main_navigation_mySpace_btn.isEnabled = true
        main_navigation_mySpace_btn.setTextColor(Color.parseColor("#ffffff"))


        val tokenMap2:HashMap<String, Any> = HashMap()
        tokenMap2["refresh_token"] = ref
        server?.refreshToken(tokenMap2)?.enqueue(object: Callback<RefreshToken> {
            override fun onFailure(call: Call<RefreshToken>?, t: Throwable?) { Log.e("REFRESH_TOKEN_FAILED", t.toString())}
            override fun onResponse(call: Call<RefreshToken>?, response: Response<RefreshToken>?) {
                val getTokenNow = response?.body()?.result?.access_token!!
                server?.getUserFromToken(getTokenNow)?.enqueue(object: Callback<UserGetFromToken> {
                    override fun onFailure(call: Call<UserGetFromToken>?, t: Throwable?) { Log.e("GET_USER_FR_TOK_FAILED", t.toString())}
                    override fun onResponse(call: Call<UserGetFromToken>?, response: Response<UserGetFromToken>?) {
                        val result = response?.body()?.result
                        navigation_header_user_name_textView.text = "${result?.name} 님"
                        navigation_header_user_email_textView.text = result?.email
                    }
                })
            }
        })

    }

    fun logOut(){
        main_header_include_logged_in.visibility = View.INVISIBLE
        main_header_include_logged_out.visibility = View.VISIBLE
        main_navigation_myInfo_btn.isEnabled = false
        main_navigation_myInfo_btn.setTextColor(Color.parseColor("#777777"))
        main_navigation_check_reservation_btn.isEnabled = false
        main_navigation_check_reservation_btn.setTextColor(Color.parseColor("#777777"))
        main_navigation_myReview_btn.isEnabled = false
        main_navigation_myReview_btn.setTextColor(Color.parseColor("#777777"))
        main_navigation_mySpace_btn.isEnabled = false
        main_navigation_mySpace_btn.setTextColor(Color.parseColor("#777777"))
    }



    var isCanBackPressed = false
    override fun onBackPressed() {
        if(main_drawer_layout.isDrawerOpen(GravityCompat.START)){
            main_drawer_layout.closeDrawer(GravityCompat.START)
        }else if(viewPagePosition != 0){
            vpMainActivity.currentItem = 0
        }
        else {
            if(!isCanBackPressed){
                Toast.makeText(this,"한번 더 누르시면 종료됩니다.",Toast.LENGTH_LONG).show()
                isCanBackPressed = true
                Handler().postDelayed({
                    isCanBackPressed = false
                }, 2000)
            }else{
                super.onBackPressed()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_CODE_LOGIN -> {
                    val pref : SharedPreferences = getSharedPreferences("prefs", 0)
                    var refreshToken =  pref.getString("refresh_token", "")!!
                    refreshToken = data!!.getStringExtra("refresh_token")
                    refToken = refreshToken
                    logIn(refToken)
                }
                REQUEST_FILTER_SEARCH -> {
                    var postCodeStarts = ""
                    for(element in PageFilterSearch.userLocationListSearch){
                        if(MainActivity.filterMap[element] != null){
                            postCodeStarts += MainActivity.filterMap[element]
                        }
                    }
                    var filterIDs = ""
                    for(element in PageFilterSearch.userPicTypeList){
                        if(MainActivity.filterMap[element] != null){
                            filterIDs += MainActivity.filterMap[element]
                        }
                    }
                    for(element in PageFilterSearch.userFacilitiesList){
                        if(MainActivity.filterMap[element] != null){
                            filterIDs += MainActivity.filterMap[element]
                        }
                    }
                    for(element in PageFilterSearch.userMoodList){
                        if(MainActivity.filterMap[element] != null){
                            filterIDs += MainActivity.filterMap[element]
                        }
                    }
                    var boardList:MutableList<BoardSearch> = mutableListOf()
                    server?.getRoomListWithFilter(postCodeStarts,filterIDs)?.enqueue(object: Callback<RoomListGet> {
                        override fun onFailure(call: Call<RoomListGet>?, t: Throwable?) { Log.e("GET_ROOM_LIST_FAILED", t.toString()) }
                        override fun onResponse(call: Call<RoomListGet>?, response: Response<RoomListGet>?) {
                            var image = " "
                            var studio_name = " "
                            var room_name = " "
                            var location = " "
                            var content = ""
                            var premium_type = 1
                            var rating = 3f
                            var room_id = 0
                            var cost = 0
                            var result = response?.body()?.result

                            for( i in 0 until result?.size!!.toInt()){
                                var resultGetI = result?.get(i)
                                if(resultGetI?.images!!.isNotEmpty()) {
                                    image = resultGetI.images!![0].toString()
                                    image += ""
                                    //image = MainActivity.makeImageString(image)
                                }
                                studio_name = resultGetI?.studio?.name!!
                                room_name = resultGetI?.name!!
                                location = resultGetI?.studio?.address?.district!!
                                for (j in 0 until resultGetI?.tags?.size!!.toInt()){
                                    content += resultGetI?.tags?.get(j).toString() + " "
                                }
                                premium_type = resultGetI?.grade!!
                                rating = resultGetI?.star_average!!.toFloat()
                                room_id = resultGetI?.id!!
                                cost = resultGetI.price?.per_hour!!
                                //Log.e("CHECK_GET_ROOM_LIST", "$image, $studio_name, $room_name, $location, $content, $premium_type, $rating, $room_id")

                                boardList.add(BoardSearch(image, studio_name, room_name, location, content, premium_type, rating, room_id, cost))
                                image = " "
                                studio_name = " "
                                room_name = " "
                                location = " "
                                content = ""
                            }

                            var boardListAdapter = ListAdapterSearch(applicationContext, boardList)
                            search_page_listview.adapter = boardListAdapter
                        }
                    })
                }
                REQUEST_FILTER_PROMOTION -> {
                    var postCodeStarts = ""
                    for(element in PageFilterPromotion.userLocationList){
                        if(MainActivity.filterMap[element] != null){
                            postCodeStarts += MainActivity.filterMap[element]
                        }
                    }
                    var startAt = PageFilterPromotion.startAt
                    var endedAt = PageFilterPromotion.endedAt
                    var boardList:MutableList<BoardPromotion> = mutableListOf()
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
                            var startTime =""
                            var endTime = ""
                            var result = response?.body()?.result

                            for( i in 0 until result?.size!!.toInt()){
                                val resultGetRoom = result?.get(i).room
                                if(resultGetRoom?.images!!.isNotEmpty()) {
                                    image = resultGetRoom?.images!![0]
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
                                startTime = result.get(i).started_at!!
                                endTime = result.get(i).ended_at!!
                                discount_percent = ((1 - won_per_hour.toDouble() / won_per_hour_old.toDouble()) * 100.0).toInt()

                                boardList.add(BoardPromotion(image, studio_name, room_name, location, content, premium_type, rating, promotion_id, room_id, discount_percent.toString(), won_per_hour,won_per_hour_old, startTime, endTime))
                                image = " "
                                studio_name = " "
                                room_name = " "
                                location = " "
                                content = ""
                            }

                            var boardListAdapter = ListAdapterPromotion(applicationContext, boardList)
                            promotion_frag_page_listview.adapter = boardListAdapter
                        }
                    })
                }
            }
        }
    }



}
