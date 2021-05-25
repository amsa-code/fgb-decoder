package au.gov.amsa.fgb.internal;

enum AttributeType {
    _121_5_MHZ_HOMING("homing121_5MHz", JsonType.BOOLEAN), //
    ACTIVATION_TYPE("activationType"), //
    ADDITIONAL_DATA_FLAG("additionalDataFlag"), //
    ADDITIONAL_ELT_NUMBERS("additionalEltNumbers", JsonType.INTEGER), //
    AIRCRAFT_24_BIT_ADDRESS_HEX("aircraft24BitAddressHex"), //
    AIRCRAFT_24_BIT_ADDRESS_OCTAL("aircraft24BitAddressOctal"), //
    AIRCRAFT_CALL_SIGN("aircraftCallsign"), //
    AIRCRAFT_OPERATOR("aircraftOperator"), //
    AIRCRAFT_REG_MARKING("aircraftRegistrationMarking"), //
    AIRCRAFT_SERIAL_NUMBER("aircraftSerialNumber", JsonType.INTEGER), //
    AUX_RADIO_LOCATING("auxiliaryRadioLocatingDevice"), // TODO set enum in schema
    BCH_2("bch2"), //
    BEACON_SERIAL_NUMBER("beaconSerialNumber", JsonType.INTEGER), //
    BEACON_TYPE("beaconType"), //
    COARSE_POSITION_LATITUDE("coarsePositionLatitude", JsonType.NUMBER), //
    COARSE_POSITION_LONGITUDE("coarsePositionLongitude", JsonType.NUMBER), //
    COUNTRY_CODE("countryCode", JsonType.INTEGER), //
    COUNTRY_OF_REGISTRATION("countryOfRegistration"), //
    CS_CERT_NO_PRESENT("cSCertNumberPresent", JsonType.BOOLEAN), //
    CS_TYPE_APPROVAL("cSTypeApproval", JsonType.INTEGER), //
    CS_TYPE_APPROVAL_NUMBER("cSTypeApprovalNumber", JsonType.INTEGER), //
    DISABLED("disabled", JsonType.BOOLEAN), //
    EMERGENCY_CODE_PRESENT("emergencyCodePresent", JsonType.BOOLEAN), //
    EMERGENCY_CODE_FLAG("emergencyCodeFlag"), //
    ENCODED_POSITION_SOURCE("encodedPositionSource"), //
    ERROR("error"), //
    ERROR_CORRECTING_CODE_1("errorCorrectingCode1"), //
    ERROR_CORRECTING_CODE_2("errorCorrectingCode2"), //
    FIRE_FLAG("fireFlag", JsonType.BOOLEAN), //
    FIXED_BITS("fixedBits"), //
    HEX_DATA("hexData"), //
    HEX_ID("hexId"), //
    ID_POSN("idPosition"), //
    IS_LONG_MESSAGE_DEFAULT("isLongMessageDefault", JsonType.BOOLEAN), //
    LATITUDE("latitude", JsonType.NUMBER), //
    LONGITUDE("longitude", JsonType.NUMBER), //
    MEDICAL_HELP_FLAG("medicalHelpFlag", JsonType.BOOLEAN), //
    MESSAGE_TYPE("messageType"), //
    NATIONAL_USE("nationalUse"), //
    NATURE_OF_DISTRESS("natureOfDistress"), //
    OFFSET_POSITION_LATITUDE_DIFF("offsetPositionLatitudeDiff", JsonType.NUMBER), //
    OFFSET_POSITION_LONGITUDE_DIFF("offsetPositionLongitudeDiff", JsonType.NUMBER), //
    ORBITOGRAPHY_4_BITS("orbitography4Bits"), //
    ORBITOGRAPHY_DATA("orbitographyData"), //
    ORBITOGRAPHY_ID("orbitographyId"), //
    PDF_2("pdf2"), //
    PROTOCOL_TYPE("protocolType"), //
    RADIO_CALLSIGN("radioCallsign"), //
    RLM_CAPABILITY_TYPE_1_AUTO("rlmCapabilityType1Auto", JsonType.BOOLEAN), //
    RLM_CAPABILITY_TYPE_2_MANUAL("rlmCapabilityType2Manual", JsonType.BOOLEAN), //
    RLM_CAPABILITY_TYPE("rlmCapabilityType"), //
    RLM_RECEIVED_TYPE_1_AUTO("rlmReceivedType1Auto", JsonType.BOOLEAN), //
    RLM_RECEIVED_TYPE_2_MANUAL("rlmReceivedType2Manual", JsonType.BOOLEAN), //
    RLS_ID("rlsId", JsonType.INTEGER), //
    RLS_PROVIDER_ID("rlsProviderId"), //
    RLS_TAC_NUMBER("rlsTacNumber"), //
    RLS_MMSI_LAST_6_DIGITS("rlsMmsiLast6Digits"), //
    SERIAL_NUMBER("serialNumber", JsonType.INTEGER), //
    SHIP_MMSI_LAST_6_DIGITS("shipMmsiLast6Digits"), //
    SHIP_MMSI("shipMmsi", JsonType.INTEGER), //
    SHIP_SECURITY_MMSI("shipSecurityMmsi", JsonType.INTEGER), //
    SPARE("spare"), //
    SPECIFIC_BEACON_NUMBER("specificBeaconNumber"), //
    SPECIFIC_ELT_NUMBER("specificEltNumber", JsonType.INTEGER), //
    SUPPLEMENTARY_DATA("supplementaryData"), //
    USER_PROTOCOL_TYPE("userProtocolType"), //
    US_MANUFACTURER_ID("uSManufacturerId", JsonType.INTEGER), //
    US_MODEL_ID("uSModelId", JsonType.INTEGER), //
    US_NATIONAL_USE("uSNationalUse"), //
    US_RUN_NO("uSRunNumber", JsonType.INTEGER), //
    US_SEQUENCE_NO("uSSequenceNumber", JsonType.INTEGER);

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

    JsonType jsonType() {
        return jsonType;
    }
}
