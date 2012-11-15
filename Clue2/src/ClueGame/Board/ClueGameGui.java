package ClueGame.Board;

import ClueGame.Player.Card.Type;
import ClueGame.Player.ComputerPlayer;
import ClueGame.Player.Player;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ClueGameGui extends JFrame{
	private Board board;
	private DetectivePanel detective;
	private ControlPanel controls;
	private PlayerHand hand;
	private boolean hasMadeTurn;
	private HumanSeg humSeg;
	private Acu acu;
	public ClueGameGui () {
		super();
		hasMadeTurn=false;
		board = new Board();
		board.loadConfigFiles();
		controls = new ControlPanel();
		hand = new PlayerHand();
		add(board, BorderLayout.CENTER);
		add(controls, BorderLayout.SOUTH);
		add(hand, BorderLayout.EAST);
		setTitle("Clue");
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());
		setSize(1100, 900);
		detective = new DetectivePanel();
		detective.setSize(600, 500);
		humSeg = new HumanSeg();
		humSeg.setSize(400, 400);
		acu = new Acu();
		acu.setSize(400, 400);
		board.addMouseListener(new mouseTracker());
		this.splashScreen();
	}

	public void toggleHumanSeg(String room) {
		if(this.humSeg.isVisible() == true) 
			humSeg.setVisible(false);
		
		else{
			humSeg.setVisible(true);
			this.humSeg.setRoom(room);
		}
	}
	
	public String getPersonFromGuess(){
		return (String) this.humSeg.panel.personSelect.getSelectedItem();
	}
	public String getWeaponFromGuess(){
		return (String) this.humSeg.panel.weaponSelect.getSelectedItem();
	}
	public String getRoomFromGuess(){
		return this.humSeg.panel.room.getText();
	}
	public int getAskee(){
		System.out.println(this.humSeg.panel.askSelect.getSelectedIndex());
		return this.humSeg.panel.askSelect.getSelectedIndex() + 1;
	}
	
	private class HumanSeg extends JDialog {
		private HumanSegPanels panel;
		private JButton button;
		public HumanSeg(){
			panel = new HumanSegPanels();
			button = new JButton("make Suggestion");
			this.setSize(400, 400);
			add(panel, BorderLayout.CENTER);
			add(button, BorderLayout.SOUTH);
			button.addActionListener(new ButtonListener());
			
		}
		public void setRoom(String room) { // only to lower the amount of method calls required
			panel.setRoom(room);
		}
		
		
		public class ButtonListener implements ActionListener {

			public void actionPerformed(ActionEvent e) {
				if(board.getPlayers().get(getAskee()).disproveSuggestion(getPersonFromGuess(), getWeaponFromGuess(), getRoomFromGuess()) != null)
					JOptionPane.showMessageDialog(null, board.getPlayers().get(getAskee()).disproveSuggestion(getPersonFromGuess(), getWeaponFromGuess(), getRoomFromGuess()).getName());
				else
					JOptionPane.showMessageDialog(null, "That person had none of those cards");
				humSeg.setVisible(false);
				
			}
			
		}
	}
	private class Acu extends JDialog {
		private HumanAcuPanels field;
		private JButton button;
		public Acu() {
			field = new HumanAcuPanels();
			button = new JButton("Make Accusation");
			add(field, BorderLayout.CENTER);
			add(button, BorderLayout.SOUTH);
			button.addActionListener(new ButtonListener());
		}
		public class ButtonListener implements ActionListener {

	
			public void actionPerformed(ActionEvent e) {
				if(board.checkAccusation((String)field.personSelect.getSelectedItem(), (String)field.weaponSelect.getSelectedItem(), (String)field.roomSelect.getSelectedItem()) == true){
					JOptionPane.showMessageDialog(null, "You Won");
					System.exit(0);
				}
				else {
					JOptionPane.showMessageDialog(null, "You da herped when ya shouldve derped");
				}
				
			}
			
		}
	}
	private class HumanAcuPanels extends JPanel {
		private JLabel  roomBorder, personBorder, weaponBorder;
		private JComboBox personSelect, weaponSelect, roomSelect; 
		public HumanAcuPanels() {
			
			roomBorder = new JLabel("Room");
			personBorder = new JLabel("Person");
			weaponBorder = new JLabel("Weapon");
			personSelect = new JComboBox();
			for(int i = 0; i< board.getPlayers().size(); ++i) {
				personSelect.addItem(board.getPlayers().get(i).getName());
			}
			
			weaponSelect = new JComboBox();
			weaponSelect.addItem("Sharpened footballs");
			weaponSelect.addItem("M1A1 Abrahms Tank");
			weaponSelect.addItem("awkward turtle");
			weaponSelect.addItem("The Magic Schoolbus");
			weaponSelect.addItem("cotton balls");
			weaponSelect.addItem("Flying Spaghetti Monster");
			roomSelect = new JComboBox();
			roomSelect.addItem("Conservatory");
			roomSelect.addItem("Indoor Pool");
			roomSelect.addItem("Kitchen");
			roomSelect.addItem("Study");
			roomSelect.addItem("Dining Room");
			roomSelect.addItem("Living Room");
			roomSelect.addItem("Entryway");
			roomSelect.addItem("Library");
			roomSelect.addItem("Tower");
			
			setLayout(new GridLayout(0,1));
			add(personBorder);
			add(personSelect);
			add(weaponBorder);
			add(weaponSelect);
			add(roomBorder);
			add(roomSelect);
			
		}
	}
	
	
	
	private class HumanSegPanels extends JPanel {
		private JLabel room, roomBorder, personBorder, weaponBorder, askBorder;
		private JComboBox personSelect, weaponSelect, askSelect; 
		public HumanSegPanels() {
			room = new JLabel("");
			roomBorder = new JLabel("Room");
			personBorder = new JLabel("Person");
			weaponBorder = new JLabel("Weapon");
			askBorder = new JLabel("The person you are suggesting");
			personSelect = new JComboBox();
			for(int i = 0; i< board.getPlayers().size(); ++i) {
				personSelect.addItem(board.getPlayers().get(i).getName());
			}
			askSelect = new JComboBox();
			for(int i = 1; i <board.getPlayers().size(); ++i) {
				
				askSelect.addItem(board.getPlayers().get(i).getName());
			}
			weaponSelect = new JComboBox();
			weaponSelect.addItem("Sharpened footballs");
			weaponSelect.addItem("M1A1 Abrahms Tank");
			weaponSelect.addItem("awkward turtle");
			weaponSelect.addItem("The Magic Schoolbus");
			weaponSelect.addItem("cotton balls");
			weaponSelect.addItem("Flying Spaghetti Monster");
			setLayout(new GridLayout(0,1));
			add(personBorder);
			add(personSelect);
			add(weaponBorder);
			add(weaponSelect);
			add(roomBorder);
			add(room);
			add(askBorder);
			add(askSelect);
			
		}
		public void setRoom(String room){
			this.room.setText(room);
		}
		
	}

	private class DieRoll extends JPanel{
		private int roll;
		private JTextArea display;
		public DieRoll(){
			setBorder(new TitledBorder (new EtchedBorder(), "Die Roll"));
			display = new JTextArea(2,20);
			updateDisplay();
			add(display);
		}
		private void updateDisplay(){
			roll=board.rollDie();
			display.setText(Integer.toString(roll));
		}
	}

	private class WhoseTurn extends JPanel{
		private String name, color;
		private JTextArea display;
		private int currentIndex = 0;
		public WhoseTurn(){
			setBorder(new TitledBorder (new EtchedBorder(), "Current Player"));
			name=board.getPlayers().get(0).getName();
			color=board.getPlayers().get(0).getColor();
			display = new JTextArea(2,20);
			updateDisplay();
			add(display);

		}
		private void updateDisplay(){
			display.setText("(" + color + ") " + name);
			display.repaint();
		}
		private void nextTurn() {
			if(currentIndex != 4){
				currentIndex++;
				this.name = board.getPlayers().get(currentIndex).getName();
				this.color = board.getPlayers().get(currentIndex).getColor();
				updateDisplay();
			}
			else{
				currentIndex=0;
				this.name = board.getPlayers().get(currentIndex).getName();
				this.color = board.getPlayers().get(currentIndex).getColor();
				updateDisplay();
			}
		}
	}

	private class ControlPanel extends JPanel{
		private WhoseTurn whoseTurn;
		private JButton nextPerson;
		private JButton makeAccusation;
		private DieRoll die;
		private GuessAsked guess;
		private GuessResult result;
		public ControlPanel(){
			this.setLayout(new GridLayout(2, 3));
			whoseTurn = new WhoseTurn();
			nextPerson = new JButton("Next Player");
			makeAccusation = new JButton("Make an accusation.");
			die = new DieRoll();
			guess = new GuessAsked();
			result = new GuessResult();
			add(whoseTurn);
			add(nextPerson);
			add(makeAccusation);
			add(die);
			add(guess);
			add(result);
			nextPerson.addActionListener(new ButtonListner());
			makeAccusation.addActionListener(new ButtonListner());
		}

		public class ButtonListner implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				Player currentPlayer = board.getPlayers().get(board.whoseTurn);
				if (e.getSource() == nextPerson){
					if (currentPlayer.getComputer()){
						hasMadeTurn=true;
						ComputerPlayer temp = (ComputerPlayer) currentPlayer;
						temp.takeTurn(board);
					}
					if (hasMadeTurn){
						currentPlayer.updateCurrentPosition(board);
						whoseTurn.nextTurn();
						die.updateDisplay();
						board.whoseTurn=whoseTurn.currentIndex;
						board.roll = die.roll;
						board.repaint();
						if(board.getCellAt(currentPlayer.currY, currentPlayer.currX).isDoorway() && currentPlayer.getComputer() == false){
							int y = board.getRoomCellAt(currentPlayer.currY, currentPlayer.currX).getRoomInitial();
							String x = determineRoom(y);
							toggleHumanSeg(x);
						}
						else if(board.getCellAt(currentPlayer.currY, currentPlayer.currX).isDoorway() && currentPlayer.getComputer() == true){
							int person = Math.abs(new Random().nextInt(5));
							int askee = Math.abs(new Random().nextInt(5));
							int weapon = Math.abs(new Random().nextInt(6));
							int room = Math.abs(new Random().nextInt(9));
							String wep = null;
							String rm = null;
							if (weapon ==0)
								wep = "Sharpened footballs";
							else if (weapon ==1)
								wep = "M1A1 Abrahms Tank";
							else if (weapon ==2)
								wep = "awkward turtle";
							else if (weapon ==3)
								wep = "The Magic Schoolbus";
							else if (weapon ==4)
								wep = "cotton balls";
							else if (weapon ==5)
								wep = "Flying Spaghetti Monster";
							else 
								wep = "";
							
							if (room == 0)
								rm = "Conservatory";
							else if(room == 1)
								rm = "Indoor Pool";
							else if(room == 2)
								rm = "Kitchen";
							else if(room == 3)
								rm = "Study";
							else if(room == 4)
								rm = "Dining Room";
							else if(room == 5)
								rm = "Living Room";
							else if(room == 6)
								rm = "Entryway";
							else if(room == 7)
								rm = "Library";
							else if(room == 8)
								rm = "Tower";
							else 
								rm = "";
							System.out.println(board.getPlayers().get(person).getName());
							System.out.println(wep);
							System.out.println(rm);
							System.out.println(askee);
							System.out.println(board.getPlayers().get(askee).getName());
							if(board.getPlayers().get(askee).disproveSuggestion(board.getPlayers().get(person).getName(), wep, rm).getName() != null)
								JOptionPane.showMessageDialog(null, "The Shown card is "+ board.getPlayers().get(askee).disproveSuggestion(board.getPlayers().get(person).getName(), wep, rm).getName());
							else 
								JOptionPane.showMessageDialog(null, "herpDerp");
						}
						hasMadeTurn=false;
					} else {
						String message = "You need to finish your turn.";
						JOptionPane.showMessageDialog(null, message);
					}
				}
				else if (e.getSource() == makeAccusation){
						acu.setVisible(true);
				}

			}
			

		}
		public String determineRoom(int roomNum){
			//uses weird numbers due to the fact that getRoomInitial returns a char that is 1-9, sigh
				if (roomNum == 49)
					return "Conservatory";
				else if (roomNum == 50)
					return "Indoor Pool";
				else if (roomNum == 51)
					return "Kitchen";
				else if (roomNum == 52)
					return "Study";
				else if (roomNum == 53)
					return "Dining Room";
				else if (roomNum == 54)
					return "Living Room";
				else if (roomNum == 55)
					return "Living Room";
				else if (roomNum == 56)
					return "Library";
				else if (roomNum == 57)
					return "Tower";
				else
					return "";
			}
	}

	public void splashScreen(){
		String message = "You are playing as " + this.board.getPlayers().get(0).getName() + " make your move.";
		JOptionPane.showMessageDialog(null, message);
	}

	private class PersonCard extends JPanel{
		private ArrayList<String> person;
		private JTextArea display;
		public PersonCard(){
			setBorder(new TitledBorder (new EtchedBorder(), "People"));
			display = new JTextArea(2,20);
			updateDisplay();
			add(display);
		}
		private void updateDisplay(){
			display.setText("");
			for(int i = 0; i< board.getPlayers().get(0).getCards().size(); ++i){
				if(board.getPlayers().get(0).getCards().get(i).getType() == ClueGame.Player.Card.Type.PERSON){
					display.setText(display.getText() + " : " + board.getPlayers().get(0).getCards().get(i).getName());
				}
			}
		}
		public void setPerson(ArrayList<String> person) {
			this.person = person;
		}
	}

	private class WeaponCard extends JPanel{
		private ArrayList<String> weapon;
		private JTextArea display;
		public WeaponCard(){
			setBorder(new TitledBorder (new EtchedBorder(), "Weapons"));
			display = new JTextArea(2,20);
			updateDisplay();
			add(display);
		}
		private void updateDisplay(){
			display.setText("");
			for(int i = 0; i< board.getPlayers().get(0).getCards().size(); ++i){
				if(board.getPlayers().get(0).getCards().get(i).getType() == ClueGame.Player.Card.Type.WEAPON){
					display.setText(display.getText() + " : " + board.getPlayers().get(0).getCards().get(i).getName());
				}
			}
		}
		public void setWeapon(ArrayList<String> weapon) {
			this.weapon = weapon;
		}
	}

	private class RoomCard extends JPanel{
		private ArrayList<String> room;
		private JTextArea display;
		public RoomCard(){
			setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
			display = new JTextArea(2,20);
			updateDisplay();
			add(display);
		}
		private void updateDisplay(){
			display.setText("");
			for(int i = 0; i< board.getPlayers().get(0).getCards().size(); ++i){
				if(board.getPlayers().get(0).getCards().get(i).getType() == ClueGame.Player.Card.Type.ROOM){
					display.setText(display.getText() + " : " + board.getPlayers().get(0).getCards().get(i).getName());
				}
			}
		}
		public void setRoom(ArrayList<String> room) {
			this.room = room;
		}
	}

	private class PlayerHand extends JPanel{
		private RoomCard room;
		private WeaponCard weapon;
		private PersonCard person;
		public PlayerHand(){
			room = new RoomCard();
			weapon = new WeaponCard();
			person = new PersonCard();
			this.setLayout(new GridLayout(0, 1));
			add(room);
			add(weapon);
			add(person);
		}
	}

	private JMenu createFileMenu(){
		JMenu menu = new JMenu("File"); 
		menu.add(createFileExitItem());
		menu.add(createDetectiveNotes());
		return menu;
	}

	private JMenuItem createFileExitItem(){
		JMenuItem item = new JMenuItem("Exit");
		class MenuItemListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}

	private JMenuItem createDetectiveNotes(){
		JMenuItem item = new JMenuItem("Detective Notes");
		class MenuUtemListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				detective.setVisible(true);
			}
		}
		item.addActionListener(new MenuUtemListener());
		return item;
	}

	public class DetectivePanel extends JDialog{
		private CheckPanel check;
		private BestGuessPanel bestGuess;
		public DetectivePanel() {
			check = new CheckPanel();
			bestGuess = new BestGuessPanel();
			this.setLayout(new GridLayout(0,2));
			add(check);
			add(bestGuess);
		}
	}

	public class CheckPanel extends JPanel {
		private PeopleBox people;
		private RoomBox room;
		private WeaponBox wpn;
		public class PeopleBox extends JPanel {
			private JCheckBox drBox, doraBox, inspecterBox, jimBox, benderBox;
			public  PeopleBox() {
				drBox = new JCheckBox("Dr. Nefarious");
				doraBox = new JCheckBox("Dora the Explorer");
				inspecterBox = new JCheckBox("Inspector Gadget");
				jimBox = new JCheckBox("Jim");
				benderBox = new JCheckBox("Bender Rodriguez");
				this.setLayout(new GridLayout(0,2));
				setBorder(new TitledBorder(new EtchedBorder(), "People"));
				add(drBox);
				add(doraBox);
				add(inspecterBox);
				add(jimBox);
				add(benderBox);
			}

		}
		public class RoomBox extends JPanel {
			private JCheckBox conservatory, indoorPool, kitchen, study, diningRoom, livingRoom, entryway, library, tower;
			public RoomBox () {
				this.setLayout(new GridLayout(0,2));
				conservatory = new JCheckBox("Conservatory");
				indoorPool = new JCheckBox("Indoor Pool");
				kitchen = new JCheckBox("Kitchen");
				study = new JCheckBox("Study");
				diningRoom = new JCheckBox("Dining Room");
				livingRoom = new JCheckBox("Living Room");
				entryway = new JCheckBox("Entryway");
				library = new JCheckBox("Library");
				tower = new JCheckBox("Tower");
				setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
				add(conservatory);
				add(indoorPool);
				add(kitchen);
				add(study);
				add(diningRoom);
				add(livingRoom);
				add(entryway);
				add(library);
				add(tower);

			}
		}
		public class WeaponBox extends JPanel {
			private JCheckBox foot, tank, balls, turtle, monster, bus;
			public WeaponBox() {
				this.setLayout(new GridLayout(0,2));
				foot = new JCheckBox("Sharpened footballs");
				tank = new JCheckBox("M1A1 Abrahms Tank");
				balls = new JCheckBox("cotton balls");
				turtle = new JCheckBox("awkward turtle");
				bus = new JCheckBox("The Magic Schoolbus");
				monster = new JCheckBox("Flying Spaghetti Monster");
				setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
				add(foot);
				add(tank);
				add(turtle);
				add(bus);
				add(balls);
				add(monster);
			}
		}
		public CheckPanel () {
			this.setLayout(new GridLayout(0,1));
			people = new PeopleBox();
			room = new RoomBox();
			wpn = new WeaponBox();
			add(people);
			add(room);
			add(wpn);
		}
	}
	public class BestGuessPanel extends JPanel{
		private personGuessBox person;
		private roomGuessBox room;
		private weaponGuessBox wpn;
		public class personGuessBox extends JPanel{
			private JComboBox<String> guess;
			public personGuessBox() {
				guess = new JComboBox<String>();
				guess.addItem("Dr. Nefarious");
				guess.addItem("Jim");
				guess.addItem("Dora the Explorer");
				guess.addItem("Inspector Gadget");
				guess.addItem("Bender Rodriguez");
				guess.addItem("Unsure");
				setBorder(new TitledBorder(new EtchedBorder(), "Person Guess"));
				add(guess, BorderLayout.EAST);
			}
		}
		public class roomGuessBox extends JPanel{
			private JComboBox<String> guess;
			public roomGuessBox () {
				guess = new JComboBox<String>();
				guess.addItem("Conservatory");
				guess.addItem("Indoor Pool");
				guess.addItem("Kitchen");
				guess.addItem("Study");
				guess.addItem("Dining Room");
				guess.addItem("Living Room");
				guess.addItem("Entryway");
				guess.addItem("Library");
				guess.addItem("Tower");
				guess.addItem("Unsure");
				setBorder(new TitledBorder(new EtchedBorder(), "Room Guess"));
				add(guess, BorderLayout.EAST);
			}
		}
		public class weaponGuessBox extends JPanel{
			private JComboBox<String> guess;
			public weaponGuessBox () {
				guess = new JComboBox<String>();
				guess.addItem("Sharpened footballs");
				guess.addItem("M1A1 Abrahms Tank");
				guess.addItem("awkward turtle");
				guess.addItem("cotton balls");
				guess.addItem("The Magic Schoolbus");
				guess.addItem("Flying Spaghetti Monster");
				guess.addItem("Unsure");
				setBorder(new TitledBorder(new EtchedBorder(), "Weapon Guess"));
				add(guess, BorderLayout.EAST);
			}
		}
		public BestGuessPanel () {
			person = new personGuessBox();
			room = new roomGuessBox();
			wpn = new weaponGuessBox();
			this.setLayout(new GridLayout(0,1));
			add(person);
			add(room);
			add(wpn);
		}
	}


	public class GuessResult extends JPanel {
		private JLabel responseField;
		public GuessResult () {
			responseField = new JLabel("");
			setBorder(new TitledBorder(new EtchedBorder(), "Respone"));
			setLayout(new GridLayout(0,2));
			updateDisplay();
			add(responseField);
		}
		public void updateDisplay(){
			responseField.setText("");
		}
	}
	public class GuessAsked extends JPanel {
		private JLabel guessField;
		public GuessAsked () {
			setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
			setLayout(new GridLayout(0,2));
			updateDisplay();
			add(guessField);
		}
		public void updateDisplay(){
			guessField = new JLabel("");
		}


	}

	public class mouseTracker implements MouseListener {


		public void mouseClicked(MouseEvent e) {


		}


		public void mouseEntered(MouseEvent e) {

		}


		public void mouseExited(MouseEvent e) {

		}


		public void mousePressed(MouseEvent e) {


		}


		public void mouseReleased(MouseEvent e) {
			int mouseX = e.getX();
			int mouseY = e.getY();
			int col = mouseX / board.SIZE + 1;
			int row = mouseY / board.SIZE + 1;
			boolean valid = false;
			for(BoardCell c : board.getTargets()){
				if(c.getX() == col && c.getY() == row){
					valid=true;
					hasMadeTurn=true;
					board.getPlayers().get(board.whoseTurn).currX = col-1;
					board.getPlayers().get(board.whoseTurn).currY = row-1;
					board.paintComponent(board.getGraphics());
				} 
			} 
			if(!valid){
				String message = "That is not a valid selection choose again.";
				JOptionPane.showMessageDialog(null, message);
			} 
		}
	}



	public static void main(String[] args){
		ClueGameGui game = new ClueGameGui();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setVisible(true);
	}
}
