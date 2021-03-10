package com.amsa.fgb.internal;

public enum AttributeType {

    IS_LONG_MESSAGE_DEFAULT("Is Long Message Default"), //
    PROTOCOL_TYPE("Protocol Type"), //
    RLS_TAC_NUMBER("RLS TAC Number") //
    , RLS_ID("RLS ID")//
    , HEX_DATA("Hex Data") //
    , HEX_ID("Hex Id") //
    , COUNTRY_CODE("Country Code") //
    , BEACON_SERIAL_NUMBER("Beacon Serial Number") //
    , AIRCRAFT_24_BIT_ADDRESS_HEX("Aircraft 24 bit address (hex)") //
    , AIRCRAFT_24_BIT_ADDRESS_OCTAL("Aircraft 24 bit address (octal)") //
    , AIRCRAFT_CALL_SIGN("Aircraft Reg. Marking") //
    , COUNTRY_OF_REGISTRATION("Country of Registration") //
    , AIRCRAFT_24_BIT_ADDRESS_BINARY("Aircraft 24 bit address Binary") //
    , SPECIFIC_BEACON_NUMBER("Specific Beacon Number") //
    , AIRCRAFT_OPERATOR("Aircraft Operator") //
    , AIRCRAFT_SERIAL_NUMBER("Aircraft Serial Number") //
    , SPARE("Spare") //
    , ADDITIONAL_DATA_FLAG("Additional Data Flag") //
    , ENCODED_POSITION_SOURCE("Encoded Position Source") //
    , _121_5_MHZ_HOMING("121.5 MHz Homing") //
    , NATIONAL_USE("National Use") //
    , LATITUDE("Latitude") //
    , LONGITUDE("Longitude") //
    , ERROR_CORRECTING_CODE("Error Correcting Code") //
    , NATURE_OF_DISTRESS("Nature of Distress") //
    , FIRE_FLAG("Fire Flag") //
    , MEDICAL_HELP_FLAG("Medical Help Flag") //
    , DISABLED("Disabled") //
    , ERROR("") //
    , PDF_2("PDF-2") //
    , BCH_2("BCH-2") //
    , ID_POSN("ID+POSN") //
    , SUPPLEMENTARY_DATA("Supplementary Data") //
    , MESSAGE_TYPE("Message Type") //
    ;

    private final String name;

    private AttributeType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
