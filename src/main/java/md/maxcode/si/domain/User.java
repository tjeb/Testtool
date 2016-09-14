/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.domain;

import md.maxcode.si.security.Role;
import md.maxcode.si.tools.UserTypeEnum;
//import org.postgresql.util.PGobject;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class User implements UserDetails {

    private Long id;
    private String username;
    private String identifier;
    private String name;
    private String companyName;
    private String password;
    private String salt;
    private String email;

    private UserTypeEnum type;
    private Boolean editable;

    /* Spring Security fields*/
    private List<Role> authorities;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;


    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable_) {
        editable = editable_;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id_) {
        this.id = id_;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username_) {
        this.username = username_;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName_) {
        companyName = companyName_;
    }

    public String getName() {
        return name;
    }

    public void setName(String name_) {
        this.name = name_;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt_) {
        salt = salt_;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled_) {
        this.enabled = enabled_;
    }

    public UserTypeEnum getType() {
        return type;
    }

    public void setType(UserTypeEnum type_) {
        type = type_;
    }

    public void setType(String type_) {
        type = UserTypeEnum.valueOf(type_);
    }

/*
    public void setType(PGobject PGObject_) {
        type = UserTypeEnum.valueOf(PGObject_.getValue());
    }
*/
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired_) {
        credentialsNonExpired = credentialsNonExpired_;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked_) {
        this.accountNonLocked = accountNonLocked_;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired_) {
        this.accountNonExpired = accountNonExpired_;
    }

    public List<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Role> authorities_) {
        this.authorities = authorities_;
    }

    @Override
    public String toString() {
        return "toString() --> User = {" + id + ", " + name + "}";
    }
}
