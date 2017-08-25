/**
 * Copyright (c) 2006-2014, huntxt 张建峰( huntxt@163.com). Licensed under the
 * Apache License, Version 2.0 (the "License");
 */

package org.dubbo.utils;


public class HttpReturn {
    //private Logger       logger = LoggerFactory.getLogger(HttpReturn.class);
    private String       url;
    private String       content;
    private String       cookie;
    
    public HttpReturn() {
       // logger.debug(HttpReturn.class.getName());
    }
    /**
     * @return the cookie
     */
    public String getCookie() {
        return cookie;
    }

    /**
     * @param cookie
     *            the cookie to set
     */
    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }
    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }
    /**
     * @param content
     *            the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
}
