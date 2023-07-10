package com.at.sistema.prueba.dao;

public class Node<T> {

    public Node(T data) {
        this.data = data;
    }
	
	private T data;
    private Node<T> previous;
    private Node<T> next;

    public T getData() {
        return data;
    }

    public Node<T> getPrevious() {
        return previous;
    }

    public void setPrevious(Node<T> previous) {
        this.previous = previous;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }
}
