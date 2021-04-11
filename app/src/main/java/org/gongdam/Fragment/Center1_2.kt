package org.gongdam.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_fragment_center_1.*
import kotlinx.android.synthetic.main.center1_fragment_1.*
import kotlinx.android.synthetic.main.center1_fragment_2.*
import org.gongdam.R

class Center1_2 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.center1_fragment_2, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        center1_page_2_1_a.setOnClickListener {
            showHide(center1_page_2_1_b)
        }

        center1_page_2_2_a.setOnClickListener {
            showHide(center1_page_2_2_b)
        }


    }

    fun showHide(view:View){
        view.visibility = if(view.visibility == View.VISIBLE){
            View.GONE
        } else{
            View.VISIBLE
        }
    }

}
