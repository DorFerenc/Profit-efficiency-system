package MVC;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import Classes.Department;
import Classes.Role;
import Emloyee.Employee;
import Emloyee.Employee.employeePreference;
import Emloyee.Employee.employeeType;
import View.CompanyView;

public class CompanyController implements Serializable, ModelEventsListener, UIEventsListener{
	
	private Company theModel;
	private CompanyView theView;
	
	public CompanyController(Company theModel, CompanyView theView) throws FileNotFoundException, ClassNotFoundException, IOException, Exception {
		this.theModel = theModel;
		this.theView = theView;

		theView.registerListener(this);
		theModel.registerListener(this);

		theModel.fireUpdateAllData();
	}
	
	public void addRoleToModel(String tempName, boolean tempIsChangable, boolean tempIsDynamic) throws Exception {
		theModel.addRole(tempName, tempIsChangable, tempIsDynamic);
	}
	
	public void updateRoleFromUI(String roleName, boolean tempIsChangable, boolean tempIsDynamic, LocalTime newStartHour) throws Exception {
		theModel.updateRoleInfo(roleName, tempIsChangable, tempIsDynamic, newStartHour);
	}
	
	public String viewAsksForEfficiencyByRole() throws Exception {
		return theModel.calcAllRolesEfficiency();
	}
	
	public String[] viewAsksForAllRoleNames() {
		return theModel.getAllRoleNamesList();
	}
	
	public Role viewAsksForRoleByName(String roleName) throws Exception {
		return theModel.getRoleByName(roleName);
	}
	
	public void addDepartmentToModel(String tempName, boolean tempIsChangable, boolean tempIsDynamic) throws Exception {
		theModel.addDepartment(tempName, tempIsChangable, tempIsDynamic);
	}
	
	public void updateDepartmentFromUI(String depName, boolean tempIsChangable, boolean tempIsDynamic, LocalTime newStartHour) throws Exception {
		theModel.updateDepartmentInfo(depName, tempIsChangable, tempIsDynamic, newStartHour);
	}

	public String viewAsksForEfficiencyByDepartment() throws Exception {
		return theModel.calcAllDepartmentsEfficiency();
	}
	
	public String[] viewAsksForAllDepartmentNames() {
		return theModel.getAllDepartmentNamesList();
	}
	
	public Department viewAsksForDepartmentByName(String depName) throws Exception {
		return theModel.getDepartmentByName(depName);
	}
	
	public void addEmployeeToModel(employeeType empType, String name, String id, LocalDate birthDate, employeePreference employeePreference, String myRoleName, String myDepartmentName, LocalTime prefStartHour) throws Exception {
		theModel.addEmployee(empType, name, id, birthDate, employeePreference, myRoleName, myDepartmentName, prefStartHour);
	}
	
	public void updateEmployeeFromUI(String tempID, employeePreference employeePreference, String myRoleName, String myDepartmentName, employeeType empType, LocalTime prefStartHour) throws Exception {
		theModel.updateEmployee(tempID, employeePreference, myRoleName, myDepartmentName, empType, prefStartHour);
	}

	public Employee viewAsksForEmployeeByID(String id) throws Exception {
		return theModel.getEmployeeByID(id);
	}
	
	public String[] viewAsksForAllEmployeeIDNames() {
		return theModel.getAllEmployeesIDNamesList();
	}
	
	public String generateMaxEffiFromUI(LocalTime minStartHour, LocalTime maxStartHour) throws Exception {
		//return theModel.maxEffi(minStartHour, maxStartHour);
		return theModel.calcMaxEffi(minStartHour, maxStartHour);
	}
	
	public <E> void removeFromUI(E elementToRemove) throws Exception {
		theModel.removeElement(elementToRemove);
	}
	
	public void saveFromUItoModel() throws Exception {
		theModel.save();
	}
	
	public void modelUpdatedAllRolesData(String allRoles) {
		theView.updateRoleData(allRoles);
	}
	
	public void modelUpdatedAllDepartmentsData(String allDepartments) {
		theView.updateDepartmentData(allDepartments);
	}
	
	public void modelUpdatedAllEmployees(String allEmployees) {
		theView.updateEmployeeData(allEmployees);
	}

}
