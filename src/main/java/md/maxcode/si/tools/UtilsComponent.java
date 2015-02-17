/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.tools;

import md.maxcode.si.xsl.XSLErrorInfo;
import org.apache.tomcat.util.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class UtilsComponent implements InitializingBean {
    public static UtilsComponent instance;
    protected final Logger logger = Logger.getLogger(getClass().getName());
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSXXX");
    private final Map<Integer, String> digits = new HashMap<Integer, String>();
    {
        digits.put(0, "000000");
        digits.put(1, "00000");
        digits.put(2, "0000");
        digits.put(3, "000");
        digits.put(4, "00");
        digits.put(5, "0");
        digits.put(6, "");
    }
    @Autowired
    private TTSettings ttSettings;
    @Autowired
    private TOTP totp;

    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;
    }

    public String toJsonString(Object object) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setDateFormat(dateFormat);

        try {
            mapper.writeValue(out, object);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }

        return new String(out.toByteArray());

    }

    public Object stringToInstance(String string_, Class class_) {
        try {
            return new ObjectMapper().readValue(string_, class_);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String toMD5(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(message.getBytes("UTF-8"));

            //converting byte array to Hexadecimal String
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }

            digest = sb.toString();

        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        return digest;
    }

    public String getFullFilePath(final String store_, final String fileName_) {
        return ttSettings.storeLocation + store_ + fileName_;
    }

    public String getFullFilePath(final String store_, final Long fileType_, final String fileName_, final boolean createFolder_) {
        final StringBuilder s = new StringBuilder();
        s.append(ttSettings.storeLocation)
                .append(store_)
                .append(fileType_)
                .append("/");

        if (createFolder_) {
            File file = new File(s.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
        }

        return s.append(fileName_).toString();
    }

    public String getFullFilePath(final String store_, final String fileName_, final String extension_) {
        return getFullFilePath(store_, fileName_) + "." + extension_;
    }

    public String getNow() {
        return new SimpleDateFormat(ttSettings.DATE_TIME_ZONE_PATTERN).format(new Date());
    }

    public String getRandomString() {
        return toMD5(getNow());
    }

    public String getPasswordHash(String password_, String salt_) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(password_).append(salt_);

        return toMD5(stringBuilder.toString());
    }

    public String toHMACBase64String(String data) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(ttSettings.hmacMasterKey.getBytes(), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        byte[] rawHMAC = mac.doFinal(data.getBytes());
        String result = Base64.encodeBase64String(rawHMAC);
        return result;
    }

    public String get6digits(final String endpointId) {
        return digits.get(endpointId.length()) + endpointId;
    }

    public Object jsonToObject(String json, Class clazz) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            logger.severe("Could not convert JSON to Object " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public String getFileContent(final String path) {
        StringBuilder stringBuilder = new StringBuilder();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            String ls = System.getProperty("line.separator");
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ("File not found");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    public void performDownload(HttpServletRequest request,
                                HttpServletResponse response,
                                String filePath_,
                                String fileNameToPropose_) throws IOException {
        ServletContext context = request.getServletContext();
        File downloadFile = new File(filePath_);
        FileInputStream inputStream = new FileInputStream(downloadFile);

        String mimeType = context.getMimeType(filePath_);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", fileNameToPropose_);
        response.setHeader(headerKey, headerValue);

        OutputStream outStream = response.getOutputStream();

        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outStream.close();
    }

    public boolean hasGalaxyErrors(final String httpResult, final String errorMessage, final Logger logger) {
        if (httpResult == null || httpResult.isEmpty()) {
            return true;
        }

        Map response = ((Map) jsonToObject(httpResult, Map.class));
        response = (Map) response.get("response");
        List<Map> errors = (List) response.get("errors");
        if (errors.size() > 0) {
            logger.severe(errorMessage);
            for (Map i : errors) {
                logger.severe(i.get("code") + " :: " + i.get("description"));
            }
            logger.severe("---------------------------------------------------");
            return true;
        }

        return false;
    }

    public String toMessage(final List<XSLErrorInfo> xslErrorInfos, final Boolean userBR) {
        String br = userBR ? "<br>" : "\r\n";
        StringBuilder s = new StringBuilder();
        for (XSLErrorInfo xslErrorInfo : xslErrorInfos) {
            s.append(xslErrorInfo.toMessage()).append(br);
        }

        return s.toString();
    }
}
