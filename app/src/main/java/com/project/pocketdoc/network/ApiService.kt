package com.project.pocketdoc.network

import com.project.pocketdoc.*
import com.project.pocketdoc.model.VisitPage
import com.project.pocketdoc.model.tables.Card
import com.project.pocketdoc.model.tables.Illness
import com.project.pocketdoc.model.tables.Pill
import com.project.pocketdoc.model.tables.Profile
import com.project.pocketdoc.network.requests.FcmRequest
import com.project.pocketdoc.network.requests.LoginRequest
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {
    @POST(LOGIN_URL)
    suspend fun login(@Body request: LoginRequest): ResponseBody<Profile>

    @GET(PROFILE_URL)
    suspend fun getProfile(@Query("fcm_id") fcmId: String): ResponseBody<Profile>

    @POST(CARD_URL)
    suspend fun createCard(@Query("fcm_id") fcmId: String, @Body card: Card): ResponseBody<Card>

    @PUT(CARD_UPDATE_URL)
    suspend fun editCard(@Path("id") cardId: Int, @Query("fcm_id") fcmId: String, @Body card: Card): ResponseBody<Card>

    @Multipart
    @POST(UPLOAD_IMAGE_URL)
    suspend fun uploadImage(@Path("id") cardId: Int, @Part photo: MultipartBody.Part?, @Part fcm: MultipartBody.Part)

    @GET(VISIT_LIST_URL)
    suspend fun getVisitPage(
        @Query("fcm_id") fcmId: String, @Query("limit") limit: Int, @Query("offset") offset: Int,
        @Query("history") history: Int, @Query("active") active: Int
    ): ResponseBody<VisitPage>

    @GET(VISIT_LIST_URL)
    suspend fun getVisitPageWithCardId(
        @Query("fcm_id") fcmId: String, @Query("limit") limit: Int, @Query("offset") offset: Int,
        @Query("history") history: Int, @Query("card_id") cardId: Int
    ): ResponseBody<VisitPage>

    @POST(VISIT_ACCEPT_URL)
    suspend fun acceptVisit(@Path("id") visitId: Int, @Body fcmRequest: FcmRequest)

    @POST(VISIT_HIDE_URL)
    suspend fun hideVisit(@Path("id") visitId: Int, @Body fcmRequest: FcmRequest)

    @POST(VISIT_FINISH_URL)
    suspend fun finishVisit(@Path("id") visitId: Int, @Body fcmRequest: FcmRequest)

    @PUT(CARD_UPDATE_URL)
    suspend fun updateCard(@Path("id") cardId: Int, @Body card: Card): ResponseBody<Card>

    @GET(ILLNESS_LIST_URL)
    suspend fun getIllnessList(
        @Query("card_id") cardId: Int,
        @Query("visit_id") visitId: Int,
        @Query("fcm_id") fcmId: String
    ): ResponseBody<List<Illness>>

    @POST(ILLNESS_CREATE_URL)
    suspend fun createIllness(@Path("id") visitId: Int, @Body illness: Illness): ResponseBody<Illness>

    @POST(ILLNESS_ID_URL)
    suspend fun updateIllness(@Path("id") illnessId: Int, @Body illness: Illness): ResponseBody<Illness>

    @HTTP(method = "DELETE", path = ILLNESS_ID_URL, hasBody = true)
    suspend fun deleteIllness(@Path("id") illnessId: Int, @Body request: FcmRequest)

    @GET(PILL_LIST_URL)
    suspend fun getPillList(
        @Query("card_id") cardId: Int,
        @Query("visit_id") visitId: Int,
        @Query("fcm_id") fcmId: String
    ): ResponseBody<List<Pill>>

    @GET(PILL_LIST_URL)
    suspend fun getPillList(@Query("card_id") cardId: Int, @Query("fcm_id") fcmId: String): ResponseBody<List<Pill>>

    @POST(PILL_LIST_URL)
    suspend fun createPill(@Body pill: Pill)

    @PUT(PILL_ID_URL)
    suspend fun updatePill(@Path("id") pillId: Int, @Body pill: Pill)

    @HTTP(method = "DELETE", path = PILL_ID_URL, hasBody = true)
    suspend fun deletePill(@Path("id") pillId: Int, @Body fcmRequest: FcmRequest)
}