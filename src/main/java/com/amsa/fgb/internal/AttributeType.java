package com.amsa.fgb.internal;

public enum AttributeType {
    _121_5_MHZ_HOMING("121.5 MHz Homing", JsonType.BOOLEAN), //
    ACTIVATION_TYPE("Activation Type"), //
    ADDITIONAL_DATA_FLAG("Additional Data Flag"), //
    ADDITIONAL_ELT_NUMBERS("Additional ELT numbers", JsonType.INTEGER), //
    AIRCRAFT_24_BIT_ADDRESS_HEX("Aircraft 24 bit address (hex)"), //
    AIRCRAFT_24_BIT_ADDRESS_OCTAL("Aircraft 24 bit address (octal)"), //
    AIRCRAFT_CALL_SIGN("Aircraft Callsign"), //
    AIRCRAFT_OPERATOR("Aircraft Operator"), //
    AIRCRAFT_REG_MARKING("Aircraft Reg. Marking"), //
    AIRCRAFT_SERIAL_NUMBER("Aircraft Serial Number", JsonType.INTEGER), //
    AUX_RADIO_LOCATING("Aux. radio-locating"), //
    BCH_2("BCH-2"), //
    BEACON_SERIAL_NUMBER("Beacon Serial Number", JsonType.INTEGER), //
    BEACON_TYPE("Beacon Type"), //
    COARSE_POSITION_LATITUDE("Coarse Position Latitude", JsonType.NUMBER), //
    COARSE_POSITION_LONGITUDE("Coarse Position Longitude", JsonType.NUMBER), //
    COUNTRY_CODE("Country Code", JsonType.INTEGER), //
    COUNTRY_OF_REGISTRATION("Country of Registration"), //
    CS_CERT_NO_PRESENT("C/S cert. no. present", JsonType.BOOLEAN), //
    CS_TYPE_APPROVAL("C/S Type approval", JsonType.INTEGER), //
    CS_TYPE_APPROVAL_NUMBER("C/S Type approval number", JsonType.INTEGER), //
    DISABLED("Disabled", JsonType.BOOLEAN), //
    EMERGENCY_CODE("Emergency Code"), //
    EMERGENCY_CODE_FLAG("Emergency Code Flag"), //
    ENCODED_POSITION_SOURCE("Encoded Position Source"), //
    ERROR("Error"), //
    ERROR_CORRECTING_CODE_1("Error Correcting Code 1"), //
    ERROR_CORRECTING_CODE_2("Error Correcting Code 2"), //
    FIRE_FLAG("Fire Flag", JsonType.BOOLEAN), //
    FIXED_BITS("Fixed bits"), //
    HEX_DATA("Hex Data"), //
    HEX_ID("Hex Id"), //
    ID_POSN("ID+POSN"), //
    IS_LONG_MESSAGE_DEFAULT("Is Long Message Default", JsonType.BOOLEAN), //
    LATITUDE("Latitude", JsonType.NUMBER), //
    LONGITUDE("Longitude", JsonType.NUMBER), //
    MEDICAL_HELP_FLAG("Medical Help Flag", JsonType.BOOLEAN), //
    MESSAGE_TYPE("Message Type"), //
    NATIONAL_USE("National Use"), //
    NATURE_OF_DISTRESS("Nature of Distress"), //
    OFFSET_POSITION_LATITUDE_DIFF("Offset Position Latitude Diff", JsonType.NUMBER), //
    OFFSET_POSITION_LONGITUDE_DIFF("Offset Position Longitude Diff", JsonType.NUMBER), //
    ORBITOGRAPHY_4_BITS("Orbitography 4 bits"), //
    ORBITOGRAPHY_DATA("Orbitography Data"), //
    ORBITOGRAPHY_ID("Orbitography ID"), //
    PDF_2("PDF-2"), //
    PROTOCOL_TYPE("Protocol Type"), //
    RADIO_CALLSIGN("Radio Callsign"), //
    RLM_CAPABILITY_TYPE_1_AUTO("RLM Capability Type-1 (Auto)", JsonType.BOOLEAN), //
    RLM_CAPABILITY_TYPE_2_MANUAL("RLM Capability Type-2 (Manual)", JsonType.BOOLEAN), //
    RLM_CAPABILITY_TYPE("RLM Capability Type"), //
    RLM_RECEIVED_TYPE_1_AUTO("RLM Received Type-1 (Auto)", JsonType.BOOLEAN), //
    RLM_RECEIVED_TYPE_2_MANUAL("RLM Received Type-2 (Manual)", JsonType.BOOLEAN), //
    RLS_ID("RLS ID", JsonType.INTEGER), //
    RLS_PROVIDER_ID("RLS Provider ID"), //
    RLS_TAC_NUMBER("RLS TAC Number"), //
    SERIAL_NUMBER("Serial Number", JsonType.INTEGER), //
    SHIP_MMSI_LAST_6_DIGITS("Ship MMSI Last 6 Digits"), //
    SHIP_MMSI("Ship MMSI", JsonType.INTEGER), //
    SHIP_SECURITY_MMSI("Ship Security MMSI", JsonType.INTEGER), //
    SPARE("Spare"), //
    SPECIFIC_BEACON_NUMBER("Specific Beacon Number"), //
    SPECIFIC_ELT_NUMBER("Specific ELT number", JsonType.INTEGER), //
    SUPPLEMENTARY_DATA("Supplementary Data"), //
    USER_PROTOCOL_TYPE("User Protocol Type"), //
    US_MANUFACTURER_ID("US Manufacturer ID", JsonType.INTEGER), //
    US_MODEL_ID("US Model ID", JsonType.INTEGER), //
    US_NATIONAL_USE("US National Use"), //
    US_RUN_NO("US Run No.", JsonType.INTEGER), //
    US_SEQUENCE_NO("US Sequence No.", JsonType.INTEGER);

    private final String name;
    private final JsonType jsonType;

    AttributeType(String name) {
        this(name, JsonType.STRING);
    }

    AttributeType(String name, JsonType jsonType) {
        this.name = name;
        this.jsonType = jsonType;
    }

    @Override
    public String toString() {
        return name;
    }

    public JsonType jsonType() {
        return jsonType;
    }
}
