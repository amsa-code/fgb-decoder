package au.gov.amsa.fgb.internal;

import org.junit.Test;

import com.github.davidmoten.junit.Asserts;

import au.gov.amsa.fgb.internal.Util;

public class UtilTest {

    @Test
    public void isUtilityClass() {
        Asserts.assertIsUtilityClass(Util.class);
    }

}
