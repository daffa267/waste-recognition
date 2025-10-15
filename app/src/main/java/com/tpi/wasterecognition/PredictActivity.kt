package com.tpi.wasterecognition

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tpi.wasterecognition.databinding.ActivityPredictBinding

class PredictActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPredictBinding

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            Glide.with(this)
                .load(it)
                .into(binding.ivWasteImage)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnInput.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }
}