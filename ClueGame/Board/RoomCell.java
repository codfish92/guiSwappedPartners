package ClueGame.Board;

import java.awt.Color;
import java.awt.Graphics;

public class RoomCell extends BoardCell {
	
	public RoomCell() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RoomCell(String roomString, int row, int col) {
		super();
		if(roomString.length()== 1)
			this.roomInitial = roomString.charAt(0);
		else{
			
			this.roomInitial = roomString.charAt(0);
			if(roomString.charAt(1) == 'R')
				setDoorDirection(doorDirection.RIGHT);
			if(roomString.charAt(1) == 'L')
				setDoorDirection(doorDirection.LEFT);
			if(roomString.charAt(1) == 'D')
				setDoorDirection(doorDirection.DOWN);
			if(roomString.charAt(1) == 'U')
				setDoorDirection(doorDirection.UP);
		}
		this.col = col;
		this.row = row;
	}

	public enum DoorDirection {
		UP, DOWN, LEFT, RIGHT, NONE;
	}
	private DoorDirection doorDirection;
	private char roomInitial;
	public boolean isDoorway(){
		if(doorDirection != null )
			return true;
		return false;
	}
	public boolean isRoom(){return true;}
	
	protected void draw(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect((row-1)*Board.SIZE, (col-1)*Board.SIZE, Board.SIZE, Board.SIZE);
		if (this.isDoorway()){
			g.setColor(Color.YELLOW);
			if (doorDirection==DoorDirection.UP){
				g.fillRect((row-1)*Board.SIZE, (col-1)*Board.SIZE, Board.SIZE, Board.SIZE-(Board.SIZE-Board.SIZE/5));
			} else if (doorDirection==DoorDirection.DOWN){
				g.fillRect((row-1)*Board.SIZE, ((col-1)*Board.SIZE)+(Board.SIZE-Board.SIZE/5), Board.SIZE, Board.SIZE-(Board.SIZE-Board.SIZE/5));
			} else if (doorDirection==DoorDirection.LEFT){
				g.fillRect((row-1)*Board.SIZE, (col-1)*Board.SIZE, Board.SIZE-(Board.SIZE-Board.SIZE/5), Board.SIZE);
			} else if (doorDirection==DoorDirection.RIGHT){
				g.fillRect((row-1)*Board.SIZE+(Board.SIZE-Board.SIZE/5), (col-1)*Board.SIZE, Board.SIZE-(Board.SIZE-Board.SIZE/5), Board.SIZE);
			}
		}
	};

	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	public void setDoorDirection(DoorDirection doorDirection) {
		this.doorDirection = doorDirection;
	}

	public char getRoomInitial() {
		return roomInitial;
	}
	
}
