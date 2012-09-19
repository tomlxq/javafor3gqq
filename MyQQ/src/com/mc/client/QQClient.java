package com.mc.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mc.domain.Group;

/**
 * QQ客户端。
 */
public class QQClient {
	//3GQQ登陆地址
	private static final String QQ_LOGIN_URL="http://pt.3g.qq.com/handleLogin";
	//QQ空间发表说说action
	private static String QQ_ADD_MSG_URL="http://blog60.z.qq.com/mmsgb/add_msg_action_switch.jsp?B_UID=";
	//QQ空间留言板
	private static String QQ_SAY_URL="http://blog60.z.qq.com/mood/mood_add_exe.jsp?sid=";
	//QQ日志评论
	private static String QQ_RIZHI_PINGLUN="http://blog30.z.qq.com/blog/add_comment.jsp?sid=";
	static List<String> all=new ArrayList<String>();
	final static String[] msg=
		{
			"留言内容1",
			"留言内容2",
			"留言内容3",
		}; 
	private static String SID=null;
	static Random random=new Random();
	public QQClient(){
		
	}
	
	/**
	 * QQ登陆
	 */
	public static String login(final String qq,final String password){
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
	 * 获取分组信息
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
	 * 根据分组地址获取好友信息
	 */
	public static void getFrindByGourp(List<Group> groupList){
		
		if(groupList!=null&&groupList.size()!=0){
		
		}
	}
	
	/**
	 *  根据分组地址获取好友信息
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
	 * 合并好友信息
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
					System.out.print(new Date()+matcher.group()+"����Ϣ���ˣ�");
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
	 */
	public boolean hasNewMsg(String qqNum,String sid){
		return false;
	}

	/**
	 * 从分组列表获取好友信息
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
	 * 获取分组
	 * @param pid
	 * @return
	 */
	private static String getGroupUrl(String groupUrl,Integer pageNum){
		groupUrl=groupUrl.substring(0, groupUrl.length()-1)+pageNum.toString();
		return groupUrl;
	}
	/**
	 * 随机刷留言
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
				System.out.println("OK");
			}
			}else{
				System.out.println("failed");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String getFriendurl(String sid){
		return(QQ_ADD_MSG_URL+getRandomQQ()+"&sid="+sid);
	}
	
	/**
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
	 */
	public static String getMsg(){
		return msg[random.nextInt(msg.length)];
	}
	
	/**
	 * 发表QQ空间说说
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
			if(response.contains("发布成功")){
				System.out.println("OK");
			}else{
				System.out.println(response);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 删除留言版本
	 */
	public static void deleteByIdS(){
		//http://blog60.z.qq.com/mmsgb/del_msg.jsp?msgId=2220&archive=0&B_UID=418537487&sid=AQp9yLbfFWEhjMknzn8-f4TD&opageNo=3
		//	http://blog30.z.qq.com/mmsgb/del_msg.jsp?msgId=1618&archive=0&B_UID=418537487&sid=AQp9yLbfFWEhjMknzn8-f4TD&opageNo=63
		try{	
		for(int i=1618;i<2220;i++){
				HashMap<String, String> params=new HashMap<String, String>();
				params.put("B_UID","418537487");
				params.put("msgId",String.valueOf(i));
				params.put("archive","0");
				params.put("action","1");
				WebUtils.doPost("http://blog60.z.qq.com/mmsgb/del_msg.jsp?sid=", params, 0, 0);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * @author qiqi
	 * 获取日志列表的第一篇
	 * @param sid  
	 * @param qqNumber  获取该QQ的日志列表
	 * @return
	 */
	public static String getRizhiList(String sid,String qqNumber){
		
		String list=null;
		String url="http://blog60.z.qq.com/blog/blog_list.jsp?B_UID="+qqNumber+"&sid="+sid;
		try {
			String response=WebUtils.doGet(url);
		  
			Pattern pattern = Pattern.compile("(?<=bId).+?(?=&amp)");
			Matcher matcher=pattern.matcher(response);
			
			while(matcher.find()){
				String s=matcher.group();
				int t=s.indexOf("=");
				list=s.substring(t+1);
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * @author qiqi
	 * QQ日志评论
	 * @param bid
	 * @param sid
	 * @param qqNumber
	 */
	public static String pingLunRizhi(String bid,String sid,String qqNumber){
		
		String isSuccess="N";
		String url= QQ_RIZHI_PINGLUN+sid;
		HashMap<String, String> params=new HashMap<String, String>();
 
		
		params.put("bId", bid);
		params.put("B_UID",qqNumber);
		params.put("content","写的好啊，支持一下~");
		
		try {
			String response=WebUtils.doPost(url, params, 0, 0);
			 
			if(response.contains("发表评论成功")){
				System.out.println("VVV发表评论成功VVV");
				isSuccess="Y";
			}else{
				System.out.println("发表评论失败");
				isSuccess="N";
				//System.out.println(response);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isSuccess;
	}
}
