package ClueGame.Board;

import java.awt.Color;
import java.awt.Graphics;

public class WalkwayCell extends BoardCell {
	
	public WalkwayCell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
		// TODO Auto-generated constructor stub
	}

	public boolean isWalkway(){return true;}
	protected void draw(Graphics g){
		g.setColor(Color.RED);
		g.fillRect((row-1)*Board.SIZE, (col-1)*Board.SIZE, Board.SIZE, Board.SIZE);
		g.setColor(Color.BLACK);
		g.drawRect((row-1)*Board.SIZE, (col-1)*Board.SIZE, Board.SIZE, Board.SIZE);
	};
}
