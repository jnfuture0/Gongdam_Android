package org.gongdam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_page_sign_up.*
import org.gongdam.Json.HotKeysGet
import org.gongdam.Json.PostUser
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PageSignUp : AppCompatActivity() {

    val server = MainActivity.server

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_sign_up)


        sign_up_page_sign_upBtn.setOnClickListener {
            if(sign_up_page_pwET.text.toString() !=
                    sign_up_page_pwET2.text.toString()){
                toast("비밀번호가 일치하지 않습니다.")
            }else {
                var infoMap: HashMap<String, Any> = HashMap()
                infoMap["account"] = sign_up_page_idET.text.toString()
                infoMap["password"] = sign_up_page_pwET.text.toString()
                infoMap["name"] = sign_up_page_nameET.text.toString()
                infoMap["email"] = sign_up_page_emailET.text.toString()
                infoMap["phone"] = ""
                val addressMap : HashMap<String, Any> = HashMap()
                addressMap["post_code"] = ""
                addressMap["district"] = ""
                addressMap["detail"] = ""
                infoMap["address"] = addressMap
                infoMap["image_url"] = ""
                server?.postUser(infoMap)?.enqueue(object: Callback<PostUser> {
                    override fun onFailure(call: Call<PostUser>?, t: Throwable?) { Log.e("GET_HOT_KEYS_FAILED", t.toString()) }
                    override fun onResponse(call: Call<PostUser>?, response: Response<PostUser>?) {
                        var result = response?.body()?.result
                        if(result == null){
                            //아이디 겹치거나 등등의 문제.
                        }else{
                            finish()
                        }
                    }
                })
            }
        }
    }
}
