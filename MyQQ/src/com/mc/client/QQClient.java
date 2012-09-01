package com.mc.client;

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
 * QQ�ࡣ��½���շ���Ϣ����������ȵȡ�
 * @author Shine_MuShi
 */
public class QQClient {
	//3GQQ�ĵ�½action
	private static final String QQ_LOGIN_URL="http://pt.3g.qq.com/handleLogin";
	//QQ�ռ������action
	private static String QQ_ADD_MSG_URL="http://blog60.z.qq.com/mmsgb/add_msg_action_switch.jsp?B_UID=";
	//����QQ�ռ�˵˵
	private static String QQ_SAY_URL="http://blog60.z.qq.com/mood/mood_add_exe.jsp?sid=";
	static List<String> all=new ArrayList<String>();
	static	String[] msg=
		{
			"��ϧ�����е�ÿһ����ʶ����ؼ��ÿһ����ů�����Ѽ��ÿһ��֪��Ĭ�����������ںη���������罫�޷��ı���䣡�����·�������Ѷ�µ�,�ҵĿռ�����ĵ�����ʣ���������������۽������������ǵ��ʺ����Ҹж�����Զף���ҵĺ�����,�Ҹ�����! ",
			"������;�У� �Ҹж�ÿһ��Ե�����٣� һ����̵��ʺ���Ϣ�� һ�����е����Խ�̸�� ��������Ҹж��� �п�Ե�������� �ж��������ܰ������һ�������ף����ֻԸ������ÿ��ÿ�붼���֣����������˺��Ҹ��� ",
			"������ǣ�ҡ�ĬĬ�ع�ע��ңң��ף������ʹһ���Ƕ��ݵģ�Ҳ���������������Ļ��䣬��Ϊ����һ�����������ķ羰��ף�����������⣬�Ҹ������� ",
			"��Ҳ�ᰮ��һ���˸����ܶ�ܶ�,��Ҳ���������ܲ��ϸ�����,����һ��ҹ�������ҵļ�,��ˮֹ��ס������һ��ҹ",
			"����һ����Ҳ����δ������ʲô,�Һ��������赲������Ի�,��������ֻ����ʺ�,ֱ����һ����Ҳ�������",
		}; 
	private static String SID=null;
	static Random random=new Random();
	public QQClient(){
		
	}
	
	/**
	 * QQ��½
	 */
	public static String login(String qq,String password){
		HashMap<String, String> params=new HashMap<String, String>();
		params.put("login_url", "http://pt.3g.qq.com/s?aid=nLogin");
		params.put("sidtype", "1");
		params.put("loginTitle", "�ֻ���Ѷ��");
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
	 * ��ȡ���з�����Ϣ
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
	 * ��ݷ����ȡ������Ϣ
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
	 * ��ݷ����URL��ȡ������Ϣ
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
	 * ͳ�ƺ�����Ϣ
	 */
	public static synchronized void mergeFrind(List<String> friendList){
		all.addAll(friendList);
	}
	
	/**
	 * ������Ϣ
	 */
	public static void sendMsg(String toQQ,String msg,String sid){
		
	}
	
	/**
	 * ��ȡ��Ϣ
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
	 * �Ƿ�������Ϣ
	 */
	public boolean hasNewMsg(String qqNum,String sid){
		return false;
	}

	/**
	 * ��ݺ��ѷ�����б���Ϣ��ȡ������Ϣ
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
					hasNext=response.indexOf("��ҳ");
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
	 * ȡָ��ҳ��ĺ���
	 * @param pid
	 * @return
	 */
	private static String getGroupUrl(String groupUrl,Integer pageNum){
		groupUrl=groupUrl.substring(0, groupUrl.length()-1)+pageNum.toString();
		return groupUrl;
	}
	/**
	 * �Զ����� ˢ����
	 */
	public static void goOtherHome(String sid){
		HashMap<String, String> params=new HashMap<String, String>();
		params.put("msg",getMsg());
		params.put("entry","board" );
		params.put("ispostmsg","1" );
		try {
			String response=WebUtils.doPost(getFriendurl(sid), params, 0, 0);
			if(response!=null){
			if(response.contains("���Գɹ�")){
				System.out.println(getFriendurl(sid));
			}else{
				System.out.println("����ʧ��");
			}
			}else{
				System.out.println("����ʧ��");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String getFriendurl(String sid){
		return(QQ_ADD_MSG_URL+getRandomQQ()+"&sid="+sid);
	}
	
	/**
	 * �������QQ��
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
	 * ���������������
	 */
	public static String getMsg(){
		return msg[random.nextInt(msg.length)];
	}
	/**
	 * ����һ��˵˵
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
			if(response.contains("����ɹ�")){
				System.out.println("����ɹ�");
			}else{
				System.out.println(response);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
