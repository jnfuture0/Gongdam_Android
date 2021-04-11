package org.gongdam

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_page_filter_promotion.*
import org.gongdam.Adapter.RecyclerAdapterFilterSearchLocation
import org.gongdam.Adapter.RecyclerAdapterTime
import org.gongdam.Struct.BoardOneString
import org.gongdam.Struct.BoardStringBoolean
import org.jetbrains.anko.toast
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PageFilterPromotion : AppCompatActivity() {

    var locationList = mutableListOf(BoardOneString("서울"),
        BoardOneString("경기"), BoardOneString("인천"), BoardOneString("강원"), BoardOneString("대전"), BoardOneString("세종"),
        BoardOneString("충정"), BoardOneString("부산"), BoardOneString("울산"), BoardOneString("경남"), BoardOneString("경북"),
        BoardOneString("대구"), BoardOneString("광주"), BoardOneString("전남"), BoardOneString("전북"), BoardOneString("제주"))

    companion object{
        var userLocationList : MutableList<String> = mutableListOf()

        var startAt = ""
        var endedAt = ""
    }

    val timeList = mutableListOf(
        BoardStringBoolean("00:00", false),BoardStringBoolean("01:00", false),BoardStringBoolean("02:00", false),BoardStringBoolean("03:00", false),
        BoardStringBoolean("04:00", false),BoardStringBoolean("05:00", false),BoardStringBoolean("06:00", false),BoardStringBoolean("07:00", false),
        BoardStringBoolean("08:00", false),BoardStringBoolean("09:00", false),BoardStringBoolean("10:00", false),BoardStringBoolean("11:00", false),
        BoardStringBoolean("12:00", false),BoardStringBoolean("13:00", false),BoardStringBoolean("14:00", false),BoardStringBoolean("15:00", false),
        BoardStringBoolean("16:00", false),BoardStringBoolean("17:00", false),BoardStringBoolean("18:00", false),BoardStringBoolean("19:00", false),
        BoardStringBoolean("20:00", false),BoardStringBoolean("21:00", false),BoardStringBoolean("22:00", false),BoardStringBoolean("23:00", false)
    )


    var calendar : Calendar = Calendar.getInstance()
    var endCalendar : Calendar = Calendar.getInstance()
    val dateFormat : DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")


    fun setTruePtoP(p1:Int, p2:Int){
        var s1 = timeList[p1].string
        timeList[p1] = BoardStringBoolean(s1, true)
        var s2 = timeList[p2].string
        timeList[p2] = BoardStringBoolean(s2, true)
        if(p1 >= p2){
            for(i in p1..p2){
                var si = timeList[i].string
                timeList[i] = BoardStringBoolean(si, true)
            }
        }else{
            for(i in p2..p1){
                var si = timeList[i].string
                timeList[i] = BoardStringBoolean(si, true)
            }
        }
    }


    var firstFlag = false
    var secondFlag = false
    var firstPosition = 0


    fun setTimeListAdapter() : RecyclerAdapterTime{
        return RecyclerAdapterTime(this, timeList){ board, isSelected, position ->
            if(firstFlag){
                secondFlag = true
            }else{
                firstFlag = true
                firstPosition = position
            }

            if(firstFlag && secondFlag){
                setTruePtoP(firstPosition, position)
                filter_promotion_time_recyclerView.adapter = setTimeListAdapter()
            }
        }
    }

    lateinit var timeListAdapter :RecyclerAdapterTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_filter_promotion)



        val colorFilterWhite : Int = ContextCompat.getColor(this@PageFilterPromotion, R.color.filter_white)
        var locationListAdapter = RecyclerAdapterFilterSearchLocation(this, locationList, userLocationList){

            if( userLocationList.contains(it.text.toString()) ){
                userLocationList.remove(it.text.toString())
            } else{
                userLocationList.add(it.text.toString())
            }

            if(it.currentTextColor == colorFilterWhite){
                it.setTextColor(resources.getColor(R.color.filter_black))
                it.setBackgroundResource(android.R.color.transparent)
            }else{
                it.setTextColor(resources.getColor(R.color.filter_white))
                it.setBackgroundResource(R.drawable.style_filter_background)
            }
        }
        filter_promotion_page_location_recyclerView.adapter = locationListAdapter
        filter_promotion_page_location_recyclerView.layoutManager = GridLayoutManager(this@PageFilterPromotion, 8)

        filter_promotion_page_location_layout_btn.setOnClickListener {
            showHideImageChange(filter_promotion_page_location_recyclerView_layout,filter_promotion_page_location_plus_minus_imageview)
        }
        filter_promotion_page_time_layout_btn.setOnClickListener {
            showHideImageChange(filter_promotion_page_time_recyclerView_layout,filter_promotion_page_time_plus_minus_imageview)
        }
        filter_promotion_page_date_layout_btn.setOnClickListener {
            showHideImageChange(filter_promotion_page_date_recyclerView_layout,filter_promotion_page_date_plus_minus_imageview)
        }

        /*시간 선택하는거......*/
        /*timeListAdapter = RecyclerAdapterTime(this, timeList){ board, isSelected, position ->
            if(firstFlag){
                secondFlag = true
            }else{
                firstFlag = true
                firstPosition = position
            }

            if(firstFlag && secondFlag){
                setTruePtoP(firstPosition, position)
                filter_promotion_time_recyclerView.adapter = timeListAdapter
                firstFlag = false
                secondFlag = false
            }
        }

        filter_promotion_time_recyclerView.adapter = timeListAdapter
        filter_promotion_time_recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)*/




        val mTimePickerListener1 : TimePicker.OnTimeChangedListener = TimePicker.OnTimeChangedListener { timePicker, hour, minute ->
            if(minute % 60 != 0){
                timePicker.currentMinute = 0
                toast("정각 단위로만 설정 가능합니다")
            }
            calendar.set(Calendar.HOUR, timePicker.currentHour)
            startAt = dateFormat.format(calendar.time)
        }
        val mTimePickerListener2 : TimePicker.OnTimeChangedListener = TimePicker.OnTimeChangedListener { timePicker, hour, minute ->
            if(minute % 60 != 0){
                timePicker.currentMinute = 0
                toast("정각 단위로만 설정 가능합니다")
            }
            endCalendar.set(Calendar.HOUR, timePicker.currentHour)
            endedAt = dateFormat.format(endCalendar.time)
        }
        filter_promotion_timePicker_start_time.currentMinute = 0
        filter_promotion_timePicker_start_time.setIs24HourView(true)
        filter_promotion_timePicker_start_time.setOnTimeChangedListener(mTimePickerListener1)
        filter_promotion_timePicker_end_time.currentMinute = 0
        filter_promotion_timePicker_end_time.setIs24HourView(true)
        filter_promotion_timePicker_end_time.setOnTimeChangedListener(mTimePickerListener2)


        var currentMonth = calendar.get(Calendar.MONTH)
        var currentDay = calendar.get(Calendar.DATE)
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DATE, currentDay)
        calendar.set(Calendar.MINUTE, 0)
        endCalendar.set(Calendar.MINUTE, 0)

        filter_promotion_calendar.minDate = calendar.timeInMillis

        /*달력 날짜 바꿀 때*/
        filter_promotion_calendar.setOnDateChangeListener { calendarView, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DATE, day)
            endCalendar.set(Calendar.YEAR, year)
            endCalendar.set(Calendar.MONTH, month)
            endCalendar.set(Calendar.DATE, day)

            startAt = dateFormat.format(calendar.time)
            endedAt = dateFormat.format(endCalendar.time)
        }


        /*초기화 버튼*/
        filter_promotion_page_refresh_btn.setOnClickListener {
            startAt = ""
            endedAt = ""
            userLocationList = mutableListOf()
            var locationListAdapter2 = RecyclerAdapterFilterSearchLocation(this, locationList, userLocationList){
                if( userLocationList.contains(it.text.toString()) ){
                    userLocationList.remove(it.text.toString())
                } else{
                    userLocationList.add(it.text.toString())
                }
                if(it.currentTextColor == colorFilterWhite){
                    it.setTextColor(resources.getColor(R.color.filter_black))
                    it.setBackgroundResource(android.R.color.transparent)
                }else{
                    it.setTextColor(resources.getColor(R.color.filter_white))
                    it.setBackgroundResource(R.drawable.style_filter_background)
                }
            }
            filter_promotion_page_location_recyclerView.adapter = locationListAdapter2
            filter_promotion_calendar.date = calendar.timeInMillis
            calendar = Calendar.getInstance()
            endCalendar = Calendar.getInstance()
            calendar.set(Calendar.MINUTE, 0)
            endCalendar.set(Calendar.MINUTE, 0)
            toast("필터가 초기화되었습니다.")
        }

        filter_promotion_apply_btn.setOnClickListener {
            //toast("start : ${startAt}\nend : ${endedAt}\nuser location list : ${userLocationList}" )
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    fun showHideImageChange(view: View, imageView: ImageView){
        if(view.visibility == View.VISIBLE){
            view.visibility = View.GONE
            imageView.setImageResource(R.drawable.plus_icon_detail)
        } else{
            view.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.minus_icon)
        }
    }
}
