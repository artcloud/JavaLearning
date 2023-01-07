package com.example.demo.classes;

import java.util.HashMap;

public abstract class Exercise {
    private int eID;
    private String eType;
    private int difficulty;
    private String stem;
    protected HashMap<Integer, String> options;
    protected HashMap<Integer, String> blanks;
    protected String[][] optionsArray;
    protected String[][] blanksArray;
    protected int[] intArray;
    protected String eKey;
    protected int optionCount;
    protected int blankCount;


    public int geteID(){
        return eID;
    }
    public void seteID(int eid){
        this.eID=eid;
    }
    public String geteType(){
        return eType;
    }
    public void seteType(String etype){
        this.eType=etype;
    }
    public int getDifficulty(){
        return difficulty;
    }
    public void setDifficulty(int diff){
        this.difficulty=diff;
    }
    public String getStem(){
        return stem;
    }
    public void setStem(String ste){
        this.stem=ste;
    }
    public String[][] getBlanksArray(){
        return null;
    }
    public void setBlanks(HashMap<Integer,String> blks){
    }
    public int getBlankCount(){
        return -1;
    }
    public void setBlankCount(int blkCount){
    }
    public int[] getPermutationArray(){
        return null;
    }

    //抽象方法
    public abstract String[][] getOptionsArray();
    public abstract void setOptions(HashMap<Integer, String> opts);
    public abstract int geteKey();
    public abstract void seteKey(int ekey);
    public abstract int getOptionCount();
    public abstract void setOptionCount(int optCount);
    public abstract void init();
}
