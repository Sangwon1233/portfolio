package com.sangwon97.portfolio.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoLinkUtil {

    private static final Pattern URL_PATTERN = Pattern.compile(
            "(https?://[\\w\\-._~:/?#[\\]@!$&'()*+,;=%]+)|(www\\.[\\w\\-._~:/?#[\\]@!$&'()*+,;=%]+)",
            Pattern.CASE_INSENSITIVE);

    public static String convertUrlsToLinks(String text) {
        Matcher matcher = URL_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String url = matcher.group();
            String href = url.startsWith("http") ? url : "http://" + url;
            String linkTag = String.format("<a href=\"%s\" target=\"_blank\" rel=\"noopener noreferrer\">%s</a>", href, url);
            matcher.appendReplacement(sb, linkTag);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
