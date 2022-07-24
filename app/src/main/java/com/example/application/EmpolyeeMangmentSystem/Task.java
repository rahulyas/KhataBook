package com.example.application.EmpolyeeMangmentSystem;

import java.io.Serializable;
import java.util.ArrayList;

public class Task implements Serializable {

    private Long id;
    private String taskName;
    private ArrayList<Long> employees_id;
    private String taskDetails;
    private String taskDate;
    private String taskDeadLine;
    private boolean done;
    private int  evaluation;

    public Task(Long id, String taskName, ArrayList<Long> employees_id, String taskDetails, String taskDate, String taskDeadLine, boolean done, int evaluation) {
        this.id = id;
        this.taskName = taskName;
        this.employees_id = employees_id;
        this.taskDetails = taskDetails;
        this.taskDate = taskDate;
        this.taskDeadLine = taskDeadLine;
        this.done = done;
        this.evaluation = evaluation;
    }

    public Task() {
        employees_id = new ArrayList<>();
        done = false;

    }

    public long getId() {
        return id;
    }

    public void setId(String id) {
        this.id = Long.parseLong(id);
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public ArrayList<Long> getEmployees_id() {
        return employees_id;
    }

    public void setEmployees_id(ArrayList<Long> employees_id) {
        this.employees_id = employees_id;
    }

    public String getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(String taskDetails) {
        this.taskDetails = taskDetails;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskDeadLine() {
        return taskDeadLine;
    }

    public void setTaskDeadLine(String taskDeadLine) {
        this.taskDeadLine = taskDeadLine;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }
}
