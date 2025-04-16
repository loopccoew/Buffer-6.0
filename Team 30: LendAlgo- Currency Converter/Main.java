import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

//Represents a currency with a name and its conversion-rate relative to base currency= USD
class Currency{
    String name;
    double rate;

    //Constructs a new Currency object
    public Currency(String name, double rate){
        this.name= name;
        this.rate= rate;
    }
}

//Stores and manages multiple currencies and their exchange rates
class CurrencyStore{
    private HashMap<String, Currency> currency;

    public CurrencyStore(){
        currency= new HashMap<>();

        addCurrency("USD", 1.00);  //United States
        addCurrency("AUD", 0.6197);  //Australia
        addCurrency("CAD", 1.3890);  //Canada
        addCurrency("EUR", 0.9346);  //Europe
        addCurrency("JPY", 143.40);  //Japan
        addCurrency("GBP", 0.7668);  //United Kingdom
        addCurrency("CHF", 0.9252);  //Switzerland
        addCurrency("INR", 85.61);  //India
        addCurrency("CNY", 7.3191);  //China
        addCurrency("BRL", 5.8731);  //Brazil
        addCurrency("ZAR", 18.50);  //South Africa
        addCurrency("MXN", 18.23);  //Mexico
        addCurrency("SAR", 3.75);  //Saudi Arabia
        addCurrency("AED", 3.67);  //United Arab Emirates
        addCurrency("HKD", 7.85);  //Hong Kong
        addCurrency("KRW", 1335.00);  //South Korea
        addCurrency("SGD", 1.36);  //Singapore
        addCurrency("NZD", 0.5979);  //New Zealand
        addCurrency("TRY", 27.31);  //Turkey
        addCurrency("IDR", 15890.00);  //Indonesia
        addCurrency("THB", 34.68);  //Thailand
        addCurrency("RUB", 96.45);  //Russia
        addCurrency("MYR", 4.70);  //Malaysia
        addCurrency("VND", 23750.00);  //Vietnam
        addCurrency("PKR", 285.00);  //Pakistan
        addCurrency("COP", 4753.55);  //Colombia
        addCurrency("CLP", 796.02);  //Chile
        addCurrency("PHP", 56.02);  //Philippines
        addCurrency("PLN", 4.32);  //Poland
        addCurrency("HUF", 366.02);  //Hungary
        addCurrency("CZK", 22.62);  //Czech Republic
        addCurrency("SEK", 10.87);  //Sweden
        addCurrency("NOK", 10.64);  //Norway
        addCurrency("DKK", 6.94);  //Denmark
        addCurrency("KWD", 0.3077);  //Kuwait
        addCurrency("BHD", 0.377);  //Bahrain
        addCurrency("OMR", 0.3845);  //Oman
        addCurrency("QAR", 3.64);  //Qatar
    }

    //Adds a currency
    public void addCurrency(String name, double rate){
        currency.put(name, new Currency(name, rate));
    }

    //Removes a currency
    public void removeCurrency(String name){
        if(currency.containsKey(name)){
            currency.remove(name);
            System.out.println("Currency " + name + " removed.");
        } 
        else{
            System.out.println("Currency not found.");
        }
    }

    //Validates a currency
    public boolean isValid(String name){
        return currency.containsKey(name);
    }

    //Returns the exchange rate of the given currency
    public double getRate(String name){
        return currency.get(name).rate;
    }

    //Displays all currencies with their rates
    public void displayAvailableCurrencies(){
        System.out.println("Available Currencies:");
        for(String name : currency.keySet()){
            System.out.printf("%s (Rate: %.2f)\n", name, getRate(name));
        }
    }

    //Converts amount from one currency to another
    public BigDecimal convert(String from, String to, double amount){
        double conversion= amount * getRate(to) / getRate(from);
        return BigDecimal.valueOf(conversion).setScale(2, RoundingMode.HALF_UP);
    }
}

//Stores a single currency conversion record
class Conversion{
    private String fromCurrency, toCurrency;
    private double amount;
    private BigDecimal result;
    private double exchangeRate;

    //Constructs a new Conversion object
    public Conversion(String fromCurrency, String toCurrency, double amount, BigDecimal result, double exchangeRate){
        this.fromCurrency= fromCurrency;
        this.toCurrency= toCurrency;
        this.amount= amount;
        this.result= result;
        this.exchangeRate= exchangeRate;
    }

    //Returns conversion
    public String displayConversion(){
        return String.format("%.2f %s = %.2f %s (Exchange Rate: %.2f)",
                amount, fromCurrency, result.doubleValue(), toCurrency, exchangeRate);
    }

    @Override
    public String toString(){
        return displayConversion();
    }
}

//Manages conversion history and provides undo/redo functionality
class ConversionManager{
    Stack<Conversion> conversionStack= new Stack<>(); //Stack for undo operations
    Stack<Conversion> redoStack= new Stack<>(); //Stack for redo operations
    ArrayList<Conversion> conversionList= new ArrayList<>(); //Stores the conversion history

    //Adds a conversion to the history. Clears the redo stack upon new entry
    void addConversion(Conversion conversion){
        conversionStack.push(conversion);
        conversionList.add(conversion);
        redoStack.clear();
        System.out.println("Conversion added: " + conversion);
    }

    //Undoes the last conversion operation
    void undoConversion(){
        if(conversionStack.isEmpty()){
            System.out.println("No conversions to undo.");
        }
        else{
            Conversion undoneConversion= conversionStack.pop();
            redoStack.push(undoneConversion);
            conversionList.remove(undoneConversion);
            System.out.println("Undoing conversion: " + undoneConversion);
        }
    }

    //Redoes the last conversion operation
    void redoConversion(){
        if(redoStack.isEmpty()){
            System.out.println("No conversions to redo.");
        }
        else{
            Conversion redoneConversion= redoStack.pop();
            conversionStack.push(redoneConversion);
            conversionList.add(redoneConversion);
            System.out.println("Redoing conversion: " + redoneConversion);
        }
    }

    //Displays all conversions
    void displayAllConversions(){
        if(conversionList.isEmpty()){
            System.out.println("No conversions have been made yet.");
        }
        else{
            System.out.println("All Conversions:");
            for(Conversion conversion : conversionList){
                System.out.println(conversion);
            }
        }
    }
}

//MAIN CLASS
public class Main{
    public static void main(String[] args){
        Scanner scan= new Scanner(System.in);
        CurrencyStore storage= new CurrencyStore(); //Available currencies
        ConversionManager manager= new ConversionManager(); //Manager for conversion history and undo/redo

        while(true){
            //Display menu options
            System.out.println("\nChoose an action:");
            System.out.println("1. Convert currency");
            System.out.println("2. Undo last action");
            System.out.println("3. Redo last action");
            System.out.println("4. View conversion history");
            System.out.println("5. View available currencies");
            System.out.println("6. Add a new currency");
            System.out.println("7. Remove a currency");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");
            int option;

            //Validate menu option input
            if(scan.hasNextInt()){
                option= scan.nextInt();
                scan.nextLine(); //Consume newline
            }
            else{
                System.out.println("Invalid input. Please enter a number.");
                scan.nextLine(); //Clear invalid input
                continue;
            }

            switch(option){
                case 1:
                    System.out.print("Enter source currency (e.g., USD, INR): ");
                    String src= scan.nextLine().toUpperCase();

                    //Validate source currency - if missing, offer to add it
                    while(!storage.isValid(src)){
                        System.out.println("Source currency not found: " + src);
                        System.out.print("Would you like to add it? (Y/N): ");
                        String choice= scan.nextLine().toUpperCase();
                        if(choice.equals("Y")){
                            System.out.print("Enter exchange rate relative to USD: ");
                            double rate;
                            if(scan.hasNextDouble()){
                                rate= scan.nextDouble();
                                scan.nextLine();
                                storage.addCurrency(src, rate);
                            }
                            else{
                                System.out.println("Invalid rate. Skipping addition.");
                                scan.nextLine();
                            }
                        } 
                        else{
                            System.out.print("Re-enter source currency: ");
                            src= scan.nextLine().toUpperCase();
                        }
                    }

                    System.out.print("Enter target currency: ");
                    String fin= scan.nextLine().toUpperCase();

                    //Validate final currency- if missing, offer to add it
                    while(!storage.isValid(fin)){
                        System.out.println("Target currency not found: " + fin);
                        System.out.print("Would you like to add it? (Y/N): ");
                        String choice= scan.nextLine().toUpperCase();
                        if(choice.equals("Y")){
                            System.out.print("Enter exchange rate relative to USD: ");
                            double rate;
                            if(scan.hasNextDouble()){
                                rate= scan.nextDouble();
                                scan.nextLine();
                                storage.addCurrency(fin, rate);
                            } 
                            else{
                                System.out.println("Invalid rate. Skipping addition.");
                                scan.nextLine();
                            }
                        } 
                        else{
                            System.out.print("Re-enter target currency: ");
                            fin= scan.nextLine().toUpperCase();
                        }
                    }

                    //Prevent converting the same currency
                    if(src.equals(fin)){
                        System.out.println("Source and target currencies cannot be the same.");
                        break;
                    }

                    System.out.print("Enter amount: ");
                    double amt= 0;
                    boolean validAmount= false;

                    //Validate amount
                    while(!validAmount){
                        if(scan.hasNextDouble()){
                            amt= scan.nextDouble();
                            scan.nextLine(); // Consume newline
                            validAmount= true;
                        }
                        else{
                            System.out.println("Invalid input. Please enter a valid number.");
                            scan.nextLine(); // Clear invalid input
                        }
                    }

                    //Conversion
                    BigDecimal result= storage.convert(src, fin, amt);
                    double exRate= storage.getRate(fin) / storage.getRate(src);
                    Conversion conversion= new Conversion(src, fin, amt, result, exRate);
                    manager.addConversion(conversion);

                    // AFTER conversion, offer to view the reverse conversion
                    System.out.print("Would you like to switch the conversion direction? (Y/N): ");
                    String swap= scan.nextLine().toUpperCase();
                    if(swap.equals("Y")){
                        BigDecimal switchedResult= storage.convert(fin, src, amt);
                        double switchedExRate= storage.getRate(src) / storage.getRate(fin);
                        Conversion switchedConversion= new Conversion(fin, src, amt, switchedResult, switchedExRate);
                        manager.addConversion(switchedConversion);
                        System.out.println("Switched conversion result: " + switchedConversion);
                    }
                    break;

                case 2:
                    manager.undoConversion();
                    break;

                case 3:
                    manager.redoConversion();
                    break;

                case 4:
                    manager.displayAllConversions();
                    break;

                case 5:
                    storage.displayAvailableCurrencies();
                    break;

                case 6:
                    System.out.print("Enter currency name to add: ");
                    String newName= scan.nextLine().toUpperCase();
                    if(storage.isValid(newName)){
                        System.out.println("Currency already exists.");
                    } 
                    else{
                        System.out.print("Enter exchange rate relative to USD: ");
                        if(scan.hasNextDouble()){
                            double newRate= scan.nextDouble();
                            scan.nextLine();
                            storage.addCurrency(newName, newRate);
                            System.out.println("Currency added successfully.");
                        } 
                        else{
                            System.out.println("Invalid rate. Currency not added.");
                            scan.nextLine();
                        }
                    }
                    break;

                case 7:
                    System.out.print("Enter currency name to remove: ");
                    String removeName= scan.nextLine().toUpperCase();
                    storage.removeCurrency(removeName);
                    break;

                case 8:
                    System.out.println("Exiting program...");
                    scan.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
