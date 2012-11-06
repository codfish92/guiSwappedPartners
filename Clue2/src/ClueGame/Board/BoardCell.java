package ClueGame.Board;

import java.awt.Graphics;

public abstract class BoardCell {
	protected int row;
	protected int col;
	private char name;
	public boolean isWalkway(){return false;}
	public boolean isRoom(){return false;}
	public boolean isDoorway(){return false;}
	public String toString(){return "row: " +row +". Col: " + col;}
	abstract protected void draw(Graphics g);
	
	public char getChar() {
		return name;
	}
	
	
}
