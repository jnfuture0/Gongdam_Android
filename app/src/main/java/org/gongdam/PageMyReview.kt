package org.gongdam

import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_fragment_center.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_page_my_review.*
import org.gongdam.Adapter.TabAdapterMyReview

class PageMyReview : AppCompatActivity() {



    private val adapter by lazy{ TabAdapterMyReview(supportFragmentManager) }

    companion object{
        lateinit var refToken :String
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_my_review)


        refToken = intent.getStringExtra("ref_token")



        vpMyReiew.adapter = PageMyReview@adapter

        vpMyReiew.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
            }
        })

        tabLayout_myReview_page.setupWithViewPager(vpMyReiew)
        tabLayout_myReview_page.getTabAt(0)?.text = "작성 가능한 후기"
        tabLayout_myReview_page.getTabAt(1)?.text = "작성한 후기"
        tabLayout_myReview_page.setTabTextColors(Color.parseColor("#bfbfbf"), Color.parseColor("#001023"))

    }
}
