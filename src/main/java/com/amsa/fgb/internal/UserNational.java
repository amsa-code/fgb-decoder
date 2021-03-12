package com.amsa.fgb.internal;

class UserNational extends UserBase {
    UserNational() {
        super("National User", "100", (u, binCode, result) -> {
            // 25 May 2005
            // Based on CDP's latest guideline: PDF-2: bit107-132; BCH-2: bit133-144
            result.add(new HexAttribute(AttributeType.PDF_2, 107, 132, binCode.substring(113, 133), ""));
            result.add(new HexAttribute(AttributeType.BCH_2, 133, 144, binCode.substring(133, 145), ""));
        });
    }
}
