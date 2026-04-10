package lt.viko.eif.tsaviscevas;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class JettyHTMLServletForms extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body><form method='post' action=''>");
        out.println("<label for=\"fname\">First name:</label><br>");
        out.println("<input type=\"text\" id=\"fname\" name=\"fname\"><br>");
        out.println("<label for=\"lname\">Last name:</label><br><br>");
        out.println("<input type=\"text\" id=\"lname\" name=\"lname\">");
        out.println(" <input type=\"submit\" value=\"Submit\">");
        out.println("</form></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        String fname = req.getParameter("fname");
        String lname = req.getParameter("lname");

        out.println("<html><body>");
        out.println("<h1>Data:</h1>");
        out.println("<p>First name: " + fname + "</p>");
        out.println("<p>Last name: " + lname + "</p>");
        out.println("</body></html>");
    }
}