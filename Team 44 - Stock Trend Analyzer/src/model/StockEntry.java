package model;

import java.time.LocalDate;

public class StockEntry {
    private LocalDate date;
    private double open, high, low, close;
    private long volume;

    public StockEntry(LocalDate date, double open, double high, double low, double close, long volume) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public LocalDate getDate() { return date; }
    public double getOpen() { return open; }
    public double getHigh() { return high; }
    public double getLow() { return low; }
    public double getClose() { return close; }
    public long getVolume() { return volume; }

    public String toString() {
        return String.format("%-12s | %-8s | %-8s | %-8s | %-8s | %-12s", 
                             date, 
                             String.format("%.2f", open), 
                             String.format("%.2f", high), 
                             String.format("%.2f", low), 
                             String.format("%.2f", close), 
                             volume);
    }
  
}
