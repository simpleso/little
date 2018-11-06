package top.andnux.little.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.View

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.reflect.Method


/**
 * @author 张春林
 * @date 16/4/9.
 */
object BitmapUtil {

    fun compressBitmapToBytes(filePath: String, reqWidth: Int, reqHeight: Int, quality: Int): ByteArray {
        val bitmap = getSmallBitmap(filePath, reqWidth, reqHeight)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
        val bytes = baos.toByteArray()
        bitmap.recycle()
        return bytes
    }

    fun compressBitmapSmallTo(filePath: String, reqWidth: Int, reqHeight: Int, maxLenth: Int): ByteArray {
        var quality = 100
        var bytes = compressBitmapToBytes(filePath, reqWidth, reqHeight, quality)
        while (bytes.size > maxLenth && quality > 0) {
            quality = quality / 2
            bytes = compressBitmapToBytes(filePath, reqWidth, reqHeight, quality)
        }
        return bytes
    }

    fun compressBitmapQuikly(filePath: String): ByteArray {
        return compressBitmapToBytes(filePath, 480, 800, 50)
    }

    fun compressBitmapQuiklySmallTo(filePath: String, maxLenth: Int): ByteArray {
        return compressBitmapSmallTo(filePath, 480, 800, maxLenth)
    }


    fun shotScreen(activity: Activity): Bitmap {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(view.drawingCache, 0, 0, view.measuredWidth, view.measuredHeight)
        view.isDrawingCacheEnabled = false
        view.destroyDrawingCache()
        return bitmap

    }


    fun blurBitmap(context: Context, bitmap: Bitmap, radius: Float): Bitmap {
        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        val outBitmap = Bitmap.createBitmap(
            bitmap.width, bitmap.height,
            Bitmap.Config.ARGB_8888
        )

        //Instantiate a new Renderscript
        val rs = RenderScript.create(context)

        //Create an Intrinsic Blur Script using the Renderscript
        val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        val allIn = Allocation.createFromBitmap(rs, bitmap)
        val allOut = Allocation.createFromBitmap(rs, outBitmap)

        //Set the radius of the blur
        blurScript.setRadius(radius)

        //Perform the Renderscript
        blurScript.setInput(allIn)
        blurScript.forEach(allOut)

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap)

        //recycle the original bitmap
        //        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        //        view.setBackground(new BitmapDrawable(context.getResources(), outBitmap));
        rs.destroy()
        return outBitmap
    }


    private val TAG = BitmapUtil::class.java.simpleName

    /**
     * Bitmap 转 Bytes
     *
     * @param b
     * @return
     */
    @JvmOverloads
    fun bitmapToByte(b: Bitmap, quality: Int = 100): ByteArray {
        val o = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.PNG, quality, o)
        return o.toByteArray()
    }


    /**
     * Bytes 转 Bitmap
     *
     * @param b
     * @return
     */
    fun byteToBitmap(b: ByteArray?): Bitmap? {
        return if (b == null || b.size == 0) null else BitmapFactory.decodeByteArray(b, 0, b.size)
    }

    /**
     * Bitmap转换成Base64编码的String
     *
     * @param bitmap
     * @return
     */
    fun bitmapToString(bitmap: Bitmap): String {
        return String(Base64.encode(bitmapToByte(bitmap), Base64.DEFAULT))
    }

    /**
     * string2Bitmap
     *
     * @param str
     * @return
     */
    @Throws(Exception::class)
    fun stringToBitmap(str: String): Bitmap? {
        if (StringUtil.isEmpty(str)) return null
        val bitmapArray = Base64.decode(str, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
    }

    /**
     * Drawable 转 Bitmap
     *
     * @param drawable
     * @return
     */
    fun drawableToBitmap(drawable: Drawable?): Bitmap? {
        return if (drawable == null) null else (drawable as BitmapDrawable).bitmap
    }

    /**
     * BitMap 转 Drawable
     *
     * @param bitmap
     * @return
     */
    fun bitmapToDrawable(bitmap: Bitmap?): Drawable? {
        return if (bitmap == null) null else BitmapDrawable(bitmap)
    }

    /**
     * 获取View的截图
     *
     * @param view
     * @return
     */
    fun viewToBitmap(view: View?): Bitmap? {
        if (view == null) return null
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null)
            bgDrawable.draw(canvas)
        else
            canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return returnedBitmap
    }

    /**
     * 屏幕截图
     *
     * @param activity
     * @return
     */
    fun viewToBitmap(activity: Activity): Bitmap {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val view = activity.window.decorView
        view.layout(0, 0, dm.widthPixels, dm.heightPixels)
        view.isDrawingCacheEnabled = true
        return Bitmap.createBitmap(view.drawingCache)
    }

    /**
     * 图片缩放
     *
     * @param org       原始图片
     * @param newWidth  缩放后的宽度
     * @param newHeight 缩放后的高
     * @return
     */
    fun scaleImageTo(org: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
        return scaleImage(org, newWidth.toFloat() / org.width, newHeight.toFloat() / org.height)
    }

    /**
     * 图片缩放
     *
     * @param org         原始图片
     * @param scaleWidth  宽度缩放倍数
     * @param scaleHeight 高度缩放倍数
     * @return
     */
    fun scaleImage(org: Bitmap?, scaleWidth: Float, scaleHeight: Float): Bitmap? {
        if (org == null) {
            return null
        }
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(org, 0, 0, org.width, org.height, matrix, true)
    }

    /**
     * 获取圆形图片
     *
     * @param bitmap
     * @return
     */
    fun toRoundCorner(bitmap: Bitmap): Bitmap {
        val height = bitmap.height
        val width = bitmap.height
        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, width, height)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        //paint.setColor(0xff424242);
        paint.color = Color.TRANSPARENT
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 2).toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    /**
     * 生成缩略图
     *
     * @param bitMap
     * @param needRecycle 原图是否销毁
     * @param newHeight
     * @param newWidth
     * @return
     */
    fun getBitmapThumbnail(bitMap: Bitmap, needRecycle: Boolean, newHeight: Int, newWidth: Int): Bitmap {
        val width = bitMap.width
        val height = bitMap.height
        // 计算缩放比例
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // 取得想要缩放的matrix参数
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片
        val newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true)
        if (needRecycle)
            bitMap.recycle()
        return newBitMap
    }

    /**
     * 保存BitMap成文件
     *
     * @param bitmap
     * @param file
     * @return
     */
    fun saveBitmap(bitmap: Bitmap?, file: File): Boolean {
        if (bitmap == null)
            return false
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return false
    }

    /**
     * 保存Bitmap到文件
     *
     * @param bitmap
     * @param absPath 文件绝对路径
     * @return
     */
    fun saveBitmap(bitmap: Bitmap, absPath: String): Boolean {
        return saveBitmap(bitmap, File(absPath))
    }

    /**
     * 反射 得到本地视频的预览图
     *
     * @param context
     * @param uri
     * @return
     */
    fun createVideoThumbnail(context: Context, uri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        val className = "android.media.MediaMetadataRetriever"
        var objectMediaMetadataRetriever: Any? = null
        val release: Method? = null
        try {
            objectMediaMetadataRetriever = Class.forName(className).newInstance()
            val setDataSourceMethod =
                Class.forName(className).getMethod("setDataSource", Context::class.java, Uri::class.java)
            setDataSourceMethod.invoke(objectMediaMetadataRetriever, context, uri)
            val getFrameAtTimeMethod = Class.forName(className).getMethod("getFrameAtTime")
            bitmap = getFrameAtTimeMethod.invoke(objectMediaMetadataRetriever) as Bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                release?.invoke(objectMediaMetadataRetriever)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return bitmap
    }


    fun buildImageGetIntent(saveTo: Uri, outputX: Int, outputY: Int, returnData: Boolean): Intent {
        return buildImageGetIntent(saveTo, 1, 1, outputX, outputY, returnData)
    }

    fun buildImageGetIntent(
        saveTo: Uri, aspectX: Int, aspectY: Int,
        outputX: Int, outputY: Int, returnData: Boolean
    ): Intent {
        val intent = Intent()
        if (Build.VERSION.SDK_INT < 19) {
            intent.action = Intent.ACTION_GET_CONTENT
        } else {
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            intent.addCategory(Intent.CATEGORY_OPENABLE)
        }
        intent.type = "image/*"
        intent.putExtra("output", saveTo)
        intent.putExtra("aspectX", aspectX)
        intent.putExtra("aspectY", aspectY)
        intent.putExtra("outputX", outputX)
        intent.putExtra("outputY", outputY)
        intent.putExtra("scale", true)
        intent.putExtra("return-data", returnData)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString())
        return intent
    }

    fun buildImageCropIntent(uriFrom: Uri, uriTo: Uri, outputX: Int, outputY: Int, returnData: Boolean): Intent {
        return buildImageCropIntent(uriFrom, uriTo, 1, 1, outputX, outputY, returnData)
    }

    fun buildImageCropIntent(
        uriFrom: Uri, uriTo: Uri, aspectX: Int, aspectY: Int,
        outputX: Int, outputY: Int, returnData: Boolean
    ): Intent {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uriFrom, "image/*")
        intent.putExtra("crop", "true")
        intent.putExtra("output", uriTo)
        intent.putExtra("aspectX", aspectX)
        intent.putExtra("aspectY", aspectY)
        intent.putExtra("outputX", outputX)
        intent.putExtra("outputY", outputY)
        intent.putExtra("scale", true)
        intent.putExtra("return-data", returnData)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString())
        return intent
    }

    fun buildImageCaptureIntent(uri: Uri): Intent {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        return intent
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val h = options.outHeight
        val w = options.outWidth
        var inSampleSize = 0
        if (h > reqHeight || w > reqWidth) {
            val ratioW = w.toFloat() / reqWidth
            val ratioH = h.toFloat() / reqHeight
            inSampleSize = Math.min(ratioH, ratioW).toInt()
        }
        inSampleSize = Math.max(1, inSampleSize)
        return inSampleSize
    }

    fun getSmallBitmap(filePath: String, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, options)
    }

    fun saveWxyPhoto(bmp: Bitmap, id: String) {
        if (FileUtil.isSDCardMounted) {
            val sdcardPath = FileUtil.sdCardRoot
            log(sdcardPath)
            if (FileUtil.getSize(File(sdcardPath)) > 0) {
                val smyDir = File(sdcardPath + "/smy")
                log(smyDir.absolutePath)
                if (!smyDir.exists()) {
                    smyDir.mkdir()
                }
                val wxyDir = File(smyDir.getAbsolutePath() + "/wxy")
                log(wxyDir.absolutePath)
                if (!wxyDir.exists()) {
                    wxyDir.mkdir()
                }
                saveBitmap(bmp, "$wxyDir${File.separator}$id)")
            } else {
            }
        } else {
        }
    }

    private fun log(str: String) {
        Log.d("BitmapUtil", str)
    }

    /**
     * 图片压缩到100k
     *
     * @param image
     * @return Bitmap
     */
    fun compressImage(image: Bitmap): Bitmap? {

        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 90
        while (baos.toByteArray().size / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset()//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos)//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10//每次都减少10
        }
        val isBm = ByteArrayInputStream(baos.toByteArray())//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null)
    }

    fun getCompressedBitmap(imagePath: String): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        // 记得把assets目录下的图片拷贝到SD卡中
        // 由于设置inJustDecodeBounds为true，因此执行下面代码后bitmap为空
        BitmapFactory.decodeFile(imagePath, options)
        // 计算缩放比例，由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        var scale = (options.outHeight / 300.toFloat()).toInt()
        // 因为结果为int型，如果相除后值为0.n，则最终结果将是0
        if (scale <= 0) {
            scale = 1
        }
        println("Scale=$scale")
        options.inSampleSize = scale
        options.inJustDecodeBounds = false
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds设回false
        val mBitmap = BitmapFactory.decodeFile(imagePath, options)
        val width = mBitmap.width
        val height = mBitmap.height
        println(width.toString() + " " + height)
        return mBitmap
    }
}
