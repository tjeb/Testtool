/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.service;

import md.maxcode.si.tools.TTSettings;
import md.maxcode.si.tools.UserTypeEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

@Service
public class StartupService implements InitializingBean {

    protected final Logger logger = Logger.getLogger(getClass().getName());
    @Autowired(required = true)
    UserService userService;
    @Autowired(required = true)
    TTSettings ttSettings;
    @Autowired(required = true)
    InboundDocumentsMonitorService inboundDocumentsMonitorService;

    @Override
    public void afterPropertiesSet() throws Exception {
        final Properties prop;
        try {
            InputStream inputStream = StartupService
                    .class
                    .getClassLoader()
                    .getResourceAsStream("main.properties");

            prop = new Properties();
            prop.load(inputStream);
            BufferedReader br = new BufferedReader(new FileReader(prop.getProperty("propertiesFilePath")));
            prop.load(br);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("There was an error related to web module's properties file");
            logger.severe("An error has occurred while trying to startup project. Please check proprety files. Detailed error: " + e.getMessage());
            return;
        }

        ttSettings.setFields(prop);

        if (userService.getByUsername("admin") == null) {            
            userService.insertUser("admin", "Simplerinvoicing", "", "admin", "lFuVfQxeRbeb0mqHrFjZ", true, UserTypeEnum.full.name(), true, false, "");
        }

        inboundDocumentsMonitorService.startMonitoring();
    }
}