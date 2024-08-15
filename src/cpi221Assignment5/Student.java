package cpi221Assignment5;

import java.util.ArrayList;

public class Student {
	
    private String firstName;
    private String lastName;
    private String studentId;
    private ArrayList<Assignment> assignments;
    
    //constructor
	public Student(String firstName, String lastName, String studentId) {
	
		this.firstName = firstName;
		this.lastName = lastName;
		this.studentId = studentId;
		this.assignments = new ArrayList<>();
	}
	
	//add assignment obj to students list of assignments
	public void addAssignment(Assignment assignment) {
		assignments.add(assignment);

	}
	
	//calculates the toal percentage pf points the student scorewd on all assignments
	public double calcTotalPercent() {
		
		int totalEarned = 0;
		int totalMax = 0;

		for (Assignment assignment : assignments) {
			
            totalEarned += assignment.getTotalEarned();
            totalMax += assignment.getTotalMax();
        }
		
		return (double) totalEarned / totalMax *100;
	}
	
	//gets lettergrades
	public static String convertToLetterGrade(double percentage) {
        if (percentage >= 90) {
            return "A";
            
        } else if (percentage >= 80) {
            return "B";
            
        } else if (percentage >= 70) {
            return "C";
            
        } else if (percentage >= 60) {
            return "D";
            
        } else {
            return "F";
        }
    }
	
	//getters 
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getStudentId() {
		return studentId;
	}

	public ArrayList<Assignment> getAssignments() {
		return assignments;
	}

}
