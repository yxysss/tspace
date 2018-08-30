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
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import com.tspace.bean.User;
import com.tspace.database.databaseuser;
import com.tspace.listener.TimerTask;

/**
 * Servlet implementation class Servletuser
 */
@WebServlet("/Servletuser")
public class Servletuser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Servletuser() {
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
	public Color getRandColor(int s, int e) {
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setDateHeader("Expires", 0);
		
		String op = request.getParameter("op");
		
		// PC端用户退出
		if ("logout".equals(op)) {
			// System.out.println("logoutsubmit");
			HttpSession session = request.getSession(false);
			if (session != null) session.removeAttribute("username");
			PrintWriter pw = response.getWriter();
			pw.print(true);
			pw.flush();
			pw.close();
			return ;
		}
		
		HttpSession session = request.getSession();
		// String sessionId = session.getId();
		// Cookie cookie = new Cookie("JESSIONID", sessionId);
		// cookie.setPath("/curpath");
		// cookie.setMaxAge(30*60);
		
		// 检查是否登录
		if ("checklogin".equals(op)) {
			Boolean flag = true;
			PrintWriter pw = response.getWriter();
			String data = (String)session.getAttribute("username");
			// System.out.println("data="+data);
			pw.print(data);
			pw.flush();
			pw.close();
			return ;
		}
		
		// 获取验证码
		if ("pin".equals(op)) {
			System.out.println("收到");
			response.setContentType("image/jpeg");
			int width=140, height=70;
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
			
			session.setAttribute("randomPIN", sb.toString());
			System.out.println(sb.toString());
			g.dispose();
			ImageIO.write(image, "JPEG", response.getOutputStream());
			return ;
			
		}
		BufferedReader reader = request.getReader();
		
		String line = null;
		StringBuilder json = new StringBuilder();
		while ((line = reader.readLine())!= null) {
			json.append(line);
		}
		System.out.println(json.toString());
		JSONObject jsonobject = null;
		try {
			jsonobject = JSONObject.fromObject(json.toString());
		} catch (Exception e) {
			TimerTask.logger.error(e.toString());
			PrintWriter pw = response.getWriter();
			// -5：提交数据格式错误
			pw.print(-5);
			pw.flush();
			pw.close();
			return ;
		}
		
		// 用户登录
		if ("login".equals(op)) {
			String username = jsonobject.optString("username", "");
			String password = jsonobject.optString("password", "");
			String pin = jsonobject.optString("pin");
			System.out.println(username + " , " + password + " , " + pin);
			Boolean flag = true;
			PrintWriter pw = response.getWriter();
			String correctpassword = databaseuser.getpassword(username);
			String randomPIN = (String)session.getAttribute("randomPIN");
			if (correctpassword != null && randomPIN != null) {
				if (correctpassword.equals(password) && randomPIN.equalsIgnoreCase(pin)) {
					flag = true;
					session.setAttribute("username", username);
					databaseuser.updatelogin(username);
				} else {
					flag = false;
				}
			} else {
				flag = false;
			}
			pw.print(flag);
			pw.flush();
			pw.close();
			return ;
		}
		
		// 用户注册
		if ("register".equals(op)) {
			String username = jsonobject.optString("username", "");
			String password = jsonobject.optString("password", "");
			String identification = jsonobject.optString("identification", "");
			String name = jsonobject.optString("name", "");
			String mobile = jsonobject.optString("mobile", "");
			User user = new User();
			user.setidentification(identification);
			user.setiduser(null);
			user.setmobile(mobile);
			user.setname(name);
			user.setpassword(password);
			user.setusername(username);
			int i = databaseuser.insert(user);
			PrintWriter pw = response.getWriter();
			if (i == -1) pw.print(false);
			else pw.print(true);
			pw.flush();
			pw.close();
			return ;
			
		}
	}
}
