package com.NBFLS.Online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HuangjiAPI implements Reachable{
	
private String reply;
	
	public HuangjiAPI(String input) {
		try {
			connect(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public HuangjiAPI() {
		
	}
	
	public void connect(String input) throws Exception{
	    String getURL = "http://www.xiaodoubi.com/simsimiapi.php?msg=" + input; 
	    URL getUrl = new URL(getURL); 
	    HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection(); 
	    connection.connect(); 


	    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
	    StringBuffer sb = new StringBuffer(); 
	    String line = ""; 
	    while ((line = reader.readLine()) != null) { 
	        sb.append(line); 
	    } 
	    reader.close();
	    connection.disconnect(); 
//	    setReply(suitify(filt(sb.toString().split("\"text\":\"")[1].split("\"}")[0])));
	    System.out.println(sb.toString());
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	private String filt(String reply) {
		reply = reply.replaceAll("图灵机器人", "Jerry");
		return reply;
	}
	
	@Override
	public boolean reachable() {
		URL url;
		try {
		url = new URL("http://www.xiaodoubi.com/simsimiapi.php");
		InputStream in = url.openStream();
		in.close();
		return true;
		} catch (IOException e) {
		return false;
		}
	}

	@Override
	public String suitify(String str) throws Exception{
		return new String(str.getBytes(), "utf-8");
	}
}
