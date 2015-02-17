/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.xsl;

public class XSLErrorInfo {
    private String test;
    private String flag;
    private String location;
    private String content;
    private String XMLPath;
    private String XSLFileName;

    public String getTest() {
        return test;
    }

    public void setTest(final String test) {
        this.test = test;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(final String flag) {
        this.flag = flag;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "XSLErrorInfo{" +
                "test='" + test + '\'' +
                ", flag='" + flag + '\'' +
                ", location='" + location + '\'' +
                ", content='" + content + '\'' +
                ", XSLFileName='" + XSLFileName + '\'' +
                ", XMLPath='" + XMLPath + '\'' +
                '}';
    }

    public void setXMLFileName(final String XMLPath) {
        this.XMLPath = XMLPath;
    }

    public String getXMLPath() {
        return XMLPath;
    }

    public String getXSLFileName() {
        return XSLFileName;
    }

    public void setXSLFileName(final String XSLFileName) {
        this.XSLFileName = XSLFileName;
    }

    public boolean isFatal() {
        return !flag.toLowerCase().equals("warning") && !flag.toLowerCase().equals("info");
    }

    public String toMessage() {
        return flag.toUpperCase() + ": " + content + "; LOCATION: " + location + "; While validating against: " + XSLFileName;
    }
}
