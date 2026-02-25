package com.weather.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weather.Entity.WeatherData;

import java.time.LocalDateTime;
import java.util.List;

public interface WeatherRepository extends JpaRepository<WeatherData, Long> {

    List<WeatherData> findByDateTimeBetween(
            LocalDateTime start,
            LocalDateTime end);
}