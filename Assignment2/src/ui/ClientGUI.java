package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import gasstation.GasStationUtility;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import bl.BlProxy;
import bl.ClientController;

public class ClientGUI extends JFrame{
	private final int PORT_NUMBER = 3456;
	private JPanel jplMain;
	private JLabel jlbCarId, jlbReqWash, jlbReqFuel, jlbServerIp;
	private JRadioButton jrbYesReqWash, jrbNoReqWash;
	private JTextField jtfCarId, jtfReqFuel, jtfServerIp;
	private JButton jbnAddCar, jbnConnect;
	private SpringLayout layout;
	private ButtonGroup groupReqWash;
	private ClientController clientController;
	
	public ClientGUI(){		
		layout = new SpringLayout();
		jplMain = new JPanel();
		jplMain.setLayout(layout);

		jlbServerIp = new JLabel("Server IP");
		jtfServerIp = new JTextField(10);
		jbnConnect = new JButton("Connect");
		
		jlbCarId = new JLabel("Car ID");
		jtfCarId = new JTextField(20);
		
		jlbReqWash = new JLabel("Required Wash");
		jrbYesReqWash = new JRadioButton("Yes");
		jrbYesReqWash.setSelected(true);
		jrbNoReqWash = new JRadioButton("No");
		
		groupReqWash = new ButtonGroup();
		groupReqWash.add(jrbNoReqWash);
		groupReqWash.add(jrbYesReqWash);
		
		jlbReqFuel = new JLabel("Required Fuel");
		jtfReqFuel = new JTextField(20);
		jtfReqFuel.setText("0");
		
		jbnAddCar = new JButton("Add Car");
		

		
		jplMain.add(jlbServerIp);
		jplMain.add(jtfServerIp);
		jplMain.add(jbnConnect);
		jplMain.add(jlbCarId);
		jplMain.add(jtfCarId);
		jplMain.add(jlbReqWash);
		jplMain.add(jrbYesReqWash);
		jplMain.add(jrbNoReqWash);
		jplMain.add(jlbReqFuel);
		jplMain.add(jtfReqFuel);
		jplMain.add(jbnAddCar);
		
		//spring layout adjustments
		layout.putConstraint(SpringLayout.WEST, jlbCarId, 5, SpringLayout.WEST, jplMain);
		layout.putConstraint(SpringLayout.NORTH, jlbCarId, 5, SpringLayout.NORTH, jplMain);		
		layout.putConstraint(SpringLayout.EAST, jtfCarId, -5, SpringLayout.EAST, jplMain);
		layout.putConstraint(SpringLayout.NORTH, jtfCarId, 5, SpringLayout.NORTH, jplMain);
		layout.putConstraint(SpringLayout.WEST, jlbReqWash, 5, SpringLayout.WEST, jplMain);
		layout.putConstraint(SpringLayout.NORTH, jlbReqWash, 35, SpringLayout.NORTH, jplMain);
		layout.putConstraint(SpringLayout.EAST, jrbYesReqWash, -75, SpringLayout.EAST, jplMain);
		layout.putConstraint(SpringLayout.NORTH, jrbYesReqWash, 33, SpringLayout.NORTH, jplMain);
		layout.putConstraint(SpringLayout.NORTH, jrbNoReqWash, 33, SpringLayout.NORTH, jplMain);
		layout.putConstraint(SpringLayout.EAST, jrbNoReqWash, -5, SpringLayout.EAST, jplMain);
		layout.putConstraint(SpringLayout.WEST, jlbReqFuel, 5, SpringLayout.WEST, jplMain);
		layout.putConstraint(SpringLayout.NORTH, jlbReqFuel, 65, SpringLayout.NORTH, jplMain);		
		layout.putConstraint(SpringLayout.EAST, jtfReqFuel, -5, SpringLayout.EAST, jplMain);
		layout.putConstraint(SpringLayout.NORTH, jtfReqFuel, 65, SpringLayout.NORTH, jplMain);
		layout.putConstraint(SpringLayout.EAST, jbnAddCar, -5, SpringLayout.EAST, jplMain);
		layout.putConstraint(SpringLayout.SOUTH, jbnAddCar, -5, SpringLayout.SOUTH, jplMain);

		layout.putConstraint(SpringLayout.SOUTH, jlbServerIp, -5, SpringLayout.SOUTH, jplMain);
		layout.putConstraint(SpringLayout.SOUTH, jtfServerIp, -5, SpringLayout.SOUTH, jplMain);
		layout.putConstraint(SpringLayout.SOUTH, jbnConnect, -5, SpringLayout.SOUTH, jplMain);
		layout.putConstraint(SpringLayout.WEST, jtfServerIp, 0, SpringLayout.EAST, jlbServerIp);
		layout.putConstraint(SpringLayout.WEST, jbnConnect, 0, SpringLayout.EAST, jtfServerIp);
		
		this.add(jplMain);
		this.setSize(400, 200);
		this.setVisible(true);
		jbnConnect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				connectToServer();
				
			}
		});
		jbnAddCar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addCarActionListener();	
				clearAddCarForm();
			}
		});
		
	}
	
	private void clearAddCarForm() {
		jtfCarId.setText("");
		jtfReqFuel.setText("0");
		jrbYesReqWash.setSelected(true);
	}
	
	private void connectToServer() {
		
		String serverIp;
		try{
			serverIp = jtfServerIp.getText();
			clientController= new ClientController(serverIp, PORT_NUMBER);
		}catch(Exception e){
			//TODO: invalid input;
			System.out.println(e);
		}
		
	}	
	
	private void addCarActionListener() {
		int carId;
		boolean requiredWash;
		boolean requiredFuel;
		int fuelAmount=0;
		
		try{
			carId = Integer.parseInt(jtfCarId.getText());
			requiredWash = jrbYesReqWash.isSelected();
			requiredFuel = Integer.parseInt(jtfReqFuel.getText()) == 0 ? false : true ;
			fuelAmount = Integer.parseInt(jtfReqFuel.getText());
			
			clientController.addCar(carId, requiredFuel, fuelAmount, requiredWash);
			
			
			
		}catch (Exception e){
			//TODO:invalid input;
			System.out.println(e);
		}		
	}
}