package org.gongdam.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.form_my_space_list.view.*
import kotlinx.android.synthetic.main.form_studio_page_room_list.view.*
import org.gongdam.Json.AddFavorite
import org.gongdam.Json.FavoriteGet
import org.gongdam.Json.ResultBoolean
import org.gongdam.MainActivity
import org.gongdam.PageRoom
import org.gongdam.R
import org.gongdam.Struct.BoardMySpaceList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

class RecyclerAdapterMySpace(val context: Context, val boardList: MutableList<BoardMySpaceList>,val token :String, val itemClick:(BoardMySpaceList) -> Unit): RecyclerView.Adapter<RecyclerAdapterMySpace.BaseViewHolder<*>>() {

    val server = MainActivity.server

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    inner class ViewHolder1(itemView:View):BaseViewHolder<BoardMySpaceList>(itemView){
        val boardStudioName = itemView.form_my_space_roomList_board_studio_name
        val boardRoomName = itemView.form_my_space_roomList_board_room_name
        val boardImage = itemView.form_my_space_roomList_board_image
        val boardContent = itemView.form_my_space_roomList_board_room_content
        val boardLayout = itemView.form_my_space_roomList_board_layout
        val boardLikeBtn = itemView.form_my_space_roomList_board_like_btn
        val boardUnlikeBtn = itemView.form_my_space_roomList_board_unlike_btn
        val boardShareBtn = itemView.form_my_space_roomList_board_share_btn

        override fun bind(board: BoardMySpaceList) {
            boardStudioName?.text = board.studioName
            boardRoomName?.text = board.roomName
            Glide.with(context).load(board.image).into(boardImage)
            boardContent?.text = board.content

            try {
                server?.getFavoriteByRoomWithQuery(token, board.roomId)?.enqueue(object : Callback<FavoriteGet> {
                    override fun onFailure(call: Call<FavoriteGet>?, t: Throwable?) { Log.e("GET_FAVORITE_FAILED", t.toString()) }
                    override fun onResponse(call: Call<FavoriteGet>?, response: Response<FavoriteGet>?) {
                        val result = response?.body()?.result!!
                        if (result.total == 1) {
                            boardLikeBtn.visibility = View.VISIBLE
                            boardUnlikeBtn.visibility = View.INVISIBLE
                        }
                    }
                })
            }catch (e: NullPointerException){

            }

            boardLikeBtn.setOnClickListener {
                server?.deleteFavorite(token, board.roomId)?.enqueue(object:Callback<ResultBoolean> {
                    override fun onFailure(call: Call<ResultBoolean>?, t: Throwable?) { Log.e("ADD_FAVORITE_FAILED", t.toString()) }
                    override fun onResponse(call: Call<ResultBoolean>?, response: Response<ResultBoolean>?) {
                        boardUnlikeBtn.visibility = View.VISIBLE
                        boardLikeBtn.visibility = View.INVISIBLE
                    }
                })
            }
            boardUnlikeBtn.setOnClickListener {
                server?.postAddFavorite(token, board.roomId)?.enqueue(object:Callback<AddFavorite> {
                    override fun onFailure(call: Call<AddFavorite>?, t: Throwable?) { Log.e("ADD_FAVORITE_FAILED", t.toString()) }
                    override fun onResponse(call: Call<AddFavorite>?, response: Response<AddFavorite>?) {
                        boardUnlikeBtn.visibility = View.INVISIBLE
                        boardLikeBtn.visibility = View.VISIBLE
                    }
                })
            }

            boardLayout.setOnClickListener {
                itemClick(board)
            }

            boardShareBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.setType("text/plain")
                intent.putExtra(Intent.EXTRA_TEXT, "url. room_id = ${board.roomId}")

                val chooser = Intent.createChooser(intent, "공유하기")
                context.startActivity(chooser)
            }
        }
    }

    inner class ViewHolder2(itemView: View):BaseViewHolder<BoardMySpaceList>(itemView){
        override fun bind(item: BoardMySpaceList) {
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when(viewType){
            0 -> {
                val view = LayoutInflater.from(context).inflate(R.layout.form_my_space_list, parent, false)
                ViewHolder1(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = boardList[position]
        when(holder){
            is ViewHolder1 -> holder.bind(element as BoardMySpaceList)
            is ViewHolder2 -> holder.bind(element as BoardMySpaceList)
            else -> throw IllegalArgumentException()
        }
    }


    override fun getItemCount(): Int {
        return boardList.size
    }

}

