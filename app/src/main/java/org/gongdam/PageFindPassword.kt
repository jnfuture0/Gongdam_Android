package org.gongdam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_page_find_password.*

class PageFindPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_find_password)

        val items = resources.getStringArray(R.array.phone_spinner)



        val phoneSpinnerAdapter = object: ArrayAdapter<String>(this, R.layout.phone_spinner){
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

                val v = super.getView(position, convertView, parent)

                if (position == count) {
                    //마지막 포지션의 textView 를 힌트 용으로 사용합니다.
                    (v.findViewById<View>(R.id.tvItemSpinner) as TextView).text = ""
                    //아이템의 마지막 값을 불러와 hint로 추가해 줍니다.
                    (v.findViewById<View>(R.id.tvItemSpinner) as TextView).hint = getItem(count)
                }

                return v
            }

            override fun getCount(): Int {
                //마지막 아이템은 힌트용으로만 사용하기 때문에 getCount에 1을 빼줍니다.
                return super.getCount() - 1
            }
        }

        phoneSpinnerAdapter.addAll(items.toMutableList())
        phoneSpinnerAdapter.add("통신사")
        find_pw_page_spinner.adapter = phoneSpinnerAdapter
        find_pw_page_spinner.setSelection(phoneSpinnerAdapter.count)
        find_pw_page_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                when (position){
                    0 -> {

                    }

                    1 -> {

                    }

                    2 -> {

                    }

                    3 -> {

                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }
}
