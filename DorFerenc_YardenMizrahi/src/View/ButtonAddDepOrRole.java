package View;

import MVC.UIEventsListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ButtonAddDepOrRole extends ButtonExample{
	
	public ButtonAddDepOrRole(String nDR, Button btnNext, VBox vbRootAction, Label lblErrors, Label lblActionTitle,  UIEventsListener myListener, Label lblOpSuccessful, Label lblDisplayDepartments, Label lblDisplayRoles) {
		super(btnNext, vbRootAction, lblErrors, lblActionTitle, myListener, lblOpSuccessful, lblDisplayDepartments, lblDisplayRoles);
		//update the errors and the action screen to null
		btnNext.setDisable(false);
		vbRootAction.getChildren().clear();
		lblErrors.setText("");
		lblActionTitle.setText("Add " + nDR);
		//Name
		Label lblEnterName = new Label("Enter Name:");
		TextField tfName = new TextField();
		HBox hbName = new HBox();
		hbName.setSpacing(5);
		hbName.getChildren().addAll(lblEnterName, tfName);

		//Dynamic or Static
		HBox hbWorkType = new HBox();
		hbWorkType.setSpacing(20);
		Label lblWorkType = new Label("What type of " + nDR + "?");
		RadioButton rbDynamic = new RadioButton("Dynamic");
		rbDynamic.setUserData(true); //set data to true --is changeable
		RadioButton rbSync = new RadioButton("Synchronized");
		rbSync.setUserData(false); //set data to false -- not changeable
		rbSync.setSelected(true);
		ToggleGroup tgWorkType = new ToggleGroup();
		rbDynamic.setToggleGroup(tgWorkType);
		rbSync.setToggleGroup(tgWorkType);
		hbWorkType.getChildren().addAll(lblWorkType, rbDynamic, rbSync);
		hbWorkType.setDisable(true);

		//Changable
		HBox hbWorkTypeBig = new HBox();
		hbWorkTypeBig.setSpacing(20);
		Label lblWorkTypeBig = new Label("Can " + nDR + " working hours change?");
		CheckBox cbChangable = new CheckBox("Changable");
		hbWorkTypeBig.getChildren().addAll(lblWorkTypeBig, cbChangable);

		cbChangable.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				if (cbChangable.isSelected()) 
					hbWorkType.setDisable(false);
				else {
					rbSync.setSelected(true);
					hbWorkType.setDisable(true);
				}
			}
		});

		//Put Info into vrAction
		vbRootAction.getChildren().addAll(lblActionTitle, hbName, hbWorkTypeBig, hbWorkType, lblErrors, btnNext);

		btnNext.setOnAction(new EventHandler<ActionEvent>() { //Next clicked
			@Override
			public void handle(ActionEvent arg0) {
				try {
					boolean flag = false;
					if (cbChangable.isSelected())
						flag = true;
					if (nDR.equals("Role"))
						myListener.addRoleToModel(tfName.getText(), flag, (boolean)tgWorkType.getSelectedToggle().getUserData());
					if (nDR.equals("Department"))
						myListener.addDepartmentToModel(tfName.getText(), flag, (boolean)tgWorkType.getSelectedToggle().getUserData());
					vbRootAction.getChildren().clear();
					vbRootAction.getChildren().addAll(lblOpSuccessful);						}
				catch (Exception e) {lblErrors.setText(e.getMessage());}
			}
		});
	}
}
