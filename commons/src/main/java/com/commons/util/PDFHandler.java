package com.commons.util;

import com.commons.response.InvoiceData;
import com.commons.response.InvoiceItemData;
import org.apache.commons.io.FileUtils;
import org.apache.fop.apps.*;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class PDFHandler {

    private static final Logger logger = Logger.getLogger(PDFHandler.class);


    public static void generatePDF(InvoiceData invoiceData, String PDF_PATH, String INVOICE_TEMPLATE_XSL) throws ParserConfigurationException, IOException, FOPException, TransformerException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // root element
        Element root = document.createElement("invoice");
        document.appendChild(root);
        double totalAmount = 0.0;

        Element date = document.createElement("date");
        date.appendChild(document.createTextNode(invoiceData.getDate()));
        root.appendChild(date);

        Element time = document.createElement("time");
        time.appendChild(document.createTextNode(invoiceData.getTime()));
        root.appendChild(time);

        Element orderId = document.createElement("orderId");
        orderId.appendChild(document.createTextNode(invoiceData.getOrderId().toString()));
        root.appendChild(orderId);

        Element clientName = document.createElement("clientName");
        clientName.appendChild(document.createTextNode(invoiceData.getClientName()));
        root.appendChild(clientName);

        Element customerName = document.createElement("customerName");
        customerName.appendChild(document.createTextNode(invoiceData.getCustomerName()));
        root.appendChild(customerName);

        Element channelName = document.createElement("channelName");
        channelName.appendChild(document.createTextNode(invoiceData.getChannelName()));
        root.appendChild(channelName);

        Element invoiceType = document.createElement("invoiceType");
        invoiceType.appendChild(document.createTextNode(invoiceData.getInvoiceType()));
        root.appendChild(invoiceType);

        // Create elements from BillData list
        for (InvoiceItemData invoiceItemData : invoiceData.getInvoiceItemDataList()) {
            Element item = document.createElement("item");
            root.appendChild(item);
            Element id = document.createElement("id");
            id.appendChild(document.createTextNode(invoiceItemData.getId().toString()));
            item.appendChild(id);

            Element productName = document.createElement("productName");
            productName.appendChild(document.createTextNode(invoiceItemData.getProductName()));
            item.appendChild(productName);

            Element brandId = document.createElement("brandId");
            brandId.appendChild(document.createTextNode(invoiceItemData.getBrandId()));
            item.appendChild(brandId);

            Element orderedQuantity = document.createElement("orderedQuantity");
            orderedQuantity.appendChild(document.createTextNode(invoiceItemData.getOrderedQuantity().toString()));
            item.appendChild(orderedQuantity);

            Element sellingPricePerUnit = document.createElement("sellingPricePerUnit");
            sellingPricePerUnit.appendChild(document.createTextNode(String.valueOf(invoiceItemData.getSellingPricePerUnit())));
            item.appendChild(sellingPricePerUnit);
            // Calculate total bill amount
            totalAmount = totalAmount + invoiceItemData.getOrderedQuantity() * invoiceItemData.getSellingPricePerUnit();
        }

        Element total = document.createElement("totalAmount");
        total.appendChild(document.createTextNode(totalAmount + " Rs."));
        root.appendChild(total);
        logger.info("XML element created");

        File file = new File(PDF_PATH+"order"+invoiceData.getOrderId()+".pdf");

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);

        ByteArrayOutputStream xmlBaos = new ByteArrayOutputStream();
        StreamResult streamResult = new StreamResult(xmlBaos);
        transformer.transform(domSource, streamResult);
        logger.info("XML transformed");


        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        ByteArrayOutputStream pdfBaos = new ByteArrayOutputStream();
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, pdfBaos);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer2 = factory.newTransformer(new StreamSource(INVOICE_TEMPLATE_XSL));
        Result res = new SAXResult(fop.getDefaultHandler());
        logger.info("XML bytes : "+xmlBaos.toByteArray().length);
        Source src = new StreamSource(new ByteArrayInputStream(xmlBaos.toByteArray()));

        transformer2.transform(src, res);
        logger.info("PDF Bytes : " + pdfBaos.toByteArray().length);
        FileUtils.writeByteArrayToFile(file,pdfBaos.toByteArray());
//        FileOutputStream fos = new FileOutputStream(file);
//        BufferedOutputStream bos = new BufferedOutputStream(fos);
//        bos.write(pdfBaos.toByteArray());

    }

}
