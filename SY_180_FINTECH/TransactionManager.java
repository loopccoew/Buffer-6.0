import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TransactionManager {
    private Map<Integer, List<Transaction>> userTransactions;
    private Map<Integer, Map<Integer, List<Integer>>> senderReceiverMap;
    private static final int AMOUNT_SPIKE_THRESHOLD = 2;
    private static final int REPEATED_LARGE_TRANSFER = 3;
    private static final int RAPID_TXN_COUNT = 3;
    private static final int CUMULATIVE_LIMIT = 10000;
    private static final int TIME_WINDOW_MINUTES = 10;

    public TransactionManager() {
        userTransactions = new HashMap<>();
        senderReceiverMap = new HashMap<>();
    }

    public void addTransaction(Transaction t) {
        userTransactions.putIfAbsent(t.senderId, new ArrayList<>());
        userTransactions.get(t.senderId).add(t);

        senderReceiverMap.putIfAbsent(t.senderId, new HashMap<>());
        senderReceiverMap.get(t.senderId).putIfAbsent(t.receiverId, new ArrayList<>());
        senderReceiverMap.get(t.senderId).get(t.receiverId).add(t.amount);

        detectSpike(t);
        detectRepeatedLargeTransfers(t);
        detectRapidTransactions(t);
        detectCumulativeTransfers(t);
    }

    private void detectSpike(Transaction t) {
        List<Transaction> txns = userTransactions.get(t.senderId);
        if (txns.size() < 5) return;

        int sum = 0;
        for (int i = 0; i < txns.size() - 1; i++) {
            sum += txns.get(i).amount;
        }
        double avg = sum / (double) (txns.size() - 1);
        if (t.amount > AMOUNT_SPIKE_THRESHOLD * avg) {
            System.out.println("\u274c FRAUD DETECTED: Sudden spike in amount for Sender " + t.senderId + ". Amount: ₹" + t.amount + ", which is more than " + AMOUNT_SPIKE_THRESHOLD + "x the average (\u20b9" + avg + ")");
        }
    }

    private void detectRepeatedLargeTransfers(Transaction t) {
        List<Integer> amounts = senderReceiverMap.get(t.senderId).get(t.receiverId);
        long largeCount = amounts.stream().filter(a -> a >= 5000).count();
        if (largeCount >= REPEATED_LARGE_TRANSFER) {
            System.out.println("\u274c FRAUD DETECTED: Repeated large transfers (>= ₹5000) to Receiver " + t.receiverId + " from Sender " + t.senderId);
        }
    }

    private void detectRapidTransactions(Transaction t) {
        List<Transaction> txns = userTransactions.get(t.senderId);
        if (txns.size() < RAPID_TXN_COUNT) return;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        int recentCount = 0;
        LocalDateTime current = LocalDateTime.parse(t.timestamp, formatter);

        for (int i = txns.size() - 2; i >= 0 && recentCount < RAPID_TXN_COUNT - 1; i--) {
            LocalDateTime prev = LocalDateTime.parse(txns.get(i).timestamp, formatter);
            if (Duration.between(prev, current).toMinutes() <= 1) {
                recentCount++;
                current = prev;
            } else {
                break;
            }
        }

        if (recentCount >= RAPID_TXN_COUNT - 1) {
            System.out.println("\u274c FRAUD DETECTED: Rapid multiple transactions detected within 1 minute for Sender " + t.senderId);
        }
    }

    private void detectCumulativeTransfers(Transaction t) {
        List<Transaction> txns = userTransactions.get(t.senderId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime current = LocalDateTime.parse(t.timestamp, formatter);

        int total = 0;
        for (Transaction tr : txns) {
            LocalDateTime time = LocalDateTime.parse(tr.timestamp, formatter);
            if (Duration.between(time, current).toMinutes() <= TIME_WINDOW_MINUTES) {
                total += tr.amount;
            }
        }

        if (total >= CUMULATIVE_LIMIT) {
            System.out.println("\u274c FRAUD DETECTED: Cumulative transfers of ₹" + total + " in last " + TIME_WINDOW_MINUTES + " minutes from Sender " + t.senderId);
        }
    }

    public void printAllTransactions() {
        System.out.println("\nAll Transactions:");
        for (List<Transaction> list : userTransactions.values()) {
            for (Transaction t : list) {
                System.out.println(t);
            }
        }
    }
}