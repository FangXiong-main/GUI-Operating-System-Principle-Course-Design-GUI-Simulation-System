package com.fangxiong.ProgramManagementGUI;

import lombok.*;

@Data
@Builder
public class PCB {
    private String pid;
    private Integer priorityIndex;
    private Integer serviceTime;
    private Integer arriveTime;
    private Integer turnOverTime;
    private Integer waitingTime;
    private Float qTurnOverTime;
}
