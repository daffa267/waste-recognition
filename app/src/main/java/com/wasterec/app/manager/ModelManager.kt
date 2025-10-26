package com.wasterec.app.manager

import android.content.Context
import com.wasterec.app.utils.IOUtils
import org.pytorch.LiteModuleLoader
import org.pytorch.Module

class ModelManager(private val context : Context) {
    private var model : Module? = null

    fun loadModel() : Module{
        System.out.println("Lokasiiiiiiiii : " + IOUtils.assetFilePath(
            context,
            "model.ptl"
        )
        )

        if (model == null){
            model = LiteModuleLoader.load(IOUtils.assetFilePath(context, "model.ptl"))
        }
        return model!!
    }


}