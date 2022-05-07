package Classes;

import java.io.Serializable;
import java.util.ArrayList;

import Emloyee.Employee;

public class OurSet<E> implements Serializable, Cloneable {
	private ArrayList<E> ourSetCollection;
	private int numOfOurSetCollection;
	
	public OurSet() {
		ourSetCollection = new ArrayList<E>();
		numOfOurSetCollection = 0;
	}
	public void add(E elementToAdd) throws Exception{ 
		//input - the element we want to add
		//output - we add the element if it does'nt already exist
		if (ourSetCollection.size() != 0 && ourSetCollection.contains(elementToAdd))  {
			throw new Exception ("This " +  elementToAdd.getClass().getSimpleName() + " is already registered");
		}
		ourSetCollection.add(elementToAdd);
		numOfOurSetCollection++;
	}
	public void remove(E elementToRemove) {
		//input - the element we want to remove
		//output - we remove that element if it exist
		if (ourSetCollection.contains(elementToRemove)) {
			ourSetCollection.remove(elementToRemove);
		}
		//ourSetCollection.remove(elementToRemove);
//		else
//			System.out.println("SHIT");
	}
	public E get(int index) {
		//get the element at given index
		return ourSetCollection.get(index);
	}
	
	public boolean containsByClass(String className) {
		for (int i = 0; i < ourSetCollection.size(); i++) {
			if (ourSetCollection.get(i).getClass().getSimpleName().equals(className))
				return true;
		}
		return false;
	}
	
	public E getByName(String name) throws Exception {
		for (int i = 0; i < ourSetCollection.size(); i++) {
			if (ourSetCollection.get(i) instanceof Role && ourSetCollection.get(i) != null) {
				Role temp = (Role)ourSetCollection.get(i);
				if (temp.getRoleName().equals(name))
					return ourSetCollection.get(i);
			}
			if (ourSetCollection.get(i) instanceof Department && ourSetCollection.get(i) != null) {
				Department temp = (Department)ourSetCollection.get(i);
				if (temp.getDepName().equals(name))
					return ourSetCollection.get(i);
			}
			if (ourSetCollection.get(i) instanceof Employee && ourSetCollection.get(i) != null) {
				Employee temp = (Employee)ourSetCollection.get(i);
				if (temp.getId().equals(name))
					return ourSetCollection.get(i);
			}
		}
		throw new Exception("*Error" /*(Role/Department/Employee)*/ + " data is not valid");
		//return null;
	}
	
	public int size() {
		return ourSetCollection.size();
	}
	
	@Override
	public OurSet<E> clone() throws CloneNotSupportedException {
		OurSet<E> temp = (OurSet<E>)super.clone();
		temp.ourSetCollection = (ArrayList<E>)ourSetCollection.clone();
		return temp;
	}
	
	@Override
	public String toString() {
		String msg = "";
		if (ourSetCollection.size() != 0) {
			for (int i = 0; i < ourSetCollection.size(); i++) {
				msg += ourSetCollection.get(i).toString() + "\n";
				if (i == (ourSetCollection.size() - 1))
					msg += "\n";
			}
		}
		return msg;
	}
	
}
