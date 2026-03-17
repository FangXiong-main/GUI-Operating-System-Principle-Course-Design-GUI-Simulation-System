package com.fangxiong.ProgramManagementGUI;

import com.sun.source.tree.Tree;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainGUI {
    private JFrame mainFrame;
    private JFrame currentSubFrame;

    private ArrayList<PCB> programs;

    private LinkedList<MemBlock> memBlocks=new LinkedList<>();
    private LinkedList<MemBlock> freeMemBlocks=new LinkedList<>();
    private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("/");

    JTable memTable;
    DefaultTableModel memTableModel;
    JTable processTable;
    DefaultTableModel processTableModel;
    JTable fileTable;
    DefaultTableModel fileTableModel;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().createMainGUI());
    }

    private void createMainGUI() {
        // 初始化主窗口
        mainFrame = new JFrame("操作系统原理课程设计");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(500, 300);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);

        // 主面板和按钮
        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton btnProcess = createFunctionButton("进程管理与调度模拟");
        JButton btnMemory = createFunctionButton("内存管理模拟");
        JButton btnFileSystem = createFunctionButton("文件系统模拟");

        // 绑定按钮事件，调用不同的子窗口内容创建方法
        btnProcess.addActionListener(e -> openProcessSubWindow());
        btnMemory.addActionListener(e -> openMemorySubWindow());
        btnFileSystem.addActionListener(e -> openFileSystemSubWindow());

        mainPanel.add(btnProcess);
        mainPanel.add(btnMemory);
        mainPanel.add(btnFileSystem);
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    // 创建统一样式按钮
    private JButton createFunctionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        button.setPreferredSize(new Dimension(300, 50));
        return button;
    }

    // -------------------------- 进程管理子窗口 --------------------------
    private void openProcessSubWindow() {
        // 关闭旧子窗口
        closeCurrentSubWindow();
        programs= new ArrayList<>();
        // 创建子窗口
        currentSubFrame = new JFrame("进程管理与调度模拟");
        initSubWindowBasicConfig(currentSubFrame);

        // ********** 进程管理模块专属内容 **********
        JPanel processPanel = new JPanel(new BorderLayout(10, 10));
        processPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. 顶部：调度算法选择
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("选择调度算法："));
        String[] algorithms = {"FCFS", "SJF", "优先级调度"};
        JComboBox<String> algoCombo = new JComboBox<>(algorithms);
        topPanel.add(algoCombo);
        processPanel.add(topPanel, BorderLayout.NORTH);

        // 2. 中间：进程列表展示
        JPanel centerPanel = new JPanel(new BorderLayout());
        // 进程表格
        String[] columnNames = {"进程ID", "到达时间", "服务时间", "优先级"};
        processTableModel = new DefaultTableModel(columnNames, 0);
        processTable = new JTable(processTableModel);
        processTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        processTable.setRowHeight(25);
        centerPanel.add(new JScrollPane(processTable), BorderLayout.CENTER);
        String outPutText="";

        // 操作按钮
        JPanel btnPanel = new JPanel();
        JButton b1 = new JButton("添加进程");b1.addActionListener(e -> openProgramMgeSubWindow());
        JButton b2 = new JButton("开始调度");
        btnPanel.add(b1);
        btnPanel.add(b2);
        centerPanel.add(btnPanel, BorderLayout.SOUTH);
        processPanel.add(centerPanel, BorderLayout.CENTER);

        // 3. 底部：调度结果展示
        JTextArea resultArea = new JTextArea(10, 0);
        resultArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        resultArea.setEditable(false);
        resultArea.setText("调度结果：\n请选择算法并点击【开始调度】按钮");
        b2.addActionListener(e -> {
            String method = algoCombo.getSelectedItem().toString();
            ArrayList<String> text = new ArrayList<>();
            if (method.equals("FCFS")) {
                text = ProgramAlgorithms.FCFS(programs);
            }else if (method.equals("SJF")) {
                text = ProgramAlgorithms.SJF(programs);
            }else if (method.equals("优先级调度")){
                text = ProgramAlgorithms.priorityService(programs);
            }
            StringBuilder builder = new StringBuilder();
            for(String s : text) {
                builder.append(s);
            }
            resultArea.setText("使用'"+algoCombo.getSelectedItem().toString()+"'"+"方法的调度结果为：\n"+builder.toString());
        });
        processPanel.add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        // 组装并显示
        addBackButtonAndShow(currentSubFrame, processPanel);
    }

    // -------------------------- 内存管理子窗口 --------------------------
    private void openMemorySubWindow() {
        closeCurrentSubWindow();
        currentSubFrame = new JFrame("内存管理模拟");
        initSubWindowBasicConfig(currentSubFrame);

        // ********** 内存管理模块专属内容 **********
        JPanel memoryPanel = new JPanel(new BorderLayout(10, 10));
        memoryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. 顶部：内存分配算法选择
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("内存分配算法："));
        String[] algorithms = {"首次适应算法", "最佳适应算法", "最坏适应算法"};
        JComboBox<String> algoCombo = new JComboBox<>(algorithms);
        topPanel.add(algoCombo);
        memoryPanel.add(topPanel, BorderLayout.NORTH);

        // 2. 中间：内存状态展示
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        // 左侧：内存块列表
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("内存块状态"));
        String[] memColumn = {"内存块ID", "起始地址","结束地址", "大小(KB)", "占用状态"};
        memTableModel = new DefaultTableModel(memColumn, 0);
        memTable = new JTable(memTableModel);
        leftPanel.add(new JScrollPane(memTable), BorderLayout.CENTER);
        centerPanel.add(leftPanel);

        // 右侧：申请/释放内存
        JTextArea resultArea = new JTextArea(12, 25);
        resultArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(80, 50));
        resultArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        resultArea.setEditable(false);
        resultArea.setText("--------");
        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JPanel noTextPanel = new JPanel(new GridLayout(2,1, 10, 10));
        JPanel noBtnPanel = new JPanel(new GridLayout(2,2, 10, 10));
        JPanel textPanel = new JPanel();textPanel.setBorder(BorderFactory.createTitledBorder("内存分配结果"));
        rightPanel.setBorder(BorderFactory.createTitledBorder("内存操作"));
        JTextField memId = new JTextField(10);
        JTextField memSize = new JTextField(10);
        noBtnPanel.add(new JLabel("进程ID："));
        noBtnPanel.add(memId);
        noBtnPanel.add(new JLabel("申请大小(KB)："));
        noBtnPanel.add(memSize);
        JButton allocMem = new JButton("申请内存");
        allocMem.addActionListener(e -> {
            String selectedAlgorithm=algoCombo.getSelectedItem().toString();
            StringBuilder outPutText = new StringBuilder();
            ArrayList<String> returnText;
            resultArea.setText("使用"+"'"+selectedAlgorithm+"'"+"申请结果为:\n");
            if (selectedAlgorithm.equals("首次适应算法")) {
                returnText=MemAllocAlgorithms.FFA(memBlocks,freeMemBlocks,Integer.parseInt(memSize.getText()));
            } else if (selectedAlgorithm.equals("最佳适应算法")) {
                returnText=MemAllocAlgorithms.BFA(memBlocks,freeMemBlocks,Integer.parseInt(memSize.getText()));
            }else {
                returnText=MemAllocAlgorithms.WFA(memBlocks,freeMemBlocks,Integer.parseInt(memSize.getText()));
            }
            for (String s : returnText) {
                outPutText.append(s);
            }
            resultArea.append(outPutText.toString());
            memTableModel.setRowCount(0);
            memBlocks.sort(Comparator.comparingInt(MemBlock::getBid));
            for (MemBlock mem:memBlocks){
                memTableModel.addRow(new Object[]{"Mem"+mem.getBid(),mem.getStartAddress(),mem.getEndAddress(),mem.getSize(),mem.getStatus()});
            }
            resultArea.append("\n已更新表格数据！");
        });
        JButton freeMem = new JButton("释放内存");
        freeMem.addActionListener(e->{
            resultArea.setText("正在释放Mem"+memTable.getSelectedRow()+".....\n");
            MemAllocAlgorithms.releaseMemBlock(memBlocks.get(memTable.getSelectedRow()),freeMemBlocks);
            resultArea.append("内存释放完毕!");
            memTableModel.setRowCount(0);
            for(MemBlock mem:memBlocks){
                memTableModel.addRow(new Object[]{"Mem"+mem.getBid(),mem.getStartAddress(),mem.getEndAddress(),mem.getSize(),mem.getStatus()});
            }
            resultArea.append("\n已更新表格数据!");
        });
        JButton initMemBlocks = new JButton("初始化内存");
        initMemBlocks.addActionListener(e -> {
            resultArea.setText("正在初始化内存....\n");
            memBlocks.clear();freeMemBlocks.clear();
            MemAllocAlgorithms.setTotalSize(0);
            MemAllocAlgorithms.InitMemBlocks(memBlocks,freeMemBlocks);
            resultArea.append("总计初始化(连续空间):"+MemAllocAlgorithms.getTotalSize()+"KB\n");
            resultArea.append("初始化完成，正在更新表格...\n");
            memTableModel.setRowCount(0);
            for(MemBlock mem:memBlocks){
                memTableModel.addRow(new Object[]{"Mem"+mem.getBid(),mem.getStartAddress(),mem.getEndAddress(),mem.getSize(),mem.getStatus()});
            }
            resultArea.append("表格更新完成！");
        });
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(allocMem);btnPanel.add(freeMem);btnPanel.add(initMemBlocks);
        noTextPanel.add(noBtnPanel);
        noTextPanel.add(btnPanel);
        textPanel.add(resultArea);
        rightPanel.add(noTextPanel);
        rightPanel.add(textPanel);
        centerPanel.add(rightPanel);
        memoryPanel.add(centerPanel, BorderLayout.CENTER);

        // 组装并显示
        addBackButtonAndShow(currentSubFrame, memoryPanel);
    }

    // -------------------------- 文件系统子窗口 --------------------------
    private DefaultMutableTreeNode parentFilePath;
    private DefaultTreeModel model;
    private JTree fileTree = new JTree(rootNode);
    private String currentOpenedFileName;
    private DefaultMutableTreeNode currentOpenedFilePath;
    private void openFileSystemSubWindow() {
        // 1. 关闭已打开的子窗口（通用逻辑）
        if (currentSubFrame != null && currentSubFrame.isVisible()) {
            currentSubFrame.dispose();
        }

        // 2. 创建文件系统子窗口
        currentSubFrame = new JFrame("文件系统模拟");
        currentSubFrame.setSize(1000, 900); // 适配更多组件的尺寸
        currentSubFrame.setLocationRelativeTo(mainFrame); // 相对于主窗口居中
        currentSubFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 关闭子窗口时自动显示主窗口
        currentSubFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                mainFrame.setVisible(true);
            }
        });

        // 3. 整体面板（边界布局）
        JPanel totalPanel = new JPanel(new BorderLayout(15, 15));
        totalPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 15, 20));

        // ===================== 左侧：文件目录树 =====================
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setPreferredSize(new Dimension(250, 0)); // 固定宽度

        // 目录树标题
        JLabel treeTitle = new JLabel("文件目录结构", SwingConstants.CENTER);
        treeTitle.setFont(new Font("微软雅黑", Font.BOLD, 16));
        leftPanel.add(treeTitle, BorderLayout.NORTH);


        // 初始化默认目录
        DefaultMutableTreeNode docNode = new DefaultMutableTreeNode("文档");
        DefaultMutableTreeNode picNode = new DefaultMutableTreeNode("图片");
        DefaultMutableTreeNode videoNode = new DefaultMutableTreeNode("视频");
        DefaultMutableTreeNode tempNode = new DefaultMutableTreeNode("临时文件");
        rootNode.add(docNode);
        rootNode.add(picNode);
        rootNode.add(videoNode);
        rootNode.add(tempNode);

        docNode.add(new DefaultMutableTreeNode(".directory"));
        picNode.add(new DefaultMutableTreeNode(".directory"));
        videoNode.add(new DefaultMutableTreeNode(".directory"));
        tempNode.add(new DefaultMutableTreeNode(".directory"));

        model = (DefaultTreeModel) fileTree.getModel();
        fileTree.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        fileTree.setRowHeight(28); // 行高
        fileTree.setEditable(false);
        // 展开根节点
        fileTree.expandPath(new TreePath(rootNode));
        // 目录树滚动面板
        JScrollPane treeScroll = new JScrollPane(fileTree);
        treeScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        leftPanel.add(treeScroll, BorderLayout.CENTER);

        // 左侧底部：目录操作按钮
        JPanel treeBtnPanel = new JPanel(new GridLayout(2, 1, 8, 8));
        JButton btnNewFolder = new JButton("新建文件夹");
        JButton btnDelFolder = new JButton("删除目录");
        // 统一按钮样式
        setFileBtnStyle(btnNewFolder);
        setFileBtnStyle(btnDelFolder);
        treeBtnPanel.add(btnNewFolder);
        treeBtnPanel.add(btnDelFolder);
        leftPanel.add(treeBtnPanel, BorderLayout.SOUTH);

        totalPanel.add(leftPanel, BorderLayout.WEST);

        // 右侧：文件操作区
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));

        // 右侧顶部：文件操作按钮组
        JPanel topBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JButton btnNewFile = new JButton("新建文件");
        JButton btnDelFile = new JButton("删除文件");
        JButton btnRenameFile = new JButton("重命名");
        JButton btnCopyFile = new JButton("复制文件");
        JButton btnMoveFile = new JButton("移动文件");
        // 统一按钮样式
        setFileBtnStyle(btnNewFile);
        setFileBtnStyle(btnDelFile);
        setFileBtnStyle(btnRenameFile);
        setFileBtnStyle(btnCopyFile);
        setFileBtnStyle(btnMoveFile);
        topBtnPanel.add(btnNewFile);
        topBtnPanel.add(btnDelFile);
        topBtnPanel.add(btnRenameFile);
        topBtnPanel.add(btnCopyFile);
        topBtnPanel.add(btnMoveFile);
        rightPanel.add(topBtnPanel, BorderLayout.NORTH);

        // 右侧中间：文件列表 + 文件内容预览（上下布局）
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // 文件列表表格
        JPanel fileTablePanel = new JPanel(new BorderLayout());
        fileTablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "当前目录文件列表",
                0, 0,
                new Font("微软雅黑", Font.BOLD, 14)
        ));
        // 表格列名
        String[] fileTableColumns = {"文件名", "大小(KB)", "创建时间", "修改时间"};
        // 初始化测试数据
        fileTableModel = new DefaultTableModel(fileTableColumns,0);
        JTable fileTable = new JTable(fileTableModel);
        fileTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        fileTable.setRowHeight(25);
        fileTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 单选
        JScrollPane tableScroll = new JScrollPane(fileTable);
        fileTablePanel.add(tableScroll, BorderLayout.CENTER);
        centerPanel.add(fileTablePanel, BorderLayout.NORTH);

        // 文件内容预览

        JPanel fileContentPanel = new JPanel(new BorderLayout());
        fileContentPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "文件内容(仅限TXT)",
                0, 0,
                new Font("微软雅黑", Font.BOLD, 14)
        ));
        JTextArea fileContentArea = new JTextArea();
        fileContentArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        fileContentArea.setLineWrap(true); // 自动换行
        fileContentArea.setEditable(true); // 允许编辑
        fileContentArea.setText("请选择文件查看/编辑内容...");
        JScrollPane contentScroll = new JScrollPane(fileContentArea);
        fileContentPanel.add(contentScroll, BorderLayout.CENTER);
        centerPanel.add(fileContentPanel, BorderLayout.CENTER);

        rightPanel.add(centerPanel, BorderLayout.CENTER);

        // 右侧底部：保存按钮
        JPanel saveBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel fileNameLabel = new JLabel();
        JButton btnSaveFile = new JButton("保存修改");
        JButton btnOpenFile = new JButton("打开文件");
        setFileBtnStyle(btnSaveFile);setFileBtnStyle(btnOpenFile);
        saveBtnPanel.add(fileNameLabel);saveBtnPanel.add(btnOpenFile);saveBtnPanel.add(btnSaveFile);
        rightPanel.add(saveBtnPanel, BorderLayout.SOUTH);

        totalPanel.add(rightPanel, BorderLayout.CENTER);

        // 底部：返回主界面按钮
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnBack = new JButton("返回主界面");
        btnBack.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        btnBack.setPreferredSize(new Dimension(120, 35));
        backPanel.add(btnBack);
        totalPanel.add(backPanel, BorderLayout.SOUTH);

        fileTree.addTreeSelectionListener(e -> {
            if (e.getNewLeadSelectionPath() != null && e.getNewLeadSelectionPath().getLastPathComponent() instanceof DefaultMutableTreeNode) {
                parentFilePath=(DefaultMutableTreeNode)e.getNewLeadSelectionPath().getLastPathComponent();
            }
            refreshFileTableAndTreeTable(false);
        });

        //绑定事件
        // 新建文件夹
        btnNewFolder.addActionListener(e -> {
            Object parentNode = fileTree.getLastSelectedPathComponent();
            if(parentNode instanceof DefaultMutableTreeNode node){
                openCreateNewDirectoryWindow(node);
                model.nodeStructureChanged((TreeNode) model.getRoot()); //刷新树
            }else{
                JOptionPane.showMessageDialog(currentSubFrame,"请先在左侧目录中选择对应的目录","提示",JOptionPane.WARNING_MESSAGE);
            }

        });
        // 删除目录
        btnDelFolder.addActionListener(e -> {
            Object parentNode = fileTree.getLastSelectedPathComponent();
            if(parentNode instanceof DefaultMutableTreeNode node){
                if(node.getUserObject() instanceof File)
                {
                    JOptionPane.showMessageDialog(currentSubFrame,"请选择正确的目录！","警告",JOptionPane.WARNING_MESSAGE);
                } else if (!(node.getUserObject() instanceof File )&& node.getUserObject().toString().equals(".directory")) {
                    JOptionPane.showMessageDialog(currentSubFrame,"目录标识不可删除！","警告",JOptionPane.WARNING_MESSAGE);
                } else{
                    int result=JOptionPane.showConfirmDialog(currentSubFrame,"此操作将删除该目录下的所有文件！","删除目录", JOptionPane.OK_CANCEL_OPTION);
                    if(result==JOptionPane.OK_OPTION){
                        Boolean det = FileManageUnit.deleteDirectory(node,rootNode);
                        if (det){
                            JOptionPane.showMessageDialog(currentSubFrame,"删除成功","提示",JOptionPane.INFORMATION_MESSAGE);
                            model.nodeStructureChanged((TreeNode) model.getRoot());
                        }else{
                            JOptionPane.showMessageDialog(currentSubFrame,"根目录不可删除","提示",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }else{
                JOptionPane.showMessageDialog(currentSubFrame,"请先选择要删除的目录","提示",JOptionPane.WARNING_MESSAGE);
            }
        });
        // 新建文件
        btnNewFile.addActionListener(e -> {
            if(parentFilePath == null){
                JOptionPane.showMessageDialog(currentSubFrame,"请先选择目录","提示",JOptionPane.WARNING_MESSAGE);
            }else{
                openAddNewFileWindow();
            }
        });
        // 删除文件
        btnDelFile.addActionListener(e -> {
            if(parentFilePath != null && fileTable.getSelectedRow() != -1){
                int det=JOptionPane.showConfirmDialog(currentSubFrame,"确认删除"+fileTable.getValueAt(fileTable.getSelectedRow(),0)+"?","提示",JOptionPane.OK_CANCEL_OPTION);
                if(det == JOptionPane.OK_OPTION){
                    String fileName=fileTable.getValueAt(fileTable.getSelectedRow(), 0).toString();
                    if(FileManageUnit.deleteFile(parentFilePath,fileName)){
                        JOptionPane.showMessageDialog(currentSubFrame,"删除成功","提示",JOptionPane.INFORMATION_MESSAGE);
                        refreshFileTableAndTreeTable(true);
                    }else{
                        JOptionPane.showMessageDialog(currentSubFrame,"删除失败，文件不存在或未知错误","提示",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }else{
                JOptionPane.showMessageDialog(currentSubFrame,"请先选择要删除的文件","提示",JOptionPane.WARNING_MESSAGE);
            }
        });
        // 重命名
        btnRenameFile.addActionListener(e -> {
            if(parentFilePath != null && fileTable.getSelectedRow() != -1){
                String oldFileName = fileTable.getValueAt(fileTable.getSelectedRow(), 0).toString();
                openRenameFileWindow(oldFileName);
            }else{
                JOptionPane.showMessageDialog(currentSubFrame,"请先选择要重命名的文件","提示",JOptionPane.WARNING_MESSAGE);
            }
        });
        // 复制文件
        btnCopyFile.addActionListener(e -> {
            if (parentFilePath != null && fileTable.getSelectedRow()!=-1){
                String fileName = fileTable.getValueAt(fileTable.getSelectedRow(),0).toString();
                openSelectDirectoryWindow(fileName,"复制");
            }else{
                JOptionPane.showMessageDialog(currentSubFrame,"请先选择要复制的文件","提示",JOptionPane.WARNING_MESSAGE);
            }
        });
        // 移动文件
        btnMoveFile.addActionListener(e -> {
            if (parentFilePath != null && fileTable.getSelectedRow()!=-1){
                String fileName = fileTable.getValueAt(fileTable.getSelectedRow(),0).toString();
                openSelectDirectoryWindow(fileName,"移动");
            }else{
                JOptionPane.showMessageDialog(currentSubFrame,"请先选择要移动的文件","提示",JOptionPane.WARNING_MESSAGE);
            }
        });
        //打开文件
        btnOpenFile.addActionListener(e -> {
            if(parentFilePath != null && fileTable.getSelectedRow()!=-1){
                String fileName = fileTable.getValueAt(fileTable.getSelectedRow(),0).toString();
                String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
                if (!fileType.equals("txt")){
                    JOptionPane.showMessageDialog(currentSubFrame,"仅支持打开txt文件","错误",JOptionPane.ERROR_MESSAGE);
                }else{
                    if(FileManageUnit.convertBytesToString(parentFilePath,fileName) instanceof String temp){
                        JOptionPane.showMessageDialog(currentSubFrame,"打开文件成功","提示",JOptionPane.INFORMATION_MESSAGE);
                        currentOpenedFileName = fileName;currentOpenedFilePath=parentFilePath;
                        fileNameLabel.setText("当前打开文件为:"+fileName);
                        fileContentArea.setText(temp);
                    }else{
                        JOptionPane.showMessageDialog(currentSubFrame,"打开文件错误","错误",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        // 保存修改
        btnSaveFile.addActionListener(e -> {
            if(!currentOpenedFileName.isEmpty() && currentOpenedFilePath!=null){
                Boolean det = FileManageUnit.saveTextContentToFile(currentOpenedFilePath,currentOpenedFileName,fileContentArea.getText());
                if(det){
                    JOptionPane.showMessageDialog(currentSubFrame,"文件保存成功","提示",JOptionPane.INFORMATION_MESSAGE);
                    fileContentArea.setText("请选择文件查看/编辑内容...");currentOpenedFileName="";fileNameLabel.setText("");currentOpenedFilePath=null;
                    refreshFileTableAndTreeTable(true);
                }else{
                    JOptionPane.showMessageDialog(currentSubFrame,"文件保存失败-未找到文件错误","错误",JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(currentSubFrame,"未检测到有文件打开","错误",JOptionPane.ERROR_MESSAGE);
            }
        });
        // 返回主界面
        btnBack.addActionListener(e -> {
            currentSubFrame.dispose();
            mainFrame.setVisible(true);
        });

        // 4. 组装并显示子窗口
        currentSubFrame.add(totalPanel);
        currentSubFrame.setVisible(true);
        mainFrame.setVisible(false);
    }

    private void refreshFileTableAndTreeTable(Boolean refreshTreeTable){
        if(refreshTreeTable) model.nodeStructureChanged((TreeNode) model.getRoot());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DecimalFormat df = new DecimalFormat("#.00");
        if(parentFilePath != null){
            if(!(parentFilePath.getUserObject() instanceof File)){
                fileTableModel.setRowCount(0);
                for(int i=0;i<parentFilePath.getChildCount();i++){
                    DefaultMutableTreeNode node=(DefaultMutableTreeNode)parentFilePath.getChildAt(i); //获取到的Child是TreeNode不是DefaultMutableTreeNode要进行转换才能获取到Object
                    Object obj = node.getUserObject();
                    if(obj instanceof File currentFile){
                        fileTableModel.addRow(new Object[]{currentFile.getFileName()+"."+currentFile.getFileType(),df.format(currentFile.getSize()),formatter.format(currentFile.getCreateTime()),formatter.format(currentFile.getUpdateTime())});
                    }
                }
            }
        }
    }

    private DefaultMutableTreeNode cloneFileTree(DefaultMutableTreeNode rootNode){
        DefaultMutableTreeNode fileTreeRootNode = new DefaultMutableTreeNode(rootNode.getUserObject());
        fileTreeRootNode.setAllowsChildren(rootNode.getAllowsChildren());
        Enumeration<TreeNode> childes = rootNode.children();
        while(childes.hasMoreElements()){
            DefaultMutableTreeNode temp = (DefaultMutableTreeNode) childes.nextElement();
            fileTreeRootNode.add(cloneFileTree(temp));
        }
        return  fileTreeRootNode;
    }

    private void openSelectDirectoryWindow(String fileName,String operateType){
        JFrame frame = new JFrame(operateType+"文件");
        frame.setSize(210,350);
        frame.setLocationRelativeTo(mainFrame);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        JTree selectionFileTree = new JTree(cloneFileTree(rootNode));
        selectionFileTree.expandPath(new TreePath(rootNode));
        JScrollPane treeScroll = new JScrollPane(selectionFileTree);
        treeScroll.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        DefaultMutableTreeNode[] destDirectory = {new DefaultMutableTreeNode()};
        selectionFileTree.addTreeSelectionListener(e -> {
            if (e.getNewLeadSelectionPath() != null && e.getNewLeadSelectionPath().getLastPathComponent() instanceof DefaultMutableTreeNode) {
                if (!(((DefaultMutableTreeNode) e.getNewLeadSelectionPath().getLastPathComponent()).getUserObject() instanceof File) && !(((DefaultMutableTreeNode) e.getNewLeadSelectionPath().getLastPathComponent()).getUserObject().toString().equals("directory"))) {
                    destDirectory[0] = (DefaultMutableTreeNode) e.getNewLeadSelectionPath().getLastPathComponent();
                }
            }
        });
        selectionFileTree.setEditable(false);
        selectionFileTree.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        selectionFileTree.setRowHeight(28);
        JPanel main = new JPanel(new BorderLayout());main.setBorder(BorderFactory.createTitledBorder("选择文件目录"));
        JLabel titleText = new JLabel(operateType+"'"+fileName+"'"+"到");
        JButton confirm = new JButton(operateType);
        confirm.addActionListener(e -> {
            if(destDirectory[0]!=null && destDirectory[0].getUserObject()!=null && destDirectory[0].getUserObject()!=".directory" && !(destDirectory[0].getUserObject() instanceof File)){
                if(operateType.equals("复制")){
                    Boolean det = FileManageUnit.duplicateOrMoveFile(rootNode,parentFilePath,destDirectory[0],fileName,operateType);
                    if(det){
                        JOptionPane.showMessageDialog(frame,fileName+"复制成功","提示",JOptionPane.INFORMATION_MESSAGE);
                        refreshFileTableAndTreeTable(true);
                        frame.dispose();
                    }else{
                        JOptionPane.showMessageDialog(frame,"该目录下有同名文件！","警告",JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    Boolean det = FileManageUnit.duplicateOrMoveFile(rootNode,parentFilePath,destDirectory[0],fileName,operateType);
                    if(det){
                        JOptionPane.showMessageDialog(frame,fileName+"移动成功","提示",JOptionPane.INFORMATION_MESSAGE);
                        refreshFileTableAndTreeTable(true);
                        frame.dispose();
                    }else{
                        JOptionPane.showMessageDialog(frame,"该目录下有同名文件！","警告",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }else{
                JOptionPane.showMessageDialog(frame,"请先选择目录！","警告",JOptionPane.ERROR_MESSAGE);
            }
        });
        main.add(titleText,BorderLayout.NORTH);main.add(treeScroll,BorderLayout.CENTER);main.add(confirm,BorderLayout.SOUTH);
        frame.add(main);
        frame.setVisible(true);
    }


    private void openRenameFileWindow(String oldFileName){
        String showTextFiled = oldFileName.substring(0,oldFileName.lastIndexOf("."));
        JFrame frame = new JFrame("重命名文件");
        frame.setSize(350,150);
        frame.setLocationRelativeTo(mainFrame);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        JPanel panel = new JPanel(new GridLayout(2,1));
        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel textLabel = new JLabel("新文件名:");
        JTextField newFileName = new JTextField(showTextFiled,8);
        JButton modifyBtn = new JButton("重命名");
        modifyBtn.addActionListener(e -> {
            Boolean det;
            det = FileManageUnit.renameFile(parentFilePath,oldFileName,newFileName.getText());
            if(det){
                JOptionPane.showMessageDialog(frame,"文件重命名成功","提示",JOptionPane.INFORMATION_MESSAGE);
                refreshFileTableAndTreeTable(true);
                frame.dispose();
            }else {
                JOptionPane.showMessageDialog(frame,"文件重命名失败,存在同名文件","提示",JOptionPane.ERROR_MESSAGE);
            }
        });
        textPanel.add(textLabel);textPanel.add(newFileName);
        buttonPanel.add(modifyBtn);
        panel.add(textPanel);panel.add(buttonPanel);
        frame.add(panel);
        frame.setVisible(true);
    }


    private void openAddNewFileWindow() {
        JFrame frame = new JFrame("新建文件");
        frame.setSize(350,150);
        frame.setLocationRelativeTo(mainFrame);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        JPanel main = new JPanel(new GridLayout(2,1,2,2));main.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        String[] fileTypes = {"txt","jpg","png","mp4","doc","ppt","excel","pdf"};
        JComboBox<String> comboBox = new JComboBox<>(fileTypes);
        JLabel nameLabel = new JLabel("文件名:");
        JTextField fileName = new JTextField(8);
        JLabel typeLabel = new JLabel("文件类型:");
        textPanel.add(nameLabel);textPanel.add(fileName);textPanel.add(typeLabel);textPanel.add(comboBox);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton addBtn = new JButton("创建");
        addBtn.addActionListener(e -> {
            if(fileName.getText().equals("")){
                JOptionPane.showMessageDialog(frame,"文件名不为空","提示",JOptionPane.ERROR_MESSAGE);
            }else{
                Boolean det=FileManageUnit.addFile(parentFilePath,fileName.getText(), comboBox.getSelectedItem().toString());
                if(det){
                    JOptionPane.showMessageDialog(frame,"文件创建成功","提示",JOptionPane.INFORMATION_MESSAGE);
                    refreshFileTableAndTreeTable(true);
                    frame.dispose();
                }else{
                    JOptionPane.showMessageDialog(frame,"文件名重复！","警告",JOptionPane.ERROR_MESSAGE);
                    fileName.setText("");
                }
            }
        });
        buttonPanel.add(addBtn);
        main.add(textPanel);main.add(buttonPanel);
        frame.add(main);
        frame.setVisible(true);
    }

    private void openCreateNewDirectoryWindow(DefaultMutableTreeNode node) {
        JFrame frame = new JFrame("新建文件夹");
        frame.setSize(300,200);
        frame.setLocationRelativeTo(mainFrame);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        JPanel main = new JPanel(new GridLayout(2,1,10,10));
        JPanel textArea = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel("请输入文件夹名称:");
        JTextField dirName = new JTextField(10);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        JButton addBtn = new JButton("创建");
        buttonPanel.add(addBtn);
        addBtn.addActionListener(e->{
            if (dirName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame,"文件夹名称不为空","提示",JOptionPane.ERROR_MESSAGE);
            }else{
                FileManageUnit.createNewDirectory(node, dirName.getText());
                JOptionPane.showMessageDialog(frame,"文件夹创建成功!","提示",JOptionPane.INFORMATION_MESSAGE);
                refreshFileTableAndTreeTable(true);
                frame.dispose();
            }
        });
        textArea.add(label);textArea.add(dirName);
        main.add(textArea);main.add(buttonPanel);
        frame.add(main);
        frame.setVisible(true);
    }

    /**
     * 统一设置文件系统按钮样式（抽离方法，避免重复）
     */
    private void setFileBtnStyle(JButton btn) {
        btn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 鼠标悬浮显示手型
    }

    // -------------------------- 通用工具方法 --------------------------

    private void openProgramMgeSubWindow(){
        JFrame frame = new JFrame("添加进程");
        frame.setSize(650, 200);
        frame.setResizable(false);
        frame.setLocationRelativeTo(mainFrame);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(3,1));
        JPanel panelText = new JPanel(new FlowLayout());
        JButton b1 = new JButton("添加");
        JLabel label1 = new JLabel("进程ID:");
        JLabel label2 = new JLabel("到达时间:");
        JLabel label3 = new JLabel("服务时间:");
        JLabel label4 = new JLabel("优先级:");
        JTextField field1= new JTextField(5);
        JTextField field2= new JTextField(8);
        JTextField field3= new JTextField(8);
        JTextField field4= new JTextField(5);
        b1.addActionListener(e -> {
            PCB pcb = PCB.builder().pid(field1.getText()).arriveTime(Integer.parseInt(field2.getText())).serviceTime(Integer.parseInt(field3.getText())).priorityIndex(Integer.parseInt(field4.getText())).build();
            programs.add(pcb);
            processTableModel.addRow(new Object[]{pcb.getPid(),pcb.getArriveTime().toString(),pcb.getServiceTime().toString(),pcb.getPriorityIndex().toString()});
            JOptionPane.showMessageDialog(frame, "添加成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            field1.setText("");field2.setText("");field3.setText("");field4.setText("");
        });
        JLabel tips = new JLabel("  ---优先级为数字---");
        panelText.add(label1);panelText.add(field1);panelText.add(label2);panelText.add(field2);
        panelText.add(label3);panelText.add(field3);panelText.add(label4);panelText.add(field4);
        panel.add(panelText);panel.add(tips);panel.add(b1);
        frame.add(panel);
        frame.setVisible(true);
    }





    /**
     * 初始化子窗口的基础配置（统一样式）
     */
    private void initSubWindowBasicConfig(JFrame subFrame) {
        subFrame.setSize(800, 700); // 放大子窗口以适配更多内容
        subFrame.setLocationRelativeTo(mainFrame);
        subFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 关闭子窗口时显示主窗口
        subFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                mainFrame.setVisible(true);
            }
        });
    }

    /**
     * 关闭当前打开的子窗口
     */
    private void closeCurrentSubWindow() {
        if (currentSubFrame != null && currentSubFrame.isVisible()) {
            currentSubFrame.dispose();
        }
    }

    /**
     * 为子窗口添加返回按钮并显示
     * @param subFrame 子窗口
     * @param contentPanel 子窗口的核心内容面板
     */
    private void addBackButtonAndShow(JFrame subFrame, JPanel contentPanel) {
        // 整体面板（内容 + 返回按钮）
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.add(contentPanel, BorderLayout.CENTER);

        // 返回按钮面板
        JPanel backPanel = new JPanel();
        JButton btnBack = new JButton("返回主界面");
        btnBack.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        btnBack.addActionListener(e -> {
            subFrame.dispose();
            mainFrame.setVisible(true);
        });
        backPanel.add(btnBack);
        totalPanel.add(backPanel, BorderLayout.SOUTH);

        // 显示子窗口，隐藏主窗口
        subFrame.add(totalPanel);
        subFrame.setVisible(true);
        mainFrame.setVisible(false);
    }

}