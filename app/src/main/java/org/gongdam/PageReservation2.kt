package org.gongdam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_page_reservation2.*
import org.gongdam.Adapter.ImageSliderAdapter
import org.gongdam.Adapter.RecyclerAdapterReservPageItem
import org.gongdam.Json.ExpectedPriceGet
import org.gongdam.Json.RoomGet
import org.gongdam.Payment.Payment
import org.gongdam.Struct.BoardNameCost
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat

class PageReservation2 : AppCompatActivity() {

    val server = MainActivity.server
    internal lateinit var imageSliderViewPager: ViewPager
    val moneyFormatter = MainActivity.moneyFormatter
    var addFaresMap :MutableMap<String,Any> = mutableMapOf()
    var addFaresSpinnerList : MutableList<String> = mutableListOf()
    var addFaresRecyclerList:MutableList<BoardNameCost> = mutableListOf()
    var addEquipMap :MutableMap<String,Any> = mutableMapOf()
    var addEquipSpinnerList : MutableList<String> = mutableListOf()
    var addEquipRecyclerList:MutableList<BoardNameCost> = mutableListOf()
    val iso8601Format : DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")

    var roomID = 0
    var additionalPerson = 0
    var equipmentIndexes :MutableList<Int> = mutableListOf()
    var additionalFareIndexes:MutableList<Int> = mutableListOf()
    var couponIds:MutableList<Int> = mutableListOf()
    var endedAt = ""
    var startedAt = ""
    var minPeople = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_reservation2)

        roomID= intent.getIntExtra("room_id", 0)
        minPeople = intent.getIntExtra("min_people", 0)
        val startTime = intent.getStringExtra("start_time")
        val endTime = intent.getStringExtra("end_time")
        val pickDate = intent.getStringExtra("user_pick_date")
        val stRoomName = intent.getStringExtra("st_ro_name")
        val userPickDate = iso8601Format.parse(pickDate)

        val monthString = if(userPickDate.month+1 < 10){
            "0${userPickDate.month+1}"
        }else{
            (userPickDate.month+1).toString()
        }
        val dateString = if(userPickDate.date < 10){
            "0${userPickDate.date}"
        }else{
            userPickDate.date.toString()
        }

        reservation_page2_reserv_date.text = "${userPickDate.year}년 ${monthString}월 ${dateString}일"
        reservation_page2_reserv_time.text = "${startTime} ~ ${endTime}"
        reservation_page2_studio_room_name.text = stRoomName

        startedAt = "${userPickDate.year}-${monthString}-${dateString}T${startTime}+09:00"
        endedAt = "${userPickDate.year}-${monthString}-${dateString}T${endTime}+09:00"
        getExpectedPrice()




        server?.getRoomWithOutToken(roomID)?.enqueue(object : Callback<RoomGet> {
            override fun onFailure(call: Call<RoomGet>?, t: Throwable?) { Log.e("GET_ROOM_FAILED", t.toString()) }
            override fun onResponse(call: Call<RoomGet>?, response: Response<RoomGet>?) {
                var result = response?.body()?.result
                /*룸 이미지 슬라이더*/
                var roomImageList: MutableList<String> = mutableListOf()
                for (i in 0 until result?.images?.size!!.toInt()) {
                    val imageString = result.images!![i].toString() //"https://dev.gongdam.kr/images/" +
                    roomImageList.add(imageString)
                }
                imageSliderViewPager =
                    findViewById(R.id.reservation_page2_image_viewPager)
                val imageSliderNowNum =
                    findViewById<TextView>(R.id.reservation_page2_image_slider_now_image_number)
                val imageSliderWholeNum =
                    findViewById<TextView>(R.id.reservation_page2_image_slider_whole_image_number)
                val imageSliderAdapter = ImageSliderAdapter(this@PageReservation2, roomImageList)
                imageSliderWholeNum.text = roomImageList.size.toString()
                val appearAnim = AnimationUtils.loadAnimation(applicationContext , R.anim.fade_in)
                val delayHandler = Handler()
                imageSliderViewPager.adapter = imageSliderAdapter
                imageSliderViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageSelected(position: Int) {
                        imageSliderNowNum.text = (position + 1).toString()
                        reservation_page2_image_slider_num_layout.visibility = View.VISIBLE
                        reservation_page2_image_slider_num_layout.animation = appearAnim
                        delayHandler.postDelayed({
                            //promotion_page_image_slider_num_layout.animation = disappearAnim
                            reservation_page2_image_slider_num_layout.visibility = View.INVISIBLE
                        }, 1500)
                    }
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                    override fun onPageScrollStateChanged(state: Int) {}
                })

                reservation_page2_reserv_people_count.text = result.price?.default_user_amount.toString()
                reservation_page2_additional_person_text.text = "※ 기본 입장 인원은 ${result.price?.default_user_amount}명 입니다."

                /*예약종류*/
                if (result.additional_fares!!.isNotEmpty()) {
                    for (element in result.additional_fares!!) {
                        addFaresSpinnerList.add(element.name.toString())
                        addFaresMap[element.name.toString()] = element.price?.amount!!.toInt()
                    }
                    val reservTypeSpinnerAdapter = object :
                        ArrayAdapter<String>(applicationContext, R.layout.form_spinner_text) {
                        override fun getView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val v = super.getView(position, convertView, parent)
                            if (position == count) {
                                (v.findViewById<View>(R.id.tvItemSpinner) as TextView).text = ""
                                (v.findViewById<View>(R.id.tvItemSpinner) as TextView).hint =
                                    getItem(count)
                            }
                            return v
                        }

                        override fun getCount(): Int {
                            return super.getCount() - 1
                        }
                    }
                    reservTypeSpinnerAdapter.addAll(addFaresSpinnerList)
                    reservTypeSpinnerAdapter.add("선택하세요.")
                    reservation_page2_reserv_type_spinner.adapter = reservTypeSpinnerAdapter
                    reservation_page2_reserv_type_spinner.setSelection(reservTypeSpinnerAdapter.count)
                    reservation_page2_reserv_type_spinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {}
                            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                                Log.e("CHECK_SPINNER", "1")
                                if (p2 != addFaresSpinnerList.size) {
                                    var name = addFaresSpinnerList[p2]
                                    Log.e("CHECK_SPINNER", "2, $name")
                                    var price = addFaresMap[name] as Int
                                    if (!addFaresRecyclerList.contains(
                                            BoardNameCost(name, price)
                                        )
                                    ) { //recycler view에 있는지 확인
                                        addFaresRecyclerList.add(BoardNameCost(name, price))
                                        Log.e("CHECK_SPINNER", "3, $price")
                                        var addFareAdapter = RecyclerAdapterReservPageItem(this@PageReservation2, addFaresRecyclerList)
                                        reservation_page2_reserv_type_recyclerView.adapter = addFareAdapter

                                        Toast.makeText(applicationContext, p2.toString(),Toast.LENGTH_SHORT).show()
                                        additionalFareIndexes.add(p2)
                                        getExpectedPrice()
                                    }
                                }
                            }
                        }
                    reservation_page2_reserv_type_recyclerView.layoutManager =
                        LinearLayoutManager(this@PageReservation2)
                } else {
                    reservation_page2_reserv_type_layout.visibility = View.GONE
                }


                /*장비추가*/
                if (result.equipments!!.isNotEmpty()) {
                    for (element in result.equipments!!) {
                        addEquipSpinnerList.add(element.information.toString())
                        addEquipMap[element.information.toString()] =
                            element.price?.amount!!.toInt()
                    }
                    val addEquipSpinnerAdapter = object :
                        ArrayAdapter<String>(applicationContext, R.layout.form_spinner_text) {
                        override fun getView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val v = super.getView(position, convertView, parent)
                            if (position == count) {
                                (v.findViewById<View>(R.id.tvItemSpinner) as TextView).text = ""
                                (v.findViewById<View>(R.id.tvItemSpinner) as TextView).hint =
                                    getItem(count)
                            }
                            return v
                        }

                        override fun getCount(): Int {
                            return super.getCount() - 1
                        }
                    }
                    addEquipSpinnerAdapter.addAll(addEquipSpinnerList)
                    addEquipSpinnerAdapter.add("선택하세요.")
                    reservation_page2_add_equip_spinner.adapter = addEquipSpinnerAdapter
                    reservation_page2_add_equip_spinner.setSelection(addEquipSpinnerAdapter.count)
                    reservation_page2_add_equip_spinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(p0: AdapterView<*>?) {}
                            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                                if (p2 != addEquipSpinnerList.size) {
                                    var name = addEquipSpinnerList[p2]
                                    var price = addEquipMap[name] as Int
                                    if (!addFaresRecyclerList.contains(
                                            BoardNameCost(name, price)
                                        )
                                    ) { //recycler view에 있는지 확인
                                        addEquipRecyclerList.add(BoardNameCost(name, price))
                                        var addEquipAdapter = RecyclerAdapterReservPageItem(
                                            this@PageReservation2,
                                            addEquipRecyclerList
                                        )
                                        reservation_page2_additional_equip_recyclerView.adapter = addEquipAdapter

                                        equipmentIndexes.add(p2)
                                        getExpectedPrice()
                                    }
                                }
                            }
                        }
                    reservation_page2_additional_equip_recyclerView.layoutManager =
                        LinearLayoutManager(this@PageReservation2)
                } else {
                    reservation_page2_add_equip_layout.visibility = View.GONE
                }

            }
        })


        /* 인원 - 버튼 */
        reservation_page2_reserv_people_minus_btn.setOnClickListener {
            var people = minPeople.toString()
            var userChoicePeopleCount = reservation_page2_reserv_people_count.text.toString()
            if(userChoicePeopleCount == "1"){
                toast("인원은 1명보다 적을 수 없습니다.")
            }else{
                var newPeople = Integer.parseInt(userChoicePeopleCount)
                newPeople -= 1
                reservation_page2_reserv_people_count.text = newPeople.toString()
                additionalPerson = newPeople - minPeople
                getExpectedPrice()
            }
        }
        /* 인원 + 버튼 */
        reservation_page2_reserv_people_plus_btn.setOnClickListener {
            var userChoicePeopleCount = reservation_page2_reserv_people_count.text.toString()
            var newPeople = Integer.parseInt(userChoicePeopleCount)
            newPeople += 1
            reservation_page2_reserv_people_count.text = newPeople.toString()
            additionalPerson = newPeople - minPeople
            getExpectedPrice()
        }


        //예약완료 버튼
        reservation_page2_make_reservation_btn.setOnClickListener {
            val intent = Intent(this, Payment::class.java)
            intent.putExtra("amount", "100")
            intent.putExtra("payname", reservation_page2_studio_room_name.text.toString())
            startActivity(intent)
        }
    }

    private fun getExpectedPrice(){
        val infoMap :HashMap<String, Any> = hashMapOf()
        infoMap["room_id"] = roomID
        infoMap["additional_person"] = additionalPerson
        infoMap["equipment_indexes"] = equipmentIndexes
        infoMap["additional_fare_indexes"] = additionalFareIndexes
        infoMap["coupon_ids"] = couponIds
        infoMap["started_at"] = startedAt
        infoMap["ended_at"] = endedAt

        Log.e("CHECK_EXPECT_PRICE", roomID.toString() + additionalPerson.toString() + equipmentIndexes.toString() + additionalFareIndexes.toString()+
        couponIds.toString()+startedAt+endedAt)

        server?.getExpectedPrice(infoMap)?.enqueue(object : Callback<ExpectedPriceGet> {
            override fun onFailure(call: Call<ExpectedPriceGet>?, t: Throwable?) { Log.e("GET_PRICE_FAILED", t.toString()) }
            override fun onResponse(call: Call<ExpectedPriceGet>?, response: Response<ExpectedPriceGet>?) {
                var result = response?.body()?.result!!
                var totalPrice = 0
                val rPrice = result.room_price!!
                val adPrice = result.additional_person_price!!
                var fPrice = 0
                for(element in result.require_additional_fares!!){
                    fPrice += element.price?.amount?.toInt()!!
                }
                var ePrice = 0
                for(element in result.require_equipments!!){
                    ePrice += element.price?.amount?.toInt()!!
                }
                totalPrice = rPrice + adPrice + fPrice + ePrice
                val money = moneyFormatter.format(totalPrice)
                reservation_page2_user_check_cost.text = "+ ${money}원"
            }
        })
    }
}
