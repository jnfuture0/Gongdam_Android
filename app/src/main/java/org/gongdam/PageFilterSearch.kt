package org.gongdam

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_page_filter_search.*
import org.gongdam.Adapter.RecyclerAdapterFilterSearchLocation
import org.gongdam.Json.FilterListGet
import org.gongdam.Json.HotKeysGet
import org.gongdam.Struct.BoardOneString
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

class PageFilterSearch : AppCompatActivity() {

    val server = MainActivity.server
    var locationList = mutableListOf(BoardOneString("서울"),
        BoardOneString("경기"), BoardOneString("인천"), BoardOneString("강원"), BoardOneString("대전"), BoardOneString("세종"),
        BoardOneString("충정"), BoardOneString("부산"), BoardOneString("울산"), BoardOneString("경남"), BoardOneString("경북"),
        BoardOneString("대구"), BoardOneString("광주"), BoardOneString("전남"), BoardOneString("전북"), BoardOneString("제주"))
    var picTypeList : MutableList<BoardOneString> = mutableListOf()
    var facilitiesList : MutableList<BoardOneString> = mutableListOf()
    var moodList : MutableList<BoardOneString> = mutableListOf()
    var hotKeyList : MutableList<BoardOneString> = mutableListOf()
    /*필터 어뎁터에서 클릭 이벤트 주는거랑, 클릭 시 백그라운드 파랑색으로 바뀌고 흰글씨로 바뀌는거 처리해줘야함.*/

    companion object{
        var userLocationListSearch : MutableList<String> = mutableListOf()
        var userPicTypeList : MutableList<String> = mutableListOf()
        var userFacilitiesList: MutableList<String> = mutableListOf()
        var userMoodList: MutableList<String> = mutableListOf()
        var userHotKeyList : MutableList<String> = mutableListOf()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_filter_search)


        filter_search_page_location_layout_btn.setOnClickListener {
            showHideImageChange(filter_search_page_location_recyclerView_layout,filter_search_page_location_plus_minus_imageview)
        }
        filter_search_page_pic_type_layout_btn.setOnClickListener {
            showHideImageChange(filter_search_page_pic_type_recyclerView_layout, filter_search_page_pic_type_plus_minus_imageview)
        }
        filter_search_page_facilities_layout_btn.setOnClickListener {
            showHideImageChange(filter_search_page_facilities_recyclerView_layout, filter_search_page_facilities_plus_minus_imageview)
        }
        filter_search_page_mood_layout_btn.setOnClickListener {
            showHideImageChange(filter_search_page_mood_recyclerView_layout, filter_search_page_mood_plus_minus_imageview)
        }
        filter_search_page_keyword_layout_btn.setOnClickListener {
            showHideImageChange(filter_search_page_keyword_recyclerView_layout, filter_search_page_keyword_plus_minus_imageview)
        }

        val colorFilterWhite : Int = ContextCompat.getColor(this@PageFilterSearch, R.color.filter_white)
        var locationListAdapter = RecyclerAdapterFilterSearchLocation(this, locationList, userLocationListSearch){
            if( userLocationListSearch.contains(it.text.toString()) ){
                userLocationListSearch.remove(it.text.toString())
            } else{
                userLocationListSearch.add(it.text.toString())
            }

            if(it.currentTextColor == colorFilterWhite){
                it.setTextColor(resources.getColor(R.color.filter_black))
                it.setBackgroundResource(android.R.color.transparent)
            }else{
                it.setTextColor(resources.getColor(R.color.filter_white))
                it.setBackgroundResource(R.drawable.style_filter_background)
            }
        }
        filter_search_page_location_recyclerView.adapter = locationListAdapter
        filter_search_page_location_recyclerView.layoutManager = GridLayoutManager(this@PageFilterSearch, 8)

        /*촬영종류 시설 분위기*/
        server?.getFilterList()?.enqueue(object: Callback<FilterListGet> {
            override fun onFailure(call: Call<FilterListGet>?, t: Throwable?) {
                Log.e("GET_FILTER_LIST_FAILED", t.toString())}
            override fun onResponse(call: Call<FilterListGet>?, response: Response<FilterListGet>?) {
                var result = response?.body()?.result
                if(result!!.isNotEmpty()){
                    for(element in result){
                        when (element.category) {
                            "촬영종류" -> picTypeList.add(BoardOneString(element.name.toString()))
                            "시설" -> facilitiesList.add(BoardOneString(element.name.toString()))
                            "분위기" -> moodList.add(BoardOneString(element.name.toString()))
                        }
                        MainActivity.filterMap[element.name!!] = element.id.toString()+","
                    }
                    val picTypeAdapter = RecyclerAdapterFilterSearchLocation(this@PageFilterSearch, picTypeList, userPicTypeList){
                        if( userPicTypeList.contains(it.text.toString()) ){
                            userPicTypeList.remove(it.text.toString())
                        } else{
                            userPicTypeList.add(it.text.toString())
                        }

                        if(it.currentTextColor == colorFilterWhite){
                            it.setTextColor(resources.getColor(R.color.filter_black))
                            it.setBackgroundResource(android.R.color.transparent)
                        }else{
                            it.setTextColor(resources.getColor(R.color.filter_white))
                            it.setBackgroundResource(R.drawable.style_filter_background)
                        }
                    }
                    filter_search_page_pic_type_recyclerView.adapter = picTypeAdapter
                    filter_search_page_pic_type_recyclerView.layoutManager = GridLayoutManager(this@PageFilterSearch, 4)
                    val facilitiesAdapter = RecyclerAdapterFilterSearchLocation(this@PageFilterSearch, facilitiesList, userFacilitiesList){
                        if( userFacilitiesList.contains(it.text.toString()) ){
                            userFacilitiesList.remove(it.text.toString())
                        } else{
                            userFacilitiesList.add(it.text.toString())
                        }

                        if(it.currentTextColor == colorFilterWhite){
                            it.setTextColor(resources.getColor(R.color.filter_black))
                            it.setBackgroundResource(android.R.color.transparent)
                        }else{
                            it.setTextColor(resources.getColor(R.color.filter_white))
                            it.setBackgroundResource(R.drawable.style_filter_background)
                        }}
                    filter_search_page_facilities_recyclerView.adapter = facilitiesAdapter
                    filter_search_page_facilities_recyclerView.layoutManager = GridLayoutManager(this@PageFilterSearch, 4)
                    val moodAdapter = RecyclerAdapterFilterSearchLocation(this@PageFilterSearch, moodList, userMoodList){
                        if( userMoodList.contains(it.text.toString()) ){
                            userMoodList.remove(it.text.toString())
                        } else{
                            userMoodList.add(it.text.toString())
                        }

                        if(it.currentTextColor == colorFilterWhite){
                            it.setTextColor(resources.getColor(R.color.filter_black))
                            it.setBackgroundResource(android.R.color.transparent)
                        }else{
                            it.setTextColor(resources.getColor(R.color.filter_white))
                            it.setBackgroundResource(R.drawable.style_filter_background)
                        }}
                    filter_search_page_mood_recyclerView.adapter = moodAdapter
                    filter_search_page_mood_recyclerView.layoutManager = GridLayoutManager(this@PageFilterSearch, 4)
                }

            }
        })

        /*키워드*/
        server?.getHotKeys()?.enqueue(object: Callback<HotKeysGet> {
            override fun onFailure(call: Call<HotKeysGet>?, t: Throwable?) { Log.e("GET_HOT_KEYS_FAILED", t.toString()) }
            override fun onResponse(call: Call<HotKeysGet>?, response: Response<HotKeysGet>?) {
                var result = response?.body()?.result
                try {
                    for (element in result!!) {
                        hotKeyList.add(BoardOneString(element))
                    }
                }catch (e:NullPointerException){}
                val hotKeyAdapter = RecyclerAdapterFilterSearchLocation(this@PageFilterSearch, hotKeyList, userHotKeyList){
                    if( userHotKeyList.contains(it.text.toString()) ){
                        userHotKeyList.remove(it.text.toString())
                    } else{
                        userHotKeyList.add(it.text.toString())
                    }

                    if(it.currentTextColor == colorFilterWhite){
                        it.setTextColor(resources.getColor(R.color.filter_black))
                        it.setBackgroundResource(android.R.color.transparent)
                    }else{
                        it.setTextColor(resources.getColor(R.color.filter_white))
                        it.setBackgroundResource(R.drawable.style_filter_background)
                    }
                }
                filter_search_page_keyword_recyclerView.adapter = hotKeyAdapter
                filter_search_page_keyword_recyclerView.layoutManager = GridLayoutManager(this@PageFilterSearch, 4)
            }
        })


        /*새로고침 버튼*/
        filter_search_page_refresh_btn.setOnClickListener {
            userLocationListSearch = mutableListOf()
            userPicTypeList = mutableListOf()
            userFacilitiesList = mutableListOf()
            userMoodList = mutableListOf()
            userHotKeyList = mutableListOf()
            var locationListAdapter2 = RecyclerAdapterFilterSearchLocation(this, locationList, userLocationListSearch){
                if( userLocationListSearch.contains(it.text.toString()) ){
                    userLocationListSearch.remove(it.text.toString())
                } else{
                    userLocationListSearch.add(it.text.toString())
                }

                if(it.currentTextColor == colorFilterWhite){
                    it.setTextColor(resources.getColor(R.color.filter_black))
                    it.setBackgroundResource(android.R.color.transparent)
                }else{
                    it.setTextColor(resources.getColor(R.color.filter_white))
                    it.setBackgroundResource(R.drawable.style_filter_background)
                }
            }
            filter_search_page_location_recyclerView.adapter = locationListAdapter2
            val picTypeAdapter2 = RecyclerAdapterFilterSearchLocation(this@PageFilterSearch, picTypeList, userPicTypeList){
                if( userPicTypeList.contains(it.text.toString()) ){
                    userPicTypeList.remove(it.text.toString())
                } else{
                    userPicTypeList.add(it.text.toString())
                }

                if(it.currentTextColor == colorFilterWhite){
                    it.setTextColor(resources.getColor(R.color.filter_black))
                    it.setBackgroundResource(android.R.color.transparent)
                }else{
                    it.setTextColor(resources.getColor(R.color.filter_white))
                    it.setBackgroundResource(R.drawable.style_filter_background)
                }
            }
            filter_search_page_pic_type_recyclerView.adapter = picTypeAdapter2
            val facilitiesAdapter2 = RecyclerAdapterFilterSearchLocation(this@PageFilterSearch, facilitiesList, userFacilitiesList){
                if( userFacilitiesList.contains(it.text.toString()) ){
                    userFacilitiesList.remove(it.text.toString())
                } else{
                    userFacilitiesList.add(it.text.toString())
                }

                if(it.currentTextColor == colorFilterWhite){
                    it.setTextColor(resources.getColor(R.color.filter_black))
                    it.setBackgroundResource(android.R.color.transparent)
                }else{
                    it.setTextColor(resources.getColor(R.color.filter_white))
                    it.setBackgroundResource(R.drawable.style_filter_background)
                }}
            filter_search_page_facilities_recyclerView.adapter = facilitiesAdapter2
            val moodAdapter2 = RecyclerAdapterFilterSearchLocation(this@PageFilterSearch, moodList, userMoodList){
                if( userMoodList.contains(it.text.toString()) ){
                    userMoodList.remove(it.text.toString())
                } else{
                    userMoodList.add(it.text.toString())
                }

                if(it.currentTextColor == colorFilterWhite){
                    it.setTextColor(resources.getColor(R.color.filter_black))
                    it.setBackgroundResource(android.R.color.transparent)
                }else{
                    it.setTextColor(resources.getColor(R.color.filter_white))
                    it.setBackgroundResource(R.drawable.style_filter_background)
                }}
            filter_search_page_mood_recyclerView.adapter = moodAdapter2
            val hotKeyAdapter2 = RecyclerAdapterFilterSearchLocation(this@PageFilterSearch, hotKeyList, userHotKeyList){
                if( userHotKeyList.contains(it.text.toString()) ){
                    userHotKeyList.remove(it.text.toString())
                } else{
                    userHotKeyList.add(it.text.toString())
                }

                if(it.currentTextColor == colorFilterWhite){
                    it.setTextColor(resources.getColor(R.color.filter_black))
                    it.setBackgroundResource(android.R.color.transparent)
                }else{
                    it.setTextColor(resources.getColor(R.color.filter_white))
                    it.setBackgroundResource(R.drawable.style_filter_background)
                }}
            filter_search_page_keyword_recyclerView.adapter = hotKeyAdapter2

            toast("필터가 초기화되었습니다.")
        }

        /*적용하기 버튼*/
        filter_search_apply_btn.setOnClickListener {
            //toast("location : ${userLocationListSearch}\npic type : ${userPicTypeList}\nfacilities : ${userFacilitiesList}\nmood : ${userMoodList}\nhotkey : $userHotKeyList")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    fun changeFilterBackground(tv : TextView){
        if(tv.currentTextColor == android.R.color.black){
            tv.setTextColor(resources.getColor(android.R.color.white))
            tv.setBackgroundResource(R.drawable.style_filter_background)
        }else{
            tv.setTextColor(resources.getColor(android.R.color.black))
            tv.setBackgroundResource(android.R.color.transparent)
        }
    }


    fun showHideImageChange(view:View, imageView:ImageView){
        if(view.visibility == View.VISIBLE){
            view.visibility = View.GONE
            imageView.setImageResource(R.drawable.plus_icon_detail)
        } else{
            view.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.minus_icon)
        }
    }
}
