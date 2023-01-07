package com.example.demo.classes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class BlankExercise extends Exercise{


    public int geteKey(){
        return -1;
    }
    public void seteKey(int ekey){
    }

    public String[][] getOptionsArray(){
        return optionsArray;
    }
    public String[][] getBlanksArray(){
        return blanksArray;
    }

    public void setBlanks(HashMap<Integer, String> blks){
        this.blanks=blks;
    }

    public void setOptions(HashMap<Integer, String> opts){
        this.options=opts;
    }

    public int getOptionCount(){
        return optionCount;
    }
    public void setOptionCount(int optCount){
        optionCount=optCount;
    }
    public int getBlankCount(){
        return blankCount;
    }
    public void setBlankCount(int blkCount){
        blankCount=blkCount;
    }
    public void init(){
        StringBuffer originalStem=new StringBuffer(getStem());
        int deviation=0;
        blanksArray=new String[3][blankCount];
        int ind=blankCount-1;
        Iterator it = blanks.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
            int i=Integer.parseInt(String.valueOf(entry.getKey()));
            originalStem.insert(i+deviation,"____");
            deviation+=4;
            blanksArray[1][ind]=Integer.toString(i);
            blanksArray[2][ind]=blanks.get(i);
            ind--;
        }
        setStem(originalStem.toString());
    }
}
