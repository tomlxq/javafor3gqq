package com.mc.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Shine_MuShi
 *
 */
public class DBConn {
	/**
	 * 获取数据库访问连接
	 * @return
	 */
	public Connection getMySQLConnection(){
		Connection conn=null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			 conn = java.sql.DriverManager.getConnection(
					    "jdbc:mysql://localhost/qqzone?useUnicode=true&characterEncoding=utf-8", "root", "root");
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	/**
	 * 关闭数据库连接
	 */
	public void close(Connection conn){
		try {
			if(conn!=null){
				conn.close();
				conn=null;
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}
}
