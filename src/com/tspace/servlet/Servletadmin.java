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

import com.tspace.database.databaseadmin;
import com.tspace.database.databaseuser;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class Servletadmin
 */
@WebServlet("/Servletadmin")
public class Servletadmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Servletadmin() {
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
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
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String op = request.getParameter("op");
		// System.out.println("admin " + op);
		
		HttpSession session = request.getSession();
		
		// 获取验证码
		if ("pin".equals(op)) {
			// System.out.println("收到");
			response.setContentType("image/jpeg");
			int width=140, height=70;
			BufferedImage image = getVerifyImage(width, height, session, "adminrandomPIN");
			ImageIO.write(image, "JPEG", response.getOutputStream());
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
		jsonobject = JSONObject.fromObject(json.toString());
		
		// 管理员登录
		if ("login".equals(op)) {
			PrintWriter pw = response.getWriter();
			String adminid = jsonobject.optString("adminid", "");
			String password = jsonobject.optString("password", "");
			String pin = jsonobject.optString("pin", "");
			if (adminid == "" || password == "" || pin == "") {
				// 提交数据格式错误
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			// System.out.println(adminid + " , " + password + " , " + pin);
			Boolean flag = true;
			String correctpassword = databaseadmin.getpassword(adminid);
			if (correctpassword == null) {
				// 系统异常
				pw.print(-2);
				pw.flush();
				pw.close();
				return ;
			}
			if (correctpassword.equals("")) {
				// 用户名不存在
				pw.print(-4);
				pw.flush();
				pw.close();
				return ;
			}
			String randomPIN = (String)session.getAttribute("adminrandomPIN");
			if (randomPIN != null) {
				if (correctpassword.equals(password) && randomPIN.equalsIgnoreCase(pin)) {
					// 用户名和密码验证正确
					flag = true;
					session.setAttribute("adminid", adminid);
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
		
		if ("deleteuser".equals(op)) {
			String adminid = (String) session.getAttribute("adminid");
			if (adminid == null) return ;
			String username = jsonobject.optString("username", "");
			if (username == "") return ;
			int result = databaseuser.delete(username);
			PrintWriter pw = response.getWriter();
			pw.print(result);
			pw.flush();
			pw.close();
			return ;
		}
		
	}

}
