package com.amsa.fgb.internal;

public enum AttributeType {
    _121_5_MHZ_HOMING("121.5 MHz Homing"), //
    ACTIVATION_TYPE("Activation Type"), //
    ADDITIONAL_DATA_FLAG("Additional Data Flag"), //
    ADDITIONAL_ELT_NUMBERS("Additional ELT numbers"), AIRCRAFT_24_BIT_ADDRESS_BINARY("Aircraft 24 bit address Binary"), //
    AIRCRAFT_24_BIT_ADDRESS_HEX("Aircraft 24 bit address (hex)"), //
    AIRCRAFT_24_BIT_ADDRESS_OCTAL("Aircraft 24 bit address (octal)"), //
    AIRCRAFT_CALL_SIGN("Aircraft Reg. Marking"), //
    AIRCRAFT_OPERATOR("Aircraft Operator"), //
    AIRCRAFT_REG_MARKING("Aircraft Reg. Marking"), //
    AIRCRAFT_SERIAL_NUMBER("Aircraft Serial Number"), //
    AUX_RADIO_LOCATING("Aux. radio-locating"), //
    BCH_2("BCH-2"), //
    BEACON_SERIAL_NUMBER("Beacon Serial Number"), //
    BEACON_TYPE("Beacon Type"), //
    COARSE_POSITION("Coarse Position"), //
    COUNTRY_CODE("Country Code"), //
    COUNTRY_OF_REGISTRATION("Country of Registration"), //
    CS_CERT_NO_PRESENT("C/S cert. no. present"), //
    CS_TYPE_APPROVAL("C/S Type approval"), //
    CS_TYPE_APPROVAL_NUMBER("C/S Type approval number"), //
    DISABLED("Disabled"), //
    EMERGENCY_CODE("Emergency Code"), //
    EMERGENCY_CODE_FLAG("Emergency Code Flag"), //
    ENCODED_POSITION_SOURCE("Encoded Position Source"), //
    ERROR(""), //
    ERROR_CORRECTING_CODE("Error Correcting Code"), //
    FINE_POSITION("Fine Position"), //
    FIRE_FLAG("Fire Flag"), //
    FIXED_BITS("Fixed bits"), //
    HEX_DATA("Hex Data"), //
    HEX_ID("Hex Id"), //
    HEX_OFFSET("Offset Position"), //
    ID_POSN("ID+POSN"), //
    IS_LONG_MESSAGE_DEFAULT("Is Long Message Default"), //
    LATITUDE("Latitude"), //
    LAT_LON("Lat Lon"), //
    LONGITUDE("Longitude"), //
    MEDICAL_HELP_FLAG("Medical Help Flag"), //
    MESSAGE_TYPE("Message Type"), //
    NATIONAL_USE("National Use"), //
    NATURE_OF_DISTRESS("Nature of Distress"), //
    OFFSET_POSITION("Offset Position"), //
    ORBITOGRAPHY_4_BITS("Orbitography 4 bits"), //
    ORBITOGRAPHY_DATA("Orbitography Data"), //
    ORBITOGRAPHY_ID("Orbitography ID"), //
    PDF_2("PDF-2"), //
    PROTOCOL_TYPE("Protocol Type"), //
    RADIO_CALLSIGN("Radio Callsign"), //
    RLM_CAPABILITY_TYPE_1_AUTO("RLM Capability Type-1 (Auto)"), //
    RLM_CAPABILITY_TYPE_2_MANUAL("RLM Capability Type-2 (Manual)"), //
    RLM_CAPABILITY_TYPE("RLM Capability Type"), //
    RLM_RECEIVED_TYPE_1_AUTO("RLM Received Type-1 (Auto)"), //
    RLM_RECEIVED_TYPE_2_MANUAL("RLM Received Type-2 (Manual)"), //
    RLS_ID("RLS ID"), //
    RLS_PROVIDER_ID("RLS Provider ID"), //
    RLS_TAC_NUMBER("RLS TAC Number"), //
    SERIAL_NUMBER("Serial Number"), //
    SHIP_MMSI_LAST_6_DIGITS("Ship MMSI Last 6 Digits"), //
    SHIP_MMSI("Ship MMSI"), //
    SHIP_SECURITY_MMSI("Ship Security MMSI"), //
    SPARE("Spare"), //
    SPECIFIC_BEACON_NUMBER("Specific Beacon Number"), //
    SPECIFIC_ELT_NUMBER("Specific ELT number"), //
    SUPPLEMENTARY_DATA("Supplementary Data"), //
    USER_PROTOCOL_TYPE("User Protocol Type"), //
    US_MANUFACTURER_ID("US Manufacturer ID"), //
    US_MODEL_ID("US Model ID"), //
    US_NATIONAL_USE("US National Use"), //
    US_RUN_NO("US Run No."), //
    US_SEQUENCE_NO("US Sequence No.");

    private final String name;

    private AttributeType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
