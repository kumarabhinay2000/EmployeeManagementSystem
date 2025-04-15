import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EmployeeManagementSystem extends JFrame {
    private JTextField idField, nameField, emailField, salaryField;
    private JTextArea outputArea;
    private EmployeeDAO dao;

    public EmployeeManagementSystem() {
        dao = new EmployeeDAO();

        setTitle("🚀 Employee Management System");
        setSize(700, 600);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for input fields
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(new EmptyBorder(20, 40, 10, 40));
        inputPanel.setBackground(new Color(240, 248, 255)); // Light background

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel idLabel = new JLabel("Employee ID:");
        idLabel.setFont(labelFont);
        idField = new JTextField();
        idField.setFont(fieldFont);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        nameField = new JTextField();
        nameField.setFont(fieldFont);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        emailField = new JTextField();
        emailField.setFont(fieldFont);

        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setFont(labelFont);
        salaryField = new JTextField();
        salaryField.setFont(fieldFont);

        inputPanel.add(idLabel); inputPanel.add(idField);
        inputPanel.add(nameLabel); inputPanel.add(nameField);
        inputPanel.add(emailLabel); inputPanel.add(emailField);
        inputPanel.add(salaryLabel); inputPanel.add(salaryField);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 40, 10, 40));
        buttonPanel.setBackground(new Color(230, 230, 250));

        JButton addBtn = styledButton("Add");
        JButton viewBtn = styledButton("View All");
        JButton searchBtn = styledButton("Search");
        JButton updateBtn = styledButton("Update");
        JButton deleteBtn = styledButton("Delete");
        JButton exportBtn = styledButton("Export CSV");

        buttonPanel.add(addBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(exportBtn);

        // Output area
outputArea = new JTextArea(15, 60); // Set 15 rows, 60 columns
outputArea.setLineWrap(true);
outputArea.setWrapStyleWord(true);
outputArea.setEditable(false);
outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
outputArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

JScrollPane scrollPane = new JScrollPane(outputArea,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
scrollPane.setPreferredSize(new Dimension(600, 250));  // Increase height
scrollPane.setBorder(new EmptyBorder(10, 40, 20, 40));

        /*outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(new EmptyBorder(10, 40, 20, 40));*/

        // Add panels to frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Add action listeners
        addBtn.addActionListener(e -> addEmployee());
        viewBtn.addActionListener(e -> viewAll());
        searchBtn.addActionListener(e -> searchEmployee());
        updateBtn.addActionListener(e -> updateEmployee());
        deleteBtn.addActionListener(e -> deleteEmployee());
        exportBtn.addActionListener(e -> exportCSV());

        setVisible(true);
    }

    // Style buttons with hover effects
    private JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(65, 105, 225));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237));
            }
        });

        return button;
    }

    private void addEmployee() {
        try {
            Employee emp = new Employee(nameField.getText(), emailField.getText(), Double.parseDouble(salaryField.getText()));
            dao.addEmployee(emp);
            outputArea.setText("✅ Employee added successfully.");
        } catch (Exception ex) {
            outputArea.setText("❌ Error: " + ex.getMessage());
        }
    }

    private void viewAll() {
        try {
            List<Employee> list = dao.getAllEmployees();
            StringBuilder sb = new StringBuilder("📄 All Employees:\n");
            for (Employee e : list) {
                sb.append(String.format("ID: %d | Name: %s | Email: %s | Salary: %.2f\n", e.getId(), e.getName(), e.getEmail(), e.getSalary()));
            }
            outputArea.setText(sb.toString());
        } catch (SQLException ex) {
            outputArea.setText("❌ Error: " + ex.getMessage());
        }
    }

    private void searchEmployee() {
        try {
            int id = Integer.parseInt(idField.getText());
            Employee e = dao.searchEmployee(id);
            if (e != null) {
                nameField.setText(e.getName());
                emailField.setText(e.getEmail());
                salaryField.setText(String.valueOf(e.getSalary()));
                outputArea.setText("✅ Employee found.");
            } else {
                outputArea.setText("⚠️ Employee not found.");
            }
        } catch (Exception ex) {
            outputArea.setText("❌ Error: " + ex.getMessage());
        }
    }

    private void updateEmployee() {
        try {
            int id = Integer.parseInt(idField.getText());
            Employee emp = new Employee(id, nameField.getText(), emailField.getText(), Double.parseDouble(salaryField.getText()));
            dao.updateEmployee(emp);
            outputArea.setText("✅ Employee updated.");
        } catch (Exception ex) {
            outputArea.setText("❌ Error: " + ex.getMessage());
        }
    }

    private void deleteEmployee() {
        try {
            int id = Integer.parseInt(idField.getText());
            dao.deleteEmployee(id);
            outputArea.setText("🗑️ Employee deleted.");
        } catch (Exception ex) {
            outputArea.setText("❌ Error: " + ex.getMessage());
        }
    }

    private void exportCSV() {
        try {
            dao.exportToCSV("employee_export.csv");
            outputArea.setText("📁 Exported to employee_export.csv");
        } catch (IOException | SQLException ex) {
            outputArea.setText("❌ Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeManagementSystem::new);
    }
}






/*import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EmployeeManagementSystem extends JFrame {
    private JTextField idField, nameField, emailField, salaryField;
    private JTextArea outputArea;
    private EmployeeDAO dao;

    public EmployeeManagementSystem() {
        dao = new EmployeeDAO();

        setTitle("Employee Management System");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(10, 2));

        idField = new JTextField(); add(new JLabel("ID:")); add(idField);
        nameField = new JTextField(); add(new JLabel("Name:")); add(nameField);
        emailField = new JTextField(); add(new JLabel("Email:")); add(emailField);
        salaryField = new JTextField(); add(new JLabel("Salary:")); add(salaryField);

        JButton addBtn = new JButton("Add");
        JButton viewBtn = new JButton("View All");
        JButton searchBtn = new JButton("Search");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton exportBtn = new JButton("Export CSV");

        outputArea = new JTextArea(10, 40);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane);
        add(new JLabel());

        add(addBtn); add(viewBtn);
        add(searchBtn); add(updateBtn);
        add(deleteBtn); add(exportBtn);

        addBtn.addActionListener(e -> addEmployee());
        viewBtn.addActionListener(e -> viewAll());
        searchBtn.addActionListener(e -> searchEmployee());
        updateBtn.addActionListener(e -> updateEmployee());
        deleteBtn.addActionListener(e -> deleteEmployee());
        exportBtn.addActionListener(e -> exportCSV());

        setVisible(true);
    }

    private void addEmployee() {
        try {
            Employee emp = new Employee(nameField.getText(), emailField.getText(), Double.parseDouble(salaryField.getText()));
            dao.addEmployee(emp);
            outputArea.setText("Employee added successfully.");
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    private void viewAll() {
        try {
            List<Employee> list = dao.getAllEmployees();
            StringBuilder sb = new StringBuilder("All Employees:\n");
            for (Employee e : list) {
                sb.append(String.format("ID: %d, Name: %s, Email: %s, Salary: %.2f\n", e.getId(), e.getName(), e.getEmail(), e.getSalary()));
            }
            outputArea.setText(sb.toString());
        } catch (SQLException ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    private void searchEmployee() {
        try {
            int id = Integer.parseInt(idField.getText());
            Employee e = dao.searchEmployee(id);
            if (e != null) {
                nameField.setText(e.getName());
                emailField.setText(e.getEmail());
                salaryField.setText(String.valueOf(e.getSalary()));
                outputArea.setText("Employee found.");
            } else {
                outputArea.setText("Employee not found.");
            }
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    private void updateEmployee() {
        try {
            int id = Integer.parseInt(idField.getText());
            Employee emp = new Employee(id, nameField.getText(), emailField.getText(), Double.parseDouble(salaryField.getText()));
            dao.updateEmployee(emp);
            outputArea.setText("Employee updated.");
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    private void deleteEmployee() {
        try {
            int id = Integer.parseInt(idField.getText());
            dao.deleteEmployee(id);
            outputArea.setText("Employee deleted.");
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    private void exportCSV() {
        try {
            dao.exportToCSV("employee_export.csv");
            outputArea.setText("Exported to employee_export.csv");
        } catch (IOException | SQLException ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeManagementSystem::new);
    }
}*/
