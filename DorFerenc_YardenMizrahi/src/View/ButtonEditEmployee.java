package View;

import java.time.LocalTime;

import javax.swing.JOptionPane;

import Emloyee.Employee;
import Emloyee.Employee.employeePreference;
import Emloyee.Employee.employeeType;
import MVC.UIEventsListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ButtonEditEmployee extends ButtonExample{

	public Label lblDisplayEmployees;
	public Label lblDisplayTempData;
	public Employee tempEmp;
	
	public ButtonEditEmployee(Button btnNext, VBox vbRootAction, Label lblErrors, Label lblActionTitle,
			UIEventsListener myListener, Label lblOpSuccessful, Label lblDisplayDepartments, Label lblDisplayRoles,
			Label lblDisplayEmployees, Label lblDisplayTempData, ScrollPane spDisplay) {
		super(btnNext, vbRootAction, lblErrors, lblActionTitle, myListener, lblOpSuccessful, lblDisplayDepartments,
				lblDisplayRoles);
		
		this.lblDisplayEmployees = lblDisplayEmployees;
		this.lblDisplayTempData = lblDisplayTempData;
		
		//update the errors and the action screen to null
		Button btnCheck = new Button("Check");
		btnCheck.setTextFill(Color.GREEN);
		btnCheck.setWrapText(true);
		btnCheck.setDisable(true);

		Button btnRemove = new Button("Remove");
		btnRemove.setTextFill(Color.RED);
		btnRemove.setWrapText(true);
		btnRemove.setDisable(true);

		btnNext.setDisable(true);
		vbRootAction.getChildren().clear();
		lblErrors.setText("");
		lblActionTitle.setText("Edit Employee");
		lblErrors.setText("Please select an Employee to change his/her data");


		//Employee ID's combo box
		Label lblID = new Label("Select ID:       ");
		ComboBox<String> cmbID = new ComboBox<String>(FXCollections.observableArrayList(myListener.viewAsksForAllEmployeeIDNames()));

		if (lblDisplayEmployees.getText().equals(""))
			lblErrors.setText("Please add at least one Employee");

		//change area
		Label lblChange = new Label("Change:");

		//Role names combo box
		Label lblRole = new Label("Role:       ");
		ComboBox<String> cmbRole = new ComboBox<String>(FXCollections.observableArrayList(myListener.viewAsksForAllRoleNames()));
		cmbRole.getSelectionModel().select(0);
		cmbRole.setDisable(true);

		//Department names combo box
		Label lblDep = new Label("Department: ");
		ComboBox<String> cmbDep = new ComboBox<String>(FXCollections.observableArrayList(myListener.viewAsksForAllDepartmentNames()));
		cmbDep.getSelectionModel().select(0);
		cmbDep.setDisable(true);

		//Preferences combo box
		Label lblPref = new Label("Preference: ");
		ComboBox<employeePreference> cmbPref = new ComboBox<employeePreference>();
		cmbPref.getItems().setAll(employeePreference.values());
		cmbPref.getSelectionModel().select(2);
		cmbPref.setDisable(true);

		//start hour picker
		Label lblStartHour = new Label("Starting Hour: ");
		ComboBox<LocalTime> cmbStart = new ComboBox<LocalTime>();
		for (int i = 0; i <= 15; i ++)
			cmbStart.getItems().add(LocalTime.of(i, 0));
		//cmbStart.getSelectionModel().select(LocalTime.of(8, 0));

		CheckBox cbHome = new CheckBox("Home");
		cbHome.setAlignment(Pos.CENTER_RIGHT);

		HBox hbStartPref = new HBox();
		hbStartPref.getChildren().addAll(cmbStart, cbHome);
		hbStartPref.setSpacing(70);
		hbStartPref.setDisable(true);
		
		//Work Type combo box
		Label lblType = new Label("Salary Type: ");
		ComboBox<employeeType> cmbType = new ComboBox<employeeType>();
		cmbType.getItems().setAll(employeeType.values());
		cmbType.getSelectionModel().select(1);
		cmbType.setDisable(true);

		//Salary

		//put Info Into grid
		GridPane gpEmploye = new GridPane();
		//gpEmploye.setPadding(new Insets(25));
		gpEmploye.setAlignment(Pos.BASELINE_LEFT);
		gpEmploye.setGridLinesVisible(false);
		gpEmploye.setHgap(25);
		gpEmploye.setVgap(15);
		gpEmploye.add(lblID, 0, 0);
		gpEmploye.add(cmbID, 1, 0);
		gpEmploye.add(lblChange, 0, 1);
		gpEmploye.add(lblRole, 0, 2);
		gpEmploye.add(cmbRole, 1, 2);
		gpEmploye.add(lblDep, 0, 3);
		gpEmploye.add(cmbDep, 1, 3);
		gpEmploye.add(lblStartHour, 0, 4);
		gpEmploye.add(hbStartPref, 1, 4);
		gpEmploye.add(lblPref, 0, 5);
		gpEmploye.add(cmbPref, 1, 5);
		gpEmploye.add(lblType, 0, 6);
		gpEmploye.add(cmbType, 1, 6);

		if (lblDisplayRoles.getText().equals("") || lblDisplayDepartments.getText().equals("")) {
			lblErrors.setText("Please add at least one Role and one Department to continue");
			gpEmploye.setDisable(true);
			btnNext.setDisable(true);
		}
		//Put Info into vrAction
		vbRootAction.getChildren().addAll(lblActionTitle, gpEmploye, lblErrors, btnCheck, btnRemove, btnNext);

		cmbID.setOnAction(new EventHandler<ActionEvent>() {
			//when a Employee is chosen
			//present the current choices 
			public void handle(ActionEvent arg0) {
				try {
					//get the employee by the first 9 chars representing the id
					tempEmp = (Employee)((myListener.viewAsksForEmployeeByID(cmbID.getValue().substring(0, 9))).clone());
					//System.out.println("1 = " + tempEmp.getEmployeePreference());
					lblDisplayTempData.setText(tempEmp.toString());
					//set his data to current selected and show him
					//System.out.println(tempEmp.getName());
					//System.out.println(tempEmp.getEmployeePreference());
					if (tempEmp.getEmployeePreference().equals(Employee.employeePreference.HOME)) {
						cbHome.setSelected(true);
						cmbStart.getSelectionModel().select(LocalTime.of(8, 0));
						cmbStart.setDisable(true);
						cmbPref.getSelectionModel().select(3); //home
						//System.out.println("yes");
					}
					else {
						cmbStart.getSelectionModel().select(tempEmp.getPrefStartHour());
						cmbPref.getSelectionModel().select(tempEmp.getEmployeePreference());
						cmbStart.setDisable(false);
						cbHome.setSelected(false);
						//System.out.println("no");
					}
					cmbRole.getSelectionModel().select(tempEmp.getMyRole().getRoleName());
					cmbDep.getSelectionModel().select(tempEmp.getMyDepartment().getDepName());
					cmbPref.getSelectionModel().select(tempEmp.getEmployeePreference());
					cmbType.getSelectionModel().select(tempEmp.getWorkType());
					cmbStart.getSelectionModel().select(tempEmp.getPrefStartHour());

					
					spDisplay.setContent(lblDisplayTempData);
					lblErrors.setText("");
					btnCheck.setDisable(false);
					btnRemove.setDisable(false);
					cmbRole.setDisable(false);
					cmbDep.setDisable(false);
					//cmbPref.setDisable(false);
					cmbType.setDisable(false);
					hbStartPref.setDisable(false);
				}
				catch (Exception e) {lblErrors.setText(e.getMessage());}
			}
		});
		

		cmbRole.setOnAction(new EventHandler<ActionEvent>() {
			//changes Employee myRole from the role chosen in comboBox to cloned employee
			public void handle(ActionEvent arg0) {
				try {
					tempEmp.setMyRole(myListener.viewAsksForRoleByName(cmbRole.getValue()));
					btnNext.setDisable(true);
				} catch (Exception e) {lblErrors.setText(e.getMessage());}
			}
		});

		cmbDep.setOnAction(new EventHandler<ActionEvent>() {
			//changes Employee myDepartment from the department chosen in comboBox to cloned employee
			public void handle(ActionEvent arg0) {
				try {
					tempEmp.setMyDepartment(myListener.viewAsksForDepartmentByName(cmbDep.getValue()));
					btnNext.setDisable(true);
				} catch (Exception e) {lblErrors.setText(e.getMessage());}
			}
		});

		cmbPref.setOnAction(new EventHandler<ActionEvent>() {
			//changes Employee preference from the chosen one in comboBox to cloned employee
			public void handle(ActionEvent arg0) {
				try {
					tempEmp.setEmployeePreference(cmbPref.getValue());
					btnNext.setDisable(true);
				} catch (Exception e) {lblErrors.setText(e.getMessage());}
			}
		});

		cmbType.setOnAction(new EventHandler<ActionEvent>() {
			//just shows the button don't change type from here 
			public void handle(ActionEvent arg0) {
				btnNext.setDisable(true);
			}
		});
		
		cbHome.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				if(cbHome.isSelected()) {
					cmbStart.getSelectionModel().select(LocalTime.of(8, 0));
					cmbStart.setDisable(true);
					cmbPref.getSelectionModel().select(3); //home
				}
				else {
					cmbStart.setDisable(false);
					cmbPref.getSelectionModel().select(2); //normal
				}
			}
		});	
		
		cmbStart.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				tempEmp.setPrefStartHour(cmbStart.getValue());
				if(cmbStart.getValue().isBefore(LocalTime.of(8, 0)))
					cmbPref.getSelectionModel().select(0); //earlier
				if(cmbStart.getValue().equals(LocalTime.of(8, 0)))
					cmbPref.getSelectionModel().select(2); //normal
				if(cmbStart.getValue().isAfter(LocalTime.of(8, 0)))
					cmbPref.getSelectionModel().select(1); //later
			}
		});
		
		btnCheck.setOnAction(new EventHandler<ActionEvent>() { //Next clicked
			@Override
			public void handle(ActionEvent arg0) {
				lblErrors.setText("");
				try {
					//System.out.println(tempEmp.getPrefStartHour());
					btnNext.setDisable(false);
					lblDisplayTempData.setText(tempEmp.toString());
					if (!(tempEmp.getWorkType().equals(cmbType.getValue()))) { //if employee work type has been changed
						String msg = tempEmp.toString();
						int index = msg.indexOf("Salary Type:"); 
						msg = msg.substring(0, index);
						msg += "Salary Type:    " + cmbType.getValue() + "\n";
						lblDisplayTempData.setText(msg);
					}
					spDisplay.setContent(lblDisplayTempData);
				} 
				catch (Exception e) {lblErrors.setText(e.getMessage());}
				if (!lblErrors.getText().equals("")) {
					btnNext.setDisable(true);
					btnCheck.setDisable(false);
				}
			}
		});

		btnRemove.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {//Permanently deleting are you sure you want to proceed?
				if (JOptionPane.showConfirmDialog(null, "You are permanently deleting " + tempEmp.getId() + "\nare you sure you want to proceed?", "Remove Employee",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					//check if sure they want to delete
					try {
						myListener.removeFromUI(tempEmp);
						lblDisplayTempData.setText("");
						vbRootAction.getChildren().clear();
						vbRootAction.getChildren().addAll(lblOpSuccessful);
					} catch (Exception e) {
						lblErrors.setText(e.getMessage());
					}
				}
			}
		});

		btnNext.setOnAction(new EventHandler<ActionEvent>() { //Next clicked
			public void handle(ActionEvent arg0) {
				//System.out.println(tempEmp.getPrefStartHour());
				if (lblErrors.getText().equals("")) {
					try {
						myListener.updateEmployeeFromUI(tempEmp.getId(), tempEmp.getEmployeePreference(), tempEmp.getMyRole().getRoleName(), tempEmp.getMyDepartment().getDepName(), cmbType.getValue(), tempEmp.getPrefStartHour());
						vbRootAction.getChildren().clear();
						vbRootAction.getChildren().addAll(lblOpSuccessful);
					}
					catch (Exception e) {lblErrors.setText(e.getMessage());}
				}
				else
					lblErrors.setText("Please make sure info is valid");
			}
		});
	}

}
