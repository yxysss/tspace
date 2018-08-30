package com.tspace.servlet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.tspace.bean.User;
import com.tspace.database.databaseschool;
import com.tspace.database.databaseuser;
import com.tspace.listener.TimerTask;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class mServletuser
 */
@WebServlet("/mServletuser")
public class mServletuser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public mServletuser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	
	/* 该方法主要作用是获得随机生成的颜色 */
	private Color getRandColor(int s, int e) {
		Random random = new Random();
		if (s > 255)
			s = 255;
		if (e > 255)
			e = 255;
		int r, g, b;
		r = s + random.nextInt(e - s); // 随机生成RGB颜色中的r值
		g = s + random.nextInt(e - s); // 随机生成RGB颜色中的g值
		b = s + random.nextInt(e - s); // 随机生成RGB颜色中的b值
		return new Color(r, g, b);
	}
	
	private Random r = new Random();
	
	// 随机字符集合中不包括0和o，O，1和l，因为这些不易区分 
	private String codes = "23456789abcdefghijkmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYXZ";  
	private char randomChar() {  
        int index = r.nextInt(codes.length());  
        return codes.charAt(index);  
	}
	
	private BufferedImage getVerifyImage(int width, int height, HttpSession session, String attr) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		Graphics2D g2d = (Graphics2D)g;
		
		Random random = new Random();
		Font mfont = new Font("楷体", Font.BOLD, 20);
		g.setColor(getRandColor(200,250));
		g.fillRect(0, 0, width, height);
		g.setFont(mfont);
		g.setColor(getRandColor(180,200));
		
		for (int i = 0; i < 100; i ++) {
			int x = random.nextInt(width-1);
			int y = random.nextInt(height-1);
			int x1 = random.nextInt(6)+1;
			int y1 = random.nextInt(12)+1;
			BasicStroke bs = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
			Line2D line0 = new Line2D.Double(x, y, x+x1, y+y1);
			g2d.setStroke(bs);
			g2d.draw(line0);
		}
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < 4; i ++) {
			String sTemp = String.valueOf(randomChar());
			sb.append(sTemp);
			Color color = new Color(20+random.nextInt(110), 20+random.nextInt(110), random.nextInt(110));
			g.setColor(color);
			
			Graphics2D g2d_word = (Graphics2D)g;
			AffineTransform trans = new AffineTransform();
			trans.rotate((45)*3.14/180, 15*i+8, 7);
			
			float scaleSize = random.nextFloat() + 0.8f;
			if (scaleSize > 1f) {
				scaleSize = 1f;
			}
			trans.scale(scaleSize, scaleSize);
			g2d_word.setTransform(trans);
			g.drawString(sTemp, 25*i+30, 28);
		}
		
		session.setAttribute(attr, sb.toString());
		// System.out.println(sb.toString());
		g.dispose();
		return image;
	}
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		// 禁用缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setDateHeader("Expires", 0);
		
		String op = request.getParameter("op");
		// System.out.println(op);
		
		HttpSession session = request.getSession();
		
		// 移动端用户注册获取验证短信前先进行验证码验证
		if ("registerpin".equals(op)) {
			// System.out.println("收到");
			response.setContentType("image/jpeg");
			int width=140, height=70;
			BufferedImage image = getVerifyImage(width, height, session, "registerpin");
			ImageIO.write(image, "JPEG", response.getOutputStream());
			return ;
		}
		
		// 移动端用户忘记密码获取验证短信前先进行验证码验证
		if ("forgetpwpin".equals(op)) {
			// System.out.println("收到");
			response.setContentType("image/jpeg");
			int width=140, height=70;
			BufferedImage image = getVerifyImage(width, height, session, "forgetpwpin");
			ImageIO.write(image, "JPEG", response.getOutputStream());
			return ;
		}
		
		// 移动端用户退出
		if ("exit".equals(op)) {
			// System.out.println("logoutsubmit");
			session.removeAttribute("username");
			PrintWriter pw = response.getWriter();
			// 返回true，退出成功
			pw.print(true);
			pw.flush();
			pw.close();
			return ;
		}
		
		// 忘记密码操作获取验证码的时间
		if ("fpcheckSmstime".equals(op)) {
			Date lastdate = (Date)session.getAttribute("fpSmstime");
			PrintWriter pw = response.getWriter();
			if (lastdate == null) {
				pw.print(0);
				pw.flush();
				pw.close();
				return ;
			}
			Date newdate = new Date();
			int seconds = (int)((newdate.getTime()-lastdate.getTime())/1000);
			if (seconds >= 60) {
				pw.print(0);
				pw.flush();
				pw.close();
				return ;
			}
			pw.print(60-seconds);
			pw.flush();
			pw.close();
			return ;
		}
		
		// 检查用户获取验证码的时间
		if ("checkSmstime".equals(op)) {
			// 返回可获取验证码短信的剩余时间
			Date lastdate = (Date)session.getAttribute("Smsrequesttime");
			PrintWriter pw = response.getWriter();
			if (lastdate == null) {
				// System.out.println("lastdate is null");
				pw.print(0);
				pw.flush();
				pw.close();
				return ;
			}
			Date newdate = new Date();
			int seconds = (int)((newdate.getTime()-lastdate.getTime())/1000);
			if (seconds >= 60) {
				pw.print(0);
				pw.flush();
				pw.close();
				return ;
			}
			pw.print(60-seconds);
			pw.flush();
			pw.close();
			return ;
		}
		
		BufferedReader reader = request.getReader();
		
		String line = null;
		StringBuilder json = new StringBuilder();
		while ((line = reader.readLine())!= null) {
			json.append(line);
		}
		// System.out.println(json.toString());
		JSONObject jsonobject = null;
		try {
			jsonobject = JSONObject.fromObject(json.toString());
		} catch(Exception e) {
			// 接收的数据非json格式
			TimerTask.logger.error(e.toString());
			PrintWriter pw = response.getWriter();
			// -5: 提交数据格式错误
			pw.print(-5);
			pw.flush();
			pw.close();
			return ;
		}
		
		// 移动端用户手机号注册前进行验证码验证
		// -3: 验证码格式错误
		// 0: 验证成功
		// -1: 验证失败
		if ("registerpinverify".equals(op)) {
			String inputpin = jsonobject.optString("inputpin", "");
			PrintWriter pw = response.getWriter();
			if (inputpin.length() != 4) {
				// 验证码格式错误
				pw.print(-3);
				pw.flush();
				pw.close();
				return ;
			}
			String ans = (String) session.getAttribute("registerpin");
			// System.out.print("ans="+ans);
			if (inputpin.equalsIgnoreCase(ans)) {
				session.setAttribute("registerpinpass", System.currentTimeMillis());
				// 验证成功
				pw.print(0);
				pw.flush();
				pw.close();
				return ;
			} else {
				// 验证失败
				pw.print(-1);
				pw.flush();
				pw.close();
				return ;
			}
		}
		
		// 移动端用户忘记密码前进行验证码验证
		// -3: 验证码格式错误
		// 0: 验证成功
		// -1: 验证失败
		if ("forgetpwpinverify".equals(op)) {
			String inputpin = jsonobject.optString("inputpin", "");
			PrintWriter pw = response.getWriter();
			if (inputpin.length() != 4) {
				// 验证码格式错误
				pw.print(-3);
				pw.flush();
				pw.close();
				return ;
			}
			String ans = (String) session.getAttribute("forgetpwpin");
			// System.out.print("ans="+ans);
			if (inputpin.equalsIgnoreCase(ans)) {
				session.setAttribute("forgetpwpinpass", System.currentTimeMillis());
				// 验证成功
				pw.print(0);
				pw.flush();
				pw.close();
				return ;
			} else {
				// 验证失败
				pw.print(-1);
				pw.flush();
				pw.close();
				return ;
			}
		}
		
		// 移动端用户登录
		// -2: 系统异常
		// -3: 用户名和密码不能为空
		// -4: 用户名不存在
		// -5: 提交数据格式错误
		// -6: 密码错误，登录失败
		// 0: 登录成功
		if ("login".equals(op)) {
			String username = jsonobject.optString("username", "");
			String password = jsonobject.optString("password", "");
			PrintWriter pw = response.getWriter();
			if (username.equals("") || password.equals("")) {
				// 用户名和密码不能为空
				pw.print(-3);
				pw.flush();
				pw.close();
				return ;
			}
			// System.out.println(username + " , " + password);
			int flag = -1;
			String correctpassword = databaseuser.getpassword(username);
			if (correctpassword != null) {
				if (correctpassword.equals("")) {
					// 用户名不存在
					pw.print(-4);
					pw.flush();
					pw.close();
				}
				if (correctpassword.equals(password)) {
					// 登录成功
					flag = 0;
					TimerTask.userloginvolume += 1;
					// System.out.println("servletuser session="+session);
					session.setAttribute("username", username);
					databaseuser.updatelogin(username);
				} else {
					// 登录失败
					flag = -6;
				}
			} else {
				// 系统内部异常
				flag = -2;
			}
			pw.print(flag);
			pw.flush();
			pw.close();
			return ;
		}
		
		// 用户修改密码
		// 0: 更改成功
		// -1： 未登录
		// -2： 系统异常
		// -5: 新旧密码不能为空
		// -4: 密码格式错误
		// -3： 原密码错误
		if ("changepassword".equals(op)) {
			String oldpassword = jsonobject.optString("oldpassword", "");
			String newpassword = jsonobject.optString("newpassword", "");
			PrintWriter pw = response.getWriter();
			if (oldpassword.equals("") || newpassword.equals("")) {
				// 新旧密码不能为空
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			
			String username = (String) session.getAttribute("username");
			if (username == null) {
				// 未登录
				pw.print(-1);
				pw.flush();
				pw.close();
				return ;
			}
			
			String password = databaseuser.getpassword(username);
			if (password == null) {
				// 系统内部异常
				pw.print(-2);
				pw.flush();
				pw.close();
				return ;
			}
			
			// 检查新密码格式是否正确
			// 密码长度为32位之间，由英文字母和数字组成
			// -4: 新密码格式不正确
			if (newpassword.length() == 32) {
				boolean flag = true;
				for (int i = 0; i < newpassword.length(); i ++) {
					flag = false;
					char c = newpassword.charAt(i);
					if (c >= '0' && c <= '9') flag = true;
					if (c >= 'a' && c <= 'z') flag = true;
					if (c >= 'A' && c <= 'Z') flag = true;
					if (flag == false) {
						pw.print(-4);
						pw.flush();
						pw.close();
						return ;
					}
				}
			} else {
				pw.print(-4);
				pw.flush();
				pw.close();
				return ;
			}
			
			if (password.equals(oldpassword)) {
				// 修改密码
				int flag = databaseuser.setpassword(username, newpassword);
				if (flag == -1) {
					// 系统内部异常
					pw.print(-2);
				} else {
					// 更改成功
					session.removeAttribute("username");
					pw.print(0);
				}
				pw.flush();
				pw.close();
				return ;
			} else {
				// 原密码错误
				pw.print(-3);
				pw.flush();
				pw.close();
				return ;
			}
			
		}
		
		// 用户注册-完善信息
		// -9：验证超时
		// -5：数据格式错误,有未填信息
		// -1：身份证格式错误
		// -2：系统异常
		// -3：密码格式错误
		// -4：学校编号错误
		// -10：学号，身份证号或手机号被使用
		if ("register1".equals(op)) {
			Boolean check = (Boolean) session.getAttribute("register1");
			// 未经手机验证直接进入，注册无效
			if (check == null) return ;
			Date present = new Date();
			Date lastdate = (Date) session.getAttribute("register1time");
			PrintWriter pw = response.getWriter();
			// 验证超时
			if ((present.getTime()-lastdate.getTime())/1000 > 1200) {
				pw.print("-9");
				pw.flush();
				pw.close();
				return ;
			}
			String username = jsonobject.optString("username", "");
			String password = jsonobject.optString("password", "");
			String identification = jsonobject.optString("identification", "");
			String name = jsonobject.optString("name", "");
			int school = jsonobject.optInt("school", 0);
			int identity = jsonobject.optInt("identity", -1);
			if (username.equals("") || password.equals("") || identification.equals("") || 
					name.equals("") || identity == -1 || school == 0) {
				// 提交数据格式错误，不完整
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			
			// 检查学号格式是否正确
			// 学号长度为11位，由数字组成
			/*
			if (username.length() == 11) {
				boolean flag = true;
				for (int i = 0; i < 11; i ++) {
					char c = username.charAt(i);
					if (c < '0' || c > '9') {
						flag = false;
						break;
					}
				}
				if (flag == false) {
					pw.print(-6);
					pw.flush();
					pw.close();
					return ;
				}
				
			} else {
				pw.print(-6);
				pw.flush();
				pw.close();
				return ;
			}
			*/
			
			// 检查身份证格式是否正确
			// 身份证号码长度18位，均由数字组成
			if (identification.length() == 18) {
				boolean flag = true;
				for (int i = 0; i < 18; i ++) {
					char c = identification.charAt(i);
					// 身份证号码中会出现x
					if (c < '0' || c > '9' && c != 'x' && c != 'X') {
						flag = false;
						break;
					}
				}
				if (flag == false) {
					pw.print(-1);
					pw.flush();
					pw.close();
				}
			} else {
				pw.print(-1);
				pw.flush();
				pw.close();
			}
			
			// 检查密码格式是否正确
			// 加密后密码长度为32，由英文字母和数字组成
			// System.out.println("password.length = " + password.length());
			if (password.length() == 32) {
				boolean flag = true;
				for (int i = 0; i < password.length(); i ++) {
					flag = false;
					char c = password.charAt(i);
					if (c >= '0' && c <= '9') flag = true;
					if (c >= 'a' && c <= 'z') flag = true;
					if (c >= 'A' && c <= 'Z') flag = true;
					if (flag == false) {
						pw.print(-3);
						pw.flush();
						pw.close();
						return ;
					}
				}
			} else {
				pw.print(-3);
				pw.flush();
				pw.close();
				return ;
			}
			
			// 检查学校编号是否正确
			String schoolname = databaseschool.getschoolname(school);
			if (schoolname == null) {
				// -2: 系统内部异常
				pw.print(-2);
				pw.flush();
				pw.close();
				return ;
			}
			if (schoolname.equals("")) {
				// -4: 学校编号错误
				pw.print(-4);
				pw.flush();
				pw.close();
				return ;
			}
			
			String mobile = (String) session.getAttribute("mobile");
			User user = new User();
			user.setidentification(identification);
			user.setiduser(null);
			user.setmobile(mobile);
			user.setname(name);
			user.setpassword(password);
			user.setusername(username);
			user.setidentity(identity);
			// System.out.println("idschool:"+school);
			user.setschool(school);
			int i = databaseuser.insert(user);
			// System.out.println(i);
			if (i == -1) {
				// 系统内部异常
				pw.print(-2);
			}
			else {
				if (i == -10) {
					// -10: 学号，身份证或手机已被使用
					pw.print(-10);
				} else {
					// 0: 注册成功
					pw.print(0);
					session.setAttribute("username", username);
					session.removeAttribute("register1");
				}
			}
			pw.flush();
			pw.close();
			return ;
			
		}
		
		// 用户注册-手机验证
		// -7：手机和验证码不匹配
		if ("register0".equals(op)) {
			PrintWriter pw = response.getWriter();
			String mobile = jsonobject.optString("mobile", "");
			int code = jsonobject.optInt("code", -1);
			String amobile = (String) session.getAttribute("mobile");
			// -7: 手机号和验证码不匹配
			// 未获取过验证码
			if (amobile == null) {
				// System.out.println("未获取过验证码");
				pw.print(-7);
				pw.flush();
				pw.close();
				return ;
				
			}
			// 获取的验证码非当前手机号
			if (!amobile.equals(mobile)) {
				// System.out.println("获取的验证码非当前手机号");
				pw.print(-7);
				pw.flush();
				pw.close();
				return ;
			}
			
			Date lastdate = (Date)session.getAttribute("Smsrequesttime");
			if (lastdate == null) {
				// System.out.println("未获取验证码");
				pw.print(-7);
				pw.flush();
				pw.close();
				return ;
			}
			Date newdate = new Date();
			int seconds = (int)((newdate.getTime()-lastdate.getTime())/1000);
			// 验证码超时
			if (seconds >= 180) {
				// System.out.println("验证码超时");
				pw.print(-7);
				pw.flush();
				pw.close();
				return ;
			}
			int acode = (int) session.getAttribute("code");
			// 验证码输入错误
			if (code != acode) {
				// System.out.println("验证码输入错误");
				pw.print(-7);
				pw.flush();
				pw.close();
				return ;
			}
			String sql = "select * from user where mobile='"+mobile+"'";
			List<User> users = databaseuser.get(sql);
			// 手机号已被使用
			if (users.size() > 0) {
				pw.print(-10);
				pw.flush();
				pw.close();
			}
			// 手机与验证码匹配
			session.setAttribute("register1", true);
			session.setAttribute("register1time", new Date());
			pw.print(0);
			pw.flush();
			pw.close();
			return ;
		}
		
		/*
		// 用户注册
		// -3：密码格式错误
		// -4: 身份证格式不正确
		// -5：提交数据格式错误
		// -6： 学号格式不正确
		// -2：系统异常
		// -9: 学校编号错误
		// -7：手机号和验证码不匹配
		// -10: 学号，身份证或手机已被使用
		// 0: 注册成功
		if ("register".equals(op)) {
			PrintWriter pw = response.getWriter();
			String username = jsonobject.optString("username", "");
			String password = jsonobject.optString("password", "");
			String identification = jsonobject.optString("identification", "");
			String name = jsonobject.optString("name", "");
			String mobile = jsonobject.optString("mobile", "");
			int code = jsonobject.optInt("code", 0);
			int school = jsonobject.optInt("school", 0);
			int identity = jsonobject.optInt("identity", -1);
			if (username.equals("") || password.equals("") || identification.equals("") || 
					name.equals("") || mobile.equals("") || code == 0 || school == 0 ||
					identity == -1) {
				// 提交数据格式错误
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			
			// 检查学号格式是否正确
			// 学号长度为11位，由数字组成
			if (username.length() == 11) {
				boolean flag = true;
				for (int i = 0; i < 11; i ++) {
					char c = username.charAt(i);
					if (c < '0' || c > '9') {
						flag = false;
						break;
					}
				}
				if (flag == false) {
					pw.print(-6);
					pw.flush();
					pw.close();
					return ;
				}
				
			} else {
				pw.print(-6);
				pw.flush();
				pw.close();
				return ;
			}
			
			// 检查身份证格式是否正确
			// 身份证号码长度18位，均由数字组成
			if (identification.length() == 18) {
				boolean flag = true;
				for (int i = 0; i < 18; i ++) {
					char c = identification.charAt(i);
					if (c < '0' || c > '9') {
						flag = false;
						break;
					}
				}
				if (flag == false) {
					pw.print(-4);
					pw.flush();
					pw.close();
				}
			} else {
				pw.print(-4);
				pw.flush();
				pw.close();
			}
			
			// 检查密码格式是否正确
			// 加密后密码长度为32，由英文字母和数字组成
			System.out.println("password.length = " + password.length());
			if (password.length() == 32) {
				boolean flag = true;
				for (int i = 0; i < password.length(); i ++) {
					flag = false;
					char c = password.charAt(i);
					if (c >= '0' && c <= '9') flag = true;
					if (c >= 'a' && c <= 'z') flag = true;
					if (c >= 'A' && c <= 'Z') flag = true;
					if (flag == false) {
						pw.print(-3);
						pw.flush();
						pw.close();
						return ;
					}
				}
			} else {
				pw.print(-3);
				pw.flush();
				pw.close();
				return ;
			}
			
			// 检查学校编号是否正确
			String schoolname = databaseschool.getschoolname(school);
			if (schoolname == null) {
				// -2: 系统内部异常
				pw.print(-2);
				pw.flush();
				pw.close();
				return ;
			}
			if (schoolname.equals("")) {
				// -9: 学校编号错误
				pw.print(-9);
				pw.flush();
				pw.close();
				return ;
			}
			
			String amobile = (String) session.getAttribute("mobile");
			// -7: 手机号和验证码不匹配
			// 未获取过验证码
			if (amobile == null) {
				System.out.println("未获取过验证码");
				pw.print(-7);
				pw.flush();
				pw.close();
				return ;
				
			}
			// 获取的验证码非当前手机号
			if (!amobile.equals(mobile)) {
				System.out.println("获取的验证码非当前手机号");
				pw.print(-7);
				pw.flush();
				pw.close();
				return ;
			}
			
			Date lastdate = (Date)session.getAttribute("Smsrequesttime");
			if (lastdate == null) {
				System.out.println("未获取验证码");
				pw.print(-7);
				pw.flush();
				pw.close();
				return ;
			}
			Date newdate = new Date();
			int seconds = (int)((newdate.getTime()-lastdate.getTime())/1000);
			// 验证码超时
			if (seconds >= 180) {
				System.out.println("验证码超时");
				pw.print(-7);
				pw.flush();
				pw.close();
				return ;
			}
			int acode = (int) session.getAttribute("code");
			// 验证码输入错误
			if (code != acode) {
				System.out.println("验证码输入错误");
				pw.print(-7);
				pw.flush();
				pw.close();
				return ;
			}
			// 添加用户信息
			User user = new User();
			user.setidentification(identification);
			user.setiduser(null);
			user.setmobile(mobile);
			user.setname(name);
			user.setpassword(password);
			user.setusername(username);
			user.setidentity(identity);
			System.out.println("idschool:"+school);
			user.setschool(school);
			int i = databaseuser.insert(user);
			if (i == -1) {
				// 系统内部异常
				pw.print(-2);
			}
			else {
				if (i == 10) {
					// -10: 学号，身份证或手机已被使用
					pw.print(-10);
				} else {
					// 0: 注册成功
					pw.print(0);
					session.setAttribute("username", username);
				}
			}
			pw.flush();
			pw.close();
			return ;
		}
		*/
		
		// 用户注册获取手机验证码
		// -5: 提交数据格式错误
		// -4： 手机号格式错误
		// -3：发送失败
		// 0：发送成功
		// -2：验证码未通过或者验证码超时
		// -6：距离上次获取验证码时间小于60s
		if ("getSmsVerify".equals(op)) {
			Long registerpinpass = (Long) session.getAttribute("registerpinpass");
			PrintWriter pw = response.getWriter();
			if (registerpinpass == null) {
				// 未通过验证码判断
				pw.print(-2);
				pw.flush();
				pw.close();
				return ;
			} else {
				Long currenttime = System.currentTimeMillis();
				if (currenttime - registerpinpass > 5*60*1000) {
					// 验证码超时
					pw.print(-2);
					pw.flush();
					pw.close();
					return ;
				}
			}
			String mobile = jsonobject.optString("mobile", "");
			
			// 检查手机号格式是否正确
			if (mobile.equals("")) {
				// -5: 手机号不能为空
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			
			// 手机号长度为11位，由数字组成
			if (mobile.length() == 11) {
				boolean flag = true;
				for (int i = 0; i < 11; i ++) {
					char c = mobile.charAt(i);
					if (c < '0' || c > '9') {
						flag = false;
					}
					if (flag == false) {
						// -4: 手机号格式错误
						pw.print(-4);
						pw.flush();
						pw.close();
						return ;
					}
				}
			} else {
				// -4: 手机号格式错误
				pw.print(-4);
				pw.flush();
				pw.close();
				return ;
			}
			session.setAttribute("mobile", mobile);
			session.removeAttribute("register1");
			Date smsdate = (Date) session.getAttribute("Smsrequesttime");
			Date date = new Date();
			if (smsdate != null) {
				if ((int)((date.getTime()-smsdate.getTime())/1000) < 60) {
					// 距离上次获取时间小于60s
					pw.print(-6);
					pw.flush();
					pw.close();
					return ;
				}
			}
			// System.out.println(date.toLocaleString());
			
			int code = getCode();
			// System.out.println("code:" + code);
			boolean flag = sendMessage(mobile, code);
			if (flag == true) {
				session.setAttribute("Smsrequesttime", date);
				session.setAttribute("code", code);
				// 0: 发送成功
				pw.print(0);
			} else {
				// -3: 发送失败
				pw.print(-3);
			}
			pw.flush();
			pw.close();
			return ;
		}
		
		// 用户忘记密码操作获取验证码
		// -2: 系统内部异常
		// -3: 用户名不存在
		// -5: 提交数据格式不正确
		// 0: 发送成功
		// -4: 发送失败
		// -6：验证码未通过或者验证码超时
		// -7：距离上次发短信时间小于60s
		if ("fpgetVerify".equals(op)) {
			PrintWriter pw = response.getWriter();
			Long forgetpwpinpass = (Long) session.getAttribute("forgetpwpinpass");
			if (forgetpwpinpass == null) {
				// 验证码未通过
				pw.print(-6);
				pw.flush();
				pw.close();
				return ;
			} else {
				Long currenttime = System.currentTimeMillis();
				if (currenttime - forgetpwpinpass > 5*60*1000) {
					// 验证码超时
					pw.print(-6);
					pw.flush();
					pw.close();
					return ;
				}
			}
			String username = jsonobject.optString("username", "");
			if (username.equals("")) {
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			// 检查学号和手机是否一致
			User user = databaseuser.getuser(username);
			if (user == null) {
				pw.print(-2);
				pw.flush();
				pw.close();
				return ;
			}
			if (user.getiduser() == -1) {
				pw.print(-3);
				pw.flush();
				pw.close();
				return ;
			}
			String amobile = user.getmobile();
			int code = getCode();
			// System.out.println("code = " + code);
			Date smsdate = (Date) session.getAttribute("fpSmstime");
			Date date = new Date();
			if ((int)((date.getTime()-smsdate.getTime())/1000) < 60) {
				pw.print(-7);
				pw.flush();
				pw.close();
				return ;
			}
			boolean flag = sendMessage(amobile, code);
			if (flag == true) {
				session.setAttribute("fpusername", username);
				session.setAttribute("fpcode", code);
				session.setAttribute("fpSmstime", date);
				pw.print(0);
				pw.flush();
				pw.close();
				return ;
			} else {
				pw.print(-4);
				pw.flush();
				pw.close();
				return ;
			}
		}
		
		// 用户忘记密码操作设置密码
		// 0：   操作成功
		// -2: 系统异常
		// -3: 密码格式错误
		// -5: 提交数据格式错误
		// -6: 未使用手机进行验证
		// -4: 验证码和用户名不一致
		if ("forgetpassword".equals(op)) {
			PrintWriter pw = response.getWriter();
			String username = jsonobject.optString("username", "");
			int code = jsonobject.optInt("code", 0);
			String password = jsonobject.optString("password", "");
			if (username.equals("") || code == 0 || password.equals("")) {
				// 提交数据格式错误
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			// 检查密码格式是否符合规范
			if (password.length() == 32) {
				boolean flag = true;
				for (int i = 0; i < password.length(); i ++) {
					flag = false;
					char c = password.charAt(i);
					if (c >= '0' && c <= '9') flag = true;
					if (c >= 'a' && c <= 'z') flag = true;
					if (c >= 'A' && c <= 'Z') flag = true;
					if (flag == false) {
						// 密码格式错误
						pw.print(-3);
						pw.flush();
						pw.close();
						return ;
					}
				}
			} else {
					// 密码格式错误
					pw.print(-3);
					pw.flush();
					pw.close();
					return ;
			}
			String fpusername = (String)session.getAttribute("fpusername");
			Integer fpcode = (Integer)session.getAttribute("fpcode");
			if (fpusername == null || fpcode == null) {
				// 为获取验证码
				pw.print(-6);
				pw.flush();
				pw.close();
				return ;
			}
			if (code == fpcode && username.equals(fpusername)) {
				int flag = databaseuser.setpassword(fpusername, password);
				if (flag == -1) {
					// 系统异常
					pw.print(-2);
					pw.flush();
					pw.close();
					return ;
				} else {
					// 密码修改成功
					pw.print(0);
					pw.flush();
					pw.close();
					return ;
				}
			} else {
				// 验证码错误或用户名与验证码不一致
				pw.print(-4);
				pw.flush();
				pw.close();
				return ;
			}
		}
	}
	
	private static int getCode() {
		return (int)((Math.random()*9+1)*100000);
	}
	
	private static String AccessKeyId = "LTAIb7DpYs8bUeTn";
	private static String AccessKeySecret = "SisPiaHnMMK2cIpJ42KKPOd69Bblkx";
	
	private static boolean sendMessage(String mobile, int code) {
		//设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		//初始化ascClient需要的几个参数
		final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
		final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
		//替换成你的AK
		final String accessKeyId = AccessKeyId;//你的accessKeyId,参考本文档步骤2
		final String accessKeySecret = AccessKeySecret;//你的accessKeySecret，参考本文档步骤2
		//初始化ascClient,暂时不支持多region（请勿修改）
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
		accessKeySecret);
		try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			TimerTask.logger.error(e.toString());
			return false;
		}
		IAcsClient acsClient = new DefaultAcsClient(profile);
		 //组装请求对象
		 SendSmsRequest request = new SendSmsRequest();
		 //使用post提交
		 request.setMethod(MethodType.POST);
		 //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
		 request.setPhoneNumbers(mobile);
		 //必填:短信签名-可在短信控制台中找到
		 request.setSignName("T空间");
		 //必填:短信模板-可在短信控制台中找到
		 request.setTemplateCode("SMS_105830043");
		 //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		 //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		 request.setTemplateParam("{\"code\":" + code + "}");
		 //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
		 //request.setSmsUpExtendCode("90997");
		 //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		 request.setOutId("yourOutId");
		//请求失败这里会抛ClientException异常
		SendSmsResponse sendSmsResponse = null;
		try {
			sendSmsResponse = acsClient.getAcsResponse(request);
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			TimerTask.logger.error(e.toString());
			return false;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			TimerTask.logger.error(e.toString());
			return false;
		}
		// System.out.println(sendSmsResponse.getCode());
		if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
			// 请求成功
			return true;
		}
		return false;
		
	}

}
