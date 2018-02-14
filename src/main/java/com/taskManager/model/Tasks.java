package com.taskManager.model;

import java.util.*;

public class Tasks {

    public static Iterable<Task> incoming(Iterable<Task> tasks, Date start, Date end) throws InstantiationException, IllegalAccessException {
        Iterable<Task> setData = new HashSet<>();
        Iterator<Task> itr = tasks.iterator();
        Task myTask;

        if (start == null || end == null) {
            System.err.print("You have entered wrong param. They should not be a null !\n");
            return setData;
        } else {
            while (itr.hasNext()) {
                myTask = itr.next();
                if (myTask.nextTimeAfter(start) != null && end.compareTo(myTask.nextTimeAfter(start)) >= 0) {
                    ((Collection<Task>)setData).add(myTask);
                }
            }
            return setData;
        }
    }

    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks, Date start, Date end) throws InstantiationException, IllegalAccessException {
        SortedMap<Date, Set<Task>> myMap = new TreeMap<Date, Set<Task>>();
        Iterable<Task> inSet = incoming(tasks, start, end);

        for (Task task : inSet) {
            Date now = task.nextTimeAfter(start);
            while(now != null && now.compareTo(end) <= 0) {
                if (myMap.containsKey(now)) {
                    myMap.get(now).add(task);
                } else {
                    Set<Task> outSet = new HashSet<Task>();
                    outSet.add(task);
                    myMap.put(now, outSet);
                }
                now = task.nextTimeAfter(now);
            }
        }
        return myMap;
    }

}
