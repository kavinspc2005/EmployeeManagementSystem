package com.ems.ui;

import com.ems.dao.EmployeeDAO;
import com.ems.model.Employee;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardFrame extends JFrame {
    private final EmployeeDAO dao = new EmployeeDAO();
    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField departmentField = new JTextField();
    private final JTextField designationField = new JTextField();
    private final JTextField salaryField = new JTextField();
    private final JTextField searchField = new JTextField();
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Name","Email","Phone","Department","Designation","Salary"}, 0) {
        public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(model);

    public DashboardFrame() {
        setTitle("Employee Management System");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Employee Management Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        JPanel searchPanel = new JPanel(new BorderLayout(8, 8));
        searchPanel.add(new JLabel("Search:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        JButton search = new JButton("Search");
        JButton showAll = new JButton("Show All");
        JPanel searchButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchButtons.add(search); searchButtons.add(showAll);
        searchPanel.add(searchButtons, BorderLayout.EAST);
        center.add(searchPanel, BorderLayout.NORTH);
        center.add(new JScrollPane(table), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(4, 4, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Employee Details"));
        idField.setEditable(false);
        addField(form, "ID", idField);
        addField(form, "Name", nameField);
        addField(form, "Email", emailField);
        addField(form, "Phone", phoneField);
        addField(form, "Department", departmentField);
        addField(form, "Designation", designationField);
        addField(form, "Salary", salaryField);
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton clear = new JButton("Clear");
        form.add(add); form.add(update); form.add(delete); form.add(clear);
        add(form, BorderLayout.SOUTH);

        add.addActionListener(e -> addEmployee());
        update.addActionListener(e -> updateEmployee());
        delete.addActionListener(e -> deleteEmployee());
        clear.addActionListener(e -> clearForm());
        search.addActionListener(e -> searchEmployees());
        showAll.addActionListener(e -> loadEmployees());
        table.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());

        loadEmployees();
    }

    private void addField(JPanel panel, String label, JTextField field) {
        panel.add(new JLabel(label)); panel.add(field);
    }

    private Employee readForm() {
        if (nameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) throw new IllegalArgumentException("Name and email are required.");
        double salary = Double.parseDouble(salaryField.getText().trim());
        int id = idField.getText().trim().isEmpty() ? 0 : Integer.parseInt(idField.getText().trim());
        return new Employee(id, nameField.getText().trim(), emailField.getText().trim(), phoneField.getText().trim(), departmentField.getText().trim(), designationField.getText().trim(), salary);
    }

    private void addEmployee() {
        try { dao.add(readForm()); JOptionPane.showMessageDialog(this, "Employee added successfully."); clearForm(); loadEmployees(); }
        catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Salary must be a valid number."); }
        catch (IllegalArgumentException | java.sql.SQLException e) { JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void updateEmployee() {
        try { if (idField.getText().trim().isEmpty()) throw new IllegalArgumentException("Select an employee first."); dao.update(readForm()); JOptionPane.showMessageDialog(this, "Employee updated successfully."); clearForm(); loadEmployees(); }
        catch (NumberFormatException e) { JOptionPane.showMessageDialog(this, "Salary must be a valid number."); }
        catch (IllegalArgumentException | java.sql.SQLException e) { JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void deleteEmployee() {
        if (idField.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Select an employee first."); return; }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete selected employee?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try { dao.delete(Integer.parseInt(idField.getText())); JOptionPane.showMessageDialog(this, "Employee deleted."); clearForm(); loadEmployees(); }
            catch (Exception e) { JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void loadEmployees() { try { populate(dao.getAll()); } catch (Exception e) { showDbError(e); } }
    private void searchEmployees() { try { populate(dao.search(searchField.getText().trim())); } catch (Exception e) { showDbError(e); } }

    private void populate(List<Employee> employees) {
        model.setRowCount(0);
        for (Employee e : employees) model.addRow(new Object[]{e.getEmployeeId(), e.getName(), e.getEmail(), e.getPhone(), e.getDepartment(), e.getDesignation(), e.getSalary()});
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        idField.setText(model.getValueAt(row, 0).toString());
        nameField.setText(model.getValueAt(row, 1).toString());
        emailField.setText(model.getValueAt(row, 2).toString());
        phoneField.setText(model.getValueAt(row, 3).toString());
        departmentField.setText(model.getValueAt(row, 4).toString());
        designationField.setText(model.getValueAt(row, 5).toString());
        salaryField.setText(model.getValueAt(row, 6).toString());
    }

    private void clearForm() { idField.setText(""); nameField.setText(""); emailField.setText(""); phoneField.setText(""); departmentField.setText(""); designationField.setText(""); salaryField.setText(""); table.clearSelection(); }
    private void showDbError(Exception e) { JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
}
