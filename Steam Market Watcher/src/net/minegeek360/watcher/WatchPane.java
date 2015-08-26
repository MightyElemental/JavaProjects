package net.minegeek360.watcher;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.xswingx.PromptSupport;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SuppressWarnings("serial")
public class WatchPane extends JPanel{
	
	public String curPriceText = "Average Price: ";
	public String lowPriceText = "Lowest Price: ";
	public String shiftPriceText = "Price Shift: ";
	
	public JLabel curPrice = new JLabel(curPriceText);
	public JLabel lowPrice = new JLabel(lowPriceText);
	public JLabel shiftPrice = new JLabel(shiftPriceText);
	public JTextField enterUrl = new JTextField("");
	
	public float totalPrices;
	public float average;
	public float lowestPrice;
	public float oldLowestPrice;
	public float totalPriceShift;
	
	public String lastUsedUrl = "";
	
	public Color improve = new Color(0,160,0);
	public Color worse = Color.red;
	
	ArrayList<Float> prices = new ArrayList<Float>();
	
	public WatchPane(){
		this.setLayout(new GridLayout(4,1));
		add(enterUrl);
		add(curPrice);
		add(lowPrice);
		add(shiftPrice);
		PromptSupport.setPrompt("Enter URL of steam item", enterUrl);
	}
	
	private int updates;
	
	public void update(){
		if(enterUrl.getText().startsWith("http://steamcommunity.com")){
			enterUrl.setBackground(improve);
			try {
				prices = getPrices(readDataFromURL(enterUrl.getText() +"/render/.json?start=0&count=50&currency=3&language=english&format=json"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(!lastUsedUrl.equals(enterUrl.getText())){
				updates = 0;
				totalPriceShift = 0;
			}
			printData();
			if(calcAverage()){
				calcLowestPrices();
				if(updates > 0){
					calcPriceShift();
				}else{
					oldLowestPrice = lowestPrice;
					System.out.println(oldLowestPrice);
				}
				updates++;
			}
		}else{
			enterUrl.setBackground(worse);
		}
		lastUsedUrl = enterUrl.getText();
	}
	
	public void calcPriceShift(){
		float temp = lowestPrice-oldLowestPrice;
		if(temp > 600){
			return;
		}
		totalPriceShift = totalPriceShift+temp;
		totalPriceShift = (float) (Math.ceil(totalPriceShift*100)/100);
		shiftPrice.setText(shiftPriceText+totalPriceShift);
		oldLowestPrice = lowestPrice;
		if(totalPriceShift < 0){
			shiftPrice.setForeground(improve);
		}else{
			shiftPrice.setForeground(worse);
		}
	}
	
	public void calcLowestPrices(){
		float temp = lowestPrice;
		lowestPrice = Float.MAX_VALUE;
		for(int i = 0; i < prices.size(); i++){
			if(prices.get(i) < lowestPrice){
				lowestPrice = prices.get(i);
			}
		}
		if(temp < lowestPrice){
			lowPrice.setForeground(worse);
		}else{
			lowPrice.setForeground(improve);
		}
		lowPrice.setText(lowPriceText+lowestPrice);
	}
	
	public boolean calcAverage(){
		average=0;
		totalPrices = 0;
		for(int i = 0; i < prices.size(); i++){
			totalPrices+=prices.get(i);
		}
		average = totalPrices/prices.size();
		average = (float) (Math.ceil(average*100)/100);
		System.out.println("AVERAGE "+average+" | size "+prices.size()+" | total prices "+totalPrices);
		curPrice.setText(curPriceText+average);
		if(Float.isNaN(average)){
			curPrice.setForeground(worse);
			lowPrice.setText(lowPriceText+"NaN");
			lowPrice.setForeground(worse);
			shiftPrice.setText(shiftPriceText+"NaN");
			shiftPrice.setForeground(worse);
			return false;
		}else{
			curPrice.setForeground(Color.BLACK);
			return true;
		}
	}
	
	public void printData(){
		try {
			System.out.println(readDataFromURL(enterUrl.getText()+"/render/.json?start=0&count=50&currency=3&language=english&format=json").get("listinginfo"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*for(int i = 0; i < prices.size(); i++){
			System.out.println(prices.get(i));
		}*/
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Float> getPrices(JsonObject jsonIn){
		Gson g = new Gson();
		ArrayList<Float> prices = new ArrayList<Float>();
		JsonObject json = (JsonObject)jsonIn.get("listinginfo");
		Map<String,String> map = new HashMap<String,String>();
		map =(Map<String,String>) g.fromJson(json, map.getClass());
		for(String item : map.keySet()){
			try{
				float temp = (
						((JsonObject)json
						.get(item.toString()))
						.get("converted_price")
						.getAsInt());
				temp*=0.82189f;
				temp/=100f;
				temp = (float) (Math.floor(temp*100)/100);
				prices.add(temp);
			}catch(NullPointerException e){
				System.out.println("Error while getting prices");
			}
		}
		return prices;
	}
	
	public JsonObject readDataFromURL(String sURL) throws IOException{

	    // Connect to the URL using java's native library
	    URL url = new URL(sURL);
	    HttpURLConnection request = (HttpURLConnection) url.openConnection();
	    request.setReadTimeout(20*1000);
	    request.connect();

	    // Convert to a JSON object to print data
	    JsonParser jp = new JsonParser(); //from gson
	    JsonElement root = jp.parse(
	    				new InputStreamReader(
	    						(InputStream) request.getContent())); //convert the input stream to a json element
	    JsonObject rootobj = root.getAsJsonObject(); //may be an array, may be an object. 
	    //String zipcode=rootobj.get("zipcode").getAsString();//just grab the zipcode
	    request.disconnect();
	    return rootobj;
	}
	
}
