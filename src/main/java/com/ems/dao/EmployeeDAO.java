package com.ems.dao;

import com.ems.model.Employee;
import com.ems.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    public void add(Employee e) throws SQLException {
        String sql = "INSERT INTO employees(name,email,phone,department,designation,salary) VALUES(?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            setFields(ps, e);
            ps.executeUpdate();
        }
    }

    public List<Employee> getAll() throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees ORDER BY employee_id";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(fromResultSet(rs));
        }
        return list;
    }

    public List<Employee> search(String keyword) throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE name LIKE ? OR email LIKE ? OR department LIKE ? OR designation LIKE ? ORDER BY employee_id";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            String k = "%" + keyword + "%";
            for (int i = 1; i <= 4; i++) ps.setString(i, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(fromResultSet(rs));
            }
        }
        return list;
    }

    public void update(Employee e) throws SQLException {
        String sql = "UPDATE employees SET name=?,email=?,phone=?,department=?,designation=?,salary=? WHERE employee_id=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            setFields(ps, e);
            ps.setInt(7, e.getEmployeeId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE employee_id=?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private void setFields(PreparedStatement ps, Employee e) throws SQLException {
        ps.setString(1, e.getName());
        ps.setString(2, e.getEmail());
        ps.setString(3, e.getPhone());
        ps.setString(4, e.getDepartment());
        ps.setString(5, e.getDesignation());
        ps.setDouble(6, e.getSalary());
    }

    private Employee fromResultSet(ResultSet rs) throws SQLException {
        return new Employee(rs.getInt("employee_id"), rs.getString("name"), rs.getString("email"),
                rs.getString("phone"), rs.getString("department"), rs.getString("designation"), rs.getDouble("salary"));
    }
}
