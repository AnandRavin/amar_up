package com.cypherx.userutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.cypherx.dto.TokenDTO;

@Service
public class CurrentValueUtils {
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }
	
	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
	  }
	
	public Double getBitcoinValueForOneDollar() throws JSONException, IOException{
		JSONObject json = readJsonFromUrl("https://api.coinbase.com/v2/prices/BTC-USD/spot");
	    JSONObject json2 = (JSONObject) json.get("data");
	    System.out.println(json.toString());
	    System.out.println(json.get("data"));
	    System.out.println(json2.get("amount"));
	    String amount = (String) json2.get("amount");
	    BigDecimal Dollars = new BigDecimal(amount);
	    System.out.println("Big Decimal: "+Dollars);
	    System.out.println("Dollars: "+ Dollars.doubleValue());
	    System.out.println("One Dollar is equal to Btc: "+1/Dollars.doubleValue());
	    Double a = 1/Dollars.doubleValue();
	    DecimalFormat df = new DecimalFormat("#.########");
	    System.out.println("Final Output: "+df.format(a));
	    double value = Double.parseDouble(df.format(a));
		return value;
	}
	 
	public Double getEtherValueForOneDollar() throws JSONException, IOException{
		JSONObject json = readJsonFromUrl("https://api.coinbase.com/v2/prices/ETH-USD/spot");
	    JSONObject json2 = (JSONObject) json.get("data");
	    
	    System.out.println(json.toString());
	    System.out.println(json.get("data"));
	    System.out.println(json2.get("amount"));
	    
	    String amount = (String) json2.get("amount");
	    BigDecimal Dollars = new BigDecimal(amount);
	   
	    System.out.println("Big Decimal: "+Dollars);
	    System.out.println("Dollars: "+ Dollars.doubleValue());
	    System.out.println("One Dollar is equal to Btc: "+1/Dollars.doubleValue());
	   
	    Double a = 1/Dollars.doubleValue();
	    DecimalFormat df = new DecimalFormat("#.###############");
	   
	    System.out.println("Final Output: "+df.format(a));
	   
	    double value = Double.parseDouble(df.format(a));
	    
		return value;
	}
}

