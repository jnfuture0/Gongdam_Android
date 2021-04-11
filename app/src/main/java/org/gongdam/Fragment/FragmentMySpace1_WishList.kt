package org.gongdam.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_fragment_my_space1.*
import kotlinx.android.synthetic.main.activity_page_room_equipment.*
import org.gongdam.*
import org.gongdam.Adapter.RecyclerAdapterMySpace
import org.gongdam.Json.AddFavorite
import org.gongdam.Json.FavoriteGet
import org.gongdam.Json.RefreshToken
import org.gongdam.Json.ResultBoolean
import org.gongdam.Struct.BoardMySpaceList
import org.gongdam.Struct.BoardSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

class FragmentMySpace1_WishList : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    var boardList:MutableList<BoardMySpaceList> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_fragment_my_space1, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun init(){
        val server = MainActivity.server
        var refToken = PageMySpace.refToken

        val tokenMap:HashMap<String, Any> = HashMap()
        tokenMap["refresh_token"] = refToken
        server?.refreshToken(tokenMap)?.enqueue(object: Callback<RefreshToken> {
            override fun onFailure(call: Call<RefreshToken>?, t: Throwable?) { Log.e("REFRESH_TOKEN_FAILED", t.toString())}
            override fun onResponse(call: Call<RefreshToken>?, response: Response<RefreshToken>?) {
                var token = response?.body()?.result?.access_token!!
                server?.getFavoriteByRoom(token)?.enqueue(object: Callback<FavoriteGet> {
                    override fun onFailure(call: Call<FavoriteGet>?, t: Throwable?) { Log.e("GET_FAVORITE_FAILED", t.toString()) }
                    override fun onResponse(call: Call<FavoriteGet>?, response: Response<FavoriteGet>?) {
                        boardList = mutableListOf()
                        try {
                            var result = response?.body()?.result!!
                            for (element in result.favorites) {
                                var eleRoom = element.room
                                val image = eleRoom?.images?.get(0)!!
                                val st_name = eleRoom.studio?.name!!
                                val room_name = eleRoom.name!!
                                val content = eleRoom.description!!
                                boardList.add(BoardMySpaceList(image,st_name,room_name,content,eleRoom.id!!))
                            }
                            var boardListAdapter = RecyclerAdapterMySpace(context!!, boardList, token){
                                val intent = Intent(context, PageRoom::class.java)
                                intent.putExtra("room_id", it.roomId)
                                intent.putExtra("ref_token",refToken)
                                startActivity(intent)
                            }
                            mySpace1_fragment_recycler_view.adapter = boardListAdapter
                        }catch (e:NullPointerException){}

                    }
                })
            }
        })
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        mySpace1_swipeRefreshLayout.setOnRefreshListener(this)


        mySpace1_fragment_recycler_view.layoutManager = LinearLayoutManager(context)

        /*mySpace1_fragment_recycler_view.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val item = adapterView.getItemAtPosition(i) as BoardSearch
            activity?.let{
                val intent = Intent(it, PageRoom::class.java)
                intent.putExtra("room_id", item.room_id)
                it.startActivity(intent)
            }
        }*/

    }

    //swipe refresh
    override fun onRefresh() {
        init()
        mySpace1_swipeRefreshLayout.isRefreshing = false
    }
}
