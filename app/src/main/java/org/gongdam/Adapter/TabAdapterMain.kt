package org.gongdam.Adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import org.gongdam.Fragment.FragmentCenter
import org.gongdam.Fragment.FragmentHome
import org.gongdam.Fragment.FragmentPromotion
import org.gongdam.Fragment.FragmentSearch

class TabAdapterMain(fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    override fun getItem(p0: Int): Fragment? {
        return when(p0){
            0 -> FragmentHome()

            1 -> FragmentSearch()

            2 -> FragmentPromotion()

            3 -> FragmentCenter()

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