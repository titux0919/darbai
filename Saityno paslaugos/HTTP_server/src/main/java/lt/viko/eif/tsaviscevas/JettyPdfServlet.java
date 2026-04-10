package lt.viko.eif.tsaviscevas;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class JettyPdfServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/pdf;charset=UTF-8");

        resp.addHeader("Content-Disposition","inline; filename="+"cities.pdf");
        ServletOutputStream out= resp.getOutputStream();

        List<City> cities=CityService.getCities();

        ByteArrayOutputStream baos = GeneratePdf.getPdfFile(cities);
        baos.writeTo(out);
    }
}