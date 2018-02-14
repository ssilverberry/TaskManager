package com.taskManager;


import com.taskManager.controller.*;
import com.taskManager.view.ConsoleView;

import java.io.File;
import java.text.ParseException;


/**
 * The main app class, which start a notification thread,
 * initiate controller and view (console)
 *
 * @author Paul Horiachyi
 */
public class App 
{
    public static void main( String[] args ) throws ParseException, WrongInput {
        ConsoleView view = new ConsoleView();
        File file = new File (new File("taskManager.txt").getAbsolutePath());
        Controller controller = new TaskManagerController(view, file);
        Daemon notify = new Daemon(controller.getModelList(), file);
        controller.run();
        notify.run();
    }
}
