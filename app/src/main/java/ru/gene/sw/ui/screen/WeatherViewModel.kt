package ru.gene.sw.ui.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.gene.sw.repository.WeatherRepository
import ru.gene.sw.data.model.ForecastResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import ru.gene.sw.Config
import ru.gene.sw.Util
import ru.gene.sw.data.db.entity.WeatherEntity
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    @param:ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _state = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val state: StateFlow<WeatherState> = _state

    init {
        loadWeather()
    }

    fun loadWeather() {
        viewModelScope.launch {
            if (!Util.isNetworkAvailable(appContext)) {
                _state.value = WeatherState.NoNetwork
                return@launch
            }

            _state.value = WeatherState.Loading
            try {
                val location = repository.getLocationByIp(Locale.getDefault().language)
                val forecast = repository.getForecast(Config.API_KEY, location)
                _state.value = WeatherState.Success(forecast)
            } catch (e: Exception) {
                val cached = repository.getCachedWeather().firstOrNull()
                if (cached != null) {
                    _state.value = WeatherState.Offline(cached)
                } else {
                    _state.value = WeatherState.Error(e.message.toString())
                }
            }
        }
    }
}

sealed class WeatherState {
    object Loading : WeatherState()
    object NoNetwork : WeatherState()
    data class Success(val forecast: ForecastResponse) : WeatherState()
    data class Offline(val cached: WeatherEntity) : WeatherState()
    data class Error(val message: String) : WeatherState()

}
