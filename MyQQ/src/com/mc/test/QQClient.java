package com.mc.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mc.domain.Group;
import com.mc.thread.GroupRunnable;

/**
 * QQ类。登陆、收发信息，更新心情等等。
 * @author Shine_MuShi
 */
public class QQClient {
	//3GQQ的登陆action
	private static final String QQ_LOGIN_URL="http://pt.3g.qq.com/handleLogin";
	//QQ空间的留言action
	private static String QQ_ADD_MSG_URL="http://blog60.z.qq.com/mmsgb/add_msg_action_switch.jsp?B_UID=";
	//发表QQ空间说说
	private static String QQ_SAY_URL="http://blog60.z.qq.com/mood/mood_add_exe.jsp?sid=";
	static List<String> all=new ArrayList<String>();
	static	String[] msg=
		{
			"珍惜人生中的每一次相识；天地间的每一份温暖；朋友间的每一个知心默契；无论你在何方，这个世界将无法改变记忆！人生的路因有朋友而不孤单,我的空间因你的到来而精彩！是网络让我们相聚交流，是朋友们的问候让我感动！永远祝福我的好朋友,幸福快乐! ",
			"生命旅途中， 我感动每一次缘的来临， 一个简短的问候信息， 一段亲切的留言交谈， 都如此让我感动， 感慨缘的美丽， 感动友谊的温馨！给你一个轻轻的祝福！只愿好朋友每分每秒都快乐！健康、好运和幸福！ ",
			"淡淡地牵挂、默默地关注、遥遥的祝福，即使一切是短暂的，也将定格成我们永恒的回忆，成为我们一生中最美丽的风景。祝朋友万事如意，幸福安康！ ",
			"你也会爱上一个人付出很多很多,你也会守着秘密不肯告诉我,　在一个夜晚偎着我的肩,泪水止不住地流了一整夜",
			"和你一样我也不懂未来还有什么,我好想替你阻挡风雨和迷惑,让你的天空只看见彩虹,直到有一天你也变成了我",
		}; 
	private static String SID=null;
	static Random random=new Random();
	public QQClient(){
		
	}
	
	/**
	 * QQ登陆
	 */
	public static String login(String qq,String password){
		HashMap<String, String> params=new HashMap<String, String>();
		params.put("login_url", "http://pt.3g.qq.com/s?aid=nLogin");
		params.put("sidtype", "1");
		params.put("loginTitle", "手机腾讯网");
		params.put("bid", "0");
		params.put("qq", qq);
		params.put("pwd", password);
		params.put("loginType", "1");
		try {
			String response=WebUtils.doPost(QQ_LOGIN_URL, params, 0, 0);
			int sidIndex=response.indexOf("sid");
			SID=response.substring(sidIndex+4, sidIndex+28);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SID;
	}
	
	/**
	 * 获取所有分组信息
	 */
	public static List<Group> getFrendGroup(String sid){
		List<Group> groupList=null;
		try {
			String response=WebUtils.doGet("http://q16.3g.qq.com/g/s?sid="+sid+"&aid=nqqGroup");
			Pattern pattern = Pattern.compile("(?<=border=\"0\"/>).+?(?=<span class=\"no\">)");
			Matcher matcher=pattern.matcher(response);
			groupList=new ArrayList<Group>();
			Group group=null;
			int i=-1;
			while(matcher.find()){
				i++;
				group=new Group();
				group.setGroupUrl("http://q32.3g.qq.com/g/s?sid="+sid+"&aid=nqqGrpF&name="+matcher.group()+"&id="+i+"&gindex="+i+"&pid=1");
				group.setGroupIndex(i);
				group.setGroupName(matcher.group());
				groupList.add(group);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return groupList;
	}
	
	/**
	 * 根据分组获取好友信息
	 */
	public static void getFrindByGourp(List<Group> groupList){
		
		if(groupList!=null&&groupList.size()!=0){
			for(Group group:groupList){
				GroupRunnable r=new GroupRunnable(group);
				Thread t=new Thread(r);
				t.start();
			}
		
		}
	}
	
	/**
	 * 根据分组的URL获取好友信息
	 */
	public static String getFrindsByGroupUrl(String groupUrl){
		String response=null;
		try {
			response=WebUtils.doGet(groupUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 统计好友信息
	 */
	public static synchronized void mergeFrind(List<String> friendList){
		all.addAll(friendList);
	}
	
	/**
	 * 发送消息
	 */
	public static void sendMsg(String toQQ,String msg,String sid){
		
	}
	
	/**
	 * 获取消息
	 */
	public static String getMsg(String qqNum,String sid){
		String msg=null;
		String getMsgUrl="http://q16.3g.qq.com/g/s?sid="+sid+"&3G_UIN="+qqNum+"&saveURL=0&aid=nqqChat";
		try {
			String response=WebUtils.doGet(getMsgUrl);
			Pattern pattern = Pattern.compile("(?<=name=\"u\" value=\").+?(?=\"/>)");
			if(response.indexOf("main-module bm-blue")!=-1){
				Matcher matcher=pattern.matcher(response);
				while(matcher.find()){
					System.out.print(new Date()+matcher.group()+"发消息来了：");
					pattern = Pattern.compile("(?<=<p>).+?(?=</p>)");
					Matcher matcher2=pattern.matcher(response);
					while(matcher2.find()){
						System.out.println(matcher2.group());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}
	/**
	 * 是否有新消息
	 */
	public boolean hasNewMsg(String qqNum,String sid){
		return false;
	}

	/**
	 * 根据好友分组的列表信息获取所有信息
	 * @param groupInfoList
	 * @return
	 */
	public static List<String> getFriendsFromGroup(List<Group> groupInfoList) {
		List<String> friendList=null;
		if(groupInfoList!=null&&groupInfoList.size()!=0){
			friendList=new ArrayList<String>();
			for(Group group:groupInfoList){
				String response;
				try {
					response = WebUtils.doGet(group.getGroupUrl());
				} catch (IOException e) {
					e.printStackTrace();
				}
				Pattern pattern = Pattern.compile("(?<=&amp;u=).+?(?=&amp)");
				Matcher matcher=null;
				int hasNext=-1;
				int pid=0;
				do{
					pid=pid+1;
					response=QQClient.getFrindsByGroupUrl(getGroupUrl(group.getGroupUrl(),pid));
					hasNext=response.indexOf("下页");
					matcher=pattern.matcher(response);
					while(matcher.find()){
						String firendQQ=matcher.group();
						friendList.add(firendQQ);
					}	
				}while(hasNext!=-1);
			}
		}
		return friendList;
	}
	
	
	/**
	 * 取指定页数的好友
	 * @param pid
	 * @return
	 */
	private static String getGroupUrl(String groupUrl,Integer pageNum){
		groupUrl=groupUrl.substring(0, groupUrl.length()-1)+pageNum.toString();
		return groupUrl;
	}
	/**
	 * 自动跑堂 刷留言
	 */
	public static void goOtherHome(String sid){
		HashMap<String, String> params=new HashMap<String, String>();
		params.put("msg",getMsg());
		params.put("entry","board" );
		params.put("ispostmsg","1" );
		try {
			String response=WebUtils.doPost(getFriendurl(sid), params, 0, 0);
			if(response!=null){
			if(response.contains("留言成功")){
				System.out.println(getFriendurl(sid));
			}else{
				System.out.println("留言失败");
			}
			}else{
				System.out.println("留言失败");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String getFriendurl(String sid){
		return(QQ_ADD_MSG_URL+getRandomQQ()+"&sid="+sid);
	}
	
	/**
	 * 产生随机QQ号
	 */
	static StringBuffer sb=new StringBuffer();
	public static String getRandomQQ(){
		sb=new StringBuffer() ;
		for(int i=0;i<8;i++){
			sb.append(random.nextInt(10));
		}
//		return sb.toString();
		return "";
	}
	
	/**
	 * 产生随机留言内容
	 */
	public static String getMsg(){
		return msg[random.nextInt(msg.length)];
	}
	/**
	 * 发表一条说说
	 */
	public static void say(String sid,String msg){
		String url=QQ_SAY_URL+sid;
		HashMap<String, String> params=new HashMap<String, String>();
		params.put("content",msg+"    [this msg is from a java application]" );
		params.put("B_UID","418537487");
		params.put("come_from","mood_add_from_infocenter");
		params.put("action","1");
		try{
			String response=WebUtils.doPost(url, params, 0, 0);
			if(response.contains("发表成功")){
				System.out.println("发表成功");
			}else{
				System.out.println(response);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
