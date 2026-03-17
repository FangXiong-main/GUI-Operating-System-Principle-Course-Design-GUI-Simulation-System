package com.fangxiong.ProgramManagementGUI;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;

public class FileManageUnit {
    public static void createNewDirectory(DefaultMutableTreeNode parentNode,String dirName){
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(dirName);
        parentNode.add(childNode);childNode.add(new DefaultMutableTreeNode(".directory"));
    }

    public static Boolean deleteDirectory(DefaultMutableTreeNode node,DefaultMutableTreeNode rootNode){
        if(node==rootNode) {
            return false;
        }else {
            node.removeAllChildren();node.removeFromParent();
            return true;
        }
    }

    public static Boolean addFile(DefaultMutableTreeNode parentNode,String fileName,String fileType){
        if(!checkDuplicate(parentNode,fileName,fileType)){
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(File.builder().fileName(fileName).size(1.0f).fileType(fileType).fileContent("".getBytes()).createTime(LocalDateTime.now()).updateTime(LocalDateTime.now()).build(),false);
            parentNode.add(childNode);
            return true;
        }else{
            return false;
        }
    }

    public static Boolean deleteFile(DefaultMutableTreeNode parentNode,String fileName){
        DefaultMutableTreeNode fileNode = searchFileNode(parentNode,substringFileName(fileName).getFirst(),substringFileName(fileName).getLast());
        if(fileNode != null){
            parentNode.remove(fileNode);
            return true;
        }
        return false;
    }

    public static Boolean duplicateOrMoveFile(DefaultMutableTreeNode rootNode,DefaultMutableTreeNode parentNode,DefaultMutableTreeNode destNode,String fileName,String operateType){
        DefaultMutableTreeNode fileNode = searchFileNode(parentNode,substringFileName(fileName).getFirst(),substringFileName(fileName).getLast());
        DefaultMutableTreeNode correctDestNode = getParentNodeFromDestNode(rootNode,destNode.getUserObject().toString());
        if( correctDestNode!= null && fileNode != null && fileNode.getUserObject() instanceof File temp && !checkDuplicate(correctDestNode,substringFileName(fileName).getFirst(),substringFileName(fileName).getLast())){
            if(operateType.equals("复制")){
                correctDestNode.add(new DefaultMutableTreeNode(File.builder().fileName(temp.getFileName()).fileType(temp.getFileType()).size(temp.getSize()).createTime(temp.getCreateTime()).updateTime(LocalDateTime.now()).fileContent(temp.getFileContent()).build()));
            }else{
                parentNode.remove(fileNode);
                correctDestNode.add(new DefaultMutableTreeNode(File.builder().fileName(temp.getFileName()).fileType(temp.getFileType()).size(temp.getSize()).createTime(temp.getCreateTime()).updateTime(LocalDateTime.now()).fileContent(temp.getFileContent()).build()));
            }
            return true;
        }
        return false;
    }

    public static DefaultMutableTreeNode getParentNodeFromDestNode(DefaultMutableTreeNode rootNode,String destDirectoryName){
        Enumeration<TreeNode> node = rootNode.children();
        while(node.hasMoreElements()){
            DefaultMutableTreeNode temp = (DefaultMutableTreeNode) node.nextElement();
            if(temp.getChildCount()!=1){
                if(getParentNodeFromDestNode(temp,destDirectoryName)!=null) temp=getParentNodeFromDestNode(temp,destDirectoryName);
            }
            if (temp!=null && !(temp.getUserObject() instanceof File) && temp.getUserObject()!=".directory" && temp.getUserObject().equals(destDirectoryName)){
                return temp;
            }
        }
        return null;
    }

    public static Boolean renameFile(DefaultMutableTreeNode parentNode,String oldFileName,String newFileName){
        String subFileName = substringFileName(oldFileName).getFirst();
        String oldFileType = substringFileName(oldFileName).getLast();
        if(!checkDuplicate(parentNode,newFileName,oldFileType)){
            DefaultMutableTreeNode fileNode = searchFileNode(parentNode,subFileName,oldFileType);
            if(fileNode != null){
                File temp = (File) fileNode.getUserObject();
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(File.builder().fileName(newFileName).size(temp.getSize()).fileType(temp.getFileType()).createTime(temp.getCreateTime()).updateTime(LocalDateTime.now()).fileContent(temp.getFileContent()).build(),false);
                parentNode.remove(fileNode);
                parentNode.add(newNode);
                return true;
            }
        }
        return false;
    }

    public static DefaultMutableTreeNode searchFileNode(DefaultMutableTreeNode parentNode,String fileName,String fileType){
        for(int i=0;i<parentNode.getChildCount();i++){
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
            if(childNode.getUserObject() instanceof File temp){
                if(temp.getFileName().equals(fileName) &&temp.getFileType().equals(fileType)){
                    return childNode;
                }
            }
        }
        return null;
    }

    public static ArrayList<String> substringFileName(String fileName){
        String FileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        String subFileName = fileName.substring(0,fileName.lastIndexOf("."));
        ArrayList<String> substringFile = new ArrayList<>();
        substringFile.add(subFileName);substringFile.add(FileType);
        return substringFile;
    }

    public static Boolean checkDuplicate(DefaultMutableTreeNode parentNode,String fileName,String fileType){
        int duplicateCount = 0;
        for(int i=0;i<parentNode.getChildCount();i++){
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
            if(childNode.getUserObject() instanceof File temp){
                if(temp.getFileName().equals(fileName)&&temp.getFileType().equals(fileType)){
                    duplicateCount++;
                }
            }
        }
        return duplicateCount != 0;
    }

    public static String convertBytesToString(DefaultMutableTreeNode parentNode,String fileName){
        String subFileName = substringFileName(fileName).getFirst();
        String fileType = substringFileName(fileName).getLast();
        if(Objects.requireNonNull(searchFileNode(parentNode, subFileName, fileType)).getUserObject() instanceof File temp){
            return new String(temp.getFileContent());
        }
        return null;
    }

    public static Boolean saveTextContentToFile(DefaultMutableTreeNode parentNode,String fileName,String content){
        String subFileName = substringFileName(fileName).getFirst();
        String fileType = substringFileName(fileName).getLast();
        DefaultMutableTreeNode tempNode = searchFileNode(parentNode, subFileName, fileType);
        if(tempNode!=null && tempNode.getUserObject() instanceof File tempFile){
            parentNode.add(new DefaultMutableTreeNode(File.builder().fileName(tempFile.getFileName()).fileType(tempFile.getFileType()).fileContent(content.getBytes()).createTime(tempFile.getCreateTime()).updateTime(LocalDateTime.now()).size((content.getBytes().length/1024.0f)+1.0f).build()));
            parentNode.remove(tempNode);
            return true;
        }
        return false;
    }
}
