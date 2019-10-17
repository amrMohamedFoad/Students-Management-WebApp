package com.amr.web.jdbc;

public class Student {

	// Fields
	// ------
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	
	
	// Constructors
	// ------------
	
	public Student(int id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
	    this.lastName = lastName;
	    this.email = email;
	}
	
	public Student(String firstName, String lastName, String email) {
		this.firstName = firstName;
	    this.lastName = lastName;
	    this.email = email;
	}
	
	
	// Getter and Setter
	// -----------------
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	
    // Methods
	// -------
	
	@Override
	public String toString() {
		return "Student [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}
	
	
	
}
