/*
 * Name:m Eric Anderson
 * Date: 4/18/24
 * Description: This program utilies JDBC and SQL for interacting with databases.
 * I approached this by implementing a switch statment for each of the 6 requrments from the spec
 * Throughout this file I connect to database, then display menu of options for the user to execute
 * The user can keep selecting options until '7' is pressed 
 *  
 * 
 */


package cpi221Assignment5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class anderson_eric_hw5 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		Connection dbConnect = null;
		Statement query = null;
		PreparedStatement prepStmt = null;
		ResultSet results = null;
		Scanner scan = new Scanner(System.in);
		int input;
		
		//database connection setup
		try {
			Class.forName("org.sqlite.JDBC"); //loads the sqlitedriver into JDBC
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dbConnect = DriverManager.getConnection("jdbc:sqlite:classDB.db");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			query = dbConnect.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//end database connection set up

		
		//prints stuff to screen/terminal
		printMenu();
		input = scan.nextInt();
		
		
		while(input != 7) {
			switch(input) {
				
				// display all of the students
				case 1:
					
					String studentListQuery = "SELECT * FROM studentTable";
					try {
						results = query.executeQuery(studentListQuery);
						System.out.printf("%-10s %-12s %s\n", "First", "Last", "ID");
						
						while(results.next()) {
							System.out.printf("%-10s %-12s %s\n",
									results.getString("first_name"),
									results.getString("last_name"),
									results.getString("student_id"));
						}
						System.out.println();
							
					} catch (SQLException e) {
						e.printStackTrace();
					}
					break;
					
				// display all of the students and their grades
				case 2:
					String studentBlockQuery = "SELECT studentTable.first_name,"
							+ " studentTable.last_name,"
							+ " assnTable.assn_num,"
							+ " assnTable.max_points,"
							+ " assnTable.earned_points " +
							"FROM studentTable "+
							"INNER JOIN assnTable ON studentTable.student_id=assnTable.student_id";
					try {
						results = query.executeQuery(studentBlockQuery);
						
						
						while(results.next()) {
							System.out.printf("%s, %s%n%s: %s/%s%n",
						    results.getString("last_name"),
						    results.getString("first_name"),
						    results.getString("assn_num"),
						    results.getString("earned_points"),
						    results.getString("max_points"));							
						}
						
						System.out.println();
						
					}catch (SQLException e) {
						e.printStackTrace();
					}
					
					break;
					
				//search/query for student by last name to get same format as case 2
				case 3:
					scan.nextLine();
					System.out.println("Enter students last name(Capitalize first letter)");
					System.out.print(">>");
					String lastName = scan.nextLine();
					String studentSearchQuery = "SELECT studentTable.first_name,"
							+ " studentTable.last_name,"
							+ " assnTable.assn_num,"
							+ " assnTable.max_points,"
							+ " assnTable.earned_points " +
							"FROM studentTable "+
							"INNER JOIN assnTable ON studentTable.student_id=assnTable.student_id "+
							"WHERE studentTable.last_name = ?";
					
					//use prepared statement for the input variable
					try {
						prepStmt = dbConnect.prepareStatement(studentSearchQuery);
						prepStmt.setString(1,lastName);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
					
					try {
						results = prepStmt.executeQuery();;
						
						//checks if last name exists then continues to formating the student
						if(!results.isBeforeFirst()) {
							System.out.println("No record with the last name: "+lastName);
						
						}else {
							while(results.next()) {
								System.out.printf("%s, %s%n%s: %s/%s%n",
							    results.getString("last_name"),
							    results.getString("first_name"),
							    results.getString("assn_num"),
							    results.getString("earned_points"),
							    results.getString("max_points"));							
							}
							System.out.println();
						}
					
					}catch(SQLException e) {
						e.printStackTrace();
					}
					
					break;
				
				// uses LIKE statement to match last names that start with a certain sequence
				case 4:
					scan.nextLine();
					System.out.println("Enter the beginning of a last name to search");
					System.out.print(">>");
					String lastNameLike = scan.nextLine();
					lastNameLike += "%";
					
					String studentLikeQuery = "SELECT studentTable.first_name,"
							+ " studentTable.last_name,"
							+ " assnTable.assn_num,"
							+ " assnTable.max_points,"
							+ " assnTable.earned_points " +
							"FROM studentTable "+
							"INNER JOIN assnTable ON studentTable.student_id=assnTable.student_id "+
							"WHERE studentTable.last_name LIKE ?";
					
					//use prepared statement for the input variable
					try {
						prepStmt = dbConnect.prepareStatement(studentLikeQuery);
						prepStmt.setString(1,lastNameLike);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
					
					try {
						results = prepStmt.executeQuery();
						if(!results.isBeforeFirst()) {
							System.out.println("No record with the last name: "+lastNameLike);
						
						}else {
							
							while(results.next()) {
//								System.out.println(results.getString("last_name")+", "+results.getString("first_name") +"\n"+results.getString("assn_num")+ ": "+ results.getString("earned_points") +"/"+results.getString("max_points") );
								System.out.printf("%s, %s%n%s: %s/%s%n",
							    results.getString("last_name"),
							    results.getString("first_name"),
							    results.getString("assn_num"),
							    results.getString("earned_points"),
							    results.getString("max_points"));							
							}
							System.out.println();
						}
					
					}catch(SQLException e) {
						e.printStackTrace();
					}
					break;
					
				//calculates class average for specific assignment 
				case 5:
					scan.nextLine();
					System.out.print("Enter assignment number to show the class average\n>>");
					
					int assnNum = scan.nextInt();
					
					String classAvgQuery = "SELECT assn_num, SUM(earned_points) AS total_earned, SUM(max_points) AS total_max "
							+ "FROM assnTable "
							+ "WHERE assn_num = ? "
							+ "GROUP BY assn_num ";
					
					//use prepared statement for the input variable
					try {
						prepStmt = dbConnect.prepareStatement(classAvgQuery);
						prepStmt.setInt(1,assnNum);
						
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
					
					try {
						
						results = prepStmt.executeQuery();
						while(results.next()) {
							double classAvg =  (double) results.getInt("total_earned") / results.getInt("total_max") * 100;
							System.out.printf("The class average for assignment %d was %.2f%%.%n", assnNum, classAvg );
						}
						
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;
				
				//Part 2 of spec
				//creates array of students
				case 6:
					scan.nextLine();
					
					ArrayList<Student> studentList = new ArrayList<>();
					String finalGradeQuery = "SELECT studentTable.first_name, studentTable.last_name, studentTable.student_id, "
							+ " SUM(assnTable.earned_points) AS total_earned, SUM(assnTable.max_points) AS total_max "
							+ " FROM studentTable "
							+ " JOIN assnTable ON studentTable.student_id = assnTable.student_id "
							+ " GROUP BY studentTable.student_id";
					
					try {
						results = query.executeQuery(finalGradeQuery);
						while(results.next()) {
							
							//creates student obj from data in the query of first name last name and student id then adds to list of students 
							Student student = new Student(results.getString("first_name"), results.getString("last_name"), results.getString("student_id"));
							studentList.add(student);
							
							//creates assignment obj from data in the query of total earned and total max using the SUM sql command then adds the assn to the student
							Assignment assignment = new Assignment(results.getInt("total_earned"),results.getInt("total_max"));
							student.addAssignment(assignment);							
						
						}
						
						//goes through each student and calculates total percentage and letter grade from assignments then prints them out in format print
						for(Student student : studentList) {
							double totalPercent = student.calcTotalPercent();
							String letterGrade = Student.convertToLetterGrade(totalPercent);
							
							System.out.printf("%s %s ID:(%s): Final Grade: %s (%.2f%%)\n\n", student.getFirstName(), student.getLastName(), student.getStudentId(), letterGrade, totalPercent);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
		
			}
			printMenu();
			input = scan.nextInt();
		}
		scan.close();
		
		
		//closes the stuff
		try {
			query.close();
			dbConnect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	//method for menu so i only needed to write it once
	private static void printMenu() {
        System.out.println("-----------------Gradebook for Students-------------------");
        System.out.println("Select an option to run one of the following queries (1-6)");
        System.out.println("1) Displays all students");
        System.out.println("2) Displays students and their grades");
        System.out.println("3) Search for student");
        System.out.println("4) Displays last names that start with same sequence");
        System.out.println("5) Calculate class average for specific assignment");
        System.out.println("6) Calculate and show students final grades");
        
        System.out.println("7) Exit Gradebook");
        System.out.println(">>");
	}

}
