package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class ClientGUI extends JFrame{
	JPanel jplMain;
	JLabel jlbCarId, jlbReqWash, jlbReqFuel;
	JRadioButton jrbYesReqWash, jrbNoReqWash;
	JTextField jtfCarId, jtfReqFuel;
	JButton jbnAddCar;
	SpringLayout layout;
	ButtonGroup groupReqWash;
	
	public ClientGUI(){
		layout = new SpringLayout();
		jplMain = new JPanel();
		jplMain.setLayout(layout);
		
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
		jbnAddCar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addCarActionListener();				
			}
		});
		
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
		
		this.add(jplMain);
		this.setSize(400, 200);
		this.setVisible(true);
		
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
			
			//TODO:add car to gas station 
		}catch (Exception e){
			//TODO:invalid input;
			System.out.println(e);
		}		
	}
}
