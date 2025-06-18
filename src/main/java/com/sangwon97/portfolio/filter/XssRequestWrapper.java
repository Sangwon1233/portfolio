package com.sangwon97.portfolio.filter;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class XssRequestWrapper extends HttpServletRequestWrapper {

    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (value != null) {
            return Jsoup.clean(value, Safelist.basicWithImages());
        }
        return null;
    }
}

