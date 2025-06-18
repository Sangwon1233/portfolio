package com.sangwon97.portfolio.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class XssFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if ("POST".equalsIgnoreCase(request.getMethod()) && isTargetUri(request)) {
            XssRequestWrapper wrappedRequest = new XssRequestWrapper(request);
            filterChain.doFilter((ServletRequest) wrappedRequest, (ServletResponse) response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private boolean isTargetUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/board/write") || uri.startsWith("/board/modify");
    }
}

