package com.wasterec.app.presentation.views

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wasterec.app.model.Destination

@Composable
fun NavigationView(context: Context, navController : NavHostController, modifier: Modifier = Modifier) {
    val startDestination = Destination.Splash
    var selectedDestination by rememberSaveable { mutableIntStateOf(0) }


    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Destination.Home> { MainView(navController) }
        composable<Destination.Info>{  }
        composable<Destination.Splash>{ SplashView() }
        composable<Destination.Training>{ TrainingView(navController) }
        composable<Destination.Annotate>{ AnnotateView(context, navController) }
        composable<Destination.FinishTraining>{ FinishTrainingView(navController) }

    }

}



@Preview(showBackground = false)
@Composable
fun MainPreview(){
    MainView(rememberNavController())
}