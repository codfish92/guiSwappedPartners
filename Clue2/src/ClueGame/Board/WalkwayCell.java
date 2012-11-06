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
		g.setColor(Color.MAGENTA);
		g.fillRect((row-1)*20, (col-1)*20, WIDTH, HEIGHT);
	};
}
