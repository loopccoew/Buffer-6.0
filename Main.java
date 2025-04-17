import java.io.*;
import java.text.*;
import java.util.*;

class Main {

    static class MarketData {
        String date;
        double close;
        double sma;
        double ema;

        MarketData(String date, double close) {
            this.date = date;
            this.close = close;
        }
    }

    static List<MarketData> dataList = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        readCSV("C:\\Users\\karti\\kartika\\Karti\\Loop_CodeZen\\TCS_stocks.csv");
        computeSMA(3);
        computeEMA(3);

        analyzeTrends();
        analyzeWeeklyTrends();
    }

    static void readCSV(String filename) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        DateFormat df = new SimpleDateFormat("M/d/yyyy");

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length < 5 || parts[0].contains("Date")) continue;
            String date = parts[0];
            double close = Double.parseDouble(parts[4]);
            dataList.add(new MarketData(date, close));
        }
        br.close();
    }

    static void computeSMA(int period) {
        for (int i = 0; i < dataList.size(); i++) {
            if (i < period - 1) continue;
            double sum = 0;
            for (int j = i - period + 1; j <= i; j++) {
                sum += dataList.get(j).close;
            }
            dataList.get(i).sma = sum / period;
        }
    }

    static void computeEMA(int period) {
        double multiplier = 2.0 / (period + 1);
        dataList.get(0).ema = dataList.get(0).close;
        for (int i = 1; i < dataList.size(); i++) {
            double prevEMA = dataList.get(i - 1).ema;
            dataList.get(i).ema = ((dataList.get(i).close - prevEMA) * multiplier) + prevEMA;
        }
    }

    static void analyzeTrends() {
        for (int i = 0; i < dataList.size(); i++) {
            MarketData today = dataList.get(i);

            // Market Rising (3-day)
            if (i >= 2 && isEMARising(i)) {
                System.out.println(today.date + ": Market Rising - EMA rising 3 days in a row");
            } else if (i >= 1 && today.ema > dataList.get(i - 1).ema) {
                System.out.println(today.date + ": Market is raising - EMA rising today");
            }

            // SMA < EMA for 3 consecutive days
            if (i >= 2 && isSMALessThanEMA(i)) {
                System.out.println(today.date + ": Hold Off Buying - SMA below EMA for 3 days");
            }

            // SMA crosses above EMA
            if (i >= 1 && dataList.get(i - 1).sma < dataList.get(i - 1).ema && today.sma >= today.ema) {
                System.out.println(today.date + ": Buy Suggestion - SMA crossed above EMA");
            }

            // Market trending down
            if (i >= 1 && today.ema < dataList.get(i - 1).ema && today.close < dataList.get(i - 1).close) {
                System.out.println(today.date + ": Spend Carefully - Market trending down");
            }

            // EMA falling 3 days
            if (i >= 2 && isEMAFalling(i)) {
                System.out.println(today.date + ": Trend Warning - EMA falling 3 days in a row");
            }
        }
    }

    static boolean isEMARising(int i) {
        return dataList.get(i).ema > dataList.get(i - 1).ema &&
               dataList.get(i - 1).ema > dataList.get(i - 2).ema;
    }

    static boolean isEMAFalling(int i) {
        return dataList.get(i).ema < dataList.get(i - 1).ema &&
               dataList.get(i - 1).ema < dataList.get(i - 2).ema;
    }

    static boolean isSMALessThanEMA(int i) {
        return dataList.get(i).sma < dataList.get(i).ema &&
               dataList.get(i - 1).sma < dataList.get(i - 1).ema &&
               dataList.get(i - 2).sma < dataList.get(i - 2).ema;
    }

    static void analyzeWeeklyTrends() throws ParseException {
        System.out.println("\nWeekly Market Insights:\n");

        Map<String, List<MarketData>> weekMap = new HashMap<>();
        DateFormat df = new SimpleDateFormat("M/d/yyyy");
        Calendar cal = Calendar.getInstance();

        for (MarketData md : dataList) {
            cal.setTime(df.parse(md.date));
            int week = cal.get(Calendar.WEEK_OF_YEAR);
            int year = cal.get(Calendar.YEAR);
            String key = "\nWeek " + week + " of " + year;

            weekMap.computeIfAbsent(key, k -> new ArrayList<>()).add(md);
        }

        // Sort weeks by year and week number
        List<String> sortedWeeks = new ArrayList<>(weekMap.keySet());
        sortedWeeks.sort((week1, week2) -> {
            try {
                int year1 = Integer.parseInt(week1.split(" ")[3]);
                int year2 = Integer.parseInt(week2.split(" ")[3]);
                int weekNum1 = Integer.parseInt(week1.split(" ")[1]);
                int weekNum2 = Integer.parseInt(week2.split(" ")[1]);

                if (year1 != year2) {
                    return year1 - year2;
                } else {
                    return weekNum1 - weekNum2;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
        });

        for (String week : sortedWeeks) {
            System.out.println(week);
            List<MarketData> weekData = weekMap.get(week);

            for (int i = 2; i < weekData.size(); i += 3) {
                List<MarketData> group = weekData.subList(Math.max(i - 2, 0), i + 1);
                MarketData last = group.get(group.size() - 1);

                if (group.size() == 3 && isEMARising(group)) {
                    System.out.println("   - " + last.date + ": Market Rising - EMA rising 3 days in a row");
                }

                if (group.size() == 3 && isEMAFalling(group)) {
                    System.out.println("   - " + last.date + ": Trend Warning - EMA falling 3 days in a row");
                }

                if (group.size() == 3 && isSMAunderEMA(group)) {
                    System.out.println("   - " + last.date + ": Hold Off Buying - SMA below EMA for 3 days");
                }
            }
        }
    }

    static boolean isEMARising(List<MarketData> g) {
        return g.get(0).ema < g.get(1).ema && g.get(1).ema < g.get(2).ema;
    }

    static boolean isEMAFalling(List<MarketData> g) {
        return g.get(0).ema > g.get(1).ema && g.get(1).ema > g.get(2).ema;
    }

    static boolean isSMAunderEMA(List<MarketData> g) {
        return g.get(0).sma < g.get(0).ema && g.get(1).sma < g.get(1).ema && g.get(2).sma < g.get(2).ema;
    }
}
