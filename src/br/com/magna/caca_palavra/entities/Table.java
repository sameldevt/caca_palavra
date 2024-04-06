package br.com.magna.caca_palavra.entities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Table {
	
	private char[][] matrix = new char[26][26];
	private List<String> words = new ArrayList<String>();
	
	public Table() {
		loadContent();
	}
	
	public char[][] getMatrix(){
		return matrix;
	}
	
	public List<String> getWords(){
		return words;
	}
	
	public void printMatrix() {
		char column = 65;
		System.out.println("   00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25\n");
		for(int i = 0; i < matrix.length; i++) {
			System.out.print(column + "  ");
			for(int j = 0; j < matrix[i].length; j++) {
				
				if(Cursor.pin1[0] == i && Cursor.pin1[1] == j) {
					System.out.print("{" + matrix[i][j] + "}");
					continue;
				}
				
				if(Cursor.pin2[0] == i && Cursor.pin2[1] == j) {
					System.out.print("{" + matrix[i][j] + "}");
					continue;
				}
				
				if(Cursor.xPosition == i && Cursor.yPosition == j) {
					System.out.print("{" + matrix[i][j] + "}");
					continue;
				}

				System.out.print(" " + matrix[i][j] + " ");
			}	
			column++;
			System.out.println();
		}
	}
	
	private void loadContent() {
		loadWordList();
		loadMatrix();
		loadRandomWords();
	}

	
	private void loadWordList() {
		try(Scanner scan = new Scanner(new File("util/words.txt"))){
			while(scan.hasNext()) {
				String line = scan.nextLine();
				words.add(line);
			}
		}
		catch(IOException e) {
			throw new RuntimeException("Error reading archive 'util/words.txt'");
		}
	}
	
	private void loadMatrix() {
		Random r = new Random();
		
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++) {
				//matrix[i][j] = ' ';
				matrix[i][j] = (char) r.nextInt(65, 90);;
			}	
		}
	}
	
	private void loadRandomWords() {
		Random r = new Random();
		int randomWordId = 0;
		int wordCount = 0;
		
		while(wordCount < 10) {
			randomWordId = r.nextInt(0, words.size() - 1);
			String randomWord = words.get(randomWordId).toUpperCase();
			
			int randomLine = r.nextInt(0, matrix.length);
			int randomColumn = r.nextInt(0, matrix.length);
			
			if(matrix.length - randomColumn > randomWord.length()) {
				for(int column = randomColumn, charpos = 0; charpos < randomWord.length(); column++, charpos++) {
					matrix[randomLine][column] = randomWord.charAt(charpos);
				}
			}
			wordCount++;
		}
		
		wordCount = 0;
		while(wordCount < 10) {
			randomWordId = r.nextInt(0, words.size() - 1);
			String randomWord = words.get(randomWordId).toUpperCase();
			
			int randomLine = r.nextInt(0, matrix.length);
			int randomColumn = r.nextInt(0, matrix.length);
			
			if(matrix.length - randomLine > randomWord.length()) {
				for(int line = randomLine, charpos = 0; charpos < randomWord.length(); line++, charpos++) {
					matrix[line][randomColumn] = randomWord.charAt(charpos);
				}
			}
			wordCount++;
		}
	}
}
