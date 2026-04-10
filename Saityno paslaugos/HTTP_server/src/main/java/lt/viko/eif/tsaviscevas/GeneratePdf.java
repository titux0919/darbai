package lt.viko.eif.tsaviscevas;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneratePdf {
    public static ByteArrayOutputStream getPdfFile(List<City> cities){
        var document=new Document();
        var bout=new ByteArrayOutputStream();

        try {
            var table = new PdfPTable(3);
            table.setWidthPercentage(60);
            table.setWidths(new int[]{1,3,3});

            Font headFont= FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            var hcell = new PdfPCell(new Phrase("Id",headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Name",headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Population",headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            for (City city: cities){
                var cell=new PdfPCell(new Phrase(city.getId().toString()));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell= new PdfPCell(new Phrase(String.valueOf(city.getName())));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell= new PdfPCell(new Phrase(String.valueOf(city.getPopulation())));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setPaddingRight(5);
                table.addCell(cell);
            }

            PdfWriter.getInstance(document,bout);
            document.open();
            document.add(table);
            document.close();
        }catch (DocumentException ex){
            Logger.getLogger(GeneratePdf.class.getName()).log(Level.SEVERE,null,ex);
        }
        return bout;
    }
}
