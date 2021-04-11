package org.gongdam.Adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import org.gongdam.Fragment.Center1_1
import org.gongdam.Fragment.Center1_2

class TabAdapterCenter1(fm:FragmentManager):FragmentStatePagerAdapter(fm){
    override fun getItem(position: Int): Fragment? {
        return when(position){
            0 -> Center1_1()

            1 -> Center1_2()

            2 -> Center1_1()

            3 -> Center1_2()

            else -> null
        }
    }

    override fun getCount() = 4

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        //return fragmentTitleList[position]    //맨위 ABCD 글자로 넣을 때
        return null //이미지만 보이고 텍스트 지우고 싶을 때
    }
}