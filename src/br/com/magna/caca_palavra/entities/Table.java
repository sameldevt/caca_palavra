package br.com.magna.caca_palavra.entities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Table {
	private char[][] matrix = new char[26][26];
	private List<String> words = new ArrayList<String>();
	private List<String> foundWords = new ArrayList<String>();
	private Map<Character, Integer> map = new HashMap<>();
    private Pattern pattern = Pattern.compile("[a-zA-Z][0123456789]{2}");
	
	public Table() {
		fillWordList();
		initiateMatrix();
		setRandomWords();
		fillMap();
	}
	
	private void clear() {for(int i = 0; i<50; i++) {System.out.println();}}
	
	private void printFoundWords() {
		for(String s : foundWords) {
			System.out.print(s + " ");
		}
	}
	private void fillMap() {
		Character character = 65;
		for(int i = 0; i < 26; i++) {
			map.put(character, i);
			character++;
		}
	}
	
	private void printWin() {
		try(Scanner scan = new Scanner(new File("util/win.txt"))){
			while(scan.hasNext()) {
				System.out.println(scan.nextLine());
			}
			System.out.println();
		}
		catch(IOException e) {
			
		}
	}
	
	public boolean guessWord() {
		Scanner scan = new Scanner(System.in);
		System.out.println("cords (A01-B02)");
		System.out.print("> ");
		String input = scan.nextLine();
		
		if(pattern.matcher(input).find()) {
			String formatedInput = input.toUpperCase();
			
			Character pos1LineChar = formatedInput.charAt(0);
			Integer pos1Column = Integer.parseInt(formatedInput.substring(1, 3));
			
			Character pos2LineChar = formatedInput.charAt(4);
			Integer pos2Column = Integer.parseInt(formatedInput.substring(5, 7));
			
			Integer pos1Line = map.get(pos1LineChar);
			Integer pos2Line = map.get(pos2LineChar);
			
			String secretWord = "";
			
			// check column
			if(pos1Line != pos2Line && pos1Column == pos2Column) {
				try {
					for(int line = pos1Line; line <= pos2Line; line++) {
						secretWord += matrix[line][pos1Column];
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("Posição inválida!");
					return false;
				}
				
				if(words.contains(secretWord)) {
					for(int line = pos1Line; line <= pos2Line; line++) {
						matrix[line][pos1Column] = '-';
					}
					foundWords.add(secretWord);
					clear();
					printMatrix();
					printFoundWords();
				}
			}
			
			// check line
			if(pos1Line == pos2Line && pos1Column != pos2Column) {
				try {
					for(int column = pos1Column; column <= pos2Column; column++) {
						secretWord += matrix[pos1Line][column];
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("Posição inválida!");
					return false;
				}
				
				if(words.contains(secretWord)) {
					for(int column = pos1Column; column <= pos2Column; column++) {
						matrix[pos1Line][column] = '-';
					}
					foundWords.add(secretWord);
					clear();
					printMatrix();
					printFoundWords();
				}
			}
			
			if(foundWords.size() == 5) {
				clear();
				printWin();
				printFoundWords();
				System.exit(0);
			}
			
			System.out.println();
			return true;
		}
		
		System.out.println("Posição ou entrada inválida!");
		return false;
	}
	
	public void printMatrix() {
		char column = 65;
		System.out.println("   00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25\n");
		for(int i = 0; i < matrix.length; i++) {
			System.out.print(column + "  ");
			for(int j = 0; j < matrix[i].length; j++) {
				System.out.print(" " + matrix[i][j] + " ");
			}	
			column++;
			System.out.println();
		}
	}
	
	private void fillWordList() {
		try(Scanner scan = new Scanner(new File("util/words.txt"))){
			while(scan.hasNext()) {
				String line = scan.nextLine();
				words.add(line);
			}
		}
		catch(IOException e) {
			
		}
	}
	
	private void setRandomWords() {
		Random r = new Random();
		int randomWordId = 0;
		int i = 0;
		
		while(i < 10) {
			randomWordId = r.nextInt(0, words.size() - 1);
			String randomWord = words.get(randomWordId).toUpperCase();
			
			int randomLine = r.nextInt(0, matrix.length);
			int randomColumn = r.nextInt(0, matrix.length);
			
			if(matrix.length - randomColumn > randomWord.length()) {
				for(int column = randomColumn, charpos = 0; charpos < randomWord.length(); column++, charpos++) {
					matrix[randomLine][column] = randomWord.charAt(charpos);
				}
			}
			i++;
		}
		
		i = 0;
		while(i < 5) {
			randomWordId = r.nextInt(0, words.size() - 1);
			String randomWord = words.get(randomWordId).toUpperCase();
			
			int randomLine = r.nextInt(0, matrix.length);
			int randomColumn = r.nextInt(0, matrix.length);
			
			if(matrix.length - randomLine > randomWord.length()) {
				for(int line = randomLine, charpos = 0; charpos < randomWord.length(); line++, charpos++) {
					matrix[line][randomColumn] = randomWord.charAt(charpos);
				}
			}
			i++;
		}
	}
	
	private void initiateMatrix() {
		Random r = new Random();
		
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = ' ';
				//matrix[i][j] = (char) r.nextInt(65, 90);;
			}	
		}
	}
}
