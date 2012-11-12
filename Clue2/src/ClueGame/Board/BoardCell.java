package ClueGame.Board;

import java.awt.Graphics;

public abstract class BoardCell {
	protected int row;
	protected int col;
	protected static final int WIDTH = 20;
	protected static final int HEIGHT = 20;
	private char name;
	public boolean isWalkway(){return false;}
	public boolean isRoom(){return false;}
	public boolean isDoorway(){return false;}
	public String toString(){return "row: " +row +". Col: " + col;}
	abstract protected void draw(Graphics g);
	
	public char getChar() {
		return name;
	}
	public int getX() {
		return row;
	}
	public int getY() {
		return col;
	}
	
	
}
