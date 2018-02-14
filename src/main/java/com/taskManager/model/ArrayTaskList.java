package com.taskManager.model;

import java.util.Arrays;

public class ArrayTaskList extends TaskList implements Cloneable {


    public void add(Task task) throws Exception {
        if (task.equals(null))
            throw new NullPointerException();
        if (defaultObj.length - 1 <= size()) {
            interSize = defaultSize + (size >> 1);
            defaultObj = Arrays.copyOf(defaultObj, interSize);
        }
        defaultObj[size] = task;
        size++;
    }

    public boolean remove(Task task) {

        if (task == null)
            throw new NullPointerException("Null task should not be deleted");
        for (int i = 0; i <= size(); ++i) {
            if (task.equals(defaultObj[i])) {
                int var2 = this.size - i;
                if (var2 > 0) {
                    System.arraycopy(defaultObj, i + 1, defaultObj, i, var2);
                }
                size--;
                return true;
            }
        }
        return false;
    }

    @Override
    public Task getTask(int index) {
        if (index > size() || index < 0)
            throw new IllegalArgumentException("Index must be above zero or less " + size());
        return defaultObj[index];
    }

    @Override
    public int size() {
        return super.size();
    }
}
