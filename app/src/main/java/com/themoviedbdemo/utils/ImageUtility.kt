package com.themoviedbdemo.utils

/**
 * Created by Saveen on 21/01/18.
 * Image related utility methods
 */

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.DrawableRes
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.RequestBody

class ImageUtility(private val context: Context) {


    /**
     * Compresses the image

     * @param filePath : Location of image file
     * *
     * @return
     */

    fun compressImage(filePath: String): String {
        var scaledBitmap: Bitmap? = null

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var bmp: Bitmap? = BitmapFactory.decodeFile(filePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth
        val maxHeight = 816.0f
        val maxWidth = 612.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }

        options.inSampleSize = ImageUtility.calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        options.inDither = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()

        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap!!)
        canvas.matrix = scaleMatrix
        canvas.drawBitmap(bmp!!, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(
                Paint.FILTER_BITMAP_FLAG))

        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)

            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            Log.e("EXIF", "Exif: " + orientation)
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.e("EXIF", "Exif: " + orientation)
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.e("EXIF", "Exif: " + orientation)
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.e("EXIF", "Exif: " + orientation)
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height,
                    matrix, true)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var out: FileOutputStream? = null
        val filename = filename
        try {
            out = FileOutputStream(filename)
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 95, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            if (bmp != null) {
                bmp.recycle()
                bmp = null
            }
            if (scaledBitmap != null) {
                scaledBitmap.recycle()
            }
        }
        return filename

    }

    val filename: String
        get() {

            val root = file
            root.mkdirs()
            val filename = uniqueImageFilename
            val file = File(root, filename)
            return file.absolutePath
        }

    val file: File
        get() = File(Environment.getExternalStorageDirectory().toString() + File.separator + "Tr1pp"
                + File.separator)

    val uniqueImageFilename: String
        get() = "img_" + System.currentTimeMillis() + ".png"

    fun getRealPathFromURI(contentUri: Uri, activity: Activity): String {
        // can post image
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activity.managedQuery(contentUri, proj, null, null, null)// Which
        // columns
        // to
        // return
        // WHERE clause; which rows to return (all rows)
        // Order-by clause (ascending by name)
        val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    @JvmOverloads fun LoadImage(url: String, imageView: ImageView, isRounded: Boolean = false) {
        Glide.with(context).load(url).into(imageView)
    }

    /**
     * Compress image and convert to multipart

     * @param filePath path of the file to be converted
     * *
     * @return multipart image for the path supplied
     */
    fun getCompressedFile(filePath: String, imageUtility: ImageUtility): Observable<RequestBody> {
        return Observable.create<RequestBody> { sub ->
            try {
                val newFilePath = getCompressedImage(filePath)
                sub.onNext(imageUtility.getRequestBodyImage(File(newFilePath)))
                sub.onComplete()
            } catch (e: Exception) {
                sub.onError(e)
            }
        }

    }

    /**
     * get request body image
     */
    fun getRequestBodyImage(file: File): RequestBody {
        return RequestBody.create(MediaType.parse("image/png"), file)
    }

    /**
     * get request body image
     */
    fun getRequestBodyImagePath(path: String): RequestBody {
        val file = File(path)
        return RequestBody.create(MediaType.parse("image/png"), file)
    }


    /**
     * convert image to base 64 string

     * @param filePath path of image
     * *
     * @return base 64 string
     */
    fun getBase64Image(filePath: String): String {
        var filePath = filePath
        filePath = getCompressedImage(filePath)
        val bm = BitmapFactory.decodeFile(filePath)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos) //bm is the bitmap object
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    /**
     * get compressed image
     */
    private fun getCompressedImage(filePath: String): String {
        val newFilePath: String
        val file_size = Integer.parseInt((File(filePath).length() shr 10).toString())
        if (file_size >= 120) {
            newFilePath = compressImage(filePath)
        } else {
            newFilePath = filePath
        }
        return filePath
    }

    @SuppressLint("CheckResult")
    fun loadImage(url: String, imageView: ImageView, @DrawableRes placeholder: Int, isRounded: Boolean = false ) {
        if (placeholder != 0) {
            //            Glide.with(context).load(url).dontAnimate().placeholder(placeholder).error(placeholder).into(imageView);

            if (isRounded){

                Glide.with(context).load(url).placeholder(placeholder).bitmapTransform(CropCircleTransformation(context)).error(placeholder).into(imageView)

            }else{
                Glide.with(context).load(url).placeholder(placeholder).error(placeholder).into(imageView)
            }

        } else {
            //            Glide.with(context).load(url).dontAnimate().into(imageView);
            Glide.with(context).load(url).into(imageView)
        }
    }



    companion object {

        fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                if (width > height) {
                    inSampleSize = Math.round(height.toFloat() / reqHeight.toFloat())
                } else {
                    inSampleSize = Math.round(width.toFloat() / reqWidth.toFloat())
                }
            }
            return inSampleSize
        }
    }


}