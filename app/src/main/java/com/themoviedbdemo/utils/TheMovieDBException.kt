package com.themoviedbdemo.utils

/**
 * Created by Saveen on 21/01/18.
 */
class TheMovieDBException(var code: Int, var serverError: String = "Unknown Error") : RuntimeException() {

    fun getLocalisedString(): String {

        return when (code) {
            TheMovieDBErrorTypes.EMAIL_ERROR.code -> "EMAIL not valid"
            TheMovieDBErrorTypes.MOBILE_ERROR.code -> "Mobile number not valid"
            TheMovieDBErrorTypes.NAME_ERROR.code -> "Name can not be blank"
            TheMovieDBErrorTypes.PASSWORD_ERROR.code -> "Password can not be blank"
            else -> {
                return serverError
            }
        }
    }
}
