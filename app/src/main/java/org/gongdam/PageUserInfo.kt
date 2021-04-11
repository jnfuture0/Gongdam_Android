package org.gongdam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_page_user_info.*
import org.gongdam.Json.RefreshToken
import org.gongdam.Json.UserGetFromToken
import org.gongdam.Json.UserModify
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PageUserInfo : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_user_info)

        var refToken = intent.getStringExtra("ref_token")



        val server = MainActivity.server
        //val token = intent.getStringExtra("token")
        var userID = 0
        var userEmail = ""


        val tokenMap:HashMap<String, Any> = HashMap()
        tokenMap["refresh_token"] = refToken
        MainActivity.server?.refreshToken(tokenMap)?.enqueue(object: Callback<RefreshToken> {
            override fun onFailure(call: Call<RefreshToken>?, t: Throwable?) { Log.e("REFRESH_TOKEN_FAILED", t.toString())}
            override fun onResponse(call: Call<RefreshToken>?, response: Response<RefreshToken>?) {
                var token = response?.body()?.result?.access_token!!
                server?.getUserFromToken(token)?.enqueue(object: Callback<UserGetFromToken> {
                    override fun onFailure(call: Call<UserGetFromToken>?, t: Throwable?) { Log.e("GET_USER_FAILED", t.toString()) }
                    override fun onResponse(call: Call<UserGetFromToken>?, response: Response<UserGetFromToken>?) {
                        var result = response?.body()?.result!!
                        userInfo_page_email_textView.text = result.account
                        userID = result.id!!
                        userEmail = result.email!!
                    }
                })
            }
        })


        userInfo_page_confirm_btn.setOnClickListener {
            val originPW = userInfo_page_origin_pwET.text.toString()
            val newPW = userInfo_page_pwET.text.toString()
            val newPW2 = userInfo_page_pwET2.text.toString()
            val nickname = userInfo_page_nicknameET.text.toString()
            if(!newPW.equals(newPW2)){
                toast("비밀번호와 비밀번호 확인이 일치하지 않습니다.")
            }else {
                val infoMap: HashMap<String, Any> = HashMap()
                infoMap["origin_password"] = originPW
                infoMap["new_password"] = newPW
                infoMap["name"] = nickname
                infoMap["email"] = userEmail
                infoMap["phone"] = ""
                infoMap["image_url"] = ""

                val tokenMap:HashMap<String, Any> = HashMap()
                tokenMap["refresh_token"] = refToken
                MainActivity.server?.refreshToken(tokenMap)?.enqueue(object: Callback<RefreshToken> {
                    override fun onFailure(call: Call<RefreshToken>?, t: Throwable?) { Log.e("REFRESH_TOKEN_FAILED", t.toString())}
                    override fun onResponse(call: Call<RefreshToken>?, response: Response<RefreshToken>?) {
                        var token = response?.body()?.result?.access_token!!
                        server?.modifyUser(token, userID, infoMap)?.enqueue(object : Callback<UserModify> {
                            override fun onFailure(call: Call<UserModify>?, t: Throwable?) { Log.e("PUT_USER_FAILED", t.toString()) }
                            override fun onResponse(call: Call<UserModify>?, response: Response<UserModify>?) {
                                if(response?.body()?.reason == "Invalid password"){
                                    toast("비밀번호가 틀립니다.")
                                }else{
                                    toast("회원 정보가 변경되었습니다.")
                                    finish()
                                }
                            }
                        })
                    }
                })

            }
        }
    }
}
