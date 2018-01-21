
package com.themoviedbdemo.utils

import android.content.Context
import android.graphics.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource


/**
 * Created by Saveen on 21/01/18.
 */
class CropCircleTransformation(context: Context) : Transformation<Bitmap> {

    private var bitmapPool: BitmapPool

    override fun getId(): String {
        return "CropCircleTransformation()"
    }

    override fun transform(resource: Resource<Bitmap>, outWidth: Int, outHeight: Int): Resource<Bitmap> {
        val source = resource.get()
        val size = Math.min(source.width, source.height)

        val width: Float = ((source.width - size) / 2).toFloat()
        val height: Float = ((source.height - size) / 2).toFloat()

        var bitmap: Bitmap? = bitmapPool.get(size, size, Bitmap.Config.ARGB_8888)
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        if (width != 0f || height != 0f) {
            // source isn't square, move viewport to center
            val matrix = Matrix()
            matrix.setTranslate(-width, -height)
            shader.setLocalMatrix(matrix)
        }
        paint.shader = shader
        paint.isAntiAlias = true

        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)

        return BitmapResource.obtain(bitmap, bitmapPool)
    }

    init {
        bitmapPool = Glide.get(context).bitmapPool
    }

}