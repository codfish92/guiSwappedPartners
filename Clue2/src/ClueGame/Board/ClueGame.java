package ClueGame.Board;

import java.awt.BorderLayout;

import javax.swing.*;

public class ClueGame extends JFrame{
	private Board board;
	public ClueGame () {
		super();
		board = new Board();
		board.loadConfigFiles();
		add(board, BorderLayout.CENTER);
		setSize(800, 600);
	}
	
	public static void main(String[] args){
		ClueGame game = new ClueGame();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setVisible(true);
	}
	
}
