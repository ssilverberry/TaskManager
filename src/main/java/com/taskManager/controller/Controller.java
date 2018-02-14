package com.taskManager.controller;

import com.taskManager.model.TaskList;
import java.text.ParseException;

/**
 * Task Manager controller interface
 *
 * @author Paul Horiachyi
 * @version 1.0
 */
public interface Controller extends Runnable {
    TaskList updateModel();
    void updateView();
    void runTaskMan() throws ParseException, WrongInput;
    TaskList getModelList();
    void run();
}
