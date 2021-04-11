package org.gongdam.Adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import org.gongdam.Fragment.*

class TabAdapterReservationConfirm(fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    override fun getItem(p0: Int): Fragment? {
        return when(p0){
            0 -> ReservConfirm1()
            1 -> ReservConfirm2()
            2 -> ReservConfirm3()
            else -> null
        }
    }

    override fun getCount() = 3

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        //return fragmentTitleList[position]    //맨위 ABCD 글자로 넣을 때
        return null //이미지만 보이고 텍스트 지우고 싶을 때
    }

}