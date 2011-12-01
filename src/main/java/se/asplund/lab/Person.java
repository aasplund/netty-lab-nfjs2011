package se.asplund.lab;

import java.io.Serializable;

public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final String firstName;
	public final String lastName;
	public final int age;
	private boolean approved;

	public Person(String firstName, String lastName, int age) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
	}
	
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	
	public boolean isApproved() {
		return approved;
	}
	
}
