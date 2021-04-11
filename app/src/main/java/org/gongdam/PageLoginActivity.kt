package org.gongdam

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_page_login.*
import org.gongdam.Json.PostToken
import org.gongdam.Json.PromotionGet
import org.gongdam.Json.RetrofitService
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.NullPointerException

class PageLoginActivity : AppCompatActivity() {

    //var token = MainActivity.getToken(MainActivity.refToken)
    var server = MainActivity.server

    //private val sessionCallback = ISession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_login)

        var account : String
        var password : String

        login_page_find_pw_btn.setOnClickListener {
            val intent = Intent(this, PageFindPassword::class.java)
            startActivity(intent)
        }

        login_page_sign_up_btn.setOnClickListener {
            val intent = Intent(this, PageSignUp::class.java)
            startActivity(intent)
        }

        login_page_loginBtn.setOnClickListener {
            var id = login_page_idET.text.toString()
            var pw = login_page_pwET.text.toString()
            var infoMap:HashMap<String,String> = HashMap()
            infoMap["account"] = id
            infoMap["password"] = pw
            server?.postToken(infoMap)?.enqueue(object: Callback<PostToken> {
                override fun onFailure(call: Call<PostToken>?, t: Throwable?) { Log.e("POST_TOKEN_FAILED", t.toString()) }
                override fun onResponse(call: Call<PostToken>?, response: Response<PostToken>?) {
                    val body = response?.body()
                    try {
                        if (body?.success!!) {
                            var refreshToken = body.result?.refresh_token
                            val pref: SharedPreferences = getSharedPreferences("prefs", 0)
                            val prefEdit = pref.edit()
                            prefEdit.putString("refresh_token", refreshToken).apply()

                            if(login_page_still_login_checkBox.isChecked){
                                prefEdit.putString("keep_token", refreshToken).apply()
                                Log.i("CHECK_BOX_CHECK","on")
                            }else{
                                prefEdit.putString("keep_token", "").apply()
                                Log.i("CHECK_BOX_CHECK","off")
                            }

                            intent.putExtra("refresh_token", refreshToken)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        } else {
                        }
                    }catch (e:NullPointerException){
                        if (body?.reason == "Invalid password") {
                            toast("비밀번호가 틀렸습니다.")
                        } else if (body?.reason == "user not found") {
                            toast("아이디가 틀렸습니다.")
                        }else{
                            toast("아이디, 비밀번호를 다시 한번 확인해주세요.")
                        }
                    }
                }
            })
        }




/*        test_tv.text = App.prefs.text_id
        test_tv2.text = App.prefs.text_pw


        signUpBtn.setOnClickListener {

        }*/



    }
}
