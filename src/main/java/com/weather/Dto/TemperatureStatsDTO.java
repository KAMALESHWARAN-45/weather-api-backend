package com.weather.Dto;

public class TemperatureStatsDTO {

    private double high;
    private double median;
    private double minimum;

    public TemperatureStatsDTO(double high, double median, double minimum) {
        this.high = high;
        this.median = median;
        this.minimum = minimum;
    }

    public double getHigh() { return high; }
    public double getMedian() { return median; }
    public double getMinimum() { return minimum; }
}