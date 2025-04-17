package dsminiproject;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class RealTimeStockPrice  {
	
		  public String getStockPrice(String symbol) throws IOException {
		        // Construct the URL for the given stock symbol
		        String urlString = "https://www.google.com/finance/quote/"+symbol+":NSE?hl=en";
		        URL url = new URL(urlString);
		        URLConnection urlConn = url.openConnection();
				InputStreamReader instream = new InputStreamReader(urlConn.getInputStream());
				BufferedReader buff = new BufferedReader(instream);
        
				String price = "not found";
				String line=buff.readLine();
				while(line != null)
				{
					//("[\"TCS\",");
					if(line.contains("[\""+symbol+"\","))
					{
						//System.out.println(line);
						int target = line.indexOf("[\""+symbol+"\",");
						int deci = line.indexOf(".", target);
						int start = deci;
						while(line.charAt(start) != '[')
						{
							start--;
						}
						price = line.substring(start+1,deci +3);
					}
					//System.out.println(line);
					line = buff.readLine();
					
				}
//		        
	//	        buff.close();
//			  URL url=new URL("https://www.google.com/finance/quote/TCS:NSE?hl=en");
				
		        return price;
		    }
	 }


