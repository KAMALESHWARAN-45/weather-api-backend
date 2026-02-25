package com.weather.Service;

import org.springframework.web.multipart.MultipartFile;

import com.weather.Dto.TemperatureStatsDTO;
import com.weather.Dto.WeatherResponseDTO;
import java.util.List;

public interface WeatherService {

    void uploadData(MultipartFile file);

    List<WeatherResponseDTO> getWeatherByDate(String date);

    List<WeatherResponseDTO> getWeatherByMonth(int year, int month);

    List<TemperatureStatsDTO> getYearlyMonthlyStats(int year);
}