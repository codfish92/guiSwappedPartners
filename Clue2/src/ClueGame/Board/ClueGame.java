package ClueGame.Board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ClueGame extends JFrame{
	private Board board;
	private DetectivePanel detective;
	private ControlPanel controls;
	public ClueGame () {
		super();
		board = new Board();
		board.loadConfigFiles();
		controls = new ControlPanel();
		add(board, BorderLayout.CENTER);
		add(controls, BorderLayout.SOUTH);
		setTitle("Clue");
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());
		setSize(1100, 900);
		detective = new DetectivePanel();
		detective.setSize(600, 500);
	}
	
	private class DieRoll extends JPanel{
		private int roll;
		private JTextArea display;
		public DieRoll(){
			setBorder(new TitledBorder (new EtchedBorder(), "Die Roll"));
			roll=board.rollDie();
			display = new JTextArea(2,20);
			updateDisplay();
			add(display);
		}
		private void updateDisplay(){
			display.setText(Integer.toString(roll));
		}
	}
	
	private class WhoseTurn extends JPanel{
		private String name, color;
		private JTextArea display;
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
		}
	}
	
	private class ControlPanel extends JPanel{
		private WhoseTurn whoseTurn;
		private JButton nextPerson;
		private JButton makeAccusation;
		private DieRoll die;
		public ControlPanel(){
			this.setLayout(new GridLayout(2, 3));
			whoseTurn = new WhoseTurn();
			nextPerson = new JButton("Next Player");
			makeAccusation = new JButton("Make an accusation.");
			die = new DieRoll();
			add(whoseTurn);
			add(nextPerson);
			add(makeAccusation);
			add(die);
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
	
	public static void main(String[] args){
		ClueGame game = new ClueGame();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setVisible(true);
	}
}
