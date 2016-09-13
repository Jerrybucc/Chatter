package com.NBFLS.Online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TulingAPI implements Reachable{
	
	private String reply;
	
	public TulingAPI(String APIKey, String info) {
		try {
			connect(APIKey, info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TulingAPI() {
		
	}
	
	public void connect(String APIKey, String info) throws Exception{
	    String getURL = "http://www.tuling123.com/openapi/api?key=" + APIKey + "&info=" + info; 
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
	    setReply(filt(sb.toString(),"text"));
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	private String filt(String reply, String key) {
		JSONObject json = JSON.parseObject(reply);
		reply = json.get(key).toString();
		reply = reply.replaceAll("图灵机器人", "Jerry");
		return reply;
	}
	
	@Override
	public boolean reachable() {
		URL url;
		try {
		url = new URL("http://www.tuling123.com/openapi/api");
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
