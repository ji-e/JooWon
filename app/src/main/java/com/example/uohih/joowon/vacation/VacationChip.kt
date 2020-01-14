package com.example.uohih.joowon.vacation

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import com.pchmn.materialchips.model.ChipInterface

class WorkerVacationChip(no: Int, name: String, joinDate: Int, phoneNum: String,
                         use: String, total: String, bitmap: String) : ChipInterface {

    val name = name
    val phoneNum = phoneNum
    val bitmap = bitmap
    val use = use
    val total = total
    val joinDate = joinDate.toString()
    val no = no




    override fun getInfo(): String {
        return phoneNum
    }

    override fun getAvatarDrawable(): Drawable? {
        return  null
    }

    override fun getLabel(): String {
        return name
    }

    override fun getId(): Any {
        val bundle = Bundle()
        bundle.putString("name", name)
        bundle.putString("phoneNum", phoneNum)
        bundle.putString("bitmap", bitmap)
        bundle.putString("use", use)
        bundle.putString("total", total)
        bundle.putString("joinDate", joinDate)
        bundle.putInt("no", no)

        return bundle
    }

    override fun getAvatarUri(): Uri {
        return Uri.parse(bitmap)
    }

}