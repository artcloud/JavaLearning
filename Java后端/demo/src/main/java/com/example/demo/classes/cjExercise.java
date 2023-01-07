package com.example.demo.classes;

public abstract class cjExercise extends Exercise{

    protected Boolean ifRandomize;
    protected int eKey;

    protected void swap(String[] arr,int a,int b){
        String s=arr[a];
        arr[a]=arr[b];
        arr[b]=s;
    }

    protected void swap(int[] arr,int a,int b){
        int s=arr[a];
        arr[a]=arr[b];
        arr[b]=s;
    }

    public int geteKey(){
        return eKey;
    }
    public void seteKey(int ekey){
        this.eKey=ekey;
    }
    public abstract int getOptionCount();
    public abstract void setOptionCount(int optCount);
    public abstract void init();
}
