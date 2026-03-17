package com.fangxiong.ProgramManagementGUI;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

public class MemAllocAlgorithms {
    @Getter
    @Setter
    private static int totalSize=0;

    private static int maxBid=0;

    private static ArrayList<String> msg=new ArrayList<>();

    public static void InitMemBlocks(LinkedList<MemBlock> memBlocks,LinkedList<MemBlock> freeBlocks) {
        Random random = new Random();int startAddress=0;
        for(int i=0;i<128;i++){
            int size=random.nextInt(128)+1;
            if(i!=0) startAddress+=1;
            memBlocks.add(MemBlock.builder().bid(i).size(size).startAddress(startAddress).endAddress(startAddress+size).status("FREE").build());
            startAddress+=size;
            totalSize+=size;
        }
        memBlocks.sort(Comparator.comparingInt(MemBlock::getBid));
        maxBid=memBlocks.getLast().getBid();
        for(MemBlock memBlock:memBlocks){
            if(memBlock.getStatus().equals("FREE")){
                freeBlocks.add(memBlock);
            }
        }
    }

    public static ArrayList<String> FFA(LinkedList<MemBlock> memBlocks,LinkedList<MemBlock> freeBlocks,int size) {
        return allocMemBlock(memBlocks,freeBlocks,msg,size,"FFA");
    }
    public static ArrayList<String> BFA (LinkedList<MemBlock> memBlocks,LinkedList<MemBlock> freeBlocks,int size){
        return allocMemBlock(memBlocks,freeBlocks,msg,size,"BFA");
    }
    public static ArrayList<String> WFA (LinkedList<MemBlock> memBlocks,LinkedList<MemBlock> freeBlocks,int size){
        return allocMemBlock(memBlocks,freeBlocks,msg,size,"WFA");
    }
    public static ArrayList<String> allocMemBlock(LinkedList<MemBlock> memBlocks,LinkedList<MemBlock> freeBlocks,ArrayList<String> msg,int size,String method){
        msg.clear();
        String temp = detectFreeBlocks(freeBlocks,method);
        if(!temp.isEmpty()) msg.add(temp);
        for(MemBlock memBlock:freeBlocks){
            if(size<=memBlock.getSize()){
                Integer memId = memBlock.getBid();msg.add("\n选中的内存块为:Mem"+memId);msg.add("\n该内存块大小为:"+memBlock.getSize()+"KB\n");
                if(size<memBlock.getSize()){
                    int remainSize=memBlock.getSize()-size;
                    freeBlocks.add(MemBlock.builder().bid(maxBid+1).size(remainSize).startAddress(memBlock.getStartAddress()+size+1).endAddress(memBlock.getEndAddress()).status("FREE").build());
                    memBlocks.add(MemBlock.builder().bid(maxBid+1).size(remainSize).startAddress(memBlock.getStartAddress()+size+1).endAddress(memBlock.getEndAddress()).status("FREE").build());
                    memBlocks.add(MemBlock.builder().bid(memBlock.getBid()).size(size).startAddress(memBlock.getStartAddress()).endAddress(memBlock.getStartAddress()+size).status("OCCUPY").build());
                    msg.add("使用的地址范围为:"+memBlock.getStartAddress()+"-"+(memBlock.getStartAddress()+size)+"\n");
                    msg.add("分配的内存块有剩余空间，剩余空间大小为："+remainSize+"KB\n剩余空间已管理为新内存块，内存块ID为:Mem"+freeBlocks.getLast().getBid()+",地址范围:"+freeBlocks.getLast().getStartAddress()+"-"+freeBlocks.getLast().getEndAddress()+"\n");
                    freeBlocks.remove(memBlock);
                    memBlocks.remove(memBlock);
                }else {
                    msg.add("\n该内存块已完全分配。");
                    msg.add("\n使用的地址范围为:"+memBlock.getStartAddress()+"-"+memBlock.getEndAddress()+"\n");
                    for(MemBlock block:memBlocks){
                        if(block.getBid().equals(memId)){
                            block.setStatus("OCCUPY");
                        }
                    }
                }
                freeBlocks.remove(memBlock);
                memBlocks.sort(Comparator.comparingInt(MemBlock::getBid));
                maxBid=memBlocks.getLast().getBid();
                return msg;
            }
        }
        msg.add("无空闲内存块空间可够申请！");
        return msg;
    }

    public static void releaseMemBlock(MemBlock memBlock,LinkedList<MemBlock> freeBlocks){
            memBlock.setStatus("FREE");
            freeBlocks.add(memBlock);
    }

    public static void autoSortBlocks(LinkedList<MemBlock> memBlocks,String method){
        if(method.equals("BFA")){
            memBlocks.sort(Comparator.comparingInt(MemBlock::getSize));
        } else if (method.equals("WFA")) {
            memBlocks.sort(Comparator.comparingInt(MemBlock::getSize).reversed());
        }
    }
    public static String detectFreeBlocks(LinkedList<MemBlock> freeBlocks,String method){
        String msg="";
        if(freeBlocks.isEmpty()){
            msg="\n无空闲内存可供分配!";
            return msg;
        }
        autoSortBlocks(freeBlocks,method);
        return "";
    }
}
