package au.gov.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

abstract class UserAviationOrMaritimeOrRadioCallsign extends User {

    private final Consumer consumer;

    UserAviationOrMaritimeOrRadioCallsign(String protocolName, String userProtocolCode,
            Consumer consumer) {
        super(protocolName, userProtocolCode);
        this.consumer = consumer;
    }

    interface Consumer {
        void accept(UserAviationOrMaritimeOrRadioCallsign u, String binCode,
                List<HexAttribute> result);
    }

    @Override
    List<HexAttribute> decode(String hexStr) {

        String binCode = Conversions.hexToBinary(hexStr);
        List<HexAttribute> result = new ArrayList<>();

        userFragment(this, hexStr, binCode, result);

        consumer.accept(this, binCode, result);

        result.add(this.auxRadioLocating(binCode, 84, 85));

        if (hexStr.length() > 15) {
            result.add(this.bch1(binCode, 86, 106));
            if (this.isLongMessage(binCode)) {
                result.add(this.encodedPositionSource(binCode, 111));
                if (this.defaultFFFFFFFF(hexStr)) {
                    result.add(this.longMessage(binCode, 113, 144));
                } else {
                    if (this.default00000000(hexStr)) {
                        result.add(this.longMessage(binCode, 113, 144));
                    } else {
                        List<HexAttribute> res = result;
                        this.latitude(binCode, 108, 119).ifPresent(x -> res.add(x));
                        this.longitude(binCode, 120, 132).ifPresent(x -> res.add(x));
                        result.add(this.bch2(binCode, 133, 144));
                    }
                    result.add(this.nationalUse(binCode, 113, 144));
                }
            } else {
                result = this.nonNationalUse(result, binCode);
            }
        }
        return result;
    }

    // This overidding method will be called by User.java
    @Override
    List<HexAttribute> allEmergencyCodes(List<HexAttribute> result, String binCode) {
        result = Common.maritimeEmergencyCodes(result, binCode);

        return result;
    }
}
