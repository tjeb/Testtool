/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.service;

import md.maxcode.si.domain.AccessPoint;
import md.maxcode.si.domain.FileType;
import md.maxcode.si.domain.User;
import md.maxcode.si.persistence.AccessPointMapper;
import md.maxcode.si.persistence.FileTypesMapper;
import md.maxcode.si.tools.UtilsComponent;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

// used to check the certificate
import java.io.StringReader;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x500.X500Name;

@Service
public class InputValidationService {

    @Autowired
    UserService userService;

    @Autowired(required = true)
    private FileTypesMapper fileTypesMapper;

    @Autowired(required = true)
    private AccessPointMapper accessPointMapper;

    @Autowired(required = true)
    private UtilsComponent utilsComponent;

    private UrlValidator urlValidator = UrlValidator.getInstance();
    private EmailValidator emailValidator = EmailValidator.getInstance();

    public Map<String, String> validateUserData(final String name_,
                                                final String companyName_,
                                                final String username_,
                                                final String identifier,
                                                final String password_,
                                                final String email,
                                                final String type_,
                                                final boolean checkUniqueness,
                                                final boolean checkEmail) {
        final Map<String, String> errors = validateUserData(name_, companyName_, username_, identifier, email, type_, checkUniqueness, checkEmail);

        if (password_ != null && !password_.equals("")) {
            testPasswordRules(errors, password_);
        }

        return errors;
    }

    private Map<String, String> testPasswordRules(final Map<String, String> errors, final String password_) {
        Pattern passwordPattern = Pattern.compile("(?=.*[a-zA-z0-9])(?=.*[!@#$%&*()_+=|<>?{}\\\\[\\\\]~-])");
        if (password_ == null || password_.length() < 6) {
            errors.put("newPassword", "New password must have at least 6 characters!");
        } else if (!passwordPattern.matcher(password_).find()) {
            errors.put("newPassword", "New password must contain at least 5 alphanumeric characters and one special character!");
        }

        return errors;
    }

    public Map<String, String> validateUserData(final String name_,
                                                final String companyName_,
                                                final String username_,
                                                final String identifier,
                                                final String email,
                                                final String type_,
                                                final boolean testUniqueness,
                                                final boolean checkEmail) {
        final Map<String, String> errors = new HashMap<String, String>();

        if (name_ == null || name_.isEmpty()) {
            errors.put("name", "Name is wrong");
        }

        if (companyName_ == null || companyName_.isEmpty()) {
            errors.put("companyName", "Company name is wrong");
        }

        if (username_ == null || username_.isEmpty()) {
            errors.put("username", "Username name is wrong");
        } else if (testUniqueness) {
            User user = userService.getByUsername(username_);
            if (user != null) {
                errors.put("username", "This username is already registered");
            }
        }

        if (identifier == null || identifier.isEmpty()) {
            errors.put("identifier", "Identifier is wrong");
        } else if (testUniqueness) {
            User user = userService.getByIdentifier(identifier);
            if (user != null) {
                errors.put("identifier", "This identifier is already registered");
            }
        }

        if (checkEmail) {
            String validationResult = validateEmail(email);

            if (validationResult != null) {
                errors.put("email", validationResult);
            }
        }

        if (type_ == null || type_.isEmpty()) {
            errors.put("type", "Type name is wrong");
        }

        return errors;
    }

    public Map<String, String> validateDocumentType(final String name_,
                                                    final String identifier_) {
        final Map<String, String> errors = new HashMap<String, String>();

        if (identifier_ == null || identifier_.isEmpty()) {
            errors.put("identifier", "Document identifier have wrong value");
        } else {
            FileType fileType = fileTypesMapper.getByIdentifier(identifier_);
            if (fileType != null) {
                errors.put("identifier", "Document type with this identifier is already registered");
            }
        }

        if (name_ == null || name_.isEmpty()) {
            errors.put("name", "Document type name have wrong value");
        }

        return errors;
    }

    public Map<String, String> validateAPData(final String name,
                                              final String url,
                                              final String certificate,
                                              final Long userId,
                                              final String description,
                                              final String contactEmail,
                                              final String technicalUrl,
                                              final boolean testUniqueness) {
        final Map<String, String> errors = new HashMap<String, String>();
        String validationResult;

        if (name == null || name.isEmpty()) {
            errors.put("name", "Invalid access point name");
        } else if (testUniqueness) {
            AccessPoint accessPoint = accessPointMapper.getByNameAndUserId(name, userId);
            if (accessPoint != null) {
                errors.put("name", "Access point with this name already exist");
            }
        }

        if (url == null || url.isEmpty()) {
            errors.put("url", "URL is wrong");
        } else {
            if (!url.contains("localhost")) {
                if (!urlValidator.isValid(url)) {
                    errors.put("url", "URL is invalid");
                }
            }
        }

        if (certificate == null || certificate.isEmpty()) {
            errors.put("certificate", "The content of the certificate should not be empty");
        } else {
            // See if certificate is valid; try to get CN from it
            try {
                System.out.println("[XX] 1");
                CertificateFactory fact = CertificateFactory.getInstance("X.509");
                X509Certificate x509certificate = (X509Certificate) fact.generateCertificate(new ByteArrayInputStream(certificate.getBytes(StandardCharsets.UTF_8)));
                X500Name x500name = new JcaX509CertificateHolder(x509certificate).getSubject();
                System.out.println("[XX] 6");
                RDN cn = x500name.getRDNs(BCStyle.CN)[0];
                System.out.println("[XX] 7");

                String x509name = IETFUtils.valueToString(cn.getFirst().getValue());
                System.out.println("[XX] 8");
            } catch (RuntimeException exc) {
                System.out.println("[XX] ERROR1");
                System.out.println("[XX] Exception: " + exc.toString());
                errors.put("certificate", "Error reading certificate: " + exc.getMessage());
            } catch (Exception exc2) {
                System.out.println("[XX] ERROR2");
                System.out.println("[XX] Exception: " + exc2.toString());
                errors.put("certificate", "Error reading certificate: " + exc2.getMessage());
            }
        }

        if (description == null || description.isEmpty()) {
            errors.put("description", "Description cannot be empty or null");
        }

        if (technicalUrl == null || technicalUrl.isEmpty()) {
            errors.put("technicalUrl", "Wrong email");
        }

        validationResult = validateUrl(technicalUrl);

        if (validationResult != null) {
            errors.put("technicalUrl", validationResult);
        }

        validationResult = validateEmail(contactEmail);

        if (validationResult != null) {
            errors.put("contactEmail", validationResult);
        }

        return errors;
    }

    public Map<String, String> validateGSMP(final Long endPointId,
                                            final String participantName) {
        final Map<String, String> errors = new HashMap<String, String>();
        String validationResult;

        if (endPointId == null || endPointId <= 0) {
            errors.put("endPointId", "Invalid endpoint selected");
        }

        if (participantName == null || participantName.isEmpty()) {
            errors.put("participantName", "Participant name cannot be empty");
        }

        return errors;
    }

    private String validateUrl(String value) {
        if (value == null || value.isEmpty()) {
            return "Url seems to be empty";
        } else {
            if (!urlValidator.isValid(value)) {
                return "Provided url is invalid";
            }
        }

        return null;
    }

    private String validateEmail(String value) {
        if (value == null || value.isEmpty()) {
            return "E-mail seems to be empty";
        } else {
            if (!emailValidator.isValid(value)) {
                return "Provided e-mail is invalid";
            }

            if (userService.getByEmail(value) != null) {
                return "This e-mail is already registered";
            }
        }

        return null;
    }

    public Map newPassword(final User user, final String currentPassword, final String newPassword_, final String newPassword2_) {
        Map<String, String> result = new HashMap<>();

        String passwordHash = utilsComponent.getPasswordHash(currentPassword, user.getSalt());

        Pattern passwordPattern = Pattern.compile("(?=.*[a-zA-z0-9])(?=.*[!@#$%&*()_+=|<>?{}\\\\[\\\\]~-])");

        if (!passwordHash.equals(user.getPassword())) {
            result.put("currentPassword", "Wrong password!");
        }

        result.putAll(newPassword(newPassword_, newPassword2_));

        return result;
    }

    public Map newPassword(final String newPassword, final String newPassword2) {
        Map<String, String> errors = new HashMap<>();

        testPasswordRules(errors, newPassword);

        if (!newPassword.equals(newPassword2)) {
            errors.put("newPassword2", "New password confirmation fails!");
        }

        return errors;
    }

    public Map name(final String name) {
        Map<String, String> result = new HashMap<>();

        if (name == null || name.isEmpty()) {
            result.put("name", "You must provide a name!");
        }

        return result;
    }

}
