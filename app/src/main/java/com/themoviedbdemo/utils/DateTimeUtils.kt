package com.themoviedbdemo.utils

import android.annotation.SuppressLint

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Created by Saveen on 21/01/18.
 * contains commonly used methods related to date & time conversion
 */
class DateTimeUtils {
    /**
     * get date form timestamp

     * @param timestamp time to be converter
     * *
     * @return date in string
     */

    fun getDateFromTimestamp(timestamp: String): String {

        val time = java.lang.Long.parseLong(timestamp) * 1000
        try {
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val netDate = Date(time)
            return sdf.format(netDate)
        } catch (ex: Exception) {
            return "xx xxxx xxxx"
        }

    }

    /**
     * To convert a date to timestamp

     * @param dateToConvert date to be converted
     * *
     * @param dateFormat    format of date entered
     * *
     * @return timestamp in milliseconds
     */

    fun convertDateToTimeStamp(dateToConvert: String, dateFormat: String): Long {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        var date: Date? = null
        try {
            date = formatter.parse(dateToConvert)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return date!!.time
    }


    /**
     * get difference between current time and provided timezone

     * @return
     */
    val timeOffset: Long
        get() {
            val currentTime = System.currentTimeMillis()
            val edtOffset = TimeZone.getTimeZone("Your Time Zone").getOffset(currentTime)
            val current = TimeZone.getDefault().getOffset(currentTime)
            return (current - edtOffset).toLong()
        }

    /**
     * Convert date from one format to another

     * @param dateToConvert date to be converted
     * *
     * @param formatFrom    the format of the date to be converted
     * *
     * @param formatTo      the format of date you want the output
     * *
     * @return date in string as per the entered formats
     */
    @SuppressLint("SimpleDateFormat")
    fun convertDateOneToAnother(dateToConvert: String, formatFrom: String, formatTo: String): String {
        var outputDateStr: String = ""
        val inputFormat = SimpleDateFormat(formatFrom)
        val outputFormat = SimpleDateFormat(formatTo)
        val date: Date
        try {
            date = inputFormat.parse(dateToConvert)
            outputDateStr = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return outputDateStr
    }

    val currentTimestamp: String
        get() {
            try {
                val sdf = SimpleDateFormat("EEEE,MMMM dd,yyyy")
                val currentDateandTime = sdf.format(Date())
                return currentDateandTime
            } catch (ex: Exception) {
                return "xxx,xxx xx,xxxx"
            }

        }

    fun getActiveJobsTimestamp(jobdate: String): Long {

        val sdf = SimpleDateFormat("EEE MM/dd")
        var strDate: Date? = null
        try {
            strDate = sdf.parse(jobdate)
            return strDate!!.time
        } catch (e: ParseException) {
            e.printStackTrace()
            return 0
        }

    }

    companion object {

        /**
         * Get current timestamp in seconds

         * @return current device time in seconds
         */
        val timeStampInSeconds: Long
            get() = System.currentTimeMillis() / 1000
    }

}
