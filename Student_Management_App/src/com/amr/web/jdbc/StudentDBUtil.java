package com.amr.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;



public class StudentDBUtil {
	
	// Fields
	// ------
	private DataSource dataSource;
	
	
	// Constructor
	// -----------
	
	public StudentDBUtil(DataSource theDataSource) {
		this.dataSource = theDataSource;
	}
	
	// Methods
	// -------
	
	public List<Student> getStudents() throws Exception{
		
		List<Student> studentsList = new ArrayList<Student>();
		
		// JDBC Objects
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			
			
			// 1- get Connection
			myConn = this.dataSource.getConnection();
			
			// 2- create sql statement
			String sql = "select * from student order by last_name";
			myStmt = myConn.createStatement();
			
			// 3- Ecexute Sql Statement
			myRs = myStmt.executeQuery(sql);
			
			// 4- process result set
			while(myRs.next()) {
				
				// 4.1- retrieve data from that row
				int id = myRs.getInt("id");
			    String firstName = myRs.getString("first_name");
			    String lastName = myRs.getString("last_name");
			    String email = myRs.getString("email");
				
			    // 4.2- combine these data into Student Object
			    Student tempStudent = new Student(id, firstName, lastName, email);
			    
			    // 4.3- add student to the list
			    studentsList.add(tempStudent);
			}
		
			return studentsList;
			
		}finally{
			// close JDBC Objects
			this.close(myConn, myStmt, myRs);
		}
	}
	
	public void addStudent(Student theStudent) throws Exception {
		
		// JDBC Objects
		Connection myConn = null;
		PreparedStatement myStmt = null; // statement that has parameters to set
		
		
		try {
			
			// 1- get Connection
			myConn = this.dataSource.getConnection();
						
			// 2- create sql statement
			String sql = "insert into student" +
			             "(first_name, last_name, email)" +
					     "values(?, ?, ?)";
	
		    //3- prepare statement
			myStmt = myConn.prepareStatement(sql); //prepeare use to prepare sql statement that has parameters
						
			//4- set parameter values
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			
			
			// 5- Ecexute Sql Statement
		    myStmt.execute();
			
			
		}finally {
			this.close(myConn, myStmt, null);
		}
	}
	
	public Student getStudent(String studentId) throws Exception {
		
		//JDBC Objects
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			
			int studId = Integer.parseInt(studentId);
			
			myConn = this.dataSource.getConnection();
			
			String sql = "select * from student where id=?";
			myStmt = myConn.prepareStatement(sql);
			
			myStmt.setInt(1, studId);
			
			myRs = myStmt.executeQuery();
			
			if(myRs.next()) {
				
			   	 int id = Integer.parseInt(myRs.getString("id"));
				 String firstName = myRs.getString("first_name");
				 String lastName = myRs.getString("last_name");
				 String email = myRs.getString("email");
					
				 return new Student(id, firstName, lastName, email);
			}else {
				throw new Exception("couldn't find student with id : " + studId);
			}
			
					
		}finally {
			this.close(myConn, myStmt, myRs);
		}
	
	}
	
	public void updateStudent(Student theStudent) throws Exception{
		
		//JDBC Objects
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			myConn = this.dataSource.getConnection();
			
			String sql = "UPDATE student SET first_name = ?, last_name = ?, email = ? WHERE id = ?";
			
			myStmt = myConn.prepareStatement(sql);
			
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			myStmt.execute();
		}
		finally {
			this.close(myConn, myStmt, null);
		}
	}
	
	public void deleteStudent(int studentId) throws Exception{
		
		//JDBC Objects
				Connection myConn = null;
				PreparedStatement myStmt = null;
				
				try {
					myConn = this.dataSource.getConnection();
					
					String sql = "DELETE FROM student WHERE id = ?";
					
					myStmt = myConn.prepareStatement(sql);
					
					
					myStmt.setInt(1, studentId);
					
					myStmt.execute();
				}
				finally {
					this.close(myConn, myStmt, null);
				}
		
	}

	public List<Student> searchStudents(String theSearchName)  throws Exception {
        List<Student> students = new ArrayList<>();
        
        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        int studentId;
        
        try {
            
            // get connection to database
            myConn = dataSource.getConnection();
            
            //
            // only search by name if theSearchName is not empty
            //
            if (theSearchName != null && theSearchName.trim().length() > 0) {
                // create sql to search for students by name
                String sql = "select * from student where lower(first_name) like ? or lower(last_name) like ?";
                // create prepared statement
                myStmt = myConn.prepareStatement(sql);
                // set params
                String theSearchNameLike = "%" + theSearchName.toLowerCase() + "%";
                myStmt.setString(1, theSearchNameLike);
                myStmt.setString(2, theSearchNameLike);
                
            } else {
                // create sql to get all students
                String sql = "select * from student order by last_name";
                // create prepared statement
                myStmt = myConn.prepareStatement(sql);
            }
            
            // execute statement
            myRs = myStmt.executeQuery();
            
            // retrieve data from result set row
            while (myRs.next()) {
                
                // retrieve data from result set row
                int id = myRs.getInt("id");
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");
                
                // create new student object
                Student tempStudent = new Student(id, firstName, lastName, email);
                
                // add it to the list of students
                students.add(tempStudent);            
            }
            
            return students;
        }
        finally {
            // clean up JDBC objects
            close(myConn, myStmt, myRs);
        }
    }
	
	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		
		try {
			
			if (myRs != null) {
				myRs.close(); 
			}
			
			if (myStmt != null) {
				myStmt.close();
			}
			
			
			if (myConn != null) {
				myConn.close(); // doesn't really close it, just puts back in connection pool for some one to use
			}
			
		}catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	

}
