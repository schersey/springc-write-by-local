package com.smart.demo;

/**
 * Created by yanchangxian on 2018/12/17.
 */
public class Node {

    private Object item;
    private Node next;

    public Node(Object item, Node next) {
        this.item = item;
        this.next = next;
    }

    public static void main(String[] args) {
        Node header = new Node(new Object(), null);
        header = new Node(new Object(), header); //一个新的节点插到了链表的头部


    }
}
