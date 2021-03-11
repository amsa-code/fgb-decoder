package com.amsa.fgb.internal;

 final class HexAttribute {

     final AttributeType desc;
     final int start;
     int finish;
     final String value;
     final String error;

     HexAttribute(AttributeType d, int s, int f, String v, String e) {
        this.desc = d;
        this.start = s;
        this.finish = f;
        this.value = v;
        this.error = e;
    }

     HexAttribute(AttributeType d, int s, int f, int v, String e) {
        this.desc = d;
        this.start = s;
        this.finish = f;
        this.value = v + "";
        this.error = e;
    }

     HexAttribute(AttributeType d, int s, String v, String e) {
        this.desc = d;
        this.start = s;
        this.value = v;
        this.error = e;
    }

     HexAttribute(AttributeType d, String v, String e) {
        this.desc = d;
        this.value = v;
        this.error = e;
        this.start = 0;
    }

     String getDesc() {
        return this.desc.toString();
    }

     String getPos() {
        String pos = "";
        if (this.start > 0) {
            pos = "b" + this.start;
            if (this.finish > 0) {
                pos += "-" + this.finish;
            }
        }
        return pos;
    }

     String getValue() {
        return this.value;
    }

     String getError() {
        return this.error;
    }

    public JsonType getJsonType() {
        return desc.jsonType();
    }

}
