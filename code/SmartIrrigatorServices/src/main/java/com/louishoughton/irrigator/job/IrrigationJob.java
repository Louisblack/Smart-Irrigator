package com.louishoughton.irrigator.job;

import com.louishoughton.irrigator.forecast.ForecastException;
import com.louishoughton.irrigator.forecast.LocationException;
import com.louishoughton.irrigator.forecast.TodaysWeather;
import com.louishoughton.irrigator.forecast.WeatherService;
import com.louishoughton.irrigator.valve.NodeConnectionException;
import com.louishoughton.irrigator.web.Error;
import com.louishoughton.irrigator.web.IrrigationRequest;
import com.louishoughton.irrigator.web.IrrigationRequestDispatcher;
import com.louishoughton.irrigator.web.IrrigationResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class IrrigationJob implements Runnable {

    private IrrigationRequestDispatcher requestDispatcher;
    private WeatherService weatherService;
    private ExecutionDao executionDao;

    private static final Logger LOG = LogManager.getLogger(IrrigationJob.class);
    
    public static final String REQUEST_DISPATCH_ERROR = "Cannot dispatch request to watering node"; 
    
    public IrrigationJob(WeatherService weatherService,
                         IrrigationRequestDispatcher requestDispatcher,
                         ExecutionDao executionDao) {
        this.weatherService = weatherService;
        this.requestDispatcher = requestDispatcher;
        this.executionDao = executionDao;
    }

    @Override
    public void run() {
        LOG.info("Starting job");
        try {
            TodaysWeather todaysWeather = weatherService.getTodaysWeather();
            LOG.info("Todays weather is " + todaysWeather);
            Optional<IrrigationRequest> request = getIrrigationRequestFromForecast(todaysWeather);
            if (request.isPresent()) {
                LOG.info("Sending request " + request.get());
                IrrigationResponse response = requestDispatcher.dispatch(request.get());
                saveExecution(todaysWeather, request.get(), response);
            } else {
                saveExecution(todaysWeather);
            }
        } catch (LocationException | ForecastException | NodeConnectionException exception) {
            LOG.error(exception);
            saveErroredExecution(exception);
        }
        LOG.info("Ending job");
    }


    private Optional<IrrigationRequest> getIrrigationRequestFromForecast(TodaysWeather weather)
            throws LocationException, ForecastException {

        if (weather.shouldIWater()) {
            return of(new IrrigationRequest(weather.howLongShouldIWater()));
        } else {
            return empty();
        }
    }

    private void saveExecution(TodaysWeather todaysWeather, IrrigationRequest request,
                               IrrigationResponse response) {
        executionDao.save(new Execution(todaysWeather.getForecast(),
                                        todaysWeather.getHistory(),
                                        request,
                                        response.getErrors()));
    }

    private void saveExecution(TodaysWeather todaysWeather) {
        executionDao.save(new Execution(todaysWeather.getForecast(), todaysWeather.getHistory()));
    }

    private void saveErroredExecution(Exception exception) {
        executionDao.save(new Execution(new Error(exception.getMessage())));
    }
}
