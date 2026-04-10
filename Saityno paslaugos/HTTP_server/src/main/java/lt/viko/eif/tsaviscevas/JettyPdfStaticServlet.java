package lt.viko.eif.tsaviscevas;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JettyPdfStaticServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pdfFileName="sample.pdf";
        File pdfFile=new File(pdfFileName);

        resp.setContentType("application/pdf");
        resp.addHeader("Content-Disposition","inline; filename="+pdfFileName);
        resp.setContentLength((int) pdfFile.length());

        FileInputStream fileInputStream=new FileInputStream(pdfFileName);
        OutputStream responseOutputStream = resp.getOutputStream();
        int bytes;
        while ((bytes=fileInputStream.read())!=-1){
            responseOutputStream.write(bytes);
        }
    }
}
