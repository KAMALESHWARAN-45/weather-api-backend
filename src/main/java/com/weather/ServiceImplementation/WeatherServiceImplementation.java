package com.weather.ServiceImplementation;

 

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.weather.Dto.TemperatureStatsDTO;
import com.weather.Dto.WeatherResponseDTO;
import com.weather.Entity.WeatherData;
import com.weather.Repository.WeatherRepository;
import com.weather.Service.WeatherService;

import jakarta.transaction.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeatherServiceImplementation implements WeatherService {

    private final WeatherRepository repository;

    public WeatherServiceImplementation(WeatherRepository repository) {
        this.repository = repository;
    }

  
    @Override
    @Transactional
    public void uploadData(MultipartFile file) {

        List<WeatherData> list = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            String line;
            reader.readLine();  

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm");

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                WeatherData weather = new WeatherData();

                 
                weather.setDateTime(
                        LocalDateTime.parse(data[0], formatter));

                 
                weather.setCondition(data[1]);

                 
                weather.setTemperature(parseDoubleSafe(data[11]));

               
                weather.setHumidity(parseDoubleSafe(data[6]));

                
                weather.setPressure(parseDoubleSafe(data[8]));

               
                weather.setHeatIndex(parseDoubleSafe(data[5]));

                list.add(weather);
            }

            repository.saveAll(list);

        } catch (Exception e) {
            throw new RuntimeException("CSV Processing Failed", e);
        }
    }

  
    @Override
    public List<WeatherResponseDTO> getWeatherByDate(String date) {

        LocalDate localDate = LocalDate.parse(date);

        LocalDateTime start = localDate.atStartOfDay();
        LocalDateTime end = localDate.atTime(23, 59);

        return repository.findByDateTimeBetween(start, end)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

 
    @Override
    public List<WeatherResponseDTO> getWeatherByMonth(int year, int month) {

        LocalDateTime start =
                LocalDateTime.of(year, month, 1, 0, 0);

        LocalDateTime end =
                start.withDayOfMonth(start.toLocalDate().lengthOfMonth())
                        .withHour(23).withMinute(59);

        return repository.findByDateTimeBetween(start, end)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

 
    @Override
    public List<TemperatureStatsDTO> getYearlyMonthlyStats(int year) {

        List<TemperatureStatsDTO> result = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {

            LocalDateTime start =
                    LocalDateTime.of(year, month, 1, 0, 0);

            LocalDateTime end =
                    start.withDayOfMonth(start.toLocalDate().lengthOfMonth())
                            .withHour(23).withMinute(59);

            List<Double> temps = repository
                    .findByDateTimeBetween(start, end)
                    .stream()
                    .map(WeatherData::getTemperature)
                    .filter(Objects::nonNull)
                    .sorted()
                    .collect(Collectors.toList());

            if (temps.isEmpty())
                continue;

            double high = temps.get(temps.size() - 1);
            double min = temps.get(0);
            double median;

            int size = temps.size();
            if (size % 2 == 0) {
                median = (temps.get(size/2 - 1)
                        + temps.get(size/2)) / 2;
            } else {
                median = temps.get(size/2);
            }

            result.add(new TemperatureStatsDTO(high, median, min));
        }

        return result;
    }

 

    private WeatherResponseDTO convertToDTO(WeatherData data) {

        return new WeatherResponseDTO(
                data.getDateTime(),
                data.getCondition(),
                data.getTemperature(),
                data.getHumidity(),
                data.getPressure()
        );
    }

    private Double parseDoubleSafe(String value) {

        if (value == null || value.isBlank())
            return null;

        value = value.trim();

        if (value.equals("-9999") || value.equalsIgnoreCase("N/A"))
            return null;

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}