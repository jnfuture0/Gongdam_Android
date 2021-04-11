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
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_page_my_review_edit.*
import org.gongdam.Adapter.RecyclerAdapterReviewImageList
import org.gongdam.Json.*
import org.gongdam.Struct.BoardStringInt
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PageMyReviewEdit : AppCompatActivity() {

    //var token = MainActivity.getToken(MainActivity.refToken)
    val REQUEST_GET_IMAGE = 105
    var imgList = mutableListOf<BoardStringInt>( BoardStringInt("", 1))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_my_review_edit)


        val server = MainActivity.server
        val reviewId = intent.getIntExtra("review_id", 0)
        val roomId = intent.getIntExtra("room_id", 0)
        val refToken = intent.getStringExtra("ref_token")


        val tokenMap:HashMap<String, Any> = HashMap()
        tokenMap["refresh_token"] = refToken
        server?.refreshToken(tokenMap)?.enqueue(object: Callback<RefreshToken> {
            override fun onFailure(call: Call<RefreshToken>?, t: Throwable?) { Log.e("REFRESH_TOKEN_FAILED", t.toString()) }
            override fun onResponse(call: Call<RefreshToken>?, response: Response<RefreshToken>?) {
                val token = response?.body()?.result?.access_token!!
                server.getUserFromToken(token).enqueue(object:Callback<UserGetFromToken>{
                    override fun onFailure(call: Call<UserGetFromToken>, t: Throwable) {}
                    override fun onResponse(call: Call<UserGetFromToken>, response2: Response<UserGetFromToken>) {
                        val userID = response2?.body()?.result?.id!!
                        server?.getUserReviews(userID).enqueue(object :Callback<UserReviewsGet>{
                            override fun onFailure(call: Call<UserReviewsGet>, t: Throwable) {}
                            override fun onResponse(call: Call<UserReviewsGet>, response3: Response<UserReviewsGet>) {
                                val result3 = response3?.body()?.result!!
                                if(result3.total!! > 0) {
                                    for (element in result3?.reviews!!) {
                                        if(element.id == reviewId){
                                            edit_my_review_page_studio_name.text = element.room?.studio?.name
                                            edit_my_review_page_room_name.text = element.room?.name
                                            edit_my_review_page_date.text = element.room?.studio?.address?.district
                                            if(element.room?.images?.size!! > 0) {
                                                val img = element.room?.images!![0]
                                                Glide.with(applicationContext).load(img).into(edit_my_review_page_image)
                                            }else{
                                                edit_my_review_page_image.visibility = View.GONE
                                            }
                                        }
                                    }
                                }
                            }
                        })
                    }
                })
            }
        })

        edit_my_review_page_apply_btn.setOnClickListener {
            //token = MainActivity.getToken(MainActivity.refToken)
            val infoMap: HashMap<String, Any> = HashMap()
            infoMap["content"] = edit_my_review_page_content_editText.text.toString()
            infoMap["images"] = ""
            infoMap["star"] = edit_my_review_page_ratingBar.rating.toInt()
            Log.e("CHECK_POST_REVIEW_", edit_my_review_page_content_editText.text.toString())
            Log.e("CHECK_POST_REVIEW", edit_my_review_page_ratingBar.rating.toInt().toString())

            val tokenMap:HashMap<String, Any> = HashMap()
            tokenMap["refresh_token"] = refToken
            server?.refreshToken(tokenMap)?.enqueue(object: Callback<RefreshToken> {
                override fun onFailure(call: Call<RefreshToken>?, t: Throwable?) { Log.e("REFRESH_TOKEN_FAILED", t.toString())}
                override fun onResponse(call: Call<RefreshToken>?, response: Response<RefreshToken>?) {
                    var token = response?.body()?.result?.access_token!!
                    server?.putReviews(token, reviewId, infoMap)?.enqueue(object: Callback<CreateReview> {
                        override fun onFailure(call: Call<CreateReview>?, t: Throwable?) { Log.e("GET_RESERV_SENT_FAILED", t.toString()) }
                        override fun onResponse(call: Call<CreateReview>?, response: Response<CreateReview>?) {
                            if(response?.body()?.success!!) {
                                toast("수정되었습니다.")
                                finish()
                            }
                        }
                    })
                }
            })
        }

        edit_my_review_page_img_recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)

        var reviewImgAdapter = RecyclerAdapterReviewImageList(this, imgList){
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_GET_IMAGE)
        }
        edit_my_review_page_img_recyclerView.adapter = reviewImgAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e("CHECK_URI_PATH", "?")
        if(resultCode == Activity.RESULT_OK){
            Log.e("CHECK_URI_PATH", "??")
            when(requestCode){
                REQUEST_GET_IMAGE -> {
                    Log.e("CHECK_URI_PATH", "???")
                    try{
                        Log.e("CHECK_URI_PATH", "????")
                        var uri = data?.data!!
                        Log.e("CHECK_URI_PATH", "?????")
                        //Log.e("CHECK_URI_PATH", getRealPathFromURI(this, uri))

                        /*finalBoardInner.image = uri.toString()
                        userAddImg.setImageURI(uri)
                        userAddImg.tag = uri.toString()*/
                        imgList.add(BoardStringInt(uri.toString(), 0))
                        edit_my_review_page_img_recyclerView.adapter?.notifyDataSetChanged()
                    }catch (e:Exception){}
                }
            }
        }
    }

    fun getRealPathFromURI(context: Context, uri: Uri):String{
        var filePath = ""
        var wholeID = DocumentsContract.getDocumentId(uri)

        var id = wholeID.split(":")[1]

        var column :Array<String> = arrayOf(MediaStore.Images.Media.DATA)

        var sel = MediaStore.Images.Media._ID + "=?"

        val idArr:Array<String> = arrayOf(id)

        var cursor : Cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,
            sel, idArr, null)!!

        var columnIndex = cursor.getColumnIndex(column[0])

        if(cursor.moveToFirst()){
            filePath = cursor.getString(columnIndex)
        }

        cursor.close()

        return filePath
    }
}
