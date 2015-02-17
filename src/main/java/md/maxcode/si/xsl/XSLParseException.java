/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.xsl;

import java.util.List;

public class XSLParseException extends Exception {

    private List<XSLErrorInfo> errors;

    public XSLParseException(List<XSLErrorInfo> errors) {
        super();

        this.errors = errors;
    }

    public List<XSLErrorInfo> getErrors() {
        return errors;
    }

    @Override
    public String getMessage() {
        StringBuilder s = new StringBuilder();
        for (XSLErrorInfo xslErrorInfo : errors) {
            s.append(xslErrorInfo.toMessage()).append("\n\r");
        }

        return s.toString();
    }

    @Override
    public String getLocalizedMessage() {
        return super.getMessage();
    }
}
