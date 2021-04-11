package org.gongdam.Adapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_page_room.*
import org.gongdam.R

class ImageSliderMainAdapter(private val context : Context, imageList : MutableList<Int>):PagerAdapter() {
    private var layoutInflater:LayoutInflater? = null

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
    val imageL = imageList
    //val layout = r_id_layout

    override fun getCount(): Int {
        return imageL.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = layoutInflater!!.inflate(R.layout.image_slider, null)
        val image = v.findViewById<View>(R.id.image_slider_image_view) as ImageView

        image.setImageResource(imageL[position])
        val vp = container as ViewPager
        vp.addView(v, 0)

        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val v = `object` as View
        vp.removeView(v)
    }

}