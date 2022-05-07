package MVC;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Vector;

import javax.swing.JOptionPane;

import Classes.Department;
import Classes.OurSet;
import Classes.Role;
import Emloyee.BaseSalEmp;
import Emloyee.Employee;
import Emloyee.HourlyEmp;
import Emloyee.SalespBaseSalEmp;
import Emloyee.Employee.employeePreference;
import Emloyee.Employee.employeeType;

public class Company implements Serializable{
	
	private transient Vector<ModelEventsListener> listeners;// = new Vector<ModelEventsListener>();
	
	private OurSet<Role> allRoles;// = new OurSet<Role>();
	private OurSet<Department> allDepartments;// = new OurSet<Department>();
	private OurSet<Employee> allEmployees;// = new OurSet<Employee>();

	public Company() throws FileNotFoundException, ClassNotFoundException, IOException, Exception {
		listeners = new Vector<ModelEventsListener>();
		shouldILoad();
	}

	public void emptyLoad() throws Exception {
		//open program without loading from saved data
		if (JOptionPane.showConfirmDialog(null, "Do you want to load Hard Coded data?", "HardCoded or Plain",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			allRoles = new OurSet<Role>();
			allDepartments = new OurSet<Department>();
			allEmployees = new OurSet<Employee>();

			Role r1 = new Role("maneger", true, false);
			Role r2 = new Role("sales", true, false);
			Role r3 = new Role("engineer", true, false);
			allRoles.add(r1);
			allRoles.add(r2);
			allRoles.add(r3);
			Department d1 = new Department("the manegers", true, false);
			Department d2 = new Department("the sales", true, true);
			Department d3 = new Department("the style coders", true, false);
			allDepartments.add(d1);
			allDepartments.add(d2);
			allDepartments.add(d3);
			BaseSalEmp e1 = new BaseSalEmp("manOne", "111111111", LocalDate.of(2000, 2, 2), employeePreference.EARLIER, r1, d1, LocalTime.of(6, 0));
			BaseSalEmp e2 = new BaseSalEmp("manTwo", "111111112", LocalDate.of(2000, 2, 2), employeePreference.EARLIER, r1, d1, LocalTime.of(6, 0));
			BaseSalEmp e3 = new BaseSalEmp("manThree", "111111113", LocalDate.of(2000, 2, 2), employeePreference.NORMAL, r1, d1, LocalTime.of(8, 0));
			HourlyEmp e4 = new HourlyEmp("manFour", "111111114", LocalDate.of(2000, 2, 2), employeePreference.LATER, r1, d1, LocalTime.of(13, 0));
			HourlyEmp e5 = new HourlyEmp("manFive", "111111115", LocalDate.of(2000, 2, 2), employeePreference.HOME, r1, d1, LocalTime.of(8, 0));
			HourlyEmp e6 = new HourlyEmp("ssalesOne", "111111116", LocalDate.of(2000, 2, 2), employeePreference.LATER, r2, d2, LocalTime.of(13, 0));
			HourlyEmp e7 = new HourlyEmp("ssalesTwo", "111111117", LocalDate.of(2000, 2, 2), employeePreference.EARLIER, r2, d2, LocalTime.of(5, 0));
			HourlyEmp e20 = new HourlyEmp("ssalesThree", "111111120", LocalDate.of(2000, 2, 2), employeePreference.NORMAL, r2, d2, LocalTime.of(8, 0));
			HourlyEmp e21 = new HourlyEmp("ssalesFour", "111111121", LocalDate.of(2000, 2, 2), employeePreference.HOME, r2, d2, LocalTime.of(8, 0));
			SalespBaseSalEmp e8 = new SalespBaseSalEmp("codeOne", "111111118", LocalDate.of(2000, 2, 2), employeePreference.EARLIER, r3, d3, LocalTime.of(3, 0));
			SalespBaseSalEmp e9 = new SalespBaseSalEmp("codeTwo", "111111119", LocalDate.of(2000, 2, 2), employeePreference.EARLIER, r3, d3, LocalTime.of(2, 0));
			allEmployees.add(e1);
			allEmployees.add(e2);
			allEmployees.add(e3);
			allEmployees.add(e4);
			allEmployees.add(e5);
			allEmployees.add(e6);
			allEmployees.add(e7);
			allEmployees.add(e8);
			allEmployees.add(e9);
			allEmployees.add(e20);
			allEmployees.add(e21);
			for (int i = 0; i < allEmployees.size(); i++) {
				updateEmployeeHelper(allEmployees.get(i)); //update their changeable and dynamic data
			}
			fireUpdateAllData();
		}
		else {
			allRoles = new OurSet<Role>();
			allDepartments = new OurSet<Department>();
			allEmployees = new OurSet<Employee>();
		}
	}
	
	public void shouldILoad() throws Exception {
		if (JOptionPane.showConfirmDialog(null, "Do you want to load previous data?", "Load Or New",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			//Get data from file (no static data needed to be updated)
			try {
				ObjectInputStream inFile = new ObjectInputStream(new FileInputStream("company.dat"));
				allRoles = (OurSet<Role>)(inFile.readObject());
				allDepartments = (OurSet<Department>)(inFile.readObject());
				allEmployees = (OurSet<Employee>)(inFile.readObject());
				inFile.close();
				fireUpdateAllData();
			}
			//in case of problems make it optional to open default data
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Can't find any saved file. Opening with default data.","File Error",JOptionPane.WARNING_MESSAGE);
				emptyLoad();
			}
		} 
		else {
			emptyLoad();
		}
	}
	
	public void fireUpdateAllData() {
		fireModelUpdatedAllRolesData();
		fireModelUpdatedAllDepartmentsData();
		fireModelUpdatedAllEmployeesData();
	}
	
	public void registerListener(ModelEventsListener listener) {
		listeners.add(listener);
	}

	public void addRole(String roleName, boolean isChangeable, boolean isDynamic) throws Exception {
		//input - variables to add a new role
		//adds a new role, adds it to the collection and open fires
		Role tempRole = new Role(roleName, isChangeable, isDynamic);
		allRoles.add(tempRole);
		fireUpdateAllData();
	}
	
	public void updateRoleInfo(String name, boolean tempIsChangable, boolean tempIsDynamic, LocalTime newStartHour) throws Exception {
		allRoles.getByName(name).setChangeable(tempIsChangable);
		allRoles.getByName(name).setDynamic(tempIsDynamic);
		allRoles.getByName(name).setStartHour(newStartHour);
		fireUpdateAllData();
	}
	
	private void fireModelUpdatedAllRolesData() {
		//fires information back to view (happens when info is updated)
		for(ModelEventsListener l : listeners) {
			l.modelUpdatedAllRolesData(allRoles.toString());
		}
	}
	
	public String calcAllRolesEfficiency() throws Exception {
		String msg = "";
		double sum = 0, temp;
		DecimalFormat numFormat = new DecimalFormat("#.##");
		for (int i = 0; i < allRoles.size(); i++) {
			temp = allRoles.get(i).calcEfficiency2(allRoles.get(i).getStartHour());
			sum += temp;
			msg += (i + 1) + ") " + allRoles.get(i).getRoleName() + ", efficiency change: " + numFormat.format(temp) + "\n";
		}
		msg += "\nTotal Company efficiency change by Roles: " + numFormat.format(sum);
		return msg;
	}
	
	public String[] getAllRoleNamesList() {
		//makes a list of all the role names in a string array
		String[] namesList = new String[allRoles.size()];
		for (int i = 0; i < allRoles.size(); i++) {
			namesList[i] = allRoles.get(i).getRoleName();
		}
		return namesList;
	}
	
	public Role getRoleByName(String roleName) throws Exception {
		return allRoles.getByName(roleName);
	}
	
	public void addDepartment(String depName, boolean isChangeable, boolean isDynamic) throws Exception {
		//adds new department to the data
		//input - variables to add a new department
		//adds a new department, adds it to the collection and open fires
		Department tempDepartment = new Department(depName, isChangeable, isDynamic);
		allDepartments.add(tempDepartment);
		fireUpdateAllData();
	}
	
	public void updateDepartmentInfo(String name, boolean tempIsChangable, boolean tempIsDynamic, LocalTime newStartHour) throws Exception {
		allDepartments.getByName(name).setChangeable(tempIsChangable);
		allDepartments.getByName(name).setDynamic(tempIsDynamic);
		allDepartments.getByName(name).setStartHour(newStartHour);
		fireUpdateAllData();
	}
	
	private void fireModelUpdatedAllDepartmentsData() {
		//fires information back to view (happens when info is updated)
		for(ModelEventsListener l : listeners) {
			l.modelUpdatedAllDepartmentsData(allDepartments.toString());
		}
	}
	
	public String calcAllDepartmentsEfficiency() throws Exception {
		String msg = "";
		double sum = 0, temp;
		DecimalFormat numFormat = new DecimalFormat("#.##");
		for (int i = 0; i < allDepartments.size(); i++) {
			temp = allDepartments.get(i).calcEfficiency2(allDepartments.get(i).getStartHour());
			sum += temp;
			msg += (i + 1) + ") " + allDepartments.get(i).getDepName() + ", efficiency change: " + numFormat.format(temp) + "\n";
		}
		msg += "\nTotal Company efficiency change by Roles: " + numFormat.format(sum);
		return msg;
	}
	
	public String[] getAllDepartmentNamesList() {
		//makes a list of all the Department names in a string array
		String[] namesList = new String[allDepartments.size()];
		for (int i = 0; i < allDepartments.size(); i++) {
			namesList[i] = allDepartments.get(i).getDepName();
		}
		return namesList;
	}
	
	public Department getDepartmentByName(String departmentName) throws Exception {
		return allDepartments.getByName(departmentName);
	}
	
	public void addEmployee(employeeType empType, String name, String id, LocalDate birthDate, employeePreference employeePreference, String myRoleName, String myDepartmentName, LocalTime prefStartHour) throws Exception{
		//add a employee to the system by 3 different types of employees
		if (allRoles.getByName(myRoleName).isChangeable() == false && allDepartments.getByName(myDepartmentName).isDynamic() == false) {
			if (allDepartments.getByName(myDepartmentName).getStartHour() != LocalTime.of(8, 0)) {
				throw new Exception("Error cant do role and department combination (sync/changeable)\n choose different role or department");
			}
		}
		if (allRoles.getByName(myRoleName).isDynamic() == false && allDepartments.getByName(myDepartmentName).isDynamic() == false) {
			if (allRoles.getByName(myRoleName).getStartHour() != allDepartments.getByName(myDepartmentName).getStartHour()) {
				throw new Exception("Error cant do role and department combination (sync/changeable)\n choose different role or department");
			}
		}
		if (allDepartments.getByName(myDepartmentName).isChangeable() == false && allRoles.getByName(myRoleName).isDynamic() == false) {
			if (allRoles.getByName(myRoleName).getStartHour() != LocalTime.of(8, 0)) {
				throw new Exception("Error cant do role and department combination (sync/changeable)\n choose different role or department");
			}
		}
		if (allDepartments.getByName(myDepartmentName).isDynamic() == false && allRoles.getByName(myRoleName).isDynamic() == false) {
			if (allDepartments.getByName(myDepartmentName).getStartHour() != allRoles.getByName(myRoleName).getStartHour()) {
				throw new Exception("Error cant do role and department combination (sync/changeable)\n choose different role or department");
			}
		}
		
		Employee tempEmployee;
		Role tempRole = allRoles.getByName(myRoleName);
		Department tempDepartment = allDepartments.getByName(myDepartmentName);
		if (empType == null) {
			throw new Exception("***Error employee salary Type is null***");
		}
		if (empType.equals(employeeType.MONTHLY))
			tempEmployee = new BaseSalEmp(name, id, birthDate, employeePreference, tempRole, tempDepartment, prefStartHour);
		else if (empType.equals(employeeType.HOURLY)) 
			tempEmployee = new HourlyEmp(name, id, birthDate, employeePreference, tempRole, tempDepartment, prefStartHour);
		else if (empType.equals(employeeType.MONTHLY_AND_SALES)) 
			tempEmployee = new SalespBaseSalEmp(name, id, birthDate, employeePreference, tempRole, tempDepartment, prefStartHour);
		else {
			throw new Exception("*$*Error employee type*$*");
		}
		//set changeable and dynamic
		if (tempEmployee.getMyDepartment().isChangeable() == false || tempEmployee.getMyRole().isChangeable() == false) { //check if he is not changeable 
			tempEmployee.setChangeable(false);
			tempEmployee.setDynamic(false);
		}
		else {
			tempEmployee.setChangeable(true); //yes changeable
			if (tempEmployee.getMyDepartment().isDynamic() == false || tempEmployee.getMyRole().isDynamic() == false) 
				tempEmployee.setDynamic(false); //not dynamic
			else
				tempEmployee.setDynamic(true); //yes dynamic
		}
			
		allEmployees.add(tempEmployee);
		fireUpdateAllData();
	}
	
	public void updateEmployeeHelper(Employee tempWorker) {
		//set changeable and dynamic
		if (tempWorker.getMyDepartment().isChangeable() == false || tempWorker.getMyRole().isChangeable() == false) { //check if he is not changeable 
			tempWorker.setChangeable(false);
			tempWorker.setDynamic(false);
		}
		else {
			tempWorker.setChangeable(true); //yes changeable
			if (tempWorker.getMyDepartment().isDynamic() == false || tempWorker.getMyRole().isDynamic() == false) 
				tempWorker.setDynamic(false); //not dynamic
			else
				tempWorker.setDynamic(true); //yes dynamic
		}
	}
	public void updateEmployee(String tempID, employeePreference employeePreference, String myRoleName, String myDepartmentName, employeeType newEmpType, LocalTime prefStartHour) throws Exception {
		if (allRoles.getByName(myRoleName).isChangeable() == false && allDepartments.getByName(myDepartmentName).isDynamic() == false) {
			if (allDepartments.getByName(myDepartmentName).getStartHour() != LocalTime.of(8, 0)) {
				throw new Exception("Error cant do role and department combination (sync/changeable)\n choose different role or department");
			}
		}
		if (allRoles.getByName(myRoleName).isDynamic() == false && allDepartments.getByName(myDepartmentName).isDynamic() == false) {
			if (allRoles.getByName(myRoleName).getStartHour() != allDepartments.getByName(myDepartmentName).getStartHour()) {
				throw new Exception("Error cant do role and department combination (sync/changeable)\n choose different role or department");
			}
		}
		if (allDepartments.getByName(myDepartmentName).isChangeable() == false && allRoles.getByName(myRoleName).isDynamic() == false) {
			if (allRoles.getByName(myRoleName).getStartHour() != LocalTime.of(8, 0)) {
				throw new Exception("Error cant do role and department combination (sync/changeable)\n choose different role or department");
			}
		}
		if (allDepartments.getByName(myDepartmentName).isDynamic() == false && allRoles.getByName(myRoleName).isDynamic() == false) {
			if (allDepartments.getByName(myDepartmentName).getStartHour() != allRoles.getByName(myRoleName).getStartHour()) {
				throw new Exception("Error cant do role and department combination (sync/changeable)\n choose different role or department");
			}
		}
		
		
		if (!allEmployees.getByName(tempID).getMyRole().getRoleName().equals(myRoleName))
			allEmployees.getByName(tempID).changeEmployeeRole(allRoles.getByName(myRoleName));
		if (!allEmployees.getByName(tempID).getMyDepartment().getDepName().equals(myDepartmentName))
			allEmployees.getByName(tempID).changeEmployeeDepartment(allDepartments.getByName(myDepartmentName));
		if(!allEmployees.getByName(tempID).getEmployeePreference().equals(employeePreference))
			allEmployees.getByName(tempID).setEmployeePreference(employeePreference);
		allEmployees.getByName(tempID).setPrefStartHour(prefStartHour);
		updateEmployeeHelper(allEmployees.getByName(tempID));
		
		if (!(allEmployees.getByName(tempID).getWorkType().equals(newEmpType))) { 
			//if this employee type is NOT equal to the one the viewer wanted to change into
			//we open a new one of that sort and remove this one
			Employee tempWorker = allEmployees.getByName(tempID);
			if (newEmpType == null) {
				throw new Exception("***Error employee salary Type is null***");
			}
			if (newEmpType.equals(employeeType.MONTHLY))
				tempWorker = new BaseSalEmp(tempWorker.getName(), tempWorker.getId(), tempWorker.getBirthDate(), employeePreference, tempWorker.getMyRole(), tempWorker.getMyDepartment(), prefStartHour);
			else if (newEmpType.equals(employeeType.HOURLY)) 
				tempWorker = new HourlyEmp(tempWorker.getName(), tempWorker.getId(), tempWorker.getBirthDate(), employeePreference, tempWorker.getMyRole(), tempWorker.getMyDepartment(), prefStartHour);
			else if (newEmpType.equals(employeeType.MONTHLY_AND_SALES)) 
				tempWorker = new SalespBaseSalEmp(tempWorker.getName(), tempWorker.getId(), tempWorker.getBirthDate(), employeePreference, tempWorker.getMyRole(), tempWorker.getMyDepartment(), prefStartHour);
			else {
				throw new Exception("*$*Error employee type*$*");
			}
			tempWorker.setPrefStartHour(prefStartHour);
			updateEmployeeHelper(tempWorker);
			allEmployees.remove(allEmployees.getByName(tempID)); //remove the old one
			allEmployees.add(tempWorker); //add the new one
		}
		fireUpdateAllData();
	}
	
	private void fireModelUpdatedAllEmployeesData() {
		for(ModelEventsListener l : listeners) {
			l.modelUpdatedAllEmployees(allEmployees.toString());
		}
	}
	
	public String[] getAllEmployeesIDNamesList() {
		//makes a list of all the role names in a string array
		String[] idNamesList = new String[allEmployees.size()];
		for (int i = 0; i < allEmployees.size(); i++) {
			idNamesList[i] = allEmployees.get(i).getId() + "     :     " + allEmployees.get(i).getName();
		}
		return idNamesList;
	}
	
	public Employee getEmployeeByID(String tempID) throws Exception {
		return allEmployees.getByName(tempID);
	}
	
	public <E> void removeElement(E element) throws Exception {
		//remove an element
		if (element instanceof Role) {
			if (((Role)element).getRoleEmployees().size() != 0) {
				throw new Exception("Can NOT delete this role it still has employees in it");
			}
			allRoles.remove((Role)element);
		}
		if (element instanceof Department) {
			if (((Department)element).getDepEmployees().size() != 0) {
				throw new Exception("Can NOT delete this Department it still has employees in it");
			}
			allDepartments.remove((Department)element);
		}
		if (element instanceof Employee) {
			((Employee)element).getMyRole().removeEmployee(((Employee)element)); //delete this employee from his role
			for (int i = 0; i < allRoles.size(); i++) {
				if (allRoles.get(i).containsEmployee(((Employee)element)))
					allRoles.get(i).removeEmployee(((Employee)element));
			}
			((Employee)element).getMyDepartment().removeEmployee(((Employee)element)); //delete this employee from his department
			for (int i = 0; i < allDepartments.size(); i++) {
				if (allDepartments.get(i).containsEmployee(((Employee)element)))
					allDepartments.get(i).removeEmployee(((Employee)element));
			}
			allEmployees.remove((Employee)element);
		}
		fireUpdateAllData();
	}
	
//	public void employeeChangeDepartment(Employee worker, Department newDepartment) throws Exception {
//		//change department to employee
//		//changes from a list of departments that already exists 
//		worker.changeEmployeeDepartment(newDepartment);
//	}
//	
//	public void employeeChangeRole(Employee worker, Role newRole) throws Exception {
//		//change Role to employee
//		//changes from a list of Roles that already exists 
//		worker.changeEmployeeRole(newRole);
//	}
	
	public String calcMaxEffi(LocalTime minStartHour, LocalTime maxStartHour) throws Exception {
		if (allRoles.equals(null) || allDepartments.equals(null) || allEmployees.equals(null)) {
			throw new Exception("First Enter at least 1 Role, 1 Department, and 1 Employee");
		}
		String msg = "----------Through Role: ----------";
		msg += maxEffiThroughRole(minStartHour, maxStartHour);
		msg += "\n\n----------Through Department: ----------";
		msg += maxEffi2ThroughDepartment(minStartHour, maxStartHour);
		return msg;
	}
	
	private String maxEffiThroughRole(LocalTime minStartHour, LocalTime maxStartHour) {
		String msg = "", depName = "";
		double tempRole;
		LocalTime maxDepTime, maxRoleTime;
		maxDepTime = LocalTime.of(8, 0);
		maxRoleTime = LocalTime.of(8, 0);
		DecimalFormat numFormat = new DecimalFormat("#.##");
		//try {
			for (int r = 0; r < allRoles.size(); r++) { //go through all the roles
				try {
				double maxForThisRole = allRoles.get(r).calcEfficiency2(LocalTime.of(8, 0));
				for (int h = minStartHour.getHour(); h <= maxStartHour.getHour(); h++) { //go through all the hours for roles
					LocalTime tempTime = LocalTime.of(h, 0);
					for (int d = 0; d < allDepartments.size(); d++) { //go through all departments
						LocalTime initialStartingHour = allDepartments.get(d).getStartHour();
						for (int h2 = minStartHour.getHour(); h2 <= maxStartHour.getHour(); h2++) { //go through all the hours for departments
							try {
								LocalTime tempTime2 = LocalTime.of(h, 0);
								allDepartments.get(d).setStartHour(tempTime2);
								tempRole = allRoles.get(r).calcEfficiency2(tempTime);
								if (tempRole >= maxForThisRole) {
									maxRoleTime = tempTime;
									maxDepTime = tempTime2;
									maxForThisRole = tempRole;
									depName = allDepartments.get(d).getDepName();
								}
							} catch (Exception e) {
								//System.out.println(e.getMessage() + " r= " + r + ", d= " + d + ", h= " + h + ", h2= " + h2);
							}
						}
						allDepartments.get(d).setStartHour(initialStartingHour);
					}
				}
				msg += "\n\nFor Role: " + allRoles.get(r).getRoleName() + "\n    The most effiecient starting hours is: " + maxRoleTime;
				msg += "\n    With Department: " + depName + " Start hour of: " + maxDepTime + "\n    The efficency is: " + numFormat.format(maxForThisRole);
				} catch (Exception e) {/*System.out.println("ROLE2222222" + e.getMessage());*/}
			}
		//} catch (CloneNotSupportedException e1) {e1.printStackTrace();} catch (Exception e1) {System.out.println(e1.getMessage()); e1.printStackTrace();}
		return msg;
	}

	private String maxEffi2ThroughDepartment(LocalTime minStartHour, LocalTime maxStartHour) {
		String msg = "", rolName = "";
		double tempDep;
		boolean flagIsProblem = false;
		LocalTime maxDepTime, maxRoleTime;
		maxDepTime = LocalTime.of(8, 0);
		maxRoleTime = LocalTime.of(8, 0);
		DecimalFormat numFormat = new DecimalFormat("#.##");
		//try {
			for (int d = 0; d < allDepartments.size(); d++) { 	//go through all departments
				try {
				double maxForThisDepartment = allDepartments.get(d).calcEfficiency2(LocalTime.of(8, 0));
				for (int h = minStartHour.getHour(); h <= maxStartHour.getHour(); h++) { //go through all the hours for departments
					LocalTime tempTime = LocalTime.of(h, 0);
					for (int r = 0; r < allRoles.size(); r++) { //go through all the roles
						LocalTime initialStartingHour = allRoles.get(r).getStartHour();
						for (int h2 = minStartHour.getHour(); h2 <= maxStartHour.getHour(); h2++) { //go through all the hours for roles
							try {
								LocalTime tempTime2 = LocalTime.of(h, 0);
								//allDepartments.get(d).setStartHour(tempTime2);
								allRoles.get(r).setStartHour(tempTime2);
								tempDep = allDepartments.get(d).calcEfficiency2(tempTime);
								if (tempDep >= maxForThisDepartment) {
									maxRoleTime = tempTime;
									maxDepTime = tempTime2;
									maxForThisDepartment = tempDep;
									rolName = allRoles.get(r).getRoleName();
								}
							} catch (Exception e) {/*System.out.println(e.getMessage() + " r= " + r + ", d= " + d + ", h= " + h + ", h2= " + h2);*/}
						}
						allRoles.get(r).setStartHour(initialStartingHour);
					}
				}
				msg += "\n\nFor Department: " + allDepartments.get(d).getDepName() + "\n    The most effiecient starting hours is: " + maxDepTime;
				msg += "\n    With Role: " + rolName + " Start hour of: " + maxRoleTime + "\n    The efficency is: " + numFormat.format(maxForThisDepartment);
				}catch (Exception e) {/*System.out.println("DEP2222222" + e.getMessage());*/}
			}
		//} catch (Exception e1) {System.out.println(e1.getMessage());}
		return msg;
	}
	//	
//	public String maxEffi(LocalTime minStartHour, LocalTime maxStartHour) /*throws Exception* Efficiency*/ {
//		String msg = "Maximum Efficiency Hours:";
//		msg += "\nInfo:\nIf it says 08:00 - you should leave it normal \n(sync) at the end - can be better if you edit company characteristics";
//		double maxDep = 0, tempDep, maxRole = 0, tempRole;
//		int maxDepIndex, maxRoleIndex;
//		LocalTime maxDepTime, maxRoleTime, tempTime;
//		maxDepTime = LocalTime.of(8, 0);
//		maxRoleTime = LocalTime.of(8, 0);
//		boolean flagIsProblem = false;
//		DecimalFormat numFormat = new DecimalFormat("#.##");
//		
//		for (int i = 0; i < allRoles.size(); i++) { //get max from role
//			for (int j = minStartHour.getHour(); j <= maxStartHour.getHour(); j++) { //go through all the hours
//				tempTime = LocalTime.of(j, 0);
//				flagIsProblem = false;
//				try {
//					tempRole = allRoles.get(i).calcEfficiency2(tempTime);
//					if (tempRole >= maxRole) {
////						System.out.println("rol bigger index: " + i + " time: " + tempTime + " effi= " + tempRole);
//						maxRoleIndex = i;
//						maxRoleTime = tempTime;
//						maxRole = tempRole;
//					}
////					else
////						System.out.println("sory but effi is: " + tempRole + " and max is: " + maxRole);
//				} catch (Exception e) {
////					System.out.println(e.getMessage());
//					flagIsProblem = true;
//				}
//			}
//			if (flagIsProblem)
//				msg += "\n\nFor Role: " + allRoles.get(i).getRoleName() + "\n    The most effiecient starting hours is: " + maxDepTime + ", (sync)";
//			else
//				msg += "\n\nFor Role: " + allRoles.get(i).getRoleName() + "\n    The most effiecient starting hours is: " + maxDepTime;
//		}
//		
//		for (int i = 0; i < allDepartments.size(); i++) { //get max from departments
//			for (int j = minStartHour.getHour(); j <= maxStartHour.getHour(); j++) { //go through all the hours
//				tempTime = LocalTime.of(j, 0);
//				flagIsProblem = false;
//				try {
//					tempDep = allDepartments.get(i).calcEfficiency2(tempTime);
//					if (tempDep >= maxDep) {
////						System.out.println("dep bigger index: " + i + " time: " + tempTime + " effi= " + tempDep);
//						maxDepIndex = i;
//						maxDepTime = tempTime;
//						maxDep = tempDep;
//					}
////					else
////						System.out.println("sory but effi is: " + tempDep + " and max is: " + maxDep);
//				} catch (Exception e) {
////					System.out.println(e.getMessage());
//					flagIsProblem = true;
//				}
//			}
//			if (flagIsProblem)
//				msg += "\n\nFor Department: " + allDepartments.get(i).getDepName() + "\n    The most effiecient starting hours is: " + maxDepTime + ", (sync)";
//			else
//				msg += "\n\nFor Department: " + allDepartments.get(i).getDepName() + "\n    The most effiecient starting hours is: " + maxDepTime;
//		}
//		
//		return msg;
//	}
	
	public void save() throws Exception {
		ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream("company.dat"));
		outFile.writeObject(allRoles);
		outFile.writeObject(allDepartments);
		outFile.writeObject(allEmployees);
		outFile.close();
	}
}
