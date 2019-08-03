package com.lthaoshao.pattern.delegate.mvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>  </p>
 *
 * @author lthaoshao
 * @version V1.0
 * @date 2019/8/3 15:30
 */
public class DispatcherServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doDispatcher(req, res);
    }

    private void doDispatcher(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String uri = req.getRequestURI();
        String mid = req.getParameter("mid");
        if (null != uri) {
            uri = uri.substring(uri.indexOf("/")+1, uri.lastIndexOf("."));
        }

        if ("getOrderByMid".equalsIgnoreCase(uri)) {
            new OrderController().getOrderByMid(mid);
        } else if ("getMemberByMid".equalsIgnoreCase(uri)) {
            new MemberController().getMemberByMid(mid);
        } else if ("logout".equalsIgnoreCase(uri)) {
            new SystemController().logout();
        } else {
            res.getWriter().write("404 Not found");
        }
        res.getWriter().write(uri +" execute success");
    }
}
