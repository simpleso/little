package top.andnux.little.core.image

import android.content.Context
import android.support.annotation.DrawableRes
import android.widget.ImageView
import top.andnux.little.core.config.DirConfig
import java.io.File

object ImageManager {

    private var imageProxy: IImageProxy? = null;

    fun init(imageProxy: IImageProxy): Unit {
        this.imageProxy = imageProxy
    }

    fun display(
        url: String,
        @DrawableRes placeholder: Int = 0,
        @DrawableRes error: Int = 0,
        width: Int = 0,
        height: Int = 0,
        imageView: ImageView,
        listener: IImageListener? = null
    ) {
        if (this.imageProxy == null) {
            this.imageProxy = GlideImageProxy()
        }
        this.imageProxy?.display(
            url, placeholder, error, width,
            height, imageView, listener
        )
    }

    fun compress(
        context: Context,
        file: List<File>,
        targetDir: String = DirConfig.HOME_IMAGE,
        listener: IImageListener? = null
    ) {
        if (this.imageProxy == null) {
            this.imageProxy = GlideImageProxy()
        }
        this.imageProxy?.compress(context, file, targetDir, listener)
    }

}