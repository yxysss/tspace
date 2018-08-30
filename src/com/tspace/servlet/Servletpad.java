package com.tspace.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tspace.bean.Application;
import com.tspace.bean.Room;
import com.tspace.database.databaseapplication;
import com.tspace.database.databasepadadmin;
import com.tspace.database.databaseroom;
import com.tspace.listener.TimerTask;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class Servletpad
 */
@WebServlet("/Servletpad")
public class Servletpad extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Servletpad() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String op = request.getParameter("op");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setDateHeader("Expires", 0);
		
		// System.out.println(op);
		
		PrintWriter pw = response.getWriter();
		
		// System.out.println("Servletpad");
		
		// Administrator登录
		// E: 数据格式不正确
		// A: 登录成功
		// B: 用户名或密码错误
		if ("login".equals(op)) {
			
			// System.out.println("loginoperation");
			
			BufferedReader reader = request.getReader();
			String line = null;
			StringBuilder json = new StringBuilder();
			while ((line = reader.readLine())!= null) {
				// System.out.println("line="+line);
				json.append(line);
			}
			// System.out.println("data="+json.toString());
			// java中String.split()方法的参数为正则表达式，对于'+'需要进行转义
			String[] string = json.toString().split("\\+");
			if (string.length != 2) return ;
			if (string[0] == null || string[1] == null) {
				pw.print("E\n");
				pw.flush();
				pw.close();
				return ;
			}
			String password = databasepadadmin.getpassword(string[0]);
			if (!string[1].equals(password)) {
				pw.print("B\n");
				pw.flush();
				pw.close();
				return ;
			}
			pw.print("A\n");
			pw.flush();
			pw.close();
			return ;
		}
		
		// 用户开门
		// E: 格式错误
		// D: 无使用
		// B: 密码错误
		// A: 密码正确
		if ("opendoor".equals(op)) {
			
			BufferedReader reader = request.getReader();
			String line = null;
			StringBuilder json = new StringBuilder();
			while ((line = reader.readLine())!= null) {
				json.append(line);
			}
			// System.out.println(json.toString());
			String[] string = json.toString().split("\\+");
			if (string.length != 2) {
				pw.print("E\n");
				pw.flush();
				pw.close();
				return ;
			}
			Integer idroom = new Integer(string[1]);
			if (string[0].length() != 6 || string[1].length() != 5 || idroom/10000 != 1) {
				pw.print("E\n");
				pw.flush();
				pw.close();
				return ;
			}
			/*String[] string = new String[2];
			string[1] = "10001";*/
			Date date = new Date();
			// System.out.println(date.toLocaleString());
			String sql = "select * from application where idroom='"+string[1]+"' and state='accept' and starttime<='"+TimerTask.dateToString(date)+"' and endtime >'"+TimerTask.dateToString(date)+"' and password='"+string[0]+"'";
			ArrayList<Application> applications = (ArrayList<Application>) databaseapplication.get(sql);
			if (applications.size() == 0) {
				pw.print("D\n");
				pw.flush();
				pw.close();
				return ;
			}
			String key = applications.get(0).getpassword();
			if (string[0].equals(key)) {
				pw.print("A\n");
				pw.flush();
				pw.close();
				return ;
			} else {
				pw.print("B\n");
				pw.flush();
				pw.close();
				return ;
			}
		}
		
		// Administrator获取房间
		if ("getroomdata".equals(op)) {
			String sql = "select * from room";
			ArrayList<Room> rooms = (ArrayList<Room>) databaseroom.get(sql);
			int length = rooms.size();
			JSONArray jsonarray = new JSONArray();
			for (int i = 0; i < length; i ++) {
				JSONObject jsonobject = new JSONObject();
				jsonobject.put("idroom", rooms.get(i).getidroom());
				jsonobject.put("roomname", rooms.get(i).getnameroom());
				jsonarray.add(jsonobject);
			}
			pw.print(jsonarray);
			pw.flush();
			pw.close();
			return ;
		}
		
		
		
		
	}
	
}
