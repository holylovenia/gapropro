package com.shampoo.webapp.controller;

import javax.servlet.http.HttpServletRequest;

public class CookieHandler {

    public CookieHandler() {

    }

    public String getAccessTokenCookie(HttpServletRequest request) {
        if (request.getCookies() != null)  {
            for (int i = 0; i < request.getCookies().length; i++) {
                javax.servlet.http.Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (javax.servlet.http.Cookie cookie : cookies) {
                        if (cookie.getName().equals("access_token")) {
                            System.out.println(cookie.getValue());
                            return cookie.getValue();
                        }
                    }
                }
            }
        }
        return null;
    }

    public boolean isTextValid(String s) {
        return s.matches("^[a-zA-Z0-9@. ]*$");
    }
}
