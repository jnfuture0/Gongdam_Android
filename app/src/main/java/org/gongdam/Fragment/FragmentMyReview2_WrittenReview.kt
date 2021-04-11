package org.gongdam.Fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_fragment_my_review2.*
import org.gongdam.Adapter.RecyclerAdapterMyReview2
import org.gongdam.Json.*
import org.gongdam.MainActivity
import org.gongdam.PageMyReview
import org.gongdam.PageMyReviewEdit
import org.gongdam.R
import org.gongdam.Struct.BoardMyReview2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentMyReview2_WrittenReview : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    var boardList:MutableList<BoardMyReview2> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_fragment_my_review2, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
        myReview2_swipeRefreshLayout.setOnRefreshListener(this)
        myReview2_listView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun init(){
        var refToken = PageMyReview.refToken
        val server = MainActivity.server

        val tokenMap:HashMap<String, Any> = HashMap()
        tokenMap["refresh_token"] = refToken
        server?.refreshToken(tokenMap)?.enqueue(object: Callback<RefreshToken> {
            override fun onFailure(call: Call<RefreshToken>?, t: Throwable?) { Log.e("REFRESH_TOKEN_FAILED", t.toString())}
            override fun onResponse(call: Call<RefreshToken>?, response: Response<RefreshToken>?) {
                var token = response?.body()?.result?.access_token!!
                server?.getUserFromToken(token)?.enqueue(object: Callback<UserGetFromToken> {
                    override fun onFailure(call: Call<UserGetFromToken>?, t: Throwable?) { Log.e("GET_RESERV_SENT_FAILED", t.toString()) }
                    override fun onResponse(call: Call<UserGetFromToken>?, response: Response<UserGetFromToken>?) {
                        var user_id = response?.body()?.result?.id!!
                        server?.getUserReviews(user_id)?.enqueue(object: Callback<UserReviewsGet> {
                            override fun onFailure(call: Call<UserReviewsGet>?, t: Throwable?) { Log.e("GET_RESERV_SENT_FAILED", t.toString()) }
                            override fun onResponse(call: Call<UserReviewsGet>?, response: Response<UserReviewsGet>?) {
                                val result = response?.body()?.result?.reviews!!

                                boardList = mutableListOf()
                                for(element in result){
                                    var image = ""
                                    var st_name = ""
                                    var room_name = ""
                                    var review_rating = 0f
                                    var review_date = ""
                                    var review_content = ""
                                    var reply_date = ""
                                    var reply_content = ""
                                    var is_reply = false
                                    var roomID = element.room?.id!!
                                    var is_img : Boolean = false
                                    val reviewId = element.id!!

                                    if(element.images!!.isNotEmpty()){
                                        image = element.images!![0]
                                        is_img = true
                                    }
                                    st_name = element.room?.studio?.name!!
                                    room_name = element.room?.name!!
                                    review_rating = element.star!!.toFloat()
                                    review_date = element.created_at!!.substring(0,10)
                                    review_content = element.content!!

                                    boardList.add(BoardMyReview2(image,st_name, room_name, review_rating, review_date, review_content, reply_date, reply_content, is_reply, reviewId, roomID, is_img))
                                }

                                //boardList에 추가한 후
                                var boardListAdapter = RecyclerAdapterMyReview2(this@FragmentMyReview2_WrittenReview.requireContext(), boardList){
                                    val builder = AlertDialog.Builder(this@FragmentMyReview2_WrittenReview.requireContext())
                                    val dialogView = layoutInflater.inflate(R.layout.dialog_review_manage, null)
                                    val dialogBtnEdit = dialogView.findViewById<Button>(R.id.dialog_review_manage_editBtn)
                                    val dialogBtnDelete = dialogView.findViewById<Button>(R.id.dialog_review_manage_deleteBtn)
                                    val dialogBtnCancel = dialogView.findViewById<Button>(R.id.dialog_review_manage_cancelBtn)
                                    val st_name = it.studio_name
                                    val reviewId = it.review_id
                                    val roomID = it.room_id

                                    /*dialogView.setBackgroundDrawable(R.drawable.style_radius10_solid_9f9f9f)
                                    dialogView.*/
                                    //dialogView.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                    //dialogView.setBackgroundResource(R.drawable.style_radius10_solid_9f9f9f)
                                    //dialogView.setBackgroundResource(android.R.color.transparent)

                                    builder.setView(dialogView)
                                    val ad : AlertDialog = builder.create()
                                    ad.show()

                                    dialogBtnEdit.setOnClickListener {
                                        activity?.let{ it2 ->
                                            val intent = Intent(it2, PageMyReviewEdit::class.java)
                                            intent.putExtra("review_id", reviewId)
                                            intent.putExtra("room_id", roomID)
                                            intent.putExtra("ref_token", refToken)
                                            it2.startActivity(intent)
                                            ad.dismiss()
                                        }
                                    }

                                    dialogBtnDelete.setOnClickListener {
                                        val builderDelete = AlertDialog.Builder(context)
                                        val dialogViewDelete = layoutInflater.inflate(R.layout.dialog_ask_again, null)
                                        val yesBtn = dialogViewDelete.findViewById<Button>(R.id.dialog_ask_again_yes_btn)
                                        val noBtn = dialogViewDelete.findViewById<Button>(R.id.dialog_ask_again_no_btn)
                                        val dialogTxt = dialogViewDelete.findViewById<TextView>(R.id.dialog_ask_again_text)
                                        builderDelete.setView(dialogViewDelete)
                                        val ad : AlertDialog = builderDelete.create()
                                        dialogTxt.text = "정말 삭제하시겠습니까?"
                                        ad.show()

                                        yesBtn.setOnClickListener {
                                            server.deleteReviews(token, reviewId).enqueue(object: Callback<DeleteReview> {
                                                override fun onFailure(call: Call<DeleteReview>?, t: Throwable?) { Log.e("DELETE_REVIEW_FAILED", t.toString()) }
                                                override fun onResponse(call: Call<DeleteReview>?, response: Response<DeleteReview>?) {
                                                    if(response?.body()?.success!!) {
                                                        Toast.makeText(context,"삭제되었습니다",Toast.LENGTH_SHORT).show()
                                                        ad.dismiss()
                                                    }
                                                }
                                            })
                                        }
                                        noBtn.setOnClickListener {
                                            ad.dismiss()
                                        }
                                    }

                                    dialogBtnCancel.setOnClickListener {
                                        //Toast.makeText(context,"cancel Button, studio name = $st_name",Toast.LENGTH_SHORT).show()
                                        ad.dismiss()
                                    }

                                }
                                myReview2_listView.adapter = boardListAdapter
                            }
                        })
                    }
                })
            }
        })
    }

    override fun onRefresh() {
        init()
        myReview2_swipeRefreshLayout.isRefreshing = false
    }
}
