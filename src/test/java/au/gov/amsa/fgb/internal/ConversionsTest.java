package au.gov.amsa.fgb.internal;

import org.junit.Test;

import com.github.davidmoten.junit.Asserts;

import au.gov.amsa.fgb.internal.Conversions;

public class ConversionsTest {

    @Test
    public void isUtilityClass() {
        Asserts.assertIsUtilityClass(Conversions.class);
    }

}
