package com.weather.Dto;

import java.time.LocalDateTime;

public class WeatherResponseDTO {

    private LocalDateTime dateTime;
    private String condition;
    private Double temperature;
    private Double humidity;
    private Double pressure;

    public WeatherResponseDTO(LocalDateTime dateTime,
                              String condition,
                              Double temperature,
                              Double humidity,
                              Double pressure) {
        this.dateTime = dateTime;
        this.condition = condition;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public LocalDateTime getDateTime() { return dateTime; }
    public String getCondition() { return condition; }
    public Double getTemperature() { return temperature; }
    public Double getHumidity() { return humidity; }
    public Double getPressure() { return pressure; }
}