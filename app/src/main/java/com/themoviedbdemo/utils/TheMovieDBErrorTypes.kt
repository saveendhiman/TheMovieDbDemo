package com.themoviedbdemo.utils

/**
 * Created by Saveen on 21/01/18.
 */
enum class TheMovieDBErrorTypes(var code: Int) {
    EMAIL_ERROR(2),
    MOBILE_ERROR(3),
    NAME_ERROR(4),
    PASSWORD_ERROR(5),
    PHOTO_ERROR(6),
    PERMISSION_DENIED_ERROR(7),

}