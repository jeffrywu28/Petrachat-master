package com.petra.petrachat;

public class Data {
    public String index;
    public String Value;

    public Data(){
        this.index = "";
        this.Value = "";
    }

    public Data(String _index, String _value){
        this.index = _index;
        this.Value = _value;
    }

    public void setIndex(String _index){
        this.index = _index;
    }

    public String getIndex(){
        return index;
    }

    public void setValue(String _value){
        this.Value = _value;
    }

    public String getValue(){
        return Value;
    }

}
