package lt.viko.eif.tsaviscevas;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class JettyMainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out=resp.getWriter();
        out.println("<html><body>");
        out.println("<a href='/pdf/static'> Test pdf</a>");
        out.println("</body></html>");
    }
}
