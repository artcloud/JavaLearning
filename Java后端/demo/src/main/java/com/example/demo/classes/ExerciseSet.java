package com.example.demo.classes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.background.DBOperator;
import com.example.demo.background.StaticServer;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ExerciseSet {
    private Exercise[] exercises;
    private int moduleID;
    private int exerciseCount;
    private String[] choiceExerciseAnswers;
    private String[] judgmentExerciseAnswers;
    private String[][] blankExerciseAnswers;
    private float score;
    private float[] scores;
    private float correctRate;
    private String[] exerciseTimeUsed;
    private String totalTimeUsed;
    private String timeSubmit;
    DBOperator dbo;

    public ExerciseSet(int module_ID){
        moduleID=module_ID;
    }

    public void init(int numChoiceExercises,int numJudgmentExercises,int numBlankExercises,String relatedKPMode,String student_ID){
        dbo=new DBOperator();
        exerciseCount=numChoiceExercises+numJudgmentExercises+numBlankExercises;
        exercises=new Exercise[exerciseCount];
        generateChoiceExercises(0,numChoiceExercises-1,relatedKPMode,student_ID);
        generateJudgmentExercises(numChoiceExercises,numChoiceExercises+numJudgmentExercises-1,relatedKPMode,student_ID);
        generateBlankExercises(numChoiceExercises+numJudgmentExercises,numChoiceExercises+numJudgmentExercises+numBlankExercises-1,relatedKPMode,student_ID);
    }

    private void generateChoiceExercises(int begin,int end,String relatedKPMode,String student_ID){
        int num=end-begin+1;
        int choiceExerciseNum=0;
        String sqlChoiceExerciseNum="select count(*) as countChoiceExerciseNum from exercise,choiceExercise where exercise.eID=choiceExercise.eID and moduleID="+moduleID;
        String sqlChoiceExercises="select exercise.eID,difficulty,stem,eKey from exercise,choiceExercise where exercise.eID=choiceExercise.eID and moduleID="+moduleID;
        ResultSet rsCountChoiceExercises=dbo.query(sqlChoiceExerciseNum);
        ResultSet rsChoiceExercises=dbo.query(sqlChoiceExercises);
        try{
            rsCountChoiceExercises.next();
            choiceExerciseNum=rsCountChoiceExercises.getInt("countChoiceExerciseNum");
        }catch(Exception e){
            e.printStackTrace();
        }
        Random r=new Random();
        ArrayList<Integer> choiceExercisePositions = new ArrayList<Integer>();
        for(int i=1;i<=num;i++){
            while(true) {
                int x=r.nextInt(choiceExerciseNum);
                Boolean flag=StaticServer.checkRelatedKPs(x,student_ID,relatedKPMode);
                if (flag&&!choiceExercisePositions.contains(x)) {
                    choiceExercisePositions.add(x);
                    break;
                }
            }
        }
        int ind=0;
        int now=begin;
        try{
            while(rsChoiceExercises.next()){
                if(choiceExercisePositions.contains(ind)){
                    Exercise ex=new ChoiceExercise(true);
                    ex.seteID(rsChoiceExercises.getInt("eID"));
                    ex.seteType("choice");
                    ex.setDifficulty(rsChoiceExercises.getInt("difficulty"));
                    ex.setStem(rsChoiceExercises.getString("stem"));
                    ex.seteKey(rsChoiceExercises.getInt("eKey"));
                    String sqlOptions="select * from choiceExerciseOption where eID= "+Integer.toString(rsChoiceExercises.getInt("eID"));
                    ResultSet rsOptions=null;
                    rsOptions=dbo.query(sqlOptions);
                    HashMap<Integer, String> options=new HashMap<Integer,String>();
                    int countOptions=0;
                    while(rsOptions.next()){
                        options.put(rsOptions.getInt("eOptionNum"),rsOptions.getString("eOption"));
                        countOptions++;
                    }
                    ex.setOptions(options);
                    ex.setOptionCount(countOptions);
                    ex.init();
                    exercises[now]=ex;
                    now++;
                }
                ind++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void generateJudgmentExercises(int begin,int end,String relatedKPMode,String student_ID){
        int num=end-begin+1;
        int judgmentExerciseNum=0;
        String sqlJudgmentExerciseNum="select count(*) as countJudgmentExerciseNum from exercise,judgmentExercise where exercise.eID=judgmentExercise.eID and moduleID="+moduleID;
        String sqlJudgmentExercises="select exercise.eID,difficulty,stem,eKey from exercise,judgmentExercise where exercise.eID=judgmentExercise.eID and moduleID="+moduleID;
        ResultSet rsCountJudgmentExercises=null;
        ResultSet rsJudgmentExercises=null;
        rsCountJudgmentExercises=dbo.query(sqlJudgmentExerciseNum);
        rsJudgmentExercises=dbo.query(sqlJudgmentExercises);
        try{
            rsCountJudgmentExercises.next();
            judgmentExerciseNum=rsCountJudgmentExercises.getInt("countJudgmentExerciseNum");
        }catch(Exception e){
            e.printStackTrace();
        }
        Random r=new Random();
        ArrayList<Integer> judgmentExercisePositions = new ArrayList<Integer>();
        for(int i=1;i<=num;i++){
            while(true) {
                int x=r.nextInt(judgmentExerciseNum);
                Boolean flag=StaticServer.checkRelatedKPs(x,student_ID,relatedKPMode);
                if (flag&&!judgmentExercisePositions.contains(x)) {
                    judgmentExercisePositions.add(x);
                    break;
                }
            }
        }
        int ind=0;
        int now=begin;
        try{
            while(rsJudgmentExercises.next()){
                if(judgmentExercisePositions.contains(ind)){
                    Exercise ex=new JudgmentExercise(true);
                    ex.seteID(rsJudgmentExercises.getInt("eID"));
                    ex.seteType("judgment");
                    ex.setDifficulty(rsJudgmentExercises.getInt("difficulty"));
                    ex.setStem(rsJudgmentExercises.getString("stem"));
                    ex.seteKey(rsJudgmentExercises.getInt("eKey"));
                    ex.init();
                    exercises[now]=ex;
                    now++;
                }
                ind++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void generateBlankExercises(int begin,int end,String relatedKPMode,String student_ID){
        int num=end-begin+1;
        int blankExerciseNum=0;
        String sqlBlankExerciseNum="select count(*) as countBlankExerciseNum from exercise,blankExercise where exercise.eID=blankExercise.eID and moduleID="+moduleID;
        String sqlBlankExercises="select exercise.eID,difficulty,stem from exercise,blankExercise where exercise.eID=blankExercise.eID and moduleID="+moduleID;
        ResultSet rsCountBlankExercises=null;
        ResultSet rsBlankExercises=null;
        rsCountBlankExercises=dbo.query(sqlBlankExerciseNum);
        rsBlankExercises=dbo.query(sqlBlankExercises);
        try{
            rsCountBlankExercises.next();
            blankExerciseNum=rsCountBlankExercises.getInt("countBlankExerciseNum");
        }catch(Exception e){
            e.printStackTrace();
        }
        Random r=new Random();
        ArrayList<Integer> blankExercisePositions = new ArrayList<Integer>();
        for(int i=1;i<=num;i++){
            while(true) {
                int x=r.nextInt(blankExerciseNum);
                Boolean flag=StaticServer.checkRelatedKPs(x,student_ID,relatedKPMode);
                if (flag&&!blankExercisePositions.contains(x)) {
                    blankExercisePositions.add(x);
                    break;
                }
            }
        }
        int ind=0;
        int now=begin;
        try{
            while(rsBlankExercises.next()){
                if(blankExercisePositions.contains(ind)){
                    Exercise ex=new BlankExercise();
                    ex.seteID(rsBlankExercises.getInt("eID"));
                    ex.seteType("blank");
                    ex.setDifficulty(rsBlankExercises.getInt("difficulty"));
                    ex.setStem(rsBlankExercises.getString("stem"));
                    String sqlOptions="select * from blankExerciseBlank where eID= "+rsBlankExercises.getString("eID");
                    ResultSet rsOptions=null;
                    rsOptions=dbo.query(sqlOptions);
                    HashMap<Integer, String> options=new HashMap<Integer,String>();
                    int countBlanks=0;
                    while(rsOptions.next()){
                        options.put(rsOptions.getInt("blankPosition"),rsOptions.getString("blankKey"));
                        countBlanks++;
                    }
                    ex.setBlanks(options);
                    ex.setBlankCount(countBlanks);
                    ex.init();
                    exercises[now]=ex;
                    now++;
                }
                ind++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void correctModuleExercises(Map<String,String> answerData,String student_ID){
        Date date=new Date();
        Timestamp timeSubmitStamp=new Timestamp(date.getTime());
        timeSubmit=String.valueOf(timeSubmitStamp);
        JSONArray jsChoiceExercise= JSON.parseArray(answerData.get("choiceExerciseData"));
        JSONArray jsJudgmentExercise=JSON.parseArray(answerData.get("judgmentExerciseData"));
        JSONArray jsBlankExercise=JSON.parseArray(answerData.get("blankExerciseData"));
        JSONArray jsExerciseTimeUsed=JSON.parseArray(answerData.get("exerciseTimeUsed"));

        score=0;
        scores=new float[exerciseCount];
        correctRate=0;
        correctChoiceExercises(jsChoiceExercise);
        correctJudgmentExercises(jsJudgmentExercise);
        correctBlankExercises(jsBlankExercise);
        if(score>=90) StaticServer.setModuleFinished(student_ID,moduleID);
        exerciseTimeUsed=new String[exerciseCount];
        for(int i=0;i<exerciseCount;i++){
            Float timeUsed=Float.parseFloat(String.valueOf(jsExerciseTimeUsed.get(i)));
            int exerciseMinutes=(int)Math.floor(timeUsed)/60;
            float exerciseSeconds=timeUsed-exerciseMinutes*60;
            exerciseTimeUsed[i]=Integer.toString(exerciseMinutes)+":"+Float.toString(exerciseSeconds);
        }
        Float timeUsed=Float.parseFloat(answerData.get("totalTimeUsed"));
        int totalMinutes=(int)Math.floor(timeUsed)/60;
        float totalSeconds=timeUsed-totalMinutes*60;
        totalTimeUsed=Integer.toString(totalMinutes)+":"+Float.toString(totalSeconds);
    }

    public void correctChoiceExercises(JSONArray jsChoiceExercise){
        choiceExerciseAnswers=new String[exerciseCount];
        for(int i=0;i<exerciseCount;i++){
            if(geteType(i)=="choice"){
                String answer=String.valueOf(jsChoiceExercise.get(i));
                choiceExerciseAnswers[i]=answer;
                if(answer.equals(getNewEKey(i))) {
                    score+=(float)100/exerciseCount;
                    scores[i]=(float)100/exerciseCount;
                    correctRate+=(float)1/exerciseCount;
                }
            }
        }
    }

    public void correctJudgmentExercises(JSONArray jsJudgmentExercise){
        judgmentExerciseAnswers=new String[exerciseCount];
        for(int i=0;i<exerciseCount;i++){
            if(geteType(i)=="judgment"){
                String answer=String.valueOf(jsJudgmentExercise.get(i));
                judgmentExerciseAnswers[i]=answer;
                if((answer.equals("true")&&geteKey(i)==1)||(answer.equals("false")&&geteKey(i)==0)){
                    score+=(float)100/exerciseCount;
                    scores[i]=(float)100/exerciseCount;
                    correctRate+=(float)1/exerciseCount;
                }
            }
        }
    }

    public void correctBlankExercises(JSONArray jsBlankExercise){
        int maxBlankCount=0;
        for(int i=0;i<exerciseCount;i++){
            if(geteType(i)=="blank"&&getBlankCount(i)>maxBlankCount){
                maxBlankCount=getBlankCount(i);
            }
        }
        blankExerciseAnswers=new String[exerciseCount][maxBlankCount];
        for(int i=0;i<exerciseCount;i++){
            if(geteType(i)=="blank"){
                String[][] blksArray=getBlanksArray(i);
                JSONArray jso=(JSONArray)jsBlankExercise.get(i);
                if(jso.size()==getBlankCount(i)){
                    Boolean flag=true;
                    for(int j=0;j<getBlankCount(i);j++){
                        String answer=String.valueOf(jso.get(j));
                        blankExerciseAnswers[i][j]=answer;
                        if(answer.equals(blksArray[2][j])){
                            score+=(float)(100/exerciseCount)/getBlankCount(i);
                            scores[i]=(float)(100/exerciseCount)/getBlankCount(i);
                        }
                        else flag=false;
                    }
                    if(flag) correctRate+=(float)1/exerciseCount;
                }
                else{
                    for(int j=0;j<getBlankCount(i);j++) {
                        blankExerciseAnswers[i][j] = null;
                    }
                }
            }
        }
    }

    public void recordModuleExercises(String student_ID){
        String sqlExerciseSetRecord="insert into completedExerciseSetRecord values ('"+student_ID+"','moduleExercise',"+moduleID+",'"+timeSubmit+"','"+totalTimeUsed+"',"+Float.toString(score)+","+Float.toString(correctRate)+")";
        dbo.update(sqlExerciseSetRecord);
        recordChoiceExercises(student_ID);
        recordJudgmentExercises(student_ID);
        recordBlankExercises(student_ID);
    }

    public void recordChoiceExercises(String student_ID){
        for(int i=0;i<exerciseCount;i++){
            if(geteType(i)=="choice"){
                String answerNum;
                if(choiceExerciseAnswers[i]!="null"&&choiceExerciseAnswers[i]!=""){
                    answerNum=Integer.toString(exercises[i].getPermutationArray()[Integer.valueOf(choiceExerciseAnswers[i].charAt(0))-65]);
                }
                else{
                    answerNum=null;
                }
                String sqlChoiceExerciseRecord="insert into completedChoiceExerciseRecord values ('"+student_ID+"','"+timeSubmit+"',"+Integer.toString(geteID(i))+",'"+exerciseTimeUsed[i]+"',"+answerNum+","+scores[i]+")";
                dbo.update(sqlChoiceExerciseRecord);
            }
        }
    }

    public void recordJudgmentExercises(String student_ID){
        for(int i=0;i<exerciseCount;i++){
            if(geteType(i)=="judgment"){
                String answerNum;
                if(judgmentExerciseAnswers[i].equals("true")){
                    answerNum="1";
                }
                else if(judgmentExerciseAnswers[i].equals("false")){
                    answerNum="0";
                }
                else{
                    answerNum=null;
                }
                String sqlJudgmentExerciseRecord="insert into completedJudgmentExerciseRecord values ('"+student_ID+"','"+timeSubmit+"',"+Integer.toString(geteID(i))+",'"+exerciseTimeUsed[i]+"',"+answerNum+","+scores[i]+")";
                dbo.update(sqlJudgmentExerciseRecord);
            }
        }
    }

    public void recordBlankExercises(String student_ID){
        for(int i=0;i<exerciseCount;i++){
            if(geteType(i)=="blank"){
                String[][] blksArray=getBlanksArray(i);
                String sqlBlankExerciseRecord="insert into completedBlankExerciseRecord values ('"+student_ID+"','"+timeSubmit+"',"+Integer.toString(geteID(i))+",'"+exerciseTimeUsed[i]+"',"+scores[i]+")";
                dbo.update(sqlBlankExerciseRecord);
                for(int j=0;j<getBlankCount(i);j++){
                    String sqlBlankExerciseBlankRecord="insert into completedBlankExerciseBlankRecord values ('"+student_ID+"','"+timeSubmit+"',"+Integer.toString(geteID(i))+",'"+blksArray[1][j]+"','"+blankExerciseAnswers[i][j]+"')";
                    dbo.update(sqlBlankExerciseBlankRecord);
                }
            }
        }
    }

    public int getExerciseCount(){
        return exerciseCount;
    }

    public int geteID(int num){
        return exercises[num].geteID();
    }

    public int geteKey(int num){
        if(exercises[num].geteType()=="choice"||exercises[num].geteType()=="judgment"){
            return exercises[num].geteKey();
        }
        return -1;
    }

    public String geteType(int num){
        return exercises[num].geteType();
    }

    public String[][] getExercise(int moduleID,int exerciseNum){
        String[][] data=null;
        String eType=geteType(exerciseNum);
        if(eType=="choice"){
            int optCount=exercises[exerciseNum].getOptionCount();
            data=new String[3][optCount>3?optCount:3];
            String[][] optionsArray=getOptionsArray(exerciseNum);
            data[0][0]=optionsArray[0][0];
            data[0][1]=getStem(exerciseNum);
            data[0][2]=eType;
            data[1]= Arrays.copyOf(optionsArray[1],optCount);
            data[2]=Arrays.copyOf(optionsArray[2],optCount);
        }
        else if(eType=="judgment"){
            data=new String[2][3];
            String[][] optionsArray=getOptionsArray(exerciseNum);
            data[0][0]=optionsArray[0][0];
            data[0][1]=getStem(exerciseNum);
            data[0][2]=eType;
            data[1]=Arrays.copyOf(optionsArray[1],2);
        }
        else if (eType == "blank") {
            int blkCount=exercises[exerciseNum].getBlankCount();
            int arrayLength=blkCount>=4?blkCount:4;
            data=new String[3][arrayLength];
            String[][] blanksArray=getBlanksArray(exerciseNum);
            data[0][1]=getStem(exerciseNum);
            data[0][2]=eType;
            data[0][3]=Integer.toString(getBlankCount(exerciseNum));
            data[1]=Arrays.copyOf(blanksArray[1],blkCount);
            data[2]=Arrays.copyOf(blanksArray[2],blkCount);
        }
        return data;
    }

    public String getNewEKey(int num){
        if(exercises[num].geteType()=="choice"){
            return exercises[num].getOptionsArray()[0][0];
        }
        return null;
    }

    public String[][] getOptionsArray(int num){
        if(exercises[num].geteType()=="choice"||exercises[num].geteType()=="judgment"){
            return exercises[num].getOptionsArray();
        }
        return null;
    }

    public String[][] getBlanksArray(int num){
        if(exercises[num].geteType()=="blank"){
            return exercises[num].getBlanksArray();
        }
        return null;
    }

    public int getBlankCount(int num){
        if(exercises[num].geteType()=="blank"){
            return exercises[num].getBlankCount();
        }
        return -1;
    }



    public String getStem(int num){
        return exercises[num].getStem();
    }

    public String getSubmitTime(){
        return timeSubmit;
    }

    public float getScore(){
        return score;
    }
}
