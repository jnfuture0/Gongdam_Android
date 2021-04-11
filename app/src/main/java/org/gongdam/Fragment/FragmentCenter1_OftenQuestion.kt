package org.gongdam.Fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_fragment_center_1.*
import org.gongdam.Adapter.TabAdapterCenter1
import org.gongdam.R

class FragmentCenter1_OftenQuestion : Fragment() {
    private val adapter by lazy{ TabAdapterCenter1(childFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_fragment_center_1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        center1_vp.adapter = FragmentCenter1@adapter
        center1_vp.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int,positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> {
                        center1_page_top_btn1.setTextColor(Color.parseColor("#303030"))
                        center1_page_top_btn2.setTextColor(Color.parseColor("#bfbfbf"))
                        center1_page_top_btn3.setTextColor(Color.parseColor("#bfbfbf"))
                        center1_page_top_btn4.setTextColor(Color.parseColor("#bfbfbf"))
                    }

                    1 -> {
                        center1_page_top_btn1.setTextColor(Color.parseColor("#bfbfbf"))
                        center1_page_top_btn2.setTextColor(Color.parseColor("#303030"))
                        center1_page_top_btn3.setTextColor(Color.parseColor("#bfbfbf"))
                        center1_page_top_btn4.setTextColor(Color.parseColor("#bfbfbf"))
                    }

                    2 -> {
                        center1_page_top_btn1.setTextColor(Color.parseColor("#bfbfbf"))
                        center1_page_top_btn2.setTextColor(Color.parseColor("#bfbfbf"))
                        center1_page_top_btn3.setTextColor(Color.parseColor("#303030"))
                        center1_page_top_btn4.setTextColor(Color.parseColor("#bfbfbf"))
                    }

                    3 -> {
                        center1_page_top_btn1.setTextColor(Color.parseColor("#bfbfbf"))
                        center1_page_top_btn2.setTextColor(Color.parseColor("#bfbfbf"))
                        center1_page_top_btn3.setTextColor(Color.parseColor("#bfbfbf"))
                        center1_page_top_btn4.setTextColor(Color.parseColor("#303030"))
                    }
                }
            }
        })



        center1_page_top_btn1.setOnClickListener { center1_vp.currentItem = 0 }
        center1_page_top_btn2.setOnClickListener { center1_vp.currentItem = 1 }
        center1_page_top_btn3.setOnClickListener { center1_vp.currentItem = 2 }
        center1_page_top_btn4.setOnClickListener { center1_vp.currentItem = 3 }

    }

}
