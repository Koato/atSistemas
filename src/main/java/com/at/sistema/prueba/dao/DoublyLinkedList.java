package com.at.sistema.prueba.dao;

public class DoublyLinkedList<T> {

	public DoublyLinkedList() {
		head = null;
		tail = null;
		size = 0;
	}

	private Node<T> head;
	private Node<T> tail;
	private int size;

	public int size() {
		return size;
	}

	public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.setPrevious(tail);
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
    }

    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            head = head.getNext();
            if (head != null) {
                head.setPrevious(null);
            }
        } else if (index == size - 1) {
            tail = tail.getPrevious();
            tail.setNext(null);
        } else {
            Node<T> currentNode = head;
            for (int i = 0; i < index; i++) {
                currentNode = currentNode.getNext();
            }
            currentNode.getPrevious().setNext(currentNode.getNext());
            currentNode.getNext().setPrevious(currentNode.getPrevious());
        }
        size--;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> currentNode = head;
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.getNext();
        }
        return currentNode.getData();
    }
}
