package com.example.application.Model;

public class DepartmentItem {
    public final Long id;
    public final String name;
    public final String details;

    public DepartmentItem(Long id, String name, String details) {
        this.id = id;
        this.name = name;
        this.details = details;
    }

    @Override
    public String toString() {
        return name;
    }
}
