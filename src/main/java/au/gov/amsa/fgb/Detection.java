package au.gov.amsa.fgb;

import com.github.davidmoten.guavamini.Preconditions;

import au.gov.amsa.fgb.internal.Decoder;

public final class Detection {
    
    private Detection() {
        // prevent instantiation
    }
    
    public static String decodeHexToJson(String hex) {
        Preconditions.checkNotNull(hex);
        Preconditions.checkArgument(hex.length() == 30, "hex must be 30 characters");
        return Decoder.decodeFullAsJson(hex);
    }

}
