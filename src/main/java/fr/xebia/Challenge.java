package fr.xebia;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/challenge")
public class Challenge extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter out = resp.getWriter()) {
            out.println("{\n" +
                    "    \"first\": \"Sebastian%20Le%20Merdy.jpg\",\n" +
                    "    \"second\": \"Florent%20Le%20Gall.jpg\",\n" +
                    "    \"name\": \"Florent Le Gall\",\n" +
                    "    \"answer\": \"second\"\n" +
                    "}");
            resp.setContentType("application/json");
        }
    }
}
