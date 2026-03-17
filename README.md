操作系统原理课程设计 - GUI 模拟系统

项目简介

本项目基于 Java Swing 开发，实现了操作系统核心机制的可视化模拟，涵盖进程调度、内存分配、文件系统管理三大核心模块。通过图形化界面（GUI）直观展示经典操作系统算法的执行过程与结果，适用于操作系统原理课程设计、教学演示及算法学习验证。
环境要求

JDK 版本：JDK 8 及以上（需兼容 Lombok、Java 8 LocalDateTime 特性）
开发 / 运行工具：IntelliJ IDEA/Eclipse（需安装 Lombok 插件）
系统环境：Windows/macOS/Linux（兼容 Java Swing 界面渲染）
核心功能模块

1. 进程管理与调度模拟

支持算法

先来先服务（FCFS）：按进程到达时间排序，计算周转时间、等待时间、带权周转时间；
短作业优先（SJF）：优先调度已到达且服务时间最短的进程；
优先级调度：按进程优先级指数（数值越低优先级越高）调度已到达进程。
操作流程

进入「进程管理与调度模拟」模块；
点击「添加进程」，输入进程 ID、到达时间、服务时间、优先级；
选择调度算法，点击「开始调度」，查看调度结果（含各进程时间指标）。
2. 内存管理模拟

支持算法

首次适应算法（FFA）：从内存起始地址匹配第一个足够大的空闲块；
最佳适应算法（BFA）：匹配最小的可用空闲块；
最坏适应算法（WFA）：匹配最大的可用空闲块。
操作流程

进入「内存管理模拟」模块；
点击「初始化内存」，自动生成 128 个随机大小的空闲内存块；
选择分配算法，输入申请内存大小，点击「申请内存」，查看分配结果（选中内存块、地址范围、剩余空间）；
选中已占用内存块，点击「释放内存」，恢复为空闲块并更新内存状态。
3. 文件系统模拟

核心实体（File 类）

封装文件元数据，核心属性如下：
表格
属性名	类型	说明
fileName	String	文件名
size	Float	文件大小（默认 1.0KB）
createTime	LocalDateTime	文件创建时间
updateTime	LocalDateTime	文件最后修改时间
fileType	String	文件类型（扩展名）
fileContent	byte[]	文件内容（字节数组存储）
支持操作

目录管理：创建 / 删除目录，以树形结构展示文件目录；
文件操作：添加 / 删除 / 重命名 / 复制 / 移动文件；
唯一性校验：同一目录下文件名 + 文件类型组合唯一。
项目核心类说明

表格
类名	功能说明
MainGUI	程序入口，创建主窗口及各子模块 GUI 界面，绑定按钮事件与算法调用；
PCB	进程控制块模型，存储进程 ID、优先级、服务时间、到达时间等核心属性；
ProgramAlgorithms	进程调度算法实现类，封装 FCFS、SJF、优先级调度的核心逻辑；
MemBlock	内存块模型，存储内存块 ID、起始 / 结束地址、大小、占用状态（FREE/OCCUPY）；
MemAllocAlgorithms	内存分配算法实现类，封装 FFA、BFA、WFA 及内存初始化 / 释放逻辑；
File	文件模型，存储文件名、大小、创建 / 修改时间、内容等元数据；
FileManageUnit	文件系统操作工具类，封装目录 / 文件的增删改查、复制移动等逻辑；
运行步骤

下载 / 克隆项目代码，导入至支持 Lombok 的 Java IDE；
配置 JDK 环境（确保版本≥1.8），确认 Lombok 插件已启用；
运行MainGUI.java的main方法，启动程序主窗口；
选择对应功能模块（进程 / 内存 / 文件系统），按界面提示完成操作。
注意事项

进程调度模块需先添加至少一个进程，否则调度无结果；
内存管理模块中，内存块 ID 自动生成，申请内存后剩余空间会生成新空闲块；
文件系统模块中，根目录「/」不可删除，同一目录下不允许同名同类型文件；
界面适配：建议使用 1080P 及以上分辨率，避免 Swing 组件排版错乱。
核心技术栈

界面开发：Java Swing（JFrame、JPanel、JTable、JTree、JComboBox 等）；
数据模型：Lombok（@Data、@Builder）简化 POJO 类编写；
算法实现：经典操作系统进程调度 / 内存分配算法的 Java 落地；
时间处理：Java 8 LocalDateTime（文件创建 / 修改时间记录）。


Operating System Principle Course Design - GUI Simulation System

Brief introduction of the project

This project is developed based on Java Swing and realizes the visual simulation of the core mechanism of the operating system, covering the three core modules of process scheduling, memory allocation and file system management. Through the graphical interface (GUI), the execution process and results of the classic operating system algorithm are intuitively displayed, which is suitable for operating system principle course design, teaching demonstration and algorithm learning verification.

Environmental requirements

JDK version: JDK 8 and above (need to be compatible with Lombok, Java 8 LocalDateTime features)

Development / Operation Tool: IntelliJ IDEA/Eclipse (Lombok plug-in installation required)

System environment: Windows/macOS/Linux (compatible with Java Swing interface rendering)

Core function module

1. Process management and scheduling simulation

Support algorithm

First-come, first-served (FCFS): Sort by process arrival time, calculate turnover time, waiting time, and weighted turnover time;

Short-term priority (SJF): priority is the process that has arrived and has the shortest service time;

Priority scheduling: Schedule the arrived process according to the process priority index (the lower the value, the higher the priority).

Operation process

Enter the "Process Management and Scheduling Simulation" module;

Click "Add Process" and enter the process ID, arrival time, service time and priority;

Select the scheduling algorithm, click "Start Scheduling" to view the scheduling results (including the time indicators of each process).

2. Memory management simulation

Support algorithm

First Adaptation Algorithm (FFA): Match the first large enough idle block from the memory starting address;

Best Adaptation Algorithm (BFA): Match the smallest available free block;

Worst Adaptation Algorithm (WFA): Match the largest available free block.

Operation process

Enter the "Memory Management Simulation" module;

Click "Initialize Memory" to automatically generate 128 free memory blocks of random size;

Select the allocation algorithm, enter the size of the application memory, click "Apply Memory" to view the allocation results (select the memory block, address range, and remaining space);

Select the occupied memory block, click "Release Memory", restore to idle block and update the memory status.

3. File system simulation

Core entity (File class)

Encapsulated file metadata, the core attributes are as follows:

Form

Attribute name	 Type	 Explain

File name	 String	 File name

Size	 Float	 File size (default 1.0KB)

Create Time	 Local Date Time	 File creation time

UpdateTime	 Local Date Time	 The last revision time of the file

File type	 String	 File type (extension)

File content	 Byte[]	 File content (byte array storage)

Support operation

Directory management: create/delete directories and display the file directory in a tree structure;

File operation: add / delete / rename / copy / move files;

Uniqueness check: the combination of file name + file type in the same directory is unique.

Description of the core class of the project

Form

Class name	 Function description

Main GUI	 Program entrance, create the main window and the GUI interface of each submodule, bind button events and algorithm calls;

PCB	 Process control block model, store process ID, priority, service time, arrival time and other core attributes;

Program Algorithms	 Process scheduling algorithm implementation class, encapsulating the core logic of FCFS, SJF and priority scheduling;

Memblock	 Memory block model, store memory block ID, start/end address, size, occupancy status (FREE/OCCUPY);

MemAllocAlgorithms	 Memory allocation algorithm implementation class, encapsulates FFA, BFA, WFA and memory initialization/release logic;

File	 File model, store file name, size, creation/modification time, content and other metadata;

FileManage Unit	 File system operation tool class, encapsulation directory / file addition, deletion, modification, copying and moving and other logics;

Running steps

Download/clone the project code and import it into the Java IDE that supports Lombok;

Configure the JDK environment (make sure that the version is ≥1.8) and confirm that the Lombok plug-in is enabled;

Run the main method of MainGUI.java and start the main window of the program;

Select the corresponding functional module (process/memory/file system) and follow the interface prompts to complete the operation.

Notes

The process scheduling module needs to add at least one process first, otherwise the scheduling will have no results;

In the memory management module, the memory block ID is automatically generated, and the remaining space will generate a new free block after applying for memory;

In the file system module, the root directory "/" cannot be deleted, and files of the same name and same type are not allowed in the same directory;

Interface adaptation: It is recommended to use a resolution of 1080P and above to avoid the layout of Swing components.

Core technology stack

Interface development: Java Swing (JFrame, JPanel, JTable, JTree, JComboBox, etc.);

Data model: Lombok (@Data, @Builder) simplifies POJO class writing;

Algorithm implementation: Java landing of classic operating system process scheduling / memory allocation algorithm;

Time processing: Java 8 LocalDateTime (file creation / modification time record).
