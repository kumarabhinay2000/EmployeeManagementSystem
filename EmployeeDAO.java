import java.sql.*;
import java.util.*;
import java.io.*;

public class EmployeeDAO {

    public void addEmployee(Employee emp) throws SQLException {
        String sql = "INSERT INTO employees (name, email, salary) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emp.getName());
            stmt.setString(2, emp.getEmail());
            stmt.setDouble(3, emp.getSalary());
            stmt.executeUpdate();
        }
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Employee(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getDouble("salary")));
            }
        }
        return list;
    }

    public Employee searchEmployee(int id) throws SQLException {
        String sql = "SELECT * FROM employees WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Employee(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getDouble("salary"));
            }
        }
        return null;
    }

    public void updateEmployee(Employee emp) throws SQLException {
        String sql = "UPDATE employees SET name=?, email=?, salary=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emp.getName());
            stmt.setString(2, emp.getEmail());
            stmt.setDouble(3, emp.getSalary());
            stmt.setInt(4, emp.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteEmployee(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void exportToCSV(String filename) throws IOException, SQLException {
        List<Employee> employees = getAllEmployees();
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("ID,Name,Email,Salary");
            for (Employee emp : employees) {
                writer.printf("%d,%s,%s,%.2f%n", emp.getId(), emp.getName(), emp.getEmail(), emp.getSalary());
            }
        }
    }
}
