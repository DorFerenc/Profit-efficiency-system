package Classes;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.regex.Pattern;

import Emloyee.Employee;
import Emloyee.Employee.employeePreference;

public class Role implements Serializable, Changeable, Synchronizedable, Cloneable{
	
	private String roleName;
	//private boolean isSalesRole; //TODO this is to able to calculate salary with monthly sales bonus
	private boolean isChangeable;
	private boolean isDynamic;
	private LocalTime startHour;
	private OurSet<Employee> roleEmployees;
	
	public Role(String roleName1, boolean isChangeable, boolean isDynamic) throws Exception {
		setRoleName(roleName1);
		//this.isSalesRole = isSalesRole;
		this.isChangeable = isChangeable;
		this.isDynamic = isDynamic;
		this.startHour = LocalTime.of(8, 0); //default start time is 8 AM
		roleEmployees = new OurSet<Employee>();
	}
	
	public void addEmployee(Employee newWorker) throws Exception {
		//add employee to this role make sure no double adding
		roleEmployees.add(newWorker);
	}
	
	public void removeEmployee(Employee oldWorker) throws Exception {
		//removes employee from this role - if he changes roles
		roleEmployees.remove(oldWorker);
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
	

	private void setRoleName(String roleName1) throws Exception {
		if (checkName(roleName1))
			this.roleName = roleName1.toLowerCase();
		else
			throw new Exception("bad name input");
	}
	
	public void setStartHour(LocalTime startHour) {
		this.startHour = startHour;
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
	
	public OurSet<Employee> getRoleEmployees() {
		return roleEmployees;
	}

	public LocalTime getStartHour() {
		return startHour;
	}

	public String getRoleName() {
		return roleName;
	}
	
	public String calcEfficiency(LocalTime newStartHour) throws Exception {
		double efficiency;
		if (this.isChangeable() == false)
			efficiency = calcRoleEfficiencyUnChangeable(newStartHour);
		else if (this.isDynamic)
			efficiency = calcRoleEfficiencyChangeDynamic(newStartHour);
		else
			efficiency = calcRoleEfficiencyChangeSync(newStartHour);
		DecimalFormat numFormat = new DecimalFormat("#.##");
		String msg = "Role:                            " + roleName;
		msg += "\n    Changable:              " + isChangeable;
		msg += "\n    Dynamic:                  " + isDynamic;
		msg += "\n    Starting Hour:          " + startHour;
		msg += "\n    Efficiency change:    " + numFormat.format(efficiency);
		return msg;
	}
	
	public double calcEfficiency2(LocalTime newStartHour) throws Exception {
		double efficiency;
		if (this.isChangeable() == false)
			efficiency = calcRoleEfficiencyUnChangeable(newStartHour);
		else if (this.isDynamic)
			efficiency = calcRoleEfficiencyChangeDynamic(newStartHour);
		else
			efficiency = calcRoleEfficiencyChangeSync(newStartHour);
		return efficiency;
	}
	
	private double calcRoleEfficiencyUnChangeable(LocalTime newStartHour) throws Exception {
		
		LocalTime defaultStart = LocalTime.of(8, 0);
		int hoursOffSet = Math.abs(newStartHour.getHour() - defaultStart.getHour()); // when unchanged the offset is always just the difference from 8
		int numOfGoodHours = 0, numOfHomeHours = 0, numOfBadHours = 0, numOfRegHours = 0;

		int restCounter = 0, prefCounter = 0;
		for (int i = 0; i < roleEmployees.size(); i++) {
			Employee tempWorker = roleEmployees.get(i);
			int tempHourOffset = Math.min(hoursOffSet, Math.abs(tempWorker.getPrefStartHour().getHour() - defaultStart.getHour()));
			if (tempWorker.getMyDepartment().isDynamic() == false && tempWorker.getMyDepartment().getStartHour() != defaultStart) {
				throw new Exception("Cannot change to Unchangeable because: " + tempWorker.getMyDepartment().getDepName() + "\n is synced to: " + tempWorker.getMyDepartment().getStartHour());
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
		if ((prefCounter + restCounter) != roleEmployees.size()) {
			//if its not the same then the algorithm is missing
			throw new Exception("WE Counted: " + (prefCounter + restCounter) +
					" and there are supposed to be: " + roleEmployees.size());
		}
		if ((numOfGoodHours + numOfBadHours + numOfHomeHours + numOfRegHours) != (roleEmployees.size()*9)) {
			//if its not them same time then algorithm is bad
			throw new Exception("WE Counted Hours: " + (numOfGoodHours + numOfBadHours + numOfHomeHours + numOfRegHours) +
					" and there are supposed to be: " + (roleEmployees.size() * 9));
		}
		
		double good = (prefCounter * numOfGoodHours * 0.2); //num of workers * num of good hours * +0.2
		double bad =  (restCounter * numOfBadHours * (-0.2)); //num of workers * num of bad hours * -0.2
		double efficiency = good + bad;
		return efficiency;
	}
	
	private double calcRoleEfficiencyChangeDynamic(LocalTime newStartHour) throws Exception { //Dynamic AND CHANGABLE
		//calculates the efficiency of all employees in this role in the case that the role is  changeable and Dynamic 
		LocalTime defaultStart = LocalTime.of(8, 0);
		int hoursOffSet = Math.abs(newStartHour.getHour() - defaultStart.getHour());
		int numOfGoodHours = 0, numOfHomeHours = 0, numOfBadHours = 0, numOfRegHours = 0;
		int restCounter = 0, prefCounter = 0, homeCounter = 0;
		for (int i = 0; i < roleEmployees.size(); i++) {
			Employee tempWorker = roleEmployees.get(i);
			int tempHourOffset = Math.min(hoursOffSet, Math.abs(tempWorker.getPrefStartHour().getHour() - defaultStart.getHour()));
			if (newStartHour.equals(defaultStart))
				tempHourOffset = Math.abs(tempWorker.getPrefStartHour().getHour() - defaultStart.getHour());
			int tempBadHourOffSet = ((Math.max(hoursOffSet, Math.abs(tempWorker.getPrefStartHour().getHour() - defaultStart.getHour()))) - tempHourOffset); //the hours that are in his direction but more then he wanted (he wanted 2 am he got 1 am there is 1 bad offset hour
			if (tempWorker.getMyDepartment().isChangeable() == false) { //department is NOT changeable
				if (tempWorker.getEmployeePreference().equals(employeePreference.NORMAL)) {
					prefCounter++;//NOT changeable but this is what the employee wanted
					numOfGoodHours += 9; //he wanted it to stay like it is so he got all 9 hours good
				}
				else {
					//this employee has to still come at a time different than what he wants
					//because his department is unchangeable (come in at 8:00) and he wants a different time from 8:00
					if (tempWorker.getEmployeePreference().equals(employeePreference.HOME)) {
						//he wants home but cannot have home...
						restCounter ++;//Possible because this role is dynamic
						numOfBadHours += Math.abs(newStartHour.getHour() - defaultStart.getHour()); //the difference wanted new time to 08:00 (default time)  
						numOfRegHours += (9 - (Math.abs(newStartHour.getHour() - defaultStart.getHour())));
					}
					else { //he wants earlier or later but cannot have it... 
						restCounter ++;//Possible because this role is dynamic
						numOfBadHours += tempHourOffset; //gives the smaller (employee wanted and what can happen in real life)
						numOfRegHours += (9 - tempHourOffset);
					}
				}
			}
			else { //department is changeable
				if (tempWorker.getMyDepartment().isDynamic()) { //if department is dynamic
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
				else { //if department is NOT dynamic
					//then only if his hours fit the synchronized departments hours he is more efficient 
					//or if he wants normal and the department, that is synchronized, start time is normal (starts at 8 am)
					if (tempWorker.getEmployeePreference().equals(employeePreference.HOME)) {
						//department is not dynamic but role is..
						//means this guy looks at all his friends working how they want but he needs to come in at a time he didnt want
						tempHourOffset = Math.abs(newStartHour.getHour() - defaultStart.getHour());
						
						restCounter++;
						numOfBadHours += tempHourOffset; //gives the smaller (employee wanted and what can happen in real life)
						numOfRegHours += (9 - tempHourOffset);
					}
					else if (tempWorker.getMyDepartment().getStartHour().equals(LocalTime.of(8, 0))) {
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
					else if (tempWorker.getMyDepartment().getStartHour().equals(tempWorker.getPrefStartHour())) {
						//employee preference is equal to department new starting hour (synced department)
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
						tempHourOffset = Math.abs(tempWorker.getMyDepartment().getStartHour().getHour() - tempWorker.getPrefStartHour().getHour());
						restCounter++;
						numOfBadHours += tempHourOffset; //gives the smaller (employee wanted and what can happen in real life)
 						numOfRegHours += (9 - tempHourOffset);
					}
				}
			}
		}
		
		if ((prefCounter + restCounter + homeCounter) != roleEmployees.size()) {
			//if its not the same then the algorithm is missing
			throw new Exception("WE Counted: " + (prefCounter + restCounter + homeCounter) +
					" and there are supposed to be: " + roleEmployees.size());
		}
		if ((numOfGoodHours + numOfBadHours + numOfHomeHours + numOfRegHours) != (roleEmployees.size()*9)) {
			throw new Exception("WE Counted Hours: " + (numOfGoodHours + numOfBadHours + numOfHomeHours + numOfRegHours) +
					" and there are supposed to be: " + (roleEmployees.size() * 9));
		}
//		System.out.println("prefCounter  " + prefCounter);
//		System.out.println("restCounter  " + restCounter);
//		System.out.println("homeCounter  " + homeCounter);
//		System.out.println("hoursOffSet  " + hoursOffSet);
		if (numOfBadHours != 0 && restCounter == 0)
			restCounter = 1;
		
		//System.out.println("goodToT: " + numOfGoodHours + " goodCounter: " + prefCounter);
		//System.out.println("badToT: " + numOfBadHours+ " badCounter: " + restCounter);
		
		double good = (prefCounter * numOfGoodHours * 0.2);
		good += (homeCounter * numOfHomeHours * 0.1);
		double bad =  (restCounter * numOfBadHours * (-0.2));
		double efficiency = good + bad;
		return efficiency;
	}
//	//FOR WHEN THERE IS NO HOUR IMPORTANCE
//	private double calcRoleEfficiencyChangeDynamic(LocalTime newStartHour) throws Exception { //Dynamic AND CHANGABLE
//		//calculates the efficiency of all employees in this role in the case that the role is  changeable and Dynamic 
//		LocalTime defaultStart = LocalTime.of(8, 0);
//		int hoursOffSet = Math.abs(newStartHour.getHour() - defaultStart.getHour());
//
//		int restCounter = 0, prefCounter = 0, homeCounter = 0;
//		for (int i = 0; i < roleEmployees.size(); i++) {
//			Employee tempWorker = roleEmployees.get(i);
//			if (tempWorker.getMyDepartment().isChangeable() == false) { //department is NOT changeable
//				if (tempWorker.getEmployeePreference().equals(employeePreference.NORMAL))
//					prefCounter++;//NOT changeable but this is what the employee wanted
//				else
//					restCounter ++;//Possible because this role is dynamic
//			}
//			else { //department is changeable
//				if (tempWorker.getMyDepartment().isDynamic()) { //if department is dynamic
//					if (tempWorker.getEmployeePreference().equals(employeePreference.HOME))
//						homeCounter++;
//					else
//						prefCounter++;
//				}
//				else { //if department is NOT dynamic
//					//then only if his hours fit the synchronized departments hours he is more efficient 
//					//or if he wants normal and the department, that is synchronized, start time is normal (starts at 8 am)
//					if (tempWorker.getEmployeePreference().equals(employeePreference.NORMAL) && tempWorker.getMyDepartment().getStartHour().equals(LocalTime.of(8, 0)))
//						prefCounter++;
//					else if (tempWorker.getMyDepartment().getStartHour().equals(newStartHour))
//						prefCounter++;
//					else
//						restCounter++;
//				}
//			}
//		}
//		
//		if ((prefCounter + restCounter + homeCounter) != roleEmployees.size()) {
//			//if its not the same then the algorithm is missing
//			throw new Exception("WE Counted: " + (prefCounter + restCounter + homeCounter) +
//					" and there are supposed to be: " + roleEmployees.size());
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
	
	
	private double calcRoleEfficiencyChangeSync(LocalTime newStartHour) throws Exception { //SYNC AND CHANGABLE
		//calculates the efficiency of all employees in this role in the case that the role is  changeable but synchronized 
		//in case that departments don't allow that then throw error
		LocalTime defaultStart = LocalTime.of(8, 0);
		double newEffi = 0;
		int hoursOffSet = Math.abs(newStartHour.getHour() - defaultStart.getHour());
		int numOfGoodHours = 0, numOfBadHours = 0, numOfRegHours = 0;

		int restCounter = 0, prefCounter = 0;
		for (int i = 0; i < roleEmployees.size(); i++) {
			Employee tempWorker = roleEmployees.get(i);
			int tempHourOffset = Math.min(hoursOffSet, Math.abs(tempWorker.getPrefStartHour().getHour() - defaultStart.getHour()));
			if (newStartHour.equals(defaultStart))
				tempHourOffset = Math.abs(tempWorker.getPrefStartHour().getHour() - defaultStart.getHour());
			int tempBadHourOffSet = ((Math.max(hoursOffSet, Math.abs(tempWorker.getPrefStartHour().getHour() - defaultStart.getHour()))) - tempHourOffset); //the hours that are in his direction but more then he wanted (he wanted 2 am he got 1 am there is 1 bad offset hour

			if (tempWorker.getMyDepartment().isChangeable() == false) {
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
							+ tempWorker.getMyDepartment().getDepName() + " department\nis NOT changeable"
							+ " and needs this Role to be in at: " + tempWorker.getMyDepartment().getStartHour());

				}
			}
			else {
				//department is changeable
				if (tempWorker.getMyDepartment().isDynamic()) {
					//if the department is changeable and dynamic then should be allowed no matter what 
					//need to check inside role
					if ((newStartHour.equals(defaultStart)) && tempWorker.getEmployeePreference().equals(employeePreference.NORMAL)) {
						//if its sync to normal and that is what he wants
						prefCounter++;
						numOfGoodHours += 9;
					}
					else if ((newStartHour.isAfter(defaultStart) && tempWorker.getEmployeePreference().equals(employeePreference.LATER)) || (newStartHour.isBefore(defaultStart) && tempWorker.getEmployeePreference().equals(employeePreference.EARLIER))) { 
						//later good or earlier good
						//System.out.println("good: " + tempHourOffset);
						//System.out.println("bad: " + tempBadHourOffSet);
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
					//in this case the department is changeable but not dynamic so lets check if hours are the same
					if (tempWorker.getMyDepartment().getStartHour().equals(newStartHour)) {
						//if the sync hour of the department equals the desired new start hour of the role
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
						throw new Exception("Can't Change this Role because the:"
								+ tempWorker.getMyDepartment().getDepName() + " department\n"
								+ "needs this Role to be Synchorized at: " + tempWorker.getMyDepartment().getStartHour());
					}
				}
			}
		}
		
		if ((prefCounter + restCounter) != roleEmployees.size()) {
			//if its not the same then the algorithm is missing
			throw new Exception("WE Counted: " + (prefCounter + restCounter ) +
					" and there are supposed to be: " + roleEmployees.size());
		}
		if ((numOfGoodHours + numOfBadHours + numOfRegHours) != (roleEmployees.size()*9)) {
			throw new Exception("WE Counted Hours: " + (numOfGoodHours + numOfBadHours + numOfRegHours) +
					" and there are supposed to be: " + (roleEmployees.size() * 9));
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
//	//FOR WHEN THERE IS NO HOUR IMPORTANCE
//	private double calcRoleEfficiencyChangeSync(LocalTime newStartHour) throws Exception { //SYNC AND CHANGABLE
//		//calculates the efficiency of all employees in this role in the case that the role is  changeable but synchronized 
//		//in case that departments don't allow that then throw error
//		LocalTime defaultStart = LocalTime.of(8, 0);
//		double initialSumHours = (roleEmployees.size() * 9), newSumHours = 0, newEffi = initialSumHours;
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
//		for (int i = 0; i < roleEmployees.size(); i++) {
//			Employee tempWorker = roleEmployees.get(i);
//			if (tempWorker.getMyDepartment().isChangeable() || (empPref.equals(employeePreference.NORMAL) && tempWorker.getEmployeePreference().equals(employeePreference.NORMAL))) { 
//				//if department of this employee is not changeable then nothing to continue with here unless the preference is normal 
//				//if the department is changeable the good but also if it is not changeable but the employee want it stay normal and the role is normal good
//				if (tempWorker.getMyDepartment().isDynamic() == false) { //if the department is a synchronized department
//					//flag = false;//if its a synchronized department and hours are not same as here then this employee cannot enjoy new hours
//					if (!tempWorker.getMyDepartment().getStartHour().equals(newStartHour)) { //check if its hours are synchronized to the new hour
//						//if it is a synchronized department and hours are same as wanted in this role then good
//						throw new Exception("Can't Change to this hour because the:\n"
//								+ tempWorker.getMyDepartment().getDepName() + " department\n"
//								+ "needs this Role to be Synchorized at: " + tempWorker.getMyDepartment().getStartHour());
//					}
//				}
//				if (tempWorker.getEmployeePreference().equals(empPref)) 
//					prefCounter++;
//				else
//					restCounter++;
//			}
//			else { //if the department there is a department that is not changeable and needs this role in at certain time
//				throw new Exception("Can't Change this Role because the:\n"
//						+ tempWorker.getMyDepartment().getDepName() + " department \nis NOT changeable"
//						+ " and needs this Role to be in at: " + tempWorker.getMyDepartment().getStartHour());
//			}
//		}
//		
//		double good = (prefCounter * hoursOffSet * 0.2);
//		double bad =  (restCounter * hoursOffSet * (-0.2));
//		newEffi = good + bad;
//		return newEffi;
//	}

	public boolean containsEmployee(Employee temp) {
		//check if contains an employee
		for (int i = 0; i < roleEmployees.size(); i++) {
			if (roleEmployees.get(i).equals(temp))
				return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		//check by name (name should be unique)
		if (!(obj instanceof Role))
			return false;
		Role tempRole = (Role)obj;
		if (roleName.equals(tempRole.roleName))
			return true;
		return false;
	}

	public String toString() {
		String msg = "Role:                            " + roleName;
		msg += "\n    Changable:              " + isChangeable;
		msg += "\n    Dynamic:                  " + isDynamic;
		msg += "\n    Starting Hour:          " + startHour;
		msg += "\n    Employees[" + roleEmployees.size() + "]:\n";
		for (int i = 0; i < roleEmployees.size(); i++) {
			msg += (i + 1) + ") " + roleEmployees.get(i).toString();
		}
		return msg;
	}

	public Role clone() throws CloneNotSupportedException {
		//clone for working on temp in view
		Role temp = (Role)super.clone();
		temp.roleEmployees = roleEmployees.clone();
		return temp;
	}
	
}
