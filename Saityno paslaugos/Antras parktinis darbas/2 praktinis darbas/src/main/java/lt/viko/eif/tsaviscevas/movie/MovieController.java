package lt.viko.eif.tsaviscevas.movie;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletResponse;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.sax.SAXResult;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Controller
public class MovieController {

    // =====================
    // HTML (VIENINTELIS /movies)
    // =====================
    @GetMapping("/generate-html")
    public void generateHtml(HttpServletResponse response) throws Exception {

        response.setContentType("text/html;charset=UTF-8");

        var xml = new org.springframework.core.io.ClassPathResource("movies.xml");
        var xsl = new org.springframework.core.io.ClassPathResource("movies.xsl");

        Transformer transformer = TransformerFactory.newInstance()
                .newTransformer(new StreamSource(xsl.getInputStream()));

        transformer.transform(
                new StreamSource(xml.getInputStream()),
                new StreamResult(response.getWriter())
        );
    }

    // =====================
    // PDF
    // =====================
    @GetMapping("/generate-pdf")
    public String generatePDF() throws Exception {

        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

        OutputStream out = new FileOutputStream("src/main/resources/static/movies.pdf");

        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

        TransformerFactory factory = TransformerFactory.newInstance();

        Transformer transformer = factory.newTransformer(
                new StreamSource(getClass().getResourceAsStream("/movies-pdf.xsl"))
        );

        Source src = new StreamSource(getClass().getResourceAsStream("/movies.xml"));

        Result res = new SAXResult(fop.getDefaultHandler());

        transformer.transform(src, res);

        out.close();

        return "redirect:/movies.pdf";
    }
}