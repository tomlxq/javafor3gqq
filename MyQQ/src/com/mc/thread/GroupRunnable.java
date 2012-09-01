package com.mc.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mc.client.QQClient;
import com.mc.domain.Group;

/**
 * @author Shine_MuShi
 *
 */
public class GroupRunnable implements Runnable{
	
	private Group group;
	
	public GroupRunnable(Group group){
		this.group=group;
	}

	@Override
	public void run() {
			String response=QQClient.getFrindsByGroupUrl(group.getGroupUrl());
			List<String> list=new ArrayList<String>();
			//ȡ���еĺ�������
			//Pattern pattern = Pattern.compile("(?<=a href=\").+?(?=\"><img src)");
			//ȡ���еĺ�����Ϣ
			Pattern pattern = Pattern.compile("(?<=&amp;u=).+?(?=&amp)");
			Matcher matcher=null;
			int hasNext=-1;
			int pid=0;
			do{
				pid=pid+1;
				response=QQClient.getFrindsByGroupUrl(getGroupUrl(pid));
				//hasNext=response.indexOf("page-nav bg tp-white");
				hasNext=response.indexOf("��ҳ");
				matcher=pattern.matcher(response);
				while(matcher.find()){
					String firendQQ=matcher.group();
					list.add(firendQQ);
				}	
			}while(hasNext!=-1);
			QQClient.mergeFrind(list);
	}
	
	/**
	 * ȡָ��ҳ��ĺ���
	 * @param pid
	 * @return
	 */
	private String getGroupUrl(Integer pid){
		String groupUrl=group.getGroupUrl();
		groupUrl=groupUrl.substring(0, groupUrl.length()-1)+pid.toString();
		return groupUrl;
	}

}
