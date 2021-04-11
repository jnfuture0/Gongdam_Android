package org.gongdam

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_page_my_review_write.*
import org.gongdam.Adapter.RecyclerAdapterReviewImageList
import org.gongdam.Json.CreateReview
import org.gongdam.Json.RefreshToken
import org.gongdam.Json.ReservSentGet
import org.gongdam.Struct.BoardStringInt
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PageMyReviewWrite : AppCompatActivity() {

    //var token = MainActivity.getToken(MainActivity.refToken)
    val REQUEST_GET_IMAGE = 105
    var imgList = mutableListOf<BoardStringInt>( BoardStringInt("", 1))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_my_review_write)


        val server = MainActivity.server
        val reservID = intent.getIntExtra("reservation_id", 0)
        var refToken = intent.getStringExtra("ref_token")

        val tokenMap:HashMap<String, Any> = HashMap()
        tokenMap["refresh_token"] = refToken
        server?.refreshToken(tokenMap)?.enqueue(object: Callback<RefreshToken> {
            override fun onFailure(call: Call<RefreshToken>?, t: Throwable?) { Log.e("REFRESH_TOKEN_FAILED", t.toString())}
            override fun onResponse(call: Call<RefreshToken>?, response: Response<RefreshToken>?) {
                var token = response?.body()?.result?.access_token!!
                /*이미지, 스튜디오 이름, 룸 이름, 날짜, 시간*/
                server.getReservationSent(token)?.enqueue(object: Callback<ReservSentGet> {
                    override fun onFailure(call: Call<ReservSentGet>?, t: Throwable?) { Log.e("GET_RESERV_SENT_FAILED", t.toString()) }
                    override fun onResponse(call: Call<ReservSentGet>?, response: Response<ReservSentGet>?) {
                        val result = response?.body()?.result

                        for(element in result!!){
                            if(element.id == reservID){
                                val image = element.room?.images?.get(0)
                                val studioName = element.room?.studio?.name
                                val roomName = element.room?.name
                                val startDate = element.started_at
                                val endDate = element.ended_at
                                val date = " " + startDate!!.substring(0,10)
                                val startTime = startDate!!.substring(11,16)
                                val endTime = endDate!!.substring(11,16)
                                val time = "$startTime ~ $endTime"
                                Glide.with(applicationContext).load(image).into(write_my_review_page_image)
                                write_my_review_page_studio_name.text = studioName
                                write_my_review_page_room_name.text = roomName
                                write_my_review_page_date.text = date
                                write_my_review_page_time.text = time
                            }
                        }
                    }
                })
            }
        })

        write_my_review_page_apply_btn.setOnClickListener {
            //token = MainActivity.getToken(MainActivity.refToken)
            val infoMap: HashMap<String, Any> = HashMap()
            infoMap["room_id"] = reservID
            infoMap["content"] = write_my_review_page_content_editText.text.toString()
            infoMap["images"] = ""
            infoMap["star"] = write_my_review_page_ratingBar.rating.toInt()
            /*Log.e("CHECK_POST_REVIEW_", reservID.toString())
            Log.e("CHECK_POST_REVIEW", write_my_review_page_content_editText.text.toString())
            Log.e("CHECK_POST_REVIEW", write_my_review_page_ratingBar.rating.toInt().toString())*/

            val tokenMap2:HashMap<String, Any> = HashMap()
            tokenMap2["refresh_token"] = refToken
            MainActivity.server?.refreshToken(tokenMap2)?.enqueue(object: Callback<RefreshToken> {
                override fun onFailure(call: Call<RefreshToken>?, t: Throwable?) { Log.e("REFRESH_TOKEN_FAILED", t.toString())}
                override fun onResponse(call: Call<RefreshToken>?, response: Response<RefreshToken>?) {
                    var token = response?.body()?.result?.access_token!!
                    server?.postReviews(token,infoMap)?.enqueue(object: Callback<CreateReview> {
                        override fun onFailure(call: Call<CreateReview>?, t: Throwable?) { Log.e("POST_REVIEW_FAILED", t.toString()) }
                        override fun onResponse(call: Call<CreateReview>?, response: Response<CreateReview>?) {
                            Log.e("CHECK_POST_REVIEW", response?.body()?.success.toString())
                            Log.e("CHECK_POST_REVIEW", response?.body()?.reason.toString())
                            if(response?.body()?.success!!) {
                                toast("저장되었습니다.")
                                finish()
                            }
                        }
                    })
                }
            })

        }

        write_my_review_page_img_recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        var reviewImgAdapter = RecyclerAdapterReviewImageList(this, imgList){
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_GET_IMAGE)
        }
        write_my_review_page_img_recyclerView.adapter = reviewImgAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_GET_IMAGE -> {
                    try{
                        var uri = data?.data!!
                        Log.e("CHECK_URI_PATH", getRealPathFromURI(this, uri))

                        /*finalBoardInner.image = uri.toString()
                        userAddImg.setImageURI(uri)
                        userAddImg.tag = uri.toString()*/
                        imgList.add(BoardStringInt(uri.toString(), 0))
                        write_my_review_page_img_recyclerView.adapter?.notifyDataSetChanged()
                    }catch (e:Exception){}
                }
            }
        }
    }

    fun getRealPathFromURI(context:Context, uri:Uri):String{
        var filePath = ""
        var wholeID = DocumentsContract.getDocumentId(uri)

        var id = wholeID.split(":")[1]

        var column :Array<String> = arrayOf(MediaStore.Images.Media.DATA)

        var sel = MediaStore.Images.Media._ID + "=?"

        val idArr:Array<String> = arrayOf(id)

        var cursor : Cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,
        sel, idArr, null)!!

        var columnIndex = cursor.getColumnIndex(column[0])

        if(cursor.moveToFirst()){
            filePath = cursor.getString(columnIndex)
        }

        cursor.close()

        return filePath
    }

}
