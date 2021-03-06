package com.louishoughton.irrigator.forecast;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ForecastIoWeatherService implements WeatherService {

    private ForecastIoForecastService forecastService;
    private ForecastIoHistoryService historyService;

    @Autowired
    public ForecastIoWeatherService(ForecastIoForecastService forecastService, 
            ForecastIoHistoryService historyService) {
        this.forecastService = forecastService;
        this.historyService = historyService;
    }

    @Override
    public TodaysWeather getTodaysWeather() throws LocationException, ForecastException {
        return new TodaysWeather(getForecast(), getHistory());
    }

    private Forecast getForecast() throws LocationException, ForecastException {
        return forecastService.getForecast();
    }

    private History getHistory() throws LocationException, ForecastException {
        return historyService.getHistory();
    }
    
}
