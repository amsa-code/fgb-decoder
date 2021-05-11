package au.gov.amsa.fgb.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import au.gov.amsa.fgb.internal.HexAttribute;
import au.gov.amsa.fgb.internal.Unknown;

public class UnknownTest {

    @Test
    public void testUnknown() {
        Unknown u = new Unknown();
        assertTrue(u.canDecode("abc"));
        List<HexAttribute> list = u.decode("abc");
        assertEquals(1, list.size());
        assertEquals("Unknown Beacon Protocol", u.getName());
    }

}
