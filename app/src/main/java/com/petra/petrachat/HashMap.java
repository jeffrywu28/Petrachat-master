package com.petra.petrachat;

import java.util.ArrayList;
import java.util.List;

public class HashMap {
    public List<Node> memo = new ArrayList<>();
    public List<counter> coun = new ArrayList<>();

    public HashMap(){
        for(int i = 0; i < 26; i++) {
            memo.add(new Node(null));
            coun.add(new counter());
        }
    }

    public int hash(String str){
        return str.charAt(0) - 'a';
    }

    public void set(String _value){
        int index = hash(_value);
        Node tmp = new Node(new Data(_value));
        if(memo.get(index).getData() == null){
            //memo.get(index).setData(tmp.getData());
            memo.set(index, tmp);
            System.out.println("root");
        }
        else{
            Node iterator = memo.get(index);
            System.out.println("next");
            while(iterator.getNext() != null){
                iterator = iterator.getNext();
            }
            iterator.setNext(tmp);
        }
        coun.get(index).plus();
    }

    public String get(String _index){
        int index = hash(_index);
        Node iterator = memo.get(index);

        if(coun.get(index).getcoun() != 0){
            while(iterator != null){
                if(iterator.getData().getValue().equals(_index)){
                    return iterator.getData().getValue();
                }
                iterator = iterator.getNext();
            }
        }
        return "tidak ada";
    }
}
