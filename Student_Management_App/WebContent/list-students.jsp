<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Students Management App</title>

<link type="text/css" rel="stylesheet" href="css/style.css">
</head>


<body>

  <div id="wrapper">
    <div id="header">
       <h2>FooBar University</h2>
    </div>
  </div>


  <div id="container">
    <div id="content">
    
    	<!-- put new button: Add Student -->
			
			<input type="button" value="Add Student" 
				   onclick="window.location.href='add-student-form.jsp'; return false;"
				   class="add-student-button"
			/>
			
			  <!--  add a search box -->
            <form action="StudentControllerServlet" method="GET">
        
                <input type="hidden" name="command" value="SEARCH" />
            
                Search student (by first-name or last-name): <input type="text" name="theSearchName" />
                
                <input type="submit" value="Search" class="add-student-button" />
            
            </form>
            
       <table>
         <tr>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Email</th>
            <th>Action</th>
         </tr>
         
         <c:forEach var="tempStudent" items="${STUDENTS_LIST}">
         
          <c:url var="tempLink" value="StudentControllerServlet">
           <c:param name="command" value="LOAD"></c:param>
           <c:param name="studentId" value="${tempStudent.id}"></c:param>
          </c:url>
          
           <c:url var="tempLink2" value="StudentControllerServlet">
           <c:param name="command" value="DELETE"></c:param>
           <c:param name="studentId" value="${tempStudent.id}"></c:param>
          </c:url>
          
          
            <tr>
            	<td>${tempStudent.firstName}</td>
            	<td>${tempStudent.lastName}</td>
                <td>${tempStudent.email}</td>
                <td><a href="${tempLink}">Update</a> | 
                    <a href="${tempLink2}" onclick="if(!(confirm('Delete this student ?'))) return false">Delete</a>
                </td>
            </tr>
         </c:forEach>
         
       </table>
    </div>
  </div>

</body>
</html>