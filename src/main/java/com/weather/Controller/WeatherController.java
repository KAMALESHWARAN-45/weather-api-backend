package com.weather.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.weather.Dto.TemperatureStatsDTO;
import com.weather.Dto.WeatherResponseDTO;
import com.weather.Service.WeatherService;

import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService service;

    public WeatherController(WeatherService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        service.uploadData(file);
        return "Data uploaded successfully";
    }

    @GetMapping("/date")
    public List<WeatherResponseDTO> getByDate(
            @RequestParam String date) {
        return service.getWeatherByDate(date);
    }

    @GetMapping("/month")
    public List<WeatherResponseDTO> getByMonth(
            @RequestParam int year,
            @RequestParam int month) {
        return service.getWeatherByMonth(year, month);
    }

    @GetMapping("/stats")
    public List<TemperatureStatsDTO> getYearStats(
            @RequestParam int year) {
        return service.getYearlyMonthlyStats(year);
    }
}