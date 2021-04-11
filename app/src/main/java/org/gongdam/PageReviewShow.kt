package org.gongdam

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_page_review_show.*
import org.gongdam.Adapter.TabAdapterReviewShowPage

class PageReviewShow : AppCompatActivity() {

    //var token = MainActivity.getToken(MainActivity.refToken)

    private val adapter by lazy{ TabAdapterReviewShowPage(supportFragmentManager) }

    companion object{
        var token = ""
        var room_id = 0
        var studio_id = 0
        var isRoom = true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_review_show)


        room_id = intent.getIntExtra("room_id", 0)
        studio_id = intent.getIntExtra("studio_id", 0)
        isRoom = intent.getBooleanExtra("isRoom", true)

        vpReviewShow.adapter = PageReviewShow@adapter

        vpReviewShow.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
            }
        })

        tabLayout_review_show_page.setupWithViewPager(vpReviewShow)
        tabLayout_review_show_page.getTabAt(0)?.text = "전체 보기"
        tabLayout_review_show_page.getTabAt(1)?.text = "포토 후기만 보기"
        tabLayout_review_show_page.setTabTextColors(Color.parseColor("#bfbfbf"), Color.parseColor("#001023"))



    }
}
