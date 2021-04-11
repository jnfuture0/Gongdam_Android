package org.gongdam

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_page_reservation.*
import kotlinx.android.synthetic.main.activity_page_reservation_confirm.*
import org.gongdam.Adapter.TabAdapterReservationConfirm
import org.gongdam.Json.RefreshToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PageReservationConfirm : AppCompatActivity() {
    private val adapter by lazy{ TabAdapterReservationConfirm(supportFragmentManager) }

    companion object{
        var server = MainActivity.server
        lateinit var refToken : String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_reservation_confirm)


        refToken = intent.getStringExtra("ref_token")




        vp_reservation_confirm.adapter = ReservationConfirm@adapter

        vp_reservation_confirm.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
            override fun onPageSelected(position: Int) {
            }
        })

        tabLayout_reservation_confirm_page.setupWithViewPager(vp_reservation_confirm)

        tabLayout_reservation_confirm_page.getTabAt(0)?.text = "다가올 예약"
        tabLayout_reservation_confirm_page.getTabAt(1)?.text = "지나간 예약"
        tabLayout_reservation_confirm_page.getTabAt(2)?.text = "취소/환불내역"
        tabLayout_reservation_confirm_page.setTabTextColors(Color.parseColor("#bfbfbf"), Color.parseColor("#001023"))
    }
}
