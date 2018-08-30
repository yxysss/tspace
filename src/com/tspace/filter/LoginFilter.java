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
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/LoginFilter")
public class LoginFilter implements Filter {

	
	String stopUrl;
	
    /**
     * Default constructor. 
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		
		// pass the request along the filter chain
		// chain.doFilter(request, response);
		// System.out.println("LoginFilter : " + httpRequest.getRequestURL());
		// System.out.println("stopUrl : " + stopUrl);
		HttpSession session = httpRequest.getSession();
		String username = (String)session.getAttribute("username");
		
		String[] strArray = stopUrl.split(";");
		for (String str : strArray) {
			if (str.equals("")) continue;
			// System.out.println("str:" + str);
			if (httpRequest.getRequestURL().indexOf(str) >= 0) {
				// pass the request along the filter chain
				// System.out.println("1");
				if (username != null) {
					// pass the request along the filter chain
					// System.out.println(username);
					chain.doFilter(request, response);
				} else {
					httpResponse.sendRedirect(httpRequest.getContextPath()+"/User-mobile/login.jsp");
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
		stopUrl = fConfig.getInitParameter("stopUrl");
	}

}
