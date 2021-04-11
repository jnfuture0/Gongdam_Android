package org.gongdam.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_fragment_center_2.*
import kotlinx.android.synthetic.main.activity_page_room_equipment.*
import kotlinx.android.synthetic.main.frag_equipment_viewpager.*
import org.gongdam.Adapter.RecyclerAdapterRoomEquip
import org.gongdam.Json.RoomGet
import org.gongdam.MainActivity
import org.gongdam.PageLoginActivity
import org.gongdam.PageRoomEquipment
import org.gongdam.R
import org.gongdam.Struct.BoardRoomEquipment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentEquipment(val itemNum : Int) : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_equipment_viewpager, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    var equipList:MutableList<BoardRoomEquipment> = mutableListOf()

    private fun listFinish(list:MutableList<BoardRoomEquipment>){
        if(list.isEmpty()){
            list.add(BoardRoomEquipment("","","",2))
        }else{
            list.add(BoardRoomEquipment("","","",1))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val roomID = PageRoomEquipment.roomID
        val server = MainActivity.server

        frag_equipment_viewpager_recyclerView.layoutManager = LinearLayoutManager(context)
        server?.getRoomWithOutToken(roomID)?.enqueue(object: Callback<RoomGet> {
            override fun onFailure(call: Call<RoomGet>?, t: Throwable?) { Log.e("GET_ROOM_FAILED", t.toString()) }
            override fun onResponse(call: Call<RoomGet>?, response: Response<RoomGet>?) {
                val result = response?.body()?.result!!
                for(element in result.equipments!!){
                    if(element.id == itemNum)
                        equipList.add(BoardRoomEquipment(element.information!!, element.price?.amount?.toInt().toString(), element.price?.unit_hour.toString(), 0))
                }
                listFinish(equipList)

                var adapterEquipment = RecyclerAdapterRoomEquip(requireContext(), equipList)
                frag_equipment_viewpager_recyclerView.adapter = adapterEquipment
            }
        })



    }

}
