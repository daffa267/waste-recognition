package com.tpi.wasterecognition

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tpi.wasterecognition.databinding.ActivityTrainingBinding

class TrainingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            Glide.with(this)
                .asGif()
                .load(R.drawable.network)
                .into(binding.ivBrain)
        }
    }
}