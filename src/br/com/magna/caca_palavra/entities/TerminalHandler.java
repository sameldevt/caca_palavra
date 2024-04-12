package br.com.magna.caca_palavra.entities;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class TerminalHandler {
	
	public static final String WIN = "util/win.txt";
	public static final String TUTORIAL = "util/tutorial.txt";
	public static final String HOW_TO_PLAY = "util/how-to-play.txt";
	
	public static void clear() {for(int i = 0; i<50; i++) {System.out.println();}}

	public static void printScreen(String path) {
		try(Scanner scan = new Scanner(new File(path))){
			while(scan.hasNext()) {
				System.out.println(scan.nextLine());
			}
			System.out.println();
		}
		catch(IOException e) {
			
		}
	}
}
