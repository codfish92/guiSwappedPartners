package ClueGame.Board;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ClueGame.Player.Card;
import ClueGame.Player.ComputerPlayer;
import ClueGame.Player.HumanPlayer;
import ClueGame.Player.Player;

public class Board extends JPanel{

	private ArrayList<BoardCell> cells = new ArrayList<BoardCell>();
	private Map<Character, String> rooms = new HashMap<Character, String>();
	private int numRows;
	private int numColumns;

	private static int GRID_PIECES;
	private Map<Integer, LinkedList<Integer>> adjMtx = new HashMap<Integer, LinkedList<Integer>>();
	private Set<BoardCell> targets = new HashSet<BoardCell>();
	private List<Card> deck = new ArrayList<Card>();
	private List<Player> players = new ArrayList<Player>();
	public int whoseTurn;
	public int roll;
	private String answerPerson;
	private String answerWeapon;
	private String answerRoom;
	public static final int SIZE = 30;
	private boolean[] seen;
	
	public int rollDie(){
		Random rand = new Random();
		int rolls = rand.nextInt(6)+1;
		roll = rolls;
		return rolls;
	}
	
	public void setPlayers(List<Player> playList) {
		players = playList;
	}

	public void loadConfigFiles() {
		loadLegend();
		loadBoard();
		loadPlayers();
		loadDeck();
		this.deal();
	}
	
	private void loadPlayers() {
		try {
			FileReader read = new FileReader("people.txt");
			Scanner scan = new Scanner(read);
			String person = scan.nextLine();
			String[] settings;
			settings=person.split(", ");
			players.add(new HumanPlayer(settings[0], settings[1], settings[2]));
			while(scan.hasNextLine()) {
				person = scan.nextLine();
				settings=person.split(", ");
				players.add(new ComputerPlayer(settings[0], settings[1], settings[2]));
			}
		} catch(FileNotFoundException e){
			e.getStackTrace();
		}
	}
	
	private void loadDeck() {
		try {
			//load player cards
			FileReader read = new FileReader("people.txt");
			Scanner scan = new Scanner(read);
			int i = 0;
			while(scan.hasNextLine()) {
				String personWithBootStrap = scan.nextLine() ;
				deck.add(i, new Card(personWithBootStrap.substring(0, personWithBootStrap.indexOf(',')), Card.Type.PERSON));
				++i;
			}
			
			//load weapon cards
			read = new FileReader("weapons.txt");
			scan = new Scanner(read);
			while(scan.hasNextLine()) {
				deck.add(i, new Card(scan.nextLine(), Card.Type.WEAPON));
				++i;
			}
			
			//load rooms
			read = new FileReader("legendBoard.txt");
			scan = new Scanner(read);
			String in = "";
			String name = "";
			while (scan.hasNextLine()) {
				in = scan.nextLine();
				String[] vars = in.split(", ");
				name = vars[1].substring(0);
				char n = vars[0].charAt(0);
				if (n != 'W'){
					deck.add(i, new Card(name, Card.Type.ROOM));
					++i;
				}
			}
		} catch (FileNotFoundException e) {
			e.getStackTrace();
		}
		
	}

	private void loadLegend() {
		try {

			FileReader read = new FileReader("legendBoard.txt");
			Scanner scan = new Scanner(read);
			String in = "";
			String name = "";
			int i = 0;
			while (scan.hasNextLine()) {
				i++;
				in = scan.nextLine();
				String[] vars = in.split(", ");
				name = vars[1].substring(0);
				char n = vars[0].charAt(0);
				if (n != 'W')
					rooms.put(n, name);
			}
			scan.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	private void loadBoard() {
		try {
			int row = 0;
			int col = 0;
			FileReader reader = new FileReader("Board.txt");
			Scanner scan = new Scanner(reader);
			String part = "";
			while (scan.hasNextLine()) {
				row++;
				col = 0;
				part = scan.nextLine();
				String[] vars = part.split(",");
				for (String str : vars) {
					col++;
					if (str.equals("W")) {
						WalkwayCell c = new WalkwayCell(col, row);
						cells.add(c);
					} else {
						RoomCell c = new RoomCell(str, col, row);
						cells.add(c);
					}
				}
				if (row <= 1){
					setNumColumns(col);
					
				}
			}
			setNumRows(row);
			scan.close();
			GRID_PIECES = numRows * numColumns;
		} catch (Exception e) {
			e.getStackTrace();
		}

	}

	public int calcIndex(int row, int col) {
		return numColumns * row + col;
	}

	public RoomCell getRoomCellAt(int row, int col) {
		RoomCell roomCell = new RoomCell();
		roomCell = (RoomCell) getCells().get(calcIndex(row, col));
		return roomCell;
	}

	public BoardCell getCellAt(int row, int col) {
		BoardCell boardCell;
		boardCell = getCells().get(calcIndex(row, col));
		return boardCell;
	}
	
	public ArrayList<BoardCell> getCells() {
		return cells;
	}

	public void setCells(ArrayList<BoardCell> cells) {
		this.cells = cells;
	}

	public Map<Character, String> getRooms() {
		return rooms;
	}

	public void setRooms(Map<Character, String> rooms) {
		this.rooms = rooms;
	}

	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public void setNumColumns(int numColumns) {
		this.numColumns = numColumns;
	}

	public LinkedList<Integer> getAdjList(int row, int col) {
		return getAdjList(calcIndex(row, col));
	}

	public void calcAdjacencies() {
		// for loops
		for (int i = 0; i < GRID_PIECES; ++i) {
			adjMtx.put(i, getAdjList(i));
		}
	}

	public void calcTargets(int vertex, int steps) {
		int start = vertex;
		targets = new HashSet();
		seen = new boolean[numRows*numColumns];
		this.setSeen();
		seen[start]=true;
		LinkedList<Integer> path = new LinkedList<Integer>();
		this.recurseTargets(start, path, steps);
	}
	
	private void recurseTargets(int target, LinkedList<Integer> path, int steps){
		LinkedList<Integer> tempAdj=new LinkedList<Integer>();
		ListIterator<Integer> itr = this.getAdjList(target).listIterator();
		while (itr.hasNext()){
			int next=itr.next();
			if (seen[next]==false){
				tempAdj.add(next);
			} else {
				continue;
			}
		}
		ListIterator<Integer> itrAdj = tempAdj.listIterator();
		while (itrAdj.hasNext()){
			int nextNode=itrAdj.next();
			seen[nextNode]=true;
			path.push(nextNode);
			if(cells.get(nextNode).isRoom() != true || cells.get(nextNode).isDoorway() == true){
				if (path.size() == steps){
					targets.add(cells.get(nextNode));
				} else if (cells.get(nextNode).isDoorway()) {
					targets.add(cells.get(nextNode));
				} else {
					recurseTargets(nextNode, path, steps);
				}
			}
			path.removeLast();
			seen[nextNode]=false;
		}
	}
	
	private void setSeen(){
		for (int i=0; i<numRows*numColumns; i++){
			seen[i]=false;
		}
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}

	public LinkedList<Integer> getAdjList(int cell) {
		LinkedList<Integer> adjList = new LinkedList<Integer>();
		if (cell % numColumns == 0) {
			// left edge
			if (cell == 0) {
				// top
				adjList.add(cell + 1);
				adjList.add(cell + numColumns);
			} else if (cell == GRID_PIECES - numColumns) {
				// bottom
				adjList.add(cell + 1);
				adjList.add(cell - numColumns);
			} else {
				// inbetween
				adjList.add(cell + 1);
				adjList.add(cell + numColumns);
				adjList.add(cell - numColumns);
			}
		} else if ((cell + 1) % numColumns == GRID_PIECES % numColumns) {
			// right edge
			if (cell == numColumns - 1) {
				// top
				adjList.add(cell - 1);
				adjList.add(cell + numColumns);
			} else if (cell == GRID_PIECES - 1) {
				// bottom
				adjList.add(cell - 1);
				adjList.add(cell - numColumns);
			} else {
				// inbetween
				adjList.add(cell - 1);
				adjList.add(cell + numColumns);
				adjList.add(cell - numColumns);
			}
		} else if (cell < numColumns) {
			// on top
			adjList.add(cell + 1);
			adjList.add(cell - 1);
			adjList.add(cell + numColumns);
		} else if (cell > (GRID_PIECES - numColumns)) {
			// on bottom
			adjList.add(cell + 1);
			adjList.add(cell - 1);
			adjList.add(cell - numColumns);
		} else {
			// inbetween
			adjList.add(cell + 1);
			adjList.add(cell - 1);
			adjList.add(cell + numColumns);
			adjList.add(cell - numColumns);
		}
		
		return adjList;
	}
	
	public void selectAnswer() {
		
	}
	
	public void deal(List<Card> deck) {
		boolean shuffle = true;
		if (shuffle) {
			//sets aside accusation
			int randomNum = Math.abs((new Random()).nextInt() % 20);
			//pick a random player card
			//keeps rolling if the card at the index is not a person card
			while(deck.get(randomNum).getType() != Card.Type.PERSON) {
				randomNum = Math.abs((new Random()).nextInt() % 20);
			}
			answerPerson = deck.get(randomNum).getName();
		
			deck.remove(randomNum);
			//pick random weapon card
			randomNum = Math.abs((new Random()).nextInt() % 19);
			while(deck.get(randomNum).getType() != Card.Type.WEAPON) {
				randomNum = Math.abs((new Random()).nextInt() % 19);
			}
			answerWeapon = deck.get(randomNum).getName();
		
			deck.remove(randomNum);
			//pick random room card
			randomNum = Math.abs((new Random()).nextInt() % 18);
			while(deck.get(randomNum).getType() != Card.Type.ROOM) {
				randomNum = Math.abs((new Random()).nextInt() % 18);
			}
			answerRoom = deck.get(randomNum).getName();
			deck.remove(randomNum);
		} else {
			// do not randomize. We are choosing Dr. Nefarious with the Flying Spaghetti Monster in the Tower
			answerPerson = deck.remove(0).getName();
			answerWeapon = deck.remove(9).getName();
			answerRoom = deck.remove(deck.size()-1).getName();
			serrln(answerPerson + " with the " + answerWeapon + " in the " + answerRoom);
		}
		List<List<Card>> allHands = new ArrayList<List<Card>>();
		for (Player p : players)
			allHands.add(new ArrayList<Card>());
		int ds = deck.size();
		for (int i=0; i<ds; i++) {
			// pull a card off the deck
			Card c = deck.remove(0);
			allHands.get(i%5).add(c);
		}
		for (List<Card> lc : allHands) {
		}
		for(int i=0; i < players.size(); i++){
			players.get(i).giveHand(allHands.get(i));
		}
		
	}
	
	public void deal() {
		deal(deck);
	}
	
	public boolean checkAccusation(String person, String weapon, String room) {
		boolean p = person.equals(answerPerson); 
		boolean w = weapon.equals(answerWeapon);
		boolean r = room.equals(answerRoom);
		return p && w && r;
	}
	
	public boolean handleSuggestion(String person, String weapon, String room) {
		return false;
	}
	
	public List<Card> getDeck() {
		return deck;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public boolean solutionContainsCard(Card c) {
		return false;
	}
	
	public Card disproveSuggestion(Player suggester, String person, String weapon, String room) {
		return suggester.disproveSuggestion(person, weapon, room);
	}
	
	public static void serrln(String message) {
		System.err.println(message);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for(int i = 0; i < cells.size(); ++i){
			cells.get(i).draw(g);
		}
		List<Player> players = this.getPlayers();
		for(int i = 0; i<players.size(); ++i){
			players.get(i).draw(g, this);
		}
		drawRoomName(g);
		drawMoves(g);
	}
	
	public void drawRoomName(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.GREEN);
		g2.drawString("Conservatory", 30, 50);
		g2.drawString("Indoor Pool", 250, 50);
		g2.drawString("Kitchen", 520, 50);
		g2.drawString("Study", 80, 240);
		g2.drawString("Dining Room", 460, 260);
		g2.drawString("Living Room", 60, 420);
		g2.drawString("Entryway", 320, 420);
		g2.drawString("Library", 500, 420);
		//draw tower
		g2.drawString("T", 295, 200);
		g2.drawString("o", 295, 210);
		g2.drawString("w", 294, 220);
		g2.drawString("e", 295, 230);
		g2.drawString("r", 296, 240);
	}
	
	public void drawMoves(Graphics g){
		players.get(whoseTurn).drawTargets(roll, this, g);
	}
}


	

