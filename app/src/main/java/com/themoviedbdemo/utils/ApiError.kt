package com.themoviedbdemo.utils

import com.google.gson.annotations.SerializedName


class ApiError(@SerializedName("status_code")val statusCode: Int, @SerializedName("message")val message: String)