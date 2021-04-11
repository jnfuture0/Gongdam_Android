package org.gongdam.Adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import org.gongdam.Fragment.*

class TabAdapterEquipment(fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    val CAM_ID = 7
    val LENS_ID = 8
    val LIGHT_ID = 9
    val BACK_ID = 10
    val MIC_ID = 11
    val ETC_ID = 12

    override fun getItem(p0: Int): Fragment? {
        return when(p0){
            0 -> FragmentEquipment(CAM_ID)

            1 -> FragmentEquipment(LENS_ID)

            2 -> FragmentEquipment(LIGHT_ID)

            3 -> FragmentEquipment(BACK_ID)

            4 -> FragmentEquipment(MIC_ID)

            5 -> FragmentEquipment(ETC_ID)

            else -> null
        }
    }

    override fun getCount() = 6

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        //return fragmentTitleList[position]    //맨위 ABCD 글자로 넣을 때
        return null //이미지만 보이고 텍스트 지우고 싶을 때
    }

}