package com.mc.domain;
/**
 * QQ好友分组信息
 * @author Shine_MuShi
 *
 */
public class Group {
	private String groupUrl;	//QQ分组URL
	private String groupName;	//QQ分组名称
	private Integer groupIndex;//QQ分组序号

	public String getGroupUrl() {
		return groupUrl;
	}

	public void setGroupUrl(String groupUrl) {
		this.groupUrl = groupUrl;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getGroupIndex() {
		return groupIndex;
	}

	public void setGroupIndex(Integer groupIndex) {
		this.groupIndex = groupIndex;
	}
	
	public	Group(){
		
	}
}
