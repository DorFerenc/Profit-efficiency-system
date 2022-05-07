package Emloyee;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import Classes.Department;
import Classes.Role;

public class SalespBaseSalEmp extends BaseSalEmp implements Serializable{

	//private Employee.employeeType salaryType;
	
//	private double SalesPercentage;
//	private int salesMonthly;
	private double monthlyBonusInHours; //random number for bonus income calculated in hours;
	
	public SalespBaseSalEmp(String name, String id, LocalDate birthDate, employeePreference employeePreference, Role myRole, Department myDepartment, LocalTime prefStartHour) throws Exception {
		super(name, id, birthDate, employeePreference, myRole, myDepartment, prefStartHour);
		//salaryType = Employee.employeeType.MONTHLY_AND_SALES;
	
		double max = 32, min = 1.6, range = max - min + 1; 	// define the range
		monthlyBonusInHours = (Math.random() * range) + min;// generate random numbers within 1.6 to 32 hours worked in month 
		super.addToMonthlySalary(monthlyBonusInHours);
		super.setSalaryType(Employee.employeeType.MONTHLY_AND_SALES);
		//super.setEmployeeType2(Employee.employeeType.MONTHLY_AND_SALES);
	}

	@Override
	public String toString() {
		String msg = super.toString();
//		msg += "\n        Salary Type:    " + salaryType + "\n";
		return msg;
	}
}
