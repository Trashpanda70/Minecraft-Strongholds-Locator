package ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import util.LinkedList;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import model.*;
/**
 * GUI for RingCalculator
 * @see RingCalculator
 */
public class RingCalculatorGUI extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JPanel top;
	private JPanel bottom;
	private JRadioButton netherRadBtn = new JRadioButton(); //If checked will display coords in nether coords
	private JTextField xField = new JTextField(5); //Field to enter x coord
	private JTextField zField = new JTextField(5); //Field to enter z coord
	private JButton calculateBtn; //Button to calculate coords
	private JButton resetBtn; //Button to reset for next input
	private JButton removeBtn; //Button to remove an entry from the table
	//labels
	private JLabel coordsLbl = new JLabel("Stronghold Coords: ");
	private JLabel xLbl = new JLabel("X = "); 
	private JLabel zLbl = new JLabel("Z = "); 
	private JLabel ringLbl = new JLabel("Ring: "); //will be updated to include the ring
	private JLabel numStrongholdsLbl = new JLabel("Num Strongholds: "); //will be updated to include the numStrongholds
	private JLabel strongholdsLeftLbl = new JLabel("Strongholds left: "); //will be updated to include numStrongholds left
	
	//Error message for when program closes unexpectedly
	private static final String ERRORMSG = 
			"An unexpected error has occured, please report this and what steps would be needed to reproduce it.";
	//Title for error message box
	private static final String ERRORTITLE = "That shouldn't happen!"; 
	
	private LinkedList<Coords> list; //List to be displayed
	boolean nether = false; //boolean to flag whether to use nether coordinates
	RingCalculator r; //Instance of RingCalculator to use for calculation
	private JTable coordsTbl; //Table that holds calculated coords
    private final String[] colNames = {"+/+", "+/-", "-/+", "-/-"}; //Table column names
    private Object[][] data; //Table column names
    private boolean setDefault = false; //Whether the user has set a default option for if the Coords are not in a ring
	//The following are all variables related to the dialog boxes used when trying to calculate coords
    private String title;
    private String message;
    private int choice;
    /**
	 * Starts GUI
	 */
	public RingCalculatorGUI()
	{
		 Container c = getContentPane();
		 setTitle("Stronghold Ring Calculator");
		 setSize(600,500);
		 setLocation(50, 50);
		 setDefaultCloseOperation(EXIT_ON_CLOSE);
		 BoxLayout box = new BoxLayout(c, BoxLayout.Y_AXIS);
		 c.setLayout(box);
		 //construct panels
		 setTop(); 
		 setBottom(); 
		 //add panels to container
		 c.add(top);
		 c.add(bottom);
		 setVisible(true);
	}
	//Construct the top panel
	private void setTop() 
	{
		//GridBagLayout constraints
		GridBagConstraints gbc = new GridBagConstraints();
		top = new JPanel();
		TitledBorder border = BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Stronghold Information");
		top.setBorder(border);
		GridBagLayout bag = new GridBagLayout();
		top.setLayout(bag);
		//set panels
		JPanel xCoord = new JPanel();
		xCoord.add(xLbl);
		xCoord.add(xField);
		JPanel zCoord = new JPanel();
		zCoord.add(zLbl);
		zCoord.add(zField);
		//Add label and panels to top row of GridBagLayout
		gbc.gridx = 0; gbc.gridy = 0; //sets "coordinates" of GBL (GridBagLayout) to add component to
		top.add(coordsLbl, gbc); //Adds component with respect to GBL
		gbc.gridx = 1; 
		top.add(xCoord, gbc);
		gbc.gridx = 2; 
		top.add(zCoord, gbc);
		//Add second row of GBL
		gbc.gridx = 0; gbc.gridy = 1;
		calculateBtn = new JButton("Calculate");
		calculateBtn.addActionListener(this);
		top.add(calculateBtn, gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2; //make it take up two spaces
		numStrongholdsLbl.setAlignmentX(LEFT_ALIGNMENT); //left align so text looks centered
		top.add(numStrongholdsLbl, gbc);
		//Add third row
		gbc.gridx = 0; gbc.gridy = 2;
		gbc.gridwidth = 1;
		top.add(ringLbl, gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		strongholdsLeftLbl.setAlignmentX(LEFT_ALIGNMENT);
		top.add(strongholdsLeftLbl, gbc);
	}
	//Construct the bottom panel
	private void setBottom()
	{
		bottom = new JPanel();
		TitledBorder border = BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Calculated Information");
		bottom.setBorder(border);
		//Create JPanel with reset button and nether radiobutton
		JPanel functions = new JPanel();
		resetBtn = new JButton("Reset");
		resetBtn.addActionListener(this);
		functions.add(resetBtn);
		removeBtn = new JButton("Remove Coords");
		removeBtn.addActionListener(this);
		functions.add(removeBtn);
		netherRadBtn = new JRadioButton("Nether Coords");
		netherRadBtn.addActionListener(this);
		functions.add(netherRadBtn);
		functions.setAlignmentX(RIGHT_ALIGNMENT);
		//Set BoxLayout for bottom panel
		BoxLayout b = new BoxLayout(bottom, BoxLayout.Y_AXIS);
		bottom.setLayout(b);
		bottom.add(functions);
		data = new Object[15][4];
		coordsTbl = new JTable(data, colNames) {
			private static final long serialVersionUID = 1L;
			//Override isCellEditable to make table not editable
			public boolean isCellEditable(int row, int column) {                
                return false;               
			};
		};
		coordsTbl.setPreferredScrollableViewportSize(new Dimension(500, 300));
		coordsTbl.setFillsViewportHeight(true);
		coordsTbl.setDragEnabled(false);
		coordsTbl.setRowSelectionAllowed(false);
		//If a cell is selected and the delete key is pressed remove data from cell
		coordsTbl.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				JTable source = (JTable) e.getSource();
				int row = source.getSelectedRow();
				int col = source.getSelectedColumn();
				Object entry = null;
				try {
					entry = data[row][col];
				}
				catch (ArrayIndexOutOfBoundsException ex) {
					//Ignore
				}
				if ((KeyEvent.VK_DELETE == e.getKeyChar() || KeyEvent.VK_BACK_SPACE == e.getKeyChar())
						&& entry != null) {
					remove(row, col);
				}
			}
		});
		JScrollPane scroll = new JScrollPane(coordsTbl);
		bottom.add(scroll);
	}
	//When an action is performed on a component
	@Override
	public void actionPerformed(ActionEvent e)
	{
		//calculate button
		if (e.getSource() == calculateBtn) {
			double x = 0;
			double z = 0;
			try {
				x = Double.parseDouble(xField.getText().trim());
				z = Double.parseDouble(zField.getText().trim());
				r = new RingCalculator(x, z, false);
				list = r.calcStrongholds(nether);
				fillData(r.CoordsToArray(list));
				update();
			}
			catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Invalid text input, please enter proper coordinates.",
						"Invalid input", JOptionPane.ERROR_MESSAGE);
				xField.setText(""); 
				zField.setText("");
			}
			catch (IllegalCoordsException ex) {
				if (!setDefault) {
					title = "Invalid Coordinates";
					message = "The provided coordinates were not calculated to be in a Stronghold ring.\n"
							+ "Would you like to enter the ring manaully, have the program guess the ring, or "
							+ "try again?";
					String[] options = {"Enter Manually", "Guess", "Try Again"};
					choice = JOptionPane.showOptionDialog(this, message, title, JOptionPane.YES_NO_CANCEL_OPTION, 
							JOptionPane.WARNING_MESSAGE, null, options, options[2]);
					
					//Possibly set default choice
					String[] defaultOptions = {"No", "Yes"};
					int defaultChoice = JOptionPane.showOptionDialog(this, "Would you like to set this as your default option?\n"
							+ "Please note that this cannot be changed unless the program is restarted.", 
							"Set default option?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, 
							defaultOptions, defaultOptions[1]);
					if (defaultChoice == 1) 
						setDefault = true;
				}
				//What to do based on choice
				if (choice == 0) { //Manually enter a ring
					Integer[] ringChoices = {1, 2, 3, 4, 5, 6, 7, 8};
					title = "Ring Selection";
					message = "Please choose a ring that the Stronghold is in.";
					Integer i = (Integer) JOptionPane.showInputDialog(this, title, message, JOptionPane.QUESTION_MESSAGE, 
							null, ringChoices, ringChoices[0]);
					try {
						r = new RingCalculator(x, z, true);
					}
					catch (IllegalCoordsException ex1) { //Unexpected exception
						JOptionPane.showMessageDialog(this, ERRORMSG, ERRORTITLE, JOptionPane.ERROR_MESSAGE);
						System.exit(1);
					}
					r.setRing(i);
					list = r.calcStrongholds(nether);
					fillData(r.CoordsToArray(list));
					update();
				}
				else if (choice == 1) { //Guess the ring
					try {
						r = new RingCalculator(x, z, true);
					}
					catch (IllegalCoordsException ex1) { //Unexpected exception
						JOptionPane.showMessageDialog(this, ERRORMSG, ERRORTITLE, JOptionPane.ERROR_MESSAGE);
						System.exit(1);
					}
					r.guessRing(x, z);
					list = r.calcStrongholds(nether);
					fillData(r.CoordsToArray(list));
					update();
				}
				else { //Try again
					xField.setText(""); 
					zField.setText("");
				}
			}
		}
		//reset button
		else if (e.getSource() == resetBtn) {
			xField.setText(""); 
			zField.setText("");
			r = null;
			fillData(new String[15][4]); //clear data by filling with new empty array
			update();
		}
		//nether radio button
		else if (e.getSource() == netherRadBtn) {
			nether = netherRadBtn.isSelected();
		}
		//remove button
		else if (e.getSource() == removeBtn) {
			int row = coordsTbl.getSelectedRow();
			int col = coordsTbl.getSelectedColumn();
			Object o = null;
			try {
				o = data[row][col];
				if (row != -1 && col != -1 && o != null)
					remove(row, col);
			}
			catch (ArrayIndexOutOfBoundsException ex) {
				//Ignore
			}
			coordsTbl.clearSelection();
		}
	}
	//Fills table data with given array
	private void fillData(String[][] arr) 
	{
		if (arr != null) {
			data = new Object[arr.length][arr[0].length];
			for (int i = 0; i < arr.length; i++) {
				for (int j = 0; j < arr[0].length; j++) {
					data[i][j] = arr[i][j];
					coordsTbl.setValueAt(arr[i][j], i, j);
				}
			}
		}
	}
	//Updates GUI with new calculations
	private void update() 
	{
		if (r == null) {
			numStrongholdsLbl.setText("Num Strongholds: ");
			strongholdsLeftLbl.setText("Strongholds Left: ");
			ringLbl.setText("Ring: ");
		}
		else {
			numStrongholdsLbl.setText("Num Strongholds: " + r.getNumStrongholds());
			strongholdsLeftLbl.setText("Strongholds Left: " + list.size());
			ringLbl.setText("Ring: " + r.getRing());
		}
	}
	//Removes a cell from the table
	private void remove(int row, int col) 
	{
		try {
			String s = (String) data[row][col];
			for (Coords c : list) {
				if (s.equals(c.toString())) {
					list.remove(c);
					if (list.size() > 0)
						fillData(r.CoordsToArray(list));
					else 
						coordsTbl.setValueAt(null, row, col);
					break;
				}
			}
			update();
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Could not remove Coordinates", "Don't do that", 
					JOptionPane.ERROR_MESSAGE);
		}
	}
	//main to start GUI
	public static void main(String[] args) {
		new RingCalculatorGUI();
	}
}