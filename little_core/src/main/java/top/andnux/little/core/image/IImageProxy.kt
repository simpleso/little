package top.andnux.little.core.image

import android.content.Context
import android.support.annotation.DrawableRes
import android.widget.ImageView
import java.io.File

interface IImageProxy {

    fun display(
        url: String,
        @DrawableRes placeholder:Int,
        @DrawableRes error:Int,
        width:Int,
        height:Int,
        imageView: ImageView,
        listener: IImageListener?
    )

    fun compress(
        context: Context,
        file: List<File>,
        targetDir:String,
        listener: IImageListener?
    )
}