package Emloyee;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import Classes.Changeable;
import Classes.Department;
import Classes.Role;
import Classes.Synchronizedable;

public abstract class Employee implements Serializable, Cloneable, Synchronizedable, Changeable {
	public enum employeePreference {EARLIER, LATER, NORMAL, HOME};
	public enum employeeType {MONTHLY, HOURLY, MONTHLY_AND_SALES}
	
	private Employee.employeeType salaryType;
	private String name;
	private String id;
	private LocalDate birthDate;
	private employeePreference employeePreference;
	private Role myRole;
	private Department myDepartment;
	private boolean isChangeable;
	private boolean isDynamic;
	private LocalTime prefStartHour;
	private double salaryTotal;
	private final int DEFAULT_WAGE = 50; //default wage per hour of work from each employee
	
	public Employee(String name, String id, LocalDate birthDate, employeePreference employeePreference, /*double salary,*/ Role myRole, Department myDepartment, LocalTime prefStartHour) throws Exception {
		setID(id);
		setName(name);
		setBirthDate(birthDate);
		//this.employeePreference = employeePreference;
		setEmployeePreference(employeePreference);
		//this.salary = salary;
		setMyRole(myRole);
		setMyDepartment(myDepartment);
		this.prefStartHour = prefStartHour;
	}
	
	private void setID(String id) throws Exception {
		//checks ID valid
		if (id.length() != 9) {
			throw new Exception("***Error ID must be 9 digits***"); 
		}
		if (!id.matches("[0-9]+")) { //TODO check + sign
			throw new Exception("***Error ID input must be numbers***");
		}
		if (Integer.parseInt(id) == 0) {
			throw new Exception("***Error ID all values are zero***"); 
		}
		this.id = id;
	}
	
	private boolean checkName(String name) throws Exception {
		String goodChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		if (name.length() <= 0) {
			throw new Exception("Error name is null");
		}
		if (name.charAt(0) == ' ' || name.charAt(name.length() - 1) == ' ') {
			throw new Exception("Name cant strat or end with ' ' (space)");
		}
		boolean spaceFlag = false;
		for (int i = 0; i < name.length(); i++) {
			spaceFlag = false;
			if (!(goodChars.contains("" + name.charAt(i)))) {
				//if this is not an English letter
				if (name.charAt(i) == ' ') {//check if its space
					if (name.charAt((i+1)) == ' ') { //if space check no double spaces
						//we know this is not the last tab because we checked that its not ending with space
						throw new Exception("Name cant have two ' ',' ' (spaces) one after another");
					}
					else
						spaceFlag = true; //we have a space here
				}
				else {
					if (spaceFlag == false) {
						//not space and not English letter
						throw new Exception("Name can only have English letters and spaces");
					}
				}
			}
		}
		return true;
		//add lower case		
	}
	
	private void setName(String name) throws Exception {
		if (checkName(name))
			this.name = name.toLowerCase();
	}
	
	private void setBirthDate(LocalDate birthDate) throws Exception {
		//checking worker age 
		if (birthDate == null) {
			throw new Exception("***Error Date data is null***");
		}
		LocalDate nowDate = LocalDate.now();
		int nowyear = (nowDate.getYear() - 100);
		nowDate = nowDate.minusYears(14); //min working age is 14
		if (nowDate.isBefore(birthDate)) {
			throw new Exception("***Error InValid Date data must be atleast 14 years old***");
		}
		if (nowyear - birthDate.getYear() >= 0) {
			throw new Exception("***Error sorry we dont accept people your age***");
		}
		this.birthDate = birthDate;
	}
	
	public void setEmployeePreference(employeePreference employeePreference) throws Exception {
		if (employeePreference != null)
			this.employeePreference = employeePreference;
		else {
			throw new Exception("***Error employee Preference is null***");
		}
	}
	
	public void setMyRole(Role myRole) throws Exception {
		if (myRole == null) {
			throw new Exception("***Error Role is null***");
		}
		this.myRole = myRole;
		if(!myRole.containsEmployee(this))
			myRole.addEmployee(this);
	}

	public void setMyDepartment(Department myDepartment) throws Exception {
		if (myDepartment == null) {
			throw new Exception("***Error Department is null***");
		}
		this.myDepartment = myDepartment;
		if (!myDepartment.containsEmployee(this))
			myDepartment.addEmployee(this);
	}

	public void changeEmployeeRole(Role newRole) throws Exception {
		//option to change employees role
		//add him to new role
		//change department that employee is connected to (make sure has that role)
		//remove employee from old Role
		if (myRole.equals(newRole))
			return;
		myRole.removeEmployee(this);
//		if (myDepartment.containsRole(newRole))
//		myDepartment.employeeChangedRole(newRole, myRole);
//		newRole.addEmployee(this);
		myRole = newRole;
		if (myRole.containsEmployee(this) == false)
			myRole.addEmployee(this);
	}
	
	public void changeEmployeeDepartment(Department newDepartment) throws Exception {
		//option to change employees department
		//add him to new department
		//update stats here and remove from old department
		if (myDepartment.equals(newDepartment))
			return;
		myDepartment.removeEmployee(this);
//		newDepartment.addEmployee(this);
		myDepartment = newDepartment;
		if (myDepartment.containsEmployee(this) == false)
			myDepartment.addEmployee(this);
	}
	
	public employeePreference getEmployeePreference() {
		return employeePreference;
	}
	
//	public abstract employeeType getWorkType(); //make all the sons add this so can use later
	
	public Role getMyRole() {
		return myRole;
	}
	
	public Department getMyDepartment() {
		return myDepartment;
	}
	
	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}
	
	public void setChangeable(boolean isChangeable) {
		this.isChangeable = isChangeable;
	}

	public void setDynamic(boolean isDynamic) {
		this.isDynamic = isDynamic;
	}

	public boolean isChangeable() {
		return isChangeable;
	}
	
	public boolean isDynamic() {
		return isDynamic;
	}
	
	public LocalTime getPrefStartHour() {
		return prefStartHour;
	}

	public void setPrefStartHour(LocalTime newPrefStartHour) {
		prefStartHour = newPrefStartHour;
	}
	
	public int getDEFAULT_WAGE() {
		return DEFAULT_WAGE;
	}

	public double getSalaryTotal() {
		return salaryTotal;
	}

	public void setSalaryTotal(double amountOfHours) {
		salaryTotal = amountOfHours * DEFAULT_WAGE;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Employee))
			return false;
		Employee tempEmployee = (Employee)obj;
		if (id.equals(tempEmployee.id))
				return true;
		return false;
	}
	
	
	public Employee.employeeType getWorkType() {
		return salaryType;
	}

	public void setSalaryType(Employee.employeeType salaryType) {
		this.salaryType = salaryType;
	}

	@Override
	public String toString() {
		String msg = "";
		msg += "    Employee:      " + name;
		msg += "\n        ID:                  " + id;
		msg += "\n        BirthDate:       " + birthDate;
		msg += "\n        Preference:     " + employeePreference;
		msg += "\n        Hour:              " + prefStartHour;
		msg += "\n        Role:               " + myRole.getRoleName();
		msg += "\n        Department:   " + myDepartment.getDepName();
		msg += "\n        Changable:     " + isChangeable;
		msg += "\n        Dynamic:        " + isDynamic;
		return msg;
	}
	
	public Employee clone() throws CloneNotSupportedException {
		//clone for working on temp in view
		Employee temp = (Employee)super.clone();
		temp.myDepartment = (Department)myDepartment.clone();
		temp.myRole = (Role)myRole.clone(); 
		return temp;
	}
	
}
