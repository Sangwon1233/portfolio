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
                .addTags("img")  // img 태그 자체를 허용
                .addAttributes("img", "src", "alt", "title", "width", "height", "style")
                .addAttributes("a", "target", "rel")
                .addProtocols("a", "href", "http", "https")
                .addProtocols("img", "src", "http", "https", "data");  // 이미지 업로드 대응 (data URL 포함)
    }
}
