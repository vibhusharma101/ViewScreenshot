package com.vibhu.imagetobitmap

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import java.io.ByteArrayOutputStream
import java.io.File


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layout  =  findViewById<LinearLayoutCompat>(R.id.layout_home)
        layout.post {
            val bitmap = loadBitmapFromView(layout)
            val dir = File(Environment.getExternalStorageDirectory().getAbsolutePath(), "folderName")
            if (!dir.exists()) dir.mkdirs()
            try {
                val uri =getImageUri(this,bitmap!!)
                var uriList =ArrayList<Uri>()
                   uriList.add(uri!!)
                shareOnWhatsapp(uriList)

            } catch (e: Exception) {
                Log.e("Karna", "Error, $e")
            }
        }


    }

    //getting image uri from bitmap
    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }


    fun shareOnWhatsapp(imageUriArray: ArrayList<Uri>){
        try {
            val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
            shareIntent.type = "text/html"
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUriArray as ArrayList<out Parcelable?>?)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "farm")
            shareIntent.putExtra(Intent.EXTRA_TEXT, "image")
            shareIntent.setPackage("com.whatsapp");
            startActivity(shareIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //getting bitmap from the view
    fun loadBitmapFromView(v: View): Bitmap? {
        val b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.run {
            layout(left, top, right, bottom)
            draw(c)
        }
        return b
    }
}