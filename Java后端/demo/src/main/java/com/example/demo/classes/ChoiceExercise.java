package com.example.demo.classes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class ChoiceExercise extends cjExercise {

    int[] permutationArray;

    public ChoiceExercise(Boolean ifRand){
        ifRandomize=ifRand;
    }

    public String[][] getOptionsArray(){
        return optionsArray;
    }
    public int[] getPermutationArray() {return permutationArray;}

    public void setOptions(HashMap<Integer, String> opts){
        this.options=opts;
    }


    public int getOptionCount(){
        return optionCount;
    }
    public void setOptionCount(int optCount){
        optionCount=optCount;
    }
    public void init(){
        optionsArray=new String[3][optionCount];
        int ind=0;
        permutationArray=new int[optionCount];
        int neweKey=0;
        if(ifRandomize){
            for(Integer i:options.keySet()){
                permutationArray[ind]=ind+1;
                optionsArray[2][ind]=options.get(i);
                if(i==eKey) neweKey=ind;
                ind++;
            }
            Random r=new Random();
            for(int i=1;i<=10;i++){
                int r1=r.nextInt(optionCount);
                int r2=r.nextInt(optionCount);
                swap(permutationArray,r1,r2);
                swap(optionsArray[2],r1,r2);
                if(r1==neweKey)
                    neweKey=r2;
                else if(r2==neweKey)
                    neweKey=r1;
            }
            optionsArray[0][0]=Character.toString((char)(65+neweKey));
            for(int i=0;i<optionCount;i++){
                optionsArray[1][i]=Character.toString((char)(65+i));
            }
        }
    }
}
