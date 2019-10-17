package com.amr.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private StudentDBUtil studentDbUtil;
    

    //Resource injection to servlet "inject resource to dataSource object in next line"
    @Resource(name="jdbc/web_student_tracker") // got name form context.xml file
    private DataSource dataSource; // connection pool
    
    
    // a method that in servlet life cycle 
    // it calls when server iniate servlet
    @Override
	public void init() throws ServletException {
		super.init();
		
		try {
			studentDbUtil = new StudentDBUtil(dataSource);
			
		}catch(Exception exc) {
			throw new ServletException(exc);
		}
	}
    
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			//Read command
			String theCommand = request.getParameter("command");
			
			if (theCommand == null) {
				theCommand = "LIST";
			}
			
			switch(theCommand) {
			case "LIST":
			    listStudents(request, response); 
			    break;
			case "ADD":
				addStudent(request, response);
				break;
			case "UPDATE":
				updateStudent(request, response);
				break;
			case "LOAD":
				loadStudent(request, response);
				break;
			case "DELETE":
				deleteStudent(request, response);
				break;
			case "SEARCH":
                searchStudents(request, response);
                break;
			default:
			    listStudents(request, response); 
			}
		   
		}
		catch(Exception exc) {
			throw new ServletException(exc);
		}
	}



	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		// get students
		List<Student> studentsList = studentDbUtil.getStudents();
			
		// put them in request object
		request.setAttribute("STUDENTS_LIST", studentsList);
			
		//forward to jsp file
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}
	
	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//get values
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		//create student object
		Student theStudent = new Student(firstName, lastName, email);
		
		
		//Add Student to Database
		studentDbUtil.addStudent(theStudent);
		
		
		listStudents(request, response);
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String studentId = request.getParameter("studentId");
		
		Student theStudent = studentDbUtil.getStudent(studentId);
		
		request.setAttribute("THE_STUDENT", theStudent);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}
	
	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//get values
	    int id = Integer.parseInt(request.getParameter("studentId"));
	    
	    String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
				
		
		//create student object
		Student theStudent = new Student(id, firstName, lastName, email);
		
		
		//update Student in Database
		studentDbUtil.updateStudent(theStudent);
		
		
		listStudents(request, response);
	}
	
	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		int studentId = Integer.parseInt(request.getParameter("studentId"));
		
		studentDbUtil.deleteStudent(studentId);
		
		listStudents(request, response);
	}
	
	private void searchStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // read search name from form data
        String theSearchName = request.getParameter("theSearchName");
        
        // search students from db util
        List<Student> students = studentDbUtil.searchStudents(theSearchName);
        
        // add students to the request
        request.setAttribute("STUDENTS_LIST", students);
                
        // send to JSP page (view)
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
        dispatcher.forward(request, response);
    }

	

	

}
