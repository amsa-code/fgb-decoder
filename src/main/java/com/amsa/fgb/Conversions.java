package com.amsa.fgb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

abstract class Conversions {

    static private Map<String, String> hexMap;
    static private Map<String, String> binMap;
    static private Map<String, String> octMap;
    static private Map<String, String> mbaudotToAsciiMap;
    static private Map<String, String> asciiToMbaudotMap;
    static private Map<String, String> aircraftMap;
    static private Map<String, String> airRegMap;

    static {
        hexMap = new HashMap<>();
        hexMap.put("0000", "0");
        hexMap.put("0001", "1");
        hexMap.put("0010", "2");
        hexMap.put("0011", "3");
        hexMap.put("0100", "4");
        hexMap.put("0101", "5");
        hexMap.put("0110", "6");
        hexMap.put("0111", "7");
        hexMap.put("1000", "8");
        hexMap.put("1001", "9");
        hexMap.put("1010", "A");
        hexMap.put("1011", "B");
        hexMap.put("1100", "C");
        hexMap.put("1101", "D");
        hexMap.put("1110", "E");
        hexMap.put("1111", "F");

        /* Make binMap from hexMap */
        binMap = new HashMap<>();
        Iterator<Entry<String, String>> iter1 = hexMap.entrySet().iterator();
        while (iter1.hasNext()) {
            Map.Entry<String, String> e = iter1.next();
            binMap.put(e.getValue(), e.getKey());
        }

        // This is used to get the Country of Aircraft Registraion.
        // Used in the 24-Bit Address
        airRegMap = new HashMap<>();
        airRegMap.put("00000000010000", "ZIMBABWE");
        airRegMap.put("00000011000000", "BOTSWANA");
        airRegMap.put("00000011010100", "COMOROS");
        airRegMap.put("00000100100000", "GUINEA-BISSAU");
        airRegMap.put("00000100101000", "LESOTHO");
        airRegMap.put("00000101101000", "MALDIVES");
        airRegMap.put("00000101111000", "MAURITANIA");
        airRegMap.put("00000110000000", "MAURITIUS");
        airRegMap.put("00000110101000", "QATAR");
        airRegMap.put("00000111010000", "SEYCHELLES");
        airRegMap.put("00000111101000", "SWAZILAND");
        airRegMap.put("00001001010000", "BENIN");
        airRegMap.put("00001001011000", "CAPE VERDE");
        airRegMap.put("00001001100000", "DJIBOUTI");
        airRegMap.put("00001001111000", "SAO TOME AND PRINCIPE");
        airRegMap.put("00001010101000", "BARBADOS");
        airRegMap.put("00001010101100", "BELIZE");
        airRegMap.put("00001011110000", "SAINT VINCENT AND THE GRENADINES");
        airRegMap.put("00001100101000", "ANTIGUA AND BARBUDA");
        airRegMap.put("00001100110000", "GRENADA");
        airRegMap.put("00100000000100", "NAMIBIA");
        airRegMap.put("00100000001000", "ERITREA");
        airRegMap.put("01001100100000", "CYPRUS");
        airRegMap.put("01001101000000", "LUXEMBOURG");
        airRegMap.put("01001101001000", "MALTA");
        airRegMap.put("01001101010000", "MONACO");
        airRegMap.put("01010000000000", "SAN MARINO");
        airRegMap.put("01010000000100", "ALBANIA");
        airRegMap.put("01010000000111", "CROATIA");
        airRegMap.put("01010000001011", "LATVIA");
        airRegMap.put("01010000001111", "LITHUANIA");
        airRegMap.put("01010000010011", "RE OF MOLDOVA");
        airRegMap.put("01010000010111", "SLOVAKIA");
        airRegMap.put("01010000011011", "SOLVENIA");
        airRegMap.put("01010000011111", "URUGUAY");
        airRegMap.put("01010000011111", "UZBEKISTAN");
        airRegMap.put("01010001000000", "BELARUS");
        airRegMap.put("01010001000100", "ESTONIA");
        airRegMap.put("01010001001000", "RE OF MACEDONIA");
        airRegMap.put("01010001001100", "BOSNIA AND HERZEGOVINA");
        airRegMap.put("01010001010000", "GEORGIA");
        airRegMap.put("01010001010100", "TAJIKISTAN");
        airRegMap.put("01100000000000", "ARMENIA");
        airRegMap.put("01100000000010", "AZERBAIJAN");
        airRegMap.put("01100000000100", "KYRGYZSTAN");
        airRegMap.put("01100000000110", "TURKMENISTAN");
        airRegMap.put("01101000000000", "BHUTAN");
        airRegMap.put("01101000000100", "MICRONESIA, FEDERATED STATES OF");
        airRegMap.put("01101000001000", "MONGOLIA");
        airRegMap.put("01101000001100", "KAZAKHSTAN");
        airRegMap.put("01101000010000", "PALAU");
        airRegMap.put("01110000110000", "OMAN");
        airRegMap.put("10001001010100", "BRUNEI DARUSSALAM");
        airRegMap.put("10001001011100", "SOLOMON ISLANDS");
        airRegMap.put("10010000000000", "MARSHALL ISLANDS");
        airRegMap.put("10010000000100", "COOK ISLANDS");
        airRegMap.put("10010000001000", "SAMOA");
        airRegMap.put("11001000101000", "NAURU");
        airRegMap.put("11001000110000", "SAINT LUCIA");
        airRegMap.put("11001000110100", "TONGA");
        airRegMap.put("11001000111000", "KIRIBATI");
        airRegMap.put("11001001000000", "VANUATU");
        airRegMap.put("000000000110", "MOZAMBIQUE");
        airRegMap.put("000000110010", "BURUNDI");
        airRegMap.put("000000110100", "CAMEROON");
        airRegMap.put("000000110110", "CONGO");
        airRegMap.put("000000111000", "COTE D'IVOIRE");
        airRegMap.put("000000111110", "GABON");
        airRegMap.put("000001000010", "EQUATORIAL GUINEA");
        airRegMap.put("000001000100", "GHANA");
        airRegMap.put("000001000110", "GUINEA");
        airRegMap.put("000001001100", "KENYA");
        airRegMap.put("000001010000", "LIBERIA");
        airRegMap.put("000001010100", "MADAGASCAR");
        airRegMap.put("000001011000", "MALAWI");
        airRegMap.put("000001011010", "ETHIOPIA");
        airRegMap.put("000001011100", "MALI");
        airRegMap.put("000001100010", "NIGER");
        airRegMap.put("000001100100", "NIGERIA");
        airRegMap.put("000001101000", "UGANDA");
        airRegMap.put("000001101100", "CENTRAL AFRICAN RE");
        airRegMap.put("000001101110", "RWANDA");
        airRegMap.put("000001110000", "SENEGAL");
        airRegMap.put("000001111000", "SOMALIA");
        airRegMap.put("000001111100", "SUDAN");
        airRegMap.put("000010000000", "UNITED RE OF TANZANIA");
        airRegMap.put("000010000100", "CHAD");
        airRegMap.put("000010001000", "TOGO");
        airRegMap.put("000010001010", "ZAMBIA");
        airRegMap.put("000010001100", "DEMOCRATIC PEOPLE'S RE OF CONGO");
        airRegMap.put("000010010000", "ANGOLA");
        airRegMap.put("000010011010", "GAMBIA");
        airRegMap.put("000010011100", "BURKINA FASO");
        airRegMap.put("000010101000", "BAHAMAS");
        airRegMap.put("000010101100", "COLOMBIA");
        airRegMap.put("000010101110", "COSTA RICA");
        airRegMap.put("000010110000", "CUBA");
        airRegMap.put("000010110010", "EL SALVADOR");
        airRegMap.put("000010110100", "GUATEMALA");
        airRegMap.put("000010110110", "GUYANA");
        airRegMap.put("000010111000", "HAITI");
        airRegMap.put("000010111010", "HONDURAS");
        airRegMap.put("000010111110", "JAMAICA");
        airRegMap.put("000011000000", "NICARAGUA");
        airRegMap.put("000011000010", "PANAMA");
        airRegMap.put("000011000100", "DOMINICAN RE");
        airRegMap.put("000011000110", "TRINIDAD AND TOBAGO");
        airRegMap.put("000011001000", "SURINAM");
        airRegMap.put("010011001010", "IRELAND");
        airRegMap.put("010011001100", "ICELAND");
        airRegMap.put("011100000000", "AFGHANISTAN");
        airRegMap.put("011100000010", "BANGLADESH");
        airRegMap.put("011100000100", "MYANMAR");
        airRegMap.put("011100000110", "KUWAIT");
        airRegMap.put("011100001000", "LAO PEOPLE'S DEMOCRATIC RE");
        airRegMap.put("011100001010", "NEPAL");
        airRegMap.put("011100001110", "CAMBODIA");
        airRegMap.put("100010010000", "YEMEN");
        airRegMap.put("100010010100", "BAHRAIN");
        airRegMap.put("100010010110", "UNITED ARAB EMIRATES");
        airRegMap.put("100010011000", "PAPUA NEW GUINEA");
        airRegMap.put("110010001000", "FIJI");
        airRegMap.put("111010000000", "CHILE");
        airRegMap.put("111010000100", "ECUADOR");
        airRegMap.put("111010001000", "PARAGUAY");
        airRegMap.put("111010001100", "PERU");
        airRegMap.put("111010010100", "BOLIVIA");
        airRegMap.put("000000001", "SOUTH AFRICA");
        airRegMap.put("000000010", "EGYPT");
        airRegMap.put("000000011", "LIBYAN ARAB JAMAHIRIYA");
        airRegMap.put("000000100", "MOROCCO");
        airRegMap.put("000000101", "TUNISIA");
        airRegMap.put("000001101", "SIERRA LEONE");
        airRegMap.put("000010100", "ALGERIA");
        airRegMap.put("000011010", "MEXICO");
        airRegMap.put("000011011", "VENEZUELA");
        airRegMap.put("010001000", "AUSTRIA");
        airRegMap.put("010001001", "BELGIUM");
        airRegMap.put("010001010", "BULGARIA");
        airRegMap.put("010001011", "DENMARK");
        airRegMap.put("010001100", "FINLAND");
        airRegMap.put("010001101", "GREECE");
        airRegMap.put("010001110", "HUNGARY");
        airRegMap.put("010001111", "NORWAY");
        airRegMap.put("010010000", "NETHERLANDS, KINGDOM OF THE");
        airRegMap.put("010010001", "POLAND");
        airRegMap.put("010010010", "PORTUGAL");
        airRegMap.put("010010011", "CZECH RE");
        airRegMap.put("010010100", "ROMANIA");
        airRegMap.put("010010101", "SWEDEN");
        airRegMap.put("010010110", "SWITZERLAND");
        airRegMap.put("010010111", "TURKEY");
        airRegMap.put("010100001", "UKRAINE");
        airRegMap.put("011100010", "SAUDI ARABIA");
        airRegMap.put("011100011", "RE OF KOREA");
        airRegMap.put("011100100", "DEMOCRATIC PEOPLE'S RE OF KOREA");
        airRegMap.put("011100101", "IRAQ");
        airRegMap.put("011100110", "IRAN");
        airRegMap.put("011100111", "ISRAEL");
        airRegMap.put("011101000", "JORDAN");
        airRegMap.put("011101001", "LEBANON");
        airRegMap.put("011101010", "MALAYSIA");
        airRegMap.put("011101011", "PHILIPPINES");
        airRegMap.put("011101100", "PAKISTAN");
        airRegMap.put("011101101", "SINGAPORE");
        airRegMap.put("011101110", "SRI LANKA");
        airRegMap.put("011101111", "SYRIAN ARAB RE");
        airRegMap.put("100010000", "THAILAND");
        airRegMap.put("100010001", "VIETNAM");
        airRegMap.put("100010100", "INDONESIA");
        airRegMap.put("110010000", "NEW ZEALAND");
        airRegMap.put("001100", "ITALY");
        airRegMap.put("001101", "SPAIN");
        airRegMap.put("001110", "FRANCE");
        airRegMap.put("001111", "GERMANY");
        airRegMap.put("010000", "UNITED KINGDOM");
        airRegMap.put("011110", "CHINA");
        airRegMap.put("011111", "AUSTRALIA");
        airRegMap.put("100000", "INDIA");
        airRegMap.put("100001", "JAPAN");
        airRegMap.put("110000", "CANADA");
        airRegMap.put("111000", "ARGENTINA");
        airRegMap.put("111001", "BRAZIL");
        airRegMap.put("0001", "RUSSIAN FEDERATION");
        airRegMap.put("1010", "UNITED STATES");

        aircraftMap = new HashMap<>();
        aircraftMap.put("0", "A");
        aircraftMap.put("1", "B");
        aircraftMap.put("2", "C");
        aircraftMap.put("3", "D");
        aircraftMap.put("4", "E");
        aircraftMap.put("5", "F");
        aircraftMap.put("6", "G");
        aircraftMap.put("7", "H");
        aircraftMap.put("8", "I");
        aircraftMap.put("9", "J");
        aircraftMap.put("10", "K");
        aircraftMap.put("11", "L");
        aircraftMap.put("12", "M");
        aircraftMap.put("13", "N");
        aircraftMap.put("14", "O");
        aircraftMap.put("15", "P");
        aircraftMap.put("16", "Q");
        aircraftMap.put("17", "R");
        aircraftMap.put("18", "S");
        aircraftMap.put("19", "T");
        aircraftMap.put("20", "U");
        aircraftMap.put("21", "V");
        aircraftMap.put("22", "W");
        aircraftMap.put("23", "X");
        aircraftMap.put("24", "Y");
        aircraftMap.put("25", "Z");
        aircraftMap.put("26", "0");
        aircraftMap.put("27", "1");
        aircraftMap.put("28", "2");
        aircraftMap.put("29", "3");
        aircraftMap.put("30", "4");
        aircraftMap.put("31", "5");
        aircraftMap.put("32", "6");
        aircraftMap.put("33", "7");
        aircraftMap.put("34", "8");
        aircraftMap.put("35", "9");

        mbaudotToAsciiMap = new HashMap<>();
        mbaudotToAsciiMap.put("56", "A");
        mbaudotToAsciiMap.put("51", "B");
        mbaudotToAsciiMap.put("46", "C");
        mbaudotToAsciiMap.put("50", "D");
        mbaudotToAsciiMap.put("48", "E");
        mbaudotToAsciiMap.put("54", "F");
        mbaudotToAsciiMap.put("43", "G");
        mbaudotToAsciiMap.put("37", "H");
        mbaudotToAsciiMap.put("44", "I");
        mbaudotToAsciiMap.put("58", "J");
        mbaudotToAsciiMap.put("62", "K");
        mbaudotToAsciiMap.put("41", "L");
        mbaudotToAsciiMap.put("39", "M");
        mbaudotToAsciiMap.put("38", "N");
        mbaudotToAsciiMap.put("35", "O");
        mbaudotToAsciiMap.put("45", "P");
        mbaudotToAsciiMap.put("61", "Q");
        mbaudotToAsciiMap.put("42", "R");
        mbaudotToAsciiMap.put("52", "S");
        mbaudotToAsciiMap.put("33", "T");
        mbaudotToAsciiMap.put("60", "U");
        mbaudotToAsciiMap.put("47", "V");
        mbaudotToAsciiMap.put("57", "W");
        mbaudotToAsciiMap.put("55", "X");
        mbaudotToAsciiMap.put("53", "Y");
        mbaudotToAsciiMap.put("49", "Z");
        mbaudotToAsciiMap.put("36", " ");
        mbaudotToAsciiMap.put("24", "-");
        mbaudotToAsciiMap.put("23", "/");
        mbaudotToAsciiMap.put("13", "0");
        mbaudotToAsciiMap.put("29", "1");
        mbaudotToAsciiMap.put("25", "2");
        mbaudotToAsciiMap.put("16", "3");
        mbaudotToAsciiMap.put("10", "4");
        mbaudotToAsciiMap.put("1", "5");
        mbaudotToAsciiMap.put("21", "6");
        mbaudotToAsciiMap.put("28", "7");
        mbaudotToAsciiMap.put("12", "8");
        mbaudotToAsciiMap.put("3", "9");

        /* Make asciiToMbaudotMap from hexMap */
        asciiToMbaudotMap = new HashMap<>();
        Iterator<Entry<String, String>> iter2 = mbaudotToAsciiMap.entrySet().iterator();
        while (iter2.hasNext()) {
            Map.Entry<String, String> e = iter2.next();
            asciiToMbaudotMap.put(e.getValue(), e.getKey());
        }

        octMap = new HashMap<>();
        octMap.put("000", "0");
        octMap.put("001", "1");
        octMap.put("010", "2");
        octMap.put("011", "3");
        octMap.put("100", "4");
        octMap.put("101", "5");
        octMap.put("110", "6");
        octMap.put("111", "7");

    }

    /*
     * ------------------------------------------------------------ * Let the code
     * begin * ------------------------------------------------------------ *
     */

    static int binaryToDecimal(String binCode) {
        int result = 0;
        int len = binCode.length();
        int value = 0;
        for (int i = 0; i < len; i++) {
            char bit = binCode.charAt(i);
            if (bit == '1') {
                int exp = len - 1 - i;
                value = 1;
                for (int y = 0; y < exp; y++) {
                    value = value * 2;
                }
                result += value;
            }
        }
        return result;
    }

    static String decimalToBinary(int dec) {
        String result = "";
        String temp = "";

        while (dec > 1) {
            temp += dec % 2;
            dec = dec / 2;
        }
        if (dec != 0)
            temp += dec;

        // Reverse the string
        for (int i = temp.length() - 1; i >= 0; i--)
            result += temp.charAt(i);

        return result;
    }

    static String binaryToOctal(String binCode) {
        String octStr = "";
        int len = binCode.length();
        for (int i = 0; i < len; i = i + 3) {
            String bits = binCode.substring(i, i + 3);
            String t = (String) octMap.get(bits);
            if (t != null) {
                octStr = octStr + t;
            }
        }
        return octStr;
    }

    static String binaryToHex(String binCode) {
        String hexStr = "";

        // Check for NULL values
        if (binCode == null) {
            binCode = "";
        }

        // obtain the length of the Binary Code
        int len = binCode.length();

        if (len > 0) {
            // Start decoding
            for (int i = 0; i < len; i = i + 4) {
                String bits = binCode.substring(i, i + 4);
                String t = (String) hexMap.get(bits);
                if (t != null) {
                    hexStr = hexStr + t;
                }
            }
        }

        return hexStr;
    }

    static String getAircraftCountryOfReg(String binCode) {
        // The binCode in this case is the 24-bit Aircraft Address.
        // In the above hashMap there are values of length 14,12,9,6,4
        // We will take it in turn.
        String code = binCode.substring(0, 14 - 1);
        String r = (String) airRegMap.get(code);
        if (r != null) {
            return r;
        } else {
            code = binCode.substring(0, 12 - 1);
            r = (String) airRegMap.get(code);
            if (r != null) {
                return r;
            } else {
                code = binCode.substring(0, 9 - 1);
                r = (String) airRegMap.get(code);
                if (r != null) {
                    return r;
                } else {
                    code = binCode.substring(0, 6 - 1);
                    r = (String) airRegMap.get(code);
                    if (r != null) {
                        return r;
                    } else {
                        code = binCode.substring(0, 4 - 1);
                        r = (String) airRegMap.get(code);
                        if (r != null) {
                            return r;
                        }
                    }
                }
            }
        }

        return "";
    }

    // NOTE: this will only work for Aust. reg'd aircraft.
    static String binaryToAircraftCallsign(String binCode) {
        int origDec = Conversions.binaryToDecimal(binCode);

        // 15 July 2005
        // This should work properly!
        int dec = origDec;
        int L1 = dec / 1296;
        int remainder = dec % 1296;
        int L2 = remainder / 36;
        int L3 = remainder % 36;

        // System.out.println("L1=" + L1);
        // System.out.println("L2=" + L2);
        // System.out.println("L3=" + L3);

        /*
         * // 14 July 2005 // This doesn't work properly for boundary cases! double dec
         * = origDec; int L1 = (int)(dec/1296);
         * 
         * //System.out.println("L1=" + L1);
         * 
         * dec = dec/1296.0 - (int)(dec/1296); int L2 = (int)(dec*36.0+0.000005);
         * 
         * //System.out.println("L2=" + L2);
         * 
         * dec = dec*36.0 - (int)(dec*36+0.000005); // Add 0.000005 is for adjusting the
         * loss precision of double value int L3 = (int)(dec*36.0+0.000005);
         * 
         * //System.out.println("L3=" + L3);
         */

        String result = "VH-";
        result += Conversions.getAircraftCallsign(L1);
        result += Conversions.getAircraftCallsign(L2);
        result += Conversions.getAircraftCallsign(L3);

        return result;
    }

    private static String getAircraftCallsign(int value) {
        try {

            String S1 = (String) aircraftMap.get(value + "");

            if (S1 != null) {
                return S1;
            } else {
                return "?";
            }
        } catch (Exception e) {
            return "?";
        }
    }

    static String hexToBinary(String hexStr) {
        int len = hexStr.length();
        hexStr = hexStr.toUpperCase();
        try {
            String r = "";
            for (int i = 0; i < len; i++) {
                String letter = hexStr.substring(i, i + 1);
                String t = (String) binMap.get(letter);

                if (t != null) {
                    r = r + t;
                }
            }

            // 16 May 2005
            // r = "0000000000000000000000000" + r;

            if (len == 15) {
                // right shift, assume short message
                // return "0" + r;
                // 16 May 2005, Can NOT assume short message! CDP's Guidance
                return "0000000000000000000000000" + "?" + r;
            } else {
                return "0000000000000000000000000" + r;
            }
        } catch (Exception e) {
            return "Unable to convert to binary. Exception caught: " + e.getMessage();
        }
    }

    // 11 May 2005, return String[] instead of String
    static String[] mBaudotBits2mBaudotStr(String protocolName, String mbaudotBits, int bits) {
        String vE[] = new String[3];

        // HashTable e = new HashTable();
        List<String> e = new ArrayList<>();
        String errStr = "";

        String mbaudotStr = "";
        int n = mbaudotBits.length() / bits;

        // System.out.println ("In Conversions.java, n="+n);

        int spaceCount = 0;

        for (int j = 0; j < n; j++) {
            String mbaudotSubBits = mbaudotBits.substring(j * bits, (j * bits) + bits);
            int d = 0;

            if (bits == 5) {
                mbaudotSubBits = "1" + mbaudotSubBits;
                d = binaryToDecimal(mbaudotSubBits);
            } else
                d = binaryToDecimal(mbaudotSubBits);

            String s;
            Object obj = mbaudotToAsciiMap.get(d + "");

            if (obj != null) {
                s = (String) obj;

                if (s.equals(" ")) {
                    if (protocolName.equalsIgnoreCase("Radio Call Sign")) {
                        // 13 May 2005
                        // mbaudotStr = mbaudotStr + "?";

                        mbaudotStr = mbaudotStr + "?";
                        if (spaceCount == j && n != 1) { // n==1 && UserMaritime (or User
                                                         // RadioCallsign) means it is for Specidic
                                                         // Beacon Number (bit 76-81)
                            e.add(mbaudotSubBits + " = Space - Left Justified\n");
                            spaceCount++;
                        } else
                            e.add(mbaudotSubBits + " = Space - Non-Spec\n");
                    } else if (protocolName.equalsIgnoreCase("Aviation")
                            || protocolName.equalsIgnoreCase("Maritime")
                            || protocolName.equalsIgnoreCase("Orbitography")) {
                        // n==1 && UserMaritime (or User RadioCallsign) means it is for Specidic
                        // Beacon Number (bit 76-81)
                        if (n == 1) {
                            mbaudotStr = mbaudotStr + "?";
                            e.add(mbaudotSubBits + " = Space - Non-Spec\n");
                        } else {
                            mbaudotStr = mbaudotStr + "*";
                            e.add(mbaudotSubBits + " = Space - Right Justified\n");
                        }
                    } else {
                        mbaudotStr = mbaudotStr + s;
                        e.add(mbaudotSubBits + " = Spec\n");
                    }
                } else if (s.equals("-")) {
                    // n==1 && UserMaritime (or User RadioCallsign) means it is for Specific Beacon
                    // Number (bit 76-81)
                    if (n == 1 && (protocolName.equalsIgnoreCase("Maritime")
                            || protocolName.equalsIgnoreCase("Radio Call Sign"))) {
                        mbaudotStr = mbaudotStr + "?";
                        e.add(mbaudotSubBits + " = Hyphen(-) - Non-Spec\n");
                    }
                    // 04 Jan 2007
                    else {
                        mbaudotStr = mbaudotStr + s;
                        e.add(mbaudotSubBits + " = Spec\n");
                    }
                } else {
                    mbaudotStr = mbaudotStr + s;
                    e.add(mbaudotSubBits + " = Spec\n");
                }
            } else {
                mbaudotStr = mbaudotStr + "?";
                e.add(mbaudotSubBits + " = Non-Spec\n");
            }

        } // end of for loop

        // Deal with SPACE if the protocol is not User Radion Call Sign, these protocols
        // are: UserAviation, UserMaritime and UserSerialAircraftOperator
        int len = mbaudotStr.length();
        String temp = "";
        for (int i = 0; i < len; i++) {
            temp += "*";
        }

        int countNum = 1;
        // 02/Nov/2005
        // If mbaudotStr is all "*", then they are all spaces, which is allowed in the
        // Orbitography User protocol
        if (!mbaudotStr.equalsIgnoreCase(temp)) {
            int index = mbaudotStr.length() - 1;
            for (; index >= 0; index--) {
                if (mbaudotStr.charAt(index) == '*')
                    continue;
                else
                    break;
            }
            for (int i = 0; i < index; i++) {
                if (mbaudotStr.charAt(i) == '*')
                    mbaudotStr = mbaudotStr.substring(0, i) + " " + mbaudotStr.substring(i + 1);
            }

            for (int i = 0; i < mbaudotStr.length(); i++) {
                if (mbaudotStr.charAt(i) == '*') {
                    mbaudotStr = mbaudotStr.substring(0, i) + "?" + mbaudotStr.substring(i + 1);
                }
            }

            int num = 0;
            while (num < mbaudotStr.length()) {
                // if (mbaudotStr.charAt(num) == '*' || mbaudotStr.charAt(num) == '?')
                if (mbaudotStr.charAt(num) == '?') {
                    errStr += "?" + countNum++ + " = " + (String) e.get(num);
                }

                num++;
            }
        } else {
            for (int i = 0; i < mbaudotStr.length(); i++) {
                if (mbaudotStr.charAt(i) == '*')
                    mbaudotStr = mbaudotStr.substring(0, i) + " " + mbaudotStr.substring(i + 1);
            }
        }

        vE[0] = mbaudotStr;
        vE[1] = errStr;
        vE[2] = (countNum - 1) + "";

        return vE;
    }

    static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= '0' && c <= '9') {
                // do nothing
            } else {
                return false;
            }
        }

        return true;
    }

    static String zeroPadFromLeft(String str, int amt) {
        int lenStr = str.length();
        for (int i = 0; i < (amt - lenStr); i++) {
            str = "0" + str;
        }
        return str;
    }

}
