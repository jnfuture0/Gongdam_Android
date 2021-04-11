package org.gongdam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_page_reservation.*
import org.gongdam.Adapter.ImageSliderAdapter
import org.gongdam.Adapter.RecyclerAdapterTimeList
import org.gongdam.Calendar.*
import org.gongdam.Json.PromotionListGet
import org.gongdam.Json.RoomGet
import org.gongdam.Struct.BoardTimeList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PageReservation : AppCompatActivity() {

    //var token = MainActivity.getToken(MainActivity.refToken)
    val moneyFormatter = MainActivity.moneyFormatter
    val server = MainActivity.server
    internal lateinit var imageSliderViewPager: ViewPager
    lateinit var timeListAdapter:RecyclerAdapterTimeList
    //date까지만
    var startTime = ""
    var endTime = ""
    //최종
    var dateString = ""
    var startTimeFinal = ""
    var endTimeFinal = ""
    var userCheckCost = 0
    var startTimeCalendar : Calendar = Calendar.getInstance()
    var endTimeCalendar : Calendar = Calendar.getInstance()
    var userPickDate:Date = Date()
    val dateFormat : DateFormat = SimpleDateFormat("yyyy-MM-dd")
    val dateTimeFormat : DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val iso8601Format :DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
    var timeList:MutableList<BoardTimeList> = mutableListOf()

    private fun setTimeList(roomID : Int, startTime:String, endTime:String){
        server?.getRoomWithOutToken(roomID)?.enqueue(object : Callback<RoomGet> {
            override fun onFailure(call: Call<RoomGet>?, t: Throwable?) { Log.e("GET_ROOM_FAILED", t.toString()) }
            override fun onResponse(call: Call<RoomGet>?, response: Response<RoomGet>?) {
                var result = response?.body()?.result
                var cost = result?.price?.per_hour!!
                server.getPromotionListWithTimeRoomID(roomID, startTime, endTime)?.enqueue(object:Callback<PromotionListGet>{
                    override fun onFailure(call: Call<PromotionListGet>, t: Throwable) {}
                    override fun onResponse(call: Call<PromotionListGet>, response2: Response<PromotionListGet>) {
                        var result2 = response2.body()?.result!!
                        timeList = mutableListOf()
                        for(i in 0..8) {
                            timeList.add(BoardTimeList(cost, 0, "0$i:00", "0${i+1}:00", false, true, 0))
                        }
                        timeList.add(BoardTimeList(cost,0, "09:00", "10:00", false, true, 0))
                        for(i in 10..23) {
                            timeList.add(BoardTimeList(cost, 0, "$i:00", "${i+1}:00", false, true, 0))
                        }

                        if(result2.isNotEmpty()) {
                            var promotionStart : Date = Date()
                            var promotionEnd:Date = Date()
                            var userPickStart :Date = iso8601Format.parse(startTime)
                            var userPickEnd :Date = iso8601Format.parse(endTime)
                            for (element in result2) {
                                promotionStart = iso8601Format.parse(element.started_at)
                                promotionEnd = iso8601Format.parse(element.ended_at)

                                Log.e("CHECK_TIME_FORMAT","0 : ${iso8601Format.format(userPickStart)},${iso8601Format.format(userPickEnd)}, ${iso8601Format.format(promotionStart)}, ${iso8601Format.format(promotionEnd)}")
                                var promotionCost = element.price?.per_hour

                                if(userPickStart >= promotionStart && userPickEnd >= promotionEnd){//앞으로 걸침
                                    Log.e("CHECK_TIME_FORMAT","1 : ${promotionStart.day}, ${promotionEnd.day}")
                                    for(i in 0 until (promotionEnd.hours)%24){
                                        timeList[i].newCost = promotionCost!!
                                        timeList[i].type = 1
                                    }
                                }else if(userPickStart >= promotionStart && userPickEnd <= promotionEnd){//포함. 특가가 더 넓음
                                    Log.e("CHECK_TIME_FORMAT","2 : ${promotionStart.day}, ${promotionEnd.day}")
                                    for(i in 0..23){
                                        timeList[i].newCost = promotionCost!!
                                        timeList[i].type = 1
                                    }
                                }else if(userPickStart <= promotionStart && userPickEnd >= promotionEnd){//포함. 특가가 더 좁음
                                    Log.e("CHECK_TIME_FORMAT","3 : ${promotionStart.day}, ${promotionEnd.day}")
                                    for(i in (promotionStart.hours)%24 until (promotionEnd.hours)%24){
                                        timeList[i].newCost = promotionCost!!
                                        timeList[i].type = 1
                                    }
                                }else if(userPickStart <= promotionStart && userPickEnd <= promotionEnd){//뒤로 걸침
                                    Log.e("CHECK_TIME_FORMAT","4 : ${promotionStart.day}, ${promotionEnd.day}")
                                    for(i in (promotionStart.hours)%24..23){
                                        timeList[i].newCost = promotionCost!!
                                        timeList[i].type = 1
                                    }
                                }
                            }
                        }
                        timeListAdapter = RecyclerAdapterTimeList(applicationContext, timeList)
                        reservation_page_time_list.adapter = timeListAdapter
                    }
                })
            }
        })
        reservation_page_time_list.layoutManager = LinearLayoutManager(applicationContext)
        reservation_page_time_list.addOnItemTouchListener(object: RecyclerView.OnItemTouchListener{
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                if(e.action == MotionEvent.ACTION_UP){
                    var child = rv.findChildViewUnder(e.x, e.y)

                    if(child != null && e.x > 400){
                        var position = rv.getChildAdapterPosition(child)
                        if(position >= 0 && position < rv.size) {
                            var count = 0
                            var checkViewIndex = 0
                            var view = rv.layoutManager?.findViewByPosition(position)
                            timeListAdapter.boardList[position].isChecked = true
                            for (i in 0 until rv.adapter!!.itemCount) {
                                var otherView = rv.layoutManager?.findViewByPosition(i) //for문으로 position이 아닌 다른 뷰들 찾기.
                                if (otherView != view) {
                                    //timeListAdapter.boardList[i].isChecked = false
                                    //다른 뷰가 체크되어있다면 count++
                                    if(timeListAdapter.boardList[i].isChecked){
                                        count += 1
                                        checkViewIndex = i
                                    }
                                }
                            }

                            if(count > 1){
                                for(k in 0 until rv.adapter!!.itemCount){
                                    var otherView = rv.layoutManager?.findViewByPosition(k)
                                    if(otherView!=view){
                                        timeListAdapter.boardList[k].isChecked = false
                                    }
                                }
                                startTimeFinal = timeListAdapter.boardList[position].time
                                endTimeFinal = timeListAdapter.boardList[position].endTime
                                userCheckCost = 0
                                userCheckCost += if(timeListAdapter.boardList[position].newCost != 0){
                                    timeListAdapter.boardList[position].newCost
                                }else {
                                    timeListAdapter.boardList[position].cost
                                }
                            }
                            else if(count == 1){ //선택 외 1개만 선택되어있어서 그 사이 시간들 모두 선택
                                var stIdx :Int
                                var endIdx :Int
                                if(position >= checkViewIndex) {
                                    stIdx = checkViewIndex
                                    endIdx = position
                                }else{
                                    stIdx = position
                                    endIdx = checkViewIndex
                                }
                                for (j in stIdx..endIdx) {
                                    timeListAdapter.boardList[j].isChecked = true
                                }
                                startTimeFinal = timeListAdapter.boardList[stIdx].time
                                endTimeFinal= timeListAdapter.boardList[endIdx].endTime

                                userCheckCost = 0
                                for(fin in stIdx..endIdx){
                                    userCheckCost += if(timeListAdapter.boardList[fin].newCost != 0){
                                        timeListAdapter.boardList[fin].newCost
                                    }else {
                                        timeListAdapter.boardList[fin].cost
                                    }
                                }

                            }
                            else{
                                startTimeFinal = timeListAdapter.boardList[position].time
                                endTimeFinal = timeListAdapter.boardList[position].endTime
                                userCheckCost += if(timeListAdapter.boardList[position].newCost != 0){
                                    timeListAdapter.boardList[position].newCost
                                }else {
                                    timeListAdapter.boardList[position].cost
                                }
                            }
                            timeListAdapter.notifyDataSetChanged()

                            val st = Integer.parseInt(startTimeFinal.substring(0,2))
                            val en = Integer.parseInt(endTimeFinal.substring(0,2))

                            reservation_page_user_check_time.text = "$dateString $startTimeFinal - $endTimeFinal (${en-st}H)"
                            val money = moneyFormatter.format(userCheckCost)
                            reservation_page_user_check_cost.text = "+ ${money}원"
                        }
                    }
                }
            }
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return true
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_reservation)


        val roomID= intent.getIntExtra("room_id", 0)
        val promotionID = intent.getIntExtra("promotion_id", 0)
        val isRoom = intent.getBooleanExtra("isRoom", true)

        //예약하기 버튼
        setBtn(roomID)

        // 룸 정보
        setRoomInfo(roomID)



        val currentYear = startTimeCalendar.get(Calendar.YEAR)
        val currentMonth = startTimeCalendar.get(Calendar.MONTH)
        val currentDate = startTimeCalendar.get(Calendar.DATE)

        /*startTimeCalendar.set(Calendar.MONTH, currentMonth)
        startTimeCalendar.set(Calendar.DATE, currentDay)*/
        endTimeCalendar.set(Calendar.MONTH, currentMonth+3)
        //endTimeCalendar.set(Calendar.DATE, currentDay)

        reservation_page_calendar.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setMinimumDate(CalendarDay.from(currentYear, currentMonth, 1))
            .setMaximumDate(CalendarDay.from(currentYear, currentMonth+3, endTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)))
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()
        val sundayDecorator = SundayDecorator()
        val saturdayDecorator = SaturdayDecorator()
        val todayDecorator = TodayDecorator(this)

        val stCalendarDay = CalendarDay(currentYear, currentMonth, currentDate)
        val enCalendarDay = CalendarDay(endTimeCalendar.get(Calendar.YEAR), endTimeCalendar.get(Calendar.MONTH), endTimeCalendar.get(Calendar.DATE))
        val minMaxDecorator = MinMaxDecorator(stCalendarDay, enCalendarDay)
        val boldDecorator = BoldDecorator(stCalendarDay, enCalendarDay)

        reservation_page_calendar.addDecorators(sundayDecorator, saturdayDecorator, boldDecorator, minMaxDecorator, todayDecorator)

        server?.getPromotionListWithTimeRoomID(room_id = roomID)?.enqueue(object: Callback<PromotionListGet> {
            override fun onFailure(call: Call<PromotionListGet>?, t: Throwable?) { Log.e("GET_PROMOTION_LIST_FAIL", t.toString()) }
            override fun onResponse(call: Call<PromotionListGet>?, response: Response<PromotionListGet>?) {
                val result = response?.body()?.result!!
                for(element in result){
                    val st = iso8601Format.parse(element.started_at)
                    val en = iso8601Format.parse(element.ended_at)
                    val stCDay = CalendarDay(st)
                    val enCDay = CalendarDay(en)
                    Log.e("CHECK_ST_EN","$st $en ${stCDay.year} ${stCDay.month} ${stCDay.day}")
                    val event = EventDecorator(stCDay, enCDay)
                    reservation_page_calendar.addDecorator(event)
                }
            }
        })


        /*if(!isRoom){
            //특가에서 들어왔다면
            server?.getPromotion(promotionID)?.enqueue(object : Callback<PromotionGet> {
                override fun onFailure(call: Call<PromotionGet>?, t: Throwable?) { Log.e("GET_PROMOTION_FAILED", t.toString()) }
                override fun onResponse(call: Call<PromotionGet>?, response: Response<PromotionGet>?) {
                    val stTIme = response?.body()?.result?.started_at
                    var stTimeParse = iso8601Format.parse(stTIme)

                    reservation_page_calendar.setDate(stTimeParse.time)

                    startTimeCalendar.set(Calendar.YEAR, stTimeParse.year + 1900)
                    startTimeCalendar.set(Calendar.MONTH, stTimeParse.month)
                    startTimeCalendar.set(Calendar.DATE, stTimeParse.date)
                    startTimeCalendar.set(Calendar.HOUR_OF_DAY, 0)
                    startTimeCalendar.set(Calendar.MINUTE, 0)
                    startTimeCalendar.set(Calendar.SECOND, 0)

                    endTimeCalendar.set(Calendar.YEAR, stTimeParse.year + 1900)
                    endTimeCalendar.set(Calendar.MONTH, stTimeParse.month)
                    endTimeCalendar.set(Calendar.DATE, stTimeParse.date+1)
                    endTimeCalendar.set(Calendar.HOUR_OF_DAY, 0)
                    endTimeCalendar.set(Calendar.MINUTE, 0)
                    endTimeCalendar.set(Calendar.SECOND, 0)
                    Log.e("CHECK_ST_TIME_PARSE", "${iso8601Format.format(startTimeCalendar.time)}, ${iso8601Format.format(endTimeCalendar.time)}")
                    var monthText = if(startTimeCalendar.get(Calendar.MONTH)+1<10){
                        "0${startTimeCalendar.get(Calendar.MONTH)+1}"
                    }else{
                        (startTimeCalendar.get(Calendar.MONTH)+1).toString()
                    }
                    var dateText = if(startTimeCalendar.get(Calendar.DATE)<10){
                        "0${startTimeCalendar.get(Calendar.DATE)}"
                    }else{
                        startTimeCalendar.get(Calendar.DATE).toString()
                    }
                    dateString = "$monthText.$dateText"
                    setTimeList(roomID, iso8601Format.format(startTimeCalendar.time), iso8601Format.format(endTimeCalendar.time))
                }
            })
        }
        else{
            startTimeCalendar = Calendar.getInstance()
            startTimeCalendar.set(Calendar.HOUR_OF_DAY, 0)
            startTimeCalendar.set(Calendar.MINUTE, 0)
            startTimeCalendar.set(Calendar.SECOND, 0)
            endTimeCalendar = Calendar.getInstance()
            endTimeCalendar.set(Calendar.DATE, startTimeCalendar.get(Calendar.DATE) + 1)
            endTimeCalendar.set(Calendar.HOUR_OF_DAY, 0)
            endTimeCalendar.set(Calendar.MINUTE, 0)
            endTimeCalendar.set(Calendar.SECOND, 0)

            var monthText = if(startTimeCalendar.get(Calendar.MONTH)+1<10){
                "0${startTimeCalendar.get(Calendar.MONTH)+1}"
            }else{
                (startTimeCalendar.get(Calendar.MONTH)+1).toString()
            }
            var dateText = if(startTimeCalendar.get(Calendar.DATE)<10){
                "0${startTimeCalendar.get(Calendar.DATE)}"
            }else{
                startTimeCalendar.get(Calendar.DATE).toString()
            }
            dateString = "$monthText.$dateText"
            Log.e("CHECK_ST_TIME_PARSE", "${iso8601Format.format(startTimeCalendar.time)}, ${iso8601Format.format(endTimeCalendar.time)}")
            setTimeList(roomID, iso8601Format.format(startTimeCalendar.time), iso8601Format.format(endTimeCalendar.time))
        }*/



        reservation_page_calendar.setOnDateChangedListener(object:OnDateSelectedListener{
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {

                if(reservation_page_time_list.visibility == View.GONE){
                    reservation_page_time_list.visibility = View.VISIBLE
                }

                val monthText = if(date.month+1<10){
                    "0${date.month+1}"
                }else{
                    (date.month+1).toString()
                }
                val dateText = if(date.day < 10){
                    "0${date.day}"
                }else{
                    date.day.toString()
                }
                userPickDate.time = date.date.time
                userPickDate.year = date.year

                dateString = "${monthText}.${dateText}"
                reservation_page_user_check_time.text = "시간을 선택해 주세요"
                reservation_page_user_check_cost.text = ""
                startTimeFinal = ""
                endTimeFinal = ""

                startTimeCalendar.set(date.year, date.month, date.day, 0, 0, 0)
                endTimeCalendar.set(date.year, date.month, date.day + 1, 0, 0, 0)

                //iso format
                startTime = iso8601Format.format(startTimeCalendar.time)
                endTime = iso8601Format.format(endTimeCalendar.time)


                setTimeList(roomID, iso8601Format.format(startTimeCalendar.time), iso8601Format.format(endTimeCalendar.time))
                /*server?.getRoomWithOutToken(roomID)?.enqueue(object : Callback<RoomGet> {
                    override fun onFailure(call: Call<RoomGet>?, t: Throwable?) { Log.e("GET_ROOM_FAILED", t.toString()) }
                    override fun onResponse(call: Call<RoomGet>?, response: Response<RoomGet>?) {
                        var result = response?.body()?.result
                        var cost = result?.price?.per_hour!!
                        server.getPromotionListWithTimeRoomID(roomID, startTime, endTime)?.enqueue(object:Callback<PromotionListGet>{
                            override fun onFailure(call: Call<PromotionListGet>, t: Throwable) {}
                            override fun onResponse(call: Call<PromotionListGet>, response2: Response<PromotionListGet>) {
                                var result2 = response2.body()?.result!!
                                timeList = mutableListOf()
                                for(i in 0..8) {
                                    timeList.add(BoardTimeList(cost, 0, "0$i:00", "0${i+1}:00", false, true, 0))
                                }
                                timeList.add(BoardTimeList(cost,0,"09:00", "10:00", false, true, 0))
                                for(i in 10..23) {
                                    timeList.add(BoardTimeList(cost, 0,  "$i:00", "${i+1}:00", false, true, 0))
                                }

                                Log.e("CHECK_TIMELIST","$timeList")
                                if(result2.isNotEmpty()) {
                                    var promotionStart : Date = Date()
                                    var promotionEnd:Date = Date()
                                    var userPickStart :Date = iso8601Format.parse(startTime)
                                    var userPickEnd :Date = iso8601Format.parse(endTime)
                                    for (element in result2) {
                                        promotionStart = iso8601Format.parse(element.started_at)
                                        promotionEnd = iso8601Format.parse(element.ended_at)

                                        Log.e("CHECK_TIME_FORMAT","0 : ${iso8601Format.format(userPickStart)}, ${iso8601Format.format(promotionStart)}, ${iso8601Format.format(promotionEnd)}")
                                        var promotionCost = element.price?.per_hour
                                        userPickDate.year

                                        if(userPickStart <= promotionStart && userPickEnd <= promotionEnd){//앞으로 걸침
                                            Log.e("CHECK_TIME_FORMAT","1 : ${promotionStart.day}, ${promotionEnd.day}")
                                            for(i in 0 until (promotionEnd.hours)%24){
                                                timeList[i].newCost = promotionCost!!
                                                timeList[i].type = 1
                                            }
                                        }else if(userPickStart >= promotionStart && userPickEnd <= promotionEnd){//포함. 특가가 더 넓음
                                            Log.e("CHECK_TIME_FORMAT","2 : ${promotionStart.day}, ${promotionEnd.day}")
                                            for(i in 0..23){
                                                timeList[i].newCost = promotionCost!!
                                                timeList[i].type = 1
                                            }
                                        }else if(userPickStart <= promotionStart && userPickEnd >= promotionEnd){//포함. 특가가 더 좁음
                                            Log.e("CHECK_TIME_FORMAT","3 : ${promotionStart.day}, ${promotionEnd.day}")
                                            for(i in (promotionStart.hours)%24 until (promotionEnd.hours)%24){
                                                timeList[i].newCost = promotionCost!!
                                                timeList[i].type = 1
                                            }
                                        }else if(userPickStart >= promotionStart && userPickEnd <= promotionEnd){//뒤로 걸침
                                            Log.e("CHECK_TIME_FORMAT","4 : ${promotionStart.day}, ${promotionEnd.day}")
                                            for(i in (promotionStart.hours)%24..23){
                                                timeList[i].newCost = promotionCost!!
                                                timeList[i].type = 1
                                            }
                                        }
                                    }
                                }
                                timeListAdapter = RecyclerAdapterTimeList(applicationContext, timeList)
                                reservation_page_time_list.adapter = timeListAdapter
                            }
                        })
                    }
                })*/
            }
        })

        /*adf{ calendarView, year, month, date ->
            userCheckCost = 0

            var monthText = if(month+1<10){
                "0${month+1}"
            }else{
                (month+1).toString()
            }
            var dateText = if(date<10){
                "0${date}"
            }else{
                date.toString()
            }
            dateString = "${monthText}.${dateText}"
            reservation_page_user_check_time.text = "시간을 선택해 주세요"
            reservation_page_user_check_cost.text = ""
            startTimeFinal = ""
            endTimeFinal = ""


            userPickDate.year = year
            userPickDate.month = month
            userPickDate.date = date

            startTimeCalendar.set(Calendar.YEAR, year)
            startTimeCalendar.set(Calendar.MONTH, month)
            startTimeCalendar.set(Calendar.DATE, date)
            startTimeCalendar.set(Calendar.HOUR_OF_DAY, 0)
            startTimeCalendar.set(Calendar.MINUTE,0)
            startTimeCalendar.set(Calendar.SECOND,0)

            endTimeCalendar.set(Calendar.YEAR, year)
            endTimeCalendar.set(Calendar.MONTH, month)
            endTimeCalendar.set(Calendar.DATE, date+1)
            endTimeCalendar.set(Calendar.HOUR_OF_DAY, 0)
            endTimeCalendar.set(Calendar.MINUTE,0)
            endTimeCalendar.set(Calendar.SECOND,0)


            //iso format
            startTime = iso8601Format.format(startTimeCalendar.time)
            endTime = iso8601Format.format(endTimeCalendar.time)

            server?.getRoomWithOutToken(roomID)?.enqueue(object : Callback<RoomGet> {
                override fun onFailure(call: Call<RoomGet>?, t: Throwable?) { Log.e("GET_ROOM_FAILED", t.toString()) }
                override fun onResponse(call: Call<RoomGet>?, response: Response<RoomGet>?) {
                    var result = response?.body()?.result
                    var cost = result?.price?.per_hour!!
                    server.getPromotionListWithTimeRoomID(roomID, startTime, endTime)?.enqueue(object:Callback<PromotionListGet>{
                        override fun onFailure(call: Call<PromotionListGet>, t: Throwable) {}
                        override fun onResponse(call: Call<PromotionListGet>, response2: Response<PromotionListGet>) {
                            var result2 = response2.body()?.result!!
                            timeList = mutableListOf()
                            for(i in 0..8) {
                                timeList.add(BoardTimeList(cost, 0, "0$i:00", "0${i+1}:00", false, true, 0))
                            }
                            timeList.add(BoardTimeList(cost,0,"09:00", "10:00", false, true, 0))
                            for(i in 10..23) {
                                timeList.add(BoardTimeList(cost, 0,  "$i:00", "${i+1}:00", false, true, 0))
                            }

                            if(result2.isNotEmpty()) {
                                var promotionStart : Date = Date()
                                var promotionEnd:Date = Date()
                                var userPickStart :Date = iso8601Format.parse(startTime)
                                var userPickEnd :Date = iso8601Format.parse(endTime)
                                for (element in result2) {
                                    promotionStart = iso8601Format.parse(element.started_at)
                                    promotionEnd = iso8601Format.parse(element.ended_at)

                                    Log.e("CHECK_TIME_FORMAT","0 : ${iso8601Format.format(userPickStart)}, ${iso8601Format.format(promotionStart)}, ${iso8601Format.format(promotionEnd)}")
                                    var promotionCost = element.price?.per_hour
                                    userPickDate.year

                                    if(userPickStart <= promotionStart && userPickEnd <= promotionEnd){//앞으로 걸침
                                        Log.e("CHECK_TIME_FORMAT","1 : ${promotionStart.day}, ${promotionEnd.day}")
                                        for(i in 0 until (promotionEnd.hours)%24){
                                            timeList[i].newCost = promotionCost!!
                                            timeList[i].type = 1
                                        }
                                    }else if(userPickStart >= promotionStart && userPickEnd <= promotionEnd){//포함. 특가가 더 넓음
                                        Log.e("CHECK_TIME_FORMAT","2 : ${promotionStart.day}, ${promotionEnd.day}")
                                        for(i in 0..23){
                                            timeList[i].newCost = promotionCost!!
                                            timeList[i].type = 1
                                        }
                                    }else if(userPickStart <= promotionStart && userPickEnd >= promotionEnd){//포함. 특가가 더 좁음
                                        Log.e("CHECK_TIME_FORMAT","3 : ${promotionStart.day}, ${promotionEnd.day}")
                                        for(i in (promotionStart.hours)%24 until (promotionEnd.hours)%24){
                                            timeList[i].newCost = promotionCost!!
                                            timeList[i].type = 1
                                        }
                                    }else if(userPickStart >= promotionStart && userPickEnd <= promotionEnd){//뒤로 걸침
                                        Log.e("CHECK_TIME_FORMAT","4 : ${promotionStart.day}, ${promotionEnd.day}")
                                        for(i in (promotionStart.hours)%24..23){
                                            timeList[i].newCost = promotionCost!!
                                            timeList[i].type = 1
                                        }
                                    }
                                }
                            }
                            timeListAdapter = RecyclerAdapterTimeList(applicationContext, timeList)
                            reservation_page_time_list.adapter = timeListAdapter
                        }
                    })
                }
            })
        }*/



    }

    private fun setRoomInfo(roomID :Int){
        server?.getRoomWithOutToken(roomID)?.enqueue(object : Callback<RoomGet> {
            override fun onFailure(call: Call<RoomGet>?, t: Throwable?) { Log.e("GET_ROOM_FAILED", t.toString()) }
            override fun onResponse(call: Call<RoomGet>?, response: Response<RoomGet>?) {
                var result = response?.body()?.result
                /*룸 이미지 슬라이더*/
                var roomImageList: MutableList<String> = mutableListOf()
                for (i in 0 until result?.images?.size!!.toInt()) {
                    val imageString =
                        result.images!![i].toString() //"https://dev.gongdam.kr/images/" +
                    roomImageList.add(imageString)
                }
                imageSliderViewPager =
                    findViewById(R.id.reservation_page_image_viewPager)
                val imageSliderNowNum =
                    findViewById<TextView>(R.id.reservation_page_image_slider_now_image_number)
                val imageSliderWholeNum =
                    findViewById<TextView>(R.id.reservation_page_image_slider_whole_image_number)
                val imageSliderAdapter = ImageSliderAdapter(this@PageReservation, roomImageList)
                imageSliderWholeNum.text = roomImageList.size.toString()
                val appearAnim = AnimationUtils.loadAnimation(applicationContext , R.anim.fade_in)
                val delayHandler = Handler()
                imageSliderViewPager.adapter = imageSliderAdapter
                imageSliderViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageSelected(position: Int) {
                        imageSliderNowNum.text = (position + 1).toString()
                        reservation_page_image_slider_num_layout.visibility = View.VISIBLE
                        reservation_page_image_slider_num_layout.animation = appearAnim
                        delayHandler.postDelayed({
                            //reservation_page_image_slider_num_layout.animation = disappearAnim
                            reservation_page_image_slider_num_layout.visibility = View.INVISIBLE
                        }, 1500)
                    }
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                    override fun onPageScrollStateChanged(state: Int) {}
                })

                /*스튜디오이름, 룸이름*/
                reservation_page_studio_name.text = result.studio?.name
                reservation_page_room_name.text = result.name

                /*이용안내*/
                val perHour = result.price?.per_hour
                reservation_page_price_per_hour_TextV.text = (perHour!! / 10000).toString()
                val defaultReservationMintue = result.price?.default_reservation_minute
                reservation_page_min_reservation_time_TextV.text =
                    (defaultReservationMintue!! / 60 + defaultReservationMintue % 60).toString()
                reservation_page_min_people_TextV.text =
                    result.price?.default_user_amount.toString()
                val additionalPerPerson = result.price?.additional_per_person
                reservation_page_one_more_people_per_hour_TextV.text =
                    (additionalPerPerson!! / 10000 + (additionalPerPerson % 10000) * 0.0001).toString()

            }
        })
    }

    private fun setBtn(roomID:Int){
        reservation_page_make_reservation_btn.setOnClickListener {
            if(userCheckCost != 0) {
                val st = Integer.parseInt(startTimeFinal.substring(0,2))
                val en = Integer.parseInt(endTimeFinal.substring(0,2))
                val minTime = Integer.parseInt(reservation_page_min_reservation_time_TextV.text.toString())
                if(minTime > en-st){
                    Toast.makeText(applicationContext, "최소 예약시간은 ${minTime}시간입니다.", Toast.LENGTH_SHORT).show()
                }else {
                    val intent = Intent(this, PageReservation2::class.java)
                    intent.putExtra("room_id", roomID)
                    val minPeopleString = reservation_page_min_people_TextV.text.toString()
                    val minPeople = Integer.parseInt(minPeopleString)
                    intent.putExtra("min_people", minPeople)
                    intent.putExtra("start_time", startTimeFinal)
                    intent.putExtra("end_time", endTimeFinal)
                    val userPick = iso8601Format.format(userPickDate)
                    intent.putExtra("user_pick_date", userPick)
                    intent.putExtra("st_ro_name", "${reservation_page_studio_name.text} - ${reservation_page_room_name.text}"
                    ) //
                    startActivity(intent)
                }
            }else{
                Toast.makeText(applicationContext, "시간을 선택해 주세요.", Toast.LENGTH_SHORT).show()
            }
            //최소예약시간 확인해야함
        }
    }
}
