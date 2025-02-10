import javax.swing.*;
import java.awt.*;

import java.util.HashMap;
import java.util.Map;

class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }
}

public class ATMInterface {
    private Map<String, BankAccount> users;
    private String currentUser;
    private JFrame frame;
    private JTextField amountField;
    private JTextArea displayArea;
    private JTextField userField;

    public ATMInterface() {
        users = new HashMap<>();
        users.put("user1", new BankAccount(1000));
        users.put("user2", new BankAccount(1500));

        frame = new JFrame("ATM Machine");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel loginPanel = new JPanel(new FlowLayout());
        loginPanel.add(new JLabel("Enter Username:"));
        userField = new JTextField(10);
        loginPanel.add(userField);
        JButton loginButton = new JButton("Login");
        loginPanel.add(loginButton);
        frame.add(loginPanel, BorderLayout.NORTH);

        displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Enter Amount:"));
        amountField = new JTextField(10);
        inputPanel.add(amountField);
        frame.add(inputPanel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton balanceButton = new JButton("Check Balance");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(balanceButton);
        buttonPanel.add(exitButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> login());
        depositButton.addActionListener(e -> deposit());
        withdrawButton.addActionListener(e -> withdraw());
        balanceButton.addActionListener(e -> checkBalance());
        exitButton.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private void login() {
        String username = userField.getText().trim();
        if (users.containsKey(username)) {
            currentUser = username;
            displayArea.setText(
                    "Welcome, " + username + "!\nYour current balance is: $" + users.get(username).getBalance());
        } else {
            displayArea.setText("User not found. Please enter a valid username.");
        }
    }

    private void deposit() {
        if (currentUser == null) {
            displayArea.setText("Please login first.");
            return;
        }
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount > 0) {
                users.get(currentUser).deposit(amount);
                displayArea.setText("Deposited: $" + amount + "\nNew Balance: $" + users.get(currentUser).getBalance());
            } else {
                displayArea.setText("Enter a valid deposit amount!");
            }
        } catch (NumberFormatException e) {
            displayArea.setText("Invalid input! Please enter a numeric value.");
        }
    }

    private void withdraw() {
        if (currentUser == null) {
            displayArea.setText("Please login first.");
            return;
        }
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount > 0) {
                if (users.get(currentUser).withdraw(amount)) {
                    displayArea.setText(
                            "Withdrawn: $" + amount + "\nNew Balance: $" + users.get(currentUser).getBalance());
                } else {
                    displayArea.setText("Insufficient balance or invalid amount!");
                }
            } else {
                displayArea.setText("Enter a valid withdrawal amount!");
            }
        } catch (NumberFormatException e) {
            displayArea.setText("Invalid input! Please enter a numeric value.");
        }
    }

    private void checkBalance() {
        if (currentUser == null) {
            displayArea.setText("Please login first.");
            return;
        }
        displayArea.setText("Current Balance: $" + users.get(currentUser).getBalance());
    }

    public static void main(String[] args) {
        new ATMInterface();
    }
}
