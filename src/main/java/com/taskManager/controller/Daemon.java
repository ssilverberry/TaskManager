package com.taskManager.controller;

import com.taskManager.model.Task;
import com.taskManager.model.TaskIO;
import com.taskManager.model.TaskList;
import com.taskManager.model.Tasks;
import com.taskManager.view.ConsoleView;
import com.taskManager.view.View;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * The class describes thread, which is running all time during
 * the user works with main app. The class sends notifications
 * to user about tasks which should be finished at the certain time.
 * Also the class overwrite file where is tasks stores and
 * if tasks are finished it will remove it from the file.
 *
 * @author Paul Horiachyi
 * @version 1.0
 * */

public class Daemon implements Runnable {
   /**
    * The main task list with which program works
    */
    private TaskList list;
   /**
    * The main thread which starts with the main app
    */
    private Thread thread;
    /**
     * Here we get possibility to print notifications
     * to console view.
     */
    private View console = new ConsoleView();
    /**
     * Here is a path to file where tasks stores
     */
    private File file;
    /**
     * Custom constructor
     *
     * @param model the main task list
     * @param file where tasks are recorded
     */
    public Daemon(TaskList model, File file) {
        list = model;
        this.file = file;
        thread = new Thread(this, "Task notification");
        thread.setDaemon(true);
        thread.start();
    }
    /**
     * The method is responsible for showing task notifications
     * which have to be accomplished
     */
    private void notifyNtf() {
        SortedMap<Date, Set<Task>> repeatedTasks;
        int oneMinute = 60000;
        int oneSecond = 1000;
        long prev = new Date().getTime() - oneMinute;
        long next = new Date().getTime() + oneSecond;
        Date pre = new Date();
        Date nex = new Date();

        nex.setTime(next);
        pre.setTime(prev);

        try {
            repeatedTasks = Tasks.calendar(list, pre, nex);
            for (Map.Entry<Date, Set<Task>> item : repeatedTasks.entrySet()) {
                System.out.print("\n-----------------------------------\n");
                System.out.print("Don't forget ! " +
                        item.getKey().toString() + "\nTasks: ");
                for (Task j : item.getValue()) {
                    System.out.print(" \'" +
                            j.getTitle().substring(0, 1).toUpperCase() +
                            j.getTitle().substring(1) +
                            "\' ");
                }
                System.out.print("\n-----------------------------------\n");
            }

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        checkForFinished(list);
    }
    /**
     * The main method in the class which start a notification thread
     */
    @Override
    public void run() {
        try {
            TaskIO.readText(list, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            notifyNtf();
            try {
                thread.sleep(59999);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * The method is responsible for checking finished tasks
     * and delete it if it was finished.
     *
     * @param taskList where from data are read
     */
    private void checkForFinished(TaskList taskList) {
        TaskList listTask = taskList.clone();
        for (Task i : listTask) {
            if (listTask.size() > 1) {
                if (!i.isRepeated() && new Date().compareTo(i.getTime()) >= 0) {
                    System.out.print("\n-----------------------------------\n");
                    System.out.print("Don't forget ! " +
                            i.getTime().toString() + "\nTasks: ");
                    System.out.print(i.getTitle() + "\' ");
                    taskList.remove(i);
                }
                if (i.isRepeated() && new Date().compareTo(i.getEndTime()) > 0) {
                    taskList.remove(i);
                }
            }
        }
        try {
            TaskIO.writeText(list, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
