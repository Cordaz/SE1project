package it.polimi.ingsw.BianchiCorneo.client;

import it.polimi.ingsw.BianchiCorneo.actions.Action;
import it.polimi.ingsw.BianchiCorneo.maps.MAPConst;
import it.polimi.ingsw.BianchiCorneo.maps.cards.AdrenalineCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.AttackCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.DefenceCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.LightCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.ObjectCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.SedativeCard;
import it.polimi.ingsw.BianchiCorneo.maps.cards.TeleportCard;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.Sector;
import it.polimi.ingsw.BianchiCorneo.maps.sectors.SectorList;
import it.polimi.ingsw.BianchiCorneo.players.Player;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/**
 * Graphical interface for user interface, implements UserInterface
 * 
 * @author Mattia Bianchi
 *
 */
public class GUI implements UserInterface {
	private JFrame frame = new JFrame("EFTAIOS");
	private JPanel jpTop = new JPanel(new BorderLayout());
	private JPanel jpMap = new JPanel(new BorderLayout());
	private JPanel jpMid = new JPanel(new BorderLayout());
	private JPanel jpBot = new JPanel(new BorderLayout());
	private JPanel infoMap = new JPanel(new BorderLayout());
	private JPanel jpCard = new JPanel(new GridLayout(3, 1));
	private JTextArea path = new JTextArea();
	private JPanel jpFill = new JPanel(new BorderLayout());
	private JButton infoCard = new JButton("Info Cards");
	private JButton card1 = new JButton("Card 1: [...]");
	private JButton card2 = new JButton("Card 2: [...]");
	private JButton card3 = new JButton("Card 3: [...]");
	private JButton move = new JButton("Move");
	private JButton attack = new JButton("Attack");
	private JButton endTurn = new JButton("End turn");
	private String choice = "";
	private JTextField insertName = new JTextField(20);
	private JScrollPane scrollPane = new JScrollPane();
	private JTextArea notifies = new JTextArea();
	private String name = "";
	private Object lock = new Object();
	private Sector[][] sectors;
	private Sector curSector;
	private SectorList possibleMoves;
	private String selection = "";
	
	
	public GUI() {
		connectionGUI();
	}
	
	/**
	 * Creates the basic frame where will be inserted all the necessary panels
	 */
	public void connectionGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setFont(new Font("Courier", Font.PLAIN, 12));
		
		setLegend();
		
		setNotify();
		
		JLabel lblCopyRights = new JLabel("Copyrights Bianchi & Corneo", JLabel.CENTER);
		lblCopyRights.setFont(new Font("Courier", Font.PLAIN, 13));
		
		jpBot.add(lblCopyRights, BorderLayout.CENTER);
		frame.add(jpBot, BorderLayout.SOUTH);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Sets the header panel, containing information on the map
	 */
	public void setLegend() {
		JTextArea legend = new JTextArea();
		legend.setText("Legend: \n\n"
				+ "Magenta: Alien base; \n"
				+ "Cyan:    Human base; \n"
				+ "Blue:    Ship potentially usable; \n"
				+ "Red:     Ship used or not usable; \n"
				+ "Green:   Current position; \n"
				+ "Yellow:  Reachable sector; \n"
				+ "Grey:    Dagerous sector; \n"
				+ "White:   Safe sector");
		legend.setFont(new Font("Courier", Font.PLAIN, 13));
		legend.setForeground(Color.GREEN);
		legend.setBackground(Color.BLACK);
		legend.setEditable(false);
		
		JLabel mapName = new JLabel();
		mapName.setFont(new Font("Courier", Font.PLAIN, 13));
		mapName.setText("           " + MAPConst.MAPNAME.substring(5));
		mapName.setForeground(Color.GREEN);
		infoMap.add(legend, BorderLayout.WEST);
		infoMap.add(mapName, BorderLayout.CENTER);
		infoMap.setBackground(Color.BLACK);
	}
	
	/**
	 * Sets up the shell where the information of the game will be displayed
	 */
	public void setNotify() {
		scrollPane.setFont(new Font("Courier", Font.PLAIN, 13));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		notifies.setFont(new Font("Courier", Font.PLAIN, 13));
		notifies.setForeground(Color.GREEN);
		notifies.setBackground(Color.BLACK);
		notifies.setEditable(false);
		scrollPane.setViewportView(notifies);
	}
	
	/**
	 * Sets the dimension and the positioning of the frame on the screen
	 * 
	 * @param x coordinate x
	 * @param y coordinate y
	 */
	public void setScreenPos(int x, int y) {
		Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		p.setLocation(p.getX() - x/2, p.getY() - y/2);
		frame.setLocation(p);
		frame.setSize(x, y);
		frame.setResizable(false);
	}

	@Override
	public void reqConn() {
		JLabel jlConn = new JLabel("Connection established", JLabel.CENTER);
		jlConn.setFont(new Font("Courier", Font.PLAIN, 13));
		jpTop.add(jlConn, BorderLayout.CENTER);
		frame.add(jpTop, BorderLayout.NORTH);
		frame.setVisible(true);
	}

	@Override
	public String reqName() {
		JPanel jpReqName = new JPanel(new BorderLayout());
		frame.add(jpMid, BorderLayout.CENTER);
		
		JLabel jlName = new JLabel("Insert your name", JLabel.CENTER);
		jlName.setFont(new Font("Courier", Font.PLAIN, 13));
		jpReqName.add(jlName, BorderLayout.NORTH);
		
		jpReqName.add(insertName, BorderLayout.CENTER);
		
		JButton btnEnter = new JButton("Enter");
		btnEnter.setFont(new Font("Courier", Font.PLAIN, 13));
		jpReqName.add(btnEnter, BorderLayout.SOUTH);
		
		jpMid.add(jpReqName);
		
		frame.pack();
		frame.setVisible(true);
		
		btnEnter.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				name = insertName.getText();
				synchronized(lock) {
					lock.notifyAll();
				}
				
			}
		});
		synchronized(lock) {
			while ("".equals(name)) {
				try {
					lock.wait();
				} catch (InterruptedException e1) {
					Logger.getGlobal().log(Level.ALL, "Error.", e1);
				}
			}
		}
		jpTop.removeAll();
		jpMid.removeAll();
		JLabel jlWaitGame = new JLabel("Waiting for game to start...", JLabel.CENTER);
		jlWaitGame.setFont(new Font("Courier", Font.PLAIN, 13));
		jpMid.add(jlWaitGame, BorderLayout.CENTER);
		
		frame.setVisible(true);
		
		setFrameComp();
		
		return name;
	}
	
	/**
	 * Sets up all the components of the frame
	 */
	public void setFrameComp() {
		path.setFont(new Font("Courier", Font.PLAIN, 13));
		path.setForeground(Color.GREEN);
		path.setBackground(Color.BLACK);
		path.setEditable(false);
		move.setFont(new Font("Courier", Font.PLAIN, 13));
		attack.setFont(new Font("Courier", Font.PLAIN, 13));
		endTurn.setFont(new Font("Courier", Font.PLAIN, 13));
		infoCard.setFont(new Font("Courier", Font.PLAIN, 13));
		infoListener();
		JPanel actions = new JPanel(new GridLayout(1,3));
		actions.setBackground(Color.BLACK);
		actions.add(move);
		actions.add(attack);
		actions.add(endTurn);
		jpFill.add(actions, BorderLayout.WEST);			
		jpFill.add(infoCard, BorderLayout.EAST);
		JLabel photo = new JLabel();
		photo.setPreferredSize(new Dimension(200,200));
		jpFill.add(photo, BorderLayout.SOUTH);
		jpFill.setBackground(Color.BLACK);
		
		card1.setFont(new Font("Courier", Font.PLAIN, 13));			//TODO check color and other things
		card2.setFont(new Font("Courier", Font.PLAIN, 13));
		card3.setFont(new Font("Courier", Font.PLAIN, 13));
		
		jpCard.setBackground(Color.BLACK);
		jpCard.add(card1);
		jpCard.add(card2);
		jpCard.add(card3);
		jpCard.setPreferredSize(new Dimension(150, 610));
	}
	
	//constants and global variables
	final static int BSIZEY = 23; //board size.
	final static int BSIZEX = 14;
	final static int HEXSIZE = 40;	//hex size in pixels
	final static int BORDERS = 15;  
	final static int SCRSIZE = HEXSIZE * (BSIZEY + 1) + BORDERS*3; //screen size (vertical dimension).
	
	/**
	 * Class that draws the map
	 * 
	 * @author Mattia Bianchi
	 *
	 */
	class DrawingPanel extends JPanel {		
		private static final long serialVersionUID = 8743411048257439382L;

		/**
		 * Basic constructor
		 */
		public DrawingPanel() {	
			setBackground(Color.BLACK);
			setPreferredSize(new Dimension(820,610));
			
			MyMouseListener ml = new MyMouseListener();            
			addMouseListener(ml);
		}
 
		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setFont(new Font("Courier", Font.PLAIN, 13));
			super.paintComponent(g2);
			//draw grid
			for (int i=0;i<BSIZEY;i++) {
				for (int j=0;j<BSIZEX;j++) {
					sectors[j][i].setXYasVertex(false); //RECOMMENDED: leave this as FALSE.
					sectors[j][i].setHeight(HEXSIZE); //Either setHeight or setSize must be run to initialize the hex
					sectors[j][i].setBorders(BORDERS);
					if (sectors[j][i].sameAs(curSector))
						sectors[j][i].drawHex(i,j,g2, Color.GREEN);
					else if (possibleMoves.contains(sectors[j][i]))
						sectors[j][i].drawHex(i,j,g2, Color.YELLOW);
					else
						sectors[j][i].drawHex(i,j,g2);
				}
			}
		}
		
		/**
		 * Listener on the map sectors, registers a click on a sector and returns the sector clicked
		 * 
		 * @author Mattia Bianchi
		 *
		 */
		class MyMouseListener extends MouseAdapter {	//inner class inside DrawingPanel 
			@Override
			public void mouseClicked(MouseEvent e) { 
				int x = e.getX(); 
				int y = e.getY(); 
				Point p = new Point( Sector.pxtoHex(x, y) );
				if (p.x < 0 || p.y < 0 || p.x >= BSIZEY || p.y >= BSIZEX) return;
 
 
				//What do you want to do when a hexagon is clicked?
				selection = sectors[p.y][p.x].toString();
				synchronized(lock) {
					lock.notifyAll();
				}
			}		 
		} //end of MyMouseListener class 
	} // end of DrawingPanel class
	
	
	@Override
	public void drawMap(Player p) { 
		sectors = p.getMap().getSectors();
		possibleMoves = p.possibleMoves();
		curSector = p.getCurSector();
		DrawingPanel panel = new DrawingPanel();
		jpMap.add(panel, BorderLayout.SOUTH);
		jpMap.add(infoMap);
		jpMap.setBackground(Color.BLACK);
		frame.add(jpMap, BorderLayout.WEST);
		updatePath(p);
		updateCards(p.getObjects());
		
	}
	
	/**
	 * Updates the path of the player during the game
	 * 
	 * @param p player
	 */
	public void updatePath(Player p) {  
		path.setText("");
		for (int i = 0; i < p.getFormattedPath().length; i++) {
			if ((i + 1) % 13 == 0 && (i + 1) != 39)
				path.setText(path.getText() + p.getFormattedPath()[i] + "\n");
			else
				path.setText(path.getText() + p.getFormattedPath()[i]);
		}
		frame.setVisible(true);
	}
	
	/**
	 * Listener of the information button
	 */
	public void infoListener() {
		infoCard.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				ObjectCard obj1 = new AdrenalineCard();
				ObjectCard obj2 = new AttackCard();
				ObjectCard obj3 = new DefenceCard();
				ObjectCard obj4 = new LightCard();
				ObjectCard obj5 = new SedativeCard();
				ObjectCard obj6 = new TeleportCard();
				JOptionPane.showMessageDialog(null, "Informations about object cards: \n\n"
						+ obj1.toString() + ": " + obj1.getMsg() + "\n"
						+ obj2.toString() + ": " + obj2.getMsg() + "\n"
						+ obj3.toString() + ": " + obj3.getMsg() + "\n"
						+ obj4.toString() + ": " + obj4.getMsg() + "\n"
						+ obj5.toString() + ": " + obj5.getMsg() + "\n"
						+ obj6.toString() + ": " + obj6.getMsg() + "\n", "Cards Informations", JOptionPane.PLAIN_MESSAGE);
			}
		});
	}
	
	@Override
	public void waitTurn(Player p) {
		
		this.setScreenPos(1440, 850);
		updatePath(p);
		jpTop.setPreferredSize(new Dimension(10, 40));
		jpTop.add(path);
		jpMid.removeAll();
		
		jpMid.add(scrollPane, BorderLayout.CENTER);
		
		jpMid.add(jpFill, BorderLayout.SOUTH);
		jpMid.setBackground(Color.BLACK);
		
		frame.add(jpCard, BorderLayout.EAST);
		
		frame.setVisible(true);
	}

	@Override
	public String getSector(Player p) {
		updatePath(p);
		notifies.setText(notifies.getText() + "> Click on the sector chosen \n");
		selection = "";
		
		synchronized(lock) {
			while ("".equals(selection)) {
				try {
					lock.wait();
				} catch (InterruptedException e1) {
					Logger.getGlobal().log(Level.ALL, "Error.", e1);
				}
			}
		}
		path.setText("");
		return selection;
	}

	@Override
	public void viewMessage(String s) {
		notifies.setText(notifies.getText() + "> " + s +"\n");
	}
	
	@Override
	public String selectToDiscard(String s) {
		choice = "";
		JFrame toDiscard = new JFrame();
		JPanel jp = new JPanel(new FlowLayout());
		JButton obj1 = new JButton(card1.getText());
		jp.add(obj1);
		JButton obj2 = new JButton(card2.getText());
		jp.add(obj2);
		JButton obj3 = new JButton(card3.getText());
		jp.add(obj3);
		JButton obj4 = new JButton(s);
		jp.add(obj4);
		toDiscard.add(jp);
		toDiscard.pack();
		toDiscard.setVisible(true);
		obj1.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				choice = "1";
				synchronized(lock) {
					lock.notifyAll();
				}
			}
		});
		obj2.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				choice = "2";
				synchronized(lock) {
					lock.notifyAll();
				}
			}
		});
		obj3.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				choice = "3";
				synchronized(lock) {
					lock.notifyAll();
				}
			}
		});
		obj4.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				choice = "4";
				synchronized(lock) {
					lock.notifyAll();
				}
				
			}
		});
		synchronized(lock) {
			while ("".equals(choice)) {
				try {
					lock.wait();
				} catch (InterruptedException e1) {
					Logger.getGlobal().log(Level.ALL, "Error.", e1);
				}
			}
		}
		toDiscard.setVisible(false);
		return choice;
	}

	/**
	 * Updates the cards held by a player
	 * 
	 * @param objs cards held by the player
	 */
	public void updateCards(ObjectCard[] objs) {
		if (objs[0] != null)
			card1.setText(objs[0].toString());
		else
			card1.setText("Card 1: [...]");
		if (objs[1] != null)
			card2.setText(objs[1].toString());
		else
			card2.setText("Card 2: [...]");
		if (objs[2] != null)
			card3.setText(objs[2].toString());
		else
			card3.setText("Card 3: [...]");
	}
	
	/**
	 * Opens a dialog that asks the player an "yes or no" question
	 * 
	 * @param s text of the question
	 * @return the will of the user
	 */
	public String questionDialog(String s) {
		final JOptionPane optionPane = new JOptionPane(s, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
		final JDialog dialog = new JDialog(frame, "Click a button", true);
		dialog.setContentPane(optionPane);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		optionPane.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				String prop = e.getPropertyName();

				if (dialog.isVisible() && (e.getSource() == optionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
					dialog.setVisible(false);
				}
			}
		});
		dialog.pack();
		dialog.setVisible(true);

		int value = ((Integer)optionPane.getValue()).intValue();
		if (value == JOptionPane.YES_OPTION) {
			return "y";
		} else if (value == JOptionPane.NO_OPTION) {
			return "n";
		}
		return null;
	}

	@Override
	public void lackOfPlayers() {
		JOptionPane.showMessageDialog(null, "Not enough players to start a new game.\n", "Lack of players", JOptionPane.WARNING_MESSAGE);
	}
	
	@Override
	public void exitGame() {
		notifies.setText(notifies.getText() + "\n> See you next time!");
	}
	
	@Override
	public void serverNotifies(Action a) {
		notifies.setText(notifies.getText() + "> " + a + "\n");
	}
	
	@Override
	public String takeAction(Player p) {
		choice = "";
		card1.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				choice = "usecard_1";
				synchronized(lock) {
					lock.notifyAll();
				}
			}
		});
		card2.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				choice = "usecard_2";
				synchronized(lock) {
					lock.notifyAll();
				}
			}
		});
		card3.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				choice = "usecard_3";
				synchronized(lock) {
					lock.notifyAll();
				}
			}
		});
		move.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				choice = "move_";
				synchronized(lock) {
					lock.notifyAll();
				}
			}
		});
		attack.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				choice = "attack_";
				synchronized(lock) {
					lock.notifyAll();
				}
			}
		});
		endTurn.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				choice = "endturn_";
				synchronized(lock) {
					lock.notifyAll();
				}
			}
		});
		synchronized(lock) {
			while ("".equals(choice)) {
				try {
					lock.wait();
				} catch (InterruptedException e1) {
					Logger.getGlobal().log(Level.ALL, "Error.", e1);
				}
			}
		}
		if ("move_".equals(choice))
			choice += getSector(p);
		return choice;
	}

	@Override
	public void banned() {
		notifies.setText(notifies.getText() + "> You have been suspended due to inactivity,\n  please restart the client to begin a new game.\n");
	}

	@Override
	public void startTurn() {
		notifies.setText(notifies.getText() + "\n> It's your turn!\n\n");
	}
	
	@Override
	public void unlock() {
		synchronized(lock) {
			choice = "end";
			lock.notifyAll();
		}
	}
}
