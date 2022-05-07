package Classes;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;

import Emloyee.Employee;
import Emloyee.Employee.employeePreference;

public class Department implements Serializable, Changeable, Synchronizedable, Cloneable{
	private String depName;
	private OurSet<Employee> depEmployees;
	//private ArrayList<Role> depRoles;
	private boolean isDynamic;
	private boolean isChangeable;
	private LocalTime startHour;
	
	public Department(String depName, boolean isChangeable, boolean isDynamic) throws Exception {
		setDepName(depName);
		this.isChangeable = isChangeable;
		this.isDynamic = isDynamic;
		this.startHour = LocalTime.of(8, 0); //default start time is 8 AM
		depEmployees = new OurSet<Employee>();
		//depRoles = new ArrayList<Role>();
	}
	
	public void addEmployee(Employee newWorker) throws Exception {
		//add employee to department
		//add employees role to department
		depEmployees.add(newWorker);
		//depRoles.add(newWorker.getMyRole());
	}
	
	public void removeEmployee(Employee oldWorker) throws Exception {
		//removes employee from this role - if he changes roles
		depEmployees.remove(oldWorker);
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
	
	private void setDepName(String depName) throws Exception {
		if (checkName(depName))
			this.depName = depName.toLowerCase();
	}
	
	public void setChangeable(boolean isChangeable) {
		this.isChangeable = isChangeable;
	}

	public void setDynamic(boolean isDynamic) {
		this.isDynamic = isDynamic;
	}


	public void setStartHour(LocalTime startHour) {
		this.startHour = startHour;
	}
	
//	public void employeeChangedRole(Role newRole, Role oldRole) throws Exception {
//		//in the case an employee changes his role in the company
//		//make sure to update his department
//		depRoles.remove(oldRole);
//		depRoles.add(newRole);
//	}
	
	public String getDepName() {
		return depName;
	}

	public OurSet<Employee> getDepEmployees() {
		return depEmployees;
	}

//	public ArrayList<Role> getDepRoles() {
//		return depRoles;
//	}

	public boolean isChangeable() {
		return isChangeable;
	}
	
	public boolean isDynamic() {
		return isDynamic;
	}

	public LocalTime getStartHour() {
		return startHour;
	}
	
	public String calcEfficiency(LocalTime newStartHour) throws Exception {
		double efficiency;
		if (this.isChangeable() == false)
			efficiency = calcDepEfficiencyUnChangeable(newStartHour);
		else if (this.isDynamic)
			efficiency = calcDepEfficiencyChangeDynamic(newStartHour);
		else
			efficiency = calcDepEfficiencyChangeSync(newStartHour);
		DecimalFormat numFormat = new DecimalFormat("#.##");
		String msg = "Department:                " + depName;
		msg += "\n    Changable:              " + isChangeable;
		msg += "\n    Dynamic:                  " + isDynamic;
		msg += "\n    Starting Hour:          " + startHour;
		msg += "\n    Efficiency change:    " + numFormat.format(efficiency);
		return msg;
	}
	
	public double calcEfficiency2(LocalTime newStartHour) throws Exception {
		double efficiency;
		if (this.isChangeable() == false)
			efficiency = calcDepEfficiencyUnChangeable(newStartHour);
		else if (this.isDynamic)
			efficiency = calcDepEfficiencyChangeDynamic(newStartHour);
		else
			efficiency = calcDepEfficiencyChangeSync(newStartHour);
		return efficiency;
	}
	private double calcDepEfficiencyUnChangeable(LocalTime newStartHour) throws Exception {
		
		LocalTime defaultStart = LocalTime.of(8, 0);
		int hoursOffSet = Math.abs(newStartHour.getHour() - defaultStart.getHour()); // when unchanged the offset is always just the difference from 8
		int numOfGoodHours = 0, numOfHomeHours = 0, numOfBadHours = 0, numOfRegHours = 0;

		int restCounter = 0, prefCounter = 0;
		for (int i = 0; i < depEmployees.size(); i++) {
			Employee tempWorker = depEmployees.get(i);
			int tempHourOffset = Math.min(hoursOffSet, Math.abs(tempWorker.getPrefStartHour().getHour() - defaultStart.getHour()));
			if (tempWorker.getMyRole().isDynamic() == false && tempWorker.getMyRole().getStartHour() != defaultStart) {
				throw new Exception("Cannot change to Unchangeable because: " + tempWorker.getMyRole().getRoleName() + "\n is synced to: " + tempWorker.getMyRole().getStartHour());
			}
			if (tempWorker.getEmployeePreference().equals(employeePreference.NORMAL)) {
				prefCounter++;//NOT changeable but this is what the employee wanted
				numOfGoodHours += 9;
			}
			else {
				if (tempWorker.getEmployeePreference().equals(employeePreference.HOME)) {
					tempHourOffset = 0;
				}
				restCounter ++;//not what he wanted and cant do anything else
				numOfBadHours += tempHourOffset; //gives the smaller (employee wanted and what can happen in real life)
				numOfRegHours += (9 - tempHourOffset);
			}
		}
		
		//check that all is good
		if ((prefCounter + restCounter) != depEmployees.size()) {
			//if its not the same then the algorithm is missing
			throw new Exception("WE Counted: " + (prefCounter + restCounter) +
					" and there are supposed to be: " + depEmployees.size());
		}
		if ((numOfGoodHours + numOfBadHours + numOfHomeHours + numOfRegHours) != (depEmployees.size()*9)) {
			//if its not them same time then algorithm is bad
			throw new Exception("WE Counted Hours: " + (numOfGoodHours + numOfBadHours + numOfHomeHours + numOfRegHours) +
					" and there are supposed to be: " + (depEmployees.size() * 9));
		}
		
		double good = (prefCounter * numOfGoodHours * 0.2); //num of workers * num of good hours * +0.2
		double bad =  (restCounter * numOfBadHours * (-0.2)); //num of workers * num of bad hours * -0.2
		double efficiency = good + bad;
		return efficiency;
	}
//	//FOR WHEN HOURS DONT COUNT
//	private double calcRoleEfficiencyUnChangeable(LocalTime newStartHour) {
//		
//		LocalTime defaultStart = LocalTime.of(8, 0);
//		int hoursOffSet = Math.abs(newStartHour.getHour() - defaultStart.getHour());
//
//		int restCounter = 0, prefCounter = 0;
//		for (int i = 0; i < depEmployees.size(); i++) {
//			Employee tempWorker = depEmployees.get(i);
//			if (tempWorker.getEmployeePreference().equals(employeePreference.NORMAL))
//				prefCounter++;//NOT changeable but this is what the employee wanted
//			else
//				restCounter ++;//Possible because this role is dynamic
//		}
//		
//		double good = (prefCounter * hoursOffSet * 0.2);
//		double bad =  (restCounter * hoursOffSet * (-0.2));
//		double efficiency = good + bad;
//		return efficiency;
//	}
	private double calcDepEfficiencyChangeDynamic(LocalTime newStartHour) throws Exception { //Dynamic AND CHANGABLE
		//calculates the efficiency of all employees in this department in the case that the department is  changeable and Dynamic 
		LocalTime defaultStart = LocalTime.of(8, 0);
		int hoursOffSet = Math.abs(newStartHour.getHour() - defaultStart.getHour());
		int numOfGoodHours = 0, numOfHomeHours = 0, numOfBadHours = 0, numOfRegHours = 0;
		int restCounter = 0, prefCounter = 0, homeCounter = 0;
		for (int i = 0; i < depEmployees.size(); i++) {
			Employee tempWorker = depEmployees.get(i);
			int tempHourOffset = Math.min(hoursOffSet, Math.abs(tempWorker.getPrefStartHour().getHour() - defaultStart.getHour()));
			if (newStartHour.equals(defaultStart))
				tempHourOffset = Math.abs(tempWorker.getPrefStartHour().getHour() - defaultStart.getHour());
			int tempBadHourOffSet = ((Math.max(hoursOffSet, Math.abs(tempWorker.getPrefStartHour().getHour() - defaultStart.getHour()))) - tempHourOffset); //the hours that are in his direction but more then he wanted (he wanted 2 am he got 1 am there is 1 bad offset hour
			if (tempWorker.getMyRole().isChangeable() == false) { //role is NOT changeable
				if (tempWorker.getEmployeePreference().equals(employeePreference.NORMAL)) {
					prefCounter++;//NOT changeable but this is what the employee wanted
					numOfGoodHours += 9; //he wanted it to stay like it is so he got all 9 hours good
				}
				else {
					//this employee has to still come at a time different than what he wants
					//because his role is unchangeable (come in at 8:00) and he wants a different time from 8:00
					if (tempWorker.getEmployeePreference().equals(employeePreference.HOME)) {
						//he wants home but cannot have home...
						restCounter ++;//Possible because this department is dynamic
						numOfBadHours += Math.abs(newStartHour.getHour() - defaultStart.getHour()); //the difference wanted new time to 08:00 (default time)  
						numOfRegHours += (9 - (Math.abs(newStartHour.getHour() - defaultStart.getHour())));
					}
					else { //he wants earlier or later but cannot have it... 
						restCounter ++;//Possible because this department is dynamic
						numOfBadHours += tempHourOffset; //gives the smaller (employee wanted and what can happen in real life)
						numOfRegHours += (9 - tempHourOffset);
					}
				}
			}
			else { //role is changeable
				if (tempWorker.getMyRole().isDynamic()) { //if role is dynamic
					if (tempWorker.getEmployeePreference().equals(employeePreference.HOME)) {
						homeCounter++;
						numOfHomeHours += 9; //he can work from home so he got all the hours he wanted..
					}
					else { //all is dynamic - everyone can come whenever they want
						tempHourOffset = Math.abs(tempWorker.getPrefStartHour().getHour() - defaultStart.getHour()); //Difference between what he wanted and what he got
						prefCounter++;
						numOfGoodHours += tempHourOffset; //gives the smaller (employee wanted and what can happen in real life)
						numOfRegHours += (9 - tempHourOffset);
					}
				}
				else { //if role is NOT dynamic
					//then only if his hours fit the synchronized role hours he is more efficient 
					//or if he wants normal and the role, that is synchronized, start time is normal (starts at 8 am)
					if (tempWorker.getEmployeePreference().equals(employeePreference.HOME)) {
						//role is not dynamic but department is..
						//means this guy looks at all his friends working how they want but he needs to comine in at a time he didnt want
						tempHourOffset = Math.abs(newStartHour.getHour() - defaultStart.getHour());
						
						restCounter++;
						numOfBadHours += tempHourOffset; //gives the smaller (employee wanted and what can happen in real life)
						numOfRegHours += (9 - tempHourOffset);
					}
					else if (tempWorker.getMyRole().getStartHour().equals(LocalTime.of(8, 0))) {
						if (tempWorker.getEmployeePreference().equals(employeePreference.NORMAL)) {
							prefCounter++;
							numOfGoodHours += 9; 
						}
						else { //this guy has to start at 8 but does not want to
							restCounter++;
							numOfBadHours += tempHourOffset; //gives the smaller (employee wanted and what can happen in real life)
							numOfRegHours += (9 - tempHourOffset);
						}
					}
					else if (tempWorker.getMyRole().getStartHour().equals(tempWorker.getPrefStartHour())) {
						//employee preference is equal to role new starting hour (synced role)
						//check if it what employee wanted		
						if ((newStartHour.isAfter(defaultStart) && tempWorker.getEmployeePreference().equals(employeePreference.LATER)) || (newStartHour.isBefore(defaultStart) && tempWorker.getEmployeePreference().equals(employeePreference.EARLIER))) { 
							//later good or earlier good
							prefCounter++;
							numOfGoodHours += tempHourOffset; //gives the smaller (employee wanted and what can happen in real life)
							numOfRegHours += (9 - tempHourOffset - tempBadHourOffSet);
							numOfBadHours += tempBadHourOffSet;
						}
						else if (tempWorker.getEmployeePreference().equals(employeePreference.NORMAL)) {
								prefCounter++;
								numOfGoodHours += 9; 
						}
						else {
							//tempHourOffset = Math.abs(tempWorker.getMyDepartment().getStartHour().getHour() - tempWorker.getPrefStartHour().getHour());
							if (tempWorker.getEmployeePreference().equals(employeePreference.HOME)) { //cant have home
								tempHourOffset = Math.abs(newStartHour.getHour() - defaultStart.getHour());
							}
							// not what they wanted (normal)							
							restCounter++;
							numOfBadHours += tempHourOffset; //gives the smaller (employee wanted and what can happen in real life)
							numOfRegHours += (9 - tempHourOffset);
						}
					}
					else {
						// they have to come in at a time that is not what they wanted
						tempHourOffset = Math.abs(tempWorker.getMyRole().getStartHour().getHour() - tempWorker.getPrefStartHour().getHour());
						restCounter++;
						numOfBadHours += tempHourOffset; //gives the smaller (employee wanted and what can happen in real life)
 						numOfRegHours += (9 - tempHourOffset);
					}
				}
			}
		}
		
		if ((prefCounter + restCounter + homeCounter) != depEmployees.size()) {
			//if its not the same then the algorithm is missing
			throw new Exception("WE Counted: " + (prefCounter + restCounter + homeCounter) +
					" and there are supposed to be: " + depEmployees.size());
		}
		if ((numOfGoodHours + numOfBadHours + numOfHomeHours + numOfRegHours) != (depEmployees.size()*9)) {
			throw new Exception("WE Counted Hours: " + (numOfGoodHours + numOfBadHours + numOfHomeHours + numOfRegHours) +
					" and there are supposed to be: " + (depEmployees.size() * 9));
		}
//		System.out.println("prefCounter  " + prefCounter);
//		System.out.println("restCounter  " + restCounter);
//		System.out.println("homeCounter  " + homeCounter);
//		System.out.println("hoursOffSet  " + hoursOffSet);
		if (numOfBadHours != 0 && restCounter == 0)
			restCounter = 1;
		
		double good = (prefCounter * numOfGoodHours * 0.2);
		good += (homeCounter * numOfHomeHours * 0.1);
		double bad =  (restCounter * numOfBadHours * (-0.2));
		double efficiency = good + bad;
		return efficiency;
	}
//	//FOR WHEN THERE IS NO HOUR IMPORTANCE
	
//	private double calcRoleEfficiencyChangeDynamic(LocalTime newStartHour) throws Exception { //Dynamic AND CHANGABLE
//		//calculates the efficiency of all employees in this department in the case that the department is  changeable and Dynamic 
//		LocalTime defaultStart = LocalTime.of(8, 0);
//		int hoursOffSet = Math.abs(newStartHour.getHour() - defaultStart.getHour());
//
//		int restCounter = 0, prefCounter = 0, homeCounter = 0;
//		for (int i = 0; i < depEmployees.size(); i++) {
//			Employee tempWorker = depEmployees.get(i);
//			if (tempWorker.getMyRole().isChangeable() == false) { //role is NOT changeable
//				if (tempWorker.getEmployeePreference().equals(employeePreference.NORMAL))
//					prefCounter++;//NOT changeable but this is what the employee wanted
//				else
//					restCounter ++;//Possible because this role is dynamic
//			}
//			else { //role is changeable
//				if (tempWorker.getMyRole().isDynamic()) { //if role is dynamic
//					if (tempWorker.getEmployeePreference().equals(employeePreference.HOME))
//						homeCounter++;
//					else
//						prefCounter++;
//				}
//				else { //if department is NOT dynamic
//					//then only if his hours fit the synchronized departments hours he is more efficient 
//					//or if he wants normal and the role, that is synchronized, start time is normal (starts at 8 am)
//					if (tempWorker.getEmployeePreference().equals(employeePreference.NORMAL) && tempWorker.getMyRole().getStartHour().equals(LocalTime.of(8, 0)))
//						prefCounter++;
//					else if (tempWorker.getMyRole().getStartHour().equals(newStartHour))
//						prefCounter++;
//					else
//						restCounter++;
//				}
//			}
//		}
//		
//		if ((prefCounter + restCounter + homeCounter) != depEmployees.size()) {
//			//if its not the same then the algorithm is missing
//			throw new Exception("WE Counted: " + (prefCounter + restCounter + homeCounter) +
//					" and there are supposed to be: " + depEmployees.size());
//		}
////		System.out.println("prefCounter  " + prefCounter);
////		System.out.println("restCounter  " + restCounter);
////		System.out.println("homeCounter  " + homeCounter);
////		System.out.println("hoursOffSet  " + hoursOffSet);
//		double good = (prefCounter * hoursOffSet * 0.2);
//		good += (homeCounter * 9 * 0.1);
//		double bad =  (restCounter * hoursOffSet * (-0.2));
//		double efficiency = good + bad;
//		return efficiency;
//	}
	
	private double calcDepEfficiencyChangeSync(LocalTime newStartHour) throws Exception { //SYNC AND CHANGABLE
		//calculates the efficiency of all employees in this department in the case that the department is  changeable but synchronized 
		//in case that role don't allow that then throw error
		LocalTime defaultStart = LocalTime.of(8, 0);
		double newEffi = 0;
		int hoursOffSet = Math.abs(newStartHour.getHour() - defaultStart.getHour());
		int numOfGoodHours = 0, numOfBadHours = 0, numOfRegHours = 0;

		int restCounter = 0, prefCounter = 0;
		for (int i = 0; i < depEmployees.size(); i++) {
			Employee tempWorker = depEmployees.get(i);
			int tempHourOffset = Math.min(hoursOffSet, Math.abs(tempWorker.getPrefStartHour().getHour() - defaultStart.getHour()));
			if (newStartHour.equals(defaultStart))
				tempHourOffset = Math.abs(tempWorker.getPrefStartHour().getHour() - defaultStart.getHour());
			int tempBadHourOffSet = ((Math.max(hoursOffSet, Math.abs(tempWorker.getPrefStartHour().getHour() - defaultStart.getHour()))) - tempHourOffset); //the hours that are in his direction but more then he wanted (he wanted 2 am he got 1 am there is 1 bad offset hour

			if (tempWorker.getMyRole().isChangeable() == false) {
				//if its not changeable
				if (newStartHour.equals(defaultStart)) { 
					//if the new sync hour is normal hours (from 8:00)
					if (tempWorker.getEmployeePreference().equals(employeePreference.NORMAL)) {
						//normal is good because not changeable and yet still want at 8:00
						prefCounter++;
						numOfGoodHours += 9;
					}
					else {
						restCounter++;
						numOfBadHours += tempHourOffset; //gives the smaller (employee wanted and what can happen in real life)
 						numOfRegHours += (9 - tempHourOffset);
					}
				}
				else {
					//not good he wanted something else and cant have it because department is unchangeable
					//its not even bad hours because we simply can not allow this to be
					throw new Exception("Can't Change to this hour because the:"
							+ tempWorker.getMyRole().getRoleName() + " role\nis NOT changeable"
							+ " and needs this department to be in at: " + tempWorker.getMyRole().getStartHour());

				}
			}
			else {
				//role is changeable
				if (tempWorker.getMyRole().isDynamic()) {
					//if the role is changeable and dynamic then should be allowed no matter what 
					//need to check inside department
					if ((newStartHour.equals(defaultStart)) && tempWorker.getEmployeePreference().equals(employeePreference.NORMAL)) {
						//if its sync to normal and that is what he wants
						prefCounter++;
						numOfGoodHours += 9;
					}
					else if ((newStartHour.isAfter(defaultStart) && tempWorker.getEmployeePreference().equals(employeePreference.LATER)) || (newStartHour.isBefore(defaultStart) && tempWorker.getEmployeePreference().equals(employeePreference.EARLIER))) { 
						//later good or earlier good
						prefCounter++;
						numOfGoodHours += tempHourOffset; //gives the smaller (employee wanted and what can happen in real life)
						numOfRegHours += (9 - tempHourOffset - tempBadHourOffSet);
						numOfBadHours += tempBadHourOffSet;
					}
					else {
						//bad or home
						if (tempWorker.getEmployeePreference().equals(employeePreference.HOME)) {
							//he cannot work from home in sync department
							tempHourOffset = Math.abs(defaultStart.getHour() - newStartHour.getHour());
						}
						restCounter++;
						numOfBadHours += tempHourOffset; //gives the smaller (employee wanted and what can happen in real life)
 						numOfRegHours += (9 - tempHourOffset); 
					}
				}
				else {
					//in this case the role is changeable but not dynamic so lets check if hours are the same
					if (tempWorker.getMyRole().getStartHour().equals(newStartHour)) {
						//if the sync hour of the role equals the desired new start hour of the department
						//then all is good
						//just need to check per employee  
						if ((newStartHour.equals(defaultStart)) && tempWorker.getEmployeePreference().equals(employeePreference.NORMAL)) {
							//if its sync to normal and that is what he wants
							prefCounter++;
							numOfGoodHours += 9;
						}
						else if ((newStartHour.isAfter(defaultStart) && tempWorker.getEmployeePreference().equals(employeePreference.LATER)) || (newStartHour.isBefore(defaultStart) && tempWorker.getEmployeePreference().equals(employeePreference.EARLIER))) { 
							//later good or earlier good
							prefCounter++;
							numOfGoodHours += tempHourOffset; //gives the smaller (employee wanted and what can happen in real life)
							numOfRegHours += (9 - tempHourOffset - tempBadHourOffSet);
							numOfBadHours += tempBadHourOffSet;
						}
						else {
							//bad or home
							if (tempWorker.getEmployeePreference().equals(employeePreference.HOME)) {
								//he cannot work from home in sync role
								tempHourOffset = Math.abs(defaultStart.getHour() - newStartHour.getHour());
							}
							restCounter++;
							numOfBadHours += tempHourOffset; //gives the smaller (employee wanted and what can happen in real life)
	 						numOfRegHours += (9 - tempHourOffset); 
						}
					}
					else {
						//this is not only bad but we can not allow this situation to happen
						throw new Exception("Can't Change this Department because the:"
								+ tempWorker.getMyRole().getRoleName() + " role\n"
								+ "needs this department to be Synchorized at: " + tempWorker.getMyRole().getStartHour());
					}
				}
			}
		}
		
		if ((prefCounter + restCounter) != depEmployees.size()) {
			//if its not the same then the algorithm is missing
			throw new Exception("WE Counted: " + (prefCounter + restCounter ) +
					" and there are supposed to be: " + depEmployees.size());
		}
		if ((numOfGoodHours + numOfBadHours + numOfRegHours) != (depEmployees.size()*9)) {
			throw new Exception("WE Counted Hours: " + (numOfGoodHours + numOfBadHours + numOfRegHours) +
					" and there are supposed to be: " + (depEmployees.size() * 9));
		}
		
		if (numOfBadHours != 0 && restCounter == 0)
			restCounter = 1;
		
		//System.out.println("goodToT: " + numOfGoodHours + " goodCounter: " + prefCounter);
		//System.out.println("badToT: " + numOfBadHours+ " badCounter: " + restCounter);
		double good = (prefCounter * numOfGoodHours * 0.2);
		double bad =  (restCounter * numOfBadHours * (-0.2));
		newEffi = good + bad;
		return newEffi;
	}
//	//FOR WHEN HOURS ARE NOT HERE
//	
//	private double calcRoleEfficiencyChangeSync(LocalTime newStartHour) throws Exception { //SYNC AND CHANGABLE
//		//calculates the efficiency of all employees in this department in the case that the department is  changeable but synchronized 
//		//in case that role don't allow that then throw error
//		LocalTime defaultStart = LocalTime.of(8, 0);
//		double initialSumHours = (depEmployees.size() * 9), newSumHours = 0, newEffi = initialSumHours;
//		int hoursOffSet = Math.abs(newStartHour.getHour() - defaultStart.getHour());
//		Employee.employeePreference empPref;
//		
//		//set variables 
//		if (newStartHour.equals(defaultStart)) { 
//			hoursOffSet = 0; //exactly like it is now
//			empPref = employeePreference.NORMAL;
//		}
//		else if (newStartHour.isAfter(defaultStart))
//			empPref = employeePreference.LATER;
//		else 
//			empPref = employeePreference.EARLIER;
//		//home can't happen if not dynamic...
//
//		int restCounter = 0, prefCounter = 0;
//		for (int i = 0; i < depEmployees.size(); i++) {
//			Employee tempWorker = depEmployees.get(i);
//			if (tempWorker.getMyRole().isChangeable() || (empPref.equals(employeePreference.NORMAL) && tempWorker.getEmployeePreference().equals(employeePreference.NORMAL))) { 
//				//if role of this employee is not changeable then nothing to continue with here unless the preference is normal 
//				//if the role is changeable the good but also if it is not changeable but the employee want it stay normal and the role is normal good
//				if (tempWorker.getMyRole().isDynamic() == false) { //if the role is a synchronized department
//					//flag = false;//if its a synchronized role and hours are not same as here then this employee cannot enjoy new hours
//					if (!tempWorker.getMyRole().getStartHour().equals(newStartHour)) { //check if its hours are synchronized to the new hour
//						//if it is a synchronized role and hours are same as wanted in this role then good
//						throw new Exception("Can't Change to this hour because his:\n"
//								+ tempWorker.getMyRole().getRoleName() + " role\n"
//								+ "needs him to be Synchorized at: " + tempWorker.getMyRole().getStartHour());
//					}
//				}
//				if (tempWorker.getEmployeePreference().equals(empPref)) 
//					prefCounter++;
//				else
//					restCounter++;
//			}
//			else { //if the role there is a role that is not changeable and needs this department in at a certain time
//				throw new Exception("Can't Change this department because the:\n"
//						+ tempWorker.getMyRole().getRoleName() + " role \nis NOT changeable"
//						+ " and needs this person (from this department) to be in at: " + tempWorker.getMyRole().getStartHour());
//			}
//		}
//		
//		double good = (prefCounter * hoursOffSet * 0.2);
//		double bad =  (restCounter * hoursOffSet * (-0.2));
//		newEffi = good + bad;
//		return newEffi;
//	}
//	
	public boolean containsEmployee(Employee temp) {
		//check if contains an employee
		for (int i = 0; i < depEmployees.size(); i++) {
			if (depEmployees.get(i).equals(temp))
				return true;
		}
		return false;
	}
//	
//	public boolean containsRole(Role temp) {
//		//check if contains an employee
//		for (int i = 0; i < depRoles.size(); i++) {
//			if (depRoles.get(i).equals(temp))
//				return true;
//		}
//		return false;
//	}
	
	@Override
	public boolean equals(Object obj) {
		//check by name (name should be unique)
		if (!(obj instanceof Department))
				return false;
		Department tempDep = (Department)obj;
		if (depName.equals(tempDep.depName))
			return true;
		return false;
	}

	
	public String toString() {
		String msg = "Department:                " + depName;
		msg += "\n    Changable:              " + isChangeable;
		msg += "\n    Dynamic:                  " + isDynamic;
		msg += "\n    Starting Hour:          " + startHour;
		msg += "\n    Employees[" + depEmployees.size() + "]:\n";
		for (int i = 0; i < depEmployees.size(); i++) {
			msg += (i + 1) + ") " + depEmployees.get(i).toString();
		}
		return msg;
	}
	
	public Department clone() throws CloneNotSupportedException {
		//clone for working on temp in view
		Department temp = (Department)super.clone();
		temp.depEmployees = depEmployees.clone();
		//temp.depRoles = (ArrayList<Role>)depRoles.clone(); 
		return temp;
	}
}
