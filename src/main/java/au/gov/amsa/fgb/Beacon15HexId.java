package au.gov.amsa.fgb;

import com.github.davidmoten.guavamini.Preconditions;

import au.gov.amsa.fgb.internal.Decoder;

public final class Beacon15HexId {

    private Beacon15HexId() {
        // prevent instantiation
    }
    
    public static String decodeHexToJson(String hex) {
        Preconditions.checkNotNull(hex);
        Preconditions.checkArgument(hex.length() == 15, "hex must be 15 characters");
        return Decoder.decodeFullAsJson(hex);
    }
}
