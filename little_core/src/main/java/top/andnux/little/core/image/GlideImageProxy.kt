package top.andnux.little.core.image

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import top.andnux.little.core.utils.BitmapUtil
import java.io.FileNotFoundException


class GlideImageProxy : IImageProxy {

    @SuppressLint("ResourceType")
    override fun display(
        url: String,
        @DrawableRes placeholder: Int,
        @DrawableRes error: Int,
        width: Int,
        height: Int,
        imageView: ImageView,
        listener: IImageListener?
    ) {
        val file = File(url)
        var options = RequestOptions()
        if (placeholder > 0) {
            options = options.placeholder(placeholder)
        }
        if (error > 0) {
            options = options.error(error)
        }
        if ((height + width) > 0) {
            options = options.override(width, height)
        }
        listener?.onStart()
        if (file.exists()) {
            Glide.with(imageView.context)
                .load(file)
                .apply(options)
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        imageView.setImageDrawable(resource)
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        listener?.onError(FileNotFoundException("下载的资源不存在"))
                    }
                })
        } else {
            Glide.with(imageView.context)
                .load(url)
                .apply(options)
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        imageView.setImageDrawable(resource)
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        listener?.onError(FileNotFoundException("下载的资源不存在"))
                    }
                })
        }
    }

    override fun compress(
        context: Context,
        file: List<File>,
        targetDir: String,
        listener: IImageListener?
    ) = Luban.with(context)
        .load(file)
        .ignoreBy(200)
        .setTargetDir(targetDir)
        .filter {
            !(TextUtils.isEmpty(it) || it.toLowerCase().endsWith(".gif"));
        }
        .setCompressListener(object : OnCompressListener {
            override fun onSuccess(file: File?) {
                file?.let { listener?.onSuccess(it) }
            }

            override fun onError(e: Throwable?) {
                e?.let { listener?.onError(it) }
            }

            override fun onStart() {
                listener?.onStart()
            }

        })
        .launch()
}