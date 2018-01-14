import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.Random;
import javax.swing.Timer;

import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;


public class Minesweeper extends Object implements ActionListener {

	static int mineRows = 9;
	static int mineColumns = 9; 
	static int bombCount = 10;
	static int bombsRemaining = bombCount;
	static int seconds = 0;
	static Timer globalTimer;
	static JLabel bombsRemainingLabel = new JLabel(Integer.toString(bombsRemaining));
	static boolean isPopup = false;

	static MyButton[][] mineGrid = new MyButton[mineRows][mineColumns];
	
	static class MyButton extends JButton {
	    public boolean isBomb = false;
	    public boolean checked = false;
	    public boolean flagged = false;
	}
	
	JButton btnNewButton;
	

	public static void checkForWin(final JPanel myPanel, final JLabel timer, final JFrame frame)
	{
		boolean won = true;
		for (int currRow = 0; currRow < mineRows; currRow++)
		{
			for (int currColumn = 0; currColumn < mineColumns; currColumn++)
			{
				//If any bombs aren't flagged
				if (mineGrid[currRow][currColumn].isBomb == true && mineGrid[currRow][currColumn].flagged == false)
				{
					won = false;
				}
				//If all bombs aren't flagged
				if (bombsRemaining != 0)
				{
					won = false;
				}
				//If any space isn't revealed and isn't flagged
				if (mineGrid[currRow][currColumn].checked == false && mineGrid[currRow][currColumn].flagged == false)
				{
					won = false;
				}
			}
		}
		
		if (won == true)
		{
			//Stop the timer
			globalTimer.stop();

			// Make buttons unclickable
			for (int currRow = 0; currRow < mineRows; currRow++)
			{
				for (int currColumn = 0; currColumn < mineColumns; currColumn++)
				{
					for (int i = 0; i < mineGrid[currRow][currColumn].getMouseListeners().length; i++) {
						mineGrid[currRow][currColumn].removeMouseListener(mineGrid[currRow][currColumn].getMouseListeners()[i]);
					}
				}
			}

			
			//Create a pop-up if one doesn't already exist
			if (isPopup == false) {
				isPopup = true;
				JFrame winPopup = new JFrame();
				JLabel message = new JLabel("<html><body>Congrats! You won!<br><br>Your time was: "
											 + seconds + " seconds</body></html>");
				winPopup.add(message);
			
				//Reset the game when the popup closes
				winPopup.addWindowListener(new WindowAdapter() {
	            	public void windowClosing(WindowEvent e) {
	            		bombsRemaining = bombCount;
	            		bombsRemainingLabel.setText(Integer.toString(bombsRemaining));
	            	
	            		myPanel.removeAll();
	                	generateBoard(myPanel, timer, frame);
	                
	                	timer.setText("0");
	                	seconds = 0;
	                	globalTimer.restart();

	                	isPopup = false;
	                
	                	frame.validate();
	                	frame.repaint();
	            	}
	       		});
			
				//Display pop-up
				winPopup.pack();
				winPopup.setVisible(true);
			}
		}
	}

	public static void lostGame(final JPanel myPanel, final JLabel timer, final JFrame frame, JButton triggeredButton) {

		for (int currRow = 0; currRow < mineRows; currRow++)
		{
			for (int currColumn = 0; currColumn < mineColumns; currColumn++)
			{
				// Make buttons unclickable
				for (int i = 0; i < mineGrid[currRow][currColumn].getMouseListeners().length; i++) {
						mineGrid[currRow][currColumn].removeMouseListener(mineGrid[currRow][currColumn].getMouseListeners()[i]);
				}

				// Reveal all other bombs on the board
				if (mineGrid[currRow][currColumn].isBomb == true)
				{
					try {
    					ImageIcon img = new ImageIcon("../bombUntriggered.png");
    					mineGrid[currRow][currColumn].setIcon(img);
					} catch (Exception io) { System.out.println("Can't find bombUntriggered image"); }
				}
			}
		}
		
		//Set the triggered bomb's picture
		try {
			ImageIcon img = new ImageIcon("../bombTriggered.png");
			triggeredButton.setIcon(img);
		} catch (Exception io) {System.out.println("Can't find bombTriggered image");}
		
		//Stop the timer
		globalTimer.stop();
		
		//Create a pop-up if one doesn't already exist
		if (isPopup == false) {
			isPopup = true;
			JFrame winPopup = new JFrame();
			JLabel message = new JLabel("<html><body>Too bad! You Lost!<br><br>Your time was: " + seconds + " seconds</body></html>");
			winPopup.add(message);
		
			//Reset the game when the popup closes
			winPopup.addWindowListener(new WindowAdapter() {
            	public void windowClosing(WindowEvent e) {
            		bombsRemaining = bombCount;
            		bombsRemainingLabel.setText(Integer.toString(bombsRemaining));
            	
            		myPanel.removeAll();
                	generateBoard(myPanel, timer, frame);
                
                	timer.setText("0");
                	seconds = 0;
                	globalTimer.restart();

                	isPopup = false;
                
                	frame.validate();
                	frame.repaint();
            	}
        	});
		
			//Display pop-up
			winPopup.pack();
			winPopup.setVisible(true);
		}

	}
	
	public static void showBombSurroundCount(int rowNumber, int columnNumber) {
		int bombSurroundCount = 0;
				
		if (mineGrid[rowNumber][columnNumber].checked == false)
		{
			mineGrid[rowNumber][columnNumber].checked = true;

			// Remove any flags and correct the flag count
			if (mineGrid[rowNumber][columnNumber].flagged == true) {
				mineGrid[rowNumber][columnNumber].flagged = false;
				bombsRemaining++;
		    	bombsRemainingLabel.setText(Integer.toString(bombsRemaining));
			}

			
			//Check surrounding squares for bombs
			try
			{
				if (mineGrid[rowNumber][columnNumber - 1].isBomb == true)		//Left
					bombSurroundCount++;
			} catch (Exception e) {};
			try
			{
				if (mineGrid[rowNumber][columnNumber + 1].isBomb == true)		//Right
					bombSurroundCount++;
			} catch (Exception e) {};
			try
			{
				if (mineGrid[rowNumber - 1][columnNumber].isBomb == true)		//Top
					bombSurroundCount++;
			} catch (Exception e) {};
			try
			{
				if (mineGrid[rowNumber + 1][columnNumber].isBomb == true)		//Bottom
					bombSurroundCount++;
			} catch (Exception e) {};
			try
			{
				if (mineGrid[rowNumber - 1][columnNumber - 1].isBomb == true)	//Top-Left
					bombSurroundCount++;
			} catch (Exception e) {};
			try
			{
				if (mineGrid[rowNumber - 1][columnNumber + 1].isBomb == true)	//Top-Right
					bombSurroundCount++;
			} catch (Exception e) {};
			try
			{
				if (mineGrid[rowNumber + 1][columnNumber + 1].isBomb == true)	//Bottom-Right
					bombSurroundCount++;
			} catch (Exception e) {};
			try
			{
				if (mineGrid[rowNumber + 1][columnNumber - 1].isBomb == true)	//Bottom-Left
					bombSurroundCount++;
			} catch (Exception e) {};
	
			
			//Display corresponding image
			try {
				ImageIcon img = new ImageIcon("../" + bombSurroundCount + ".png");
				mineGrid[rowNumber][columnNumber].setIcon(img);
			} catch (Exception io) {System.out.println("Can't find num image");}
			
			
			//Call recursively to check all neighboring squares if the current one is blank
			if (bombSurroundCount == 0)
			{
				if (columnNumber != 0)
					showBombSurroundCount(rowNumber, columnNumber - 1);		//Left
				if (columnNumber != (mineColumns - 1))
					showBombSurroundCount(rowNumber, columnNumber + 1);		//Right
				if (rowNumber != 0)
					showBombSurroundCount(rowNumber - 1, columnNumber);		//Top
				if (rowNumber != (mineRows - 1))
					showBombSurroundCount(rowNumber + 1, columnNumber);		//Bottom
				if (columnNumber != 0 && (rowNumber != 0))
					showBombSurroundCount(rowNumber - 1, columnNumber - 1);	//Top-Left
				if (columnNumber != (mineColumns - 1) && (rowNumber != 0))
					showBombSurroundCount(rowNumber - 1, columnNumber + 1);	//Top-Right
				if (columnNumber != (mineColumns - 1) && (rowNumber != (mineRows - 1)))
					showBombSurroundCount(rowNumber + 1, columnNumber + 1);	//Bottom-Right
				if (columnNumber != 0 && (rowNumber != (mineRows - 1)))
					showBombSurroundCount(rowNumber + 1, columnNumber - 1);	//Bottom-Left
				//Logic could be optimized, but the program runs without noticeable slowdown as it is
			}
		}
		return;
	}
	
	public static void generateBoard(final JPanel myPanel, final JLabel timer, final JFrame frame) {
		mineGrid = new MyButton[mineRows][mineColumns];
		
		// Randomize bomb layout
		// Store all bombs in a hashtable with ordered pairs based on row, column (e.g. 4,3)
		Hashtable <String, Integer> bombPositions = new Hashtable <String, Integer>();
		Random rand = new Random();

		for (int i = 0; i < bombCount; i++)
		{	
			int randomRow = rand.nextInt(mineRows);
			int randomColumn = rand.nextInt(mineColumns);

			// While a bomb already exists in that position, keep randomly generating positions
			while (bombPositions.containsKey(Integer.toString(randomRow) + "," + Integer.toString(randomColumn)))
			{
				randomRow = rand.nextInt(mineRows);
				randomColumn = rand.nextInt(mineColumns);
			}
			bombPositions.put(Integer.toString(randomRow) + "," + Integer.toString(randomColumn), 1);
		}
    	
    	// Generate board
        for (int currRow = 0; currRow < mineRows; currRow++)
        {
        	for (int currColumn = 0; currColumn < mineColumns; currColumn++)
        	{
	        	final MyButton btemp = new MyButton();
	        	btemp.setBorder(BorderFactory.createRaisedBevelBorder());
	        	btemp.setPreferredSize(new Dimension(20,20));
	        	
	        	// If this square contains a bomb
	        	if (bombPositions.containsKey(Integer.toString(currRow) + "," + Integer.toString(currColumn)))
	        	{
	        		btemp.isBomb = true;
	        	}
	        	
	        	
	        	// Add click logic for each button
    			final int currRowFinal = currRow;
    			final int currColumnFinal = currColumn;
    			
	        	btemp.addMouseListener(new MouseAdapter()
	            {
	            	public void mouseClicked(MouseEvent e)
	            	{
	            		
	            		//Right-Click places flags
	            		if (e.getButton() == MouseEvent.BUTTON3)
	            		{
	            			if (btemp.checked == false)		//You can't flag an already revealed square
	            			{
	            				if (btemp.flagged == true)
		            			{
		            				btemp.flagged = false;
		            				bombsRemaining++;
		            				bombsRemainingLabel.setText(Integer.toString(bombsRemaining));
		            				
		            				//Remove Flag Icon
		            				btemp.setIcon(null);
		            			}
		            			else
		            			{
		            				btemp.flagged = true;
		            				bombsRemaining--;
		            				bombsRemainingLabel.setText(Integer.toString(bombsRemaining));
		            				
			            			//Set Flag Icon
			            			try {
			        					ImageIcon img = new ImageIcon("../Flag.png");
			        					btemp.setIcon(img);
			        				} catch (Exception io) {System.out.println("Can't find flag image");}
		            			}
	            			}
	            		}
	            		
	            		//Left-Click reveals squares
	            		else
	            		{
		            		//Bomb triggered- game lost
		            		if (btemp.isBomb == true)
		            		{
		            			lostGame(myPanel, timer, frame, btemp);
		            		}
		            		
		            		//Show the number of surrounding bombs 
		            		else
		            			{
		            				showBombSurroundCount(currRowFinal, currColumnFinal);
		            				
		            				// Remove flag if there is one
		            				if (btemp.flagged == true)
		            				{
		            					bombsRemaining++;
		            					bombsRemainingLabel.setText(Integer.toString(bombsRemaining));
		            				}
		            			}
	            		}
	            		
	            		checkForWin(myPanel, timer, frame);
	            	}
	            });
	        	
	        	mineGrid[currRow][currColumn] = btemp;
	        	myPanel.add(btemp);
        	}
        }
    }


    // Specify the look and feel to use by defining the LOOKANDFEEL constant
    // Valid values are: null (use the default), "Metal", "System", "Motif",
    // and "GTK"
    final static String LOOKANDFEEL = "Motif";
    
    // If you choose the Metal L&F, you can also choose a theme.
    // Specify the theme to use by defining the THEME constant
    // Valid values are: "DefaultMetal", "Ocean",  and "Test"
    final static String THEME = "DefaultMetal";

    
    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = frame.getContentPane();
        c.setLayout(new BorderLayout());
        
        
    	//Flow layout for top elements (timer, bomb counter, New game button)
    	JPanel myPanel = new JPanel();
        final JPanel myPanel2 = new JPanel();
        myPanel.setBackground( Color.gray );
        myPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        final JLabel timer = new JLabel(Integer.toString(seconds));
        
        myPanel.add(bombsRemainingLabel);
        
        JButton b1 = new JButton("New");
        b1.setBorder(BorderFactory.createRaisedBevelBorder());
        b1.setPreferredSize(new Dimension(40,40));
        b1.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		bombsRemaining = bombCount;
        		bombsRemainingLabel.setText(Integer.toString(bombsRemaining));
        		
        		myPanel2.removeAll();
        		generateBoard(myPanel2, timer, frame);
        		
        		timer.setText("0");
        		seconds = 0;
        		globalTimer.restart();
        		
        		frame.validate();
        		frame.repaint();
        	}
        });
        myPanel.add(b1);
        
        myPanel.add(timer);
        
        ActionListener taskPerformer = new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		seconds++;
        		timer.setText(Integer.toString(seconds));
        	}
        };
        globalTimer = new Timer(1000, taskPerformer);
        globalTimer.start();


        c.add(myPanel, BorderLayout.NORTH);
        
        
        //Grid layout for mine squares  
        myPanel2.setBackground(Color.black);
        myPanel2.setLayout(new GridLayout(mineRows, mineColumns, 0, 0));
        
        generateBoard(myPanel2, timer, frame);

        c.add(myPanel2, BorderLayout.SOUTH);
        

        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;
        JRadioButtonMenuItem rbMenuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("Game");
        menu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu);

        //New- re-randomize the board with the same # of rows and columns
        menuItem = new JMenuItem("New",
                                 KeyEvent.VK_F2);
        menuItem.setMnemonic(KeyEvent.VK_F2);
        menuItem.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		bombsRemaining = bombCount;
        		bombsRemainingLabel.setText(Integer.toString(bombsRemaining));
        		
        		myPanel2.removeAll();
        		generateBoard(myPanel2, timer, frame);
        		
        		timer.setText("0");
                seconds = 0;
                globalTimer.restart();
        		
        		frame.validate();
        		frame.repaint();
        	}
        });
        menu.add(menuItem);
        

        menu.addSeparator();
        ButtonGroup group = new ButtonGroup();
        
        //Beginner- 9x9 with 10 bombs
        rbMenuItem = new JRadioButtonMenuItem("Beginner");
        rbMenuItem.setSelected(true);
        rbMenuItem.setMnemonic(KeyEvent.VK_B);
        rbMenuItem.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		mineRows = 9;
        		mineColumns = 9;
        		bombCount = 10;
        		bombsRemaining = bombCount;
        		bombsRemainingLabel.setText(Integer.toString(bombsRemaining));
        		
        		//Redraw game board
        		myPanel2.removeAll();
        		myPanel2.setLayout(new GridLayout(mineRows, mineColumns, 0, 0));
        		generateBoard(myPanel2, timer, frame);
        		
        		timer.setText("0");
                seconds = 0;
                globalTimer.restart();
        		
        		frame.validate();
        		frame.repaint();
        		frame.pack();
        		//Re-randomize board layout
        	}
        });
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        //Intermediate- 16x16 with 40 bombs
        rbMenuItem = new JRadioButtonMenuItem("Intermediate");
        rbMenuItem.setMnemonic(KeyEvent.VK_N);
        rbMenuItem.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		mineRows = 16;
        		mineColumns = 16;
        		bombCount = 40;
        		bombsRemaining = bombCount;
        		bombsRemainingLabel.setText(Integer.toString(bombsRemaining));
        		
        		//Redraw game board
        		myPanel2.removeAll();
        		myPanel2.setLayout(new GridLayout(mineRows, mineColumns, 0, 0));
                generateBoard(myPanel2, timer, frame);
                
                timer.setText("0");
                seconds = 0;
                globalTimer.restart();
        		
        		frame.validate();
        		frame.repaint();
        		frame.pack();

        		//Re-randomize board layout
        	}
        });
        group.add(rbMenuItem);
        menu.add(rbMenuItem);
        
        //Expert- 30x16 with 99 bombs
        rbMenuItem = new JRadioButtonMenuItem("Expert");
        rbMenuItem.setMnemonic(KeyEvent.VK_E);
        rbMenuItem.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		mineRows = 16;
        		mineColumns = 30;
        		bombCount = 99;
        		bombsRemaining = bombCount;
        		bombsRemainingLabel.setText(Integer.toString(bombsRemaining));
        		
        		//Redraw game board
        		myPanel2.removeAll();
        		myPanel2.setLayout(new GridLayout(mineRows, mineColumns, 0, 0));
                generateBoard(myPanel2, timer, frame);
                
                timer.setText("0");
                seconds = 0;
                globalTimer.restart();
        		
        		frame.validate();
        		frame.repaint();
        		frame.pack();

        		//Re-randomize board layout
        	}
        });
        group.add(rbMenuItem);
        menu.add(rbMenuItem);
        
        menu.addSeparator();
        
        //Exit
        menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		System.exit(0);
        	}
        });
        group.add(menuItem);
        menu.add(menuItem);


        // Display the frame
        frame.setJMenuBar(menuBar);

        frame.pack();
        frame.setVisible(true);
    }

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}

}


class MyWin extends WindowAdapter {
	 public void windowClosing(WindowEvent e)
  {
      System.exit(0);
  }
}
