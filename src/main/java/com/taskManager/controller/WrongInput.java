package com.taskManager.controller;

/**
 * The class responds for creating a custom exception.
 *
 * @author Paul Horiachyi
 * @version 1.0
 */
public class WrongInput extends Exception {
    /**
     * Exception class constructor
     *
     * @param msg Takes string value
     */
    public WrongInput (String msg)  { super(msg);}
    /**
     * Exception method for displaying custom messages
     *
     * @param msg Takes string value
     */
    public void wrongInputPrint (String msg) {
        System.out.println(msg);
    }
}
