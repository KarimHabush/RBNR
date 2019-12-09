/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rbnr.api;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "SharingFilter", urlPatterns = {"/*"})
public class SharingFilter implements Filter {

    private final static Logger log = Logger.getLogger(SharingFilter.class.getName() );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
          log.info("Adding headers");
          HttpServletResponse http = (HttpServletResponse) response;
          http.addHeader("Access-Control-Allow-Origin", "*");
          http.addHeader("Access-Control-Allow-Credentials", "true");
          http.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, HEAD, OPTIONS");
          http.addHeader("Access-Control-Allow-Headers", "SOAPAction, Content-Typedr");

        } 
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}