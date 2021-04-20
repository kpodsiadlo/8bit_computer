package com.kpodsiadlo.eightbitcomputer.servlet;

import com.kpodsiadlo.eightbitcomputer.config.WebsocketServerConfigurator;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@WebServlet("/index.html")
public class DefaultServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger("DefaultServlet");
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cookieName = "8bitComputerCookie";

        Optional<String> requestCookieValue = getCookieValue(req, cookieName);

        if (requestCookieValue.isPresent()) {
            req.setAttribute(cookieName, requestCookieValue.get());
        } else {
            req.setAttribute(cookieName, UUID.randomUUID().toString());
        }

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("default.jsp");
        requestDispatcher.forward(req, resp);
    }


    private Optional<String> getCookieValue(HttpServletRequest req, String cookieName) {

        List<Cookie> reqCookies;
        try {
            reqCookies = List.of(req.getCookies());
        } catch (NullPointerException e) {
            return Optional.empty();
        }
        Map<String, String> cookies = reqCookies.stream().
                collect(Collectors.toMap(Cookie::getName, Cookie::getValue));
        return Optional.ofNullable(cookies.get(cookieName));
    }
}
