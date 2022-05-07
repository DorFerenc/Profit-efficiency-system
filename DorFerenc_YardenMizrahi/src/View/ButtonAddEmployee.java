package View;

import java.time.LocalDate;
import java.time.LocalTime;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ButtonAddEmployee extends ButtonExample{

	public ButtonAddEmployee(Button btnNext, VBox vbRootAction, Label lblErrors, Label lblActionTitle,  UIEventsListener myListener, Label lblOpSuccessful, Label lblDisplayDepartments, Label lblDisplayRoles) {
		super(btnNext, vbRootAction, lblErrors, lblActionTitle, myListener, lblOpSuccessful, lblDisplayDepartments,
				lblDisplayRoles);	
		
		//----------------------------Add Employee-------------------------------
		btnNext.setDisable(false);
		vbRootAction.getChildren().clear();
		lblErrors.setText("");
		lblActionTitle.setText("Add Employee");

		//ID
		Label lblEnterID = new Label("Enter ID:");
		TextField tfID = new TextField();

		//Name
		Label lblEnterName = new Label("Enter Name:");
		TextField tfName = new TextField();

		//Date
		Label lblBirthDate = new Label("BirthDate:");
		DatePicker dBirthDate = new DatePicker();
		dBirthDate.setValue(LocalDate.of(2000, 2, 2)); //initial age

		//Role names combo box
		Label lblRole = new Label("Role:       ");
		ComboBox<String> cmbRole = new ComboBox<String>(FXCollections.observableArrayList(myListener.viewAsksForAllRoleNames()));
		cmbRole.getSelectionModel().select(0);

		//Department names combo box
		Label lblDep = new Label("Department: ");
		ComboBox<String> cmbDep = new ComboBox<String>(FXCollections.observableArrayList(myListener.viewAsksForAllDepartmentNames()));
		cmbDep.getSelectionModel().select(0);

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
		cmbStart.getSelectionModel().select(LocalTime.of(8, 0));

		cmbStart.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				if(cmbStart.getValue().isBefore(LocalTime.of(8, 0)))
					cmbPref.getSelectionModel().select(0); //earlier
				if(cmbStart.getValue().equals(LocalTime.of(8, 0)))
					cmbPref.getSelectionModel().select(2); //normal
				if(cmbStart.getValue().isAfter(LocalTime.of(8, 0)))
					cmbPref.getSelectionModel().select(1); //later
			}
		});
		CheckBox cbHome = new CheckBox("Home");
		cbHome.setAlignment(Pos.CENTER_RIGHT);
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

		HBox hbStartPref = new HBox();
		hbStartPref.getChildren().addAll(cmbStart, cbHome);
		hbStartPref.setSpacing(70);

		//Work Type combo box
		Label lblType = new Label("Salary Type: ");
		ComboBox<employeeType> cmbType = new ComboBox<employeeType>();
		cmbType.getItems().setAll(employeeType.values());
		cmbType.getSelectionModel().select(1);

		//Salary

		//put Info Into grid
		GridPane gpEmploye = new GridPane();
		//gpEmploye.setPadding(new Insets(25));
		gpEmploye.setAlignment(Pos.BASELINE_LEFT);
		gpEmploye.setGridLinesVisible(false);
		gpEmploye.setHgap(25);
		gpEmploye.setVgap(15);
		gpEmploye.add(lblEnterID, 0, 0);
		gpEmploye.add(tfID, 1, 0);
		gpEmploye.add(lblEnterName, 0, 1);
		gpEmploye.add(tfName, 1, 1);
		gpEmploye.add(lblBirthDate, 0, 2);
		gpEmploye.add(dBirthDate, 1, 2);
		gpEmploye.add(lblRole, 0, 3);
		gpEmploye.add(cmbRole, 1, 3);
		gpEmploye.add(lblDep, 0, 4);
		gpEmploye.add(cmbDep, 1, 4);
		gpEmploye.add(lblStartHour, 0, 5);
		gpEmploye.add(hbStartPref, 1, 5);
		gpEmploye.add(lblPref, 0, 6);
		gpEmploye.add(cmbPref, 1, 6);
		gpEmploye.add(lblType, 0, 7);
		gpEmploye.add(cmbType, 1, 7);

		if (lblDisplayRoles.getText().equals("") || lblDisplayDepartments.getText().equals("")) {
			lblErrors.setText("Please add at least one Role and one Department to continue");
			gpEmploye.setDisable(true);
			btnNext.setDisable(true);
		}
		//Put Info into vrAction
		vbRootAction.getChildren().addAll(lblActionTitle, gpEmploye, lblErrors, btnNext);
		btnNext.setOnAction(new EventHandler<ActionEvent>() { //Next clicked
			@Override
			public void handle(ActionEvent arg0) {
				try {
					myListener.addEmployeeToModel(cmbType.getValue(), tfName.getText(), tfID.getText(), dBirthDate.getValue(), cmbPref.getValue(), cmbRole.getValue(), cmbDep.getValue(), cmbStart.getValue());
					vbRootAction.getChildren().clear();
					vbRootAction.getChildren().addAll(lblOpSuccessful);
				}
				catch (Exception e) {lblErrors.setText(e.getMessage());}
			}
		});
	}
}
