package View;

import MVC.UIEventsListener;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ButtonExample {
	public Button btnNext;
	public VBox vbRootAction;
	public Label lblErrors;
	public Label lblActionTitle;
	public UIEventsListener myListener;
	
	public ButtonExample(Button btnNext, VBox vbRootAction, Label lblErrors, Label lblActionTitle,  UIEventsListener myListener, Label lblOpSuccessful, Label lblDisplayDepartments, Label lblDisplayRoles) {
		this.btnNext = btnNext;
		this.vbRootAction = vbRootAction;
		this.lblActionTitle = lblActionTitle;
		this.lblErrors = lblErrors;
		this.myListener = myListener;
	}
}
