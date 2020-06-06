package com.project.pocketdoc

const val BASE_URL = "http://31.131.18.129/"
const val LOGIN_URL = "api/auth/login"
const val PROFILE_URL = "api/cabinet/profile"
const val VISIT_LIST_URL = "api/visit"
const val VISIT_FINISH_URL = "$VISIT_LIST_URL/{id}/finish"
const val VISIT_ACCEPT_URL = "$VISIT_LIST_URL/{id}/accept"
const val VISIT_HIDE_URL = "$VISIT_LIST_URL/{id}/skip"
const val CARD_URL = "api/card"
const val CARD_UPDATE_URL = "$CARD_URL/{id}"
const val UPLOAD_IMAGE_URL = "$CARD_UPDATE_URL/photo"
const val ILLNESS_LIST_URL = "api/illnesses"
const val ILLNESS_CREATE_URL = "$VISIT_LIST_URL/{id}/make_illness"
const val ILLNESS_ID_URL = "$ILLNESS_LIST_URL/{id}"
const val PILL_LIST_URL = "api/medicine"
const val PILL_ID_URL = "$PILL_LIST_URL/{id}"

fun imageDownloadUrl(cardId: Int, fcmId: String) = "$BASE_URL/$CARD_URL/$cardId/photo?fcm_id=$fcmId"