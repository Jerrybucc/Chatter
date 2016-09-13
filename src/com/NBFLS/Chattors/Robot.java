package com.NBFLS.Chattors;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import javax.swing.JTextField;

import com.NBFLS.AI.ChatAI;
import com.NBFLS.AI.Talkable;
import com.NBFLS.Commons.Age;
import com.NBFLS.Commons.Gender;
import com.NBFLS.Commons.Name;
import com.NBFLS.Commons.Randomly;
import com.NBFLS.Online.HuangjiAPI;
import com.NBFLS.Online.Reachable;
import com.NBFLS.Online.SimsimiAPI;
import com.NBFLS.Online.TulingAPI;

public class Robot extends JTextField implements Randomly, Reachable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4430832556166065325L;
	
	ChatAI chatAI;
	Name name;
	Gender gender;
	Age age;
	
	AIListener aiListener;
	OnlineListener onlineListener;
	
	TulingAPI tulingAPI;
	SimsimiAPI simsimiAPI;
	HuangjiAPI huangjiAPI;
	
	public Robot(ChatAI chatAI,Name name, Gender gender, Age age) {
		this.chatAI = chatAI;
		this.name = name;
		this.gender = gender;
		this.age = age;
		autoOnline();
		setText(start());
	}
	
	public Robot(ChatAI chatAI){
		this.chatAI = chatAI;
		randomly();
		autoOnline();
		setText(start());
	}
	
	public Robot(){
		randomly();
		autoOnline();
		setText(start());
	}
	
	public void randomly() {
		this.name = new Name();
		this.gender = new Gender(true);
		this.age = new Age(17);
	}
	
	public void basicSet() {
		setVisible(true);
		setFont(new Font("Arial", Font.PLAIN , 20));
		setColumns(100);
	}
	
	public void autoOnline() {
		boolean reachable = new TulingAPI().reachable();
		if (reachable){
			generateOnline();
		}else{
			generate();
		}
	}
	
	public void generate() {
		basicSet();
		aiListener = new AIListener();
		addActionListener(aiListener);
		aiListener.start();
	}
	
	public void generateOnline(){
		basicSet();
		onlineListener = new OnlineListener();
		addActionListener(onlineListener);
		onlineListener.start();
	}
	
	public String start() {
		String name = this.name.getName();
		String gender = this.age.getAge()<=25?this.gender.getGender()?"Boy":"Girl":this.gender.getGender()?"Man":"Woman";
		String age = Integer.toString(this.age.getAge());
		return "Hi, My name is "+name+", I am a "+gender+", I am "+age+" years old now!";
	}
	
	@Override
	public void setText(String t) {
		try {
			super.setText(suitify(t));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class AIListener implements ActionListener {
		
		Robot robot = Robot.this;
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String input = robot.getText();
			robot.chatAI.addSentence(input);
			robot.setText(robot.chatAI.generateSentence());
			robot.setEditable(false);
			Timer timer = new Timer(); 
			timer.schedule(new Clear(), 2000);
		}
		
		public void start() {
			robot.setEditable(false);
			Timer timer = new Timer(); 
			timer.schedule(new Clear(), 2500);
		}
		
		class Clear extends TimerTask{
			Robot robot = AIListener.this.robot;
			public void run()
			  {
			    robot.setText("");
			    robot.setEditable(true);
			  }
		}
	}
	
	class OnlineListener implements ActionListener {
		
		Robot robot = Robot.this;
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String input = robot.getText();
			robot.chatAI.addSentence(input);
			String reply = switchSource(input);
			robot.chatAI.addSentence(reply);
			setText(reply);
			robot.setEditable(false);
			Timer timer = new Timer(); 
			timer.schedule(new Clear(), 2000);
		}
		
		public String switchSource(String input) {
			boolean isChi = Pattern.compile("[\u4e00-\u9fa5]").matcher(input).find();
			boolean isEng = input.matches("^[a-zA-Z]*");
			if(isEng){
				simsimiAPI = new SimsimiAPI(78763350, input);
				return simsimiAPI.getReply();
//				robot.removeActionListener(this);
//				robot.generate();
//				return robot.chatAI.generateSentence();
			}
			if (isChi){
				tulingAPI = new TulingAPI("e0f24e39e25b20dbb6e285d9c4ee2d26", input);
				return tulingAPI.getReply();
			}
			return "";
		}
		
		public void start() {
			robot.setEditable(false);
			Timer timer = new Timer(); 
			timer.schedule(new Clear(), 2500);
		}
		
		class Clear extends TimerTask{
			Robot robot = OnlineListener.this.robot;
			public void run()
			  {
			    robot.setText("");
			    robot.setEditable(true);
			  }
		}
	}

	@Override
	public boolean reachable() {
		return false;
	}

	@Override
	public String suitify(String str) throws Exception{
		return new String(str.getBytes(), "utf-8");
	}
}
