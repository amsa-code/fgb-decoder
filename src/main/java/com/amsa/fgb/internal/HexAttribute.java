package com.amsa.fgb.internal;

final class HexAttribute {

    private final AttributeType desc;
    private final int start;
    private final int finish;
    private final String value;
    private final String error;

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
        this.finish = 0;
    }

    HexAttribute(AttributeType d, String v, String e) {
        this.desc = d;
        this.value = v;
        this.error = e;
        this.start = 0;
        this.finish = 0;
    }

    AttributeType desc() {
        return desc;
    }

    int start() {
        return start;
    }

    int finish() {
        return finish;
    }

    String value() {
        return this.value;
    }

    String error() {
        return this.error;
    }

    public JsonType jsonType() {
        return desc.jsonType();
    }

}
