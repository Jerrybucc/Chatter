package com.NBFLS.Online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class SimsimiAPI implements Reachable{
private String reply;
	
	public SimsimiAPI(int uid, String info) {
		try {
			connect(uid, info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public SimsimiAPI() {
		
	}
	
	public void connect(int uid, String info) throws Exception{
	    String getURL = "http://www.simsimi.com/requestChat?lc=en&ft=1.0&req="+ info +"&uid="+ uid +"&did=0"; 
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
	    setReply(filt(sb.toString(),"res","msg"));
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	private String filt(String reply,String key,String subkey) {
		JSONObject json = JSON.parseObject(reply);
		reply = ((JSONObject)json.get(key)).get(subkey).toString();
		String regex = "I HAVE NO RESPONSE.<br>PLEASE TEACH ME.<br>Android: <a href=\'javascript:clickTeach(\\\"A\\\");\'>simsimi.com/app/android</a><br>iOS: <a href=\'javascript:clickTeach(\\\"I\\\");\'>simsimi.com/app/iOS</a>";
		String replacemnet = "What u said doesn't make any sense.";
		reply = reply.replace(regex, replacemnet);
		return reply;
	}
	
	@Override
	public boolean reachable() {
		URL url;
		try {
		url = new URL("http://www.simsimi.com");
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
