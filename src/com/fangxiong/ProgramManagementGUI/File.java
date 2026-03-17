package com.fangxiong.ProgramManagementGUI;

import lombok.Builder;
import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
@Data
@Builder
public class File {
    private String fileName;
    private Float size;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String fileType;
    private byte[] fileContent;
}
