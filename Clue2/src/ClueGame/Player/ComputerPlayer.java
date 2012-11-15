package ClueGame.Player;

import java.util.Random;
import java.util.Set;

import ClueGame.Board.Board;
import ClueGame.Board.BoardCell;

public class ComputerPlayer extends Player {
	public ComputerPlayer(String name, String color, String start) {
		super(name, color, start);
		this.isComputer=true;
	}
	public ComputerPlayer() {
		super();
		this.isComputer=true;
	}

	private char lastRoomVisited;
	
	public BoardCell pickMove(Board b) {
		Set<BoardCell> tgts = b.getTargets();
		int rnum = Math.abs((new Random()).nextInt() % tgts.size());
		return (BoardCell) tgts.toArray()[rnum];
	}
	
	public void setComputer(){
		this.isComputer = true;
	}
	
	public void takeTurn(Board b){
		BoardCell newCell = pickMove(b);
		int col = newCell.getX();
		int row = newCell.getY();
		this.setCurrX(col-1);
		this.setCurrY(row-1);
		b.repaint();
		this.updateCurrentPosition(b);
	}
	
	public void createSuggestion() {
		
	}
	
	public void updateSeen(Card seen) {
		
	}

	public char getLastRoom() {
		return lastRoomVisited;
	}
	
	@Override
	public Card disproveSuggestion(String person, String weapon, String room) {

		for (Card c : this.hand) {
			if (c.getName().equals(person)|| c.getName().equals(weapon) || c.getName().equals(room)){
				return c;
			}
		}
		return null;
	}
}
