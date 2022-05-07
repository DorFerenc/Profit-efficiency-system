package Emloyee;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;

import Classes.Department;
import Classes.Role;

public class BaseSalEmp extends Employee implements Serializable{
	
	//private Employee.employeeType salaryType;
	private final int monthlyHours = 160; //it is given to us as a fact that they do 160 monthly hours
	
	public BaseSalEmp(String name, String id, LocalDate birthDate, employeePreference employeePreference, Role myRole, Department myDepartment, LocalTime prefStartHour) throws Exception {
		super(name, id, birthDate, employeePreference, myRole, myDepartment, prefStartHour);
		//salaryType = Employee.employeeType.MONTHLY;
		super.setSalaryType(Employee.employeeType.MONTHLY);
		addToMonthlySalary(0);
		//setEmployeeType2(Employee.employeeType.MONTHLY);
	}
	
//	public void setEmployeeType2(Employee.employeeType temp) {
//		setEmployeeType(temp);
//	}
	
	public void addToMonthlySalary(double num) {
		super.setSalaryTotal(num + monthlyHours);
	}
	
	@Override
	public String toString() {
		DecimalFormat numFormat = new DecimalFormat("#.##");
		String msg = super.toString();
		msg += "\n        Salary Type:    " + super.getWorkType();
		msg += "\n        Income:           " + numFormat.format(super.getSalaryTotal()) + "$\n";
		return msg;
	}
}
