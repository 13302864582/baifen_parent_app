package com.daxiong.fun.dispatch;

import android.os.Bundle;
import android.os.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分发消息的抽象类
 * 实现handleImMsg抽象方法，来处理IM消息
 * @author parsonswang
 */
public abstract class ImMsgDispatch {

//	private static int keyGenerater = 1;
	public static final short PRIORITY_LEVEL_EXCLUSIVE = 1;
	
	public static final short PRIORITY_LEVEL_ONE = 2;
	public static final short PRIORITY_LEVEL_TWO = 3;
	public static final short PRIORITY_LEVEL_NORMAL = 4;
	
	private Map<Integer, Short> specialPriorityCMDs = new ConcurrentHashMap<Integer, Short>();
	
	/**
	 * 处理分发的消息
	 * @param msg 消息
	 * @return 处理完消息后，可以向其他低优先级接受者传递一些信息
	 * 该信息会被追加到广播的消息当中去
	 */
	public abstract Bundle handleImMsg(Message msg);
	
	
	/**
	 * 向priorityCMDs队列中添加一个命令的优先级
	 * 如果该命令的优先级已存在，则用新的优先级替换之
	 * @param cmd
	 * @param priority
	 */
	public void addCmdPriority(int cmd, short priority){
		synchronized (specialPriorityCMDs) {
			if(cmd >= 0){
				//该命令已存在列表中，则替换之
				if(specialPriorityCMDs.containsKey(cmd)){
					specialPriorityCMDs.remove(cmd);
				}
				specialPriorityCMDs.put(cmd, priority);
			}
		}
	}
	
	/**
	 * 修改命令的优先级
	 * @param cmd
	 * @param priority
	 */
	public void modifyCmdPriority(int cmd, short priority){
		synchronized (specialPriorityCMDs) {
			if(cmd >= 0){
				if(specialPriorityCMDs.containsKey(cmd)){
					specialPriorityCMDs.remove(cmd);
					specialPriorityCMDs.put(cmd, priority);
				}
			}
		}
	}
	
	/**
	 * 获取某命令对应的优先级
	 * 如果该命令不存在，则返回PRIORITY_LEVEL_NORMAL.
	 * @param cmd
	 * @return
	 */
	public short getCmdPriority(int cmd){
		synchronized (specialPriorityCMDs) {
			if(specialPriorityCMDs.containsKey(cmd)){
				return specialPriorityCMDs.get(cmd);
			}
			else{
				//非特殊命令，则返回默认优先级
				return PRIORITY_LEVEL_NORMAL;
			}
		}
	}
}
