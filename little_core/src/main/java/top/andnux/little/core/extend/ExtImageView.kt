package top.andnux.little.core.extend

import android.widget.ImageView
import top.andnux.little.core.image.ImageManager

fun ImageView.setImageUrl(url:String,defRes:Int){
    ImageManager.display(url,imageView = this)
}