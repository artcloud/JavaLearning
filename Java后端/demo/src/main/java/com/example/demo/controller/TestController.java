package com.example.demo.controller;

import com.example.demo.background.ExerciseServer;
import com.example.demo.background.UserServer;
import org.springframework.web.bind.annotation.*;


import com.example.demo.background.StaticServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {
    HashMap<String, UserServer> userServers = new HashMap<String, UserServer>();
    HashMap<String, ExerciseServer> exerciseServers =new HashMap<String,ExerciseServer>();


    @RequestMapping("/register")
    public String register(@RequestParam String student_ID,@RequestParam String student_password) {
        return StaticServer.createAccount(student_ID,student_password);
    }

    @RequestMapping("/login")
    public String login(@RequestParam String ID,@RequestParam String student_password) {
        String status=StaticServer.login(ID,student_password);
        if(status=="success"){
            if(ID!="admin"){
                if(exerciseServers.containsKey(ID)) exerciseServers.remove(ID);
                ExerciseServer es=new ExerciseServer(ID);
                exerciseServers.put(ID,es);
                exerciseServers.get(ID).init();
            }
            if(userServers.containsKey(ID)) userServers.remove(ID);
            UserServer us=new UserServer();
            userServers.put(ID,us);
        }
        return status;
    }

    @RequestMapping("/logOut")
    public void logOut(@RequestParam String student_ID) {
        userServers.remove(student_ID);
        exerciseServers.remove(student_ID);
    }

    @RequestMapping("/getModules")
    public ArrayList<HashMap<String,String>> getModules() {
        return StaticServer.getModules();
    }

    @RequestMapping("/getFinishedModules")
    public String[][] getFinishedModules(@RequestParam String student_ID) {
        return StaticServer.getFinishedModules(student_ID);
    }

    @RequestMapping("/getUnfinishedModules")
    public String[][] getUnfinishedModules(@RequestParam String student_ID) {
        return StaticServer.getUnfinishedModules(student_ID);
    }

    @RequestMapping("/getLockedModules")
    public String[][] getLockedModules(@RequestParam String student_ID) {
        return StaticServer.getLockedModules(student_ID);
    }

    @RequestMapping("/setModuleFinished")
    public void setModuleFinished(@RequestParam String student_ID,@RequestParam String moduleID) {
        StaticServer.setModuleFinished(student_ID,Integer.parseInt(moduleID));
    }

    @RequestMapping("/getDependencyNames")
    public String[] getDependencyNames(@RequestParam String moduleID) {
        return StaticServer.getDependencyNames(moduleID);
    }

    @RequestMapping("/getModuleName")
    public String getModuleName(@RequestParam String moduleID) {
        return StaticServer.getModuleName(moduleID);
    }

    @RequestMapping("/getModulesAndKnowledgeSets")
    public ArrayList<HashMap<String,String[]>> getModulesAndKnowledgeSets() {
        return StaticServer.getModulesAndKnowledgeSets();
    }

    @RequestMapping("/getKnowledgeSetsAndPoints")
    public ArrayList<HashMap<String,String[]>> getKnowledgeSetsAndPoints(@RequestParam String student_ID,@RequestParam String moduleID) {
        return exerciseServers.get(student_ID).getKnowledgeSetsAndPoints(moduleID);
    }

    @RequestMapping("/getKnowledgePoint")
    public String[] getKnowledgePoint(@RequestParam String moduleID,@RequestParam String ksID,@RequestParam String kpID) {
        return StaticServer.getKnowledgePoint(moduleID,ksID,kpID);
    }

    @RequestMapping("/getPrevkp")
    public String[] getPrevkp(@RequestParam String moduleID,@RequestParam String ksID,@RequestParam String kpID) {
        return StaticServer.getPrevkp(moduleID,ksID,kpID);
    }

    @RequestMapping("/getNextkp")
    public String[] getNextkp(@RequestParam String moduleID,@RequestParam String ksID,@RequestParam String kpID) {
        return StaticServer.getNextkp(moduleID,ksID,kpID);
    }

    @RequestMapping("/getMinMaxkp")
    public String[] getMinMaxkp(@RequestParam String moduleID) {
        return StaticServer.getMinMaxkp(moduleID);
    }

    @RequestMapping("/getKPIfLearnt")
    public Boolean getKPIfLearnt(@RequestParam String student_ID,@RequestParam String moduleID,@RequestParam String ksID,@RequestParam String kpID) {
        return StaticServer.getKPIfLearnt(student_ID,moduleID,ksID,kpID);
    }

    @RequestMapping("/setFinishLearningKP")
    public void setFinishLearningKP(@RequestParam String student_ID,@RequestParam String moduleID,@RequestParam String ksID,@RequestParam String kpID) {
        StaticServer.setFinishLearningKP(student_ID,moduleID,ksID,kpID);
    }

    @RequestMapping("/getKPsIfLearnt")
    public Boolean getKPsIfLearnt(@RequestParam String student_ID,@RequestParam String moduleID) {
        return StaticServer.getKPsIfLearnt(student_ID,moduleID);
    }

    @RequestMapping("/generateExercises")
    public void generateExercises(@RequestParam String student_ID,@RequestParam String moduleID) {
        exerciseServers.get(student_ID).generateExercises(Integer.parseInt(moduleID),4,"finishedModules");
    }

    @RequestMapping("/getExerciseCount")
    public int getExerciseCount(@RequestParam String student_ID,@RequestParam String moduleID) {
        return exerciseServers.get(student_ID).getExerciseCount(Integer.parseInt(moduleID));
    }

    @RequestMapping("/getExercise")
    public String[][] getExercise(@RequestParam String student_ID,@RequestParam String moduleID,@RequestParam String exerciseNum) {
        return exerciseServers.get(student_ID).getExercise(Integer.parseInt(moduleID),Integer.parseInt(exerciseNum));
    }

    @RequestMapping("/submitModuleExerciseSet")
    public HashMap<String,String> submitModuleExerciseSet(@RequestParam Map<String,String> answerData) {
        String student_ID=answerData.get("student_ID");
        String moduleID=answerData.get("moduleID");
        return exerciseServers.get(student_ID).correctAndRecordExercises(Integer.parseInt(moduleID),answerData);
    }

    @RequestMapping("/getReviewExerciseSetData")
    public HashMap<String, String> getReviewExerciseSetData(@RequestParam String student_ID, @RequestParam String timeSubmit) {
        return exerciseServers.get(student_ID).getReviewExerciseSetData(timeSubmit);
    }

    @RequestMapping("/getReviewExerciseData")
    public String[][][] getReviewExerciseData(@RequestParam String student_ID,@RequestParam String timeSubmit) {
        return exerciseServers.get(student_ID).getReviewExerciseData(timeSubmit);
    }

    @RequestMapping("/getRelatedKPs")
    public HashMap<String, String[]> getRelatedKPs(@RequestParam String eID) {
        return StaticServer.getRelatedKPs(eID);
    }

    @RequestMapping("/getCompletedExerciseSets")
    public ArrayList<HashMap<String,String>> getCompletedExerciseSets(@RequestParam String student_ID) {
        return exerciseServers.get(student_ID).getCompletedExerciseSets();
    }

    @RequestMapping("/addChoiceExercise")
    public void addChoiceExercise(@RequestParam Map<String,String> addChoiceExerciseData) {
        StaticServer.addChoiceExercise(addChoiceExerciseData);
    }

    @RequestMapping("/addJudgmentExercise")
    public void addJudgmentExercise(@RequestParam Map<String,String> addJudgmentExerciseData) {
        StaticServer.addJudgmentExercise(addJudgmentExerciseData);
    }

    @RequestMapping("/getLearningData")
    public HashMap<String,String> getLearningData(@RequestParam String student_ID) {
        return exerciseServers.get(student_ID).getLearningData();
    }

    @RequestMapping("/getCompletedExerciseData")
    public HashMap<String,String> getCompletedExerciseData(@RequestParam String student_ID) {
        return exerciseServers.get(student_ID).getCompletedExerciseData();
    }

    @RequestMapping("/getRecommendKP")
    public HashMap<String,Integer> getRecommendKP(@RequestParam String student_ID) {
        return exerciseServers.get(student_ID).getRecommendKP(student_ID);
    }

    public void hott(){
        System.out.println("1");
    }
}