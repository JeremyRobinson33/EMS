package cognixia.jump.project;

import java.io.Serializable;

/*
 * Class Purpose - utility class (no main method)
 * 		used to stream Employees and the attributes that describe an Employee object
 * 
 * Make the attributes, getters and setters, and override the .toString() method
 * 
 * Can file / code to class, no need to live code.
 */

public class Employee extends Person implements Serializable {

	private static int idCounter = 1;

	private int id;
	private int salary;
	private String department;

	public Employee(String name, int salary, String department) {
		super();
		this.name = name;
		this.salary = salary;
		this.department = department;
		this.id = idCounter++;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", salary=" + salary + ", department=" + department + "]";
	}
}