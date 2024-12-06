//Author: @WillThompson
import java.util.ArrayList;
import java.util.Scanner;

public class User implements BankActions {
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String routingNumber;
    private double checkingBalance;
    private double savingBalance;

    private Scanner sc = new Scanner(System.in);

    public User(String fullName, String username, String password, String email, String routingNumber, double initialDeposit) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.routingNumber = routingNumber;
        this.checkingBalance = initialDeposit * 0.98;
        this.savingBalance = initialDeposit * 0.02;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public String getRoutingNumber() {
        return routingNumber;
    }
    public double getCheckingBalance() {
        return checkingBalance;
    }
    public void setCheckingBalance(double checkingBalance) {
        this.checkingBalance = checkingBalance;
    }
    public double getSavingBalance() {
        return savingBalance;
    }
    public void setSavingBalance(double savingBalance) {
        this.savingBalance = savingBalance;
    }

    @Override
    public void home() {
        System.out.println("Full name: " + fullName);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Email: " + email);
        System.out.println("Routing number: " + routingNumber);
        System.out.println("Checking account balance: $" + checkingBalance);
        System.out.println("Saving account balance: $" + savingBalance);
    }

    @Override
    public void deposit() {
        try {
            System.out.println("Which account would you like to deposit into?");
            System.out.println("1. Checking account");
            System.out.println("2. Saving account");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Enter the amount to deposit: $");
            double amount = Double.parseDouble(sc.nextLine().trim());

            if (amount <= 0) {
                System.out.println("Deposit amount must be positive.");
                return;
            }

            if (choice == 1) {
                double savingAmount = amount * 0.02;
                double checkingAmount = amount * 0.98;
                savingBalance += savingAmount;
                checkingBalance += checkingAmount;
                System.out.println("Deposit successful. New checking account balance: $" + checkingBalance);
            } else if (choice == 2) {
                savingBalance += amount;
                System.out.println("Deposit successful. New saving account balance: $" + savingBalance);
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid numbers.");
        }
    }

    @Override
    public void withdraw() {
        try {
            System.out.println("Which account would you like to withdraw from?");
            System.out.println("1. Checking account");
            System.out.println("2. Saving account");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Enter the amount to withdraw: $");
            double amount = Double.parseDouble(sc.nextLine().trim());

            if (amount <= 0) {
                System.out.println("Withdrawal amount must be positive.");
                return;
            }

            if (choice == 1) {
                if (amount <= checkingBalance) {
                    checkingBalance -= amount;
                    System.out.println("Withdrawal successful. New checking account balance: $" + checkingBalance);
                } else {
                    System.out.println("Insufficient funds in checking account.");
                }
            } else if (choice == 2) {
                double fee = amount * 0.05;
                double totalAmount = amount + fee;
                if (totalAmount <= savingBalance) {
                    savingBalance -= totalAmount;
                    System.out.println("Withdrawal successful. Withdrawal fee of $" + fee + " has been applied. New saving account balance: $" + savingBalance);
                } else {
                    System.out.println("Insufficient funds in saving account.");
                }
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid numbers.");
        }
    }

    @Override
    public void update() {
        System.out.println("Updating profile...");
        System.out.print("Enter your new full name: ");
        String newFullName = sc.nextLine().trim();
        System.out.print("Enter your new password: ");
        String newPassword = sc.nextLine().trim();
        setFullName(newFullName);
        setPassword(newPassword);
        System.out.println("Profile updated successfully!");
    }

    @Override
    public void transfer(ArrayList<User> users) {
        System.out.println("Users can transfer money only to accounts associated with the same bank (internal accounts)!");
        System.out.print("Enter the recipient's email: ");
        String recipientEmail = sc.nextLine().trim();

        if (!recipientEmail.endsWith("@bank.com")) {
            System.out.println("Invalid recipient email domain.");
            return;
        }

        User recipient = null;
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(recipientEmail)) {
                recipient = user;
                break;
            }
        }

        if (recipient == null) {
            System.out.println("No account associated with the entered email!");
            return;
        }

        try {
            System.out.print("Enter the amount to transfer: $");
            double amount = Double.parseDouble(sc.nextLine().trim());

            if (amount <= 0) {
                System.out.println("Transfer amount must be positive.");
                return;
            }

            if (amount <= checkingBalance) {
                checkingBalance -= amount;
                recipient.checkingBalance += amount;
                System.out.println("Transfer successful.");
            } else {
                System.out.println("Insufficient funds in checking account.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }

    @Override
    public void cancel(ArrayList<User> users) {
        System.out.print("Would you like to delete your account? Y/N: ");
        String choice = sc.nextLine().trim();
        if (choice.equalsIgnoreCase("Y")) {
            System.out.print("Enter your username: ");
            String uname = sc.nextLine().trim();
            System.out.print("Enter your password: ");
            String pwd = sc.nextLine().trim();
            if (uname.equals(username) && pwd.equals(password)) {
                users.remove(this);
                System.out.println("Your account has been deleted successfully!");
            } else {
                System.out.println("Invalid username or password.");
            }
        }
    }
}

class RegularUser extends User {
    public RegularUser(String fullName, String username, String password, String email, String routingNumber, double initialDeposit) {
        super(fullName, username, password, email, routingNumber, initialDeposit);
    }
}