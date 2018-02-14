package com.taskManager.model;

import java.io.*;
import java.util.Iterator;

public abstract class TaskList implements Iterable<Task>, Cloneable, Serializable {
    int defaultSize = 10;
    int interSize;
    int size = 0;

    Task[] defaultObj = new Task[defaultSize];

    public abstract void add(Task task) throws Exception;

    public abstract boolean remove(Task task);

    public int size() {
        return this.size;
    }

    public Task getTask(int index) {
        if (index > size() || index < 0)
            throw new IllegalArgumentException("Index must be above zero or less " + size());
        else
            return defaultObj[index];
    }

    public Iterator<Task> iterator() {
        return new Iterator<Task>() {
            private int cursor = 0;
            private int lastRet = -1;

            public boolean hasNext() {
                return (cursor != size());
            }

            public Task next() {
                if (!hasNext())
                    return null;
                else {
                    int i = cursor;
                    Task next = getTask(i);
                    lastRet = i;
                    cursor = i + 1;
                    return next;
                }
            }

            public void remove() {
                if (lastRet < 0)
                    throw new IllegalStateException();
                try {
                    TaskList.this.remove(getTask(lastRet));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (cursor > lastRet)
                    cursor--;
                lastRet = -1;
            }
        };
    }

    @Override
    public int hashCode() {
        int coeff = 31;
        int result = 1;
        for (int i = 0; i < size(); i++) {
            result = coeff * result + i;
        }
        result = coeff * result + size();
        return result;
    }

    @Override
    public boolean equals(Object list) {
        if (list == null)
            return false;
        if (this == list)
            return true;
        if (this.getClass() != list.getClass())
            return false;
        if (this.hashCode() == list.hashCode())
            return true;
        TaskList tasks = (TaskList) list;

        if (size == tasks.size) return true;

        return false;
    }

    @Override
    public TaskList clone() {
        TaskList clone = null;
        TaskList cloned = null;
        try {
            clone = (TaskList) super.clone();
            cloned = getDeepCloning(clone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cloned;
    }

    private TaskList getDeepCloning(Object obj) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (TaskList) ois.readObject();
    }
}
