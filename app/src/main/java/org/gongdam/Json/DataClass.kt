package org.gongdam.Json

data class DataClass(var id:Int? = null)

data class PostUser(var success: Boolean?, var result:Int?, var reason: String?)


data class PostToken(var success: Boolean? = null, var result:Token2? = null, var reason: String? = null)
data class Token2(var access_token: String?, var refresh_token:String?)

data class RefreshToken(var success: Boolean?, var result:RefreshTokenResult?, var reason: String?)
data class RefreshTokenResult(var access_token:String?)



data class UserGetFromToken(var success: Boolean? = null, var result:User9? = null, var reason: String? = null)
/*id account name email phone address image_url favorite_total studio_total*/
data class User9(var id: Int? = null, var account:String? = null, var name: String? = null, var email: String? = null, var phone:String? = null, var address: Address3? = null, var image_url: String? = null, var favorite_total: Int? = null, var studio_total : Int? = null)


data class UserModify(var success: Boolean?, var result:User9, var reason: String?)



data class RoomGet(var success:Boolean? = null, var result:RoomGetResult? = null, var reason :String? = null)
/*id studio user tags price additional_fares name description images equipments extra grade is_verified created_at modified_at filters favorite_total review_total star_total star_average*/
data class RoomGetResult(var id: Int? = null, var studio:RoomGetResultStudio?=null, var user: User3?=null,
                         var tags:List<String>? = null, var price: Price4?=null, var additional_fares: List<RoomGetResultAdditionalFares>? = null,
                         var name: String?=null, var description: String?=null, var images: List<Any>? = null, var equipments:List<Equipments4>? = null,
                         var extra:Any?=null, var grade:Int?=null, var is_verified:Boolean? = null, var created_at : String? = null, var modified_at :String? = null, var filters: List<Filter3>? = null,
                         var favorite_total: Int? = null, var review_total:Int? = null, var star_total:Int? = null, var star_average:Double? = null)
data class Filter3(var id:Int?=null, var name : String?=null, var category:String?=null)
/*id name description images address coordinate business_time staffs extra created_at modified_at*/
data class RoomGetResultStudio(var id: Int? = null, var name:String? = null, var description: String? = null, var images: List<String>? = null,
                               var address:Address3? = null, var coordinate:Coordinate2? = null, var business_time:BusinessTime2? = null,
                               var staffs:List<Staff2>? = null, var extra:Any?=null, var created_at: String? = null, var modified_at: String? = null)
data class User3(var id: Int? = null, var name: String? = null, var email:String? = null)

data class Price4(var per_hour:Int? = null, var additional_per_person:Int? = null, var default_user_amount:Int? = null, var default_reservation_minute:Int? = null)

data class RoomGetResultAdditionalFares(var name:String? = null, var price:AdditionalFaresPrice3? = null, var etc:String? = null)

data class AdditionalFaresPrice3(var amount:Float? = null, var unit_hour:Int? = null, var max_value_amount:Float?=null)

data class Equipments4(var id: Int? = null, var information:String? = null, var price:AdditionalFaresPrice3? = null, var details:List<String>? = null)

data class Address3(var post_code:String? = null, var district:String? = null, var detail:String? = null)

data class Coordinate2(var longitude:Float? = null, var latitude:Float? = null)

data class BusinessTime2(var start:Int? = null, var end:Int? = null)

data class Staff2(var name:String? = null, var image_url:String? = null)


data class RoomReviewGet(var success:Boolean? = null, var result:RoomReviewGetResult? = null, var reason: String? = null)
data class RoomReviewGetResult(var total:Int? = null, var reviews:List<RoomReviewGetResultReviews>? = null)

/*id user room content images star created_at modified_at*/
data class RoomReviewGetResultReviews(var id:Int? = null, var user:User3? = null, var room:RoomReviewGetResultReviewsRoom? = null,
                                      var content:String? = null, var images:List<String>? = null, var star:Int? = null, var created_at: String? = null, var modified_at: String? = null)

/*id name description tags price images created_at modified_at studio*/
data class RoomReviewGetResultReviewsRoom(var id: Int? = null, var name: String? = null, var description: String? = null, var tags: List<String>? = null,
                                          var price: Price4? = null, var images: List<String>? = null, var created_at: String? = null, var modified_at: String? = null,
                                          var studio:RoomReviewGetResultReviewsRoomStudio? = null)

/*id name description images address coordinate staffs created_at modified_at*/
data class RoomReviewGetResultReviewsRoomStudio(var id: Int? = null, var name: String? = null, var description: String? = null, var images: List<String>? = null,
                                                var address: Address3? = null, var coordinate: Coordinate2? = null, var staffs: List<Staff2>? = null, var created_at: String? = null, var modified_at: String? = null)


data class RoomListGet(var success:Boolean? = null, var result: List<RoomGetResult>?=null, var reason: String? = null)



data class FilterListGet(var success:Boolean?=null, var result:List<FilterListGetResult>? = null, var reason: String?=null)
data class FilterListGetResult(var id:Int?=null, var name: String?=null, var category: String?=null)




data class PromotionGet(var success: Boolean?, var result:PromotionListGetResult?, var reason: String?)

data class PromotionListGet(var success: Boolean?, var result:List<PromotionListGetResult>?, var reason: String?)

/*id user room price started_at ended_at created_at modified_at*/
data class PromotionListGetResult(var id: Int?, var user: User3?, var room: PromotionListGetResultRoom?, var price: Price4?, var started_at: String?, var ended_at: String?,
                                  var created_at: String?, var modified_at: String?)

/*id name description tags price images equipments is_verified created_at modified_at extra grade additional_fares studio favorite_total review_total star_total star_average*/
data class PromotionListGetResultRoom(var id: Int?, var name: String?, var description: String?, var tags: List<String>?, var price: Price4?,
                                      var images: List<String>?, var equipments: List<Equipments4>?, var is_verified: Boolean?, var created_at: String?,
                                      var modified_at: String?, var extra: Any?, var grade: Int?, var additional_fares: List<RoomGetResultAdditionalFares>?, var studio: PromotionListGetResultRoomStudio?, var filters: List<Filter3>?,
                                      var favorite_total: Int?, var review_total: Int?, var star_total: Int?, var star_average: Double?)

/*id name description images address coordinate business_time staffs created_at modified_at*/
data class PromotionListGetResultRoomStudio(var id: Int? = null, var name:String? = null, var description: String? = null, var images: List<String>? = null,
                                            var address:Address3? = null, var coordinate:Coordinate2? = null, var business_time:BusinessTime2? = null,
                                            var staffs:List<Staff2>? = null, var created_at: String? = null, var modified_at: String? = null)




data class StudioGet(var success: Boolean?, var result: StudioGetResult?, var reason: String?)
/*id user name description images address coordinate business_time staffs extra created_at modified_at rooms favorite_total review_total star_average*/
data class StudioGetResult(var id: Int?, var user: User3?, var name: String?, var description: String?, var images: List<String>?, var address: Address3?, var coordinate: Coordinate2?, var business_time: BusinessTime2?,
                           var staffs: List<Staff2>?, var extra: Any?, var created_at: String?, var modified_at: String?, var rooms : List<StudioGetResultRooms>, var favorite_total: Int?, var review_total: Int?, var star_average: Double?)
/*id name description tags price images equipments is_verified extra grade created_at modified_at favorite_total review_total star_average*/
data class StudioGetResultRooms(var id: Int?, var name: String?, var description: String?, var tags: List<String>?, var price: Price4?,
                                var images: List<String>?, var equipments: List<Equipments4>?, var is_verified: Boolean?, var extra: Any?, var grade: Int?, var created_at: String?,
                                var modified_at: String?, var favorite_total: Int?, var review_total: Int?, var star_average: Double?)

data class StudioGetRoomsGet(var success: Boolean?, var result:List<RoomGetResult>?, var reason: String?)

data class StudioReviewsGet(var success: Boolean?, var result : StudioReviewsGetResult?, var reason: String?)
data class StudioReviewsGetResult(var total: Int?, var reviews: List<RoomReviewGetResultReviews>?)


data class FavoriteGet(var success: Boolean?, var result:FavoriteGetResult, var reason: String?)
data class FavoriteGetResult(var total:Int?, var favorites:List<FavoriteGetResultFavorites>)
data class FavoriteGetResultFavorites(var id: Int?, var user: User3?, var room: FavoriteGetResultRoom?)
/*id name description tags price images equipments is_verified created_at modified_at studio review_total star_average*/
data class FavoriteGetResultRoom(var id: Int?, var name: String?, var description: String?, var tags: List<String>?, var price: Price4?,
                                 var images: List<String>?, var equipments: List<Equipments4>?, var is_verified: Boolean?, var created_at: String?,
                                 var modified_at: String?, var studio: RoomReviewGetResultReviewsRoomStudio?, var review_total: Int?, var star_average: Double?)


data class AddFavorite(var success: Boolean?, var result:AddFavoriteResult, var reason: String?)
data class AddFavoriteResult(var room_id:Int?, var added_at:String?)

data class ResultBoolean(var success: Boolean?, var result: Boolean?, var reason: String?)


data class RecentRoomsGet(var success: Boolean?, var result:List<RoomGetResult>?, var reason: String?)


data class Price5(var room_price:Int?, var additional_person_price:Int?, var additional_fare_price:Int?, var discount_price:Int?, var equipment_price:Int?)


data class ReservSentGet(var success: Boolean?, var result:List<ReservSentGetResult>?, var reason: String?)
/*id user room require_equipments additional_person require_additional_fares prices coupon_ids started_at ended_at status created_at modified_at promotions*/
data class ReservSentGetResult(var id: Int?, var user: User3?, var room:ReservSentGetResultRoom?, var required_equipments:List<Any>?, var additional_person:Int?,
                               var require_additional_fares:List<Any>?, var prices:Price5?, var coupon_ids:List<Int>?, var started_at: String?, var ended_at: String?, var status:Int?,
                               var created_at: String?, var modified_at: String?, var promotions: List<Any>?)
/*id name description tags price images equipments is_verified created_at modified_at studio*/
data class ReservSentGetResultRoom(var id: Int?, var name: String?, var description: String?, var tags: List<String>?, var price: Price4?,
                                   var images: List<String>?, var equipments: List<Equipments4>?, var is_verified: Boolean?, var created_at: String?,
                                   var modified_at: String?, var studio:RoomReviewGetResultReviewsRoomStudio?)



data class UserReviewsGet(var success: Boolean?, var result:UserReviewsGetResult?, var reason: String?)
/*total reviews*/
data class UserReviewsGetResult(var total: Int?, var reviews: List<RoomReviewGetResultReviews>?)


data class CreateReview(var success: Boolean?, var result: RoomReviewGetResultReviews?, var reason: String?)

data class DeleteReview(var success: Boolean?, var result:Any?, var reason: String?)

data class HotKeysGet(var success: Boolean?, var result: List<String>?, var reason: String?)

data class ExpectedPriceGet(var success: Boolean?, var result:ExpectedPriceGetResult?, var reason: String?)
data class ExpectedPriceGetResult(var room_price: Int?, var promotions:List<Any>?, var additional_person: Int?, var additional_person_price: Int?,
                                  var require_equipments:List<Equipments4>?, var require_additional_fares: List<RoomGetResultAdditionalFares>?,
                                  var discount_price: Int?, var additional_fare_price: Int?, var equipment_price: Int?)