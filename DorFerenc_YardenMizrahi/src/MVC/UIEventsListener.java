package MVC;

import java.time.LocalDate;
import java.time.LocalTime;

import Classes.Department;
import Classes.Role;
import Emloyee.Employee;
import Emloyee.Employee.employeePreference;
import Emloyee.Employee.employeeType;

public interface UIEventsListener {
	public void addRoleToModel(String tempName, boolean tempIsChangable, boolean tempIsDynamic) throws Exception;
	public void updateRoleFromUI(String roleName, boolean tempIsChangable, boolean tempIsDynamic, LocalTime newStartHour) throws Exception;
	public String[] viewAsksForAllRoleNames();
	public Role viewAsksForRoleByName(String roleName) throws Exception;
	public String viewAsksForEfficiencyByRole() throws Exception;

	public void addDepartmentToModel(String tempName, boolean tempIsChangable, boolean tempIsDynamic) throws Exception;
	public void updateDepartmentFromUI(String depName, boolean tempIsChangable, boolean tempIsDynamic, LocalTime newStartHour) throws Exception;
	public String[] viewAsksForAllDepartmentNames();
	public Department viewAsksForDepartmentByName(String depName) throws Exception;
	public String viewAsksForEfficiencyByDepartment() throws Exception;

	public void addEmployeeToModel(employeeType empType, String name, String id, LocalDate birthDate, employeePreference employeePreference, String myRoleName, String myDepartmentName, LocalTime prefStartHour) throws Exception;
	public void updateEmployeeFromUI(String tempID, employeePreference employeePreference, String myRoleName, String myDepartmentName, employeeType empType, LocalTime prefStartHour) throws Exception;
	public Employee viewAsksForEmployeeByID(String id) throws Exception ; 
	public String[] viewAsksForAllEmployeeIDNames();
	
	public String generateMaxEffiFromUI(LocalTime minStartHour, LocalTime maxStartHour) throws Exception;
	public <E> void removeFromUI(E elementToRemove) throws Exception;
	public void saveFromUItoModel() throws Exception;
}
