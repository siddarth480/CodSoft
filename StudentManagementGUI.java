import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import java.awt.*;

class Student {
    private String name;
    private int rollNumber;
    private String grade;

    public Student(String name, int rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public String getName() {
        return name;
    }

    public String getGrade() {
        return grade;
    }

    public String toString() {
        return "Name: " + name + " | Roll No: " + rollNumber + " | Grade: " + grade;
    }
}

class StudentManagementSystem {
    private List<Student> students = new ArrayList<>();
    private static final String FILE_NAME = "students.txt";

    public void addStudent(Student student) {
        students.add(student);
        saveToFile();
    }

    public void removeStudent(int rollNumber) {
        students.removeIf(student -> student.getRollNumber() == rollNumber);
        saveToFile();
    }

    public Student searchStudent(int rollNumber) {
        return students.stream().filter(s -> s.getRollNumber() == rollNumber).findFirst().orElse(null);
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Student student : students) {
                writer.println(student.getRollNumber() + "," + student.getName() + "," + student.getGrade());
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        students.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    students.add(new Student(parts[1], Integer.parseInt(parts[0]), parts[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("No previous data found.");
        }
    }
}

public class StudentManagementGUI {
    private StudentManagementSystem sms;
    private JFrame frame;
    private JTextArea displayArea;
    private JTextField nameField, rollField, gradeField;

    public StudentManagementGUI() {
        sms = new StudentManagementSystem();
        sms.loadFromFile();
        frame = new JFrame("Student Management System");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Roll Number:"));
        rollField = new JTextField();
        inputPanel.add(rollField);
        inputPanel.add(new JLabel("Grade:"));
        gradeField = new JTextField();
        inputPanel.add(gradeField);
        frame.add(inputPanel, BorderLayout.NORTH);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        frame.add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 5, 5));
        JButton addButton = new JButton("Add Student");
        JButton removeButton = new JButton("Remove Student");
        JButton searchButton = new JButton("Search Student");
        JButton displayButton = new JButton("Display All");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(exitButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addStudent());
        removeButton.addActionListener(e -> removeStudent());
        searchButton.addActionListener(e -> searchStudent());
        displayButton.addActionListener(e -> displayStudents());
        exitButton.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private void addStudent() {
        try {
            String name = nameField.getText().trim();
            int rollNumber = Integer.parseInt(rollField.getText().trim());
            String grade = gradeField.getText().trim();
            sms.addStudent(new Student(name, rollNumber, grade));
            clearFields();
            displayStudents();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeStudent() {
        try {
            int rollNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter roll number to remove:"));
            sms.removeStudent(rollNumber);
            displayStudents();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid roll number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchStudent() {
        try {
            int rollNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter roll number to search:"));
            Student student = sms.searchStudent(rollNumber);
            JOptionPane.showMessageDialog(frame, student != null ? student.toString() : "Student not found.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid roll number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayStudents() {
        displayArea.setText("Student List:\n------------------\n");
        for (Student student : sms.getAllStudents()) {
            displayArea.append(student.toString() + "\n");
        }
    }

    private void clearFields() {
        nameField.setText("");
        rollField.setText("");
        gradeField.setText("");
    }

    public static void main(String[] args) {
        new StudentManagementGUI();
    }
}
