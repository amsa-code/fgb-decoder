package com.amsa.fgb;

public class HexAttribute {

    public String desc;
    public int start;
    public int finish;
    public String value;
    public String error;

    public HexAttribute(String d, int s, int f, String v, String e) {
	this.desc = d;
	this.start = s;
	this.finish = f;
	this.value = v;
	this.error = e;
    }

    public HexAttribute(String d, int s, int f, int v, String e) {
	this.desc = d;
	this.start = s;
	this.finish = f;
	this.value = v + "";
	this.error = e;
    }

    public HexAttribute(String d, int s, String v, String e) {
	this.desc = d;
	this.start = s;
	this.value = v;
	this.error = e;
    }

    public HexAttribute(String d, String v, String e) {
	this.desc = d;
	this.value = v;
	this.error = e;
    }

    public HexAttribute(String d, int s, int v, String e) {
	this.desc = d;
	this.start = s;
	this.value = v + "";
	this.error = e;
    }

    public String getDesc() {
	return this.desc;
    }

    public String getPos() {
	String pos = "";
	if (this.start > 0) {
	    pos = "b" + this.start;
	    if (this.finish > 0) {
		pos += "-" + this.finish;
	    }
	}
	return pos;
    }

    public String getValue() {
	return this.value;
    }

    public String getError() {
	return this.error;
    }

    public void setError(String e)
    {
	this.error = e;
    }
}


