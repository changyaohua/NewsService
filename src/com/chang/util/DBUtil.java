package com.chang.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 获取数据库的连接实例和关闭与数据库连接的相关资源的通用类
 * 
 * @since 2015年8月20号
 * @author 常耀华
 * @version v1.0
 *
 */
public class DBUtil {
	/**
	 * 返回与mysql数据库进行连接的实例化对象的方法
	 * 
	 * @return 连接的实例化对象
	 * @throws Exception
	 *             与数据库进行连接发生的异常
	 */
	public Connection getConnection() throws Exception {
		Connection connection = null;
		String className="com.mysql.jdbc.Driver";
		Class.forName(className).newInstance();
		String url="jdbc:mysql://55e05a44118d8.sh.cdb.myqcloud.com:16778/HongTaiNews?useUnicode=true&characterEncoding=utf-8";
		String username="cdb_outerroot";
		String userpwd="mysql123";
		connection  = DriverManager.getConnection(url, username, userpwd);
		return connection;
	}

	/**
	 * 关闭与数据库连接的相关资源的方法
	 * 
	 * @param connection
	 *            数据库的连接对象
	 * @param preparedStatement
	 *            数据库的preparedStatement对象
	 * @param resultSet
	 *            数据库的resultSet对象
	 * @throws Exception
	 *             关闭数据库的异常
	 */
	public void closeDBSourse(Connection connection,
			PreparedStatement preparedStatement, ResultSet resultSet)
			throws Exception {
		if (preparedStatement != null) {
			preparedStatement.close();
		}
		if (resultSet != null) {
			resultSet.close();
		}
		if (connection != null) {
			connection.close();
		}
	}
}
