import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters for username and password
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class MCQ {
    private String question;
    private String[] options;
    private int correctAnswerIndex;

    public MCQ(String question, String[] options, int correctAnswerIndex) {
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    // Getters for question, options, and correctAnswerIndex
    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}

class UserDatabase {
    private Map<String, User> users;

    public UserDatabase() {
        users = new HashMap<>();
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public User getUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public void updateUser(User user, String newPassword) {
        user.setPassword(newPassword);
        System.out.println("Password updated successfully!");
    }
}

public class Main {
    private UserDatabase userDatabase;
    private Map<String, MCQ> mcqs;
    private User currentUser;
    private Scanner scanner;
    private Timer timer;
    private int timeLimit; // Time limit in seconds

    public Main() {
        userDatabase = new UserDatabase();
        mcqs = new HashMap<>();
        scanner = new Scanner(System.in);
        timer = new Timer();
        timeLimit = 300; // Set the time limit to 5 minutes (300 seconds)
        addDefaultQuestions(); // Add default questions during initialization
    }

    private void addDefaultQuestions() {
        String[] options1 = { "true", "false" };
        MCQ mcq1 = new MCQ("Java is an object-oriented language?", options1, 0);
        mcqs.put(mcq1.getQuestion(), mcq1);

        String[] options2 = { "JDK", "JIT" };
        MCQ mcq2 = new MCQ("Which component is used to compile, debug, and execute the Java programs?", options2, 0);
        mcqs.put(mcq2.getQuestion(), mcq2);

        String[] options3 = { "js", "java" };
        MCQ mcq3 = new MCQ("What is the extension of Java code files?", options3, 1);
        mcqs.put(mcq3.getQuestion(), mcq3);
    }

    public void run() {
        boolean exit = false;

        while (!exit) {
            if (currentUser == null) {
                System.out.println("Welcome to the Exam System!");
                System.out.println("Select an option:");
                System.out.println("1. Register and Create Account");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        registerAndCreateAccount();
                        break;
                    case 2:
                        login();
                        if (currentUser != null) {
                            userMenu();
                        }
                        break;
                    case 3:
                        exit = true;
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } else {
                userMenu();
            }
        }
    }

    private void registerAndCreateAccount() {
        System.out.println("Enter your username:");
        String username = scanner.nextLine();

        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        User user = new User(username, password);
        userDatabase.addUser(user);

        System.out.println("Account created successfully!");
    }

    private void login() {
        System.out.println("Enter your username:");
        String username = scanner.nextLine();

        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        currentUser = userDatabase.getUser(username, password);

        if (currentUser == null) {
            System.out.println("Invalid username or password. Login failed.");
        } else {
            System.out.println("Login successful!");
        }
    }

    private void userMenu() {
        System.out.println("User Menu");
        System.out.println("1. Start Exam");
        System.out.println("2. Update Profile");
        System.out.println("3. Change Password");
        System.out.println("4. Logout");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (choice) {
            case 1:
                startExam();
                break;
            case 2:
                updateProfile();
                break;
            case 3:
                changePassword();
                break;
            case 4:
                logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    private void startExam() {
        System.out.println("Starting the exam...");
        System.out.println("You have " + timeLimit + " seconds to complete the exam.");

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                submitExam();
            }
        }, timeLimit * 1000); // Schedule the timer task to run after the specified time limit

        int score = 0;
        int questionCount = 1;

        for (MCQ mcq : mcqs.values()) {
            System.out.println("Question " + questionCount + ":");
            System.out.println(mcq.getQuestion());
            String[] options = mcq.getOptions();
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            System.out.println("Enter your answer (1-" + options.length + "):");
            int answer = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            if (answer == mcq.getCorrectAnswerIndex() + 1) {
                score++;
                System.out.println("Correct answer!");
            } else {
                System.out.println("Incorrect answer!");
            }

            questionCount++;
        }

        System.out.println("Exam finished!");
        System.out.println("Your score: " + score + "/" + mcqs.size());
    }

    private void submitExam() {
        System.out.println("Time limit exceeded! Submitting the exam...");
        timer.cancel();
        timer.purge();
        userMenu();
    }

    private void updateProfile() {
        System.out.println("Enter your new username:");
        String newUsername = scanner.nextLine();

        currentUser.setUsername(newUsername);
        System.out.println("Username updated successfully!");
    }


    private void changePassword() {
        System.out.println("Enter your current password:");
        String currentPassword = scanner.nextLine();

        if (currentUser.getPassword().equals(currentPassword)) {
            System.out.println("Enter your new password:");
            String newPassword = scanner.nextLine();

            userDatabase.updateUser(currentUser, newPassword);
        } else {
            System.out.println("Invalid current password. Password update failed.");
        }
    }

    private void logout() {
        currentUser = null;
        System.out.println("Logged out successfully!");
    }

    public static void main(String[] args) {
        Main examSystem = new Main();
        examSystem.run();
    }
}
