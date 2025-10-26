package com.wasterec.app.manager

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import com.wasterec.app.model.ModelConiguration
import com.wasterec.app.model.TrainingModel
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.torchvision.TensorImageUtils

class EfficientNetB0(val context: Context) {
    val model: Module = ModelManager(context).loadModel()

    @RequiresApi(Build.VERSION_CODES.O)
    fun predict(bitmap: Bitmap) : Pair<Int, Float>{
        val safeBitmap = if (bitmap.config == Bitmap.Config.HARDWARE) {
            bitmap.copy(Bitmap.Config.ARGB_8888, false)
        } else {
            bitmap
        }

        val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(safeBitmap,
            TensorImageUtils.TORCHVISION_NORM_MEAN_RGB,
            TensorImageUtils.TORCHVISION_NORM_STD_RGB
            )
        val outputTensor = model.forward(IValue.from(inputTensor)).toTensor()
        val scores = outputTensor.dataAsFloatArray
        val output = scores.indices.maxByOrNull { scores[it] }
        val expScores = scores.map { kotlin.math.exp(it) }
        val sumExp = expScores.sum()
        val probabilities = expScores.map { it / sumExp }

        // Ambil index dengan nilai tertinggi
        val outputIdx = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
        val confidence = if (outputIdx != -1) probabilities[outputIdx] else 0f

        return Pair(outputIdx, confidence)
    }

    fun train(config : ModelConiguration , dataset : List<TrainingModel>){
        repeat(config.epoch){

        }
    }
}