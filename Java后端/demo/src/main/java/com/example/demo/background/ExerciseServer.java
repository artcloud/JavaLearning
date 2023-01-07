package com.example.demo.background;


import com.example.demo.classes.*;

import java.sql.ResultSet;
import java.util.*;

public class ExerciseServer {
    String student_ID;
    HashMap<Integer, ExerciseSet> exerciseSets =new HashMap<Integer,ExerciseSet>();
    DBOperator dbo;

    public ExerciseServer(String st_ID){
        this.student_ID=st_ID;
    }

    public void init(){
        dbo=new DBOperator();
    }

    public void generateExercises(int module_ID,int exerciseCount,String relatedKPMode){
        Random r=new Random();
        ExerciseSet es=new ExerciseSet(module_ID);
        exerciseSets.put(module_ID,es);
        int numChoiceExercises,numJudgmentExercises,numBlankExercises=0;
        if(exerciseCount==2){
            numChoiceExercises=2;
            numJudgmentExercises=0;
            numBlankExercises=0;
        }
        else if(exerciseCount==3){
            numChoiceExercises=2;
            numJudgmentExercises=1;
            numBlankExercises=0;
        }
        else if(exerciseCount==4){
            numChoiceExercises=2;
            numJudgmentExercises=1;
            numBlankExercises=1;
        }
        else {
            numChoiceExercises = r.nextInt((int) Math.round(exerciseCount * 0.2)) + (int) Math.round(exerciseCount * 0.4);
            numJudgmentExercises = r.nextInt((int) Math.round(exerciseCount * 0.2)) + (int) Math.round(exerciseCount * 0.3);
            numBlankExercises = exerciseCount - numChoiceExercises - numJudgmentExercises;
        }
        exerciseSets.get(module_ID).init(numChoiceExercises,numJudgmentExercises,numBlankExercises,relatedKPMode,student_ID);
    }

    public HashMap<String,String> correctAndRecordExercises(int module_ID, Map<String,String> answerData){
        exerciseSets.get(module_ID).correctModuleExercises(answerData,student_ID);
        exerciseSets.get(module_ID).recordModuleExercises(student_ID);
        HashMap<String,String> data=new HashMap<String, String>();
        data.put("timeSubmit",exerciseSets.get(module_ID).getSubmitTime());
        data.put("score",String.valueOf(exerciseSets.get(module_ID).getScore()));
        return data;
    }

    public int getExerciseCount(int moduleID){
        return exerciseSets.get(moduleID).getExerciseCount();
    }

    public String[][] getExercise(int moduleID,int exerciseNum){
        return exerciseSets.get(moduleID).getExercise(moduleID,exerciseNum);
    }

    public ArrayList<HashMap<String,String[]>> getKnowledgeSetsAndPoints(String moduleID){
        ArrayList<HashMap<String,String[]>> knowledgeSets=new ArrayList<HashMap<String,String[]>>();
        String sql="select ksID,ksName from knowledgeSet,module where knowledgeSet.moduleId=module.moduleID and knowledgeSet.moduleID="+moduleID;
        String sqlCountKS="select count(*) as count from knowledgeSet,module where knowledgeSet.moduleId=module.moduleID and knowledgeSet.moduleID="+moduleID;
        ResultSet rs=null;
        ResultSet rsCountKS=null;
        DBOperator dbo=new DBOperator();
        rs=dbo.query(sql);
        rsCountKS=dbo.query(sqlCountKS);
        int countKS=0;
        try{
            rsCountKS.next();
            countKS=rsCountKS.getInt("count");
        }catch(Exception e){
            e.printStackTrace();
        }
        String[][] ksNames=new String[countKS][1];
        String[][] ksIDs=new String[countKS][1];
        int indi=0;
        try{
            while(rs.next()){
                knowledgeSets.add(new HashMap<String,String[]>());
                ksIDs[indi][0]=Integer.toString(rs.getInt("ksID"));
                ksNames[indi][0]=rs.getString("ksName");
                indi++;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        for(int i=0;i<countKS;i++){
            knowledgeSets.get(i).put("ksID",ksIDs[i]);
            knowledgeSets.get(i).put("ksName",ksNames[i]);
            String sql2="select kpID,kpName from knowledgePoint where moduleID="+moduleID+" and ksID="+ksIDs[i][0];
            String sqlLearnt="select kpID from learntKnowledgePoint where student_ID='"+student_ID+"' and moduleID="+moduleID+" and ksID="+ksIDs[i][0];
            String sqlCountKP="select count(*) as count from knowledgePoint where moduleID="+moduleID+" and ksID="+ksIDs[i][0];
            ResultSet rs2=dbo.query(sql2);
            ResultSet rsLearnt=dbo.query(sqlLearnt);
            ResultSet rsCountKP=dbo.query(sqlCountKP);
            int countKP=0;
            try{
                rsCountKP.next();
                countKP=rsCountKP.getInt("count");
            }catch(Exception e){
                e.printStackTrace();
            }
            String[] kpNames=new String[countKP];
            String[] kpIDs=new String[countKP];
            ArrayList<String> kpIDsAL=new ArrayList<>();
            String[] learntKPIDs=new String[countKP];
            int indix=0;
            int indixLearnt=0;
            try{
                while(rs2.next()){
                    kpIDs[indix]=Integer.toString(rs2.getInt("kpID"));
                    kpIDsAL.add(Integer.toString(rs2.getInt("kpID")));
                    kpNames[indix]=rs2.getString("kpName");
                    learntKPIDs[indix]="false";
                    indix++;
                }

                while(rsLearnt.next()){
                    String le=Integer.toString(rsLearnt.getInt("kpID"));
                    if(kpIDsAL.contains(le)){
                        learntKPIDs[kpIDsAL.indexOf(le)]="true";
                    }
                    indixLearnt++;
                }
                knowledgeSets.get(i).put("kpIDs",kpIDs);
                knowledgeSets.get(i).put("kpNames",kpNames);
                knowledgeSets.get(i).put("learntKPIDs",learntKPIDs);
                String[] ksLearnt=new String[1];
                if(indix==indixLearnt)
                    ksLearnt[0]="true";
                else
                    ksLearnt[0]="false";
                knowledgeSets.get(i).put("ksLearnt",ksLearnt);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return knowledgeSets;
    }

    public HashMap<String,String> getReviewExerciseSetData(String timeSubmit){
        HashMap<String,String> data=new HashMap<String,String>();
        String sql="select esType,totalTimeUsed,score,correctRate,moduleID from completedExerciseSetRecord where student_ID='"+student_ID+"' and timeSubmit='"+timeSubmit+"'";
        String moduleID=null;
        String moduleName=null;
        ResultSet rs=null;
        rs=dbo.query(sql);
        try{
            rs.next();
            data.put("esType",rs.getString("esType"));
            data.put("totalTimeUsed",rs.getString("totalTimeUsed"));
            data.put("score",rs.getString("score"));
            data.put("correctRate",rs.getString("correctRate"));
            moduleID=rs.getString("moduleID");
        }catch(Exception e){
            e.printStackTrace();
        }
        if(moduleID!=null){
            moduleName=StaticServer.getModuleName(moduleID);
            data.put("moduleName",moduleName);
        }
        else{
            data.put("moduleName",null);
        }
        return data;
    }

    public String[][][] getReviewExerciseData(String timeSubmit){
        String[][][] reviewChoiceExerciseData=getReviewChoiceExerciseData(timeSubmit);
        String[][][] reviewJudgmentExerciseData=getReviewJudgmentExerciseData(timeSubmit);
        String[][][] reviewBlankExerciseData=getReviewBlankExerciseData(timeSubmit);
        String[][][] reviewExerciseData=new String[reviewChoiceExerciseData.length+ reviewJudgmentExerciseData.length+reviewBlankExerciseData.length][3][reviewChoiceExerciseData[0][1].length>reviewBlankExerciseData[0][1].length?reviewChoiceExerciseData[0][1].length:reviewBlankExerciseData[0][1].length];
        int indi=0;
        for(int i=0;i<reviewChoiceExerciseData.length;i++){
            reviewExerciseData[indi]=reviewChoiceExerciseData[i];
            indi++;
        }
        for(int i=0;i<reviewJudgmentExerciseData.length;i++) {
            reviewExerciseData[indi] = reviewJudgmentExerciseData[i];
            indi++;
        }
        for(int i=0;i<reviewBlankExerciseData.length;i++) {
            reviewExerciseData[indi] = reviewBlankExerciseData[i];
            indi++;
        }
        return reviewExerciseData;
    }

    public String[][][] getReviewChoiceExerciseData(String timeSubmit){
        String[][][] reviewChoiceExerciseData;
        int count=0;
        String sqlReviewChoiceExerciseCount="select count(*) as count from completedChoiceExerciseRecord where student_ID='"+student_ID+"' and timeSubmit='"+timeSubmit+"'";
        String sqlCompletedReviewChoiceExercises="select eID,timeUsed,answer,score from completedChoiceExerciseRecord where student_ID='"+student_ID+"' and timeSubmit='"+timeSubmit+"'";
        ResultSet rsReviewChoiceExerciseCount=dbo.query(sqlReviewChoiceExerciseCount);
        ResultSet rsCompletedReviewChoiceExercises=dbo.query(sqlCompletedReviewChoiceExercises);
        int[] ids=null;
        String[] timeUsed=null;
        String[] answers=null;
        String[] scores=null;
        try{
            rsReviewChoiceExerciseCount.next();
            count=rsReviewChoiceExerciseCount.getInt("count");
            ids=new int[count];
            timeUsed=new String[count];
            answers=new String[count];
            scores=new String[count];
            int indi=0;
            while(rsCompletedReviewChoiceExercises.next()){
                ids[indi]=rsCompletedReviewChoiceExercises.getInt("eID");
                timeUsed[indi]=rsCompletedReviewChoiceExercises.getString("timeUsed");
                answers[indi]=rsCompletedReviewChoiceExercises.getString("answer");
                scores[indi]=Float.toString(rsCompletedReviewChoiceExercises.getFloat("score"));
                indi++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        int maxOptionCount=0;
        for(int i=0;i<count;i++){
            String sqlOptions="select count(*) as count from choiceExerciseOption where eID= "+Integer.toString(ids[i]);
            ResultSet rsOptions=null;
            rsOptions=dbo.query(sqlOptions);
            try{
                rsOptions.next();
                if(rsOptions.getInt("count")>maxOptionCount) maxOptionCount=rsOptions.getInt("count");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        int lengthInt=maxOptionCount>8?maxOptionCount:8;
        reviewChoiceExerciseData=new String[count][3][lengthInt];
        for(int i=0;i<count;i++){
            String sqlOptions="select * from choiceExerciseOption where eID= "+Integer.toString(ids[i]);
            ResultSet rsOptions=null;
            rsOptions=dbo.query(sqlOptions);
            try{
                int ind=0;
                while(rsOptions.next()){
                    reviewChoiceExerciseData[i][1][ind]=Integer.toString(ind+1);
                    reviewChoiceExerciseData[i][2][ind]=rsOptions.getString("eOption");
                    ind++;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            String sqlChoiceExercise="select exercise.eID,difficulty,stem,eKey from exercise,choiceExercise where exercise.eID=choiceExercise.eID and exercise.eID="+Integer.toString(ids[i]);
            ResultSet rsChoiceExercise=dbo.query(sqlChoiceExercise);
            try{
                rsChoiceExercise.next();
                reviewChoiceExerciseData[i][0][0]=Integer.toString(rsChoiceExercise.getInt("eID"));
                reviewChoiceExerciseData[i][0][1]="choice";
                reviewChoiceExerciseData[i][0][2]=Integer.toString(rsChoiceExercise.getInt("difficulty"));
                reviewChoiceExerciseData[i][0][3]=rsChoiceExercise.getString("stem");
                reviewChoiceExerciseData[i][0][4]=Integer.toString(rsChoiceExercise.getInt("eKey"));
            }catch(Exception e){
                e.printStackTrace();
            }
            reviewChoiceExerciseData[i][0][5]=timeUsed[i];
            reviewChoiceExerciseData[i][0][6]=answers[i];
            reviewChoiceExerciseData[i][0][7]=scores[i];
        }
        return reviewChoiceExerciseData;
    }

    public String[][][] getReviewJudgmentExerciseData(String timeSubmit){
        String[][][] reviewJudgmentExerciseData;
        int count=0;
        String sqlReviewJudgmentExerciseCount="select count(*) as count from completedJudgmentExerciseRecord where student_ID='"+student_ID+"' and timeSubmit='"+timeSubmit+"'";
        String sqlCompletedReviewJudgmentExercises="select eID,timeUsed,answer,score from completedJudgmentExerciseRecord where student_ID='"+student_ID+"' and timeSubmit='"+timeSubmit+"'";
        ResultSet rsReviewJudgmentExerciseCount=dbo.query(sqlReviewJudgmentExerciseCount);
        ResultSet rsCompletedReviewJudgmentExercises=dbo.query(sqlCompletedReviewJudgmentExercises);
        int[] ids=null;
        String[] timeUsed=null;
        String[] answers=null;
        String[] scores=null;
        try{
            rsReviewJudgmentExerciseCount.next();
            count=rsReviewJudgmentExerciseCount.getInt("count");
            System.out.println(timeSubmit);
            ids=new int[count];
            timeUsed=new String[count];
            answers=new String[count];
            scores=new String[count];
            int indi=0;
            while(rsCompletedReviewJudgmentExercises.next()){
                ids[indi]=rsCompletedReviewJudgmentExercises.getInt("eID");
                timeUsed[indi]=rsCompletedReviewJudgmentExercises.getString("timeUsed");
                answers[indi]=rsCompletedReviewJudgmentExercises.getString("answer");
                scores[indi]=Float.toString(rsCompletedReviewJudgmentExercises.getFloat("score"));
                indi++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        int lengthInt=8;
        reviewJudgmentExerciseData=new String[count][3][lengthInt];
        for(int i=0;i<count;i++){
            reviewJudgmentExerciseData[i][1][0]=Integer.toString(0);
            reviewJudgmentExerciseData[i][2][0]="true";
            reviewJudgmentExerciseData[i][1][1]=Integer.toString(1);
            reviewJudgmentExerciseData[i][2][1]="false";
            String sqlJudgmentExercise="select exercise.eID,difficulty,stem,eKey from exercise,judgmentExercise where exercise.eID=judgmentExercise.eID and exercise.eID="+Integer.toString(ids[i]);
            ResultSet rsJudgmentExercise=dbo.query(sqlJudgmentExercise);
            try{
                rsJudgmentExercise.next();
                reviewJudgmentExerciseData[i][0][0]=Integer.toString(rsJudgmentExercise.getInt("eID"));
                reviewJudgmentExerciseData[i][0][1]="judgment";
                reviewJudgmentExerciseData[i][0][2]=Integer.toString(rsJudgmentExercise.getInt("difficulty"));
                reviewJudgmentExerciseData[i][0][3]=rsJudgmentExercise.getString("stem");
                reviewJudgmentExerciseData[i][0][4]=Integer.toString(rsJudgmentExercise.getInt("eKey"));
            }catch(Exception e){
                e.printStackTrace();
            }
            reviewJudgmentExerciseData[i][0][5]=timeUsed[i];
            reviewJudgmentExerciseData[i][0][6]=answers[i];
            reviewJudgmentExerciseData[i][0][7]=scores[i];
        }
        return reviewJudgmentExerciseData;
    }

    public String[][][] getReviewBlankExerciseData(String timeSubmit){
        String[][][] reviewBlankExerciseData;
        int count=0;
        String sqlReviewBlankExerciseCount="select count(*) as count from completedBlankExerciseRecord where student_ID='"+student_ID+"' and timeSubmit='"+timeSubmit+"'";
        String sqlCompletedReviewBlankExercises="select eID,timeUsed,score from completedBlankExerciseRecord where student_ID='"+student_ID+"' and timeSubmit='"+timeSubmit+"'";
        ResultSet rsReviewBlankExerciseCount=dbo.query(sqlReviewBlankExerciseCount);
        ResultSet rsCompletedReviewBlankExercises=dbo.query(sqlCompletedReviewBlankExercises);
        int[] ids=null;
        String[] timeUsed=null;
        String[] scores=null;
        try{
            rsReviewBlankExerciseCount.next();
            count=rsReviewBlankExerciseCount.getInt("count");
            System.out.println(timeSubmit);
            ids=new int[count];
            timeUsed=new String[count];
            scores=new String[count];
            int indi=0;
            while(rsCompletedReviewBlankExercises.next()){
                ids[indi]=rsCompletedReviewBlankExercises.getInt("eID");
                timeUsed[indi]=rsCompletedReviewBlankExercises.getString("timeUsed");
                scores[indi]=Float.toString(rsCompletedReviewBlankExercises.getFloat("score"));
                indi++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        int maxBlankCount=0;
        for(int i=0;i<count;i++){
            String sqlBlanks="select count(*) as count from blankExerciseBlank where eID= "+Integer.toString(ids[i]);
            ResultSet rsBlanks=dbo.query(sqlBlanks);
            try{
                rsBlanks.next();
                if(rsBlanks.getInt("count")>maxBlankCount) maxBlankCount=rsBlanks.getInt("count");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        int lengthInt=maxBlankCount>8?maxBlankCount:8;
        reviewBlankExerciseData=new String[count][3][lengthInt];
        StringBuffer originalStem=null;
        for(int i=0;i<count;i++){
            String sqlBlankExercise="select exercise.eID,difficulty,stem from exercise,blankExercise where exercise.eID=blankExercise.eID and exercise.eID="+Integer.toString(ids[i]);
            ResultSet rsBlankExercise=dbo.query(sqlBlankExercise);
            try{
                rsBlankExercise.next();
                reviewBlankExerciseData[i][0][0]=Integer.toString(rsBlankExercise.getInt("eID"));
                reviewBlankExerciseData[i][0][1]="blank";
                reviewBlankExerciseData[i][0][2]=Integer.toString(rsBlankExercise.getInt("difficulty"));
                originalStem=new StringBuffer(rsBlankExercise.getString("stem"));
            }catch(Exception e){
                e.printStackTrace();
            }
            reviewBlankExerciseData[i][0][5]=timeUsed[i];
            reviewBlankExerciseData[i][0][7]=scores[i];
            String sqlBlankCount="select count(*) as count from blankExerciseBlank where eID="+reviewBlankExerciseData[i][0][0];
            ResultSet rsBlankCount=dbo.query(sqlBlankCount);
            int blankCount=0;
            try{
                rsBlankCount.next();
                blankCount=rsBlankCount.getInt("count");
            }catch(Exception e){
                e.printStackTrace();
            }
            String sqlBlanks="select blankExerciseBlank.eID,blankExerciseBlank.blankPosition,blankKey,blankAnswer from blankExerciseBlank,completedBlankExerciseBlankRecord where blankExerciseBlank.eID=completedBlankExerciseBlankRecord.eID and blankExerciseBlank.blankPosition=completedBlankExerciseBlankRecord.blankPosition and student_ID='"+student_ID+"' and timeSubmit='"+timeSubmit+"' and blankExerciseBlank.eID="+reviewBlankExerciseData[i][0][0];
            ResultSet rsBlanks=dbo.query(sqlBlanks);
            String[] blankPositions=new String[blankCount];
            try{
                int ind=0;
                while(rsBlanks.next()){
                    reviewBlankExerciseData[i][1][ind]=rsBlanks.getString("blankKey");
                    reviewBlankExerciseData[i][2][ind]=rsBlanks.getString("blankAnswer");
                    blankPositions[ind]=rsBlanks.getString("blankPosition");
                    ind++;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            int deviation=0;
            for(int j=0;j<blankCount;j++){
                originalStem.insert(Integer.parseInt(blankPositions[j])+deviation,"____");
                deviation+=4;
            }
            reviewBlankExerciseData[i][0][3]=originalStem.toString();
        }
        return reviewBlankExerciseData;
    }

    public ArrayList<HashMap<String,String>> getCompletedExerciseSets(){
        System.out.println(1);
        ArrayList<HashMap<String,String>> completedExerciseSets=new ArrayList<HashMap<String,String>>();
        String sqlCompletedExerciseSets="select * from completedExerciseSetRecord,module where module.moduleID=completedExerciseSetRecord.moduleID and student_ID = '"+student_ID+"' order by timeSubmit desc";
        ResultSet rsCompletedExerciseSets=dbo.query(sqlCompletedExerciseSets);
        try{
            while(rsCompletedExerciseSets.next()){
                HashMap<String,String> completedExerciseSet=new HashMap<String,String>();
                completedExerciseSet.put("esType",rsCompletedExerciseSets.getString("esType"));
                completedExerciseSet.put("moduleID",Integer.toString(rsCompletedExerciseSets.getInt("moduleID")));
                completedExerciseSet.put("moduleName",rsCompletedExerciseSets.getString("moduleName"));
                completedExerciseSet.put("timeSubmit",rsCompletedExerciseSets.getString("timeSubmit"));
                completedExerciseSet.put("totalTimeUsed",rsCompletedExerciseSets.getString("totalTimeUsed"));
                completedExerciseSet.put("score",String.valueOf(rsCompletedExerciseSets.getFloat("score")));
                completedExerciseSet.put("correctRate",String.valueOf(rsCompletedExerciseSets.getFloat("correctRate")));
                completedExerciseSets.add(completedExerciseSet);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return completedExerciseSets;
    }

    public HashMap<String,String> getLearningData(){
        HashMap<String,String> completedExerciseData=new HashMap<String,String>();
        String sqlLearningModuleData="select count(*) as count from finishedModule where student_ID='"+student_ID+"'";
        String sqlLearningKPData="select count(*) as count from learntKnowledgePoint where student_ID='"+student_ID+"'";
        ResultSet rsLearningModuleData=dbo.query(sqlLearningModuleData);
        ResultSet rsLearningKPData=dbo.query(sqlLearningKPData);
        try{
            rsLearningModuleData.next();
            completedExerciseData.put("countFinishedModule",Integer.toString(rsLearningModuleData.getInt("count")));
            rsLearningKPData.next();
            completedExerciseData.put("countLearntKP",Integer.toString(rsLearningKPData.getInt("count")));
        }catch(Exception e){
            e.printStackTrace();
        }
        return completedExerciseData;
    }

    public HashMap<String,String> getCompletedExerciseData(){
        HashMap<String,String> completedExerciseData=new HashMap<String,String>();
        String sqlCompletedExerciseData="select count(*) as count,avg(score) as avgScore,avg(correctRate) as avgCorrectRate from completedExerciseSetRecord where student_ID='"+student_ID+"'";
        String sqlCompletedExerciseTotalTimeUsed="select totalTimeUsed from completedExerciseSetRecord where student_ID='"+student_ID+"'";
        ResultSet rsCompletedExerciseData=dbo.query(sqlCompletedExerciseData);
        ResultSet rsCompletedExerciseTotalTimeUsed=dbo.query(sqlCompletedExerciseTotalTimeUsed);
        int count;
        try{
            rsCompletedExerciseData.next();
            count=rsCompletedExerciseData.getInt("count");
            completedExerciseData.put("countExerciseSetCompleted",Integer.toString(count));
            float avgTime=0;
            while(rsCompletedExerciseTotalTimeUsed.next()){
                String ttu=rsCompletedExerciseTotalTimeUsed.getString("totalTimeUsed");
                String[] separatedTtu=ttu.split(":");
                float seconds=Integer.parseInt(separatedTtu[0])*60+Float.parseFloat(separatedTtu[1]);
                avgTime+=seconds/count;
            }
            int avgMinutes=(int)Math.floor(avgTime)/60;
            String avgSeconds=new Formatter().format("%.1f",avgTime-avgMinutes*60).toString();

            if(rsCompletedExerciseData.getInt("count")>0){
                completedExerciseData.put("averageScore",new Formatter().format("%.2f",rsCompletedExerciseData.getFloat("avgScore")).toString());
                completedExerciseData.put("averageCorrectRate",new Formatter().format("%.2f",rsCompletedExerciseData.getFloat("avgCorrectRate")*100).toString()+"%");
                completedExerciseData.put("averageTimeUsed",Integer.toString(avgMinutes)+":"+avgSeconds);
                completedExerciseData.put("averageTimeUsedFloat",Float.toString(avgTime));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return completedExerciseData;
    }

    public HashMap<String,Integer> getRecommendKP(String student_ID){
        HashMap<String,Integer> recommendKP=new HashMap<String,Integer>();
        String[][] unfinishedModules=StaticServer.getUnfinishedModules(student_ID);
        Random r=new Random();
        int xModule=r.nextInt(unfinishedModules[0].length);
        recommendKP.put("moduleID",Integer.parseInt(unfinishedModules[0][xModule]));
        ArrayList<HashMap<String,String[]>> knowledgeSetsAndPoints=getKnowledgeSetsAndPoints(unfinishedModules[0][xModule]);
        int lengthKP;
        int xKS;
        Boolean flag=false;
        for(int i=0;i<knowledgeSetsAndPoints.size();i++){
            if(knowledgeSetsAndPoints.get(i).get("ksLearnt")[0].equals("false")){
                flag=true;
                break;
            }
        }
        if(!flag){
            recommendKP.put("found",-1);
            return recommendKP;
        }
        while(true){
            xKS=r.nextInt(knowledgeSetsAndPoints.size());
            if(knowledgeSetsAndPoints.get(xKS).get("ksLearnt")[0].equals("false")){
                lengthKP=knowledgeSetsAndPoints.get(xKS).get("kpIDs").length;
                recommendKP.put("ksID",Integer.parseInt(knowledgeSetsAndPoints.get(xKS).get("ksID")[0]));
                break;
            }
        }
        int xKP=0;
        while(true){
            xKP=r.nextInt(lengthKP);
            if(knowledgeSetsAndPoints.get(xKS).get("learntKPIDs")[xKP]=="false"){
                recommendKP.put("kpID",Integer.parseInt(knowledgeSetsAndPoints.get(xKS).get("kpIDs")[xKP]));
                break;
            }
        }
        return recommendKP;
    }
}
