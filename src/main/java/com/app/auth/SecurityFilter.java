package com.app.auth;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class SecurityFilter
 */
@WebFilter(urlPatterns = { "/*" }, dispatcherTypes = { DispatcherType.REQUEST,
		DispatcherType.INCLUDE })
public class SecurityFilter implements Filter {
	// private Logger l = LoggerFactory.getLogger(SecurityFilter.class);

	/**
	 * Default constructor.
	 */
	public SecurityFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest req = (HttpServletRequest) request;
		final String servletPath = req.getServletPath();

		// Allow access to login functionality.
		if (servletPath.equals("/LoginServlet")) {
			chain.doFilter(request, response);
			return;
		} else if (servletPath.equals("/")) {
			req.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}

		if (req.getSession().getAttribute("user") == null) {
			req.getRequestDispatcher("/login.jsp").forward(request, response);
		} else {
			// pass the request along the filter chain
			chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}
}
