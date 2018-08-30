package com.tspace.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.tspace.listener.TimerTask;

public class druidconnectionpool {
	
	
	public static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<Connection>();
	
	private static DruidDataSource druidDataSource = null;
	
	static {
		Properties properties = loadPropertiesFile("jdbc.properties");
		if (properties == null) {
			// System.out.println("properties == null");
		}
		try {
			druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			TimerTask.logger.error("[JDBC Exception --> Faile to configured the Druid DataSources,"
					+ "the exception message is:" + e.getMessage());
		}
	}
	
	public static Connection getConnection() {
		Connection connection = connectionThreadLocal.get();
		try {
			if (null == connection) {
				connection = druidDataSource.getConnection();
				connectionThreadLocal.set(connection);
			}
		} catch (SQLException e) {
			TimerTask.logger.error("[JDBC Exception --> Faile to create a connection,"
					+ "the exception message is:" + e.getMessage());
		}
		return connection;
	}
	
	public static void closeConnection() {
		Connection connection = connectionThreadLocal.get();
		if (null != connection) {
			try {
				connection.close();
				connectionThreadLocal.remove();
			} catch (SQLException e) {
				TimerTask.logger.error("[JDBC Exception --> Faile to closed the DruidPooledConnection,"
						+ "the exception message is:" + e.getMessage());
			}
		}
	}
	
	public static void startTransaction() {
		Connection conn = connectionThreadLocal.get();
		
		try {
			if (null == conn) {
				conn = getConnection();
				connectionThreadLocal.set(conn);
			}
			conn.setAutoCommit(false);
		} catch (Exception e) {
			TimerTask.logger.error("[JDBC Exception --> Faile to start the transaction,"
					+ "the exception message is:" + e.getMessage());
		}
	}
	
	public static void commit() {
		try {
			Connection conn = connectionThreadLocal.get();
			if (null != conn) {
				conn.commit();
			}
		} catch (Exception e) {
			TimerTask.logger.error("[JDBC Exception --> Faile to commit the transaction,"
					+ "the exception message is:" + e.getMessage());
		}
	}
	
	
	public static void rollback() {
		try {
			Connection conn = connectionThreadLocal.get();
			if (conn != null) {
				conn.rollback();
				connectionThreadLocal.remove();
			}
		} catch (Exception e) {
			TimerTask.logger.error("[JDBC Exception --> Faile to rollback the transaction,"
					+ "the exception message is:" + e.getMessage());
		}
	}
	
	private static Properties loadPropertiesFile(String fullFile) {
		if (null == fullFile || fullFile.equals("")) {
			throw new IllegalArgumentException("Properties file path can not be null" + fullFile);
		}
		Properties prop = new Properties();
		try {
			prop.load(druidconnectionpool.class.getClassLoader().getResourceAsStream(fullFile));
		} catch (IOException e) {
			
		}
		return prop;
	}

}
