/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.security;

public class Privilege {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name_) {
        name = name_;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Privilege [name=");
        builder.append(name);
        builder.append("]");
        return builder.toString();
    }
}