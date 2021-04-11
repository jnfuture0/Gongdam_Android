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
import kotlinx.android.synthetic.main.activity_fragment_my_space2.*
import org.gongdam.Adapter.RecyclerAdapterMySpace
import org.gongdam.Json.*
import org.gongdam.MainActivity
import org.gongdam.PageMySpace
import org.gongdam.PageRoom
import org.gongdam.R
import org.gongdam.Struct.BoardMySpaceList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

class FragmentMySpace2_RecentSpace : Fragment(), SwipeRefreshLayout.OnRefreshListener{

    var boardList:MutableList<BoardMySpaceList> = mutableListOf()
    val server = MainActivity.server
    var refToken = PageMySpace.refToken

    val tokenMap:HashMap<String, Any> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_fragment_my_space2, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun init(){
        tokenMap["refresh_token"] = refToken
        server?.refreshToken(tokenMap)?.enqueue(object: Callback<RefreshToken> {
            override fun onFailure(call: Call<RefreshToken>?, t: Throwable?) { Log.e("REFRESH_TOKEN_FAILED", t.toString())}
            override fun onResponse(call: Call<RefreshToken>?, response: Response<RefreshToken>?) {
                var token = response?.body()?.result?.access_token!!
                server?.getRecentRoom(token)?.enqueue(object: Callback<RecentRoomsGet> {
                    override fun onFailure(call: Call<RecentRoomsGet>?, t: Throwable?) { Log.e("GET_FAVORITE_FAILED", t.toString()) }
                    override fun onResponse(call: Call<RecentRoomsGet>?, response: Response<RecentRoomsGet>?) {
                        boardList = mutableListOf()
                        try {
                            val result = response?.body()?.result!!
                            for (element in result) {
                                val image = element?.images?.get(0)!!.toString()
                                val st_name = element.studio?.name!!
                                val room_name = element.name!!
                                val content = element.description!!
                                boardList.add(BoardMySpaceList(image,st_name,room_name,content,element.id!!))
                            }
                            val boardListAdapter = RecyclerAdapterMySpace(context!!, boardList, token){
                                val intent = Intent(context, PageRoom::class.java)
                                intent.putExtra("room_id", it.roomId)
                                intent.putExtra("ref_token",refToken)
                                startActivity(intent)
                            }
                            mySpace2_fragment_recyclerView.adapter = boardListAdapter
                        }catch (e: NullPointerException){}

                    }
                })
            }
        })
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()

        mySpace2_fragment_recyclerView.layoutManager = LinearLayoutManager(context)

        mySpace2_swipeRefreshLayout.setOnRefreshListener(this)
        /*mySpace1_fragment_recycler_view.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val item = adapterView.getItemAtPosition(i) as BoardSearch
            activity?.let{
                val intent = Intent(it, PageRoom::class.java)
                intent.putExtra("room_id", item.room_id)
                it.startActivity(intent)
            }
        }*/




    }

    override fun onRefresh() {
        init()
        mySpace2_swipeRefreshLayout.isRefreshing = false
    }

}
