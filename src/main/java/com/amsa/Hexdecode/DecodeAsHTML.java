package com.amsa.Hexdecode;

import java.util.List;

public class DecodeAsHTML implements DecodeFilter {
    
    public static final DecodeAsHTML INSTANCE = new DecodeAsHTML();
    
    private DecodeAsHTML() {
        // force use of singleton
    }

    @Override
    public String getData(List<HexAttribute> v) {
        String data = "<html>\n";
        data = data + "<head>\n";
        data = data + "<title>HEXDECODE of 406 Beacon</title>\n";
        data = data + "</head>\n";
        data = data + "<body>\n";
        data = data + "<table>\n";
        data = data + "<TR ALIGN=LEFT><TH>Item</TH><TH>Bits</TH><TH>Value</TH></TR>\n";

	if (v.size() > 0)
	{
	    // Get the body of the decode.
	    for (int i=0; i<v.size(); i++) {
		HexAttribute h = v.get(i);
		String line = "";
		String desc = h.getDesc();
		String pos  = h.getPos();
		String val  = h.getValue();
		String d = "";
		for (int j=0; j<val.length(); j++) {
		    char c = val.charAt(j);
		    String str = c + "";
		    if (str.equals("\n")) {
			d += "<BR>";
		    } else {
			d += val.charAt(j);
		    }
		}
		int len = desc.length();
		line = "<TR><TD>" + desc + "</TD>";
		line = line + "<TD>" + pos + "</TD>";
		line = line + "<TD>" + d + "</TD></TR>\n";
		data = data + line;
	    }
	    data = data + "</table>\n";


	    data = data + "<table>\n";
	    // Get any errors that we might have encountered
	    for (int i=0; i<v.size(); i++) {
		HexAttribute h = (HexAttribute) v.get(i);
		String err = h.getError();
		String errMsg = "";
		for (int j=0; j<err.length(); j++) {
		    char c = err.charAt(j);
		    String str = c + "";
		    if (str.equals("\n")) {
			errMsg += "<BR>";
		    } else {
			errMsg += err.charAt(j);
		    }
		}
		if (errMsg.length() > 0 ) {
		    data = data + "<TR><TD>" + errMsg + "</TD></TR>\n";
		}
	    }
	    data = data + "</table>\n";

	}

	data = data + "</body>\n";
	data = data + "</html>\n";

	return data;
    }
}
