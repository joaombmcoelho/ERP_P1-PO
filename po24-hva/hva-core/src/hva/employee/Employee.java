package hva.employee;

import hva.EntityWriter;
import hva.Hotel;
import hva.vaccine.ResponsibilityUnknownException;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public abstract class Employee implements Serializable, Comparable<Employee> {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    private final String _key;

    private final String _name;

    private final String _role;

    protected Hotel _hotel;

    public Employee(Hotel hotel, String key, String name, String role) {
        _key = key;
        _name = name;
        _role = role;
        _hotel = hotel;
    }

    public String getKey() {
        return _key;
    }

    public String getName() {
        return _name;
    }

    public String getRole() {
        return _role;
    }

    public abstract List<String> responsibilities();

    public abstract void addResponsibilities(String... responsibilities) throws ResponsibilityUnknownException;

    public abstract void removeResponsibilities(String... responsibilities) throws ResponsibilityUnknownException;

    public abstract boolean hasResponsibility(String responsibility);

    public abstract double satisfaction();

    public abstract String accept(EntityWriter writer);

    @Override
    public int compareTo(Employee other) {
        return _key.compareToIgnoreCase(other._key);
    }
}
