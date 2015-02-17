/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.tools;

import nu.xom.xslt.XSLException;
import org.apache.commons.io.FilenameUtils;
import org.xml.sax.SAXParseException;

public class Response {
    private final String _message;
    private final Boolean _error;
    private final Object _data;
    private boolean _warning;

    public Response(String message_, Boolean error_) {
        _message = message_;
        _error = error_;
        _data = null;
    }

    public Response(String message_, Boolean error_, Object data_) {
        _message = message_;
        _error = error_;
        _data = data_;
    }

    public Response(Throwable e) {
        StringBuilder cause = new StringBuilder();

        if (e.getClass() == SAXParseException.class) {
            String fileName = ((SAXParseException) e).getSystemId();
            cause.append("Error encountered in --> ")
                    .append(FilenameUtils.getName(fileName))
                    .append(" :: ")
                    .append("Line number: ")
                    .append(((SAXParseException) e).getLineNumber())
                    .append(" :: ")
                    .append("Column number: ")
                    .append(((SAXParseException) e).getColumnNumber());
        }

        if (e.getClass() == XSLException.class) {
            cause.append(e.getLocalizedMessage());
        }

        if (e.getCause() != null) {
            cause.append(" :: ")
                    .append(e.getCause().getMessage());
        } else if (e.getMessage() != null) {
            cause.append(" :: ")
                    .append(e.getMessage());
        }

        _message = cause.append(" :: ").append(e.getClass().getName()).toString();
        _error = true;
        _data = null;
    }

    public Response(Boolean error_, Object data_) {
        _message = "[DEFAULT MESSAGE]";
        _error = error_;
        _data = data_;
    }

    public Response(Boolean error_) {
        _message = "[DEFAULT MESSAGE]";
        _error = error_;
        _data = null;
    }

    public Response(final boolean error_, final boolean warning_, final String jsonData, final String message_) {
        _message = message_;
        _error = error_;
        _warning = warning_;
        _data = jsonData;
    }

    public String getMessage() {
        return _message;
    }

    public Boolean getError() {
        return _error;
    }

    public Object getData() {
        return _data;
    }

    public boolean getWarning() {
        return _warning;
    }
}
