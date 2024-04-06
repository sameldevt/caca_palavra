package br.com.magna.caca_palavra.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Game {
	
	private Table t = new Table();
	private Pattern regex = Pattern.compile("[a-zA-Z][0123456789]{2}");
	private Scanner scan = new Scanner(System.in);
	
	private List<String> words = t.getWords();
	private List<String> foundWords = new ArrayList<String>();	
	private Map<Character, Integer> map = new HashMap<>();
    
	private char[][] tableMatrix = t.getMatrix();
	private boolean isGameWon = false;	
	
	public void start() {
		mapAlphabetToIndex();
		gameLoop();
	}
	
	private void gameLoop() {
		while(!isGameWon) {
			TerminalHandler.clear();
			t.printMatrix();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			printFoundWords();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(!tryPosition()) {
				System.out.println("Posição ou entrada inválida!");
			}
			
			if(verifyWinCondition()) {
				TerminalHandler.clear();
				TerminalHandler.printScreen(TerminalHandler.WIN);
				isGameWon = true;
				printFoundWords();
			}
		}
	}
	
	private boolean verifyWinCondition() {
		return foundWords.size() == 5;
	}
	
	private boolean tryPosition() {
		System.out.println("\nDigite as posições que deseja (Ex: a01 a12)");
		System.out.print("> ");
		
		try {
			String input = scan.nextLine();
			
			if(regex.matcher(input).find()) {
				String formatedInput = input.toUpperCase();
				
				Character pos1LineChar = formatedInput.charAt(0);
				Integer pos1Column = Integer.parseInt(formatedInput.substring(1, 3));
				
				Character pos2LineChar = formatedInput.charAt(4);
				Integer pos2Column = Integer.parseInt(formatedInput.substring(5, 7));
				
				Integer pos1Line = map.get(pos1LineChar);
				Integer pos2Line = map.get(pos2LineChar);
	
				if(!verifyPositions(pos1Line, pos2Line, pos1Column, pos2Column)) {
					return false;
				}
				
				return true;
			}
		}	
		catch(InputMismatchException e) {
			return false;
		}
		catch(NumberFormatException e) {
			return false;
		}
		catch(NullPointerException e) {
			return false;
		}
		
		return false;
	}
	
	private boolean verifyPositions(int pos1Line, int pos2Line, int pos1Column, int pos2Column) {
		String secretWord = "";
		
		//check column
		if(pos1Line != pos2Line && pos1Column == pos2Column) {
			try {
				for(int line = pos1Line; line <= pos2Line; line++) {
					secretWord += tableMatrix[line][pos1Column];
				}
			}
			catch(ArrayIndexOutOfBoundsException e) {
				return false;
			}
			
			if(words.contains(secretWord)) {
				for(int line = pos1Line; line <= pos2Line; line++) {
					tableMatrix[line][pos1Column] = '-';
				}
				foundWords.add(secretWord);
				TerminalHandler.clear();
				t.printMatrix();
				printFoundWords();
			}
			
			return true;
		}
		//check line
		else if(pos1Line == pos2Line && pos1Column != pos2Column) {
			try {
				for(int column = pos1Column; column <= pos2Column; column++) {
					secretWord += tableMatrix[pos1Line][column];
				}
			}
			catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("Posição inválida!");
				return false;
			}
			
			if(words.contains(secretWord)) {
				for(int column = pos1Column; column <= pos2Column; column++) {
					tableMatrix[pos1Line][column] = '-';
				}
				foundWords.add(secretWord);
				TerminalHandler.clear();
				t.printMatrix();
				printFoundWords();
			}
			
			return true;
		}
		
		return false;
	}
	
	private void printFoundWords() {
		if(!isGameWon) {
			System.out.println("\nPossíveis palavras");
			for(String s : words) {
				System.out.print(s + " ");
			}
			System.out.println();
		}
		
		System.out.println("\nPalavras encontradas");
		for(String s : foundWords) {
			System.err.print(s + " ");
		}
	}
	
	private void mapAlphabetToIndex() {
		Character character = 65;
		for(int i = 0; i < 26; i++) {
			map.put(character, i);
			character++;
		}
	}
}
