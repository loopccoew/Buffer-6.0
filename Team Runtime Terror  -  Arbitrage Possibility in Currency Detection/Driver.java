import java.util.*;

public class Driver {
    public static void main(String[] args) {
        boolean isError = false;

        CurrencyArbitrage ca = new CurrencyArbitrage();
        Scanner sc = new Scanner(System.in);

        Map<Integer, String> availableCurrencies = ca.getIndexToCurrency();

        System.out.println("List of currencies : ");
        for(Integer idx : availableCurrencies.keySet()) {
            System.out.println(idx + "\t : \t" + availableCurrencies.get(idx));
        }

        do {
            System.out.println("\n");
            System.out.println("Enter the source currency index : ");
            int src = sc.nextInt();
            System.out.println("Enter the destination currency index : ");
            int dest = sc.nextInt();

            String srcCurr = ca.getIndexToCurrency().get(src);
            String destCurr = ca.getIndexToCurrency().get(dest);

            isError = ca.findMostProfitablePath(srcCurr, destCurr);
        } while(isError);

    }
}

