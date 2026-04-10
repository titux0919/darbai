package lt.viko.eif.tsaviscevas;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class JettyHTMLServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out=resp.getWriter();
        out.println("<html><body>");
        out.println("<h1> Hello</h1>");
        out.println("<img src=\"/images/lithuania.jpg\" "+
                "alt=\"Girl in a jacket\" width=\"800\"height=\"600\">");
        out.println("</body></html>");
    }
}