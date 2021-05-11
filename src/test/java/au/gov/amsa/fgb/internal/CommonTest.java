package au.gov.amsa.fgb.internal;

import org.junit.Test;

import com.github.davidmoten.junit.Asserts;

import au.gov.amsa.fgb.internal.Common;

public class CommonTest {

    @Test
    public void isUtilityClass() {
        Asserts.assertIsUtilityClass(Common.class);
    }

}
