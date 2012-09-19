package com.mc.test;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;

import com.mc.client.QQClient;
import com.mc.domain.Group;

/**
 * @author Shine_MuShi
 */
public class Test {
	static HttpURLConnection conn=null;
	static String rsp=null;
	static OutputStream out = null;

	public static void main(String[] args) {
		try {
		String sid=QQClient.login("1623901988", "meng8633096");
		List<Group> groupInfoList=QQClient.getFrendGroup(sid);
		List<String> firendList=QQClient.getFriendsFromGroup(groupInfoList);
		for(String qq:firendList){
			System.out.println(qq);
		}
//		if(firendList!=null&&firendList.size()!=0){
//			while(true){
//				
//			}
//		}
//			while(true){
//				QQClient.getMsg("",sid);
//			}
//		while(true){
//			QQClient.getRandomQQ();
//		}
		//QQClient.say("AaOA1KXemnXYtzTiHrqBzTLP", "test");
//		while(true){
//			QQClient.goOtherHome(sid);
//		}
//		while(true){
//			QQClient.deleteByIdS();
//		}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
}
