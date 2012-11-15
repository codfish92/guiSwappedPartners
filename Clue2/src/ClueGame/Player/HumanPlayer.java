package ClueGame.Player;

public class HumanPlayer extends Player {

	public HumanPlayer(String name, String color, String start) {
		super(name, color, start);
		this.setHuman();
		// TODO Auto-generated constructor stub
	}
	
	public void setHuman(){
		this.isComputer=false;
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
