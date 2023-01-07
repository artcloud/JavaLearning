package com.example.demo.background;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.sql.ResultSet;
import java.util.*;

public class StaticServer {

    public static String createAccount(String ID,String password){
        String sql="select * from student where student_ID='"+ID+"'";
        String sql2="insert into student values ('"+ID+"','"+password+"')";
        DBOperator dbo=new DBOperator();
        ResultSet rs=dbo.query(sql);
        Boolean duplicated=false;
        try {
            if(rs.next()) duplicated=true;
        }catch(Exception e){
            e.printStackTrace();
        }
        if(!duplicated){
            dbo.update(sql2);
            return "success";
        }
        else{
            return "duplicated";
        }
    }

    public static String login(String ID,String password){
        String cc="";

        String sql2="select student_password from student where student_ID = '"+ID+"'";
        System.out.println(sql2);
        DBOperator dbo=new DBOperator();
        ResultSet rs=dbo.query(sql2);
        try {
            if(rs.next()) cc = rs.getString("student_password");
        }catch(Exception e){
            e.printStackTrace();
        }

        if(ID.equals("admin")&&password.equals("admin")){
            return "adminsuccess";
        }
        if(cc!=null&&cc!=""&&cc.equals(password)){
            return "success";
        }
        else return "fail";
    }

    public static ArrayList<HashMap<String,String>> getModules(){
        ArrayList<HashMap<String,String>> modules=new ArrayList<HashMap<String,String>>();
        String sql="select module.moduleID,moduleName from module";
        ResultSet rs=null;
        DBOperator dbo=new DBOperator();
        rs=dbo.query(sql);
        int indi=0;
        try{
            while(rs.next()){
                HashMap<String,String> module=new HashMap<String,String>();
                module.put("moduleID",Integer.toString(rs.getInt("moduleID")));
                module.put("moduleName",rs.getString("moduleName"));
                indi++;
                modules.add(module);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return modules;
    }

    public static String[][] getFinishedModules(String ID){
        String sql="select module.moduleID,moduleName from finishedModule,module where finishedModule.moduleID=module.moduleID and student_ID = '"+ID+"'";
        ResultSet rs=null;
        DBOperator dbo=new DBOperator();
        rs=dbo.query(sql);
        String[] moduleNames=new String[10];
        String[] moduleIDs=new String[10];
        int indi=0;
        try{
            while(rs.next()){
                moduleIDs[indi]=Integer.toString(rs.getInt("moduleID"));
                moduleNames[indi]=rs.getString("moduleName");
                indi++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        String[][] modules=new String[2][indi];
        modules[0]= Arrays.copyOf(moduleIDs,indi);
        modules[1]= Arrays.copyOf(moduleNames,indi);
        return modules;
    }


    public static String[][] getUnfinishedModules(String ID){
        String sql="select module.moduleID,moduleName from module where module.moduleID not in (select module.moduleID from finishedModule,module where finishedModule.moduleID=module.moduleID and student_ID = '"+ID+"') and module.moduleID not in (select module.moduleID from moduleDependency,module where moduleDependency.moduleID=module.moduleID and dependency not in (select moduleID from finishedModule where student_ID = '"+ID+"'))";
        ResultSet rs=null;
        DBOperator dbo=new DBOperator();
        rs=dbo.query(sql);
        String[] moduleName=new String[10];
        String[] moduleID=new String[10];
        int indi=0;
        try{
            while(rs.next()){
                moduleID[indi]=Integer.toString(rs.getInt("moduleID"));
                moduleName[indi]=rs.getString("moduleName");
                indi++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        String[][] modules=new String[2][indi];
        modules[0]= Arrays.copyOf(moduleID,indi);
        modules[1]= Arrays.copyOf(moduleName,indi);
        return modules;
    }

    public static String[][] getLockedModules(String ID){
        String sql="select module.moduleID,moduleName from moduleDependency,module where moduleDependency.moduleID=module.moduleID and dependency not in (select moduleID from finishedModule where student_ID = '"+ID+"')";
        ResultSet rs=null;
        DBOperator dbo=new DBOperator();
        rs=dbo.query(sql);
        String[] moduleName=new String[10];
        String[] moduleID=new String[10];
        int indi=0;
        try{
            while(rs.next()){
                moduleID[indi]=Integer.toString(rs.getInt("moduleID"));
                moduleName[indi]=rs.getString("moduleName");
                indi++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        String[][] modules=new String[2][indi];
        modules[0]= Arrays.copyOf(moduleID,indi);
        modules[1]= Arrays.copyOf(moduleName,indi);
        return modules;
    }

    public static void setModuleFinished(String student_ID,int moduleID){
        String sql="insert into finishedModule values ('"+student_ID+"',"+moduleID+")";
        DBOperator dbo=new DBOperator();
        dbo.update(sql);
    }

    public static String[] getDependencyNames(String moduleID){
        String sql="select moduleName from module,moduleDependency where  module.moduleID=moduleDependency.dependency and moduleDependency.moduleID="+moduleID;
        ResultSet rs=null;
        DBOperator dbo=new DBOperator();
        rs=dbo.query(sql);
        String[] dependencyName=new String[10];
        int indi=0;
        try{
            while(rs.next()){
                dependencyName[indi]=rs.getString("moduleName");
                indi++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        String[] dependencyName2=new String[indi];
        dependencyName2= Arrays.copyOf(dependencyName,indi);
        return dependencyName2;
    }

    public static String getModuleName(String moduleID){
        String sql="select moduleName from module where moduleID="+moduleID;
        ResultSet rs=null;
        DBOperator dbo=new DBOperator();
        rs=dbo.query(sql);
        String moduleName=null;
        try{
            while(rs.next()){
                moduleName=rs.getString("moduleName");
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return moduleName;
    }

    public static String getModuleNameInReview(String moduleID,String timeSubmit){
        String sql="select moduleName from module where moduleID="+moduleID;
        ResultSet rs=null;
        DBOperator dbo=new DBOperator();
        rs=dbo.query(sql);
        String moduleName=null;
        try{
            while(rs.next()){
                moduleName=rs.getString("moduleName");
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return moduleName;
    }

    public static ArrayList<HashMap<String,String[]>> getModulesAndKnowledgeSets(){
        ArrayList<HashMap<String,String[]>> modules=new ArrayList<HashMap<String,String[]>>();
        String sql="select moduleID,moduleName from module";
        String sqlCountModule="select count(*) as count from module";
        ResultSet rs=null;
        ResultSet rsCountModule=null;
        DBOperator dbo=new DBOperator();
        rs=dbo.query(sql);
        rsCountModule=dbo.query(sqlCountModule);
        int countModule=0;
        try{
            rsCountModule.next();
            countModule=rsCountModule.getInt("count");
        }catch(Exception e){
            e.printStackTrace();
        }
        String[][] moduleNames=new String[countModule][1];
        String[][] moduleIDs=new String[countModule][1];
        int indi=0;
        try{
            while(rs.next()){
                modules.add(new HashMap<String,String[]>());
                moduleIDs[indi][0]=Integer.toString(rs.getInt("moduleID"));
                moduleNames[indi][0]=rs.getString("moduleName");
                indi++;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        for(int i=0;i<countModule;i++){
            modules.get(i).put("moduleID",moduleIDs[i]);
            modules.get(i).put("moduleName",moduleNames[i]);
            String sql2="select ksID,ksName from knowledgeSet where moduleID="+moduleIDs[i][0];
            String sqlCountKS="select count(*) as count from knowledgeSet where moduleID="+moduleIDs[i][0];
            ResultSet rs2=dbo.query(sql2);
            ResultSet rsCountKS=dbo.query(sqlCountKS);
            int countKS=0;
            try{
                rsCountKS.next();
                countKS=rsCountKS.getInt("count");
            }catch(Exception e){
                e.printStackTrace();
            }
            String[] ksNames=new String[countKS];
            String[] ksIDs=new String[countKS];
            int indix=0;
            try{
                while(rs2.next()){
                    ksIDs[indix]=Integer.toString(rs2.getInt("ksID"));
                    ksNames[indix]=rs2.getString("ksName");
                    indix++;
                }
                modules.get(i).put("ksIDs",ksIDs);
                modules.get(i).put("ksNames",ksNames);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return modules;
    }


    public static String[] getKnowledgePoint(String moduleID,String ksID,String kpID){
        String sql="select kpName,content from knowledgePoint where moduleID= "+moduleID+" and ksID= "+ksID+" and kpID= "+kpID;
        ResultSet rs=null;
        DBOperator dbo=new DBOperator();
        rs=dbo.query(sql);
        String[] kp=new String[2];
        try{
            rs.next();
            kp[0]=rs.getString("kpName");
            kp[1]=rs.getString("content");
        }catch(Exception e){
            e.printStackTrace();
        }
        return kp;
    }

    public static String[] getPrevkp(String moduleID,String ksID,String kpID){
        String[] data=new String[5];
        String sqlmin="select min(kpID) as minkpid from knowledgePoint where moduleID= "+moduleID+" and ksID= "+ksID;
        int kpid=Integer.parseInt(kpID);
        ResultSet rs=null;
        DBOperator dbo=new DBOperator();
        rs=dbo.query(sqlmin);
        int minkpid=0;
        try{
            rs.next();
            minkpid=rs.getInt("minkpid");
        }catch(Exception e){
            e.printStackTrace();
        }
        if(kpid>minkpid){
            String sql="select ksID,kpID,kpName,content from knowledgePoint where moduleID= "+moduleID+" and ksID= "+ksID+" and kpID="+kpID+"-1";
            ResultSet rs2=null;
            rs2=dbo.query(sql);
            data[0]=Integer.toString(minkpid);
            try{
                rs2.next();
                data[1]=Integer.toString(rs2.getInt("ksID"));
                data[2]=Integer.toString(rs2.getInt("kpID"));
                data[3]=rs2.getString("kpName");
                data[4]=rs2.getString("content");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        else{
            int maxkpid=0;
            String sql0="select max(kpID) as maxkpid from knowledgePoint where moduleID= "+moduleID+" and ksID= "+ksID+"-1";
            ResultSet rs2=null;
            rs2=dbo.query(sql0);
            try{
                rs2.next();
                maxkpid=rs2.getInt("maxkpid");
            }catch(Exception e){
                e.printStackTrace();
            }
            String sql="select ksID,kpID,kpName,content from knowledgePoint where moduleID= "+moduleID+" and ksID= "+ksID+"-1 and kpID="+maxkpid;
            ResultSet rs3=null;
            rs3=dbo.query(sql);
            try{
                rs3.next();
                data[1]=Integer.toString(rs3.getInt("ksID"));
                data[2]=Integer.toString(rs3.getInt("kpID"));
                data[3]=rs3.getString("kpName");
                data[4]=rs3.getString("content");
            }catch(Exception e){
                e.printStackTrace();
            }
            String sql2="select min(kpID) as minkpid from knowledgePoint where moduleID= "+moduleID+" and ksID= "+ksID+"-1";
            ResultSet rs4=null;
            rs4=dbo.query(sql2);
            try{
                rs4.next();
                data[0]=Integer.toString(rs4.getInt("minkpid"));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return data;
    }

    public static String[] getNextkp(String moduleID,String ksID,String kpID){
        String[] data=new String[5];
        String sqlmax="select max(kpID) as maxkpid from knowledgePoint where moduleID= "+moduleID+" and ksID= "+ksID;
        int kpid=Integer.parseInt(kpID);
        ResultSet rs=null;
        DBOperator dbo=new DBOperator();
        rs=dbo.query(sqlmax);
        int maxkpid=0;
        try{
            rs.next();
            maxkpid=rs.getInt("maxkpid");
        }catch(Exception e){
            e.printStackTrace();
        }
        if(kpid<maxkpid){
            String sql="select ksID,kpID,kpName,content from knowledgePoint where moduleID= "+moduleID+" and ksID= "+ksID+" and kpID="+kpID+"+1";
            ResultSet rs2=null;
            rs2=dbo.query(sql);
            data[0]=Integer.toString(maxkpid);
            try{
                rs2.next();
                data[1]=Integer.toString(rs2.getInt("ksID"));
                data[2]=Integer.toString(rs2.getInt("kpID"));
                data[3]=rs2.getString("kpName");
                data[4]=rs2.getString("content");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        else{
            int minkpid=0;
            String sql0="select min(kpID) as minkpid from knowledgePoint where moduleID= "+moduleID+" and ksID= "+ksID+"+1";
            ResultSet rs2=null;
            rs2=dbo.query(sql0);
            try{
                rs2.next();
                minkpid=rs2.getInt("minkpid");
            }catch(Exception e){
                e.printStackTrace();
            }
            String sql="select ksID,kpID,kpName,content from knowledgePoint where moduleID= "+moduleID+" and ksID= "+ksID+"+1 and kpID="+minkpid;
            ResultSet rs3=null;
            rs3=dbo.query(sql);
            try{
                rs3.next();
                data[1]=Integer.toString(rs3.getInt("ksID"));
                data[2]=Integer.toString(rs3.getInt("kpID"));
                data[3]=rs3.getString("kpName");
                data[4]=rs3.getString("content");
            }catch(Exception e){
                e.printStackTrace();
            }
            String sql2="select max(kpID) as maxkpid from knowledgePoint where moduleID= "+moduleID+" and ksID= "+ksID+"+1";
            ResultSet rs4=null;
            rs4=dbo.query(sql2);
            try{
                rs4.next();
                data[0]=Integer.toString(rs4.getInt("maxkpid"));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return data;
    }

    public static String[] getMinMaxkp(String moduleID){
        String[] data=new String[4];
        String sql0="select min(kpID) as minkpid from knowledgePoint where ksID=1 and moduleID= "+moduleID;
        ResultSet rs0=null;
        DBOperator dbo=new DBOperator();
        rs0=dbo.query(sql0);
        data[0]="1";
        try{
            rs0.next();
            data[1]=rs0.getString("minkpid");
        }catch(Exception e){
            e.printStackTrace();
        }
        String sql="select max(ksID) as maxksid from knowledgePoint where moduleID= "+moduleID;
        ResultSet rs=null;
        rs=dbo.query(sql);
        try{
            rs.next();
            data[2]=rs.getString("maxksid");
        }catch(Exception e){
            e.printStackTrace();
        }
        String sql2="select max(kpID) as maxkpid from knowledgePoint where ksID = "+data[2]+" and moduleID= "+moduleID;
        ResultSet rs2=null;
        rs2=dbo.query(sql2);
        try{
            rs2.next();
            data[3]=rs2.getString("maxkpid");
        }catch(Exception e){
            e.printStackTrace();
        }
        return data;
    }

    public static Boolean getKPIfLearnt(String student_ID,String moduleID,String ksID,String kpID){
        String sql="select * from learntKnowledgePoint where student_ID = '"+student_ID+"' and moduleID = "+moduleID+" and ksID = "+ksID+" and kpID = "+kpID;
        ResultSet rs=null;
        DBOperator dbo=new DBOperator();
        rs=dbo.query(sql);

        try{
            if(rs.next()){
                return true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void setFinishLearningKP(String student_ID,String moduleID,String ksID,String kpID){
        String sql="insert into learntKnowledgePoint values ('"+student_ID+"',"+moduleID+","+ksID+","+kpID+")";
        DBOperator dbo=new DBOperator();
        dbo.update(sql);
    }

    public static Boolean getKPsIfLearnt(String student_ID,String moduleID){
        int countKPs=0;
        int countLearntKPs=0;
        String sql="select count(*) as countKPs from knowledgePoint where moduleID = "+moduleID;
        String sqlLearnt="select count(*) as countLearntKPs from learntKnowledgePoint where student_ID = '"+student_ID+"' and moduleID = "+moduleID;
        ResultSet rs=null;
        ResultSet rsLearnt=null;
        DBOperator dbo=new DBOperator();
        DBOperator dboLearnt=new DBOperator();
        rs=dbo.query(sql);
        rsLearnt=dbo.query(sqlLearnt);

        try{
            rs.next();
            countKPs=rs.getInt("countKPs");
            rsLearnt.next();
            countLearntKPs=rsLearnt.getInt("countLearntKPs");
        }catch(Exception e){
            e.printStackTrace();
        }
        if(countKPs==countLearntKPs) return true;
        else return false;
    }

    public static HashMap<String,String> getExerciseKP(String eID){
        HashMap<String,String> exerciseKP=new HashMap<String,String>();
        String sqlExerciseKP="select exercise.moduleID,exercise.ksID,exercise.kpID,kpName from exercise,knowledgePoint where exercise.moduleID=knowledgePoint.moduleID and exercise.ksID=knowledgePoint.ksID and exercise.kpID=knowledgePoint.kpID and eID="+eID;
        ResultSet rsExerciseKP=null;
        DBOperator dbo=new DBOperator();
        rsExerciseKP=dbo.query(sqlExerciseKP);
        try{
            rsExerciseKP.next();
            exerciseKP.put("moduleID",rsExerciseKP.getString("moduleID"));
            exerciseKP.put("ksID",rsExerciseKP.getString("ksID"));
            exerciseKP.put("kpID",rsExerciseKP.getString("kpID"));
            exerciseKP.put("kpName",rsExerciseKP.getString("kpName"));
        }catch(Exception e){
            e.printStackTrace();
        }
        return exerciseKP;
    }

    public static Boolean checkRelatedKPs(int eID,String student_ID,String relatedKPMode){
        Boolean flag=true;
        if(relatedKPMode=="finishedModules"){
            String sqlCheckRelatedKPs="select moduleID from exerciseRelatedKnowledgePoint where eID = "+Integer.toString(eID);
            DBOperator dbo=new DBOperator();
            ResultSet rsCheckRelatedKPs=dbo.query(sqlCheckRelatedKPs);
            String[] unfinishedModuleIDs= StaticServer.getUnfinishedModules(student_ID)[0];
            ArrayList<String> arrUnfinishedModuleIDs=new ArrayList<>();
            Collections.addAll(arrUnfinishedModuleIDs,unfinishedModuleIDs);
            try{
                while(rsCheckRelatedKPs.next()){
                    if(arrUnfinishedModuleIDs.contains(Integer.toString(rsCheckRelatedKPs.getInt("moduleID")))){
                        flag=false;
                        break;
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return flag;
    }

    public static HashMap<String,String[]> getRelatedKPs(String eID){
        HashMap<String,String[]> relatedKPs=new HashMap<String,String[]>();
        int countRelatedKPs=0;
        String sqlCountRelatedKPs ="select count(*) as countRelatedKPs from exerciseRelatedKnowledgePoint where eID="+eID;
        String sqlRelatedKPs="select exerciseRelatedKnowledgePoint.moduleID,exerciseRelatedKnowledgePoint.ksID,exerciseRelatedKnowledgePoint.kpID,kpName from exerciseRelatedKnowledgePoint,knowledgePoint where exerciseRelatedKnowledgePoint.moduleID=knowledgePoint.moduleID and exerciseRelatedKnowledgePoint.ksID=knowledgePoint.ksID and exerciseRelatedKnowledgePoint.kpID=knowledgePoint.kpID and eID="+eID;
        ResultSet rsCountRelatedKPs =null;
        ResultSet rsRelatedKPs=null;
        DBOperator dbo=new DBOperator();
        rsCountRelatedKPs =dbo.query(sqlCountRelatedKPs);
        try{
            rsCountRelatedKPs.next();
            countRelatedKPs=rsCountRelatedKPs.getInt("countRelatedKPs")+1;
        }catch(Exception e){
            e.printStackTrace();
        }

        String[] moduleIDs=new String[countRelatedKPs];
        String[] ksIDs=new String[countRelatedKPs];
        String[] kpIDs=new String[countRelatedKPs];
        String[] kpNames=new String[countRelatedKPs];

        HashMap<String,String> mainKP=getExerciseKP(eID);
        moduleIDs[0]=mainKP.get("moduleID");
        ksIDs[0]=mainKP.get("ksID");
        kpIDs[0]=mainKP.get("kpID");
        kpNames[0]=mainKP.get("kpName");
        rsRelatedKPs=dbo.query(sqlRelatedKPs);
        int ind=1;
        try{
            while(rsRelatedKPs.next()) {
                moduleIDs[ind]=Integer.toString(rsRelatedKPs.getInt("moduleID"));
                ksIDs[ind]=Integer.toString(rsRelatedKPs.getInt("ksID"));
                kpIDs[ind]=Integer.toString(rsRelatedKPs.getInt("kpID"));
                kpNames[ind]=rsRelatedKPs.getString("kpName");
                ind++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        relatedKPs.put("moduleIDs",moduleIDs);
        relatedKPs.put("ksIDs",ksIDs);
        relatedKPs.put("kpIDs",kpIDs);
        relatedKPs.put("kpNames",kpNames);
        return relatedKPs;
    }

    public static void addChoiceExercise(Map<String,String> addExerciseData){
        String sqlMaxeID="select max(eID) as maxeID from exercise";
        DBOperator dbo=new DBOperator();
        ResultSet rsMaxeID=dbo.query(sqlMaxeID);
        String nexteID=null;
        try{
            rsMaxeID.next();
            nexteID=Integer.toString(rsMaxeID.getInt("maxeID")+1);
        }catch(Exception e){
            e.printStackTrace();
        }
        JSONArray eOptions= JSON.parseArray(addExerciseData.get("eOptions"));
        String moduleID=addExerciseData.get("moduleID");
        String ksID=addExerciseData.get("ksID");
        String kpID=addExerciseData.get("kpID");
        String stem=addExerciseData.get("stem");
        String eKey=addExerciseData.get("eKey");
        String sqlExercise="insert into exercise values ("+nexteID+",1,'"+stem+"',"+moduleID+","+ksID+","+kpID+")";
        dbo.update(sqlExercise);
        String sqlChoiceExercise="insert into choiceExercise values ("+nexteID+","+eKey+")";
        dbo.update(sqlChoiceExercise);
        for(int i=0;i<eOptions.size();i++){
            String sqlChoiceExerciseOption="insert into choiceExerciseOption values ("+nexteID+",'"+eOptions.get(i)+"',"+Integer.toString(i+1)+")";
            dbo.update(sqlChoiceExerciseOption);
        }
    }

    public static void addJudgmentExercise(Map<String,String> addExerciseData){
        String sqlMaxeID="select max(eID) as maxeID from exercise";
        DBOperator dbo=new DBOperator();
        ResultSet rsMaxeID=dbo.query(sqlMaxeID);
        String nexteID=null;
        try{
            rsMaxeID.next();
            nexteID=Integer.toString(rsMaxeID.getInt("maxeID")+1);
        }catch(Exception e){
            e.printStackTrace();
        }
        String moduleID=addExerciseData.get("moduleID");
        String ksID=addExerciseData.get("ksID");
        String kpID=addExerciseData.get("kpID");
        String stem=addExerciseData.get("stem");
        String eKey=addExerciseData.get("eKey");
        String sqlExercise="insert into exercise values ("+nexteID+",1,'"+stem+"',"+moduleID+","+ksID+","+kpID+")";
        dbo.update(sqlExercise);
        String sqlJudgmentExercise="insert into judgmentExercise values ("+nexteID+","+eKey+")";
        dbo.update(sqlJudgmentExercise);
    }
}
