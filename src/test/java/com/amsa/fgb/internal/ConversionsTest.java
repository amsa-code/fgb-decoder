package com.amsa.fgb.internal;

import org.junit.Test;

import com.github.davidmoten.junit.Asserts;

public class ConversionsTest {

    @Test
    public void isUtilityClass() {
        Asserts.assertIsUtilityClass(Conversions.class);
    }

}
