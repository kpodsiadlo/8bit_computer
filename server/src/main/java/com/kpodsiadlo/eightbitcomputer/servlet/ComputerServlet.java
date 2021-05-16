package com.kpodsiadlo.eightbitcomputer.servlet;

import com.kpodsiadlo.eightbitcomputer.authentication.VerifyRecaptcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/computer")
public class ComputerServlet extends HttpServlet {
    private static final  Logger logger = LoggerFactory.getLogger("DefaultServlet");

    @Override
    public void doPost(HttpServletRequest req,
    HttpServletResponse resp) throws ServletException, IOException {
        String gRecaptchaResponse = req.getParameter("g-recaptcha-response");
        boolean isHuman = VerifyRecaptcha.verify(gRecaptchaResponse);
        if (isHuman) {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("computer.jsp");
            requestDispatcher.forward(req, resp);
            logger.info("Recaptcha succesful");
        }
        else {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/index.html");
            requestDispatcher.forward(req, resp);
            logger.info("Recaptcha failed");

        }
    }
}
