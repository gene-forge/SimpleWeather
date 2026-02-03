package ru.gene.sw.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.gene.sw.Util
import ru.gene.sw.R
import kotlin.collections.emptyList

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is WeatherState.Loading -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center

        ) {
            CircularProgressIndicator()
        }

        is WeatherState.Success -> {
            val forecast = (state as WeatherState.Success).forecast
            val current = forecast.current
            val days = forecast.forecast.forecastday

            LazyColumn(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxSize()

                    .padding(top = 2.dp),

                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = forecast.location.name,
                            style = MaterialTheme.typography.h5
                        )
                        Text(
                            text = "${Util.formatTime(forecast.location.localtime)} • ${
                                Util.formatDate(
                                    forecast.location.localtime
                                )
                            }",
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                        )
                    }
                }

                item {
                    Card(
                        elevation = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("${current.temp_c} °C", style = MaterialTheme.typography.h4)
                                Text(stringResource(R.string.feels_like, current.feelslike_c))
                                Text(stringResource(R.string.wind, current.wind_kph))
                                Text(stringResource(R.string.humidity, current.humidity))
                            }
                            AsyncImage(
                                model = "https:${current.condition.icon}",
                                contentDescription = current.condition.text,
                                modifier = Modifier.size(80.dp),
                                error = null,
                                placeholder = null
                            )
                        }
                    }
                }

                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val todayHours = days.firstOrNull()?.hour ?: emptyList()
                        items(todayHours) { hour ->
                            Card(elevation = 2.dp) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(hour.time.takeLast(5))
                                    AsyncImage(
                                        model = "https:${hour.condition.icon}",
                                        contentDescription = hour.condition.text,
                                        modifier = Modifier.size(40.dp),
                                        error = null,
                                        placeholder = null
                                    )
                                    Text("${hour.temp_c} °C")
                                }
                            }
                        }
                    }
                }

                items(days.drop(0).take(3)) { day ->
                    Card(
                        elevation = 4.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    Util.formatDayLabel(day.date),
                                    style = MaterialTheme.typography.subtitle1
                                )
                                Text(
                                    stringResource(
                                        R.string.max_min,
                                        day.day.maxtemp_c,
                                        day.day.mintemp_c
                                    )
                                )
                                Text(
                                    day.day.condition.text,
                                    style = MaterialTheme.typography.caption
                                )
                            }
                            AsyncImage(
                                model = "https:${day.day.condition.icon}",
                                contentDescription = day.day.condition.text,
                                modifier = Modifier.size(50.dp),
                                error = null,
                                placeholder = null
                            )
                        }
                    }
                }
            }
        }

        is WeatherState.Offline -> {
            val cached = (state as WeatherState.Offline).cached
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = cached.locationName,
                        style = MaterialTheme.typography.h5
                    )

                }
                Card(
                    elevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("${cached.tempC} °C", style = MaterialTheme.typography.h4)
                            Text(stringResource(R.string.feels_like, cached.feelsLikeC))
                            Text(stringResource(R.string.wind, cached.windKph))
                            Text(stringResource(R.string.humidity, cached.humidity))
                        }
                    }
                }

                Card(
                    backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f),
                    elevation = 0.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        stringResource(R.string.offline_message, cached.updatedAt),
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        is WeatherState.NoNetwork -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f),
                    elevation = 0.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.no_network_message),
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        is WeatherState.Error -> {
            val message = (state as WeatherState.Error).message
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f),
                    elevation = 0.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        stringResource(R.string.error_prefix, message),
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}
