/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.service;

import com.lisasoft.wfsvalidator.validator.ValidationError;
import md.maxcode.si.tools.UtilsComponent;
import md.maxcode.si.xsl.XSLErrorInfo;
import md.maxcode.si.xsl.XSLParseException;
import nu.xom.*;
import nu.xom.xslt.XSLException;
import nu.xom.xslt.XSLTransform;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ValidationService {
    private final Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    private UtilsComponent utilsComponent;

    public void againstXSD(String xsdPath_, String xmlPath_) throws IOException, SAXException {
        Source schemaFile = new StreamSource(new File(xsdPath_));
        Source xmlFile = new StreamSource(new File(xmlPath_));
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(schemaFile);
        Validator validator = schema.newValidator();

        validator.validate(xmlFile);
        logger.info(xmlFile.getSystemId() + " is valid");
    }

    public void againstSCH(String schPath_, String xmlPath_) throws IOException, SAXException, ValidationError {
        FileInputStream fileInputStream = new FileInputStream(new File(xmlPath_));
    }

    public List<XSLErrorInfo> againstXSL(String xslPath_, String xmlPath_) throws ParsingException, IOException, XSLException, XSLParseException {
        List<XSLErrorInfo> result = new ArrayList<>();
        Builder builder = new Builder();

        Document in;
        in = builder.build(new File(xmlPath_));

        Document stylesheet = builder.build(new File(xslPath_));

        XSLTransform transform = new XSLTransform(stylesheet);

        Nodes nodes = transform.transform(in);

        boolean valid = true;
        if (nodes.size() < 1) {
            throw new ParsingException("Nodes were not found");
        }

        int nr = nodes.size();
        for (int i = 0; i < nr; i++) {
            Elements elements = ((Element) nodes.get(i)).getChildElements();

            for (int j = 0; j < elements.size(); j++) {
                Element element = elements.get(j);

                if (element.getLocalName().equals("failed-assert")) {
                    final XSLErrorInfo xslError = new XSLErrorInfo();
                    xslError.setXSLFileName(FilenameUtils.getName(xslPath_));
                    xslError.setXMLFileName(FilenameUtils.getName(xmlPath_));
                    xslError.setTest(element.getAttributeValue("test"));
                    xslError.setFlag(element.getAttributeValue("flag"));
                    xslError.setLocation(element.getAttributeValue("location"));
                    xslError.setContent(element.getValue());

                    if (xslError.isFatal()) {
                        valid = false;
                    }

                    result.add(xslError);

                    logger.warning(xslError.toString());
                }
            }
        }

        if (!valid) {
            throw new XSLParseException(result);
        }

        return result;
    }

}
