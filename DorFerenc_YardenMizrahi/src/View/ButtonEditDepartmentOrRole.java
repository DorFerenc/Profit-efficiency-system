package View;

import java.time.LocalTime;

import javax.swing.JOptionPane;

import Classes.Department;
import Classes.Role;
import MVC.UIEventsListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ButtonEditDepartmentOrRole  extends ButtonExample {
	
	private Role tempRole;
	private Department tempDep;
	private ScrollPane spDisplay;
	private Label lblDisplayTempData;
	
	public ButtonEditDepartmentOrRole(Button btnNext, VBox vbRootAction, Label lblErrors, Label lblActionTitle,
			UIEventsListener myListener, Label lblOpSuccessful, Label lblDisplayDepartments, Label lblDisplayRoles, 
			ScrollPane spDisplay2, Label lblDisplayTempData, String nDorR) {
		super(btnNext, vbRootAction, lblErrors, lblActionTitle, myListener, lblOpSuccessful, lblDisplayDepartments,
				lblDisplayRoles);

		this.spDisplay = spDisplay2;
		this.lblDisplayTempData = lblDisplayTempData;

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
		lblErrors.setText("Please choose a " + nDorR + " to experiment with");
		lblActionTitle.setText("Edit " + nDorR);

		//Role names combo box
		Label lblRole = new Label(nDorR + ":       ");
		ComboBox<String> cmbRoleOrDep = new ComboBox<String>();;
		if (nDorR.equals("Role")) {
			cmbRoleOrDep.getItems().addAll(FXCollections.observableArrayList(myListener.viewAsksForAllRoleNames()));
		}
		else { //department
			cmbRoleOrDep.getItems().addAll(FXCollections.observableArrayList(myListener.viewAsksForAllDepartmentNames()));
		}

		if (lblDisplayRoles.getText().equals(""))
			lblErrors.setText("Please add at least one " + nDorR);

		//Dynamic or Static
		Label lblWorkType = new Label("What type of " + nDorR + "?");
		RadioButton rbDynamic = new RadioButton("Dynamic");
		rbDynamic.setUserData(true); //set data to true --is changeable
		RadioButton rbSync = new RadioButton("Synchronized");
		rbSync.setUserData(false); //set data to false -- not changeable
		ToggleGroup tgWorkType = new ToggleGroup();
		rbDynamic.setToggleGroup(tgWorkType);
		rbSync.setToggleGroup(tgWorkType);
		rbDynamic.setDisable(true);
		rbSync.setDisable(true);

		//Changable
		Label lblWorkTypeBig = new Label("Can " + nDorR + " hours be changed?");
		CheckBox cbChangable = new CheckBox("Changable");
		cbChangable.setDisable(true);

		//start hour picker
		Label lblStartHour = new Label("Start Hour: ");
		ComboBox<LocalTime> cmbStart = new ComboBox<LocalTime>();
		for (int i = 0; i <= 15; i ++)
			cmbStart.getItems().add(LocalTime.of(i, 0));
		cmbStart.getSelectionModel().select(LocalTime.of(8, 0));
		cmbStart.setDisable(true);

		Label lblEndHour = new Label("End Hour: ");
		Label lblEndHourInfo = new Label("" + cmbStart.getValue().plusHours(9));

		//put Info Into grid
		GridPane gpEditRole = new GridPane();
		//gpEmploye.setPadding(new Insets(25));
		gpEditRole.setAlignment(Pos.BASELINE_LEFT);
		gpEditRole.setGridLinesVisible(false);
		gpEditRole.setHgap(25);
		gpEditRole.setVgap(15);
		gpEditRole.add(lblRole, 0, 0);
		gpEditRole.add(cmbRoleOrDep, 1, 0);
		gpEditRole.add(lblWorkTypeBig, 0, 1);
		gpEditRole.add(cbChangable, 1, 1);
		gpEditRole.add(lblWorkType, 0, 2);
		gpEditRole.add(rbDynamic, 0, 3);
		gpEditRole.add(rbSync, 1, 3);
		gpEditRole.add(lblStartHour, 0, 4);
		gpEditRole.add(cmbStart, 1, 4);
		gpEditRole.add(lblEndHour, 0, 5);
		gpEditRole.add(lblEndHourInfo, 1, 5);

		vbRootAction.getChildren().addAll(lblActionTitle, gpEditRole, lblErrors, btnCheck, btnRemove, btnNext);

		cmbRoleOrDep.setOnAction(new EventHandler<ActionEvent>() {
			//when a role is chosen
			//present the current choices 
			public void handle(ActionEvent arg0) {
				try {
					if (nDorR.equals("Role")) {
						tempRole = (Role)((myListener.viewAsksForRoleByName(cmbRoleOrDep.getValue())).clone());
						cmbStart.getSelectionModel().select(tempRole.getStartHour());
						lblDisplayTempData.setText(tempRole.calcEfficiency(cmbStart.getValue()));
						if (tempRole.isChangeable())
							cbChangable.setSelected(true);
						else
							cbChangable.setSelected(false);
						if (tempRole.isDynamic())
							rbDynamic.setSelected(true);
						else
							rbSync.setSelected(true);
						//cmbStart.setDisable(false);
					}
					else
					{
						tempDep = (Department)((myListener.viewAsksForDepartmentByName(cmbRoleOrDep.getValue())).clone());
						cmbStart.getSelectionModel().select(tempDep.getStartHour());
						lblDisplayTempData.setText(tempDep.calcEfficiency(cmbStart.getValue()));
						if (tempDep.isChangeable())
							cbChangable.setSelected(true);
						else
							cbChangable.setSelected(false);
						if (tempDep.isDynamic())
							rbDynamic.setSelected(true);
						else
							rbSync.setSelected(true);
						//cmbStart.setDisable(false);
					}
					spDisplay.setContent(lblDisplayTempData);
					lblErrors.setText("");
					cbChangable.setDisable(false);
					btnNext.setDisable(true);
					btnCheck.setDisable(false);
					btnRemove.setDisable(false);

				}
				catch (Exception e) {lblErrors.setText(e.getMessage());}
			}
		});

		cbChangable.selectedProperty().addListener(new ChangeListener<Boolean>() {
			//if changable then show option to expiriment with dynamic and sync
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				btnNext.setDisable(true);
				btnCheck.setDisable(false);
				if (cbChangable.isSelected()) {
					if (nDorR.equals("Role")) 
						tempRole.setChangeable(true);
					else
						tempDep.setChangeable(true);
					rbDynamic.setDisable(false);
					rbSync.setDisable(false);
					cmbStart.setDisable(false);
				}
				else {
					if (nDorR.equals("Role")) {
						tempRole.setChangeable(false);
						tempRole.setDynamic(false);
					}
					else {
						tempDep.setChangeable(false);
						tempDep.setDynamic(false);
					}
					cmbStart.getSelectionModel().select(LocalTime.of(8, 0));
					cmbStart.setDisable(true);
					rbSync.setSelected(true);
					rbDynamic.setDisable(true);
					rbSync.setDisable(true);
				}
			}
		});

		//when changed block next option until check
		rbDynamic.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				btnNext.setDisable(true);
				//btnCheck.setDisable(false);
			}
		});
		rbSync.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				btnNext.setDisable(true);
				//btnCheck.setDisable(false);
			}
		});

		cmbStart.setOnAction(new EventHandler<ActionEvent>() {
			//changes in the hour comboBox
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (nDorR.equals("Role")) 
					tempRole.setStartHour(cmbStart.getValue());
				else
					tempDep.setStartHour(cmbStart.getValue());
				btnNext.setDisable(true);
				//btnCheck.setDisable(false);
				lblEndHourInfo.setText("" + cmbStart.getValue().plusHours(9));
			}
		});

		btnCheck.setOnAction(new EventHandler<ActionEvent>() { //Next clicked
			@Override
			public void handle(ActionEvent arg0) {
				lblErrors.setText("");
				try {
//					System.out.println(tempRole.getRoleName() + "\n" + tempRole.isChangeable());
//					System.out.println(tempRole.isDynamic() + "\n" + tempRole.getStartHour());
					btnNext.setDisable(false);
					//btnCheck.setDisable(true);
					if (nDorR.equals("Role")) {
						tempRole.setDynamic((boolean)tgWorkType.getSelectedToggle().getUserData());
						lblDisplayTempData.setText(tempRole.calcEfficiency(cmbStart.getValue()));
					}
					else {
						tempDep.setDynamic((boolean)tgWorkType.getSelectedToggle().getUserData());
						lblDisplayTempData.setText(tempDep.calcEfficiency(cmbStart.getValue()));
					}
					spDisplay.setContent(lblDisplayTempData);
//					spDisplay.con
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
				if (JOptionPane.showConfirmDialog(null, "You are permanently deleting " + tempRole.getRoleName() + "\nare you sure you want to proceed?", "Remove Role",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					//check if sure they want to delete
					try {
						if (nDorR.equals("Role")) 
							myListener.removeFromUI(tempRole);
						else
							myListener.removeFromUI(tempDep);
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
				if (lblErrors.getText().equals("")) {
					try {
						if (nDorR.equals("Role")) { 
							myListener.updateRoleFromUI(tempRole.getRoleName(), tempRole.isChangeable(), tempRole.isDynamic(), tempRole.getStartHour());
						}
						else {
							myListener.updateDepartmentFromUI(tempDep.getDepName(), tempDep.isChangeable(), tempDep.isDynamic(), tempDep.getStartHour());
						}
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
