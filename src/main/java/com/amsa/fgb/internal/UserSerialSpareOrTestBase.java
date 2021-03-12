package com.amsa.fgb.internal;

abstract class UserSpareOrTestBase extends UserBase {

    UserSpareOrTestBase(String protocolName, String userProtocolCode) {
        super(protocolName, userProtocolCode, (u, binCode, result) -> {
            // 25 May 2005
            // According to CDP, make it consistent with UserTest protocol
            result.add(u.nationalUse(binCode, 107, 132));
            result.add(u.bch2(binCode, 133, 144));
        });
    }
}
