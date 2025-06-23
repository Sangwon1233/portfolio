package com.sangwon97.portfolio.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class XssRequestWrapper extends HttpServletRequestWrapper {

    private static final Safelist safelist = createSafeList();

    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (value != null) {
            return Jsoup.clean(value, safelist);
        }
        return null;
    }

    private static Safelist createSafeList() {
        return Safelist.relaxed()
                .addAttributes("a", "target", "rel")  // a 태그 target, rel 속성 허용
                .addProtocols("a", "href", "http", "https")
                .addProtocols("img", "src", "http", "https", "data");
    }
}
