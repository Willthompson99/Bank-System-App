//Author: @WillThompson
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class App {
    private ArrayList<User> users;
    private Scanner sc;

    public App() {
        users = new ArrayList<>();
        sc = new Scanner(System.in);
        loadUserData();
    }

    public static void main(String[] args) {
        App app = new App();
        app.start();
        app.saveUserData();
    }

    public void start() {
        String choice = "";
        while (!choice.equalsIgnoreCase("E")) {
            displayMainMenu();
            choice = sc.nextLine().trim();
            switch (choice.toUpperCase()) {
                case "S":
                    signUp();
                    break;
                case "L":
                    logIn();
                    break;
                case "E":
                    System.out.println("Exiting the application.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public void displayMainMenu() {
        System.out.println("*******************************************************");
        System.out.println("*                   Bank System Page                  *");
        System.out.println("*******************************************************");
        System.out.println("*  Select an option:                                   *");
        System.out.println("*    (S) Sign Up                                       *");
        System.out.println("*    (L) Log In                                        *");
        System.out.println("*    (E) Exit                                          *");
        System.out.println("*******************************************************");
    }

    public void loadUserData() {
        Path path = Paths.get("users.csv");
        if (Files.exists(path)) {
            try (BufferedReader br = Files.newBufferedReader(path)) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    String fullName = data[0];
                    String username = data[1];
                    String password = data[2];
                    String email = data[3];
                    String routingNumber = data[4];
                    double checkingBalance = Double.parseDouble(data[5]);
                    double savingBalance = Double.parseDouble(data[6]);
                    User user = new RegularUser(fullName, username, password, email, routingNumber, 0);
                    user.setCheckingBalance(checkingBalance);
                    user.setSavingBalance(savingBalance);
                    users.add(user);
                }
            } catch (IOException e) {
                System.out.println("Error reading user data: " + e.getMessage());
            }
        } else {
            System.out.println("No user data found. Starting with empty user list.");
        }
    }

    public void saveUserData() {
        Path path = Paths.get("users.csv");
        try (BufferedWriter bw = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (User user : users) {
                String line = String.join(",",
                        user.getFullName(),
                        user.getUsername(),
                        user.getPassword(),
                        user.getEmail(),
                        user.getRoutingNumber(),
                        String.valueOf(user.getCheckingBalance()),
                        String.valueOf(user.getSavingBalance())
                );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }

    public String generateRoutingNumber() {
        Random rand = new Random();
        StringBuilder routingNumber = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            routingNumber.append(rand.nextInt(10));
        }
        return routingNumber.toString();
    }

    public void signUp() {
        System.out.print("Enter your full name: ");
        String fullName = sc.nextLine().trim();

        String username;
        while (true) {
            System.out.print("Enter a username: ");
            username = sc.nextLine().trim();
            if (findUserByUsername(username) == null) {
                break;
            } else {
                System.out.println("Username already exists. Please choose a different username.");
            }
        }

        System.out.print("Enter a password: ");
        String password = sc.nextLine().trim();

        String email;
        while (true) {
            System.out.print("Enter your email address (your_username@bank.com): ");
            email = sc.nextLine().trim();
            if (!email.equals(username + "@bank.com")) {
                System.out.println("Enter a valid email (your_username@bank.com).");
            } else if (findUserByEmail(email) != null) {
                System.out.println("Email already exists....enter a new email.");
            } else {
                break;
            }
        }

        double initialDeposit = 0;
        while (true) {
            try {
                System.out.print("Enter your initial deposit amount: $");
                initialDeposit = Double.parseDouble(sc.nextLine().trim());
                if (initialDeposit > 0) {
                    break;
                } else {
                    System.out.println("Deposit amount must be positive.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a valid number.");
            }
        }

        String routingNumber = generateRoutingNumber();

        User user = new RegularUser(fullName, username, password, email, routingNumber, initialDeposit);
        users.add(user);

        System.out.println("Account created successfully.");
    }

    public void logIn() {
        System.out.print("Enter your username: ");
        String username = sc.nextLine().trim();
        System.out.print("Enter your password: ");
        String password = sc.nextLine().trim();

        User user = findUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login successful....");
            userSession(user);
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    public void userSession(User user) {
        String choice = "";
        while (!choice.equalsIgnoreCase("L")) {
            displayUserMenu();
            choice = sc.nextLine().trim();
            switch (choice.toUpperCase()) {
                case "H":
                    user.home();
                    break;
                case "D":
                    user.deposit();
                    break;
                case "W":
                    user.withdraw();
                    break;
                case "U":
                    user.update();
                    break;
                case "T":
                    user.transfer(users);
                    break;
                case "C":
                    user.cancel(users);
                    if (!users.contains(user)) {
                        choice = "L";
                    }
                    break;
                case "L":
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public void displayUserMenu() {
        System.out.println("*********Main Menu**********");
        System.out.println("Enter your choice: (H)ome");
        System.out.println("Enter your choice: (D)eposit");
        System.out.println("Enter your choice: (W)ithdraw");
        System.out.println("Enter your choice: (U)pdate");
        System.out.println("Enter your choice: (T)ransfer");
        System.out.println("Enter your choice: (C)ancel");
        System.out.println("Enter your choice: (L)ogOut");
        System.out.println("*****************************");
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }
}