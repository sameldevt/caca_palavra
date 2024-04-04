package br.com.magna.caca_palavra.program;

import br.com.magna.caca_palavra.entities.Table;

public class Main {
	public static void main(String[] args) {
		Table t = new Table();
		
		t.printMatrix();
		
		while(true) {
			t.guessWord();
		}
	}
}
