package com.amsa.fgb;

import java.util.List;

public class DecodeAsText implements DecodeFilter {

    public static final DecodeAsText INSTANCE = new DecodeAsText();
    
    private DecodeAsText() {
        // force use as singleton
    }
    
    @Override
    public String getData(List<HexAttribute> v) {
	String data = "";
	// Get the body of the decode.
	for (int i=0; i<v.size(); i++) {
	    HexAttribute h = v.get(i);
	    String line = "";
	    String desc = h.getDesc();
	    String pos  = h.getPos();
	    String val  = h.getValue();
	    int len = desc.length();
	    if (len > 0 ) {
		line = desc;
		for (int j=0; j<(30 - len); j++) {
		    line = line + " ";
		}
		len = pos.length();
		for (int j=0; j<(13 - len); j++) {
		    pos = pos + " ";
		}
		line = line + pos + val + "\n";;
	    }
	    data = data + line;
	}

	// Get any errors that we might have encountered
	for (int i=0; i<v.size(); i++) {
	    HexAttribute h = (HexAttribute) v.get(i);
	    String line = h.getError();
	    if (h.error.length() > 0 ) {
		data = data + line + "\n";
	    }
	}

	return data;
    }

}
