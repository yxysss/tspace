package com.tspace.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class adminfilter
 */
@WebFilter("/adminfilter")
public class AdminFilter implements Filter {

    /**
     * Default constructor. 
     */
    public AdminFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	String allowUrl;
	
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain

		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		
		// pass the request along the filter chain
		// chain.doFilter(request, response);
		// System.out.println("AdminLoginFilter : " + httpRequest.getRequestURL());
		// System.out.println("allowUrl : " + allowUrl);
		HttpSession session = httpRequest.getSession();
		String adminid = (String)session.getAttribute("adminid");
		
		String[] strArray = allowUrl.split(";");
		for (String str : strArray) {
			if (str.equals("")) continue;
			// System.out.println("str:" + str);
			if (httpRequest.getRequestURL().indexOf(str) <= 0) {
				// pass the request along the filter chain
				// System.out.println("1");
				if (adminid == null) {
					// pass the request along the filter chain
					httpResponse.sendRedirect(httpRequest.getContextPath()+"/Administrator/adminlogin.jsp");
				} else {
					// System.out.println(adminid);
					chain.doFilter(request, response);
				}
				return ;
			}
		} 
		
		chain.doFilter(request, response);
		return ;
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		allowUrl = fConfig.getInitParameter("allowUrl");
	}

}
