package org.gongdam.Fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_fragment_center.*
import org.gongdam.Adapter.TabAdapterCenter
import org.gongdam.R

class FragmentCenter : Fragment() {

    private val adapter by lazy{ TabAdapterCenter(childFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_fragment_center, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        vpCenter.adapter = FragmentCenter@adapter
        vpCenter.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int,positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> { }
                    1 -> { }
                }
            }
        })


        tabLayout_center_page.setupWithViewPager(vpCenter)
        tabLayout_center_page.getTabAt(0)?.text = "자주 묻는 질문"
        tabLayout_center_page.getTabAt(1)?.text = "1:1 문의"
        tabLayout_center_page.setTabTextColors(Color.parseColor("#bfbfbf"), Color.parseColor("#001023"))
    }
}
