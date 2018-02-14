package com.taskManager.view;

import com.taskManager.model.Task;
import com.taskManager.model.TaskList;

import java.util.Date;
import java.util.Set;
import java.util.SortedMap;

public interface View {
    void printMenu();
    void printCalendar(SortedMap<Date, Set<Task>> args);
    void displayList(TaskList taskList);
    public void forDate();
}
