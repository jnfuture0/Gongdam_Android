package org.gongdam

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_page_room_equipment.*
import kotlinx.android.synthetic.main.tablayout_equip_background.*
import kotlinx.android.synthetic.main.tablayout_equip_camera.*
import kotlinx.android.synthetic.main.tablayout_equip_etc.*
import kotlinx.android.synthetic.main.tablayout_equip_lens.*
import kotlinx.android.synthetic.main.tablayout_equip_light.*
import kotlinx.android.synthetic.main.tablayout_equip_mic.*
import org.gongdam.Adapter.TabAdapterEquipment

class PageRoomEquipment : AppCompatActivity() {

    private val equipAdapter by lazy{ TabAdapterEquipment(supportFragmentManager) }

    companion object{
        var roomID = 0
    }

    private fun setIcon(){
        tab_equip_camera_text.setTextColor(Color.parseColor("#dbdae0"))
        tab_equip_lens_text.setTextColor(Color.parseColor("#dbdae0"))
        tab_equip_light_text.setTextColor(Color.parseColor("#dbdae0"))
        tab_equip_background_text.setTextColor(Color.parseColor("#dbdae0"))
        tab_equip_mic_text.setTextColor(Color.parseColor("#dbdae0"))
        tab_equip_etc_text.setTextColor(Color.parseColor("#dbdae0"))
        tab_equip_camera_layout.setBackgroundResource(R.drawable.style_radius5_strokedbdae0_solid_fff_is_it_used)
        tab_equip_lens_layout.setBackgroundResource(R.drawable.style_radius5_strokedbdae0_solid_fff_is_it_used)
        tab_equip_light_layout.setBackgroundResource(R.drawable.style_radius5_strokedbdae0_solid_fff_is_it_used)
        tab_equip_background_layout.setBackgroundResource(R.drawable.style_radius5_strokedbdae0_solid_fff_is_it_used)
        tab_equip_mic_layout.setBackgroundResource(R.drawable.style_radius5_strokedbdae0_solid_fff_is_it_used)
        tab_equip_etc_layout.setBackgroundResource(R.drawable.style_radius5_strokedbdae0_solid_fff_is_it_used)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_room_equipment)

        roomID = intent.getIntExtra("room_id", 1)

        room_equipment_page_viewPager.adapter = equipAdapter
        room_equipment_page_viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> {
                        setIcon()
                        tab_equip_camera_text.setTextColor(Color.parseColor("#303030"))
                        tab_equip_camera_layout.setBackgroundResource(R.drawable.style_radius5_stroke303030_solid_eeeeee)
                    }
                    1 -> {
                        setIcon()
                        tab_equip_lens_text.setTextColor(Color.parseColor("#303030"))
                        tab_equip_lens_layout.setBackgroundResource(R.drawable.style_radius5_stroke303030_solid_eeeeee)
                    }
                    2 -> {
                        setIcon()
                        tab_equip_light_text.setTextColor(Color.parseColor("#303030"))
                        tab_equip_light_layout.setBackgroundResource(R.drawable.style_radius5_stroke303030_solid_eeeeee)
                    }
                    3 -> {
                        setIcon()
                        tab_equip_background_text.setTextColor(Color.parseColor("#303030"))
                        tab_equip_background_layout.setBackgroundResource(R.drawable.style_radius5_stroke303030_solid_eeeeee)
                    }
                    4 -> {
                        setIcon()
                        tab_equip_mic_text.setTextColor(Color.parseColor("#303030"))
                        tab_equip_mic_layout.setBackgroundResource(R.drawable.style_radius5_stroke303030_solid_eeeeee)
                    }
                    5 -> {
                        setIcon()
                        tab_equip_etc_text.setTextColor(Color.parseColor("#303030"))
                        tab_equip_etc_layout.setBackgroundResource(R.drawable.style_radius5_stroke303030_solid_eeeeee)
                    }
                }
            }
        })

        room_equipment_page_tabLayout.setupWithViewPager(room_equipment_page_viewPager)
        room_equipment_page_tabLayout.getTabAt(0)?.setCustomView(R.layout.tablayout_equip_camera)
        room_equipment_page_tabLayout.getTabAt(1)?.setCustomView(R.layout.tablayout_equip_lens)
        room_equipment_page_tabLayout.getTabAt(2)?.setCustomView(R.layout.tablayout_equip_light)
        room_equipment_page_tabLayout.getTabAt(3)?.setCustomView(R.layout.tablayout_equip_background)
        room_equipment_page_tabLayout.getTabAt(4)?.setCustomView(R.layout.tablayout_equip_mic)
        room_equipment_page_tabLayout.getTabAt(5)?.setCustomView(R.layout.tablayout_equip_etc)


        val vGroup :ViewGroup= room_equipment_page_tabLayout.getChildAt(0) as ViewGroup
        val tabLen = vGroup.childCount
        for(j in 0 until tabLen){
            val v = vGroup.getChildAt(j)
            v.setPadding(0,0,0,0)
        }



    }
}
