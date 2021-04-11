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
import kotlinx.android.synthetic.main.activity_fragment_search.*
import org.gongdam.*
import org.gongdam.Adapter.ListAdapterSearch
import org.gongdam.Json.RoomListGet
import org.gongdam.Struct.BoardPromotion
import org.gongdam.Struct.BoardSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.awt.font.NumericShaper

class FragmentSearch : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    var boardList:MutableList<BoardSearch> = mutableListOf()

    val server = MainActivity.server
    fun sortSelectorPremiumType(p: BoardSearch) : Int = p.type_0_premium_1_normal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_fragment_search, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var refToken = MainActivity.refToken

        init()

        search_page_swipeRefreshLayout.setOnRefreshListener(this)

        /*클릭하는 부분*/
        search_page_listview.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val item = adapterView.getItemAtPosition(i) as BoardSearch
            activity?.let{
                val intent = Intent(it, PageRoom::class.java)
                intent.putExtra("room_id", item.room_id)
                intent.putExtra("ref_token", refToken)
                it.startActivity(intent)
            }
        }

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
        search_page_swipeRefreshLayout.isRefreshing = false
    }

    private fun init(){
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
                boardList = mutableListOf()
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

                boardList.sortByDescending { sortSelectorPremiumType(it) }
                var boardListAdapter = ListAdapterSearch(this@FragmentSearch.requireContext(), boardList)
                search_page_listview.adapter = boardListAdapter
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
