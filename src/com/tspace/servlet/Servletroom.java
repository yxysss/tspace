package com.tspace.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;

import net.sf.json.JSONObject;

import com.tspace.bean.Room;
import com.tspace.database.databaseroom;
import com.tspace.listener.TimerTask;

/**
 * Servlet implementation class Servletroom
 */
@WebServlet("/Servletroom")
public class Servletroom extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Servletroom() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setDateHeader("Expires", 0);
		
		String op = request.getParameter("op");
		
		HttpSession session = request.getSession();
		
		// 获取真实路径
		StringBuilder realpathbuilder = new StringBuilder(request.getSession().getServletContext().getRealPath(request.getRequestURI()));
		String realpath = realpathbuilder.substring(0, realpathbuilder.length()-18).replace('\\', '/');
		// System.out.println(realpath);		
		// System.out.println(realpath);
		
		// 添加房间
		if ("insertroom".equals(op)) {
			String adminid = (String) session.getAttribute("adminid"); 
			PrintWriter pw = response.getWriter();
			if (adminid == null) {
				// -1：管理员未登录
				pw.print(-1);
				pw.flush();
				pw.close();
				return ;
			}
			File repository = null;
			File savedFile = null;
			try {
				// int tmpc = ++TimerTask.tmpcount;
				repository = new File(realpath+"upload/tmp/");
				// File repository = new File("C:/Program Files/server/apache-tomcat-7.0.79/webapps/tspace/upload/tmp" + tmpc + "/");
				if (!repository.exists()) {
					repository.mkdirs();
				}
				// System.out.println(repository);
				DiskFileItemFactory factory = new DiskFileItemFactory();
								
				// 设置内存缓冲区的大小，默认值为10K。当上传文件大于缓冲区大小时，fileupload组件将使用临时文件缓存上传文件。
				// 设置缓存的临界线为4KB
				factory.setSizeThreshold(4096);
				factory.setRepository(repository);
				// 删除缓存文件
				// 仅当Tomcat正常关闭时才会清楚缓存临时文件
				/*FileCleaningTracker fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(this.getServletContext());
				factory.setFileCleaningTracker(fileCleaningTracker);*/
								
				ServletFileUpload upload = new ServletFileUpload(factory);
				
				// 上传的所有文件之和的最大值4MB
				upload.setSizeMax(4194304);
								
				List<FileItem> items = upload.parseRequest(request);
				// System.out.println(items.size());
				Iterator<FileItem> i = items.iterator();
				Room room = new Room();
				int filecount = 0;
				while (i.hasNext()) {
					FileItem fi = (FileItem) i.next();
					if (fi.isFormField()) {
						// System.out.println(fi.getFieldName() + "," + fi.getString("UTF-8"));
						String fieldname = fi.getFieldName();
						String fieldcontent = fi.getString("UTF-8");
						if (fieldcontent == null || "".equals(fieldcontent)) {
							room.setschoolroom(0);
							break;
						}
						if (fieldname.equals("school")) {
							Integer school = new Integer(fieldcontent);
							if (school <= 0 || school == null) {
								room.setschoolroom(0);
								break;
							}
							room.setschoolroom(school);
						}
						if (fieldname.equals("roomid")) {
							room.setidroom(new Integer(fieldcontent));
						}
						if (fieldname.equals("roomname")) {		
							room.setnameroom(fieldcontent);
						}
						if (fieldname.equals("roomaddress")) {							
							room.setaddressroom(fieldcontent);							
						}
						if (fieldname.equals("roomcapacity")) {
							room.setcapacityroom(new Integer(fieldcontent));							
						}
						if (fieldname.equals("roomtype")) {
							if (fieldcontent.equals("0")) {
								room.settyperoom(null);
								break;
							}
							room.settyperoom(fieldcontent);
						}
						if (fieldname.equals("roomdescription")) {							
							room.setdescriptionroom(fieldcontent);
						}
						continue;
					}
					String filename = fi.getName();
					// System.out.println(filename+".");
					// 防止出现多个文件上传，恶意上传
					if (filecount > 0) {
						room.setnameroom(null);
						break;
					}
					if (filename != null && !filename.equals("")) {
						filecount ++;
						filename = room.getnameroom() + ".jpg";
						// System.out.println(filename);
						savedFile = new File(realpath+"upload/picture/", filename);
						// savedFile = new File("C:/Program Files/server/apache-tomcat-7.0.79/webapps/tspace/upload/picture/", filename);
						if (!savedFile.exists()) {
							savedFile.getParentFile().mkdir();
							savedFile.createNewFile();
						}
						/*System.out.println(savedFile);
						System.out.println(savedFile.exists());*/
						room.setpictureroom("https://www.tspace.top/tspace/upload/picture/" + filename);
						// room.setpictureroom("http://123.56.2.41/tspace/upload/picture/" + filename);
						fi.write(savedFile);
					} else {
						room.setnameroom(null);
						break;
					}
				}
				// 检查上传参数是否齐全
				if (room.getnameroom() == null || room.getschoolroom() == 0 
						|| room.getaddressroom() == null || room.getcapacityroom() == 0 
						|| room.getdescriptionroom() == null || room.gettyperoom() == null
						|| room.getpictureroom() == null) {
					// 删除缓存文件
					deleteDir(repository);
					// 上传参数不全，删除图片
					if (savedFile != null) {
						if (savedFile.exists() && savedFile.isFile()) {
							savedFile.delete();
						}
					}
					pw.print(-5);
					pw.flush();
					pw.close();
					return ;
				}
				int result = databaseroom.insert(room);
				// 删除缓存文件
				deleteDir(repository);
				if (result == 0) {
					// System.out.println("upload succeed");
					// 上传成功
					pw.print(0);
					pw.flush();
					pw.close();
					return ;
				} else {
					// 数据库添加失败，删除上传文件
					// System.out.println(result);
					if (savedFile != null) {
						if (savedFile.exists() && savedFile.isFile()) {
							savedFile.delete();
						}
					}
					// 数据库添加失败
					pw.print(-2);
					pw.flush();
					pw.close();
					return ;
				}
			} catch (Exception e) {
				TimerTask.logger.error(e.toString());
			}
			return ;
		}
		
		
		// 更新房间
		if ("updateroom".equals(op)) {
			String adminid = (String) session.getAttribute("adminid"); 
			PrintWriter pw = response.getWriter();
			if (adminid == null) {
				// -1：管理员未登录
				pw.print(-1);
				pw.flush();
				pw.close();
				return ;
			}
			File repository = null;
			File savedFile = null;
			try {
				// int tmpc = ++TimerTask.tmpcount;
				// 初始化文件上传工具
				repository = new File(realpath+"upload/tmp/");
				// File repository = new File("C:/Program Files/server/apache-tomcat-7.0.79/webapps/tspace/upload/tmp" + tmpc + "/");
				if (!repository.exists()) {
					repository.mkdirs();
				}
				// System.out.println(repository);
				DiskFileItemFactory factory = new DiskFileItemFactory();
								
				// 设置内存缓冲区的大小，默认值为10K。当上传文件大于缓冲区大小时，fileupload组件将使用临时文件缓存上传文件。
				// 设置缓存的临界线为4KB
				factory.setSizeThreshold(4096);
				factory.setRepository(repository);
				// 删除缓存文件
				// 仅当Tomcat正常关闭时才会清楚缓存临时文件
				/*FileCleaningTracker fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(this.getServletContext());
				factory.setFileCleaningTracker(fileCleaningTracker);*/
								
				ServletFileUpload upload = new ServletFileUpload(factory);
				
				// 上传的所有文件之和的最大值4MB
				upload.setSizeMax(4194304);
								
				List<FileItem> items = upload.parseRequest(request);
				// System.out.println(items.size());
				Iterator<FileItem> i = items.iterator();
				Room room = new Room();
				int filecount = 0;
				while (i.hasNext()) {
					FileItem fi = (FileItem) i.next();
					if (fi.isFormField()) {
						// System.out.println(fi.getFieldName() + "," + fi.getString("UTF-8"));
						String fieldname = fi.getFieldName();
						String fieldcontent = fi.getString("UTF-8");
						if (fieldcontent == null || "".equals(fieldcontent)) {
							room.setschoolroom(0);
							break;
						}
						if (fieldname.equals("school")) {
							Integer school = new Integer(fieldcontent);
							if (school <= 0 || school == null) {
								room.setschoolroom(0);
								break;
							}
							room.setschoolroom(school);
						}
						if (fieldname.equals("roomid")) {
							room.setidroom(new Integer(fieldcontent));
						}
						if (fieldname.equals("roomname")) {		
							room.setnameroom(fieldcontent);
						}
						if (fieldname.equals("roomaddress")) {							
							room.setaddressroom(fieldcontent);							
						}
						if (fieldname.equals("roomcapacity")) {
							room.setcapacityroom(new Integer(fieldcontent));							
						}
						if (fieldname.equals("roomtype")) {
							if (fieldcontent.equals("0")) {
								room.settyperoom(null);
								break;
							}
							room.settyperoom(fieldcontent);
						}
						if (fieldname.equals("roomdescription")) {							
							room.setdescriptionroom(fieldcontent);
						}
						continue;
					}
					String filename = fi.getName();
					// System.out.println(filename+".");
					// 防止出现多个文件上传，恶意上传
					if (filecount > 0) {
						room.setnameroom(null);
						break;
					}
					// System.out.println(filename);
					// 如果有文件上传，就接收
					if (filename != null && !filename.equals("")) {
						filecount ++;
						filename = room.getnameroom() + "new.jpg";
						// System.out.println(filename);
						savedFile = new File(realpath+"upload/picture/", filename);
						// savedFile = new File("C:/Program Files/server/apache-tomcat-7.0.79/webapps/tspace/upload/picture/", filename);
						if (!savedFile.exists()) {
							savedFile.getParentFile().mkdir();
							savedFile.createNewFile();
						}
						/*System.out.println(savedFile);
						System.out.println(savedFile.exists());*/
						// room.setpictureroom("http://localhost:8080/tspace/upload/picture/" + room.getnameroom()+".jpg");
						room.setpictureroom("https://www.tspace.top/tspace/upload/picture/" + room.getnameroom()+".jpg");
						// room.setpictureroom("http://123.56.2.41/tspace/upload/picture/" + filename);
						fi.write(savedFile);
					}
				}
				// room.setpictureroom("http://localhost:8080/tspace/upload/picture/" + room.getnameroom()+".jpg");
				room.setpictureroom("https://www.tspace.top/tspace/upload/picture/" + room.getnameroom()+".jpg");
				// 检查上传参数是否齐全
				if (room.getnameroom() == null || room.getschoolroom() == 0 
						|| room.getaddressroom() == null || room.getcapacityroom() == 0 
						|| room.getdescriptionroom() == null || room.gettyperoom() == null) {
					// 删除缓存文件
					deleteDir(repository);
					// 上传参数不全，删除图片
					if (savedFile != null) {
						if (savedFile.exists() && savedFile.isFile()) {
							savedFile.delete();
						}
					}
					pw.print(-5);
					pw.flush();
					pw.close();
					return ;
				}
				// System.out.println(room.toString());
				// 获取原有房间信息
				Room oldroom = databaseroom.getroom(room.getidroom());
				int result = databaseroom.update(room);
				// System.out.println(result);
				// 删除缓存文件
				deleteDir(repository);
				if (result == 0) {
					// System.out.println("upload succeed");
					// 房间信息更新成功
					// 如果上传了新图片，删除原图片，并将新文件改名，去掉文件名中的"new"。
					// System.out.println("filecount="+result);
					if (filecount > 0) {
						File oldFile = new File(realpath+"upload/picture/", oldroom.getnameroom()+".jpg");
						if (oldFile.exists() && oldFile.isFile()) {
							oldFile.delete();
						}
						File tmpFile = new File(realpath+"upload/picture/" + room.getnameroom()+"new.jpg");
						File newFile = new File(realpath+"upload/picture/", room.getnameroom()+".jpg");
						if (tmpFile.exists()) {
							tmpFile.renameTo(newFile);
						}
					} else {
						// 没有上传新图片，如果房间名修改，将图片改名
						if (!room.getnameroom().equals(oldroom.getnameroom())) {
							File newFile = new File(realpath+"upload/picture/", room.getnameroom()+".jpg");
							File oldFile = new File(realpath+"upload/picture/", oldroom.getnameroom()+".jpg");
							// System.out.println(oldFile.getPath());
							if (oldFile.exists()) {
								// System.out.println("rename success");
								oldFile.renameTo(newFile);
							}/* else {
								System.out.println("rename fail");
							}*/
						}
					}
					pw.print(0);
					pw.flush();
					pw.close();
					return ;
				} else {
					// 数据库添加失败，删除上传文件
					// System.out.println(result);
					if (savedFile != null) {
						if (savedFile.exists() && savedFile.isFile()) {
							savedFile.delete();
						}
					}
					// 数据库添加失败
					pw.print(-2);
					pw.flush();
					pw.close();
					return ;
				}
			} catch (Exception e) {
				// 删除缓存文件
				if (repository != null) deleteDir(repository);
				// 上传参数不全，删除图片
				if (savedFile != null) {
					if (savedFile.exists() && savedFile.isFile()) {
						savedFile.delete();
					}
				}
				// e.printStackTrace();
				TimerTask.logger.error(e.toString());
			}
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
		} catch (Exception e) {
			TimerTask.logger.error(e.toString());
			PrintWriter pw = response.getWriter();
			// -5：提交数据格式错误
			pw.print(-5);
			pw.flush();
			pw.close();
			return ;
		}
		
		// 获取房间详细信息
		// json数据：获取成功
		// -2：系统异常
		// -3：房间不存在
		// -5：提交数据格式错误
		if ("detail".equals(op)) {
			PrintWriter pw = response.getWriter();
			int idroom = jsonobject.optInt("idroom", 0);
			if (idroom == 0) {
				// -5：提交数据格式错误
				pw.print(-5);
				pw.flush();
				pw.close();
				return ;
			}
			Room room = databaseroom.getroom(idroom);
			if (room == null) {
				// -2：系统异常
				pw.print(-2);
				pw.flush();
				pw.close();
				return ;
			}
			if (room.getidroom() == -1) {
				// -3：房间不存在
				pw.print(-3);
				pw.flush();
				pw.close();
				return ;
			}
			
			pw.print(room.toString());
			pw.flush();
			pw.close();
			return ;
			
		}
	}
	
	private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //删除目录中的子文件
            for (int i=0; i<children.length; i++) {
                boolean success = new File(dir, children[i]).delete();
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

}
