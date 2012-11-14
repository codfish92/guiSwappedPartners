package ClueGame.Player;

import java.util.Random;
import java.util.Set;

import ClueGame.Board.BoardCell;

public class ComputerPlayer extends Player {
	public ComputerPlayer(String name, String color, String start) {
		super(name, color, start);
	}
	public ComputerPlayer() {
		super();
		this.setComputer();
	}
	

	private char lastRoomVisited;
	
	public BoardCell pickMove(int roll) {
		Set<BoardCell> tgts = getTargets();
		int rnum = Math.abs((new Random()).nextInt() % tgts.size());
		return (BoardCell) tgts.toArray()[rnum];
	}
	
	public void setComputer(){
		this.isComputer = true;
		this.isHuman = false;
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
		System.err.println("Hand: " + this.hand);
		for (Card c : this.hand) {
			if (c.getName().equals(person)|| c.getName().equals(weapon) || c.getName().equals(room)){
				return c;
			}
		}
		return null;
	}
}
