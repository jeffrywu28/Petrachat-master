package com.petra.petrachat;

public class Node {
    public Data data;
    public Node next;

    public Node(){}

    public Node(Data _data){
        this.data = _data;
        next = null;
    }

    public void setData(Data _data){
        this.data = _data;
    }

    public Data getData(){
        return data;
    }

    public void setNext(Node _next){
        this.next = _next;
    }

    public Node getNext(){
        return next;
    }

}
