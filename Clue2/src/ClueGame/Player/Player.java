package ClueGame.Player;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ClueGame.Board.Board;
import ClueGame.Board.BoardCell;

public abstract class Player {
	private String name, color;
	public Player(String name, String color, String start) {
		super();
		this.name = name;
		this.color = color;
		this.plaColor = convertColor(color);
		this.currentPosition=Integer.parseInt(start)-1;
	}
	public Player() {
		super();
	}
	
	protected List<Card> hand;
	private int currentPosition;
	private Set<BoardCell> targets;
	private Color plaColor;
	private int currX;
	private int currY;
	
	public String getName(){
		return name;
	}
	
	public String getColor(){
		return color;
	}
	
	public Color convertColor(String strColor) {
		Color color; 
		try {     
			// We can use reflection to convert the string to a color
			Field field = Class.forName("java.awt.Color").getField(strColor.trim());     
			color = (Color)field.get(null); } 
		catch (Exception e) {  
			color = null; // Not defined } 
		}
		return color;
	}
	
	public Set<BoardCell> getTargets(){
		return targets;
	}
	
	public Set<BoardCell> calcTargets(int roll, Board brd) {
		brd.calcTargets(currentPosition, roll);
		targets = brd.getTargets();
		return getTargets();
	}
	
	abstract public Card disproveSuggestion(String person, String weapon, String room);
	
	public List<Card> getCards() {
		return hand;
	}
	
	public void initTurn(Board b) {
		int roll = (new Random()).nextInt() % 6;
		b.calcTargets(currentPosition, roll);
		targets = b.getTargets();
	}
	
	public void giveHand(List<Card> hand) {
		this.hand = hand;
	}
	
	public void convertIndex(Board b){
		int cols = b.getNumColumns();
		currY=currentPosition/cols;
		currX=currentPosition%cols;
	}
	
	public void draw(Graphics g, Board b){
		g.setColor(plaColor);
		this.convertIndex(b);
		g.fillOval(currX*Board.SIZE, currY*Board.SIZE, Board.SIZE, Board.SIZE);
	}
	
	public void drawTargets(int roll, Board brd, Graphics g) {
		for(BoardCell c : calcTargets(roll, brd)){
			if(c.isRoom() != true || c.isDoorway() == true){
				g.setColor(Color.RED);
				g.fillRect((c.getX()-1)*30, (c.getY()-1)*30, 30, 30);
			}
			
		}
		this.draw(g, brd); // puts the player oval back on top
	}
}
