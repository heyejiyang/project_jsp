package org.choongang.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Interceptor {
    boolean preHandle();
}
