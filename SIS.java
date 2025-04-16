import java.util.Scanner;

public class StudentInformationSystem {
    private static final int MAX_STUDENTS = 100;
    private static int[][] studentData = new int[MAX_STUDENTS][2]; // [ID, Age]
    private static String[] studentNames = new String[MAX_STUDENTS];
    private static String[] studentPasswords = new String[MAX_STUDENTS];
    private static String[] studentCourses = new String[MAX_STUDENTS];
    private static double[][] studentGrades = new double[MAX_STUDENTS][3]; // [Programming, HCI, Average]
    private static int studentCount = 0;
    private static boolean isAdminLoggedIn = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nWelcome to PHINMA COC SIS");
            System.out.println("1. Admin");
            System.out.println("2. Student Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = getIntInput(scanner);

            switch (choice) {
                case 1:
                    if (adminLogin(scanner)) {
                        adminMenu(scanner);
                    }
                    break;
                case 2:
                    studentLogin(scanner);
                    break;
                case 3:
                    System.out.println("Exiting the system. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static boolean adminLogin(Scanner scanner) {
        System.out.print("\nEnter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        if (username.equals("admin") && password.equals("admin123")) {
            System.out.println("Admin login successful! Welcome, Admin.");
            isAdminLoggedIn = true;
            return true;
        }
        System.out.println("Invalid admin credentials. Please try again.");
        return false;
    }

    private static void adminMenu(Scanner scanner) {
        while (isAdminLoggedIn) {
            System.out.println("\nAdmin Menu");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Add Grades");
            System.out.println("4. Search Student by ID");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int choice = getIntInput(scanner);

            switch (choice) {
                case 1:
                    addStudent(scanner);
                    break;
                case 2:
                    viewStudents();
                    break;
                case 3:
                    addGrades(scanner);
                    break;
                case 4:
                    searchStudentByID(scanner);
                    break;
                case 5:
                    System.out.println("Logging out... Goodbye, Admin!");
                    isAdminLoggedIn = false;
                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    private static void addStudent(Scanner scanner) {
        if (studentCount >= MAX_STUDENTS) {
            System.out.println("Student limit reached! Cannot add more students.");
            return;
        }

        System.out.print("Enter Student ID: ");
        int id = getIntInput(scanner);

        if (isDuplicateID(id)) {
            System.out.println("Student ID already exists. Try again.");
            return;
        }

        String name;
        while (true) {
            System.out.print("Enter Student Name: ");
            name = scanner.nextLine();

            // Check if the name contains only letters and spaces
            if (name.matches("[a-zA-Z ]+")) {
                break;
            } else {
                System.out.println("Invalid name! Please enter a valid name (letters only).");
            }
        }

        System.out.print("Enter Student Course: ");
        String course = scanner.nextLine();

        System.out.print("Enter Student Age: ");
        int age = getIntInput(scanner);

        String password;
        while (true) {
            System.out.print("Enter Student Password: ");
            password = scanner.nextLine().trim(); // Trim to remove spaces

            // Check if password is empty
            if (!password.isEmpty()) {
                break;
            } else {
                System.out.println("Invalid password! Password cannot be empty. Try again.");
            }
        }

        studentData[studentCount][0] = id;
        studentData[studentCount][1] = age;
        studentNames[studentCount] = name;
        studentCourses[studentCount] = course;
        studentPasswords[studentCount] = password;

        studentGrades[studentCount][0] = -1;
        studentGrades[studentCount][1] = -1;
        studentGrades[studentCount][2] = -1;

        studentCount++;
        System.out.println("Student added successfully!");
    }

    private static void viewStudents() {
        if (studentCount == 0) {
            System.out.println("No students available.");
            return;
        }

        System.out.println("\nList of Students:");
        for (int i = 0; i < studentCount; i++) {
            System.out.println("ID: " + studentData[i][0] +
                    ", Name: " + studentNames[i] +
                    ", Course: " + studentCourses[i] +
                    ", Age: " + studentData[i][1] +
                    ", Programming: " + formatGrade(studentGrades[i][0]) +
                    ", HCI: " + formatGrade(studentGrades[i][1]) +
                    ", Average: " + formatGrade(studentGrades[i][2]));
        }
    }

    private static void addGrades(Scanner scanner) {
        System.out.print("\nEnter Student ID: ");
        int id = getIntInput(scanner);
        int index = getStudentIndex(id);
    
        if (index == -1) {
            System.out.println("Student not found.");
            return;
        }
    
        // Present a choice for the subjects before adding grades
        System.out.println("\nWhich subject would you like to add grades for?");
        System.out.println("1. Programming");
        System.out.println("2. HCI");
        System.out.println("3. Cancel");
    
        int choice = getIntInput(scanner);
    
        // Handle different choices
        if (choice == 1) {
            System.out.print("Enter Programming Grade: ");
            studentGrades[index][0] = getDoubleInput(scanner);
            System.out.println("Programming grade added successfully!");
        } else if (choice == 2) {
            System.out.print("Enter HCI Grade: ");
            studentGrades[index][1] = getDoubleInput(scanner);
            System.out.println("HCI grade added successfully!");
        } else if (choice == 3) {
            System.out.println("Operation canceled.");
            return; // Exit without making any changes
        } else {
            System.out.println("Invalid choice. Please try again.");
            return; // Exit if an invalid choice is made
        }
    
        // After adding the grade for the selected subject, calculate the average
        studentGrades[index][2] = (studentGrades[index][0] + studentGrades[index][1]) / 2; // Calculate Average
    
        System.out.println("Grades added successfully!");
    }
    

    private static void searchStudentByID(Scanner scanner) {
        System.out.print("\nEnter Student ID to search: ");
        int id = getIntInput(scanner);
        int index = getStudentIndex(id);

        if (index == -1) {
            System.out.println("Student not found.");
        } else {
            System.out.println("\nStudent Information:");
            System.out.println("ID: " + studentData[index][0]);
            System.out.println("Name: " + studentNames[index]);
            System.out.println("Course: " + studentCourses[index]);
            System.out.println("Age: " + studentData[index][1]);
            System.out.println("Programming: " + formatGrade(studentGrades[index][0]));
            System.out.println("HCI: " + formatGrade(studentGrades[index][1]));
            System.out.println("General Average: " + formatGrade(studentGrades[index][2]));
        }
    }

    private static void studentLogin(Scanner scanner) {
        System.out.print("\nEnter Student ID: ");
        int id = getIntInput(scanner);
        int index = getStudentIndex(id);

        if (index == -1) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (studentPasswords[index].equals(password)) {
            System.out.println("\nLogin successful! Welcome, " + studentNames[index]);
            studentMenu(scanner, index); // Open student menu
        } else {
            System.out.println("Incorrect password. Try again.");
        }
    }

    private static void studentMenu(Scanner scanner, int index) {
        while (true) {
            System.out.println("\nStudent Menu:");
            System.out.println("1. View Personal Information & Grades");
            System.out.println("2. Update Password");
            System.out.println("3. View Course Details");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            int choice = getIntInput(scanner);

            switch (choice) {
                case 1:
                    displayStudentInfo(index);
                    break;
                case 2:
                    updateStudentPassword(scanner, index);
                    break;
                case 3:
                    viewCourseDetails(index);
                    break;
                case 4:
                    System.out.println("Logging out... Goodbye, " + studentNames[index] + "!");
                    return; // Exit student menu
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void displayStudentInfo(int index) {
        System.out.println("\nStudent Information:");
        System.out.println("ID: " + studentData[index][0]);
        System.out.println("Name: " + studentNames[index]);
        System.out.println("Course: " + studentCourses[index]);
        System.out.println("Age: " + studentData[index][1]);
        System.out.println("Programming: " + formatGrade(studentGrades[index][0]));
        System.out.println("HCI: " + formatGrade(studentGrades[index][1]));
        System.out.println("General Average: " + formatGrade(studentGrades[index][2]));
    }

    private static void updateStudentPassword(Scanner scanner, int index) {
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine().trim();

        while (newPassword.isEmpty()) {
            System.out.println("Password cannot be empty! Try again.");
            System.out.print("Enter new password: ");
            newPassword = scanner.nextLine().trim();
        }

        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine().trim();

        while (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match! Please try again.");
            System.out.print("Enter new password: ");
            newPassword = scanner.nextLine().trim();
            System.out.print("Confirm new password: ");
            confirmPassword = scanner.nextLine().trim();
        }

        studentPasswords[index] = newPassword;
        System.out.println("Password updated successfully!");
    }

    private static void viewCourseDetails(int index) {
        System.out.println("\nCourse Details:");
        System.out.println("Student Name: " + studentNames[index]);
        System.out.println("Course: " + studentCourses[index]);
        System.out.println("This course is designed to provide students with foundational knowledge in their field.");
    }

    private static boolean isDuplicateID(int id) {
        return getStudentIndex(id) != -1;
    }

    private static int getStudentIndex(int id) {
        for (int i = 0; i < studentCount; i++) {
            if (studentData[i][0] == id) {
                return i;
            }
        }
        return -1;
    }

    private static int getIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input! Please enter a number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    private static double getDoubleInput(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input! Please enter a valid number: ");
            scanner.next();
        }
        return scanner.nextDouble();
    }

    private static String formatGrade(double grade) {
        return (grade == -1) ? "N/A" : String.format("%.2f", grade);
    }
}
