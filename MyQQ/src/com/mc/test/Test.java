package com.mc.test;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;

import com.mc.domain.Friend;
import com.mc.domain.Group;
/**
 * 3GQQ测试的一个小Demo
 * @author Shine_MuShi
 */
public class Test {
	static final String url="http://pt.3g.qq.com/";
	static String ctype = "application/x-www-form-urlencoded;charset=utf-8" ;
	static String userAgent="Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/525.13 (KHTML, like Gecko) Chrome/0.2.149.29 Safari/525.13";
	
	static HttpURLConnection conn=null;
	static String rsp=null;
	static OutputStream out = null;
	/**
	 * 首先登陆。
	 * 然后获取分组信息。
	 * 然后根据分组信息取所有好友QQ号。
	 * @param args
	 */
	public static void main(String[] args) {
		try {
		System.out.println("开始抓取。。");
		long a=System.currentTimeMillis();
		String sid=QQClient.login("", "");
		//System.out.println(sid);
//		List<Group> groupInfoList=QQClient.getFrendGroup(sid);
		//	QQClient.getFrindByGourp(groupInfoList);
//		List<String> firendList=QQClient.getFriendsFromGroup(groupInfoList);
//		long b=System.currentTimeMillis();
//		System.out.println("抓取结束。。耗时"+(b-a)+" 好友数："+firendList.size());
//		for(String qq:firendList){
//			System.out.println(qq);
//		}
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
		while(true){
			QQClient.goOtherHome(sid);
		}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
}
