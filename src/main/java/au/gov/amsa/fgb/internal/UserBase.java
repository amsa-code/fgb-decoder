package au.gov.amsa.fgb.internal;

import java.util.ArrayList;
import java.util.List;

abstract class UserBase extends User {

    private final Consumer consumer;

    UserBase(String protocolName, String userProtocolCode, Consumer consumer) {
        super(protocolName, userProtocolCode);
        this.consumer = consumer;
    }

    interface Consumer {
        void accept(UserBase u, String binCode, List<HexAttribute> result);
    }

    @Override
    List<HexAttribute> decode(String hexStr) {
        String binCode = Conversions.hexToBinary(hexStr);
        List<HexAttribute> result = new ArrayList<>();

        userFragment(this, hexStr, binCode, result);

        // 25 May 2005
        // CDP's latest guideline
        result.add(this.nationalUse(binCode, 40, 85));

        if (hexStr.length() > 15) {
            result.add(this.bch1(binCode, 86, 106));
            if (this.isLongMessage(binCode)) {
                consumer.accept(this, binCode, result);
            } else {
                result.add(this.nationalUse(binCode, 107, 112));
            }
        }
        return result;
    }

}
