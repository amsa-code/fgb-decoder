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
    , COARSE_POSITION("Coarse Position") //
    , HEX_OFFSET("Offset Position") //
    , FINE_POSITION("Fine Position") //
    , LAT_LON("Lat Lon") //
    , RLM_CAPABILITY_TYPE("RLM Capability Type") //
    , RLS_PROVIDER_ID("RLS Provider ID") //
    , BEACON_TYPE("RLS Provider ID") //
    , RLM_CAPABILITY_TYPE_1_AUTO("RLM Capability Type-1 (Auto)") //
    , RLM_CAPABILITY_TYPE_2_MANUAL("RLM Capability Type-2 (Manual)") //
    , RLM_RECEIVED_TYPE_1_AUTO("RLM Received Type-1 (Auto)") //
    , RLM_RECEIVED_TYPE_2_MANUAL("RLM Received Type-2 (Manual)") //
    , OFFSET_POSITION("Offset Position") //
    , CS_TYPE_APPROVAL("C/S Type approval") //
    , SHIP_MMSI("Ship MMSI") //
    , SHIP_SECURITY_MMSI("Ship Security MMSI") //
    , FIXED_BITS("Fixed bits") //
    , USER_PROTOCOL_TYPE("User Protocol Type") //
    , SPECIFIC_ELT_NUMBER("Specific ELT number") //
    , AUX_RADIO_LOCATING("Aux. radio-locating") //
    , EMERGENCY_CODE("Emergency Code") //
    , ACTIVATION_TYPE("Activation Type") //
    , AIRCRAFT_REG_MARKING("Aircraft Reg. Marking") //
    , RADIO_CALLSIGN("Radio Callsign") //
    , SHIP_MMSI_LAST_6_DIGITS("Ship MMSI Last 6 Digits") //
    , EMERGENCY_CODE_FLAG("Emergency Code Flag") //
    , ORBITOGRAPHY_DATA("Orbitography Data") //
    , ORBITOGRAPHY_ID("Orbitography ID") //
    , ORBITOGRAPHY_4_BITS("Orbitography 4 bits") //
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
