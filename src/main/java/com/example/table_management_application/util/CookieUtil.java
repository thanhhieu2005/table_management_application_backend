package com.example.table_management_application.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
  
  public static void addJwtCookie(HttpServletResponse response, String token) {
    Cookie cookie = new Cookie("access_token", token);

    cookie.setHttpOnly(true);
    cookie.setSecure(false);
    cookie.setPath("/");
    cookie.setMaxAge(60 * 60 * 24 * 14);

    response.addCookie(cookie);
  }
  
  public static void clearJwtCookie(HttpServletResponse response) {
    Cookie cookie = new Cookie("access_token", null);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(0);

    response.addCookie(cookie);
  }
}
