package View;

import java.io.Serializable;
import java.time.LocalTime;

import javax.swing.JOptionPane;

import MVC.UIEventsListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CompanyView implements Serializable {

	private UIEventsListener myListener;
	private Label lblDisplayRoles = new Label();
	private Label lblDisplayDepartments = new Label();
	private Label lblDisplayEmployees = new Label();
	private Label lblDisplayTempData = new Label();
	private HBox hbbigBox = new HBox();

	public CompanyView(Stage theStage) {
		theStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				if (JOptionPane.showConfirmDialog(null, "You are closing without saving!\nDo you want to save from here?", "Close Without Saving",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					try {
						myListener.saveFromUItoModel();//Successfully   
						JOptionPane.showMessageDialog(null, "Saved Successfully","Saved",JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Can't save sorry","Error",JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});


		theStage.setTitle("Company Efficiency Experiment Tool");
		
		GridPane gpRoot = new GridPane();
		gpRoot.setPadding(new Insets(25));
		gpRoot.setAlignment(Pos.CENTER);
		gpRoot.setGridLinesVisible(false);
		gpRoot.setHgap(25);
		gpRoot.setVgap(15);
		gpRoot.setMinSize(1800, 600);

		Button btnExitAndSave = new Button("Save and Exit");
		Button btnGenMaxEfficiency = new Button("Generate Max Efficiency");
		btnGenMaxEfficiency.setMaxWidth(143);
		Label lblTitle = new Label("----- Company Efficiency Experiment  -----");
		lblTitle.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 14));
		Label lblSubTitle = new Label("(enter data into system and experiment with your options to see how they affect your company's efficiency)");

		Button btnNext = new Button("Next");
		btnNext.setTextFill(Color.BLUE);
		btnNext.setWrapText(true);
		Label lblErrors = new Label();
		lblErrors.setTextFill(Color.RED);
		lblErrors.setMinHeight(40);
		Label lblOpSuccessful = new Label();
		lblOpSuccessful.setText("Operation was successful");
		lblOpSuccessful.setTextFill(Color.BLUE);

		//action buttons
		Label lblActionScreen = new Label("Action Screen: ");
		Label lblActionTitle = new Label();
		lblActionTitle.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 14));
		VBox vbRootAction = new VBox();
		vbRootAction.setSpacing(10);
		vbRootAction.setPadding(new Insets(15));
		vbRootAction.setMinWidth(400);
		vbRootAction.setMaxWidth(400);

		Button btnAddRole = new Button("Add Role");
		Button btnAddDepartment = new Button("Add Department");
		Button btnAddEmployee = new Button("Add Employee");
		Button btnEditRole = new Button("Edit Role");
		Button btnEditDepartment = new Button("Edit Department");
		Button btnEditEmployee = new Button("Edit Employee");
//		btnAddRole.setMinWidth(143);
//		btnAddDepartment.setMinWidth(143);
//		btnAddEmployee.setMinWidth(143);
//		btnEditRole.setMinWidth(143);
//		btnEditDepartment.setMinWidth(143);
//		btnEditEmployee.setMinWidth(143);


		//----------------------------Display Section-------------------------------
		//----------------------------Display Section-------------------------------
		//----------------------------Display Section-------------------------------
		//display buttons
		Label lblDisplayScreen = new Label("Display Screen: ");
		VBox vbRootDisplay = new VBox();
		vbRootDisplay.setSpacing(10);
		vbRootDisplay.setPadding(new Insets(15));
		vbRootDisplay.setMinWidth(400);
		//vbRootDisplay.setMinHeight(410);

		ScrollPane spDisplay = new ScrollPane();
		spDisplay.setPadding(new Insets(15));
		spDisplay.setMinWidth(400);
		spDisplay.setMaxWidth(400);
		spDisplay.setMinHeight(410);
		spDisplay.setMaxHeight(400);
		spDisplay.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		spDisplay.setVbarPolicy(ScrollBarPolicy.ALWAYS);

		vbRootDisplay.getChildren().addAll(spDisplay);
		VBox.setVgrow(spDisplay, Priority.ALWAYS);

		Button btnDisplayAllRoles = new Button("Display Roles");
		Button btnDisplayAllDepartments = new Button("Display Departments");
		Button btnDisplayAllEmployees = new Button("Display Employees");
		Label lblDisEfficiency = new Label("Only efficiency data:");
		Button btnDisEffByRole = new Button("By Role");
		Button btnDisEffByDepartment = new Button("By Department");
		//		HBox hbEfficiencyByRole= new HBox();
		//		Label lblEfficiencyByRole = new Label("By Role:             ");
		//		ComboBox<Integer> cmbDisplayRoleEfficiency = new ComboBox<Integer>(); 
		//		hbEfficiencyByRole.getChildren().addAll(lblEfficiencyByRole, cmbDisplayRoleEfficiency);
		//		//cmbDisplayRoleEfficiency.getItems().setAll(generator());
		//		
		//		HBox hbEfficiencyByDepartmen= new HBox();
		//		Label lblEfficiencyByDepartment = new Label("By Department: ");
		//		ComboBox<Integer> cmbDisplayDepartmentEfficiency = new ComboBox<Integer>(); 
		//		hbEfficiencyByDepartmen.getChildren().addAll(lblEfficiencyByDepartment, cmbDisplayDepartmentEfficiency);
		//		//cmbDisplayRoleEfficiency.getItems().setAll(generator());

		Label lblNoData = new Label("No Data Yet - Try again after entering data");
		//display all roles
		btnDisplayAllRoles.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				spDisplay.setContent(lblDisplayRoles);
				if (lblDisplayRoles.getText().equals("")) //if still null
					spDisplay.setContent(lblNoData);
			}
		});

		//display all departments
		btnDisplayAllDepartments.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				spDisplay.setContent(lblDisplayDepartments);
				if (lblDisplayDepartments.getText().equals(""))//if still null
					spDisplay.setContent(lblNoData);
			}
		});

		//display all employees
		btnDisplayAllEmployees.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				spDisplay.setContent(lblDisplayEmployees);
				if (lblDisplayEmployees.getText().equals(""))//if still null
					spDisplay.setContent(lblNoData);
			}
		});

		//display Efficiency By Role
		btnDisEffByRole.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					lblDisplayTempData.setText(myListener.viewAsksForEfficiencyByRole());
					spDisplay.setContent(lblDisplayTempData);
					if (lblDisplayTempData.getText().equals(""))//if still null
						spDisplay.setContent(lblNoData);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Can't get data\nTry adding things first\n","Error",JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		//display Efficiency By Role
		btnDisEffByDepartment.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					lblDisplayTempData.setText(myListener.viewAsksForEfficiencyByDepartment());
					spDisplay.setContent(lblDisplayTempData);
					if (lblDisplayTempData.getText().equals(""))//if still null
						spDisplay.setContent(lblNoData);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Can't get data\nTry adding things first","Error",JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		//----------------------------Action Section-------------------------------
		//----------------------------Action Section-------------------------------
		//----------------------------Action Section-------------------------------

		//----------------------------Add Role-------------------------------
		btnAddRole.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				/* ButtonAddDepOrRole btnAddRoleGo = */new ButtonAddDepOrRole("Role", btnNext, vbRootAction, lblErrors, lblActionTitle, myListener, lblOpSuccessful, lblDisplayDepartments, lblDisplayRoles);
			}
		});
		//----------------------------Add Department-------------------------------
		btnAddDepartment.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				/* ButtonAddDepOrRole btnAddDepGo = */new ButtonAddDepOrRole("Department", btnNext, vbRootAction, lblErrors, lblActionTitle, myListener, lblOpSuccessful, lblDisplayDepartments, lblDisplayRoles);
			}
		});

		//----------------------------Add Employee-------------------------------
		btnAddEmployee.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				if (lblDisplayDepartments.getText().equals("") || lblDisplayRoles.getText().equals(""))
					JOptionPane.showMessageDialog(null, "Can't get data\nNeed At least 1 departmetn and 1 Role\n","Error",JOptionPane.WARNING_MESSAGE);
				else
					new ButtonAddEmployee(btnNext, vbRootAction, lblErrors, lblActionTitle, myListener, lblOpSuccessful, lblDisplayDepartments, lblDisplayRoles);
				/* ButtonAddEmployee btnAddEmpGo = */
			}
		});

		//----------------------------Edit Role-------------------------------
		btnEditRole.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (lblDisplayRoles.getText().equals(""))
					JOptionPane.showMessageDialog(null, "Can't get data\nTry adding 1 Role First\n","Error",JOptionPane.WARNING_MESSAGE);
				else
					new ButtonEditDepartmentOrRole(btnNext, vbRootAction, lblErrors, lblActionTitle, myListener, lblOpSuccessful, lblDisplayDepartments, lblDisplayRoles, spDisplay, lblDisplayTempData, "Role");
				/* ButtonEditDepartmentOrRole btnEditRole = */
			}
		});

		//----------------------------Edit Department-------------------------------
		btnEditDepartment.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (lblDisplayDepartments.getText().equals(""))
					JOptionPane.showMessageDialog(null, "Can't get data\nTry adding one department\n","Error",JOptionPane.WARNING_MESSAGE);
				else
					new ButtonEditDepartmentOrRole(btnNext, vbRootAction, lblErrors, lblActionTitle, myListener, lblOpSuccessful, lblDisplayDepartments, lblDisplayRoles, spDisplay, lblDisplayTempData, "Department");
				/* ButtonEditDepartmentOrRole btnEditDep = */
			}
		});

		//----------------------------Edit Employee-------------------------------
		btnEditEmployee.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				if (lblDisplayEmployees.getText().equals(""))
					JOptionPane.showMessageDialog(null, "Can't get data\nTry adding Employees first\n","Error",JOptionPane.WARNING_MESSAGE);
				else
					new ButtonEditEmployee(btnNext, vbRootAction, lblErrors, lblActionTitle, myListener, lblOpSuccessful, lblDisplayDepartments, lblDisplayRoles, lblDisplayEmployees, lblDisplayTempData, spDisplay);
				/* ButtonEditEmployee btnEditEmp = */
			}
		});
		
		//----------------------------Generate Max Efficiency-------------------------------
		btnGenMaxEfficiency.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				//update the errors and the action screen to null
				Button btnCheck = new Button("Check");
				btnCheck.setTextFill(Color.GREEN);
				btnCheck.setWrapText(true);
				btnCheck.setDisable(true);

				btnNext.setDisable(true);
				vbRootAction.getChildren().clear();
				lblErrors.setText("Please Select First Minimum starting hour");
				lblActionTitle.setText("Generate Max Efficiency");


				//min start hour picker
				Label lblMinStartHour = new Label("Minimum company Starting Hour: ");
				ComboBox<LocalTime> cmbMinStart = new ComboBox<LocalTime>();
				for (int i = 0; i <= 15; i ++)
					cmbMinStart.getItems().add(LocalTime.of(i, 0));
				cmbMinStart.getSelectionModel().select(LocalTime.of(8, 0));

				Label lblEndHour = new Label("Minimum company End Hour: ");
				Label lblEndHourInfo = new Label("" + cmbMinStart.getValue().plusHours(9));
				
				//max start hour picker
				Label lblMaxStartHour = new Label("Maximum company Starting Hour: ");
				ComboBox<LocalTime> cmbMaxStart = new ComboBox<LocalTime>();
//				for (int i = 0; i <= 15; i ++)
//					cmbMaxStart.getItems().add(LocalTime.of(i, 0));
				cmbMaxStart.setDisable(true);

				Label lblEndHour2 = new Label("Maximum company End Hour: ");
				Label lblEndHourInfo2 = new Label();//"" + cmbMaxStart.getValue().plusHours(9));

				//put Info Into grid
				GridPane gpEditDep = new GridPane();
				//gpEmploye.setPadding(new Insets(25));
				gpEditDep.setAlignment(Pos.BASELINE_LEFT);
				gpEditDep.setGridLinesVisible(false);
				gpEditDep.setHgap(25);
				gpEditDep.setVgap(15);
				gpEditDep.add(lblMinStartHour, 0, 0);
				gpEditDep.add(cmbMinStart, 1, 0);
				gpEditDep.add(lblEndHour, 0, 1);
				gpEditDep.add(lblEndHourInfo, 1, 1);
				gpEditDep.add(lblMaxStartHour, 0, 2);
				gpEditDep.add(cmbMaxStart, 1, 2);
				gpEditDep.add(lblEndHour2, 0, 3);
				gpEditDep.add(lblEndHourInfo2, 1, 3);

				vbRootAction.getChildren().addAll(lblActionTitle, gpEditDep, lblErrors, btnCheck/*, btnNext*/);

				cmbMinStart.setOnAction(new EventHandler<ActionEvent>() {
					//changes in the hour comboBox
					public void handle(ActionEvent arg0) {
						lblErrors.setText("");
						btnNext.setDisable(true);
						btnCheck.setDisable(false);
						for (int i = cmbMinStart.getValue().plusHours(9).getHour(); i <= 15; i ++)
							cmbMaxStart.getItems().add(LocalTime.of(i, 0));
						cmbMaxStart.getSelectionModel().select(cmbMinStart.getValue().plusHours(9));;
						cmbMaxStart.setDisable(false);
						//btnCheck.setDisable(false);
						lblEndHourInfo.setText("" + cmbMinStart.getValue().plusHours(9));
						lblEndHourInfo2.setText("" + cmbMaxStart.getValue().plusHours(9));
					}
				});
				
				cmbMaxStart.setOnAction(new EventHandler<ActionEvent>() {
					//changes in the hour comboBox
					public void handle(ActionEvent arg0) {
						lblEndHourInfo2.setText("" + cmbMaxStart.getValue().plusHours(9));
					}
				});

				btnCheck.setOnAction(new EventHandler<ActionEvent>() { //Next clicked
					@Override
					public void handle(ActionEvent arg0) {
						lblErrors.setText("");
						try {
							btnNext.setDisable(false);
							//btnCheck.setDisable(true);
							lblDisplayTempData.setText(myListener.generateMaxEffiFromUI(cmbMinStart.getValue(), cmbMaxStart.getValue()));
							spDisplay.setContent(lblDisplayTempData);
						} 
						catch (Exception e) {lblErrors.setText(e.getMessage());}
						if (!lblErrors.getText().equals("")) {
							btnNext.setDisable(true);
							btnCheck.setDisable(false);
						}
					}
				});
			}
		});

		//save and exit
		btnExitAndSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					myListener.saveFromUItoModel();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				theStage.close();
			}
		});

		//grid stuff
		gpRoot.add(lblTitle, 0, 0, 4, 1);
		gpRoot.add(lblSubTitle, 0, 0, 4, 2);
		GridPane.setHalignment(lblTitle, HPos.CENTER);
		GridPane.setValignment(lblTitle, VPos.TOP);
		GridPane.setHalignment(lblSubTitle, HPos.CENTER);
		GridPane.setValignment(lblSubTitle, VPos.BOTTOM);
		//add action stuff to grid
		gpRoot.add(btnAddRole, 0, 4);
		gpRoot.add(btnAddDepartment, 0, 5);
		gpRoot.add(btnAddEmployee, 0, 6);
		gpRoot.add(btnEditRole, 0, 7);
		gpRoot.add(btnEditDepartment, 0, 8);
		gpRoot.add(btnEditEmployee, 0, 9);
		gpRoot.add(btnGenMaxEfficiency, 0, 10);

		gpRoot.add(lblActionScreen, 1, 2);
		GridPane.setHalignment(lblActionScreen, HPos.CENTER);
		gpRoot.add(vbRootAction, 1, 3, 1, 11);

		//add display stuff to grid
		gpRoot.add(btnDisplayAllRoles, 2, 4);
		gpRoot.add(btnDisplayAllDepartments, 2, 5);
		gpRoot.add(btnDisplayAllEmployees, 2, 6);
		gpRoot.add(lblDisEfficiency, 2, 7);
		gpRoot.add(btnDisEffByRole, 2, 8);
		gpRoot.add(btnDisEffByDepartment, 2, 9);

		gpRoot.add(lblDisplayScreen, 3, 2);
		GridPane.setHalignment(lblDisplayScreen, HPos.CENTER);
		gpRoot.add(vbRootDisplay, 3, 3, 1, 11);

		//gpRoot.add(DisMaxEfficiency, 2, 10);
		gpRoot.add(btnExitAndSave, 3, 14);
		GridPane.setHalignment(btnExitAndSave, HPos.RIGHT);

		//gpRoot.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		//spDisplay.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		//BackgroundImage myBI= new BackgroundImage(new Image("/COMPANY/EFFECTS.jpg",32,32,false,true), null, null, null, null);
		//then you set to your node
		//gpRoot.setBackground(new Background(myBI));
		
		//HBox hbbigBox = new HBox();
		hbbigBox.getChildren().addAll(gpRoot);
		hbbigBox.setAlignment(Pos.CENTER);

		Scene scene = new Scene(hbbigBox, 1275, 575);
		//scene.setFill(Color.web("#81c483"));
		theStage.setScene(scene);
		theStage.show();
	}

	public void registerListener(UIEventsListener newListener) {
		myListener = newListener;
	}

	public void updateRoleData(String newAllRoles) {
		lblDisplayRoles.setText(newAllRoles);
	}
	public void updateDepartmentData(String newAllDepartments) {
		lblDisplayDepartments.setText(newAllDepartments);
	}
	public void updateEmployeeData(String newAllEmployeess) {
		lblDisplayEmployees.setText(newAllEmployeess);
	}
}
