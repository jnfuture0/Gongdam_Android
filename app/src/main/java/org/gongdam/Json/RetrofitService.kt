package org.gongdam.Json

import android.content.res.Resources
import android.media.Image
import android.provider.MediaStore
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File
import java.net.URL

interface RetrofitService {
    @GET("/oauth/authorize")
    fun kakaoLogin(@Query("client_id")client_id: String,
                   @Query("redirect_uri")redirect_uri:String,
                   @Query("response_type")response_type:String)


    @Headers("Content-Type: application/json")
    @POST("/users")
    fun postUser(@Body ac_pw_na_ema_pho_add3_img:HashMap<String,Any>) : Call<PostUser>

    @Headers("Content-Type: application/json")
    @POST("/tokens")
    fun postToken(@Body account_pw:HashMap<String,String>):Call<PostToken>

    @Headers("Content-Type: application/javascript")
    @PUT("/tokens")
    fun refreshToken(@Body refreshTokenString: HashMap<String, Any>):Call<RefreshToken>


    @GET("/tokens")
    fun getUserFromToken(@Header("Authorization")Authorization : String):Call<UserGetFromToken>

    @Headers("Content-Type: application/json")
    @PUT("/users/{user_id}")
    fun modifyUser(@Header("Authorization")Authorization : String,
                   @Path("user_id")user_id:Int,
                   @Body opw_pw_name_email_phone_image: HashMap<String, Any>):Call<UserModify>

    @GET("/rooms/{room_id}")
    fun getRoomWithToken(@Path("room_id")room_id:Int,
                         @Header("Authorization")authorization:String ): Call<RoomGet>

    /*token 필요 없음*/
    @GET("/rooms/{room_id}")
    fun getRoomWithOutToken(@Path("room_id")room_id:Int): Call<RoomGet>

    @GET("/rooms/{room_id}/reviews")
    fun getRoomReview(@Path("room_id")room_id:Int) : Call<RoomReviewGet>

    @GET("/rooms")
    fun getRoomList() : Call<RoomListGet>

    @GET("/rooms")
    fun getRoomListSize(@Query("size") size:Int? = null) : Call<RoomListGet>


    @GET("/rooms")
    fun getRoomListWithFilter(@Query("post_code_starts")p_code:Any? = null,
                              @Query("filter_ids")filter_ids:Any? = null): Call<RoomListGet>

    @Headers("Content-Type: application/json")
    @GET("/filters")
    fun getFilterList() : Call<FilterListGet>


    @GET("/promotions/{promotion_id}")
    fun getPromotion(@Path("promotion_id")promotion_id:Int) : Call<PromotionGet>

    @GET("/promotions")
    fun getPromotionList() : Call<PromotionListGet>

    @GET("/promotions")
    fun getPromotionListWithTimeRoomID(@Query("room_id")room_id:Int? = null,
                                   @Query("started_at")started_at:Any? = null,
                                   @Query("ended_at")ended_at:Any? = null):Call<PromotionListGet>


    @GET("/promotions")
    fun getPromotionListWithFilter(@Query("post_code_starts")p_code:Any? = null,
                                   @Query("started_at")started_at:Any? = null,
                                   @Query("ended_at")ended_at:Any? = null):Call<PromotionListGet>

    @GET("/promotions")
    fun getPromotionListSize(@Query("size")size:Int) : Call<PromotionListGet>



    @GET("/studios/{studio_id}")
    fun getStudio(@Path("studio_id")studio_id:Int) : Call<StudioGet>

    @GET("/studios/{studio_id}/rooms")
    fun getStudioGetRooms(@Path("studio_id")studio_id:Int) : Call<StudioGetRoomsGet>

    @GET("/studios/{studio_id}/reviews")
    fun getStudioReviews(@Path("studio_id")studio_id:Int): Call<StudioReviewsGet>




    @GET("/users/self/favorites/rooms")
    fun getFavoriteByRoomWithQuery(@Header("Authorization") Authorization : String,
                                   @Query("room_ids")room_ids:Int) : Call<FavoriteGet>

    @GET("/users/self/favorites/rooms")
    fun getFavoriteByRoom(@Header("Authorization") Authorization : String) : Call<FavoriteGet>

    @POST("/users/self/favorites/rooms/{room_ids}")
    fun postAddFavorite(@Header("Authorization") Authorization : String ,
                        @Path("room_ids") room_ids:Int) : Call<AddFavorite>

    @DELETE("/users/self/favorites/rooms/{room_id}")
    fun deleteFavorite(@Header("Authorization") Authorization : String ,
                       @Path("room_id") room_id: Int) : Call<ResultBoolean>


    @GET("/recents/rooms")
    fun getRecentRoom(@Header("Authorization") Authorization : String):Call<RecentRoomsGet>

    @GET("/reservations/sent")
    fun getReservationSent(@Header("Authorization")Authorization : String):Call<ReservSentGet>


    @GET("/users/{user_id}/reviews")
    fun getUserReviews(@Path("user_id")user_id:Int):Call<UserReviewsGet>



    @POST("/reviews")
    fun postReviews(@Header("Authorization")Authorization : String,
                    @Body rid_content_images_star: HashMap<String, Any>) : Call<CreateReview>


    @Headers("Content-Type: application/json")
    @PUT("/reviews/{review_id}")
    fun putReviews(@Header("Authorization")Authorization : String,
                   @Path("review_id")reviewId:Int,
                   @Body cont_imgs_star: HashMap<String, Any>):Call<CreateReview>

    @DELETE("/reviews/{review_id}")
    fun deleteReviews(@Header("Authorization")Authorization : String,
                      @Path("review_id")reviewId:Int):Call<DeleteReview>


    @GET("/trends/hot/keywords")
    fun getHotKeys():Call<HotKeysGet>


    @Headers("Content-Type: application/json")
    @POST("/reservations/expected_price")
    fun getExpectedPrice(@Body rid_adp_equip_adf_cpn_st_en:HashMap<String, Any>):Call<ExpectedPriceGet>

    @Headers("Content-Type: application/json")
    @PUT("/reservations/{reservNum}/status")
    fun modifyReservStatus(@Header("Authorization") Authorization : String ,
                           @Path("reservNum")reservNum:Int,
                           @Body status:HashMap<String, Any>):Call<ReservSentGet>

    @Multipart
    @Headers("Content-Type: application/json")
    @POST("/images")
    fun uploadImage(@Header("Authorization") Authorization : String,
                    @Part file:MultipartBody.Part)
}