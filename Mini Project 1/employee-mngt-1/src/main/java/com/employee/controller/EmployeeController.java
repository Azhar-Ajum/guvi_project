package com.employee.controller;

import com.employee.model.Employee;
import com.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        return "index";
    }

    @GetMapping("/add-employee")
    public String showAddEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "add-employee";
    }

    @PostMapping("/save-employee")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        employeeService.saveEmployee(employee);
        return "redirect:/success";
    }

    @GetMapping("/success")
    public String success(Model model) {
        return "success";
    }

    @GetMapping("/view-all")
    public String viewAllEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "view-all";
    }

    @GetMapping("/search")
    public String showSearchForm(Model model) {
        model.addAttribute("searchTerm", "");
        model.addAttribute("searchType", "id"); // Default search type
        return "search";
    }

    @PostMapping("/search")
    public String searchEmployee(@RequestParam("searchTerm") String searchTerm,
                                 @RequestParam("searchType") String searchType,
                                 Model model) {
        List<Employee> employees;
        if ("id".equals(searchType)) {
            try {
                Long id = Long.parseLong(searchTerm);
                Optional<Employee> employee = employeeService.getEmployeeById(id);
                employees = employee.isPresent() ? List.of(employee.get()) : List.of();
            } catch (NumberFormatException e) {
                employees = List.of(); // Invalid input handling
            }
        } else {
            employees = employeeService.getEmployeesByName(searchTerm);
        }
        model.addAttribute("employees", employees);
        return "search";
    }

    @GetMapping("/update-delete")
    public String showUpdateDeleteForm(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "update-delete";
    }

    @GetMapping("/update-employee")
    public String showUpdateEmployeeForm(@RequestParam("id") Long id, Model model) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        model.addAttribute("employee", employee.orElse(null));
        return "update-employee";
    }

    @PostMapping("/update-employee")
    public String updateEmployee(@ModelAttribute("employee") Employee employee) {
        employeeService.saveEmployee(employee);
        return "redirect:/update-delete";
    }

    @PostMapping("/delete-employee")
    public String deleteEmployee(@RequestParam("id") Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/update-delete";
    }
}
