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

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ClueGameGui extends JFrame{
	private Board board;
	private DetectivePanel detective;
	private ControlPanel controls;
	private PlayerHand hand;
	public ClueGameGui () {
		super();

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
		board.addMouseListener(new mouseTracker());
		this.splashScreen();
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
					currentPlayer.updateCurrentPosition(board);
					whoseTurn.nextTurn();
					die.updateDisplay();
					board.whoseTurn=whoseTurn.currentIndex;
					board.roll = die.roll;
					board.repaint();
					if (currentPlayer.getComputer()){
						ComputerPlayer tempPlayer = (ComputerPlayer) currentPlayer;
						BoardCell pickedCell = tempPlayer.pickMove(board.roll);
						board.getPlayers().get(board.whoseTurn).currX = pickedCell.getX();
						board.getPlayers().get(board.whoseTurn).currY = pickedCell.getY();
						board.getPlayers().get(board.whoseTurn).updateCurrentPosition(board);
						board.paintComponent(board.getGraphics());
					}
				}
				else if (e.getSource() == makeAccusation){

				}

			}

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
