package com.tpi.wasterecognition

import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        enableEdgeToEdge()
        binding = ActivityPredictBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val initialPaddingBottom = binding.main.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft,
                v.paddingTop,
                v.paddingRight,
                initialPaddingBottom + systemBars.bottom
            )
            insets
        }

        binding.btnInput.setOnClickListener {
            openGallery()
        }

        binding.btnCorrect.setOnClickListener {
            val predictedLabel = binding.tvPredictionResult.text.toString()
            Toast.makeText(this, "Confirmed: $predictedLabel", Toast.LENGTH_SHORT).show()
        }

        binding.btnWrong.setOnClickListener {
            showChooseLabelDialog()
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun showChooseLabelDialog() {
        val builder = MaterialAlertDialogBuilder(this)
        val dialogView = layoutInflater.inflate(R.layout.choose_label, null)
        builder.setView(dialogView)

        val autoCompleteTextView = dialogView.findViewById<AutoCompleteTextView>(R.id.auto_complete_text_view)
        val btnSave = dialogView.findViewById<MaterialButton>(R.id.btn_dialog_save)
        val btnCancel = dialogView.findViewById<MaterialButton>(R.id.btn_dialog_cancel)

        val labels = resources.getStringArray(R.array.waste_labels)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, labels)
        autoCompleteTextView.setAdapter(adapter)

        val dialog = builder.create()
        dialog.show()

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSave.setOnClickListener {
            val selectedLabel = autoCompleteTextView.text.toString()
            if (selectedLabel.isNotEmpty()) {
                Toast.makeText(this, "Label corrected to: $selectedLabel", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please select a label", Toast.LENGTH_SHORT).show()
            }
        }
    }
}