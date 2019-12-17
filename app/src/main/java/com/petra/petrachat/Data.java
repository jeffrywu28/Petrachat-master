package com.petra.petrachat;

public class Data {
    public String Value;

    public Data(){
        this.Value = null;
    }
    public Data(String _value){
        this.Value = _value;
    }
    public void setValue(String _value){
        this.Value = _value;
    }
    public String getValue(){
        return Value;
    }
}
