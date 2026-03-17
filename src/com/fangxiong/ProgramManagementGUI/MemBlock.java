package com.fangxiong.ProgramManagementGUI;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemBlock {
    private Integer bid;
    private Integer startAddress;
    private Integer endAddress;
    private Integer size;
    private String status;
}
