package com.example.uohih.joowon.view.main

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import com.example.uohih.joowon.R
import kotlinx.android.synthetic.main.activity_picture.*

class PictureActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)


        if (intent.hasExtra("picture")) {
            var bitmap = BitmapFactory.decodeFile(intent.getStringExtra("picture"))
            picture_img?.setImageBitmap(bitmap)
        }

    }

}
