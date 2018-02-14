package com.taskManager.controller;

import com.taskManager.model.*;
import com.taskManager.view.View;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The class responds for Controller and View interacting.
 *
 * @author Paul Horiachyi
 * @version 1.0
 */

public class TaskManagerController implements Controller, Runnable {

    // Logger log4j
    private static final Logger logger = LogManager.getLogger(TaskManagerController.getLogger());
    // Task class instance for getting it's methods
    private Task model;
    // View class instance for getting it's methods
    private View view;
    /*
     Main task list where tasks will be written and
     where its will be taken from file
    */
    private TaskList taskList = new LinkedTaskList();
    // Task list clone for safe work
    private TaskList taskListClone = taskList.clone();
    // Scanner for getting user input
    private Scanner scanner = new Scanner(System.in);
    /*
      File where tasks store and where its been taken
      and where its will be written
    */
    private File file;
    // String date format instance
    private final String FORMATT = "yy.MM.dd HH:mm:ss";
    // Date format
    private SimpleDateFormat format = new SimpleDateFormat(FORMATT);
    // Custom exception class instance for using it's method
    private WrongInput wrIn = new WrongInput("Smth wrong with your input");
    /**
     * Controller constructor
     *
     * @param view Class View object
     */
    public TaskManagerController(View view, File file) {
        this.view = view;
        model = new Task(" ", new Date());
        this.file = file;
    }
    /**
     * Model update method.
     * It is used for updating data about certain task
     */
    @Override
    public TaskList updateModel() {
        logger.info("Model was updated");
        return taskList;
    }
    /**
     * View update method.
     * It is used for a console updating and displaying text information
     */
    @Override
    public void updateView() {
        view.displayList(taskList);
        logger.info("View was updated");
    }
    /**
     * Method for getting log4j logger instance
     */
    private static Logger getLogger() {
        return logger;
    }
    /**
     * Method for working with menu.
     * Also contains the logic of interaction with the task and task list.
     */
    public void runTaskMan() throws ParseException, WrongInput {
        String a;
        String string;
        Date end = new Date();
        Calendar c = Calendar.getInstance();

        view.printMenu();
        a = keyboardReadWholeLn();

        switch (a) {
            case "1":
                if (checkListForCntnt(taskListClone)) {
                    view.displayList(taskListClone);
                    //view.printText("\n"+getPointer(), '\n');
                } else {
                    System.out.println("It seems you don't have any task there ;)");
                    createTask();
                }
                runTaskMan();
                break;
            case "2":
                System.out.println("\nChoose menu item number.\n");
                if (checkListForCntnt(taskListClone)) {
                    view.forDate();
                    string = keyboardReadWholeLn();
                    switch (string) {
                        case "1":
                            c.add((Calendar.DATE), 3);
                            end.setTime(c.getTimeInMillis());
                            System.out.println(format.format(end));
                            makeCalendar(end);
                            break;
                        case "2":
                            c.add((Calendar.DATE), 5);
                            end.setTime(c.getTimeInMillis());
                            System.out.println(format.format(end));
                            makeCalendar(end);
                            break;
                        case "3":
                            c.add((Calendar.DATE), 7);
                            end.setTime(c.getTimeInMillis());
                            System.out.println(format.format(end));
                            makeCalendar(end);
                            break;
                        case "4":
                            if (c.getActualMaximum(Calendar.DAY_OF_MONTH) == 30) {
                                c.add((Calendar.DATE), 30);
                                end.setTime(c.getTimeInMillis());
                            } else if (c.getActualMaximum(Calendar.DAY_OF_MONTH) == 31) {
                                c.add((Calendar.DATE), 31);
                                end.setTime(c.getTimeInMillis());
                            } else if (c.getActualMaximum(Calendar.DAY_OF_MONTH) == 28) {
                                c.add((Calendar.DATE), 28);
                                end.setTime(c.getTimeInMillis());
                            } else if (c.getActualMaximum(Calendar.DAY_OF_MONTH) == 29) {
                                c.add((Calendar.DATE), 29);
                                end.setTime(c.getTimeInMillis());
                            }
                            makeCalendar(end);
                            break;
                        case "5":
                            String str;
                            System.out.print("Enter your end date (yy.MM.dd HH:mm:ss): ");
                            str = keyboardReadWholeLn();
                            end.setTime(format.parse(str).getTime());
                            makeCalendar(end);
                            break;
                        default:
                            System.out.println("\nEnter correct number of menu item!");

                    }
                }
                runTaskMan();
                break;
            case "3":
                createTask();
                runTaskMan();
                break;
            case "4":
                changeTask();
                this.notify();
                runTaskMan();
                break;
            case "5":
                removeTask();
                this.notify();
                runTaskMan();
                break;
            case "6":
                exitTaskManager();
                break;
            default:
                System.out.println("ENTER A NUMBER !");
                run();
        }
        this.notifyAll();
    }
    /**
     * Method for getting the whole line of input
     */
    private String keyboardReadWholeLn() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
    /**
     * The method checks if there is a task in the list
     */
    private boolean checkListForCntnt(TaskList list) {
        return list.size() != 0;
    }
    /**
     * Method for creating a task
     */
    private void createTask() {
        String str;
        String strTitle;
        String strDate;
        Task modelClone = new Task(" ", new Date());

        System.out.println();
        System.out.print(String.format("%25s", "Creating a task..."));
        System.out.print("\nEnter task title: ");

        strTitle = keyboardReadWholeLn();
        modelClone.setTitle(strTitle);

        System.out.print("\nEnter task date executing (yy.MM.dd HH:mm:ss): ");
        strDate = keyboardReadWholeLn();

        try {
            modelClone.setTime(format.parse(strDate));
        } catch (ParseException e) {
            wrIn.wrongInputPrint("ENTER CORRECT DATE ! TRY AGAIN TO MAKE A TASK !");
            run();
        }
        System.out.print("\nDo you want to make the task repeatable (Y/N) ? ");
        str = keyboardReadWholeLn();

        if (str.toLowerCase().equals("y")) {
            modelClone.setActive(true);
            System.out.print("\nEnter end task time point (yy.MM.dd HH:mm:ss): ");
            strDate = keyboardReadWholeLn();
            makeInterval(modelClone);
            try {
                if(modelClone.getStartTime().compareTo(format.parse(strDate)) < 0)
                    modelClone.setTime(modelClone.getStartTime(), format.parse(strDate), modelClone.getRepeatInterval());
                else {
                    System.out.println("You have entered end task time which is earlier than your start task time !");
                    createTask();
                }
            } catch (ParseException e) {
                wrIn.wrongInputPrint("Date could not be parsed! Enter correct  date, pls.");
                run();
            }
        }
        model = modelClone;
        try {
            taskListClone.add(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        acceptChanges();
        logger.info("Task \'" + modelClone.getTitle() + "\' was created");
    }
    /**
     * Method for editing a certain task
     */
    private void changeTask() {
        String str;
        String strTitle;
        String strDate;

        view.displayList(taskListClone);
        System.out.println();
        System.out.println("What task do you want to change (enter it's title)? ");
        str = keyboardReadWholeLn();

        for (Task i : taskListClone) {
            if (str.toLowerCase().equals(i.getTitle().toLowerCase())) {
                System.out.println("\n1. Title");
                System.out.println("2. Time");
                System.out.println("3. Start time");
                System.out.println("4. End time");
                System.out.println("5. Interval");
                System.out.println("6. Make it active/inactive");
                System.out.print("Type your variant: ");
                str = keyboardReadWholeLn();

                switch (str) {
                    case "1":
                        System.out.print("Enter task title: ");
                        strTitle = keyboardReadWholeLn();
                        i.setTitle(strTitle);
                        break;
                    case "2":
                        System.out.print("\nEnter task date executing (yy.MM.dd HH:mm:ss): ");
                        strDate = keyboardReadWholeLn();
                        try {
                            i.setTime(format.parse(strDate));
                            i.setActive(i.isActive());
                        } catch (ParseException e) {
                            wrIn.wrongInputPrint("Date could not be parsed! Enter correct  date, pls.");
                            changeTask();
                        }
                        break;
                    case "3":
                        System.out.print("\nEnter start task time point (yy.MM.dd HH:mm:ss):");
                        strDate = keyboardReadWholeLn();
                        try {
                            i.setTime(format.parse(strDate), i.getEndTime(), i.getRepeatInterval());
                        } catch (ParseException e) {
                            wrIn.wrongInputPrint("Date could not be parsed! Enter correct  date, pls.");
                            changeTask();
                        }
                        break;
                    case "4":
                        System.out.print("\nEnter end task time point (yy.MM.dd HH:mm:ss): ");
                        strDate = keyboardReadWholeLn();
                        try {
                            if (i.getStartTime().compareTo(format.parse(strDate)) < 0)
                                i.setTime(i.getStartTime(), format.parse(strDate), i.getRepeatInterval());
                            else {
                                logger.warn("Your end task time is incorrect, it is earlier than your start task time !");
                                changeTask();
                            }
                        } catch (ParseException e) {
                            wrIn.wrongInputPrint("Date could not be parsed! Enter correct  date, pls.");
                            changeTask();
                        }
                        break;
                    case "5":
                        makeInterval(i);
                        break;
                    case "6":
                        System.out.println("Enter 1/0 (activate = 1, deactivate = 0): ");
                        str = keyboardReadWholeLn();
                        if (str.equals("1"))
                            i.setActive(true);
                        else
                            i.setActive(false);
                        break;
                }
                model = i;
            }
        }
        acceptChanges();
        logger.info("Task was modified");
    }
    /**
     * Method for removing a certain task
     */
    private void removeTask() {
        String str;
        view.displayList(taskListClone);
        System.out.print("What task do you want to remove (enter it's title)? ");
        str = keyboardReadWholeLn();
        TaskList listClone = taskListClone.clone();

        for (Task i : taskListClone) {
            if (str.toLowerCase().equals(i.getTitle().toLowerCase())) {
                logger.info("Task \'" + i.getTitle() + "\' was removed");
                listClone.remove(i);
            }
        }
        taskListClone = listClone;
        try {
            TaskIO.writeText(taskListClone, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateModel();
        updateView();
    }
    /**
     * Method for going out from the app
     */
    private void exitTaskManager() {
        System.exit(0);
    }
    /**
     * Method for accepting changes after editing/creating task or task list
     */
    private void acceptChanges() {
        taskList = taskListClone;
        try {
            TaskIO.writeText(taskList, file);
        } catch (Exception e) {
            wrIn.wrongInputPrint("Smth wrong with removing existing tasks");
        }
        updateModel();
        updateView();
    }
    /**
     * Method for making a calendar for some period of time
     *
     * @param endPoint It is a last task execution time point
     */
    private void makeCalendar(Date endPoint) {
        try {
            SortedMap<Date, Set<Task>> calendar = Tasks.calendar(taskListClone, new Date(), endPoint);
            view.printCalendar(calendar);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    /**
     * Method which responds for returning the whole task list .
     *
     * @return Task list
     */
    public TaskList getModelList() {
        return taskListClone;
    }
    /**
     * The main class method for starting a program
     */
    @Override
    synchronized public void run() {
        try {
            runTaskMan();
        } catch (ParseException e) {
            wrIn.wrongInputPrint("Something wrong with your input");
        } catch (WrongInput wrongInput) {
            wrongInput.printStackTrace();
        }
    }
    /**
     * Method for a getting time in days/hours/minutes/seconds
     */
    private void makeInterval(Task task) {
        int days;
        int hours;
        int minutes;
        int seconds;
        int time = 0;
        String str;
        System.out.print("\nEnter interval (value & day/hour/minutes/seconds): ");
        str = keyboardReadWholeLn();
        String[] dateArr = str.split(" ");
        for (int j = 0; j < dateArr.length; j++) {
            dateArr[j] = dateArr[j].toLowerCase();
            char tmp = dateArr[j].charAt(0);
            try {
                if (tmp == 'd') {
                days = Integer.parseInt(dateArr[j-1]) * 86400;
                time += days;
                } else if (tmp == 'h') {
                    hours = Integer.parseInt(dateArr[j-1]) * 3600;
                    time += hours;
                } else if (tmp == 'm') {
                    minutes = Integer.parseInt(dateArr[j-1]) * 60;
                    time += minutes;
                } else if (tmp == 's') {
                    seconds = Integer.parseInt(dateArr[j-1]);
                    time += seconds;
                }
            } catch(ArrayIndexOutOfBoundsException exp) {
                wrIn.wrongInputPrint("You type incorrect interval ! Pls, enter VALUE then UNITS !");
                run();
            }
        }
        task.setTime(task.getStartTime(), task.getEndTime(), time);
    }
}
