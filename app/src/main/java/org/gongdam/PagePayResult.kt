package org.gongdam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_page_pay_result.*

class PagePayResult : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_pay_result)

        val payName = intent.getStringExtra("name")
        val payCost = intent.getStringExtra("cost")
        pay_result_page_text.text = payName
        pay_result_page_text2.text = payCost
    }
}
