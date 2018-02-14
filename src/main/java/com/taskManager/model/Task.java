package com.taskManager.model;

import java.io.*;
import java.util.Date;


/**
 * This is a main class for <b>"Task Manager"</b> program.
 * Here is a logical part of code for creating a task with parametres.
 *
 * @author paulhoriachyi
 * @version 1.0
 */
public class Task implements Cloneable, Serializable {
    private String title = null;

    private Date time;
    private Date start;
    private Date end;
    private int interval;

    private boolean active;
    private boolean repeated;


    /**
     * Class constructor for not repeated task.
     *
     * @param title set task name.
     * @param time  set task execution time.
     */
    public Task(String title, Date time) {
        this.title = title;
        this.time = (Date)time.clone();
    }

    /**
     * Class constructor for repeated task.
     * <p>
     * The first two param are the same to previous
     * class constructor for not repeated task.
     *
     * @param title    set task name.
     * @param start    set task start execution point.
     * @param end      set task end execution point.
     * @param interval set task execution frequency.
     */
    public Task(String title, Date start, Date end, int interval) {
        this.title = title;
        this.start = (Date)start.clone();
        this.end = (Date)end.clone();
        this.interval =  interval;
        this.repeated = true;
    }

    /**
     * ...method getTitle is a task name getter...
     *
     * @return task name value.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * ...method setTitle is a task name setter...
     *
     * @param title it's a task name
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * ...method isActive is a task state getter...
     *
     * @return active task state value
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * ...method setActive is a task state setter...
     *
     * @param active it's a task active state
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * ...method getTime is a time getter for not repeated task...
     *
     * @return time is a task start execution point
     */
    public Date getTime() {
        if (isRepeated())
            return start;
        else
            return time;
    }

    /**
     * ...method setTime is a time setter for not repeated task...
     *
     * @param time task start execution point
     */
    public void setTime(Date time) {
        if (isRepeated())
            this.repeated = false;
        this.time = (Date)time.clone();
    }

    /**
     * ...method getStartTime is a start time getter for repeated task...
     *
     * @return time task start execution point
     */
    public Date getStartTime() {
        if (!isRepeated())
            return this.time;
        else
            return this.start;
    }

    /**
     * ...method getEndTime is an end time getter for repeated task...
     *
     * @return time task end execution point
     */
    public Date getEndTime() {
        if (!isRepeated())
            return this.time;
        else
            return this.end;
    }

    /**
     * ...method getRepeatInterval is a frequency getter for repeated task...
     *
     * @return time task end execution point
     */
    public int getRepeatInterval() {
        if (!isRepeated())
            return 0;
        else
            return this.interval;
    }

    /**
     * ...method setTime is a time setter for repeated task...
     *  @param start    is a task start execution point
     * @param end      is a task end execution point
     * @param interval is a task exe. frequency
     */
    public void setTime(Date start, Date end, int interval) {
        this.start = (Date) start.clone();
        this.end = (Date) end.clone();
        this.interval = interval;
        this.repeated = true;
    }

    /**
     * ...method setTime is a time setter for repeated task...
     *
     * @return repeated
     */
    public boolean isRepeated() {
        return this.repeated;
    }

    /**
     * ...method nextTimeAfter is a tsk method for getting next task time
     * after current time point...
     *
     * @param current is a current task time point
     * @return -1 if task is not active any more
     */
    public Date nextTimeAfter(Date current) {
        if (isActive()) {
            if (isRepeated()) {
                Date i = new Date();
                int millSec = interval * 1000;
                i.setTime(start.getTime());
                while (i.compareTo(end) <= 0) {
                    if (current.before(start)) {
                        return start;
                    }
                    else if (current.before(i)) {
                        return i;
                    }
                    i.setTime(millSec + i.getTime());
                }
            } else if (current.before(time))
                return time;

        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        if (this.hashCode() == task.hashCode()) return true;
        if (this.getTitle() == task.title) return true;
        if (interval != task.interval) return false;
        if (active != task.active) return false;
        if (repeated != task.repeated) return false;
        if (title != null ? !title.equals(task.title) : task.title != null) return false;
        if (time != null ? !time.equals(task.time) : task.time != null) return false;
        if (start != null ? !start.equals(task.start) : task.start != null) return false;
        return false;
    }

    @Override
    public int hashCode() {
        int result = title != null ? 0001 : 0;
        result = 31 * result + (time != null ? 0010 : 0);
        result = (start != null ? 0011 : 0);
        result = (end != null ? 0100 : 0);
        result = title.hashCode();
        return result;
    }

    @Override
    public Task clone() {
        Task clone = null;
        Task cloned = null;
        try {
            clone = (Task) super.clone();
           // clone.time = new Date();
            cloned = getDeepCloning(clone);
            cloned.time = time;
            //System.out.println(clone.time + "System.out.println(this.time);");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cloned;
    }

    private Task getDeepCloning(Object obj) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream ous = new ObjectOutputStream(baos);
        ous.writeObject(obj);
        ous.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Task) ois.readObject();
    }

    @Override
    public String toString() {
        return "Task {" +
                "title='" + title + '\'' +
                ", time=" + time +
                ", start=" + start +
                ", end=" + end +
                ", interval=" + interval +
                ", active=" + active +
                ", repeated=" + repeated +
                " }";
    }
}