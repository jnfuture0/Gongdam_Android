package org.gongdam

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_page_my_space.*
import org.gongdam.Adapter.TabAdapterMySpace

class PageMySpace : AppCompatActivity() {


    private val adapter by lazy{ TabAdapterMySpace(supportFragmentManager) }

    companion object{
        lateinit var refToken :String
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_my_space)



        refToken = intent.getStringExtra("ref_token")


        vpMySpace.adapter = PageMyReview@adapter

        vpMySpace.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

            }
        })

        tabLayout_mySpace_page.setupWithViewPager(vpMySpace)
        tabLayout_mySpace_page.getTabAt(0)?.text = "위시리스트"
        tabLayout_mySpace_page.getTabAt(1)?.text = "최근 본 공간"
        tabLayout_mySpace_page.setTabTextColors(Color.parseColor("#bfbfbf"), Color.parseColor("#001023"))
    }
}
