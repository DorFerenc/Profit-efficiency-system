package Emloyee;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;

import Classes.Department;
import Classes.Role;

public class HourlyEmp extends Employee implements Serializable{

	//private Employee.employeeType salaryType;
	private int hoursPerMonth; //random number for how many hours this guy works a month
	
	public HourlyEmp(String name, String id, LocalDate birthDate, employeePreference employeePreference, Role myRole, Department myDepartment, LocalTime prefStartHour) throws Exception {
		super(name, id, birthDate, employeePreference, myRole, myDepartment, prefStartHour);
		//salaryType = Employee.employeeType.HOURLY;
		int max = 200, min = 100, range = max - min + 1; 	// define the range
		hoursPerMonth = (int)(Math.random() * range) + min;// generate random numbers within 100 to 200 hours worked in month 
		super.setSalaryTotal(hoursPerMonth);
		super.setSalaryType(Employee.employeeType.HOURLY);
	}

//	@Override
//	public employeeType getWorkType() {
//		return super.getSalaryType();
//	}
	
	@Override
	public String toString() {
		DecimalFormat numFormat = new DecimalFormat("#.##");
		String msg = super.toString();
		msg += "\n        Salary Type:    " + super.getWorkType();
		msg += "\n        Income:           " + numFormat.format(super.getSalaryTotal()) + "$\n";
		return msg;
	}
}
