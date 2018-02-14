package com.taskManager.model;

import java.io.Serializable;

public class LinkedTaskList extends TaskList {
    Node head;
    Node tail;

    int size = 0;

    public LinkedTaskList() {
        head = new Node(null, null, tail);
        tail = new Node(null, head, null);
    }

    public void add(Task e) {

        if (head.getNext() == null) {
            Node prev;
            prev = tail;
            head.setNext(prev);
            prev.setItem(e);
            tail = new Node(null, prev, null);
            prev.setNext(tail);
            size++;
        } else {
            Node prev;
            prev = tail;
            prev.setItem(e);
            tail = new Node(null, prev, null);
            prev.setNext(tail);
            size++;
        }
    }


    public boolean remove(Task task) {

        Node target = head.getNext();

        while (target != tail) {
            if (target.item.equals(task)) {
                if (target.getNext() == tail) {
                    Node prev = target.getPrev();
                    tail.setPrev(prev);
                    prev.setNext(tail);
                    size--;
                    return true;
                } else {
                    Node prev = target.getPrev();
                    Node next = target.getNext();
                    prev.setNext(next);
                    next.setPrev(prev);
                    size--;
                    return true;
                }
            }
            target = getNextElement(target);
        }
        return false;
    }

    public int size() { return this.size; }

    public Task getTask(int index) {

        if (index > size()) {
            System.err.println("Index is out of List range");
            throw new NullPointerException();
        }

        Node target = head.getNext();
        for (int i = 0; i < index; i++) {
            target = getNextElement(target);
        }
        return target.getItem();
    }

    Node getNextElement(Node element) {
        return element.getNext();
    }

    private class Node implements Serializable {
        Task item;
        Node next;
        Node prev;

        Node(Task element, Node prev, Node next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }

        public Task getItem() {
            return item;
        }

        public void setItem(Task item) {
            this.item = item;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public Node getNext() {
            return next;
        }

        public Node getPrev() {
            return prev;
        }
    }
}
