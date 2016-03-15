/* Copyright (c) 2016 - SIDN */

package md.maxcode.si.tools;
import java.lang.String;

public class ActorID {
    // Contents: Scheme, Code
    // Specified in https://joinup.ec.europa.eu/svn/peppol/TransportInfrastructure/PEPPOL_Policy%20for%20use%20of%20identifiers-300.pdf
    private static String[][] SCHEMES = {
        { "FR:SIRENE", "0002" },
        { "SE:ORGNR", "0007" },
        { "FR:SIRET", "0009" },
        { "FI:OVT", "0037" },
        { "DUNS", "0060" },
        { "GLN", "0088" },
        { "DK:P", "0096" },
        { "IT:FTI", "0097" },
        { "NL:KVK", "0106" },
        { "IT:SIA", "0135" },
        { "IT:SECETI", "0142" },
        { "DK:CPR", "9901" },
        { "DK:CVR", "9902" },
        { "DK:SE", "9904" },
        { "IT:VAT", "9906" },
        { "IT:CF", "9907" },
        { "NO:ORGNR", "9908" },
        { "NO:VAT", "9909" },
        { "HU:VAT", "9910" },
        { "EU:VAT", "9912" },
        { "EU:REID", "9913" },
        { "AT:VAT", "9914" },
        { "AT:GOV", "9915" },
        { "IS:KT", "9917" },
        { "IBAN", "9918" },
        { "ES:VAT", "9920" },
        { "IT:IPA", "9921" },
        { "AD:VAT", "9922" },
        { "AL:VAT", "9923" },
        { "BA:VAT", "9924" },
        { "BE:VAT", "9925" },
        { "BG:VAT", "9926" },
        { "CH:VAT", "9927" },
        { "CY:VAT", "9928" },
        { "CZ:VAT", "9929" },
        { "DE:VAT", "9930" },
        { "EE:VAT", "9931" },
        { "GB:VAT", "9932" },
        { "GR:VAT", "9933" },
        { "HR:VAT", "9934" },
        { "IE:VAT", "9935" },
        { "LI:VAT", "9936" },
        { "LT:VAT", "9937" },
        { "LU:VAT", "9938" },
        { "LV:VAT", "9939" },
        { "MC:VAT", "9940" },
        { "ME:VAT", "9941" },
        { "MK:VAT", "9942" },
        { "MT:VAT", "9943" },
        { "NL:VAT", "9944" },
        { "PL:VAT", "9945" },
        { "PT:VAT", "9946" },
        { "RO:VAT", "9947" },
        { "RS:VAT", "9948" },
        { "SI:VAT", "9949" },
        { "SK:VAT", "9950" },
        { "SM:VAT", "9951" },
        { "TR:VAT", "9952" },
        { "VA:VAT", "9953" },
        { "NL:OIN", "9954" },
        { "NL:ION", "9954" },
        { "SE:VAT", "9955" },
        { "ZZZ", "9999" }
    };

    String actorIDString;

    public ActorID(String actorID) {
        actorIDString = actorID;
    }

    public ActorID(String scheme, String actor) {
        // If the scheme is "iso6523-actorid-upis", or
        // if the scheme is null, actor is already
        // the full identifier
        if (scheme == null || scheme.equals("iso6523-actorid-upis")) {
            actorIDString = actor;
        } else {
            actorIDString = schemeToCode(scheme) + ":" + actor;
        }
    }

    public static String schemeToCode(String scheme) {
        for (String[] s : SCHEMES) {
            if (s[0].equals(scheme.toUpperCase())) {
                return s[1];
            }
        }
        return "<Unknown scheme>";
    }

    public boolean equals(ActorID other) {
        return actorIDString.equals(other.toString());
    }

    public String toString() {
        return actorIDString;
    }
}
