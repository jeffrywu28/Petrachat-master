package com.petra.petrachat;

public class counter {
    public int coun;

    public counter(){
        this.coun = 0;
    }
    public counter(int c){
        this.coun = c;
    }
    public void plus(){
        this.coun = coun + 1;
    }
    public int getcoun(){
        return coun;
    }
}
