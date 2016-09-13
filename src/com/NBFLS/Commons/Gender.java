package com.NBFLS.Commons;

import java.util.Random;

public class Gender implements Randomly{
		
	private boolean gender; //true for male, false for female

	public Gender(boolean gender) {
		this.gender = gender;
	}
	public Gender() {
		randomly();
	}
	
	public void randomly() {
		this.gender = new Random().nextBoolean();
	}

	public boolean getGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}
}
