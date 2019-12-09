package com.petra.petrachat;

import java.util.ArrayList;
import java.util.List;

public class HashMap {
    public List<Node> memo = new ArrayList<Node>();

    public HashMap(){
        for(int i = 0; i < 26; i++) {
            memo.add(new Node());
        }
    }

    public int hash(String str){
        return str.charAt(0) - 'a';
    }

    public void set(String _index, String _value){
        int index = hash(_index);
        Node tmp = new Node(new Data(_index, _value));
        if(memo.get(index) == null){
            memo.get(index).setData(tmp.getData());
        }
        else{
            Node iterator = memo.get(index);
            while(iterator.getNext() != null){
                iterator = iterator.getNext();
            }
            iterator.setNext(tmp);
        }
    }

    public String get(String _index){
        int index = hash(_index);
        Node iterator = memo.get(index);

        while(iterator != null){
            if(iterator.getData().getIndex() == _index){
                return iterator.getData().getValue();
            }
            iterator = iterator.getNext();
        }
        return "";
    }

}
