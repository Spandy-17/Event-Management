//package com.eventclub.backend.Controller;
//
//import org.springframework.web.bind.annotation.*;
//import org.springframework.http.*;
//
//import java.io.ByteArrayOutputStream;
//
//@RestController
//@RequestMapping("/api/certificate")
//@CrossOrigin(origins = "*")
//public class CertificateController {
//
//    @GetMapping("/download")
//    public ResponseEntity<byte[]> downloadCertificate(
//            @RequestParam String username,
//            @RequestParam String eventName) throws Exception {
//
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        String content = "Certificate of Participation\n\n" +
//                "This is to certify that " + username +
//                " has participated in " + eventName;
//
//        out.write(content.getBytes());
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION,
//                        "attachment; filename=certificate.txt")
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(out.toByteArray());
//    }
//}
//package com.eventclub.backend.Controller;
//
//import org.springframework.web.bind.annotation.*;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.Paragraph;
//import com.itextpdf.kernel.colors.ColorConstants;
//
//import java.io.ByteArrayOutputStream;
//
//@RestController
//@RequestMapping("/api/certificate")
//@CrossOrigin(origins = "*")
//public class CertificateController {
//
//    @GetMapping("/download")
//    public ResponseEntity<byte[]> generateCertificate(
//            @RequestParam String username,
//            @RequestParam String eventName) throws Exception {
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//        PdfWriter writer = new PdfWriter(baos);
//        PdfDocument pdf = new PdfDocument(writer);
//        Document document = new Document(pdf);
//
//        // Title
//        document.add(new Paragraph("CERTIFICATE OF PARTICIPATION")
//                .setFontSize(24)
//                .setBold()
//                .setFontColor(ColorConstants.BLUE));
//
//        document.add(new Paragraph("\n"));
//
//        // Content
//        document.add(new Paragraph("This is to certify that").setFontSize(14));
//
//        document.add(new Paragraph(username)
//                .setFontSize(20)
//                .setBold());
//
//        document.add(new Paragraph(
//                "has successfully participated in \"" + eventName + "\" event.")
//                .setFontSize(14));
//
//        document.close();
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION,
//                        "attachment; filename=certificate.pdf")
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(baos.toByteArray());
//    }
//}
//package com.eventclub.backend.Controller;
//
//import org.springframework.web.bind.annotation.*;
//import org.springframework.http.*;
//
//import com.itextpdf.kernel.pdf.*;
//import com.itextpdf.layout.*;
//import com.itextpdf.layout.element.*;
//import com.itextpdf.kernel.colors.ColorConstants;
//import com.itextpdf.kernel.font.PdfFontFactory;
//
//import java.io.ByteArrayOutputStream;
//
//@RestController
//@RequestMapping("/api/certificate")
//@CrossOrigin(origins = "*")
//public class CertificateController {
//
//    @GetMapping("/download")
//    public ResponseEntity<byte[]> generateCertificate(
//            @RequestParam String username,
//            @RequestParam String eventName) throws Exception {
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//        PdfWriter writer = new PdfWriter(baos);
//        PdfDocument pdf = new PdfDocument(writer);
//        Document document = new Document(pdf);
//
//        // 🎨 Title
//        Paragraph title = new Paragraph("CERTIFICATE OF PARTICIPATION")
//                .setFontSize(24)
//                .setBold()
//                .setFontColor(ColorConstants.BLUE);
//
//        // 👤 Name
//        Paragraph name = new Paragraph(username)
//                .setFontSize(20)
//                .setBold();
//
//        // 📄 Content
//        Paragraph content = new Paragraph(
//                "This is to certify that")
//                .setFontSize(14);
//
//        Paragraph eventText = new Paragraph(
//                "has successfully participated in \"" + eventName + "\" event.")
//                .setFontSize(14);
//
//        document.add(title);
//        document.add(new Paragraph("\n"));
//        document.add(content);
//        document.add(name);
//        document.add(eventText);
//
//        document.close();
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION,
//                        "attachment; filename=certificate.pdf")
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(baos.toByteArray());
//    }
//}
package com.eventclub.backend.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@RestController
@RequestMapping("/api/certificate")
@CrossOrigin(origins = "*")
public class CertificateController {

    @GetMapping("/download")
    public ResponseEntity<byte[]> generateCertificate(
            @RequestParam String username,
            @RequestParam String eventName) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // ✅ LOAD CANVA IMAGE FROM resources/static
        InputStream is = getClass().getResourceAsStream("/static/certificate.png");

        if (is == null) {
            throw new RuntimeException("certificate.png NOT FOUND in resources/static");
        }

        byte[] imageBytes = is.readAllBytes();

        Image bg = new Image(ImageDataFactory.create(imageBytes));
        bg.setFixedPosition(0, 0);
        bg.scaleToFit(595, 842); // A4 size

        document.add(bg);

        // 👤 USERNAME (position adjust using marginTop)
        Paragraph name = new Paragraph(username)
                .setFontSize(28)
                .setBold()
                .setMarginTop(350);

        // 📄 EVENT NAME
        Paragraph event = new Paragraph("For participating in " + eventName)
                .setFontSize(18);

        document.add(name);
        document.add(event);

        document.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=certificate.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(baos.toByteArray());
    }
}