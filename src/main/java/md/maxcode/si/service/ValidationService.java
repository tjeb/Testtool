/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.service;

import com.lisasoft.wfsvalidator.validator.ValidationError;
import md.maxcode.si.tools.ActorID;
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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Service
public class ValidationService
{
    @Autowired
    private UtilsComponent utilsComponent;
    private XPathContext xpc;
    private static final String nsUri = "http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader";
    private static final String invUri = "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2";
    private static final String cacUri = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2";
    private static final String cbcUri = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2";

    public ValidationService() {
        xpc = new XPathContext("sbdh", nsUri);
        xpc.addNamespace("inv", invUri);
        xpc.addNamespace("cac", cacUri);
        xpc.addNamespace("cbc", cbcUri);
    }

    public class ReceivedDocument
    {
        private Document doc;
        private ActorID receiver;

        public ReceivedDocument(String xmlPath_) throws IOException, ParsingException {
            Builder builder = new Builder();
            doc = builder.build(xmlPath_);
            receiver = null;

            Element root = doc.getRootElement();
            ActorID receiverActorID = null;
            if (root.getLocalName() == "StandardBusinessDocument") {
                // take out the invoice, and replace the document with it
                Elements children = root.getChildElements();
                for (int i = 0; i < children.size(); i++) {
                    Element wrappedElement = children.get(i);
                    if (wrappedElement.getLocalName() == "StandardBusinessDocumentHeader") {
                        // If there is a Recipitent here, we want to have one additional
                        // check, apart from the actual Schematron checks (which are only
                        // on the invoice); the recipient must match either
                        Nodes receiverNodes = wrappedElement.query("./sbdh:Receiver/sbdh:Identifier", xpc);
                        if (receiverNodes.size() > 0) {
                            Element receiverElement = (Element)receiverNodes.get(0);
                            String authority = receiverElement.getAttributeValue("Authority", nsUri);
                            receiver = new ActorID(receiverElement.getAttributeValue("Authority"),
                                    receiverElement.getValue());
                        }
                    } else {
                        doc = new Document((Element)wrappedElement.copy());
                    }
                }
            }
        }

        public boolean wasSBDH() {
            return receiver != null;
        }

        public Document getDocument() {
            return doc;
        }

        public ActorID getReceiver() {
            return receiver;
        }

    }

    private final Logger logger = Logger.getLogger(getClass().getName());

    public void againstXSD(String xsdPath_, String xmlPath_) throws IOException, SAXException, ParsingException
    {
        Source schemaFile = new StreamSource(new File(xsdPath_));
        Source xmlFile = new StreamSource(new File(xmlPath_));
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(schemaFile);

        Document in = new ReceivedDocument(xmlPath_).getDocument();

        Validator validator = schema.newValidator();

        // there does not appear to be a direct way to pass a xom Document
        // to the validator
        ByteArrayInputStream instream = new ByteArrayInputStream(in.toXML().getBytes());
        validator.validate(new StreamSource(instream));
        logger.info(xmlFile.getSystemId() + " is valid");
    }

    private ActorID getActorIDFromInvoice(Document in, String path) {
        ActorID actor = null;
        Nodes nodes = in.query(path, xpc);
        int i;
        for (i = 0; i < nodes.size(); i++) {
            Element el = (Element)nodes.get(i);
            actor = new ActorID(el.getAttributeValue("schemeID"),
                                el.getValue());
        }
        return actor;
     }
    public void againstSCH(String schPath_, String xmlPath_) throws IOException, SAXException, ValidationError
{
        FileInputStream fileInputStream = new FileInputStream(new File(xmlPath_));
}

public List<XSLErrorInfo> againstXSL(String xslPath_, String xmlPath_) throws ParsingException, IOException, XSLException, XSLParseException
    {
        List<XSLErrorInfo> result = new ArrayList<>();
Builder builder = new Builder();

  // If the file was sent over AS2, it is wrapped in a SBDH envelope
        // Oxalis does not remove this, unfortunately, so we'll
        // have to do it ourselves (or shove it up even one layer more...)
        // For some discussion, see https://github.com/difi/oxalis/pull/241
        System.err.println("[XX] xml path: " + xmlPath_);


        ReceivedDocument doc = new ReceivedDocument(xmlPath_);

        // Additional check, see above
        if (doc.wasSBDH()) {
            // Should match one of these
            ActorID customerActorID = getActorIDFromInvoice(doc.getDocument(), "/inv:Invoice/cac:AccountingCustomerParty/cac:Party/cac:PartyLegalEntity/cbc:CompanyID");
            if (customerActorID == null) {
                customerActorID = getActorIDFromInvoice(doc.getDocument(), "/inv:Invoice/cac:AccountingCustomerParty/cac:Party/cbc:EndpointID");
            }
            if (customerActorID != null && !customerActorID.equals(doc.getReceiver())) {
                final XSLErrorInfo xslError = new XSLErrorInfo();
                xslError.setXSLFileName(FilenameUtils.getName(xslPath_));
                xslError.setXMLFileName(FilenameUtils.getName(xmlPath_));
                xslError.setTest("SI-INV-AS2-RECEIVER");
                xslError.setFlag("warning");
                xslError.setLocation("");
                xslError.setContent("AS2 Receiver identity does not match AccountingCustomerParty");
                result.add(xslError);
            }
        }

        Document stylesheet = builder.build(new File(xslPath_));

        XSLTransform transform = new XSLTransform(stylesheet);

        Nodes nodes = transform.transform(doc.getDocument());

boolean valid = true;
if(nodes.size() < 1)
{
throw new ParsingException("Nodes were not found");
}

int nr = nodes.size();
for(int i = 0; i < nr; i++)
{
Elements elements = ((Element)nodes.get(i)).getChildElements();

for(int j=0; j<elements.size(); j++)
{
Element element = elements.get(j);

if(element.getLocalName().equals("failed-assert"))
{
                    final XSLErrorInfo xslError = new XSLErrorInfo();
                    xslError.setXSLFileName(FilenameUtils.getName(xslPath_));
                    xslError.setXMLFileName(FilenameUtils.getName(xmlPath_));
                    xslError.setTest(element.getAttributeValue("test"));
                    xslError.setFlag(element.getAttributeValue("flag"));
                    xslError.setLocation(element.getAttributeValue("location"));
                    xslError.setContent(element.getValue());

                    if(xslError.isFatal())
                    {
                        valid = false;
                    }

                    result.add(xslError);

                    logger.warning(xslError.toString());
}
}

}

if(!valid)
{
throw new XSLParseException(result);
}

        return result;
}

}
