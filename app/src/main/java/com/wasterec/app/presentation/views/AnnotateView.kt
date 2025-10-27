package com.wasterec.app.presentation.views

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import android.os.Build
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.wasterec.app.manager.ResNet18
import com.wasterec.app.model.Destination
import com.wasterec.app.ui.theme.Typography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.graphics.ImageDecoder as AndroidImageDecoder
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.wasterec.app.R
import com.wasterec.app.ui.theme.app_background
import com.wasterec.app.ui.theme.dark_teal
import com.wasterec.app.ui.theme.medium_teal
import com.wasterec.app.ui.theme.text_secondary
import com.wasterec.app.ui.theme.divider_gray
import com.wasterec.app.ui.theme.card_background
import com.wasterec.app.ui.theme.soft_red
import com.wasterec.app.ui.theme.text_primary
import kotlinx.coroutines.launch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.window.DialogProperties
import com.wasterec.app.ui.theme.lightGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnotateView(
    context: Context,
    navController : NavHostController
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var predictResult by remember { mutableStateOf<String?>(null)}
    var confidentLevel by remember { mutableStateOf(0f)}
    var resNet: ResNet18 = ResNet18( context )
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    val label = arrayOf("Glass", "Paper", "Cardboard",  "Metal","Plastic", "Trash")
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showWrongDialog by remember { mutableStateOf(false) }

    if (showWrongDialog) {
        CorrectLabelDialog(
            labels = label,
            onDismiss = { showWrongDialog = false },
            onSave = { selectedLabel ->
                if (selectedLabel.isNotEmpty()) {
                    Toast.makeText(context, "Label corrected to: $selectedLabel", Toast.LENGTH_SHORT).show()
                    showWrongDialog = false
                } else {
                    Toast.makeText(context, "Please select a label", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(app_background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 70.dp), // Padding atas diubah ke 40dp
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Waste Rec",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.waste),
                    contentDescription = "Logo",
                    tint = dark_teal,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Divider(
            color = divider_gray,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 16.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Not enough yet",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = (32.dp - 24.dp))
            )
            Text(
                text = "Need 20 sample : 12/20",
                color = text_secondary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            val bitmap = selectedImageUri?.let { uri ->
                remember(uri) {
                    try {
                        if (Build.VERSION.SDK_INT < 28) {
                            BitmapFactory.decodeStream(
                                context.contentResolver.openInputStream(uri)
                            )
                        } else {
                            val source = AndroidImageDecoder.createSource(context.contentResolver, uri)
                            AndroidImageDecoder.decodeBitmap(source)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(top = 24.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.pict),
                        contentDescription = "Preview Placeholder",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Text(
                text = predictResult ?: "...",
                color = dark_teal,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )

            val confidenceText = if (predictResult != null) {
                "Confident Lv. : ${String.format("%.2f", confidentLevel * 100f)}%"
            } else {
                "Confident Lv. : 0.00%"
            }
            Text(
                text = confidenceText,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        imagePickerLauncher.launch("image/*")
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = card_background)
                ) {
                    Text("Input", fontSize = 16.sp, color = text_primary, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (bitmap != null) {
                            scope.launch {
                                predictResult = "Loading..."
                                withContext(Dispatchers.IO) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        val output = resNet.predict(
                                            bitmap.copy(Bitmap.Config.ARGB_8888, false)
                                        )
                                        val outputIdx = output.first
                                        val labelResult = label.getOrNull(outputIdx)
                                        withContext(Dispatchers.Main) {
                                            predictResult = labelResult ?: "Unknown"
                                            confidentLevel = output.second
                                            if (confidentLevel == 0.0f) {
                                                predictResult = "Unknown"
                                            }
                                        }
                                    } else {
                                        withContext(Dispatchers.Main) {
                                            predictResult = "Unsupported OS"
                                            confidentLevel = 0f
                                            Toast.makeText(context, "Prediction requires Android 8.0 (Oreo) or newer.", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(context, "Please select an image first", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = card_background)
                ) {
                    Text("Predict", fontSize = 16.sp, color = text_primary, fontWeight = FontWeight.Bold)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        val currentPrediction = predictResult ?: "..."
                        Toast.makeText(context, "Confirmed: $currentPrediction", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = medium_teal)
                ) {
                    Text("Correct", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.width(8.dp))

                Button(
                    onClick = {
                        showWrongDialog = true
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = soft_red)
                ) {
                    Text("Wrong", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    navController.navigate(route = Destination.Training)
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = medium_teal),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Next",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CorrectLabelDialog(
    labels: Array<String>,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedLabel by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Choose Correct Label") },
        text = {
            Column {
                Spacer(Modifier.height(16.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = selectedLabel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Label") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        labels.forEach { label ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedLabel = label
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(selectedLabel) },
                colors = ButtonDefaults.buttonColors(containerColor = medium_teal)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = text_secondary)
            ) {
                Text("Cancel")
            }
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}


@Preview(showBackground = true)
@Composable
fun Prev(){
    //AnnotateView(LocalContext.current, rememberNavController())
}

