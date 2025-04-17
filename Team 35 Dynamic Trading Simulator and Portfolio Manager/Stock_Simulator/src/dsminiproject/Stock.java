package dsminiproject;
import java.io.Serializable;
public class Stock implements Serializable,Comparable<Stock> {
	
	    
	    public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSector() {
		return sector;
	}


	public void setSector(String sector) {
		this.sector = sector;
	}


	public double getMarketCap() {
		return marketCap;
	}


	public void setMarketCap(double marketCap) {
		this.marketCap = marketCap;
	}


	public String getCapsize() {
		return capsize;
	}


	public void setCapsize(String capsize) {
		this.capsize = capsize;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public double getVolume() {
		return volume;
	}


	public void setVolume(double volume) {
		this.volume = volume;
	}


	public double getVolatility() {
		return volatility;
	}


	public void setVolatility(double volatility) {
		this.volatility = volatility;
	}


	 private static final long serialVersionUID = 1L;
		private String name;
	    private String sector;
	    private double marketCap; // in billions (e.g., Large Cap, Mid Cap, Small Cap)
	    private String capsize;
	    private double price;
	    private double volume;
        private double volatility;
        private String symbol;
        
       
        public String getSymbol() {
			return symbol;
		}


		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}


		Stock(String name, String sector, double volume,double price,double volatility, double marketcap,String capsize)
        {
        	this.name=name;
        	this.sector=sector;
        	this.volume=volume;
        	this.price=price;
        	this.volatility=volatility;
        	this.marketCap=marketcap;
        	this.capsize=capsize;
        	
        }
       Stock(long marketCap)
       {
           this.marketCap = marketCap;

       }
       @Override
       public int compareTo(Stock other) {
           // Compare by marketCap in descending order
           return Double.compare(other.marketCap, this.marketCap);
       }
        // Constructor, Getters, and Setters

        
        
        
        
        
        
	
	

}
