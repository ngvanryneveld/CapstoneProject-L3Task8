package task8;

import java.sql.*;
import java.util.*;

public class PoisedDB {

	public static void main(String[] args) {
		
		// try catch block around code where dealing with SQL
		try {
			Scanner sc = new Scanner(System.in);
			
			// establishes the connection to the database
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/PoisedDB?useSSL=false",
					"root",
					"A9958Cmoo"
					);

			// uses the connection from DriverManager to establish the Statement
			Statement statement = connection.createStatement();

			// while true means the program runs until the user selects the quit menu option
			while (true) {
				menuOption();
				int menuSelect = sc.nextInt();
				if (menuSelect == 0) {
					break;
				}
				// switch cases between the menu options for the user, allows various things to be done
				switch (menuSelect) {
					case (0):
						break;
					
					case (1):
						createProject(statement);
						break;
				
					case (2):
						editProject(statement);
						break;
					
					case (3):
						printAllProjects(statement);
						break;
				
					case (4):
						createPerson(statement);
						break;
					
					case (5):
						editPerson(statement);
						break;
					
					case (6):
						printAllPersons(statement);
						break;
				
					case (7):
						finaliseProject(statement);
				
				}
			}
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}

	}
	
	// this function utilizes the statement and executes queries from the user to finalise and print out an invoice to the client
	private static void finaliseProject(Statement statement) {
		// try catch block around code where dealing with SQL
		try {
			Scanner sc = new Scanner(System.in);
			
			// input to select which project is being finalised
			System.out.println("Please enter the project number you want to finalise: ");
			int selectedProjectNumber = sc.nextInt(); 
			
			// while loop to print out the persons within the database with their roles
			ResultSet results = statement.executeQuery("SELECT name, role FROM persons;");
			while (results.next()) {
				System.out.println("Name: " + results.getString("name")
						+ "\nRole: " + results.getString("role") + "\n");
			}
			// takes input from the user to select the client in the finalised project
			System.out.println("Please enter the first name of the client: ");
			String firstName = sc.next();
			System.out.println("Please enter the last name of the client: ");
			String lastName = sc.next();
			String clientName = firstName + " " + lastName;
			
			// takes input from the user to select the contractor in the finalised project
			System.out.println("Please enter the first name of the contractor: ");
			firstName = sc.next();
			System.out.println("Please enter the last name of the contractor: ");
			lastName = sc.next();
			String contractorName = firstName + " " + lastName;
			
			// takes input from the user to select the architect in the finalised project
			System.out.println("Please enter the first name of the architect: ");
			firstName = sc.next();
			System.out.println("Please enter the last name of the architect: ");
			lastName = sc.next();
			String architectName = firstName + " " + lastName;
			
			// uses the inputs from the user to update the databse and then select all information to calculate if the project has been paid off yet
			statement.execute("Update projects set project_completion='yes' where project_number=" + selectedProjectNumber + ";");
			results = statement.executeQuery("Select * from projects where project_number=" + selectedProjectNumber + ";");
			results.next();
			double fee = results.getDouble("project_fee");
			double paid = results.getDouble("paid_to_date");
			double totalRemaining = (fee - paid);
			
			// if statement to check the total remaining to see if the project has been paid off
			if (totalRemaining == 0) {
				System.out.println("This project has been marked as complete but there is no invoice as it has been paid off already. ");
			}
			else {
				// this is if there is an amount outstanding
				// the executeQuery statements gather the information and store it into a projectInvoice variable appropriately and in an easy-to-read output
				System.out.println("What is the completed date of the task (YYYY-MM-DD): ");
				String completedDate = sc.next();
				String projectInvoice = "";
				
				// selecting and storing the clients details and then the contractor and architect used on the project
				results = statement.executeQuery("select * from persons where name ='" + clientName + "';");
				results.next();
				projectInvoice += "Client name: " + results.getString("name");
				results = statement.executeQuery("select * from persons where name ='" + clientName + "';");
				results.next();
				projectInvoice += "\nClient's telephone number: +27 " + results.getString("telephone_number");
				results = statement.executeQuery("select * from persons where name ='" + clientName + "';");
				results.next();
				projectInvoice += "\nClient's email address: " + results.getString("email_address");
				results = statement.executeQuery("select * from persons where name ='" + contractorName + "';");
				results.next();
				projectInvoice += "\nContractor name: " + results.getString("name");
				results = statement.executeQuery("select * from persons where name ='" + architectName + "';");
				results.next();
				projectInvoice += "\nArchitect name: " + results.getString("name") + "\n";
				
				// selecting and storing projects that are from the project selected by the user
				results = statement.executeQuery("select * from projects where project_number =" + selectedProjectNumber + ";");
				results.next();
				projectInvoice += "\nProject number: " + results.getInt("project_number");
				projectInvoice += "\nProject name: " + results.getString("project_name");
				projectInvoice += "\nBuilding type: " + results.getString("building_type");
				projectInvoice += "\nProject address: " + results.getString("project_address");
				projectInvoice += "\nERF number: " + results.getInt("erf_number");
				projectInvoice += "\nDeadline date: " + results.getDate("deadline_date") + "\n";
				projectInvoice += "\nInvoice";
				projectInvoice += "\nCompletion date: " + completedDate;
				projectInvoice += "\nProject fee: " + results.getDouble("project_fee");
				projectInvoice += "\nPaid to date: " + results.getDouble("paid_to_date");
				projectInvoice += "\nTotal remaining: " + totalRemaining + "\n";
				System.out.println(projectInvoice);
			}
		}		
		catch(SQLException ex) {
			ex.printStackTrace();
		}
	}

	// print all persons function used to view all the people stored in the database and their roles
	private static void printAllPersons(Statement statement) {
		// try catch block around code where dealing with SQL
		try {
			// selects the results of the execute query from the persons table and prints out all the persons
			ResultSet results = statement.executeQuery("SELECT name, telephone_number, email_address, physical_address, role FROM persons;");
			while (results.next()) {
				System.out.println("Name: " + results.getString("name")
						+ "\nTelephone number: "+ results.getString("telephone_number")
						+ "\nEmail address: " + results.getString("email_address")
						+ "\nPhysical address: "+ results.getString("physical_address")
						+ "\nRole: " + results.getString("role") + "\n");
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	// print all projects function views all the projects that are within the database
	private static void printAllProjects(Statement statement) {
		// try catch block around code where dealing with SQL
		try {
			// the executeQuery statement selects all the information needed and prints them out in an appropriate format
			ResultSet results = statement.executeQuery("SELECT project_number, project_name, building_type, project_address, erf_number, project_fee, paid_to_date, deadline_date, project_completion FROM projects;");
			while (results.next()) {
				System.out.println("Project number: " + results.getInt("project_number")
						+ "\nProject name: "+ results.getString("project_name")
						+ "\nBuilding type: " + results.getString("building_type")
						+ "\nProject address: "+ results.getString("project_address")
						+ "\nERF number: " + results.getInt("erf_number")
						+ "\nProject fee: " + results.getDouble("project_fee")
						+ "\nPaid to date: " + results.getDouble("paid_to_date")
						+ "\nDeadline date: " + results.getDate("deadline_date")
						+ "\nProject completion: " + results.getString("project_completion") + "\n");
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// create project function used to store a new project to the database
	private static void createProject(Statement statement) {
		// try catch block around code where dealing with SQL
		try {
			Scanner sc = new Scanner(System.in);
			// takes user inputs for all the information needed and in a way so that it can be stored appropriately
			System.out.println("Please enter the project number: ");
			int projectNumber = sc.nextInt();
	
			System.out.println("Please enter the project name: ");
			String projectName = sc.next();
			
			System.out.println("Please enter the building type: ");
			String buildingType = sc.next();
			
			System.out.println("Enter the physcial Address of the project.\nEnter the number: ");
			String numberAddress = sc.next();
			System.out.println("Enter the street name: ");
			String streetName = sc.next();
			System.out.println("\nEnter the street road type (street, road, way, close, lane etc.): ");
			String roadType = sc.next();
			String projectAddress = numberAddress + " " + streetName + " " + roadType;
			
			System.out.println("Enter the Erf number: ");
			int erfNumber = sc.nextInt();
			
			System.out.println("Enter the project fee: ");
			double projectFee = sc.nextDouble();
			
			System.out.println("Enter the amount paid to date: ");
			double paidToDate = sc.nextDouble();
			
			System.out.println("Enter the deadline date (yyyy-mm-dd): ");
			String deadlineDate = sc.next();
			
			System.out.println("Enter no or yes for the project completion: ");
			String projectCompleted = sc.next();
			
			// executeUpdate statement to insert the new entry into the projects table within the database
			statement.executeUpdate("INSERT INTO projects VALUES (" + projectNumber + ", '" + projectName + "', '" + buildingType + "', '" + projectAddress + "', " + erfNumber + ", " + projectFee + ", " + paidToDate + ", '" + deadlineDate + "', '" + projectCompleted + "')");
			System.out.println("\nProject has been added.");	
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// create persons function used to store a new person to the database


	private static void createPerson(Statement statement) {
		// try catch block around code where dealing with SQL
		try {
			Scanner sc = new Scanner(System.in);
			// takes inputs that store the variables about the new entry
			System.out.println("Creating a new person.\nEnter the person's first name: ");
			String firstName = sc.next();
			System.out.println("\nEnter the person's last name: ");
			String lastName = sc.next();
			String fullName = firstName + " " + lastName;
			
			System.out.println("Enter the telephone number: ");
			String telephoneNumber = sc.next();
			
			System.out.println("Enter the email address: ");
			String emailAddress = sc.next();
			
			System.out.println("Enter the physcial Address of the person.\nEnter the number: ");
			String numberAddress = sc.next();
			System.out.println("Enter the person's street name: ");
			String streetName = sc.next();
			System.out.println("\nEnter the street road type (street, road, way, close, lane etc.): ");
			String roadType = sc.next();
			String physicalAddress = numberAddress + " " + streetName + " " + roadType;
		
			System.out.println("Enter the role that this person has (client, contractor, architect): ");
			String role = sc.next();
			
			// statement executeUpdate inserts a new person into the table persons within the database 
			statement.executeUpdate("INSERT INTO persons VALUES ('" + fullName + "', " + telephoneNumber + ", '" + emailAddress + "', '" + physicalAddress + "', '" + role + "')");
	
			System.out.println(role + " has been added.");
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}



	// this function displays the menu options that are available for the user to engage with
	private static void menuOption() {
		System.out.println("If you would like to add a project input 1.");
		System.out.println("If you would like to edit a project, input 2.");
		System.out.println("If you would like to view all projects, input 3.");
		System.out.println("If you would like to add a new person, input 4.");
		System.out.println("If you would like to edit a person, input 5.");
		System.out.println("If you would like to view all persons, input 6.");	
		System.out.println("If you would like to finalise a project, input 7.");
		System.out.println("Enter 0 to quit.");
	}
	
	// this function is used to edit an existing project within the table projects
	private static void editProject(Statement statement) {
		// try catch block around code where dealing with SQL
		try {
			Scanner sc = new Scanner(System.in);
			
			// takes input to see which project is being updated and then takes input again to see which element of the project is being changed
			System.out.println("Select the project number that you would like to update: ");
			int projectSelect = sc.nextInt();
			int update;
			System.out.println("Enter the appropriate number you are wanting to update:"
					+ "\n1 - Project number"
					+ "\n2 - Project name"
					+ "\n3 - Buildin type"
					+ "\n4 - Project address"
					+ "\n5 - ERF number"
					+ "\n6 - Project fee"
					+ "\n7 - Paid to date"
					+ "\n8 - Deadline date"
					+ "\n9 - Project completion: ");
			update = sc.nextInt();
			
			// while user inputs one of the given options, it will continue to work
			while (update != 1 && update != 2 && update != 3 && update != 4 && update != 5 && update != 6 && update != 7 && update !=8 && update != 9) {
				System.out.println("Please enter one of the numbers given: ");
				update = sc.nextInt();
			}
			
			// switch cases for which option is being used
			// each case appropriately takes input for the new information being used and then executes a statement to update the project in the field that is being change
			switch (update) {
				case (1):
					System.out.println("Please enter the new project number: ");
					int newProjectNumber = sc.nextInt();
					statement.execute("UPDATE projects set project_number=" + newProjectNumber + " where project_number=" + projectSelect + ";");
					break;
					
				case (2):
					System.out.println("Enter the new project name ");
					String newProjectName = sc.next();
					statement.execute("UPDATE projects set project_name='" + newProjectName + "' where project_number=" + projectSelect + ";");
					break;
					
				case (3):	
					System.out.println("Enter the new building type ");
					String newBuildingType = sc.next();
					statement.execute("UPDATE projects set building_type='" + newBuildingType + "' where project_number=" + projectSelect + ";");
					break;
					
				case (4):
					System.out.println("Enter the new physcial Address of the project.\nEnter the number: ");
					String numberAddress = sc.next();
					System.out.println("Enter the street name: ");
					String streetName = sc.next();
					System.out.println("\nEnter the street road type (street, road, way, close, lane etc.): ");
					String roadType = sc.next();
					String newProjectAddress = numberAddress + " " + streetName + " " + roadType;
					statement.execute("UPDATE projects set project_address='" + newProjectAddress + "' where project_number=" + projectSelect + ";");
					break;
					
				case (5):
					System.out.println("Please enter the new ERF number: ");
					int newERFNumber = sc.nextInt();
					statement.execute("UPDATE projects set erf_number=" + newERFNumber + " where project_number=" + projectSelect + ";");
					break;
					
				case (6):
					System.out.println("Please enter the new project fee: ");
					int newProjectFee = sc.nextInt();
					statement.execute("UPDATE projects set project_fee=" + newProjectFee + " where project_number=" + projectSelect + ";");
					break;
					
				case (7):
					System.out.println("Please enter the new paid to date: ");
					int newPaidToDate = sc.nextInt();
					statement.execute("UPDATE projects set paid_to_date=" + newPaidToDate + " where project_number=" + projectSelect + ";");
					break;
					
				case (8):
					System.out.println("Enter the new deadline date (YYYY-MM-DD): ");
					String newDeadlineDate = sc.next();
					statement.execute("UPDATE projects set deadline_date='" + newDeadlineDate + "' where project_number=" + projectSelect + ";");
					break;
					
				case (9):
					System.out.println("Enter the new project completion: ");
					String newProjectCompletion = sc.next();
					statement.execute("UPDATE projects set project_completion='" + newProjectCompletion + "' where project_number=" + projectSelect + ";");
					break;
			}
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	// this function is used to edit a person who is already entered into the table persons

	
	private static void editPerson(Statement statement) {
		// try catch block around code where dealing with SQL
		try {
			Scanner sc = new Scanner(System.in);
			// takes input from the user to select who will be edited, then it takes input again as to which element of that person is going to be updated
			System.out.println("Select the person name that you would like to update.\nEnter the person's first name: ");
			String firstNameSelect = sc.next();
			System.out.println("Enter the last name of " + firstNameSelect + ": ");
			String lastNameSelect = sc.next();
			String personSelect = firstNameSelect + " " + lastNameSelect;
			int update;
			System.out.println("Enter the appropriate number you are wanting to update:"
					+ "\n1 - Person name"
					+ "\n2 - Telephone number"
					+ "\n3 - Email address"
					+ "\n4 - Physical address"
					+ "\n5 - Role: ");
			update = sc.nextInt();
			
			// while user inputs one of the given options, this will not run
			while (update != 1 && update != 2 && update != 3 && update != 4 && update != 5) {
				System.out.println("Please enter one of the numbers given: ");
				update = sc.nextInt();
			}
			// switch cases used for the options of what is being edited about the person
			// each case takes input and then executes the update to the table about the updated variable
			switch (update) {
				case (1):
					System.out.println("Entering the new name.\nEnter the first name: ");
					String firstName = sc.next();
					System.out.println("Enter the last name: ");
					String lastName = sc.next();
					String newFullName = firstName + " " + lastName;
					statement.execute("UPDATE persons set name='" + newFullName + "' where name='" + personSelect + "';");
					break;
					
				case (2):
					System.out.println("Enter the new telephone number: ");
					String newTelephoneNumber = sc.next();
					statement.execute("UPDATE persons set telephone_number=" + newTelephoneNumber + " where name='" + personSelect + "';");
					break;
					
				case (3):	
					System.out.println("Enter the new email address: ");
					String newEmailAddress = sc.next();
					statement.execute("UPDATE persons set email_address='" + newEmailAddress + "' where name='" + personSelect + "';");
					break;
					
				case (4):
					System.out.println("Enter the new physcial Address of the person.\nEnter the number: ");
					String numberAddress = sc.next();
					System.out.println("Enter the street name: ");
					String streetName = sc.next();
					System.out.println("\nEnter the street road type (street, road, way, close, lane etc.): ");
					String roadType = sc.next();
					String newPersonAddress = numberAddress + " " + streetName + " " + roadType;
					statement.execute("UPDATE persons set physical_address='" + newPersonAddress + "' where name='" + personSelect + "';");
					break;
					
				case (5):
					System.out.println("Please enter the new role: ");
					int newRole = sc.nextInt();
					statement.execute("UPDATE persons set role='" + newRole + "' where name='" + personSelect + "';");
					break;
			}
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
