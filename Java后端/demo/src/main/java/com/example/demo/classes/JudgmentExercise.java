package com.example.demo.classes;

import java.util.HashMap;
import java.util.Random;

public class JudgmentExercise extends cjExercise {
    public String[][] getOptionsArray(){
        return optionsArray;
    }

    public JudgmentExercise(Boolean ifRand){
        ifRandomize=ifRand;
    }

    public void setOptions(HashMap<Integer, String> opts){
    }

    public int getOptionCount(){
        return 2;
    }
    public void setOptionCount(int optCount){
    }
    public void init(){
        optionsArray=new String[2][2];
        if(ifRandomize){
            optionsArray[1][0]="true";
            optionsArray[1][1]="false";
            Random r=new Random();
            for(int i=1;i<=5;i++){
                int r1=r.nextInt(2);
                int r2=r.nextInt(2);
                swap(optionsArray[1],r1,r2);
            }
            optionsArray[0][0]=(eKey==1?"true":"false");
        }
    }
}
