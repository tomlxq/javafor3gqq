package com.mc.domain;
/**
 * QQ分组信息
 * @author Shine_MuShi
 *
 */
public class Group {
	private String groupUrl;	//分组链接
	private String groupName;	//分组名称
	private Integer groupIndex;//分组索引

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
