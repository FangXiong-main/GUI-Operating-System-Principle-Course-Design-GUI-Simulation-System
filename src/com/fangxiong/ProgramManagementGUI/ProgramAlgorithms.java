package com.fangxiong.ProgramManagementGUI;

import java.util.*;

public class ProgramAlgorithms {
    public static ArrayList<String> FCFS(ArrayList<PCB> list) {
        ArrayList<PCB> programs = new ArrayList<>(list);
        programs.sort(Comparator.comparingInt(PCB::getArriveTime));
        int currentTime=programs.getFirst().getArriveTime();
        for (PCB pcb : programs){
            currentTime+=pcb.getServiceTime();
            pcb.setTurnOverTime(currentTime-pcb.getArriveTime());
            pcb.setWaitingTime(pcb.getTurnOverTime()-pcb.getServiceTime());
            pcb.setQTurnOverTime((float)pcb.getTurnOverTime()/pcb.getServiceTime());
        }
        return outPutTool(programs);
    }
    public static ArrayList<String> SJF(ArrayList<PCB> list) {
        ArrayList<PCB> programs = new ArrayList<>(list);
        ArrayList<PCB> finalList = new ArrayList<>();
        programs.sort(Comparator.comparingInt(PCB::getArriveTime));
        int currentTime =0;
        ArrayList<PCB> temp = programs;
        ArrayList<PCB> arrivedList;
        while(!temp.isEmpty()){
            arrivedList=generateArrivedList(temp,currentTime);
            if (arrivedList!=null){
                arrivedList.sort(Comparator.comparingInt(PCB::getServiceTime));
                currentTime+=arrivedList.getFirst().getServiceTime();
                arrivedList.getFirst().setTurnOverTime(currentTime-arrivedList.getFirst().getArriveTime());
                arrivedList.getFirst().setWaitingTime(arrivedList.getFirst().getTurnOverTime()-arrivedList.getFirst().getServiceTime());
                arrivedList.getFirst().setQTurnOverTime((float)arrivedList.getFirst().getTurnOverTime()/arrivedList.getFirst().getServiceTime());
                finalList.add(arrivedList.getFirst());
                temp.remove(arrivedList.getFirst());
            }else{
                currentTime++;
            }
        }
        return outPutTool(finalList);
    }

    //用于生成等待序列
    public static ArrayList<PCB> generateArrivedList(ArrayList<PCB> programs,int currentTime){
        ArrayList<PCB> arrivedList = new ArrayList<>();
        for(int i=0;i<programs.size();i++){
            if(programs.get(i).getArriveTime()<=currentTime) {
                arrivedList.add(programs.get(i));
            }
        }
        if (arrivedList.isEmpty()) return null;
        return arrivedList;
    }

    public static ArrayList<String> priorityService(ArrayList<PCB> list) {
        ArrayList<PCB> programs = new ArrayList<>(list);
        ArrayList<PCB> finalList = new ArrayList<>();
        programs.sort(Comparator.comparingInt(PCB::getArriveTime));
        int currentTime =0;
        ArrayList<PCB> temp = programs;
        ArrayList<PCB> arrivedList;
        while(!temp.isEmpty()){
            arrivedList=generateArrivedList(temp,currentTime);
            if (arrivedList!=null){
                arrivedList.sort(Comparator.comparingInt(PCB::getPriorityIndex));
                currentTime+=arrivedList.getFirst().getServiceTime();
                arrivedList.getFirst().setTurnOverTime(currentTime-arrivedList.getFirst().getArriveTime());
                arrivedList.getFirst().setWaitingTime(arrivedList.getFirst().getTurnOverTime()-arrivedList.getFirst().getServiceTime());
                arrivedList.getFirst().setQTurnOverTime((float)arrivedList.getFirst().getTurnOverTime()/arrivedList.getFirst().getServiceTime());
                finalList.add(arrivedList.getFirst());
                temp.remove(arrivedList.getFirst());
            }else{
                currentTime++;
            }
        }
        return outPutTool(finalList);
    }
    public static ArrayList<String> outPutTool(ArrayList<PCB> programs) {
        ArrayList<String> result = new ArrayList<>();
        int time = 0;
        int allTurnOvertime=0;
        for (PCB pcb : programs){
            time+=pcb.getServiceTime();
            allTurnOvertime+=pcb.getTurnOverTime();
        }
        for(PCB pcb : programs) {
            result.add("——>进程ID:"+pcb.getPid() +",到达时间："+pcb.getArriveTime()+",服务时间:"+pcb.getServiceTime()+",完成时间：" + time + ",周转时间:" + pcb.getTurnOverTime() +",带权周转时间："+pcb.getQTurnOverTime()+",等待时间:" + pcb.getWaitingTime() + "\n");
        }
        result.add("总计服务用时："+time+"\n");
        result.add("平均周转时间："+(float)allTurnOvertime/programs.size());
        return result;
    }
}
