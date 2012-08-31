package com.mc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * Êý¾Ý²åÈë
 * @author Shine_MuShi
 *
 */
public class BaseDao {
	String sql="insert into qq_zone('fqq','msg','time','result') values(?,?,?,?)";
	DBConn db=null;
	public BaseDao(){
		db=new DBConn();
	}
	
	public boolean saveResult(String fqq,String msg,Date date,String result){
		Connection conn=db.getMySQLConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, fqq);
			ps.setString(2, msg);
			ps.setString(3, date.toString());
			ps.setString(1, result);
			int a=ps.executeUpdate();
			System.out.println(a);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
