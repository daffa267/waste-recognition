package com.wasterec.app.presentation.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.wasterec.app.R
import com.wasterec.app.model.Destination
import com.wasterec.app.ui.theme.app_background
import com.wasterec.app.ui.theme.dark_teal
import com.wasterec.app.ui.theme.medium_teal
import com.wasterec.app.ui.theme.text_primary
import com.wasterec.app.ui.theme.text_secondary
import com.wasterec.app.ui.theme.card_background

@Composable
fun HomeView(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(app_background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.waste),
            contentDescription = "Waste Rec Logo",
            modifier = Modifier
                .padding(top = 32.dp)
                .size(90.dp),
            colorFilter = ColorFilter.tint(medium_teal)
        )

        Text(
            text = "Waste Rec",
            color = text_primary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = "Model Statistic :",
            color = text_secondary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = card_background
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp, vertical = (12.dp - 8.dp))
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    StatisticItem("Accuracy :", "50%", Modifier.weight(2f))
                    StatisticItem("Loss :", "0.15", Modifier.weight(2f))
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    StatisticItem("Arch :", "ResNet-18", Modifier.weight(2f))
                    StatisticItem("Last training :", "21 Okt 2025", Modifier.weight(2f))
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                navController.navigate(route = Destination.Annotate)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(top = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = medium_teal
            )
        ) {
            Text(
                text = "Start Predict",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun StatisticItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = text_secondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            color = text_primary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

