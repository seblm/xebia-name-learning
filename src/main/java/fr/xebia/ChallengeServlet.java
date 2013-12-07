package fr.xebia;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

@WebServlet(name = "challenge", value = "/challenge")
public class ChallengeServlet extends HttpServlet {
    private Challenge challenge;

    @Override
    public void init() throws ServletException {
        challenge = new Challenge(getServletContext().getResourcePaths("/images"), getQuestions());
    }

    private Questions getQuestions() {
        String questionsParameter = getServletConfig().getInitParameter("questions");
        if (questionsParameter != null) {
            return new PredictableQuestions(questionsParameter);
        }
        return new RandomQuestions(new Random());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("{");
            out.format("    \"firstImage\": \"%s\",%n", challenge.getFirstImage());
            out.format("    \"secondImage\": \"%s\",%n", challenge.getSecondImage());
            out.format("    \"name\": \"%s\",%n", challenge.getName());
            out.format("    \"answer\": \"%s\"%n", challenge.getAnswer());
            out.println("}");
        }

        challenge.next();
    }
}
