package org.gongdam.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.form_studio_page_room_list.view.*
import org.gongdam.Json.AddFavorite
import org.gongdam.Json.FavoriteGet
import org.gongdam.Json.ResultBoolean
import org.gongdam.MainActivity
import org.gongdam.PageRoom
import org.gongdam.R
import org.gongdam.Struct.Board3String1Int
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

class RecyclerAdapterStudioPageRoomList(val context: Context, val boardList: MutableList<Board3String1Int>,val token :String, val itemClick:(Board3String1Int) -> Unit): RecyclerView.Adapter<RecyclerAdapterStudioPageRoomList.MainViewHolder>() {

    val server = MainActivity.server

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = MainViewHolder(parent)

    override fun getItemCount(): Int = boardList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(boardList[position], context)
    }

    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.form_studio_page_room_list, parent, false)) {
        val boardLayout = itemView.form_studio_page_roomList_board_layout
        val boardImage = itemView.form_studio_page_roomList_board_image
        val boardName = itemView.form_studio_page_roomList_board_room_name
        val boardContent = itemView.form_studio_page_roomList_board_room_content
        val boardLikeBtn = itemView.form_studio_page_roomList_board_like_btn
        val boardUnlikeBtn = itemView.form_studio_page_roomList_board_unlike_btn
        val boardShareBtn = itemView.form_studio_page_roomList_board_share_btn

        fun bind(board: Board3String1Int, context: Context){
            Glide.with(context).load(board.image).into(boardImage)
            boardName.text = board.name
            boardContent.text = board.content

            try {
                server?.getFavoriteByRoomWithQuery(token, board.room_id)?.enqueue(object : Callback<FavoriteGet> {
                        override fun onFailure(call: Call<FavoriteGet>?, t: Throwable?) { Log.e("GET_FAVORITE_FAILED", t.toString()) }
                        override fun onResponse(call: Call<FavoriteGet>?, response: Response<FavoriteGet>?) {
                            val result = response?.body()?.result!!
                            if (result.total == 1) {
                                boardLikeBtn.visibility = View.VISIBLE
                                boardUnlikeBtn.visibility = View.INVISIBLE
                            }
                        }
                    })
            }catch (e:NullPointerException){ }

            boardLayout.setOnClickListener {
                itemClick(board)
            }

            boardShareBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.setType("text/plain")
                intent.putExtra(Intent.EXTRA_TEXT, "url. room_id = ${board.room_id}")

                val chooser = Intent.createChooser(intent, "공유하기")
                context.startActivity(chooser)
            }
            boardLikeBtn.setOnClickListener {
                server?.deleteFavorite(token, board.room_id)?.enqueue(object:Callback<ResultBoolean> {
                    override fun onFailure(call: Call<ResultBoolean>?, t: Throwable?) { Log.e("ADD_FAVORITE_FAILED", t.toString()) }
                    override fun onResponse(call: Call<ResultBoolean>?, response: Response<ResultBoolean>?) {
                        boardUnlikeBtn.visibility = View.VISIBLE
                        boardLikeBtn.visibility = View.INVISIBLE
                    }
                })
            }
            boardUnlikeBtn.setOnClickListener {
                server?.postAddFavorite(token, board.room_id)?.enqueue(object:Callback<AddFavorite> {
                    override fun onFailure(call: Call<AddFavorite>?, t: Throwable?) { Log.e("ADD_FAVORITE_FAILED", t.toString()) }
                    override fun onResponse(call: Call<AddFavorite>?, response: Response<AddFavorite>?) {
                        boardUnlikeBtn.visibility = View.INVISIBLE
                        boardLikeBtn.visibility = View.VISIBLE
                    }
                })
            }
        }
    }
}