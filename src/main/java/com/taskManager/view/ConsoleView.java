package com.taskManager.view;

import com.taskManager.model.Task;
import com.taskManager.model.TaskList;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class ConsoleView implements View {

    public void printCalendar(SortedMap<Date, Set<Task>> args) {
        for (Map.Entry<Date, Set<Task>> item : args.entrySet())
            for (Task i : item.getValue()) {
                System.out.println(item.getKey() +
                        " \'" +
                        i.getTitle().substring(0, 1).toUpperCase() +
                        i.getTitle().substring(1) +
                        "\'");
            }
    }

    public void printMenu() {
        char spc = ' ';
        String hello = "Hello";
        String first = "1. Display all tasks.";
        String second = "2. Get planned tasks for a some period.";
        String third = "3. Create task";
        String fourth = "4. Change task.";
        String fifth = "5. Remove task";
        String sixth = "6. Exit.";
        String choose = "Choose a number from 1 - 6 : ";
        String ntf =  "App is running.";
        String specChar = "-------------------------------";

        System.out.println(String.format("%17s", hello));
        System.out.println(specChar);
        System.out.println(first);
        System.out.println(second);
        System.out.println(third);
        System.out.println(fourth);
        System.out.println(fifth);
        System.out.println(sixth);
        System.out.println(specChar);
        System.out.println(String.format("%22s", ntf));
        System.out.println(String.format("%30s", choose));
        System.out.println(specChar);
    }

    public void displayList(TaskList list) {
        for (Task i : list)
            System.out.println(i);
    }

    public void forDate() {
        System.out.println("1. For next 3 days");
        System.out.println("2. For next 5 days");
        System.out.println("3. For next 7 days");
        System.out.println("4. For month");
        System.out.println("5. Enter your end date");
    }
}
