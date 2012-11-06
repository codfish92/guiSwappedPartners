package ClueGame.Board;

import java.awt.Color;
import java.awt.Graphics;

public class WalkwayCell extends BoardCell {
	
	public WalkwayCell() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean isWalkway(){return true;}
	protected void draw(Graphics g){
		g.setColor(Color.MAGENTA);
		g.fillRect(row*10, col*10, 20, 20);
	};
}
