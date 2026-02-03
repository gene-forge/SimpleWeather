package ru.gene.sw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.gene.sw.ui.screen.WeatherScreen
import ru.gene.sw.ui.screen.WeatherViewModel
import ru.gene.sw.ui.theme.SimpleWeatherTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleWeatherTheme {
                val viewModel: WeatherViewModel = hiltViewModel()
                WeatherScreen(viewModel)
            }
        }
    }
}