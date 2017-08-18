/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import data.EmployeeManagerDA;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author fssco
 */
public class EmployeeManager implements Serializable {

    private ArrayList<Person> employees;

    public EmployeeManager() {
        employees = EmployeeManagerDA.getAllEmployees();
    }

    /**
     * @return the employees
     */
    public ArrayList<Person> getEmployees() {
        return employees;
    }

    /**
     * @param employees the employees to set
     */
    public void setEmployees(ArrayList<Person> employees) {
        this.employees = employees;
    }

    /**
     * 
     * @param searchDate
     * @param searchCriteria 
     * @return ArrayList<Person>
     */
    public ArrayList<Person> search(LocalDate searchDate, String searchCriteria) {
        
        ArrayList<Person> temp = new ArrayList<Person>();
      
        if (searchCriteria.equals("before")) {
            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getHireDate().isBefore(searchDate) ||
                        employees.get(i).getHireDate().compareTo(searchDate) == 0) {
                    temp.add(employees.get(i));
                }
            }
        }
        else {
            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getHireDate().isAfter(searchDate) ||
                        employees.get(i).getHireDate().compareTo(searchDate) == 0) {
                    temp.add(employees.get(i));
                }
            }
        }

        return temp;
    }
    
    /**
     * 
     * @param searchDate
     * @param searchCriteria
     * @return a list matching the search criteria
     */
    public ArrayList<Person> searchDB(LocalDate searchDate, String searchCriteria) {
        
        ArrayList<Person> matchedEmployees = EmployeeManagerDA.searchHireDate(searchDate, searchCriteria);
        
        return matchedEmployees;
    }



}
