package com.NBFLS.Commons;

import java.util.Random;

public class Age implements Randomly{
	
	private int age;
	
	public Age(int age) {
		this.setAge(age);
	}
	
	public Age() {
		randomly();
	}
	
	public void randomly() {
		this.setAge(new Random().nextInt(120));
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
