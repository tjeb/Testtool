/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.tools;

import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Properties;

@Component("ttSettings")
public class TTSettings {
    public final String DATE_TIME_ZONE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSXXX";

    public String smpUrl;
    public String smpToken;
    public String smpHmacAlgorithm;
    public String smpUserName;
    public String smpHmacKey;
    public String AS2SystemIdentifier;

    public String storeLocation;
    public String storeSchemaFiles;
    public String storeCertificateFiles;
    public String user_menu_color;
    public String admin_menu_color;
    public String storeUserFiles;
    public String channelId;
    public String hmacMasterKey;
    public String keyStorePassword;
    public String openSSL_bin;
    public String openSSL_conf;
    public String privateKey_pass;
    public Object triggerUrl;
    public String loopbackUrl;

    public String defaultSenderId;
    public String defaultRecipientId;
    public String terminal;
    public String inbound_documents_folder;
    public String oxalis_standalone_jar;
    public String java_bin;
    public String galaxy_prefix;
    public String galaxy_suffix;

    public void setFields(final Properties prop) {
        final TTSettings self = this;
        final String int_name = int.class.getName();
        final String Integer_name = Integer.class.getName();
        final String Long_name = Long.class.getName();
        final String long_name = long.class.getName();

        ReflectionUtils.doWithFields(TTSettings.class, new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
                final String fieldName = field.getName();
                if (prop.containsKey(fieldName)) {
                    String className = field.getType().getName();

                    String propertyValue = prop.getProperty(fieldName);
                    Object value = propertyValue;

                    if (className.equals(long_name) || className.equals(Long_name)) {
                        value = Long.parseLong(propertyValue);
                    } else if (className.equals(int_name) || className.equals(Integer_name)) {
                        value = Integer.parseInt(propertyValue);
                    }

                    field.set(self, value);
                }
            }
        });
    }
}
