package com.tpi.wasterecognition

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tpi.wasterecognition.databinding.ActivityMainBinding // 1. Impor kelas Binding

class MainActivity : AppCompatActivity() {

    // 2. Deklarasikan variabel untuk binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 3. Inisialisasi binding dan gunakan sebagai layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Kode bawaan untuk menangani padding edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 4. Atur fungsi klik untuk tombol "Start Predict"
        binding.btnStartPredict.setOnClickListener {
            // Buat Intent untuk pindah ke PredictActivity
            val intent = Intent(this, PredictActivity::class.java)
            // Mulai activity baru
            startActivity(intent)
        }
    }
}