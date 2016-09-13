/**
 * 
 */
package com.NBFLS.Chattors;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.NBFLS.AI.ChatAI;


/**
 * @author Jerry Chen
 *
 */
public class Main extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5385810049571673991L;
	
	static{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	
	public Main() throws UnsupportedEncodingException {
		
		add(new Robot(new ChatAI("brain.dat")));
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
	}
	public static void main(String[] args) throws UnsupportedEncodingException{
		new Main();

	}

}
