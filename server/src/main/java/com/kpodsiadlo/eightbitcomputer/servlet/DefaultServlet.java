package com.kpodsiadlo.eightbitcomputer.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/index.html")
public class DefaultServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger("DefaultServlet");

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("default.jsp");
        String name = "8bitComputerCookie";
        String value = UUID.randomUUID().toString();
        Cookie cookie = new Cookie(name, value);
        resp.addCookie(cookie);
        requestDispatcher.forward(req, resp);
    }
}
