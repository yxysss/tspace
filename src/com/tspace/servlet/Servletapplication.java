package com.tspace.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

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
import com.tspace.bean.Application;
import com.tspace.bean.Room;
import com.tspace.bean.User;
import com.tspace.database.databaseapplication;
import com.tspace.database.databaseroom;
import com.tspace.database.databaseuser;
import com.tspace.listener.TimerTask;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class Servletapplication
 */
@WebServlet("/Servletapplication")
public class Servletapplication extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Servletapplication() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String op = request.getParameter("op");
		// System.out.println(op);
		
		HttpSession session = request.getSession(true);
		String username = (String)session.getAttribute("username");
		String adminid = (String)session.getAttribute("adminid");
		PrintWriter pw = response.getWriter();
		
		// System.out.println("Servletapplication");
		BufferedReader reader = request.getReader();
		String line = null;
		StringBuilder json = new StringBuilder();
		while ((line = reader.readLine())!= null) {
			json.append(line);
		}
		// System.out.println(json.toString());
		JSONObject jsonobject = null;
		try {
			// 接收数据非json格式
			jsonobject = JSONObject.fromObject(json.toString());
		} catch(Exception e) {
			TimerTask.logger.error(e.toString()); 
			// 接收数据格式错误
			pw.print(-5);
			pw.flush();
			pw.close();
			return ;
		}
		
		// 用户查看申请详情
		// -1：未登录
		// -5：数据格式错误
		// -6：非申请者访问
		// -2：系统异常
		// -3：无此申请
		if ("detail".equals(op)) {
			// System.out.println("detail");
			if (username == null) {
				pw.print(-1);
				pw.flush();
				pw.close();
				return ;
			}
			int idapplication = jsonobject.optInt("idapplication", 0);
			if (idapplication == 0) {
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			
			Application application = databaseapplication.getapplication(idapplication);
			if (application == null) {
				pw.print(-2);
				pw.flush();
				pw.close();
				return ;
			}
			if (application.getidapplication() == -1) {
				pw.print(-3);
				pw.flush();
				pw.close();
				return ;
			}
			if (!username.equals(application.getapplicant())) {
				pw.print(-6);
				pw.flush();
				pw.close();
				return ;
			}
			
			// System.out.println(application.toString());
			pw.print(application.toString());
			pw.flush();
			pw.close();
			return ;
		}
		
		// 用户取消申请
		// 0：取消申请成功
		// -3: 取消申请失败
		// -5: 提交格式不正确
		if ("cancel".equals(op)) {
			if (username == null) {
				// -1: 未登录
				pw.print(-1);
				pw.flush();
				pw.close();
				return ;
			}
			int idapplication = jsonobject.optInt("idapplication", 0);
			if (idapplication == 0) {
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			
			int flag = databaseapplication.cancelapplication(username, idapplication);
			if (flag == -1) {
				// 取消申请失败
				pw.print(-3);
			} else {
				// 取消申请成功
				pw.print(0);
			}
			pw.flush();
			pw.close();
			return ;
			
		}
		
		int starttime0, endtime0;
		Calendar startcalendar = Calendar.getInstance(), endcalendar = Calendar.getInstance();
		int day, hour, minute;
		
		
		// 用户提交申请
		// -5：提交数据格式错误
		// -1：用户未登录
		// -9：房间日期未更新，稍后再试
		if ("submit".equals(op)) {
			// System.out.println("submit username=" + username);
			if (username == null) {
				// -1: 未登录
				pw.print(-1);
				pw.flush();
				pw.close();
				return ;
			}
			// 首先check房间表中的日期更新了没有
			Calendar calendar = Calendar.getInstance();
			int y = calendar.get(Calendar.YEAR);
			int m = calendar.get(Calendar.MONTH) + 1;
			int d = calendar.get(Calendar.DAY_OF_MONTH);
			int h = calendar.get(Calendar.HOUR_OF_DAY);
			if (y != TimerTask.year || m != TimerTask.month || d != TimerTask.day) {
				// 日期未更新
				pw.print(-9);
				pw.flush();
				pw.close();
				return ;
			}
			// 检查提交的数据是否符合格式要求
			Application application = new Application();
			application.setidapplication(null);
			int idroom = jsonobject.optInt("idroom", 0);
			if (idroom == 0) {
				// -5: 提交数据格式错误
				// System.out.println("1");
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			application.setidroom(idroom);
			starttime0 = jsonobject.optInt("starttime", 0);
			endtime0 = jsonobject.optInt("endtime", 0);
			if (starttime0 == 0 || endtime0 == 0) {
				// -5: 提交数据格式错误
				// System.out.println("2");
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			day = starttime0/100;
			if (day < 1 || day > 8 || day != endtime0/100 || starttime0 > endtime0) {
				// -5: 提交数据格式错误
				// System.out.println("3");
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			String date = jsonobject.optString("date", "");
			if (date == "") {
				// -5: 提交数据格式错误
				// System.out.println("4");
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			Long datetime = Long.parseLong(date);
			long daytime = datetime/(1000*60*60*24);
			Date Datenow = new Date();
			Long datenow = Datenow.getTime()/(1000*60*60*24);
			/*System.out.println(daytime);
			System.out.println(datenow);*/
			if (daytime != datenow) {
				// -5: 提交数据格式错误
				/*System.out.println("5");
				System.out.println("datetime = " + datetime);
				System.out.println("datenow = " + datenow);
				System.out.println("datenow-7 = " + (datenow-1000*60*60*24*7));*/
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			if (starttime0%100 < 1 || starttime0%100 > 30) {
				// -5: 提交数据格式错误
				// System.out.println("6");
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			if (endtime0%100 < 1 || endtime0%100 > 31) {
				// -5: 提交数据格式错误
				// System.out.println("7");
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			startcalendar.setTimeInMillis(datetime);
			startcalendar.add(Calendar.DATE, day);
			hour = (starttime0%100)/2+7;
			if (starttime0%2 == 1) minute = 30;
			else minute = 0;
			startcalendar.set(Calendar.HOUR_OF_DAY, hour);
			startcalendar.set(Calendar.MINUTE, minute);			
			startcalendar.set(Calendar.SECOND, 0);
			startcalendar.set(Calendar.MILLISECOND, 0);
			endcalendar.setTimeInMillis(Long.parseLong(date));
			endcalendar.add(Calendar.DATE, day);
			hour = ((endtime0%100)-1)/2+8;
			if (endtime0%2 == 1) minute = 0;
			else minute = 30;
			endcalendar.set(Calendar.HOUR_OF_DAY, hour);
			endcalendar.set(Calendar.MINUTE, minute);
			endcalendar.set(Calendar.SECOND, 0);		
			endcalendar.set(Calendar.MILLISECOND, 0);
			application.setstarttime((Date)startcalendar.getTime());
			application.setendtime((Date)endcalendar.getTime());
			application.setapplicant(username);
			String reason = jsonobject.optString("reason", "");
			if (reason.length() > 50) {
				// -5: 提交数据格式错误
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			application.setreason(reason);
			String participants = jsonobject.optString("participants", "");
			if (participants.length() > 50) {
				// -5: 提交数据格式错误
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			application.setparticipants(participants);
			application.setstate("unsettle");
			application.setapplytime(new Date());
			application.setaudittime(null);
			
			// result 
			// 0：提交成功
			// -2:连接数据库出错(系统出错)
			// -3:申请失败，申请日期已有申请
			// -4:申请失败，申请时间段已被占用
			// -5: 提交格式错误
			// -6: 申请的房间不存在
			int result = databaseapplication.insert(application);
			if (result > 0) {
				pw.print(0);
			} else {
				pw.print(result);
			}
			pw.flush();
			pw.close();
			return ;
		}
		
		// 管理员接受申请
		// 0: 操作成功
		// -1：未登录
		// -2: 系统异常
		// -3: 申请不存在
		// -4: 申请中的房间不存在
		// -5: 提交数据格式错误
		// -6：申请已被审核
		if ("acceptapplication".equals(op)) {
			if (adminid == null) {
				// -1：未登录
				pw.print(-1);
				pw.flush();
				pw.close();
				return ;
			}
			int idapplication = jsonobject.optInt("idapplication", 0);
			if (idapplication == 0) {
				// -5: 提交数据格式错误
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			int password = getCode();
			int flag = databaseapplication.acceptapplication(idapplication, password);
			// -3: 被接受的申请不存在
			// -2: 系统异常
			// -4: 申请中的房间不存在
			// -6: 申请已被审核
			// 0: 操作成功
			pw.print(flag);
			pw.flush();
			pw.close();
			if (flag == 0) {
				// 发送通知短信
				Application application = databaseapplication.getapplication(idapplication);
				if (application == null || application.getidapplication() == -1) {
					TimerTask.logger.error("通知短信发送失败！申请获取失败！");
					return ;
				}
				Room room = databaseroom.getroom(application.getidroom());
				if (room == null || room.getidroom() == -1) {
					TimerTask.logger.error("通知短信发送失败！房间获取失败！");
					return ;
				}
				User user = databaseuser.getuser(application.getapplicant());
				if (user == null || user.getiduser() == -1) {
					TimerTask.logger.error("通知短信发送失败！用户获取失败！");
				}
				boolean bflag = sendMessage(user.getmobile(), user.getname(), application.getstarttime(), application.getendtime(),
						room.getnameroom(), password);
				// System.out.println("bflag=" + bflag);
				if (bflag == false) {
					TimerTask.logger.error("通知短信发送失败！发送失败！");
				}
			}
			return ;
		}
		
		// 管理员拒绝申请
		// 0：拒绝申请成功
		// -1： 未登录
		// -2：系统异常
		// -3：申请不存在或已被审核
		// -5：提交数据格式错误
		if ("declineapplication".equals(op)) {
			if (adminid == null) {
				// -1：未登录
				pw.print(-1);
				pw.flush();
				pw.close();
				return ;
			}
			int idapplication = jsonobject.optInt("idapplication", 0);
			if (idapplication == 0) {
				// -5: 提交数据格式错误
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
						
			}
			// -2: 系统异常
			// -3：申请不存在或已被审核
			// 0: 拒绝申请成功
			int flag = databaseapplication.declineapplication(idapplication);
			pw.print(flag);
			pw.flush();
			pw.close();
			return ;
		}

		
	}
	
	private static int getCode() {
		return (int)((Math.random()*9+1)*100000);
	}

	private static String AccessKeyId = "LTAIb7DpYs8bUeTn";
	private static String AccessKeySecret = "SisPiaHnMMK2cIpJ42KKPOd69Bblkx";
	
	private static boolean sendMessage(String mobile, String name, Date start, Date end, String roomname, int password) {
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
		 request.setTemplateCode("SMS_105765056");
		 //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		 //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		 String json = "{\"name\":\"" + name + "\",\"start\":\"" + TimerTask.dateToString(start) + "\",\"end\":\"" 
				 + TimerTask.dateToString(end) + "\",\"room\":\"" + roomname + "\",\"p\":\"" + password + "\"}";
		 // System.out.println("json:" + json);
		 request.setTemplateParam(json);
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
