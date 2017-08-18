/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import business.Person;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import util.DBUtil;

/**
 *
 * @author fssco
 */
public class EmployeeManagerDA {
    
    public static int addEmployeesToDB() {
        
        ArrayList<Person> employees = getEmployeeList();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        int rowcount = 0;

        String query
                = "INSERT INTO employees (firstName, middleName, lastName, employeeID, birthDate, hireDate) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            for (Person employee : employees) {
                ps = connection.prepareStatement(query);

                ps.setString(1, employee.getFirstName());
                ps.setString(2, employee.getMiddleName());
                ps.setString(3, employee.getLastName());
                ps.setString(4, employee.getEmployeeID());
                ps.setDate(5, java.sql.Date.valueOf(employee.getBirthDate()));
                ps.setDate(6, java.sql.Date.valueOf(employee.getHireDate()));

                rowcount += ps.executeUpdate();
            }

            return rowcount;
        } catch (SQLException e) {
            System.out.println(e);
            return rowcount;
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    
    public static ArrayList<Person> getEmployeeList(){
        
        ArrayList<Person> all = new ArrayList<Person>();
        
        all.add(new Person("Aaron", "A", "Aaronson", "65",
                LocalDate.of(1980, Month.JANUARY, 1), LocalDate.of(2013, Month.JANUARY, 2)));
        
        all.add(new Person("Erin", "E", "Erinson", "66",
                LocalDate.of(1980, Month.JANUARY, 1), LocalDate.of(2012, Month.JANUARY, 1)));
        
        all.add(new Person("Beatrix", "", "Kiddo", "1313",
                LocalDate.of(1976, Month.FEBRUARY, 28), LocalDate.of(2003, Month.OCTOBER, 10)));
        
        all.add(new Person("Paul", "Muad'Dib", "Atreides", "2000",
                LocalDate.of(1965, Month.APRIL, 4), LocalDate.of(1984, Month.MAY, 5)));
        
        all.add(new Person("Marty", "", "McFly", "1985",
                LocalDate.of(1968, Month.JUNE, 12), LocalDate.of(1885, Month.JANUARY, 1)));
        
        all.add(new Person("Roy", "", "Batty", "734",
                LocalDate.of(2016, Month.JANUARY, 8), LocalDate.of(2016, Month.JANUARY, 9)));
        
        all.add(new Person("Molly", "", "Millions", "1337",
                LocalDate.of(1984, Month.JULY, 1), LocalDate.of(2000, Month.APRIL, 30)));
        
        all.add(new Person("Arnold", "D", "Palmer", "8675",
                LocalDate.of(1929, Month.SEPTEMBER, 10), LocalDate.of(2005, Month.JUNE, 15)));
 
        all.add(new Person("Sylvester", "G", "Stallone", "1234",
                LocalDate.of(1946, Month.JULY, 6), LocalDate.of(1994, Month.AUGUST, 3)));
        
        all.add(new Person("Jason", "", "Statham", "7564",
                LocalDate.of(1967, Month.JULY, 26), LocalDate.of(2015, Month.DECEMBER, 10)));
        
        all.add(new Person("Molly", "K", "Ringwald", "1616",
                LocalDate.of(1968, Month.FEBRUARY, 18), LocalDate.of(2007, Month.MAY, 5)));
        
        all.add(new Person("Ally", "E", "Sheedy", "5813",
                LocalDate.of(1862, Month.JUNE, 13), LocalDate.of(1992, Month.MARCH, 14)));
        
        return all;
    }
    
    public static ArrayList<Person> getAllEmployees() {
        
        ArrayList<Person> employees = new ArrayList<Person>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM employees";

        try {
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            Person person = null;

            while (rs.next()) {
                person = new Person();
                person.setFirstName(rs.getString("firstName"));
                person.setMiddleName(rs.getString("middleName"));
                person.setLastName(rs.getString("lastName"));
                person.setEmployeeID(Integer.toString(rs.getInt("employeeID")));
                person.setBirthDate(rs.getDate("birthDate").toLocalDate());
                person.setHireDate(rs.getDate("hireDate").toLocalDate());

                employees.add(person);
            }

            return employees;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    
    public static ArrayList<Person> searchHireDate(LocalDate searchDate, String searchCriteria) {
        
        ArrayList<Person> employees = new ArrayList<Person>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";

        if (searchCriteria.equals("before")) {
            query = "SELECT * FROM employees "
                         + "WHERE hireDate <= ?";
        }
        else {
            query = "SELECT * FROM employees "
                         + "WHERE hireDate >= ?";
        }

        try {
            ps = connection.prepareStatement(query);
            ps.setDate(1, java.sql.Date.valueOf(searchDate));
            rs = ps.executeQuery();

            while (rs.next()) {
                Person person = new Person();
                person.setFirstName(rs.getString("firstName"));
                person.setMiddleName(rs.getString("middleName"));
                person.setLastName(rs.getString("lastName"));
                person.setEmployeeID(Integer.toString(rs.getInt("employeeID")));
                person.setBirthDate(rs.getDate("birthDate").toLocalDate());
                person.setHireDate(rs.getDate("hireDate").toLocalDate());

                employees.add(person);
            }

            return employees;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    
    /**
     * 
     * @param Person person
     * @return person
     */
    public static Person selectPersonByID(int id) {
        
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String query = "SELECT * FROM employees "
                     + "WHERE employeeID = ?";
        
        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            Person person = null;
            if (rs.next()) {
                person = new Person();
                person.setFirstName(rs.getString("firstName"));
                person.setMiddleName(rs.getString("middleName"));
                person.setLastName(rs.getString("lastName"));
                person.setBirthDate(rs.getDate("birthDate").toLocalDate());
                person.setHireDate(rs.getDate("hireDate").toLocalDate());
            }
            return person;
        }
        catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    
    public static int addNewEmployee(Person person) {
        
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        
        String query = "INSERT INTO employees (firstName, middleName, lastName, birthDate, hireDate) "
                     + "VALUES (?, ?, ?, ?, ?)";
        
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, person.getFirstName());
            ps.setString(2, person.getMiddleName());
            ps.setString(3, person.getLastName());
            ps.setDate(4, java.sql.Date.valueOf(person.getBirthDate()));
            ps.setDate(5, java.sql.Date.valueOf(person.getHireDate()));
            return ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e);
            return 0;
        } 
        finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
    }
    
}
